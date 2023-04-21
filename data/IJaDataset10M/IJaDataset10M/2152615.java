package de.shandschuh.jaolt.gui.dialogs.importdialogs;

import java.awt.EventQueue;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import com.jgoodies.forms.builder.PanelBuilder;
import de.shandschuh.jaolt.core.Auction;
import de.shandschuh.jaolt.core.AuctionPlatformAccount;
import de.shandschuh.jaolt.core.FileImport;
import de.shandschuh.jaolt.core.Language;
import de.shandschuh.jaolt.gui.FormManager;
import de.shandschuh.jaolt.gui.Lister;
import de.shandschuh.jaolt.gui.core.TagsJPanel;
import de.shandschuh.jaolt.gui.listener.core.ProgressBarStatusListener;
import de.shandschuh.jaolt.gui.listener.dialogs.importdialogs.OpenFileListener;
import de.shandschuh.jaolt.gui.maintabbedpane.ListJPanel;

public class ImportAuctionsFromFileFormManager extends FormManager {

    private FileImport fileImport;

    private JButton importJButton;

    private JComboBox auctionPlatformAccountJComboBox;

    private JComboBox auctionListJPanelJComboBox;

    private TagsJPanel tagsJPanel;

    private JButton fileJButton;

    private JTextField filenameJTextField;

    private JProgressBar progressBar;

    private Auction[] auctions;

    public ImportAuctionsFromFileFormManager(FileImport fileImport, JButton importJButton) {
        this.fileImport = fileImport;
        this.importJButton = importJButton;
        auctionPlatformAccountJComboBox = new JComboBox(Lister.getCurrentInstance().getMember().getAuctionPlatformAccounts());
        auctionListJPanelJComboBox = new JComboBox(Lister.getCurrentInstance().getMainJPanel().getListJPanel(Auction.class));
        auctionListJPanelJComboBox.setSelectedItem(Lister.getCurrentInstance().getMainJPanel().getCurrentListJPanel());
        filenameJTextField = new JTextField(15);
        filenameJTextField.setEditable(false);
        fileJButton = new JButton(Language.translateStatic("BUTTON_CHOOSEFILE_TEXT"));
        fileJButton.addActionListener(new OpenFileListener(fileImport.getFileExtension(), filenameJTextField));
        tagsJPanel = new TagsJPanel();
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setString("");
    }

    @Override
    protected void addPanelBuilderComponents(PanelBuilder panelBuilder) {
        panelBuilder.addSeparator(Language.translateStatic("SETTINGS"), getCellConstraints(1, 1, 7));
        panelBuilder.add(new JLabel(Language.translateStatic("AUCTIONPLATFORMACCOUNT")), getCellConstraints(2, 3));
        panelBuilder.add(auctionPlatformAccountJComboBox, getCellConstraints(4, 3, 3));
        panelBuilder.add(new JLabel(Language.translateStatic("AUCTIONFOLDER")), getCellConstraints(2, 5));
        panelBuilder.add(auctionListJPanelJComboBox, getCellConstraints(4, 5, 3));
        panelBuilder.add(new JLabel(Language.translateStatic("FILE")), getCellConstraints(2, 7));
        panelBuilder.add(filenameJTextField, getCellConstraints(4, 7));
        panelBuilder.add(fileJButton, getCellConstraints(6, 7));
        panelBuilder.add(new JLabel(Language.translateStatic("TAGS")), getCellConstraints(2, 9));
        panelBuilder.add(tagsJPanel, getCellConstraints(4, 9, 3));
        panelBuilder.addSeparator(Language.translateStatic("STATUS"), getCellConstraints(1, 11, 7));
        panelBuilder.add(progressBar, getCellConstraints(2, 13, 5));
    }

    @Override
    protected String getColumnLayout() {
        return "5dlu, left:p, 4dlu, p, 4dlu, p, fill:4dlu:grow";
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    protected String getRowLayout() {
        return "p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p";
    }

    @Override
    public boolean rebuildNeeded() {
        return false;
    }

    @Override
    protected void reloadLocal(boolean rebuild) {
    }

    @Override
    protected void saveLocal() throws Exception {
        EventQueue.invokeLater(new Thread() {

            public void run() {
                importJButton.setEnabled(false);
                progressBar.setString(Language.translateStatic("MESSAGE_STARTED"));
            }
        });
        try {
            progressBar.setValue(0);
            AuctionPlatformAccount auctionPlatformAccount = (AuctionPlatformAccount) auctionPlatformAccountJComboBox.getSelectedItem();
            auctions = fileImport.getAuctions(new File(filenameJTextField.getText()), auctionPlatformAccount, new ProgressBarStatusListener(progressBar));
            tagsJPanel.appyTags(auctions);
        } catch (Exception exception) {
            EventQueue.invokeLater(new Thread() {

                public void run() {
                    progressBar.setString(Language.translateStatic("ERROR"));
                }
            });
            throw exception;
        }
    }

    @Override
    protected void validateLocal() throws Exception {
    }

    @SuppressWarnings("unchecked")
    public void saveImportedAuctions() {
        ((ListJPanel<Auction>) auctionListJPanelJComboBox.getSelectedItem()).addObjects(auctions);
    }
}
