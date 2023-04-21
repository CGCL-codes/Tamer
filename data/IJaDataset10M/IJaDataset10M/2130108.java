package jorg.gui.config;

import javax.swing.JOptionPane;
import jorg.gui.Main;

public class ConfiguratorWindow extends javax.swing.JFrame {

    private Main main;

    public ConfiguratorWindow(Main main) {
        this();
        this.main = main;
    }

    private ConfiguratorWindow() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jTabPanel = new javax.swing.JTabbedPane();
        jPnGeneral = new javax.swing.JPanel();
        jlblLanguage = new javax.swing.JLabel();
        jTxtLanguage = new javax.swing.JTextField();
        jlblLookAndFeel = new javax.swing.JLabel();
        jTxtLookAndFeel = new javax.swing.JTextField();
        jlblFilesNotIndexable = new javax.swing.JLabel();
        jTxtFilesNotIndexable = new javax.swing.JTextField();
        jLblWordsNotIndexable = new javax.swing.JLabel();
        jTxtWordsNotIndexable = new javax.swing.JTextField();
        jLblNumberOfResults = new javax.swing.JLabel();
        jTxtNumberOfResults = new javax.swing.JTextField();
        jPnGrouping = new javax.swing.JPanel();
        jLblPersonal = new javax.swing.JLabel();
        jLblVideo = new javax.swing.JLabel();
        jLblAudio = new javax.swing.JLabel();
        jLblPicture = new javax.swing.JLabel();
        jLblHtml = new javax.swing.JLabel();
        jLblOfficeDocs = new javax.swing.JLabel();
        jLblDiskImages = new javax.swing.JLabel();
        jLblCompression = new javax.swing.JLabel();
        jTxtPersonal = new javax.swing.JTextField();
        jTxtVideo = new javax.swing.JTextField();
        jTxtAudio = new javax.swing.JTextField();
        jTxtPicture = new javax.swing.JTextField();
        jTxtHtml = new javax.swing.JTextField();
        jTxtOfficeDocs = new javax.swing.JTextField();
        jTxtDiskImage = new javax.swing.JTextField();
        jTxtCompression = new javax.swing.JTextField();
        jLblInfo = new javax.swing.JLabel();
        jbtBack = new javax.swing.JButton();
        jbtSave = new javax.swing.JButton();
        setTitle("Jorg - Config");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }

            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        jTabPanel.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        jlblLanguage.setForeground(new java.awt.Color(0, 0, 153));
        jlblLanguage.setText("Language (filename of lang)");
        jlblLookAndFeel.setForeground(new java.awt.Color(0, 0, 153));
        jlblLookAndFeel.setText("Look & Feel (Metal, Nimbus, System, Motif, GTK)\n");
        jlblFilesNotIndexable.setForeground(new java.awt.Color(0, 0, 153));
        jlblFilesNotIndexable.setText("Files not indexable (ex: *.ini,Folder.jpg,Thumbs.db,albumart_*)\n");
        jLblWordsNotIndexable.setForeground(new java.awt.Color(0, 0, 153));
        jLblWordsNotIndexable.setText("Words not indexable (Stop words -> Words that doesn't mean nothing when you are searching) ");
        jLblNumberOfResults.setForeground(new java.awt.Color(0, 0, 153));
        jLblNumberOfResults.setText("Number of results");
        javax.swing.GroupLayout jPnGeneralLayout = new javax.swing.GroupLayout(jPnGeneral);
        jPnGeneral.setLayout(jPnGeneralLayout);
        jPnGeneralLayout.setHorizontalGroup(jPnGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPnGeneralLayout.createSequentialGroup().addContainerGap().addGroup(jPnGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPnGeneralLayout.createSequentialGroup().addComponent(jLblWordsNotIndexable).addContainerGap()).addGroup(jPnGeneralLayout.createSequentialGroup().addComponent(jlblFilesNotIndexable).addGap(413, 413, 413)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPnGeneralLayout.createSequentialGroup().addGroup(jPnGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jTxtFilesNotIndexable, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 803, Short.MAX_VALUE).addComponent(jTxtWordsNotIndexable, javax.swing.GroupLayout.DEFAULT_SIZE, 803, Short.MAX_VALUE)).addContainerGap()).addGroup(jPnGeneralLayout.createSequentialGroup().addGroup(jPnGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLblNumberOfResults).addComponent(jTxtNumberOfResults, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(624, Short.MAX_VALUE)).addGroup(jPnGeneralLayout.createSequentialGroup().addGroup(jPnGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTxtLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jlblLanguage)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPnGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jlblLookAndFeel).addComponent(jTxtLookAndFeel, javax.swing.GroupLayout.DEFAULT_SIZE, 608, Short.MAX_VALUE)).addContainerGap()))));
        jPnGeneralLayout.setVerticalGroup(jPnGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPnGeneralLayout.createSequentialGroup().addContainerGap().addGroup(jPnGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jlblLanguage).addComponent(jlblLookAndFeel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(2, 2, 2).addGroup(jPnGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jTxtLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jTxtLookAndFeel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jlblFilesNotIndexable).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTxtFilesNotIndexable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLblWordsNotIndexable, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTxtWordsNotIndexable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLblNumberOfResults).addGap(2, 2, 2).addComponent(jTxtNumberOfResults, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(81, Short.MAX_VALUE)));
        jTabPanel.addTab("General", new javax.swing.ImageIcon(getClass().getResource("/jorg/gui/picture/Server24.gif")), jPnGeneral);
        jLblPersonal.setForeground(new java.awt.Color(0, 0, 102));
        jLblPersonal.setText("Personal");
        jLblVideo.setForeground(new java.awt.Color(0, 0, 102));
        jLblVideo.setText("Video");
        jLblAudio.setForeground(new java.awt.Color(0, 0, 102));
        jLblAudio.setText("Audio");
        jLblPicture.setForeground(new java.awt.Color(0, 0, 102));
        jLblPicture.setText("Picture");
        jLblHtml.setForeground(new java.awt.Color(0, 0, 102));
        jLblHtml.setText("Html");
        jLblOfficeDocs.setForeground(new java.awt.Color(0, 0, 102));
        jLblOfficeDocs.setText("Office Docs");
        jLblDiskImages.setForeground(new java.awt.Color(0, 0, 102));
        jLblDiskImages.setText("Disk Image (Iso)");
        jLblCompression.setForeground(new java.awt.Color(0, 0, 102));
        jLblCompression.setText("Compression");
        javax.swing.GroupLayout jPnGroupingLayout = new javax.swing.GroupLayout(jPnGrouping);
        jPnGrouping.setLayout(jPnGroupingLayout);
        jPnGroupingLayout.setHorizontalGroup(jPnGroupingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPnGroupingLayout.createSequentialGroup().addContainerGap().addGroup(jPnGroupingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPnGroupingLayout.createSequentialGroup().addComponent(jLblPersonal).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTxtPersonal, javax.swing.GroupLayout.DEFAULT_SIZE, 758, Short.MAX_VALUE)).addGroup(jPnGroupingLayout.createSequentialGroup().addComponent(jLblVideo).addGap(18, 18, 18).addComponent(jTxtVideo, javax.swing.GroupLayout.DEFAULT_SIZE, 759, Short.MAX_VALUE)).addGroup(jPnGroupingLayout.createSequentialGroup().addComponent(jLblAudio).addGap(18, 18, 18).addComponent(jTxtAudio, javax.swing.GroupLayout.DEFAULT_SIZE, 758, Short.MAX_VALUE)).addGroup(jPnGroupingLayout.createSequentialGroup().addComponent(jLblPicture).addGap(12, 12, 12).addComponent(jTxtPicture, javax.swing.GroupLayout.DEFAULT_SIZE, 758, Short.MAX_VALUE)).addGroup(jPnGroupingLayout.createSequentialGroup().addComponent(jLblHtml).addGap(24, 24, 24).addComponent(jTxtHtml, javax.swing.GroupLayout.DEFAULT_SIZE, 758, Short.MAX_VALUE)).addGroup(jPnGroupingLayout.createSequentialGroup().addComponent(jLblOfficeDocs).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTxtOfficeDocs, javax.swing.GroupLayout.DEFAULT_SIZE, 744, Short.MAX_VALUE)).addGroup(jPnGroupingLayout.createSequentialGroup().addComponent(jLblDiskImages).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTxtDiskImage, javax.swing.GroupLayout.DEFAULT_SIZE, 721, Short.MAX_VALUE)).addGroup(jPnGroupingLayout.createSequentialGroup().addComponent(jLblCompression).addGap(18, 18, 18).addComponent(jTxtCompression, javax.swing.GroupLayout.DEFAULT_SIZE, 724, Short.MAX_VALUE))).addContainerGap()));
        jPnGroupingLayout.setVerticalGroup(jPnGroupingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPnGroupingLayout.createSequentialGroup().addContainerGap().addGroup(jPnGroupingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLblPersonal).addComponent(jTxtPersonal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPnGroupingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLblVideo).addComponent(jTxtVideo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPnGroupingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLblAudio).addComponent(jTxtAudio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPnGroupingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLblPicture).addComponent(jTxtPicture, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPnGroupingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLblHtml).addComponent(jTxtHtml, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPnGroupingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLblOfficeDocs).addComponent(jTxtOfficeDocs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPnGroupingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLblDiskImages).addComponent(jTxtDiskImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPnGroupingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLblCompression).addComponent(jTxtCompression, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(54, Short.MAX_VALUE)));
        jTabPanel.addTab("Grouping", new javax.swing.ImageIcon(getClass().getResource("/jorg/gui/picture/ComposeMail24.gif")), jPnGrouping);
        jLblInfo.setFont(new java.awt.Font("Tahoma", 0, 32));
        jLblInfo.setForeground(new java.awt.Color(51, 51, 255));
        jLblInfo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLblInfo.setText("Configuration");
        jLblInfo.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jbtBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jorg/gui/picture/Rewind24.gif")));
        jbtBack.setText("Back");
        jbtBack.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtBackActionPerformed(evt);
            }
        });
        jbtSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jorg/gui/picture/Save24.gif")));
        jbtSave.setText("Save");
        jbtSave.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtSaveActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTabPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 915, Short.MAX_VALUE).addComponent(jLblInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 915, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(jbtSave).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jbtBack))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLblInfo).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTabPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jbtBack).addComponent(jbtSave)).addContainerGap()));
        pack();
    }

    private void formWindowActivated(java.awt.event.WindowEvent evt) {
        loadFileValuesToFiedls();
    }

    private void formWindowOpened(java.awt.event.WindowEvent evt) {
    }

    private void jbtBackActionPerformed(java.awt.event.ActionEvent evt) {
        setVisible(false);
    }

    private void jbtSaveActionPerformed(java.awt.event.ActionEvent evt) {
        saveAll();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new ConfiguratorWindow().setVisible(true);
            }
        });
    }

    private javax.swing.JLabel jLblAudio;

    private javax.swing.JLabel jLblCompression;

    private javax.swing.JLabel jLblDiskImages;

    private javax.swing.JLabel jLblHtml;

    private javax.swing.JLabel jLblInfo;

    private javax.swing.JLabel jLblNumberOfResults;

    private javax.swing.JLabel jLblOfficeDocs;

    private javax.swing.JLabel jLblPersonal;

    private javax.swing.JLabel jLblPicture;

    private javax.swing.JLabel jLblVideo;

    private javax.swing.JLabel jLblWordsNotIndexable;

    private javax.swing.JPanel jPnGeneral;

    private javax.swing.JPanel jPnGrouping;

    private javax.swing.JTabbedPane jTabPanel;

    private javax.swing.JTextField jTxtAudio;

    private javax.swing.JTextField jTxtCompression;

    private javax.swing.JTextField jTxtDiskImage;

    private javax.swing.JTextField jTxtFilesNotIndexable;

    private javax.swing.JTextField jTxtHtml;

    private javax.swing.JTextField jTxtLanguage;

    private javax.swing.JTextField jTxtLookAndFeel;

    private javax.swing.JTextField jTxtNumberOfResults;

    private javax.swing.JTextField jTxtOfficeDocs;

    private javax.swing.JTextField jTxtPersonal;

    private javax.swing.JTextField jTxtPicture;

    private javax.swing.JTextField jTxtVideo;

    private javax.swing.JTextField jTxtWordsNotIndexable;

    private javax.swing.JButton jbtBack;

    private javax.swing.JButton jbtSave;

    private javax.swing.JLabel jlblFilesNotIndexable;

    private javax.swing.JLabel jlblLanguage;

    private javax.swing.JLabel jlblLookAndFeel;

    private void loadFileValuesToFiedls() {
        getjTxtLanguage().setText(Configurator.getSetupProprerty("language.file"));
        getjTxtLookAndFeel().setText(Configurator.getSetupProprerty("lookandfeel"));
        getjTxtFilesNotIndexable().setText(Configurator.getSetupProprerty("files.not.indexable"));
        getjTxtWordsNotIndexable().setText(Configurator.getSetupProprerty("stop.words"));
        getjTxtNumberOfResults().setText(Configurator.getSetupProprerty("number.results"));
        getjTxtPersonal().setText(Configurator.getSetupProprerty("types.personal"));
        getjTxtCompression().setText(Configurator.getSetupProprerty("types.compression"));
        getjTxtDiskImage().setText(Configurator.getSetupProprerty("types.image"));
        getjTxtOfficeDocs().setText(Configurator.getSetupProprerty("types.docs"));
        getjTxtHtml().setText(Configurator.getSetupProprerty("types.html"));
        getjTxtPicture().setText(Configurator.getSetupProprerty("types.picture"));
        getjTxtAudio().setText(Configurator.getSetupProprerty("types.audio"));
        getjTxtVideo().setText(Configurator.getSetupProprerty("types.video"));
    }

    private void saveAll() {
        try {
            String previousLangFile = Configurator.getSetupProprerty("language.file");
            String settedLangFile = getjTxtLanguage().getText();
            Configurator.saveSetup("language.file", getjTxtLanguage().getText());
            if (!previousLangFile.equals(settedLangFile)) {
                getMain().internationalize();
                getMain().repaint();
                getMain().pack();
                JOptionPane.showMessageDialog(this, Configurator.getInternationlizedText("config.msg.could.require"));
            }
            String previousLookAndFeel = Configurator.getSetupProprerty("lookandfeel");
            String settedLookAndFeel = getjTxtLookAndFeel().getText();
            Configurator.saveSetup("lookandfeel", getjTxtLookAndFeel().getText());
            if (!previousLookAndFeel.equals(settedLookAndFeel)) {
                JOptionPane.showMessageDialog(this, Configurator.getInternationlizedText("config.msg.require"));
            }
            Configurator.saveSetup("files.not.indexable", getjTxtFilesNotIndexable().getText());
            Configurator.saveSetup("stop.words", getjTxtWordsNotIndexable().getText());
            Configurator.saveSetup("number.results", getjTxtNumberOfResults().getText());
            Configurator.saveSetup("types.personal", getjTxtPersonal().getText());
            Configurator.saveSetup("types.compression", getjTxtCompression().getText());
            Configurator.saveSetup("types.image", getjTxtDiskImage().getText());
            Configurator.saveSetup("types.docs", getjTxtOfficeDocs().getText());
            Configurator.saveSetup("types.html", getjTxtHtml().getText());
            Configurator.saveSetup("types.picture", getjTxtPicture().getText());
            Configurator.saveSetup("types.audio", getjTxtAudio().getText());
            Configurator.saveSetup("types.video", getjTxtVideo().getText());
        } catch (ConfiguratorException ex) {
            JOptionPane.showMessageDialog(this, Configurator.getInternationlizedText("config.msg.error") + ex);
        }
        loadFileValuesToFiedls();
    }

    /**
     * @return the main
     */
    public Main getMain() {
        return main;
    }

    /**
     * @param main the main to set
     */
    public void setMain(Main main) {
        this.main = main;
    }

    /**
     * @return the jLblAudio
     */
    public javax.swing.JLabel getjLblAudio() {
        return jLblAudio;
    }

    /**
     * @param jLblAudio the jLblAudio to set
     */
    public void setjLblAudio(javax.swing.JLabel jLblAudio) {
        this.jLblAudio = jLblAudio;
    }

    /**
     * @return the jLblCompression
     */
    public javax.swing.JLabel getjLblCompression() {
        return jLblCompression;
    }

    /**
     * @param jLblCompression the jLblCompression to set
     */
    public void setjLblCompression(javax.swing.JLabel jLblCompression) {
        this.jLblCompression = jLblCompression;
    }

    /**
     * @return the jLblDiskImages
     */
    public javax.swing.JLabel getjLblDiskImages() {
        return jLblDiskImages;
    }

    /**
     * @param jLblDiskImages the jLblDiskImages to set
     */
    public void setjLblDiskImages(javax.swing.JLabel jLblDiskImages) {
        this.jLblDiskImages = jLblDiskImages;
    }

    /**
     * @return the jLblHtml
     */
    public javax.swing.JLabel getjLblHtml() {
        return jLblHtml;
    }

    /**
     * @param jLblHtml the jLblHtml to set
     */
    public void setjLblHtml(javax.swing.JLabel jLblHtml) {
        this.jLblHtml = jLblHtml;
    }

    /**
     * @return the jLblInfo
     */
    public javax.swing.JLabel getjLblInfo() {
        return jLblInfo;
    }

    /**
     * @param jLblInfo the jLblInfo to set
     */
    public void setjLblInfo(javax.swing.JLabel jLblInfo) {
        this.jLblInfo = jLblInfo;
    }

    /**
     * @return the jLblNumberOfResults
     */
    public javax.swing.JLabel getjLblNumberOfResults() {
        return jLblNumberOfResults;
    }

    /**
     * @param jLblNumberOfResults the jLblNumberOfResults to set
     */
    public void setjLblNumberOfResults(javax.swing.JLabel jLblNumberOfResults) {
        this.jLblNumberOfResults = jLblNumberOfResults;
    }

    /**
     * @return the jLblOfficeDocs
     */
    public javax.swing.JLabel getjLblOfficeDocs() {
        return jLblOfficeDocs;
    }

    /**
     * @param jLblOfficeDocs the jLblOfficeDocs to set
     */
    public void setjLblOfficeDocs(javax.swing.JLabel jLblOfficeDocs) {
        this.jLblOfficeDocs = jLblOfficeDocs;
    }

    /**
     * @return the jLblPersonal
     */
    public javax.swing.JLabel getjLblPersonal() {
        return jLblPersonal;
    }

    /**
     * @param jLblPersonal the jLblPersonal to set
     */
    public void setjLblPersonal(javax.swing.JLabel jLblPersonal) {
        this.jLblPersonal = jLblPersonal;
    }

    /**
     * @return the jLblPicture
     */
    public javax.swing.JLabel getjLblPicture() {
        return jLblPicture;
    }

    /**
     * @param jLblPicture the jLblPicture to set
     */
    public void setjLblPicture(javax.swing.JLabel jLblPicture) {
        this.jLblPicture = jLblPicture;
    }

    /**
     * @return the jLblVideo
     */
    public javax.swing.JLabel getjLblVideo() {
        return jLblVideo;
    }

    /**
     * @param jLblVideo the jLblVideo to set
     */
    public void setjLblVideo(javax.swing.JLabel jLblVideo) {
        this.jLblVideo = jLblVideo;
    }

    /**
     * @return the jLblWordsNotIndexable
     */
    public javax.swing.JLabel getjLblWordsNotIndexable() {
        return jLblWordsNotIndexable;
    }

    /**
     * @param jLblWordsNotIndexable the jLblWordsNotIndexable to set
     */
    public void setjLblWordsNotIndexable(javax.swing.JLabel jLblWordsNotIndexable) {
        this.jLblWordsNotIndexable = jLblWordsNotIndexable;
    }

    /**
     * @return the jPnGeneral
     */
    public javax.swing.JPanel getjPnGeneral() {
        return jPnGeneral;
    }

    /**
     * @param jPnGeneral the jPnGeneral to set
     */
    public void setjPnGeneral(javax.swing.JPanel jPnGeneral) {
        this.jPnGeneral = jPnGeneral;
    }

    /**
     * @return the jPnGrouping
     */
    public javax.swing.JPanel getjPnGrouping() {
        return jPnGrouping;
    }

    /**
     * @param jPnGrouping the jPnGrouping to set
     */
    public void setjPnGrouping(javax.swing.JPanel jPnGrouping) {
        this.jPnGrouping = jPnGrouping;
    }

    /**
     * @return the jTabPanel
     */
    public javax.swing.JTabbedPane getjTabPanel() {
        return jTabPanel;
    }

    /**
     * @param jTabPanel the jTabPanel to set
     */
    public void setjTabPanel(javax.swing.JTabbedPane jTabPanel) {
        this.jTabPanel = jTabPanel;
    }

    /**
     * @return the jTxtAudio
     */
    public javax.swing.JTextField getjTxtAudio() {
        return jTxtAudio;
    }

    /**
     * @param jTxtAudio the jTxtAudio to set
     */
    public void setjTxtAudio(javax.swing.JTextField jTxtAudio) {
        this.jTxtAudio = jTxtAudio;
    }

    /**
     * @return the jTxtCompression
     */
    public javax.swing.JTextField getjTxtCompression() {
        return jTxtCompression;
    }

    /**
     * @param jTxtCompression the jTxtCompression to set
     */
    public void setjTxtCompression(javax.swing.JTextField jTxtCompression) {
        this.jTxtCompression = jTxtCompression;
    }

    /**
     * @return the jTxtDiskImage
     */
    public javax.swing.JTextField getjTxtDiskImage() {
        return jTxtDiskImage;
    }

    /**
     * @param jTxtDiskImage the jTxtDiskImage to set
     */
    public void setjTxtDiskImage(javax.swing.JTextField jTxtDiskImage) {
        this.jTxtDiskImage = jTxtDiskImage;
    }

    /**
     * @return the jTxtFilesNotIndexable
     */
    public javax.swing.JTextField getjTxtFilesNotIndexable() {
        return jTxtFilesNotIndexable;
    }

    /**
     * @param jTxtFilesNotIndexable the jTxtFilesNotIndexable to set
     */
    public void setjTxtFilesNotIndexable(javax.swing.JTextField jTxtFilesNotIndexable) {
        this.jTxtFilesNotIndexable = jTxtFilesNotIndexable;
    }

    /**
     * @return the jTxtHtml
     */
    public javax.swing.JTextField getjTxtHtml() {
        return jTxtHtml;
    }

    /**
     * @param jTxtHtml the jTxtHtml to set
     */
    public void setjTxtHtml(javax.swing.JTextField jTxtHtml) {
        this.jTxtHtml = jTxtHtml;
    }

    /**
     * @return the jTxtLanguage
     */
    public javax.swing.JTextField getjTxtLanguage() {
        return jTxtLanguage;
    }

    /**
     * @param jTxtLanguage the jTxtLanguage to set
     */
    public void setjTxtLanguage(javax.swing.JTextField jTxtLanguage) {
        this.jTxtLanguage = jTxtLanguage;
    }

    /**
     * @return the jTxtLookAndFeel
     */
    public javax.swing.JTextField getjTxtLookAndFeel() {
        return jTxtLookAndFeel;
    }

    /**
     * @param jTxtLookAndFeel the jTxtLookAndFeel to set
     */
    public void setjTxtLookAndFeel(javax.swing.JTextField jTxtLookAndFeel) {
        this.jTxtLookAndFeel = jTxtLookAndFeel;
    }

    /**
     * @return the jTxtNumberOfResults
     */
    public javax.swing.JTextField getjTxtNumberOfResults() {
        return jTxtNumberOfResults;
    }

    /**
     * @param jTxtNumberOfResults the jTxtNumberOfResults to set
     */
    public void setjTxtNumberOfResults(javax.swing.JTextField jTxtNumberOfResults) {
        this.jTxtNumberOfResults = jTxtNumberOfResults;
    }

    /**
     * @return the jTxtOfficeDocs
     */
    public javax.swing.JTextField getjTxtOfficeDocs() {
        return jTxtOfficeDocs;
    }

    /**
     * @param jTxtOfficeDocs the jTxtOfficeDocs to set
     */
    public void setjTxtOfficeDocs(javax.swing.JTextField jTxtOfficeDocs) {
        this.jTxtOfficeDocs = jTxtOfficeDocs;
    }

    /**
     * @return the jTxtPersonal
     */
    public javax.swing.JTextField getjTxtPersonal() {
        return jTxtPersonal;
    }

    /**
     * @param jTxtPersonal the jTxtPersonal to set
     */
    public void setjTxtPersonal(javax.swing.JTextField jTxtPersonal) {
        this.jTxtPersonal = jTxtPersonal;
    }

    /**
     * @return the jTxtPicture
     */
    public javax.swing.JTextField getjTxtPicture() {
        return jTxtPicture;
    }

    /**
     * @param jTxtPicture the jTxtPicture to set
     */
    public void setjTxtPicture(javax.swing.JTextField jTxtPicture) {
        this.jTxtPicture = jTxtPicture;
    }

    /**
     * @return the jTxtVideo
     */
    public javax.swing.JTextField getjTxtVideo() {
        return jTxtVideo;
    }

    /**
     * @param jTxtVideo the jTxtVideo to set
     */
    public void setjTxtVideo(javax.swing.JTextField jTxtVideo) {
        this.jTxtVideo = jTxtVideo;
    }

    /**
     * @return the jTxtWordsNotIndexable
     */
    public javax.swing.JTextField getjTxtWordsNotIndexable() {
        return jTxtWordsNotIndexable;
    }

    /**
     * @param jTxtWordsNotIndexable the jTxtWordsNotIndexable to set
     */
    public void setjTxtWordsNotIndexable(javax.swing.JTextField jTxtWordsNotIndexable) {
        this.jTxtWordsNotIndexable = jTxtWordsNotIndexable;
    }

    /**
     * @return the jbtBack
     */
    public javax.swing.JButton getJbtBack() {
        return jbtBack;
    }

    /**
     * @param jbtBack the jbtBack to set
     */
    public void setJbtBack(javax.swing.JButton jbtBack) {
        this.jbtBack = jbtBack;
    }

    /**
     * @return the jbtSave
     */
    public javax.swing.JButton getJbtSave() {
        return jbtSave;
    }

    /**
     * @param jbtSave the jbtSave to set
     */
    public void setJbtSave(javax.swing.JButton jbtSave) {
        this.jbtSave = jbtSave;
    }

    /**
     * @return the jlblFilesNotIndexable
     */
    public javax.swing.JLabel getJlblFilesNotIndexable() {
        return jlblFilesNotIndexable;
    }

    /**
     * @param jlblFilesNotIndexable the jlblFilesNotIndexable to set
     */
    public void setJlblFilesNotIndexable(javax.swing.JLabel jlblFilesNotIndexable) {
        this.jlblFilesNotIndexable = jlblFilesNotIndexable;
    }

    /**
     * @return the jlblLanguage
     */
    public javax.swing.JLabel getJlblLanguage() {
        return jlblLanguage;
    }

    /**
     * @param jlblLanguage the jlblLanguage to set
     */
    public void setJlblLanguage(javax.swing.JLabel jlblLanguage) {
        this.jlblLanguage = jlblLanguage;
    }

    /**
     * @return the jlblLookAndFeel
     */
    public javax.swing.JLabel getJlblLookAndFeel() {
        return jlblLookAndFeel;
    }

    /**
     * @param jlblLookAndFeel the jlblLookAndFeel to set
     */
    public void setJlblLookAndFeel(javax.swing.JLabel jlblLookAndFeel) {
        this.jlblLookAndFeel = jlblLookAndFeel;
    }
}
