package testes;

import br.ufpe.cin.ontocompo.infra.extractor.IExtractor;
import testes.ManageOWL;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.model.OWLEntity;
import org.semanticweb.owl.model.OWLObjectProperty;

/**
 *
 * @author  Camila Bezerra
 */
public class Main1 extends javax.swing.JFrame {

    private ManageOWL manageOWL;

    private OWLEntity selectedEntity;

    private TreeModel classesTreeModel;

    private List<OWLEntity> selectedClassesList;

    private List<OWLEntity> selectedPropertiesList;

    private List<OWLEntity> selectedSuperClassesList;

    private List<OWLEntity> selectedSubClassesList;

    /** Creates new form Teste */
    public Main1() {
        initComponents();
        selectedClassesList = new ArrayList();
        selectedPropertiesList = new ArrayList();
        selectedSubClassesList = new ArrayList();
        selectedSuperClassesList = new ArrayList();
        classesTree.setModel(createTree());
    }

    private void initComponents() {
        jTabbedPane4 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        panelClasses = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listClasses = new javax.swing.JList();
        buttonAddClasses = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        listImportClasses = new javax.swing.JList();
        jScrollPane6 = new javax.swing.JScrollPane();
        textAreaCommentClasses = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        buttonDelClasses = new javax.swing.JButton();
        label1 = new java.awt.Label();
        jScrollPane8 = new javax.swing.JScrollPane();
        listSuperClasses = new javax.swing.JList();
        jScrollPane9 = new javax.swing.JScrollPane();
        listImportSuperClasses = new javax.swing.JList();
        jLabel7 = new javax.swing.JLabel();
        buttonAddSuperClasses = new javax.swing.JButton();
        buttonDelSuperClasses = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        listSubClasses = new javax.swing.JList();
        jLabel9 = new javax.swing.JLabel();
        buttonAddSubClasses = new javax.swing.JButton();
        buttonDelSubClasses = new javax.swing.JButton();
        jScrollPane11 = new javax.swing.JScrollPane();
        listImportSubClasses = new javax.swing.JList();
        jLabel10 = new javax.swing.JLabel();
        panelProperties = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        listProperties = new javax.swing.JList();
        buttonAddProperty = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        listImportProperties = new javax.swing.JList();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        textAreaCommentsProperties = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        buttonDelProperty = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        classesTree = new javax.swing.JTree();
        jMenuBar3 = new javax.swing.JMenuBar();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItemOpen = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("OntoCompo");
        jTabbedPane4.setFont(new java.awt.Font("Tahoma", 0, 12));
        panelClasses.setBorder(javax.swing.BorderFactory.createTitledBorder("Classes"));
        listClasses.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listClassesValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(listClasses);
        buttonAddClasses.setText(">>");
        buttonAddClasses.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAddClassesActionPerformed(evt);
            }
        });
        jScrollPane2.setViewportView(listImportClasses);
        textAreaCommentClasses.setColumns(20);
        textAreaCommentClasses.setRows(5);
        jScrollPane6.setViewportView(textAreaCommentClasses);
        jLabel3.setFont(new java.awt.Font("Dialog", 1, 11));
        jLabel3.setForeground(new java.awt.Color(0, 0, 255));
        jLabel3.setText("Comments");
        jLabel5.setFont(new java.awt.Font("Dialog", 1, 11));
        jLabel5.setForeground(new java.awt.Color(0, 0, 255));
        jLabel5.setText("Classes");
        buttonDelClasses.setText("<<");
        buttonDelClasses.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDelClassesActionPerformed(evt);
            }
        });
        label1.setFont(new java.awt.Font("Dialog", 1, 11));
        label1.setForeground(new java.awt.Color(0, 0, 255));
        label1.setText("Imported classes");
        listSuperClasses.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listSuperClassesValueChanged(evt);
            }
        });
        jScrollPane8.setViewportView(listSuperClasses);
        jScrollPane9.setViewportView(listImportSuperClasses);
        jLabel7.setFont(new java.awt.Font("Dialog", 1, 11));
        jLabel7.setForeground(new java.awt.Color(0, 0, 255));
        jLabel7.setText("SuperClasses");
        buttonAddSuperClasses.setText(">>");
        buttonAddSuperClasses.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAddSuperClassesActionPerformed(evt);
            }
        });
        buttonDelSuperClasses.setText("<<");
        buttonDelSuperClasses.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDelSuperClassesActionPerformed(evt);
            }
        });
        jLabel8.setFont(new java.awt.Font("Dialog", 1, 11));
        jLabel8.setForeground(new java.awt.Color(0, 0, 255));
        jLabel8.setText("Imported superclasses");
        listSubClasses.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listSubClassesValueChanged(evt);
            }
        });
        jScrollPane10.setViewportView(listSubClasses);
        jLabel9.setFont(new java.awt.Font("Dialog", 1, 11));
        jLabel9.setForeground(new java.awt.Color(0, 0, 255));
        jLabel9.setText("SubClasses");
        buttonAddSubClasses.setText(">>");
        buttonAddSubClasses.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAddSubClassesActionPerformed(evt);
            }
        });
        buttonDelSubClasses.setText("<<");
        buttonDelSubClasses.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDelSubClassesActionPerformed(evt);
            }
        });
        jScrollPane11.setViewportView(listImportSubClasses);
        jLabel10.setFont(new java.awt.Font("Dialog", 1, 11));
        jLabel10.setForeground(new java.awt.Color(0, 0, 255));
        jLabel10.setText("Imported subclasses");
        javax.swing.GroupLayout panelClassesLayout = new javax.swing.GroupLayout(panelClasses);
        panelClasses.setLayout(panelClassesLayout);
        panelClassesLayout.setHorizontalGroup(panelClassesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelClassesLayout.createSequentialGroup().addGroup(panelClassesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelClassesLayout.createSequentialGroup().addGap(19, 19, 19).addComponent(jLabel3)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelClassesLayout.createSequentialGroup().addContainerGap().addGroup(panelClassesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelClassesLayout.createSequentialGroup().addComponent(jLabel7).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 409, Short.MAX_VALUE)).addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelClassesLayout.createSequentialGroup().addGroup(panelClassesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelClassesLayout.createSequentialGroup().addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(panelClassesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(buttonDelSubClasses).addComponent(buttonAddSubClasses)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(panelClassesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelClassesLayout.createSequentialGroup().addComponent(jLabel10).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 97, Short.MAX_VALUE)).addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE))).addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelClassesLayout.createSequentialGroup().addGroup(panelClassesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelClassesLayout.createSequentialGroup().addGroup(panelClassesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel5).addGroup(panelClassesLayout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(panelClassesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(buttonAddClasses).addComponent(buttonDelClasses))).addGroup(panelClassesLayout.createSequentialGroup().addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(panelClassesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(buttonDelSuperClasses).addComponent(buttonAddSuperClasses)))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(panelClassesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel8)))).addGap(22, 22, 22))))).addContainerGap()));
        panelClassesLayout.setVerticalGroup(panelClassesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelClassesLayout.createSequentialGroup().addGroup(panelClassesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelClassesLayout.createSequentialGroup().addComponent(jLabel5).addGroup(panelClassesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelClassesLayout.createSequentialGroup().addGap(27, 27, 27).addComponent(buttonAddClasses).addGap(18, 18, 18).addComponent(buttonDelClasses)).addGroup(panelClassesLayout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(panelClassesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING))))).addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(panelClassesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelClassesLayout.createSequentialGroup().addGap(22, 22, 22).addGroup(panelClassesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel7).addComponent(jLabel8)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(panelClassesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jScrollPane9).addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE))).addGroup(panelClassesLayout.createSequentialGroup().addGap(68, 68, 68).addComponent(buttonAddSuperClasses).addGap(20, 20, 20).addComponent(buttonDelSuperClasses))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE).addGroup(panelClassesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelClassesLayout.createSequentialGroup().addGroup(panelClassesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel9).addComponent(jLabel10)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(panelClassesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE).addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))).addGroup(panelClassesLayout.createSequentialGroup().addGap(46, 46, 46).addComponent(buttonAddSubClasses).addGap(20, 20, 20).addComponent(buttonDelSubClasses))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        panelProperties.setBorder(javax.swing.BorderFactory.createTitledBorder("Properties"));
        listProperties.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listPropertiesValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(listProperties);
        buttonAddProperty.setText(">>");
        buttonAddProperty.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAddPropertyActionPerformed(evt);
            }
        });
        jScrollPane5.setViewportView(listImportProperties);
        jLabel2.setFont(new java.awt.Font("Dialog", 1, 11));
        jLabel2.setForeground(new java.awt.Color(0, 0, 255));
        jLabel2.setText("Imported properties");
        jLabel4.setFont(new java.awt.Font("Dialog", 1, 11));
        jLabel4.setForeground(new java.awt.Color(0, 0, 255));
        jLabel4.setText("Comments");
        textAreaCommentsProperties.setColumns(20);
        textAreaCommentsProperties.setRows(5);
        jScrollPane7.setViewportView(textAreaCommentsProperties);
        jLabel6.setFont(new java.awt.Font("Dialog", 1, 11));
        jLabel6.setForeground(new java.awt.Color(0, 0, 255));
        jLabel6.setText("Properties");
        buttonDelProperty.setText("<<");
        buttonDelProperty.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDelPropertyActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout panelPropertiesLayout = new javax.swing.GroupLayout(panelProperties);
        panelProperties.setLayout(panelPropertiesLayout);
        panelPropertiesLayout.setHorizontalGroup(panelPropertiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelPropertiesLayout.createSequentialGroup().addContainerGap().addGroup(panelPropertiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addGroup(panelPropertiesLayout.createSequentialGroup().addGroup(panelPropertiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel6).addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(panelPropertiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(buttonAddProperty).addComponent(buttonDelProperty)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(panelPropertiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel2).addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))).addComponent(jLabel4).addComponent(jScrollPane7)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        panelPropertiesLayout.setVerticalGroup(panelPropertiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelPropertiesLayout.createSequentialGroup().addGroup(panelPropertiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelPropertiesLayout.createSequentialGroup().addGroup(panelPropertiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel6).addComponent(jLabel2)).addGap(6, 6, 6).addGroup(panelPropertiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelPropertiesLayout.createSequentialGroup().addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE).addComponent(jLabel4)).addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addGroup(panelPropertiesLayout.createSequentialGroup().addGap(47, 47, 47).addComponent(buttonAddProperty).addGap(18, 18, 18).addComponent(buttonDelProperty))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(25, 25, 25)));
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(panelClasses, javax.swing.GroupLayout.PREFERRED_SIZE, 521, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(panelProperties, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(panelProperties, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(panelClasses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jTabbedPane4.addTab("Import entities", jPanel1);
        jScrollPane4.setViewportView(classesTree);
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(884, Short.MAX_VALUE)));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 696, Short.MAX_VALUE));
        jTabbedPane4.addTab("Content", jPanel2);
        jMenu5.setText("File");
        jMenu5.setFont(new java.awt.Font("Tahoma", 0, 12));
        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, 0));
        jMenuItem1.setIcon(new javax.swing.ImageIcon("F:\\Mestrado\\OntoCompo\\OntoCompo\\resources\\file.open.gif"));
        jMenuItem1.setText("open ontology");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem1);
        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, 0));
        jMenuItem2.setIcon(new javax.swing.ImageIcon("F:\\Mestrado\\OntoCompo\\OntoCompo\\resources\\module.gif"));
        jMenuItem2.setText("new module");
        jMenu5.add(jMenuItem2);
        jMenuBar3.add(jMenu5);
        jMenu6.setText("Edit");
        jMenu6.setFont(new java.awt.Font("Tahoma", 0, 12));
        jMenuBar3.add(jMenu6);
        jMenu1.setText("File");
        jMenuItemOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemOpen.setIcon(new javax.swing.ImageIcon("F:\\Mestrado\\OntoCompo\\OntoCompo\\resources\\file.open.gif"));
        jMenuItemOpen.setText("open ontology");
        jMenuItemOpen.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOpenActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemOpen);
        jMenuBar1.add(jMenu1);
        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);
        setJMenuBar(jMenuBar1);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTabbedPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 1083, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jTabbedPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 725, Short.MAX_VALUE)));
        pack();
    }

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jMenuItemOpenActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser fc = new JFileChooser(new File("F://Mestrado//OntoCompo//OntoCompo"));
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fc.showOpenDialog(this);
        File file = fc.getSelectedFile();
        URI uri = file.toURI();
        manageOWL = new ManageOWL(uri);
        listClasses.setListData(manageOWL.getClasses().toArray());
        List list = new ArrayList();
        for (Iterator it = manageOWL.getDataProperties().iterator(); it.hasNext(); ) {
            OWLDataProperty object = (OWLDataProperty) it.next();
            list.add(object);
        }
        for (Iterator it = manageOWL.getObjectProperties().iterator(); it.hasNext(); ) {
            OWLObjectProperty object = (OWLObjectProperty) it.next();
            list.add(object);
        }
        listProperties.setListData(list.toArray());
    }

    private void listClassesValueChanged(javax.swing.event.ListSelectionEvent evt) {
        selectedEntity = (OWLEntity) listClasses.getSelectedValue();
        String text = manageOWL.getComments(selectedEntity);
        textAreaCommentClasses.setText(text);
        listSuperClasses.setListData(manageOWL.getSuperClasses((OWLClass) selectedEntity).toArray());
        listSubClasses.setListData(manageOWL.getSubClasses((OWLClass) selectedEntity).toArray());
    }

    private void listPropertiesValueChanged(javax.swing.event.ListSelectionEvent evt) {
        selectedEntity = (OWLEntity) listProperties.getSelectedValue();
        String text = manageOWL.getComments(selectedEntity);
        textAreaCommentsProperties.setText(text);
    }

    private void buttonAddClassesActionPerformed(java.awt.event.ActionEvent evt) {
        if (selectedEntity != null) {
            selectedClassesList.add(selectedEntity);
            listImportClasses.setListData(selectedClassesList.toArray());
            addNode(selectedEntity.toString());
        }
    }

    private void buttonAddPropertyActionPerformed(java.awt.event.ActionEvent evt) {
        if (selectedEntity != null) {
            selectedPropertiesList.add(selectedEntity);
            listImportProperties.setListData(selectedPropertiesList.toArray());
        }
    }

    private void buttonDelPropertyActionPerformed(java.awt.event.ActionEvent evt) {
        Object value = listImportProperties.getSelectedValue();
        if (value == null) {
            JOptionPane.showMessageDialog(null, "No selected entity!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        selectedPropertiesList.remove(listImportProperties.getSelectedValue());
        listImportProperties.setListData(selectedPropertiesList.toArray());
    }

    private void buttonDelClassesActionPerformed(java.awt.event.ActionEvent evt) {
        Object value = listImportClasses.getSelectedValue();
        if (value == null) {
            JOptionPane.showMessageDialog(null, "No selected entity!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        selectedClassesList.remove(listImportClasses.getSelectedValue());
        listImportClasses.setListData(selectedClassesList.toArray());
    }

    private void listSuperClassesValueChanged(javax.swing.event.ListSelectionEvent evt) {
        selectedEntity = (OWLClass) listSuperClasses.getSelectedValue();
        String text = manageOWL.getComments(selectedEntity);
        textAreaCommentClasses.setText(text);
    }

    private void buttonAddSuperClassesActionPerformed(java.awt.event.ActionEvent evt) {
        if (selectedEntity != null) {
            selectedSuperClassesList.add(selectedEntity);
            listImportSuperClasses.setListData(selectedSuperClassesList.toArray());
        }
    }

    private void buttonDelSuperClassesActionPerformed(java.awt.event.ActionEvent evt) {
        Object value = listImportSuperClasses.getSelectedValue();
        if (value == null) {
            JOptionPane.showMessageDialog(null, "No selected entity!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        selectedSuperClassesList.remove(listImportSuperClasses.getSelectedValue());
        listImportSuperClasses.setListData(selectedSuperClassesList.toArray());
    }

    private void listSubClassesValueChanged(javax.swing.event.ListSelectionEvent evt) {
        selectedEntity = (OWLClass) listSubClasses.getSelectedValue();
        String text = manageOWL.getComments(selectedEntity);
        textAreaCommentClasses.setText(text);
    }

    private void buttonAddSubClassesActionPerformed(java.awt.event.ActionEvent evt) {
        if (selectedEntity != null) {
            selectedSubClassesList.add(selectedEntity);
            listImportSubClasses.setListData(selectedSubClassesList.toArray());
        }
    }

    private void buttonDelSubClassesActionPerformed(java.awt.event.ActionEvent evt) {
        Object value = listImportSubClasses.getSelectedValue();
        if (value == null) {
            JOptionPane.showMessageDialog(null, "No selected entity!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        selectedSubClassesList.remove(listImportSubClasses.getSelectedValue());
        listImportSubClasses.setListData(selectedSubClassesList.toArray());
    }

    public void addNode(String className) {
        DefaultTreeModel model = (DefaultTreeModel) classesTree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        DefaultMutableTreeNode parent;
        parent = new DefaultMutableTreeNode(className);
        root.add(parent);
        classesTree.setModel(new DefaultTreeModel(root));
    }

    public TreeModel createTree() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
        DefaultMutableTreeNode parent;
        return new DefaultTreeModel(root);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Main1().setVisible(true);
            }
        });
    }

    private javax.swing.JButton buttonAddClasses;

    private javax.swing.JButton buttonAddProperty;

    private javax.swing.JButton buttonAddSubClasses;

    private javax.swing.JButton buttonAddSuperClasses;

    private javax.swing.JButton buttonDelClasses;

    private javax.swing.JButton buttonDelProperty;

    private javax.swing.JButton buttonDelSubClasses;

    private javax.swing.JButton buttonDelSuperClasses;

    private javax.swing.JTree classesTree;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JMenu jMenu1;

    private javax.swing.JMenu jMenu2;

    private javax.swing.JMenu jMenu5;

    private javax.swing.JMenu jMenu6;

    private javax.swing.JMenuBar jMenuBar1;

    private javax.swing.JMenuBar jMenuBar3;

    private javax.swing.JMenuItem jMenuItem1;

    private javax.swing.JMenuItem jMenuItem2;

    private javax.swing.JMenuItem jMenuItemOpen;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane10;

    private javax.swing.JScrollPane jScrollPane11;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JScrollPane jScrollPane3;

    private javax.swing.JScrollPane jScrollPane4;

    private javax.swing.JScrollPane jScrollPane5;

    private javax.swing.JScrollPane jScrollPane6;

    private javax.swing.JScrollPane jScrollPane7;

    private javax.swing.JScrollPane jScrollPane8;

    private javax.swing.JScrollPane jScrollPane9;

    private javax.swing.JTabbedPane jTabbedPane4;

    private java.awt.Label label1;

    private javax.swing.JList listClasses;

    private javax.swing.JList listImportClasses;

    private javax.swing.JList listImportProperties;

    private javax.swing.JList listImportSubClasses;

    private javax.swing.JList listImportSuperClasses;

    private javax.swing.JList listProperties;

    private javax.swing.JList listSubClasses;

    private javax.swing.JList listSuperClasses;

    private javax.swing.JPanel panelClasses;

    private javax.swing.JPanel panelProperties;

    private javax.swing.JTextArea textAreaCommentClasses;

    private javax.swing.JTextArea textAreaCommentsProperties;
}
