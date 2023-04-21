package geisler.projekt.editor.view;

import geisler.projekt.editor.test.EditorTest;
import geisler.projekt.game.test.GameTest;
import geisler.projekt.game.util.ResourceHelper;
import java.io.File;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author geislern
 */
public class StartAuswahlDialog extends JDialog {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private static Log LOG = LogFactory.getLog(StartAuswahlDialog.class);

    private String selectedProjectName;

    /** Creates new form StartAuswahlDialog */
    public StartAuswahlDialog() {
        initComponents();
    }

    private void initComponents() {
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        btnStartGame = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        btnStartEditor = new javax.swing.JButton();
        btnNewProject = new javax.swing.JButton();
        lblListProjects = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jlistProjects = new javax.swing.JList();
        jButton3 = new javax.swing.JButton();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Start Projekt-Auswahl");
        jSplitPane1.setDividerLocation(180);
        jSplitPane1.setDividerSize(10);
        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel2.setText("Game-Modus");
        btnStartGame.setFont(new java.awt.Font("Tahoma", 1, 11));
        btnStartGame.setText("Start");
        btnStartGame.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartGameActionPerformed(evt);
            }
        });
        btnStartGame.setEnabled(false);
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(btnStartGame, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE).addComponent(jLabel2)).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(btnStartGame, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE).addContainerGap()));
        jSplitPane1.setLeftComponent(jPanel1);
        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel3.setText("Editor-Modus");
        btnStartEditor.setFont(new java.awt.Font("Tahoma", 1, 11));
        btnStartEditor.setText("Start");
        btnStartEditor.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartEditorActionPerformed(evt);
            }
        });
        btnStartEditor.setEnabled(false);
        btnNewProject.setFont(new java.awt.Font("Tahoma", 1, 11));
        btnNewProject.setText("Neues-Projekt");
        btnNewProject.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewProjectActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(btnNewProject, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE).addComponent(btnStartEditor, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE).addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)).addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addComponent(jLabel3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(btnStartEditor, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnNewProject, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        jSplitPane1.setRightComponent(jPanel2);
        lblListProjects.setFont(new java.awt.Font("Tahoma", 1, 14));
        lblListProjects.setText("Liste Projekte:");
        jlistProjects.setModel(new DefaultListModel());
        jlistProjects.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        String sPath = System.getProperty("user.home") + "/Application/GameEditor/";
        try {
            File folderPath = new File(sPath);
            if (folderPath.exists()) {
                boolean logPropExists = false;
                File[] projectFolders = folderPath.listFiles();
                for (File project : projectFolders) {
                    if (project.isDirectory() && !project.getName().equalsIgnoreCase("logs")) {
                        LOG.info(project.getName());
                        ((DefaultListModel) jlistProjects.getModel()).addElement(project.getName());
                    }
                    if (project.isFile() && project.getName().equalsIgnoreCase("log4j.properties")) {
                        LOG.info("Properties-Datei f�r Log4j ist vorhanden.");
                        logPropExists = true;
                    }
                }
                if (!logPropExists) {
                    ResourceHelper.copyResourceInDirectory("/log4j.properties", folderPath, false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        jlistProjects.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jlistProjectsValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jlistProjects);
        jButton3.setFont(new java.awt.Font("Tahoma", 1, 11));
        jButton3.setText("Beenden");
        jButton3.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 367, Short.MAX_VALUE).addContainerGap()).addGroup(layout.createSequentialGroup().addComponent(lblListProjects).addContainerGap(277, Short.MAX_VALUE)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 367, Short.MAX_VALUE).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 367, Short.MAX_VALUE)).addContainerGap()))));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(lblListProjects).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE).addGap(18, 18, 18).addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        pack();
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }

    private void btnNewProjectActionPerformed(java.awt.event.ActionEvent evt) {
        NeuesProjektDialog neuDialog = new NeuesProjektDialog(this, true);
        neuDialog.setVisible(true);
        if (neuDialog.getReturnStatus() == NeuesProjektDialog.RET_OK) {
            selectedProjectName = NeuesProjektDialog.projectName;
            ((DefaultListModel) jlistProjects.getModel()).addElement(selectedProjectName);
            jlistProjects.setSelectedValue(selectedProjectName, true);
        }
    }

    private void btnStartGameActionPerformed(java.awt.event.ActionEvent evt) {
        LOG.info(selectedProjectName + " Projekt wurde im Spiel-Modus gestartet");
        if (selectedProjectName != null && !selectedProjectName.isEmpty()) {
            this.dispose();
            GameTest.main(new String[] { selectedProjectName });
        }
    }

    private void btnStartEditorActionPerformed(java.awt.event.ActionEvent evt) {
        LOG.info(selectedProjectName + " Projekt wurde im Editor-Modus gestartet");
        if (selectedProjectName != null && !selectedProjectName.isEmpty()) {
            this.dispose();
            EditorTest.main(new String[] { selectedProjectName });
        }
    }

    private void jlistProjectsValueChanged(javax.swing.event.ListSelectionEvent evt) {
        btnStartEditor.setEnabled(true);
        btnStartGame.setEnabled(true);
        selectedProjectName = (String) jlistProjects.getSelectedValue();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new StartAuswahlDialog().setVisible(true);
            }
        });
        PropertyConfigurator.configureAndWatch(System.getProperty("user.home") + "/" + "Application" + "/" + "GameEditor" + "/" + "log4j.properties");
    }

    private javax.swing.JButton btnNewProject;

    private javax.swing.JButton btnStartEditor;

    private javax.swing.JButton btnStartGame;

    private javax.swing.JButton jButton3;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JSplitPane jSplitPane1;

    private javax.swing.JList jlistProjects;

    private javax.swing.JLabel lblListProjects;
}
