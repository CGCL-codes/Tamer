package ast.gui.quotesrc;

import ast.DocumentController;
import ast.common.data.Image;
import ast.common.data.Source;
import ast.common.error.ASTError;
import ast.common.error.ConfigHandlerError;
import ast.common.error.DBError;
import ast.common.util.PathUtils;
import ast.gui.DialogController;
import com.sun.star.awt.ActionEvent;
import com.sun.star.awt.XActionListener;
import com.sun.star.awt.XDialog;
import com.sun.star.lang.EventObject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Handler for new and edit image dialog.
 *
 * @author feh
 * @author Chrissyx
 */
public class ImageDialogHandler implements XActionListener {

    /**
     * Reference to the {@link ast.DocumentController}.
     */
    private DocumentController docController;

    /**
     * Dialog file to handle its events.
     */
    private XDialog xDialog;

    /**
     * Shortinfo of the source which will be edited.
     */
    private String shortinfo;

    /**
     * Default constructor.
     *
     * @param aDocController Reference to the {@link ast.DocumentController}
     * @param xDialog Dialog file being worked with
     * @param sShortinfo Brief description of image to edit or null
     * @throws ConfigHandlerError If needed property retrieval for SourceCon failed
     * @throws DBError If database init of SourceCon failed
     */
    public ImageDialogHandler(final DocumentController aDocController, final XDialog xDialog, final String sShortinfo) throws ConfigHandlerError, DBError {
        this.docController = aDocController;
        this.xDialog = xDialog;
        this.shortinfo = sShortinfo;
        if (this.shortinfo != null) {
            final Image image = this.docController.getSourceController().getImageByShortinfo(this.shortinfo);
            this.fillTXTFields(image);
        }
        this.xDialog.setTitle(this.docController.getLanguageController().__("Imagesource"));
        this.docController.getDialogUtils().registerButtons(new String[][] { new String[] { "FilePicker", this.docController.getLanguageController().__("...") }, new String[] { "Import", this.docController.getLanguageController().__("Import") }, this.docController.getLanguageController().captionOK, this.docController.getLanguageController().captionCancel }, this.xDialog, this);
        this.docController.getDialogUtils().setTextForLBLs(new String[][] { this.docController.getSourceController().captionTitle, this.docController.getSourceController().captionShortinfo, this.docController.getSourceController().captionAuthor, this.docController.getSourceController().captionPublisher, this.docController.getSourceController().captionCityOfPublisher, this.docController.getSourceController().captionEdition, this.docController.getSourceController().captionYearOfPublication, this.docController.getSourceController().captionFilepath }, this.xDialog);
        this.docController.getLogger().fine("ImageDialogHandler started");
    }

    /**
     * {@inheritDoc}
     *
     * @see com.sun.star.awt.XActionListener#actionPerformed(com.sun.star.awt.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent aActionEvent) {
        try {
            final String sName = this.docController.getDialogUtils().getAction(aActionEvent);
            if (sName.equals("cmdOK")) {
                final String sInfo = this.docController.getDialogUtils().getTextFromTXT(this.xDialog, "Shortinfo");
                if (!sInfo.isEmpty()) {
                    try {
                        final Source src = this.docController.getSourceController().getSourceByShortinfo(sInfo);
                        if (src != null && !src.getShortinfo().equalsIgnoreCase(this.shortinfo)) DocumentController.getStaticMessageBox().showWarningBox(this.docController.getLanguageController().__("Image"), this.docController.getLanguageController().__("The brief description is already used by another source.")); else {
                            final Image newImage = this.generateImage();
                            if (this.shortinfo == null) {
                                this.docController.getSourceController().addNewSource(newImage);
                                DocumentController.getStaticMessageBox().showInfoBox(this.docController.getLanguageController().__("New image"), this.docController.getLanguageController().__("Image \"{0}\" added.", newImage.getShortinfo()));
                            } else {
                                this.docController.getSourceController().updateSource(this.shortinfo, newImage);
                                DocumentController.getStaticMessageBox().showInfoBox(this.docController.getLanguageController().__("Edit image"), this.docController.getLanguageController().__("Image \"{0}\" edited.", newImage.getShortinfo()));
                            }
                            this.xDialog.endExecute();
                        }
                    } catch (final ASTError e) {
                        e.severe();
                    }
                } else DocumentController.getStaticMessageBox().showWarningBox(this.docController.getLanguageController().__("Image"), this.docController.getLanguageController().__("Please enter a brief description first."));
            } else if (sName.equals("cmdFilePicker")) {
                try {
                    this.docController.getDialogUtils().setTextForTXT(this.xDialog, "Filepath", this.docController.getPathUtils().getSystemPathFromFileURL(this.docController.getDialogUtils().chooseFileDialog(this.docController.getPathUtils().getPath(PathUtils.WORK), false, new String[][] { new String[] { this.docController.getLanguageController().__("All images (*.bmp; *.gif; *.jpg; *.jpeg; *.png)"), "*.bmp; *.gif; *.jpg; *.jpeg; *.png" }, new String[] { this.docController.getLanguageController().__("BMP images (*.bmp)"), "*.bmp" }, new String[] { this.docController.getLanguageController().__("GIF images (*.gif)"), "*.gif" }, new String[] { this.docController.getLanguageController().__("JPG images (*.jpg, *.jpeg)"), "*.jpg; *.jpeg" }, new String[] { this.docController.getLanguageController().__("PNG images (*.png)"), "*.png" } }, 0)[0]), false);
                } catch (final ASTError e) {
                    e.severe();
                }
            } else if (sName.equals("cmdImport")) {
                try {
                    final Image newImage = this.generateImage();
                    File bibTexFile = new File(this.docController.getPathUtils().getSystemPathFromFileURL(this.docController.getDialogUtils().chooseFileDialog(this.docController.getPathUtils().getPath(PathUtils.WORK), false, new String[][] { new String[] { this.docController.getLanguageController().__("BibTeX file (*.bib)"), "*.bib" } }, 0)[0]));
                    if (this.docController.getSourceController().importUtils.getEntryCountFromBibTex(bibTexFile) == 1) this.docController.getSourceController().importUtils.getSourceDataFromBibFile(bibTexFile, newImage); else if (this.docController.getSourceController().importUtils.getEntryCountFromBibTex(bibTexFile) >= 1) {
                        final DialogController dialogController = this.docController.getDialogController();
                        new ChooseBibTexEntryDialogHandler(this.docController, dialogController.loadDialog("dlgChooseBibTexEntry.xdl"), newImage, bibTexFile);
                        dialogController.showDialog();
                    } else DocumentController.getStaticMessageBox().showWarningBox(this.docController.getLanguageController().__("BibTeX import"), this.docController.getLanguageController().__("File contains no readable BibTeX entries."));
                    this.fillTXTFields(newImage);
                } catch (final ASTError e) {
                    e.severe();
                }
            } else this.xDialog.endExecute();
        } catch (final com.sun.star.uno.Exception e) {
            this.docController.getLogger().severe("Can't handle event! " + e.getMessage());
        }
    }

    /**
     * Fill the TXT fields with the data source.
     *
     * @param aImage
     */
    private void fillTXTFields(final Image aImage) {
        this.docController.getDialogUtils().setTextForTXT(this.xDialog, "Title", aImage.getTitle(), false);
        this.docController.getDialogUtils().setTextForTXT(this.xDialog, "Shortinfo", aImage.getShortinfo(), false);
        for (final String curAuthor : aImage.getAuthor()) if (!curAuthor.isEmpty()) this.docController.getDialogUtils().setTextForTXT(this.xDialog, "Author", curAuthor + ";", true);
        this.docController.getDialogUtils().setTextForTXT(this.xDialog, "Publisher", aImage.getPublisher(), false);
        this.docController.getDialogUtils().setTextForTXT(this.xDialog, "CityOfPublisher", aImage.getCityOfPublisher(), false);
        this.docController.getDialogUtils().setTextForTXT(this.xDialog, "Edition", aImage.getEdition(), false);
        this.docController.getDialogUtils().setValueForNUM(this.xDialog, "YearOfPublication", Double.valueOf(aImage.getYearOfPublication()));
        this.docController.getDialogUtils().setTextForTXT(this.xDialog, "Filepath", aImage.getFile().getPath(), false);
    }

    /**
     * Generates an image from the text elements.
     *
     * @return New generated image
     */
    private Image generateImage() {
        final List<String> author = new ArrayList<String>();
        for (final StringTokenizer authors = new StringTokenizer(this.docController.getDialogUtils().getTextFromTXT(this.xDialog, "Author"), ";"); authors.hasMoreTokens(); ) author.add(authors.nextToken());
        return new Image(0, author, this.docController.getDialogUtils().getTextFromTXT(this.xDialog, "CityOfPublisher"), this.docController.getDialogUtils().getTextFromTXT(this.xDialog, "Edition"), new ArrayList<String>(), "", "", this.docController.getDialogUtils().getTextFromTXT(this.xDialog, "Publisher"), this.docController.getDialogUtils().getTextFromTXT(this.xDialog, "Shortinfo"), this.docController.getDialogUtils().getTextFromTXT(this.xDialog, "Title"), "", "", "", ((Double) this.docController.getDialogUtils().getValueFromNUM(this.xDialog, "YearOfPublication")).intValue(), new File(this.docController.getDialogUtils().getTextFromTXT(this.xDialog, "Filepath")), 0, 0, null);
    }

    /**
     * {@inheritDoc}
     *
     * @see com.sun.star.awt.XActionListener#disposing(com.sun.star.lang.EventObject)
     */
    @Override
    public void disposing(final EventObject aEventObject) {
        this.xDialog = null;
    }
}
