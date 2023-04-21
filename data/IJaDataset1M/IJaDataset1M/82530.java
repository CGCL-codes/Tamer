package jschoolmanagerportable;

import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import jschoolmanagerportable.gui.AlumnosAdminView;
import jschoolmanagerportable.gui.AsignaturasAdminView;
import jschoolmanagerportable.gui.ColegiosAdminView;
import jschoolmanagerportable.gui.ConfiguracionView;
import jschoolmanagerportable.gui.CursosAdminView;
import jschoolmanagerportable.gui.NotasAdminView;
import jschoolmanagerportable.gui.reports.InformeDeNotasView;
import jschoolmanagerportable.gui.reports.ListadoAlumnosView;

/**
 * The application's main frame.
 */
public class JSchoolManagerPortableView extends FrameView {

    public JSchoolManagerPortableView(SingleFrameApplication app) {
        super(app);
        initComponents();
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            SwingUtilities.updateComponentTreeUI(mainPanel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public void showAboutBox(ActionEvent e) {
        if (aboutBox == null) {
            JFrame mainFrame = JSchoolManagerPortableApp.getApplication().getMainFrame();
            aboutBox = new JSchoolManagerPortableAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        JSchoolManagerPortableApp.getApplication().show(aboutBox);
    }

    private void initComponents() {
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        configuracionMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        registroMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        informesMenu = new javax.swing.JMenu();
        informeAlumnoMenuItem = new javax.swing.JMenuItem();
        informeCursoMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        buttonGroup1 = new javax.swing.ButtonGroup();
        entityManager = javax.persistence.Persistence.createEntityManagerFactory("JSchoolManagerPortablePU").createEntityManager();
        colegioQuery = entityManager.createQuery("SELECT c FROM Colegio c");
        colegioList = org.jdesktop.observablecollections.ObservableCollections.observableList(colegioQuery.getResultList());
        cursoQuery = entityManager.createQuery("SELECT c FROM Curso c");
        cursoList = org.jdesktop.observablecollections.ObservableCollections.observableList(cursoQuery.getResultList());
        alumnoQuery = entityManager.createQuery("SELECT a FROM Alumno a");
        alumnoList = org.jdesktop.observablecollections.ObservableCollections.observableList(alumnoQuery.getResultList());
        asignaturaQuery = entityManager.createQuery("SELECT a FROM Asignatura a");
        asignaturaList = org.jdesktop.observablecollections.ObservableCollections.observableList(asignaturaQuery.getResultList());
        mainPanel = new javax.swing.JDesktopPane();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        menuBar.setName("menuBar");
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(jschoolmanagerportable.JSchoolManagerPortableApp.class).getContext().getResourceMap(JSchoolManagerPortableView.class);
        fileMenu.setText(resourceMap.getString("fileMenu.text"));
        fileMenu.setName("fileMenu");
        configuracionMenuItem.setText(resourceMap.getString("configuracionMenuItem.text"));
        configuracionMenuItem.setName("configuracionMenuItem");
        configuracionMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                configuracionMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(configuracionMenuItem);
        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(jschoolmanagerportable.JSchoolManagerPortableApp.class).getContext().getActionMap(JSchoolManagerPortableView.class, this);
        exitMenuItem.setAction(actionMap.get("quit"));
        exitMenuItem.setName("exitMenuItem");
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        registroMenu.setText(resourceMap.getString("registroMenu.text"));
        registroMenu.setName("registroMenu");
        jMenuItem1.setText(resourceMap.getString("jMenuItem1.text"));
        jMenuItem1.setName("jMenuItem1");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        registroMenu.add(jMenuItem1);
        jMenuItem2.setText(resourceMap.getString("jMenuItem2.text"));
        jMenuItem2.setName("jMenuItem2");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        registroMenu.add(jMenuItem2);
        jMenuItem3.setText(resourceMap.getString("jMenuItem3.text"));
        jMenuItem3.setName("jMenuItem3");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        registroMenu.add(jMenuItem3);
        jMenuItem4.setText(resourceMap.getString("jMenuItem4.text"));
        jMenuItem4.setName("jMenuItem4");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        registroMenu.add(jMenuItem4);
        jMenuItem8.setText(resourceMap.getString("jMenuItem8.text"));
        jMenuItem8.setName("jMenuItem8");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        registroMenu.add(jMenuItem8);
        menuBar.add(registroMenu);
        informesMenu.setText(resourceMap.getString("informesMenu.text"));
        informesMenu.setName("informesMenu");
        informeAlumnoMenuItem.setText(resourceMap.getString("informeAlumnoMenuItem.text"));
        informeAlumnoMenuItem.setName("informeAlumnoMenuItem");
        informeAlumnoMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                informeAlumnoMenuItemActionPerformed(evt);
            }
        });
        informesMenu.add(informeAlumnoMenuItem);
        informeCursoMenuItem.setText(resourceMap.getString("informeCursoMenuItem.text"));
        informeCursoMenuItem.setName("informeCursoMenuItem");
        informeCursoMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                informeCursoMenuItemActionPerformed(evt);
            }
        });
        informesMenu.add(informeCursoMenuItem);
        menuBar.add(informesMenu);
        helpMenu.setText(resourceMap.getString("helpMenu.text"));
        helpMenu.setName("helpMenu");
        aboutMenuItem.setAction(actionMap.get("showAboutBox"));
        aboutMenuItem.setName("aboutMenuItem");
        helpMenu.add(aboutMenuItem);
        menuBar.add(helpMenu);
        statusPanel.setName("statusPanel");
        statusMessageLabel.setName("statusMessageLabel");
        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel");
        progressBar.setName("progressBar");
        org.jdesktop.layout.GroupLayout statusPanelLayout = new org.jdesktop.layout.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(statusPanelSeparator, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE).add(statusPanelLayout.createSequentialGroup().addContainerGap().add(statusMessageLabel).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 614, Short.MAX_VALUE).add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(statusAnimationLabel).addContainerGap()));
        statusPanelLayout.setVerticalGroup(statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(statusPanelLayout.createSequentialGroup().add(statusPanelSeparator, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(statusMessageLabel).add(statusAnimationLabel).add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(3, 3, 3)));
        mainPanel.setMinimumSize(new java.awt.Dimension(400, 300));
        mainPanel.setName("mainPanel");
        mainPanel.setPreferredSize(new java.awt.Dimension(800, 400));
        jLabel1.setIcon(resourceMap.getIcon("jLabel1.icon"));
        jLabel1.setText(resourceMap.getString("jLabel1.text"));
        jLabel1.setName("jLabel1");
        jLabel1.setBounds(340, 230, 96, 83);
        mainPanel.add(jLabel1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLabel2.setFont(resourceMap.getFont("jLabel2.font"));
        jLabel2.setText(resourceMap.getString("jLabel2.text"));
        jLabel2.setName("jLabel2");
        jLabel2.setBounds(360, 320, 70, 14);
        mainPanel.add(jLabel2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {
        ColegiosAdminView ca = new ColegiosAdminView();
        mainPanel.add(ca);
        ca.setEnabled(true);
        ca.setVisible(true);
    }

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {
        CursosAdminView ca = new CursosAdminView();
        mainPanel.add(ca);
        ca.setEnabled(true);
        ca.setVisible(true);
    }

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {
        AlumnosAdminView ca = new AlumnosAdminView();
        mainPanel.add(ca);
        ca.setEnabled(true);
        ca.setVisible(true);
    }

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {
        AsignaturasAdminView ca = new AsignaturasAdminView();
        mainPanel.add(ca);
        ca.setEnabled(true);
        ca.setVisible(true);
    }

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {
        NotasAdminView ca = new NotasAdminView();
        mainPanel.add(ca);
        ca.setEnabled(true);
        ca.setVisible(true);
    }

    private void informeCursoMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        ListadoAlumnosView lav = new ListadoAlumnosView();
        mainPanel.add(lav);
        lav.setEnabled(true);
        lav.setVisible(true);
    }

    private void informeAlumnoMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        InformeDeNotasView idnv = new InformeDeNotasView();
        mainPanel.add(idnv);
        idnv.setEnabled(true);
        idnv.setVisible(true);
    }

    private void configuracionMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        ConfiguracionView cv = new ConfiguracionView();
        mainPanel.add(cv);
        cv.setEnabled(true);
        cv.setVisible(true);
    }

    private java.util.List<jschoolmanagerportable.model.Alumno> alumnoList;

    private javax.persistence.Query alumnoQuery;

    private java.util.List<jschoolmanagerportable.model.Asignatura> asignaturaList;

    private javax.persistence.Query asignaturaQuery;

    private javax.swing.ButtonGroup buttonGroup1;

    private java.util.List<jschoolmanagerportable.model.Colegio> colegioList;

    private javax.persistence.Query colegioQuery;

    private javax.swing.JMenuItem configuracionMenuItem;

    private java.util.List<jschoolmanagerportable.model.Curso> cursoList;

    private javax.persistence.Query cursoQuery;

    private javax.persistence.EntityManager entityManager;

    private javax.swing.JMenuItem informeAlumnoMenuItem;

    private javax.swing.JMenuItem informeCursoMenuItem;

    private javax.swing.JMenu informesMenu;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JMenuItem jMenuItem1;

    private javax.swing.JMenuItem jMenuItem2;

    private javax.swing.JMenuItem jMenuItem3;

    private javax.swing.JMenuItem jMenuItem4;

    private javax.swing.JMenuItem jMenuItem8;

    private javax.swing.JDesktopPane mainPanel;

    private javax.swing.JMenuBar menuBar;

    private javax.swing.JProgressBar progressBar;

    private javax.swing.JMenu registroMenu;

    private javax.swing.JLabel statusAnimationLabel;

    private javax.swing.JLabel statusMessageLabel;

    private javax.swing.JPanel statusPanel;

    private final Timer messageTimer;

    private final Timer busyIconTimer;

    private final Icon idleIcon;

    private final Icon[] busyIcons = new Icon[15];

    private int busyIconIndex = 0;

    private JDialog aboutBox;
}
