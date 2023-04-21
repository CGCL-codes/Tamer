package ces.platform.infoplat.service.ftpserver.ftp.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import ces.platform.infoplat.utils.gui.GuiUtils;
import ces.platform.infoplat.service.ftpserver.ftp.FtpConfig;
import ces.platform.infoplat.service.ftpserver.ftp.FtpFileListener;
import ces.platform.infoplat.service.ftpserver.ftp.FtpStatistics;
import ces.platform.infoplat.service.ftpserver.ftp.FtpStatisticsListener;
import ces.platform.infoplat.service.ftpserver.ftp.FtpUser;

/**
 * Ftp server global statistics panel.
 *
 * @author <a href="mailto:rana_b@yahoo.com">Rana Bhattacharyya</a>
 */
public class FtpStatisticsPanel extends PluginPanel implements FtpStatisticsListener, FtpFileListener {

    private static final String RELOAD_IMG = "ces/platform/infoplat/service/ftpserver/res/img/reload.gif";

    private static final SimpleDateFormat DATE_FMT = new SimpleDateFormat("dd,MMM,yyyy HH:mm");

    private FtpConfig mConfig;

    private FtpStatistics mStat;

    private JTextField mjStartTimeTxt;

    private JTextField mjUploadNbrTxt;

    private JTextField mjDownloadNbrTxt;

    private JTextField mjDeleteNbrTxt;

    private JTextField mjUploadBytesTxt;

    private JTextField mjDownloadBytesTxt;

    private JTextField mjLoginNbrTxt;

    private JTextField mjAnonLoginNbrTxt;

    private JTextField mjConNbrTxt;

    private JTextField mjTotalLoginNbrTxt;

    private JTextField mjTotalAnonLoginNbrTxt;

    private JTextField mjTotalConNbrTxt;

    private FtpFileTableModel mUploadModel;

    private FtpFileTableModel mDownloadModel;

    private FtpFileTableModel mDeleteModel;

    /**
     * Creates new panel to display ftp global statistics.
     */
    public FtpStatisticsPanel(FtpTree tree) {
        super(tree);
        initComponents();
        mUploadModel = new FtpFileTableModel();
        mDownloadModel = new FtpFileTableModel();
        mDeleteModel = new FtpFileTableModel();
    }

    /**
     * This method is called from within the constructor to
     * initialize the panel.
     */
    private void initComponents() {
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(3, 0, 0, 3);
        setLayout(new GridBagLayout());
        int yindex = -1;
        JLabel jStartTimeLab = new JLabel("Start Time");
        jStartTimeLab.setHorizontalAlignment(JLabel.RIGHT);
        jStartTimeLab.setForeground(Color.black);
        gc.gridx = 0;
        gc.gridy = ++yindex;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.EAST;
        add(jStartTimeLab, gc);
        mjStartTimeTxt = new JTextField();
        mjStartTimeTxt.setColumns(12);
        mjStartTimeTxt.setEditable(false);
        mjStartTimeTxt.setBackground(Color.white);
        gc.gridx = 2;
        gc.gridy = yindex;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.WEST;
        add(mjStartTimeTxt, gc);
        JLabel jUploadNbrLab = new JLabel("Number of uploads");
        jUploadNbrLab.setHorizontalAlignment(JLabel.RIGHT);
        jUploadNbrLab.setForeground(Color.black);
        gc.gridx = 0;
        gc.gridy = ++yindex;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.EAST;
        add(jUploadNbrLab, gc);
        mjUploadNbrTxt = new JTextField();
        mjUploadNbrTxt.setColumns(6);
        mjUploadNbrTxt.setEditable(false);
        mjUploadNbrTxt.setBackground(Color.white);
        gc.gridx = 2;
        gc.gridy = yindex;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.WEST;
        add(mjUploadNbrTxt, gc);
        JLabel jDownloadNbrLab = new JLabel("Number of downloads");
        jDownloadNbrLab.setHorizontalAlignment(JLabel.RIGHT);
        jDownloadNbrLab.setForeground(Color.black);
        gc.gridx = 0;
        gc.gridy = ++yindex;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.EAST;
        add(jDownloadNbrLab, gc);
        mjDownloadNbrTxt = new JTextField();
        mjDownloadNbrTxt.setColumns(6);
        mjDownloadNbrTxt.setEditable(false);
        mjDownloadNbrTxt.setBackground(Color.white);
        gc.gridx = 2;
        gc.gridy = yindex;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.WEST;
        add(mjDownloadNbrTxt, gc);
        JLabel jDeleteNbrLab = new JLabel("Number of deletes");
        jDeleteNbrLab.setHorizontalAlignment(JLabel.RIGHT);
        jDeleteNbrLab.setForeground(Color.black);
        gc.gridx = 0;
        gc.gridy = ++yindex;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.EAST;
        add(jDeleteNbrLab, gc);
        mjDeleteNbrTxt = new JTextField();
        mjDeleteNbrTxt.setColumns(6);
        mjDeleteNbrTxt.setEditable(false);
        mjDeleteNbrTxt.setBackground(Color.white);
        gc.gridx = 2;
        gc.gridy = yindex;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.WEST;
        add(mjDeleteNbrTxt, gc);
        JLabel jUploadBytesLab = new JLabel("Uploaded bytes");
        jUploadBytesLab.setHorizontalAlignment(JLabel.RIGHT);
        jUploadBytesLab.setForeground(Color.black);
        gc.gridx = 0;
        gc.gridy = ++yindex;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.EAST;
        add(jUploadBytesLab, gc);
        mjUploadBytesTxt = new JTextField();
        mjUploadBytesTxt.setColumns(12);
        mjUploadBytesTxt.setEditable(false);
        mjUploadBytesTxt.setBackground(Color.white);
        gc.gridx = 2;
        gc.gridy = yindex;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.WEST;
        add(mjUploadBytesTxt, gc);
        JLabel jDownloadBytesLab = new JLabel("Downloaded bytes");
        jDownloadBytesLab.setHorizontalAlignment(JLabel.RIGHT);
        jDownloadBytesLab.setForeground(Color.black);
        gc.gridx = 0;
        gc.gridy = ++yindex;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.EAST;
        add(jDownloadBytesLab, gc);
        mjDownloadBytesTxt = new JTextField();
        mjDownloadBytesTxt.setColumns(12);
        mjDownloadBytesTxt.setEditable(false);
        mjDownloadBytesTxt.setBackground(Color.white);
        gc.gridx = 2;
        gc.gridy = yindex;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.WEST;
        add(mjDownloadBytesTxt, gc);
        JLabel jLoginNbrLab = new JLabel("Current logins");
        jLoginNbrLab.setHorizontalAlignment(JLabel.RIGHT);
        jLoginNbrLab.setForeground(Color.black);
        gc.gridx = 0;
        gc.gridy = ++yindex;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.EAST;
        add(jLoginNbrLab, gc);
        mjLoginNbrTxt = new JTextField();
        mjLoginNbrTxt.setColumns(6);
        mjLoginNbrTxt.setEditable(false);
        mjLoginNbrTxt.setBackground(Color.white);
        gc.gridx = 2;
        gc.gridy = yindex;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.WEST;
        add(mjLoginNbrTxt, gc);
        JLabel jTotalLoginNbrLab = new JLabel("Total logins");
        jTotalLoginNbrLab.setHorizontalAlignment(JLabel.RIGHT);
        jTotalLoginNbrLab.setForeground(Color.black);
        gc.gridx = 0;
        gc.gridy = ++yindex;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.EAST;
        add(jTotalLoginNbrLab, gc);
        mjTotalLoginNbrTxt = new JTextField();
        mjTotalLoginNbrTxt.setColumns(6);
        mjTotalLoginNbrTxt.setEditable(false);
        mjTotalLoginNbrTxt.setBackground(Color.white);
        gc.gridx = 2;
        gc.gridy = yindex;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.WEST;
        add(mjTotalLoginNbrTxt, gc);
        JLabel jAnonLoginNbrLab = new JLabel("Current anonymous logins");
        jAnonLoginNbrLab.setHorizontalAlignment(JLabel.RIGHT);
        jAnonLoginNbrLab.setForeground(Color.black);
        gc.gridx = 0;
        gc.gridy = ++yindex;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.EAST;
        add(jAnonLoginNbrLab, gc);
        mjAnonLoginNbrTxt = new JTextField();
        mjAnonLoginNbrTxt.setColumns(6);
        mjAnonLoginNbrTxt.setEditable(false);
        mjAnonLoginNbrTxt.setBackground(Color.white);
        gc.gridx = 2;
        gc.gridy = yindex;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.WEST;
        add(mjAnonLoginNbrTxt, gc);
        JLabel jTotalAnonLoginNbrLab = new JLabel("Total anonymous logins");
        jTotalAnonLoginNbrLab.setHorizontalAlignment(JLabel.RIGHT);
        jTotalAnonLoginNbrLab.setForeground(Color.black);
        gc.gridx = 0;
        gc.gridy = ++yindex;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.EAST;
        add(jTotalAnonLoginNbrLab, gc);
        mjTotalAnonLoginNbrTxt = new JTextField();
        mjTotalAnonLoginNbrTxt.setColumns(6);
        mjTotalAnonLoginNbrTxt.setEditable(false);
        mjTotalAnonLoginNbrTxt.setBackground(Color.white);
        gc.gridx = 2;
        gc.gridy = yindex;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.WEST;
        add(mjTotalAnonLoginNbrTxt, gc);
        JLabel jConNbrLab = new JLabel("Current connections");
        jConNbrLab.setHorizontalAlignment(JLabel.RIGHT);
        jConNbrLab.setForeground(Color.black);
        gc.gridx = 0;
        gc.gridy = ++yindex;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.EAST;
        add(jConNbrLab, gc);
        mjConNbrTxt = new JTextField();
        mjConNbrTxt.setColumns(6);
        mjConNbrTxt.setEditable(false);
        mjConNbrTxt.setBackground(Color.white);
        gc.gridx = 2;
        gc.gridy = yindex;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.WEST;
        add(mjConNbrTxt, gc);
        JLabel jTotalConNbrLab = new JLabel("Total connections");
        jTotalConNbrLab.setHorizontalAlignment(JLabel.RIGHT);
        jTotalConNbrLab.setForeground(Color.black);
        gc.gridx = 0;
        gc.gridy = ++yindex;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.EAST;
        add(jTotalConNbrLab, gc);
        mjTotalConNbrTxt = new JTextField();
        mjTotalConNbrTxt.setColumns(6);
        mjTotalConNbrTxt.setEditable(false);
        mjTotalConNbrTxt.setBackground(Color.white);
        gc.gridx = 2;
        gc.gridy = yindex;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.WEST;
        add(mjTotalConNbrTxt, gc);
        JButton reloadButton = new JButton("Reload", GuiUtils.createImageIcon(RELOAD_IMG));
        gc.gridx = 0;
        gc.gridy = ++yindex;
        gc.gridwidth = 4;
        gc.anchor = GridBagConstraints.CENTER;
        add(reloadButton, gc);
        reloadButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                reloadStatistics();
            }
        });
    }

    /**
     * Get upload file table model.
     */
    public FtpFileTableModel getUploadModel() {
        return mUploadModel;
    }

    /**
     * Get download file table model.
     */
    public FtpFileTableModel getDownloadModel() {
        return mDownloadModel;
    }

    /**
     * Get delete file table model.
     */
    public FtpFileTableModel getDeleteModel() {
        return mDeleteModel;
    }

    /**
     * Upload notification.
     */
    public void notifyUpload() {
        mjUploadNbrTxt.setText(String.valueOf(mStat.getFileUploadNbr()));
        mjUploadBytesTxt.setText(String.valueOf(mStat.getFileUploadSize()));
    }

    /**
     * Download notification.
     */
    public void notifyDownload() {
        mjDownloadNbrTxt.setText(String.valueOf(mStat.getFileDownloadNbr()));
        mjDownloadBytesTxt.setText(String.valueOf(mStat.getFileDownloadSize()));
    }

    /**
     * Delete notification.
     */
    public void notifyDelete() {
        mjDeleteNbrTxt.setText(String.valueOf(mStat.getFileDeleteNbr()));
    }

    /**
     * User login notification.
     */
    public void notifyLogin() {
        mjLoginNbrTxt.setText(String.valueOf(mStat.getLoginNbr()));
        mjAnonLoginNbrTxt.setText(String.valueOf(mStat.getAnonLoginNbr()));
        mjTotalLoginNbrTxt.setText(String.valueOf(mStat.getTotalLoginNbr()));
        mjTotalAnonLoginNbrTxt.setText(String.valueOf(mStat.getTotalAnonLoginNbr()));
    }

    /**
     * User logout notification.
     */
    public void notifyLogout() {
        notifyLogin();
    }

    /**
     * Notify open/close connection
     */
    public void notifyConnection() {
        mjConNbrTxt.setText(String.valueOf(mStat.getConnectionNbr()));
        mjTotalConNbrTxt.setText(String.valueOf(mStat.getTotalConnectionNbr()));
    }

    /**
     * Notify file upload
     */
    public void notifyUpload(final File fl, final FtpUser user) {
        mUploadModel.newEntry(fl.getAbsolutePath(), user);
    }

    /**
     * Notify file download
     */
    public void notifyDownload(final File fl, final FtpUser user) {
        mDownloadModel.newEntry(fl.getAbsolutePath(), user);
    }

    /**
     * Notify file delete
     */
    public void notifyDelete(final File fl, final FtpUser user) {
        mDeleteModel.newEntry(fl.getAbsolutePath(), user);
    }

    /**
     * Reload data
     */
    private void reloadStatistics() {
        FtpConfig cfg = mConfig;
        if (cfg != null) {
            mjStartTimeTxt.setText(DATE_FMT.format(mStat.getStartTime()));
            notifyUpload();
            notifyDownload();
            notifyDelete();
            notifyLogin();
            notifyConnection();
        }
    }

    /**
     * Initialize the statistics data.
     */
    public void refresh(FtpConfig cfg) {
        mConfig = cfg;
        if (cfg != null) {
            mStat = mConfig.getStatistics();
            mStat.setListener(this);
            mStat.setFileListener(this);
            reloadStatistics();
        } else {
            mStat = null;
        }
        mUploadModel.reset();
        mDownloadModel.reset();
        mDeleteModel.reset();
    }

    /**
     * Is displayable
     */
    public boolean isDisplayable() {
        return mConfig != null;
    }
}
