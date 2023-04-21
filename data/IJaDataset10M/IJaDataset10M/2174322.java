package edu.udo.scaffoldhunter.view.util;

import static edu.udo.scaffoldhunter.util.I18n._;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.basic.BasicFileChooserUI;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;
import edu.udo.scaffoldhunter.gui.util.FileChooser;
import edu.udo.scaffoldhunter.gui.util.StandardButtonFactory;
import edu.udo.scaffoldhunter.util.FileType;
import edu.udo.scaffoldhunter.util.Resources;

/**
 * A file dialog to export a graph to an image.
 * 
 * @author Anke Arndt, Arbia Ben Ahmed, Michael Rex, Vanessa Bembenek
 * @author Henning Garus
 */
public class ExportDialog extends JDialog {

    private int returnValue;

    private Dimension screenDimension;

    private Dimension allDimension;

    private Dimension currentDimension;

    private JFileChooser fileChooser;

    private JSpinner widthSpinner;

    private JSpinner heightSpinner;

    private JToggleButton exportAllToggle;

    private Action saveAction;

    private Action cancelAction;

    private Action lockToggleAction;

    private boolean locked;

    private FileType fileType;

    private File file;

    private Dimension dimension;

    private boolean exportAll = true;

    /**
     * Create a new export dialog with the given parent.
     * @param parent 
     *            the parent window
     * @param screenDimension 
     *            the dimensions of the current viewPort
     * @param allDimension 
     *            the dimensions of the whole tree
     */
    public ExportDialog(Window parent, Dimension screenDimension, Dimension allDimension) {
        super(parent, _("Export.Title"), ModalityType.APPLICATION_MODAL);
        this.screenDimension = screenDimension;
        this.allDimension = allDimension;
        this.currentDimension = allDimension;
        returnValue = JFileChooser.CANCEL_OPTION;
        locked = true;
        initActions();
        initGUI();
        setLocationRelativeTo(parent);
    }

    /**
     * Displays the export dialog and returns whether the user approved or
     * canceled the export operation.
     * 
     * @return <code>JFileChooser.APPROVE_OPTION</code> or
     *         <code>JFileChooser.CANCEL_OPTION</code>
     */
    public int showExportDialog() {
        setVisible(true);
        return returnValue;
    }

    private void initActions() {
        saveAction = new SaveAction();
        cancelAction = new CancelAction();
        lockToggleAction = new LockToggleAction();
    }

    private void initGUI() {
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel chooserPanel = new JPanel(new BorderLayout());
        fileChooser = new FileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(FileType.PNG.getFileFilter());
        fileChooser.addChoosableFileFilter(FileType.TIFF.getFileFilter());
        fileChooser.addChoosableFileFilter(FileType.SVG.getFileFilter());
        fileChooser.setControlButtonsAreShown(false);
        FormLayout sizeLayout = new FormLayout("5dlu, p, 5dlu, p, 5dlu, p, 2dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 2dlu, p, 5dlu", "default, 5dlu, d");
        PanelBuilder builder = new PanelBuilder(sizeLayout);
        builder.addLabel(_("Export.ImageSize") + ":", CC.xy(2, 1));
        builder.addLabel(_("Export.ImageWidth") + ":", CC.xy(4, 1));
        widthSpinner = new JSpinner(new SpinnerNumberModel(allDimension.width, 1, Integer.MAX_VALUE, 1));
        widthSpinner.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (locked) {
                    double aspectRatio = currentDimension.getWidth() / currentDimension.height;
                    int height = (int) ((Integer) widthSpinner.getValue() / aspectRatio);
                    heightSpinner.setValue(height);
                }
            }
        });
        builder.add(widthSpinner, CC.xy(6, 1));
        builder.addLabel(_("Export.Pixel"), CC.xy(8, 1));
        JToggleButton lockToggle = new JToggleButton(lockToggleAction);
        lockToggle.setSelectedIcon(Resources.getIcon("lock.png"));
        lockToggle.setSelected(locked);
        builder.add(lockToggle, CC.xy(10, 1));
        builder.addLabel(_("Export.ImageHeight") + ":", CC.xy(12, 1));
        heightSpinner = new JSpinner(new SpinnerNumberModel(currentDimension.height, 1, Integer.MAX_VALUE, 1));
        heightSpinner.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (locked) {
                    double aspectRatio = (double) currentDimension.width / currentDimension.height;
                    Long width = Math.round((Integer) heightSpinner.getValue() * aspectRatio);
                    widthSpinner.setValue(width.intValue());
                }
            }
        });
        builder.add(heightSpinner, CC.xy(14, 1));
        builder.addLabel(_("Export.Pixel"), CC.xy(16, 1));
        ButtonGroup group = new ButtonGroup();
        exportAllToggle = new JRadioButton(_("Export.Graph"));
        JRadioButton exportCurrent = new JRadioButton(_("Export.View"));
        group.add(exportAllToggle);
        group.add(exportCurrent);
        exportAllToggle.setSelected(exportAll);
        ActionListener l = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                boolean newDim = locked && widthSpinner.getValue().equals(currentDimension.width);
                if (exportAllToggle.isSelected()) {
                    currentDimension = allDimension;
                } else {
                    currentDimension = screenDimension;
                }
                if (newDim) {
                    widthSpinner.setValue(currentDimension.width);
                }
            }
        };
        exportAllToggle.addActionListener(l);
        exportCurrent.addActionListener(l);
        builder.add(exportAllToggle, CC.xyw(4, 3, 5));
        builder.add(exportCurrent, CC.xyw(12, 3, 5));
        chooserPanel.add(fileChooser, BorderLayout.NORTH);
        chooserPanel.add(builder.getPanel(), BorderLayout.CENTER);
        add(chooserPanel, BorderLayout.NORTH);
        add(getButtonBar(), BorderLayout.SOUTH);
        pack();
    }

    private JPanel getButtonBar() {
        JButton saveExport = new JButton(saveAction);
        JButton cancelExport = StandardButtonFactory.createCancelButton(cancelAction);
        JPanel buttonpanel = ButtonBarFactory.buildOKCancelBar(saveExport, cancelExport);
        buttonpanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return buttonpanel;
    }

    /**
     * @return the fileType
     */
    public FileType getFileType() {
        return fileType;
    }

    /**
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * @return the dimension
     */
    public Dimension getDimension() {
        return dimension;
    }

    /**
     * @return the exportAll
     */
    public boolean isExportAll() {
        return exportAll;
    }

    private class LockToggleAction extends AbstractAction {

        public LockToggleAction() {
            super("");
            putValue(Action.SMALL_ICON, Resources.getIcon("unlock.png"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            locked = !locked;
            if (locked) {
                double aspectRatio = currentDimension.getHeight() / currentDimension.getWidth();
                heightSpinner.setValue((int) ((Integer) widthSpinner.getValue() * aspectRatio));
            }
        }
    }

    private class CancelAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            returnValue = JFileChooser.CANCEL_OPTION;
            setVisible(false);
        }
    }

    private class SaveAction extends AbstractAction {

        public SaveAction() {
            super(_("Button.Save"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            returnValue = JFileChooser.APPROVE_OPTION;
            if (fileChooser.getUI() instanceof BasicFileChooserUI) {
                BasicFileChooserUI ui = (BasicFileChooserUI) fileChooser.getUI();
                ui.getApproveSelectionAction().actionPerformed(null);
            }
            file = fileChooser.getSelectedFile();
            FileFilter ff = fileChooser.getFileFilter();
            fileType = FileType.getFileType(ff, null);
            if (fileType == null) {
                String filename = file.getName();
                int lastDot = filename.lastIndexOf('.');
                String extension = filename.substring(lastDot + 1);
                fileType = FileType.getFileType(extension, FileType.SVG);
            }
            file = fileType.addExtension(file);
            dimension = new Dimension((Integer) widthSpinner.getValue(), (Integer) heightSpinner.getValue());
            exportAll = exportAllToggle.isSelected();
            if (file.exists()) {
                int response = JOptionPane.showConfirmDialog(ExportDialog.this, _("Message.FileOverwrite", file.getAbsolutePath()), _("Title.Warning"), JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.NO_OPTION) {
                    return;
                }
            }
            setVisible(false);
        }
    }
}
