package Maquettes_Flo_Gilles;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle;
import javax.swing.ListModel;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import Maquette_Mathieu_Ivan.DisplayTeacherServices;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class RessourcePanelUI extends javax.swing.JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private Font subtitleFont;

    private JTextField acroFormaTextField;

    private JComboBox statutTeacherComboBox;

    private JComboBox titleTeacherComboBox;

    private JLabel titleTeacherLabel;

    private JComboBox anneeNaisComboBox;

    private JComboBox moisNaisComboBox;

    private JComboBox jourNaisComboBox;

    private JPanel datNaisPanel;

    private JButton teachingManagTeacherButton;

    private JLabel hTeacherLabel;

    private JTextField hTeacherTextField;

    private JLabel hObliTeacherLabel;

    private JTextField typGrpLibelleTextField;

    private JLabel typGrpLibelleLabel;

    private JList typGrpList;

    private JScrollPane typGrpScrollPane;

    private JLabel acroFormaLabel;

    private JLabel intitFormaLabel;

    private JLabel descFormaLabel;

    private JTextField intitFormaTextField;

    private JButton addFormaButton;

    private JButton delFormaButton;

    private JButton goSYManagButton;

    private JPanel etudNorthPanel;

    private JList studyTypeList;

    private JScrollPane studyTypeScrollPane;

    private JLabel hStudyTypeLabel;

    private JButton modStudyTypeButton;

    private JLabel acroStudyTypeLabel;

    private JLabel intitStudyTypeLabel;

    private JLabel equivTDHStudyTypeLabel;

    private JTextField intitStudyTypeTextField;

    private JTextField acroStudyTypeTextField;

    private JTextField equivStudyTypeTextField;

    private JButton delStudyTypeButton;

    private JButton addStudyTypeButton;

    private JPanel etudCenterPanel;

    private JPanel etudSouthPanel;

    private JList roomList;

    private JScrollPane roomScrollPane;

    private JButton modRoomButton;

    private JButton supRoomButton;

    private JButton addRoomButton;

    private JLabel nameRoomLabel;

    private JLabel aliasTeacherLabel;

    private JPanel shadowPanel;

    private JPanel jPanel1;

    private JPanel southRHPanel;

    private JTextField libellGroupTypeTextField;

    private JLabel libellGroupTypeLabel;

    private JList groupTypeList;

    private JScrollPane groupTypeScrollPane;

    private JPanel groupTypePanel;

    private JButton delGroupTypeButton;

    private JButton modGroupTypeButton;

    private JButton addGroupTypeButton;

    private JPanel studyTypePanel;

    private JPanel jPanel9;

    private JPanel jPanel8;

    private JPanel formaPanel;

    private JPanel centerEtudPanel;

    private JTextField aliasTeacherTextField;

    private JTextPane formaTextPane;

    private JScrollPane descScrollPane;

    private JList formaList;

    private JPanel northRHPanel;

    private JPanel centerRhPanel;

    private JScrollPane formaScrollPane;

    private JButton modFormaButton;

    private JPanel etudPanel;

    private JScrollPane etudScrollPane;

    private JTextField nameRoomTextField;

    private JTextField capacityRoomTextField;

    private JLabel capacityRoomLabel;

    private JPanel roomPanel;

    private JTextField cityTeacherTextField;

    private JTextField pcTeacherTextField;

    private JTextField adrTeacherTextField;

    private JButton suppTeacherButton;

    private JButton modTeacherButton;

    private JButton addTeacherButton;

    private JTextField firstnameTeacherTextField;

    private JTextField nameTeacherTextField;

    private JLabel cityTeacherLabel;

    private JLabel pcTeacherLabel;

    private JLabel adrTeacherLabel;

    private JLabel statusTeacherLabel;

    private JLabel firstnameTeacherLabel;

    private JLabel nameTeacherLabel;

    private JList teacherList;

    private JScrollPane teacherScrollPane;

    private JPanel teacherManagPanel;

    private JPanel rhPanel;

    private JScrollPane rhScrollPane;

    private JButton ajoutButton;

    private JTabbedPane ressTabbedPane1;

    {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RessourcePanelUI() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            this.subtitleFont = new Font(null, Font.BOLD, 14);
            BorderLayout thisLayout = new BorderLayout();
            this.setLayout(thisLayout);
            this.setPreferredSize(new java.awt.Dimension(779, 534));
            {
                ressTabbedPane1 = new JTabbedPane();
                this.add(ressTabbedPane1, BorderLayout.CENTER);
                ressTabbedPane1.setPreferredSize(new java.awt.Dimension(779, 543));
                {
                    etudScrollPane = new JScrollPane();
                    ScrollPaneLayout etudScrollPaneLayout = new ScrollPaneLayout();
                    etudScrollPane.setLayout(etudScrollPaneLayout);
                    ressTabbedPane1.addTab("Etudes et groupes", null, etudScrollPane, null);
                    etudScrollPane.setSize(new java.awt.Dimension(770, 497));
                    {
                        etudPanel = new JPanel();
                        BorderLayout etudPanelLayout = new BorderLayout();
                        etudScrollPane.setViewportView(etudPanel);
                        etudPanel.setLayout(etudPanelLayout);
                        etudPanel.setPreferredSize(new java.awt.Dimension(748, 481));
                        {
                            centerEtudPanel = new JPanel();
                            BorderLayout jPanel1Layout = new BorderLayout();
                            jPanel1Layout.setVgap(5);
                            etudPanel.add(centerEtudPanel, BorderLayout.CENTER);
                            centerEtudPanel.setLayout(jPanel1Layout);
                            centerEtudPanel.setPreferredSize(new java.awt.Dimension(770, 473));
                            {
                                etudNorthPanel = new JPanel();
                                BorderLayout jPanel4Layout = new BorderLayout();
                                jPanel4Layout.setVgap(20);
                                centerEtudPanel.add(etudNorthPanel, BorderLayout.NORTH);
                                etudNorthPanel.setBackground(new java.awt.Color(237, 231, 241));
                                etudNorthPanel.setLayout(jPanel4Layout);
                                etudNorthPanel.setPreferredSize(new java.awt.Dimension(772, 187));
                                etudNorthPanel.setBounds(27, 30, 707, 166);
                                etudNorthPanel.setBorder(BorderFactory.createTitledBorder(null, "Gestion des formations", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
                                {
                                    formaPanel = new JPanel();
                                    GroupLayout jPanel5Layout = new GroupLayout((JComponent) formaPanel);
                                    formaPanel.setSize(760, 122);
                                    etudNorthPanel.add(formaPanel, BorderLayout.CENTER);
                                    formaPanel.setLayout(jPanel5Layout);
                                    formaPanel.setAutoscrolls(true);
                                    formaPanel.setBackground(new java.awt.Color(237, 231, 241));
                                    {
                                        goSYManagButton = new JButton();
                                        goSYManagButton.setText("Gestion des ann�es d'�tude >>");
                                        goSYManagButton.setBounds(285, 135, 194, 21);
                                        goSYManagButton.addActionListener(new ActionListener() {

                                            public void actionPerformed(ActionEvent arg0) {
                                                StudyYearJDialog dialGestionAE = new StudyYearJDialog(null);
                                                dialGestionAE.setVisible(true);
                                            }
                                        });
                                    }
                                    {
                                        delFormaButton = new JButton();
                                        delFormaButton.setText("Supprimer");
                                        delFormaButton.setBounds(169, 135, 105, 23);
                                    }
                                    {
                                        addFormaButton = new JButton();
                                        addFormaButton.setText("Ajouter");
                                        addFormaButton.setBounds(169, 98, 106, 23);
                                    }
                                    {
                                        intitFormaTextField = new JTextField();
                                        intitFormaTextField.setText("Informatique et Gestion");
                                        intitFormaTextField.setBounds(249, 46, 134, 25);
                                    }
                                    {
                                        acroFormaTextField = new JTextField();
                                        acroFormaTextField.setText("IG");
                                        acroFormaTextField.setBounds(249, 16, 134, 24);
                                    }
                                    {
                                        descFormaLabel = new JLabel();
                                        descFormaLabel.setText("Description");
                                        descFormaLabel.setBounds(419, 13, 60, 17);
                                    }
                                    {
                                        intitFormaLabel = new JLabel();
                                        intitFormaLabel.setText("Intitul�");
                                        intitFormaLabel.setBounds(173, 51, 34, 17);
                                    }
                                    {
                                        acroFormaLabel = new JLabel();
                                        acroFormaLabel.setText("Acronyme");
                                        acroFormaLabel.setBounds(173, 18, 57, 19);
                                    }
                                    {
                                        modFormaButton = new JButton();
                                        modFormaButton.setText("Modifier");
                                        modFormaButton.setBounds(285, 98, 108, 23);
                                    }
                                    {
                                        formaScrollPane = new JScrollPane();
                                        formaScrollPane.setBounds(17, 12, 131, 146);
                                        {
                                            ListModel jList2Model = new DefaultComboBoxModel(new String[] { "IG", "MEA", "STE", "STIA" });
                                            formaList = new JList();
                                            formaScrollPane.setViewportView(formaList);
                                            formaList.setModel(jList2Model);
                                            formaList.setBounds(17, 12, 131, 146);
                                        }
                                    }
                                    {
                                        descScrollPane = new JScrollPane();
                                        descScrollPane.setBounds(419, 36, 331, 85);
                                        {
                                            formaTextPane = new JTextPane();
                                            descScrollPane.setViewportView(formaTextPane);
                                            formaTextPane.setText("Alii nullo quaerente vultus severitate adsimulata patrimonia sua in inmensum extollunt, cultorum ut puta feracium multiplicantes annuos fructus, quae a primo ad ultimum solem se abunde iactitant possidere, ignorantes profecto maiores suos, per quos ita magnitudo Romana porrigitur, non divitiis eluxisse sed per bella saevissima, nec opibus nec victu nec indumentorum vilitate gregariis militibus discrepantes opposita cuncta superasse virtute.");
                                            formaTextPane.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
                                            formaTextPane.setBounds(419, 36, 253, 84);
                                            formaTextPane.setPreferredSize(new java.awt.Dimension(313, 118));
                                        }
                                    }
                                    jPanel5Layout.setHorizontalGroup(jPanel5Layout.createSequentialGroup().addContainerGap().addComponent(formaScrollPane, GroupLayout.PREFERRED_SIZE, 131, GroupLayout.PREFERRED_SIZE).addGap(21).addGroup(jPanel5Layout.createParallelGroup().addGroup(jPanel5Layout.createSequentialGroup().addGroup(jPanel5Layout.createParallelGroup().addComponent(acroFormaLabel, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE).addGroup(GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup().addComponent(intitFormaLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addGap(23))).addGap(19).addGroup(jPanel5Layout.createParallelGroup().addGroup(jPanel5Layout.createSequentialGroup().addComponent(acroFormaTextField, GroupLayout.PREFERRED_SIZE, 134, GroupLayout.PREFERRED_SIZE)).addGroup(jPanel5Layout.createSequentialGroup().addComponent(intitFormaTextField, GroupLayout.PREFERRED_SIZE, 134, GroupLayout.PREFERRED_SIZE))).addGap(0, 354, Short.MAX_VALUE)).addGroup(jPanel5Layout.createSequentialGroup().addGroup(jPanel5Layout.createParallelGroup().addComponent(addFormaButton, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE).addComponent(delFormaButton, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel5Layout.createParallelGroup().addGroup(GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup().addComponent(modFormaButton, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE).addGap(26).addGroup(jPanel5Layout.createParallelGroup().addGroup(GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup().addComponent(descFormaLabel, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE).addGap(0, 254, Short.MAX_VALUE)).addComponent(descScrollPane, GroupLayout.Alignment.LEADING, 0, 314, Short.MAX_VALUE))).addGroup(GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup().addComponent(goSYManagButton, GroupLayout.PREFERRED_SIZE, 194, GroupLayout.PREFERRED_SIZE).addGap(0, 254, Short.MAX_VALUE))))).addContainerGap());
                                    jPanel5Layout.setVerticalGroup(jPanel5Layout.createSequentialGroup().addContainerGap().addGroup(jPanel5Layout.createParallelGroup().addGroup(jPanel5Layout.createSequentialGroup().addGroup(jPanel5Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(acroFormaTextField, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE).addComponent(acroFormaLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel5Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(intitFormaTextField, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE).addComponent(intitFormaLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)).addGap(27).addGroup(jPanel5Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(modFormaButton, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(addFormaButton, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addGap(37)).addGroup(jPanel5Layout.createParallelGroup().addGroup(GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup().addComponent(descFormaLabel, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(descScrollPane, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE).addGap(11).addGroup(jPanel5Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(delFormaButton, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(goSYManagButton, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))).addGroup(GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup().addComponent(formaScrollPane, 0, 136, Short.MAX_VALUE).addGap(0, 6, GroupLayout.PREFERRED_SIZE)))).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED));
                                }
                            }
                            {
                                etudCenterPanel = new JPanel();
                                BorderLayout jPanel3Layout = new BorderLayout();
                                centerEtudPanel.add(etudCenterPanel, BorderLayout.CENTER);
                                etudCenterPanel.setBackground(new java.awt.Color(233, 236, 254));
                                etudCenterPanel.setBorder(BorderFactory.createTitledBorder(null, "Gestion des types d'enseignement", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11)));
                                etudCenterPanel.setLayout(jPanel3Layout);
                                etudCenterPanel.setPreferredSize(new java.awt.Dimension(770, 167));
                                etudCenterPanel.setBounds(0, 25, 709, 469);
                                {
                                    studyTypePanel = new JPanel();
                                    etudCenterPanel.add(studyTypePanel, BorderLayout.CENTER);
                                    GroupLayout jPanel10Layout = new GroupLayout((JComponent) studyTypePanel);
                                    studyTypePanel.setLayout(jPanel10Layout);
                                    studyTypePanel.setPreferredSize(new java.awt.Dimension(758, 120));
                                    studyTypePanel.setBackground(new java.awt.Color(233, 236, 254));
                                    {
                                        addStudyTypeButton = new JButton();
                                        addStudyTypeButton.setText("Ajouter");
                                        addStudyTypeButton.setBounds(334, 75, 106, 23);
                                    }
                                    {
                                        delStudyTypeButton = new JButton();
                                        delStudyTypeButton.setText("Supprimer");
                                        delStudyTypeButton.setBounds(566, 75, 106, 23);
                                    }
                                    {
                                        equivStudyTypeTextField = new JTextField();
                                        equivStudyTypeTextField.setHorizontalAlignment(SwingConstants.TRAILING);
                                        equivStudyTypeTextField.setText("1,5");
                                        equivStudyTypeTextField.setBounds(309, 45, 38, 20);
                                    }
                                    {
                                        acroStudyTypeTextField = new JTextField();
                                        acroStudyTypeTextField.setText("TP");
                                        acroStudyTypeTextField.setBounds(254, 15, 151, 23);
                                    }
                                    {
                                        intitStudyTypeTextField = new JTextField();
                                        intitStudyTypeTextField.setText("Travaux pratiques");
                                        intitStudyTypeTextField.setBounds(473, 16, 199, 23);
                                    }
                                    {
                                        equivTDHStudyTypeLabel = new JLabel();
                                        equivTDHStudyTypeLabel.setText("Equivalence en heure TD");
                                        equivTDHStudyTypeLabel.setBounds(174, 46, 131, 20);
                                    }
                                    {
                                        intitStudyTypeLabel = new JLabel();
                                        intitStudyTypeLabel.setText("Intitul�");
                                        intitStudyTypeLabel.setBounds(427, 22, 36, 11);
                                    }
                                    {
                                        acroStudyTypeLabel = new JLabel();
                                        acroStudyTypeLabel.setText("Acronyme");
                                        acroStudyTypeLabel.setBounds(174, 19, 55, 15);
                                    }
                                    {
                                        modStudyTypeButton = new JButton();
                                        modStudyTypeButton.setText("Modifier");
                                        modStudyTypeButton.setBounds(450, 75, 106, 23);
                                    }
                                    {
                                        hStudyTypeLabel = new JLabel();
                                        hStudyTypeLabel.setText("h.");
                                        hStudyTypeLabel.setBounds(351, 48, 62, 14);
                                    }
                                    {
                                        studyTypeScrollPane = new JScrollPane();
                                        studyTypeScrollPane.setBounds(20, 11, 128, 88);
                                        {
                                            ListModel jList1Model = new DefaultComboBoxModel(new String[] { "CM", "TP", "TD" });
                                            studyTypeList = new JList();
                                            studyTypeScrollPane.setViewportView(studyTypeList);
                                            studyTypeList.setModel(jList1Model);
                                            studyTypeList.setBounds(20, 11, 128, 88);
                                            studyTypeList.setPreferredSize(new java.awt.Dimension(126, 67));
                                        }
                                    }
                                    jPanel10Layout.setHorizontalGroup(jPanel10Layout.createSequentialGroup().addContainerGap().addComponent(studyTypeScrollPane, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE).addGap(25).addGroup(jPanel10Layout.createParallelGroup().addGroup(GroupLayout.Alignment.LEADING, jPanel10Layout.createSequentialGroup().addComponent(equivTDHStudyTypeLabel, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(equivStudyTypeTextField, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE).addGap(6)).addGroup(GroupLayout.Alignment.LEADING, jPanel10Layout.createSequentialGroup().addComponent(addStudyTypeButton, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(modStudyTypeButton, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE)).addGroup(GroupLayout.Alignment.LEADING, jPanel10Layout.createSequentialGroup().addComponent(acroStudyTypeLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(acroStudyTypeTextField, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE).addGap(51))).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel10Layout.createParallelGroup().addGroup(GroupLayout.Alignment.LEADING, jPanel10Layout.createSequentialGroup().addComponent(hStudyTypeLabel, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE).addGap(0, 166, Short.MAX_VALUE)).addGroup(jPanel10Layout.createSequentialGroup().addPreferredGap(hStudyTypeLabel, delStudyTypeButton, LayoutStyle.ComponentPlacement.INDENT).addGroup(jPanel10Layout.createParallelGroup().addGroup(GroupLayout.Alignment.LEADING, jPanel10Layout.createSequentialGroup().addComponent(delStudyTypeButton, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE).addGap(0, 90, Short.MAX_VALUE)).addGroup(GroupLayout.Alignment.LEADING, jPanel10Layout.createSequentialGroup().addComponent(intitStudyTypeLabel, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(intitStudyTypeTextField, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE).addGap(0, 0, Short.MAX_VALUE))))).addContainerGap(170, 170));
                                    jPanel10Layout.setVerticalGroup(jPanel10Layout.createSequentialGroup().addContainerGap().addGroup(jPanel10Layout.createParallelGroup().addGroup(jPanel10Layout.createSequentialGroup().addGroup(jPanel10Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(intitStudyTypeLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE).addComponent(intitStudyTypeTextField, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE).addComponent(acroStudyTypeLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE).addComponent(acroStudyTypeTextField, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel10Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(equivStudyTypeTextField, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE).addComponent(hStudyTypeLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE).addComponent(equivTDHStudyTypeLabel, GroupLayout.Alignment.BASELINE, 0, 22, Short.MAX_VALUE)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel10Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(delStudyTypeButton, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(addStudyTypeButton, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(modStudyTypeButton, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)).addGroup(jPanel10Layout.createSequentialGroup().addComponent(studyTypeScrollPane, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE).addGap(0, 0, Short.MAX_VALUE))).addContainerGap());
                                }
                            }
                            {
                                etudSouthPanel = new JPanel();
                                BorderLayout jPanel2Layout = new BorderLayout();
                                centerEtudPanel.add(etudSouthPanel, BorderLayout.SOUTH);
                                etudSouthPanel.setBackground(new java.awt.Color(220, 248, 248));
                                etudSouthPanel.setLayout(jPanel2Layout);
                                etudSouthPanel.setPreferredSize(new java.awt.Dimension(770, 114));
                                etudSouthPanel.setBounds(0, 0, 772, 513);
                                etudSouthPanel.setBorder(BorderFactory.createTitledBorder(null, "Gestion des types de groupes", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11)));
                                {
                                    groupTypePanel = new JPanel();
                                    GroupLayout jPanel12Layout = new GroupLayout((JComponent) groupTypePanel);
                                    etudSouthPanel.add(groupTypePanel, BorderLayout.CENTER);
                                    groupTypePanel.setLayout(jPanel12Layout);
                                    groupTypePanel.setPreferredSize(new java.awt.Dimension(758, 88));
                                    groupTypePanel.setBackground(new java.awt.Color(220, 248, 248));
                                    {
                                        groupTypeScrollPane = new JScrollPane();
                                        {
                                            ListModel groupTypeListModel = new DefaultComboBoxModel(new String[] { "Item One", "Item Two" });
                                            groupTypeList = new JList();
                                            BorderLayout addButtonLayout = new BorderLayout();
                                            groupTypeScrollPane.setViewportView(groupTypeList);
                                            groupTypeList.setModel(groupTypeListModel);
                                            groupTypeList.setLayout(null);
                                            groupTypeList.setPreferredSize(new java.awt.Dimension(106, 73));
                                        }
                                    }
                                    {
                                        delGroupTypeButton = new JButton();
                                        BorderLayout delButtonLayout = new BorderLayout();
                                        delGroupTypeButton.setText("Supprimer");
                                        delGroupTypeButton.setLayout(null);
                                    }
                                    {
                                        modGroupTypeButton = new JButton();
                                        BorderLayout modButtonLayout = new BorderLayout();
                                        modGroupTypeButton.setText("Modifier");
                                        modGroupTypeButton.setLayout(null);
                                    }
                                    {
                                        libellGroupTypeLabel = new JLabel();
                                        libellGroupTypeLabel.setText("Libell�");
                                        libellGroupTypeLabel.setLayout(null);
                                    }
                                    {
                                        libellGroupTypeTextField = new JTextField();
                                        libellGroupTypeTextField.setText("Demi");
                                    }
                                    {
                                        addGroupTypeButton = new JButton();
                                        addGroupTypeButton.setText("Ajouter");
                                        addGroupTypeButton.setLayout(null);
                                    }
                                    jPanel12Layout.setHorizontalGroup(jPanel12Layout.createSequentialGroup().addContainerGap().addComponent(groupTypeScrollPane, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE).addGap(22).addGroup(jPanel12Layout.createParallelGroup().addGroup(GroupLayout.Alignment.LEADING, jPanel12Layout.createSequentialGroup().addComponent(addGroupTypeButton, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE).addGap(15).addComponent(modGroupTypeButton, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE)).addGroup(GroupLayout.Alignment.LEADING, jPanel12Layout.createSequentialGroup().addComponent(libellGroupTypeLabel, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(libellGroupTypeTextField, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE).addGap(23))).addGap(15).addComponent(delGroupTypeButton, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE).addContainerGap(260, Short.MAX_VALUE));
                                    jPanel12Layout.setVerticalGroup(jPanel12Layout.createSequentialGroup().addContainerGap().addGroup(jPanel12Layout.createParallelGroup().addGroup(jPanel12Layout.createSequentialGroup().addGroup(jPanel12Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(libellGroupTypeLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE).addComponent(libellGroupTypeTextField, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)).addGap(16).addGroup(jPanel12Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(addGroupTypeButton, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(modGroupTypeButton, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(delGroupTypeButton, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))).addComponent(groupTypeScrollPane, GroupLayout.Alignment.LEADING, 0, 63, Short.MAX_VALUE)).addContainerGap());
                                }
                            }
                        }
                        {
                            jPanel8 = new JPanel();
                            etudPanel.add(jPanel8, BorderLayout.EAST);
                            jPanel8.setLayout(null);
                        }
                        {
                            jPanel9 = new JPanel();
                            etudPanel.add(jPanel9, BorderLayout.WEST);
                            jPanel9.setLayout(null);
                        }
                    }
                }
                {
                    rhScrollPane = new JScrollPane();
                    ressTabbedPane1.addTab("Rh et Locaux", null, rhScrollPane, null);
                    rhScrollPane.setSize(new java.awt.Dimension(770, 505));
                    rhScrollPane.setPreferredSize(new java.awt.Dimension(740, 460));
                    {
                        rhPanel = new JPanel();
                        BorderLayout rhPanelLayout = new BorderLayout();
                        rhScrollPane.setViewportView(rhPanel);
                        rhPanel.setLayout(rhPanelLayout);
                        rhPanel.setPreferredSize(new java.awt.Dimension(757, 481));
                        {
                            centerRhPanel = new JPanel();
                            BorderLayout centerRhPanelLayout = new BorderLayout();
                            centerRhPanelLayout.setVgap(20);
                            rhPanel.add(centerRhPanel, BorderLayout.CENTER);
                            centerRhPanel.setLayout(centerRhPanelLayout);
                            centerRhPanel.setPreferredSize(new java.awt.Dimension(753, 483));
                            {
                                northRHPanel = new JPanel();
                                BorderLayout northRHPanelLayout = new BorderLayout();
                                northRHPanelLayout.setVgap(20);
                                northRHPanel.setLayout(northRHPanelLayout);
                                centerRhPanel.add(northRHPanel, BorderLayout.NORTH);
                                northRHPanel.setBorder(BorderFactory.createTitledBorder(null, "Gestion des enseignants", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11)));
                                northRHPanel.setBackground(new java.awt.Color(255, 247, 234));
                                {
                                    teacherManagPanel = new JPanel();
                                    northRHPanel.add(teacherManagPanel, BorderLayout.CENTER);
                                    GroupLayout jPanel6Layout = new GroupLayout((JComponent) teacherManagPanel);
                                    teacherManagPanel.setLayout(jPanel6Layout);
                                    teacherManagPanel.setBackground(new java.awt.Color(255, 247, 234));
                                    teacherManagPanel.setPreferredSize(new java.awt.Dimension(772, 270));
                                    {
                                        teacherScrollPane = new JScrollPane();
                                        teacherScrollPane.setBounds(29, 24, 164, 223);
                                        {
                                            ListModel jList3Model = new DefaultComboBoxModel(new String[] { "POCHARD Herv�", "STRATULAT Tiberiu" });
                                            teacherList = new JList();
                                            teacherScrollPane.setViewportView(teacherList);
                                            teacherList.setModel(jList3Model);
                                            teacherList.setPreferredSize(new java.awt.Dimension(145, 221));
                                        }
                                    }
                                    {
                                        nameTeacherLabel = new JLabel();
                                        nameTeacherLabel.setText("Nom");
                                        nameTeacherLabel.setBounds(215, 44, 89, 25);
                                    }
                                    {
                                        firstnameTeacherLabel = new JLabel();
                                        firstnameTeacherLabel.setText("Prenom");
                                        firstnameTeacherLabel.setBounds(215, 76, 94, 21);
                                    }
                                    {
                                        statusTeacherLabel = new JLabel();
                                        statusTeacherLabel.setText("Statut");
                                        statusTeacherLabel.setBounds(476, 63, 90, 20);
                                    }
                                    {
                                        adrTeacherLabel = new JLabel();
                                        adrTeacherLabel.setText("Adresse");
                                        adrTeacherLabel.setBounds(215, 172, 78, 22);
                                    }
                                    {
                                        pcTeacherLabel = new JLabel();
                                        pcTeacherLabel.setText("Code Postal");
                                        pcTeacherLabel.setBounds(215, 205, 79, 21);
                                    }
                                    {
                                        cityTeacherLabel = new JLabel();
                                        cityTeacherLabel.setText("Ville");
                                        cityTeacherLabel.setBounds(215, 233, 75, 22);
                                    }
                                    {
                                        nameTeacherTextField = new JTextField();
                                        nameTeacherTextField.setBounds(308, 45, 125, 23);
                                        nameTeacherTextField.setText("POCHARD");
                                    }
                                    {
                                        firstnameTeacherTextField = new JTextField();
                                        firstnameTeacherTextField.setBounds(308, 75, 125, 23);
                                        firstnameTeacherTextField.setText("Herv�");
                                    }
                                    {
                                        addTeacherButton = new JButton();
                                        addTeacherButton.setText("Ajouter");
                                        addTeacherButton.setBounds(486, 142, 106, 23);
                                    }
                                    {
                                        modTeacherButton = new JButton();
                                        modTeacherButton.setText("Modifier");
                                        modTeacherButton.setBounds(486, 171, 106, 23);
                                    }
                                    {
                                        suppTeacherButton = new JButton();
                                        suppTeacherButton.setText("Supprimer");
                                        suppTeacherButton.setBounds(598, 171, 106, 23);
                                    }
                                    {
                                        adrTeacherTextField = new JTextField();
                                        adrTeacherTextField.setBounds(308, 171, 125, 23);
                                        adrTeacherTextField.setText("125 route de Grabel");
                                    }
                                    {
                                        pcTeacherTextField = new JTextField();
                                        pcTeacherTextField.setBounds(309, 202, 125, 23);
                                        pcTeacherTextField.setText("34090");
                                    }
                                    {
                                        cityTeacherTextField = new JTextField();
                                        cityTeacherTextField.setBounds(309, 234, 125, 23);
                                        cityTeacherTextField.setText("MONTPELLIER");
                                    }
                                    {
                                        ComboBoxModel titreComboBoxModel = new DefaultComboBoxModel(new String[] { "ATER", "Professeur", "Ma�tre de conf�rence", "Ajouter/Modifier" });
                                        statutTeacherComboBox = new JComboBox();
                                        statutTeacherComboBox.setModel(titreComboBoxModel);
                                        statutTeacherComboBox.setBounds(557, 63, 125, 23);
                                        statutTeacherComboBox.addActionListener(new ActionListener() {

                                            @Override
                                            public void actionPerformed(ActionEvent arg0) {
                                                if (statutTeacherComboBox.getSelectedItem().equals("Ajouter/Modifier")) {
                                                    JDialogStatuts stats = new JDialogStatuts(null);
                                                    stats.setVisible(true);
                                                }
                                            }
                                        });
                                    }
                                    {
                                        hObliTeacherLabel = new JLabel();
                                        hObliTeacherLabel.setText("Nb heures obligatoires");
                                        hObliTeacherLabel.setBounds(477, 103, 107, 12);
                                    }
                                    {
                                        hTeacherTextField = new JTextField();
                                        hTeacherTextField.setText("192");
                                        hTeacherTextField.setBounds(589, 99, 65, 19);
                                        hTeacherTextField.setHorizontalAlignment(SwingConstants.RIGHT);
                                        hTeacherTextField.setEditable(false);
                                    }
                                    {
                                        hTeacherLabel = new JLabel();
                                        hTeacherLabel.setText("h");
                                        hTeacherLabel.setBounds(658, 103, 30, 12);
                                    }
                                    {
                                        teachingManagTeacherButton = new JButton();
                                        teachingManagTeacherButton.setText("Consultation des enseignements >>");
                                        teachingManagTeacherButton.setBounds(486, 224, 220, 23);
                                        teachingManagTeacherButton.addActionListener(new ActionListener() {

                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                DisplayTeacherServices disTeach = new DisplayTeacherServices("Herv� POCHARD");
                                                disTeach.setVisible(true);
                                            }
                                        });
                                    }
                                    {
                                        datNaisPanel = new JPanel();
                                        datNaisPanel.setBounds(215, 103, 219, 63);
                                        datNaisPanel.setBorder(BorderFactory.createTitledBorder("Date de naissance"));
                                        datNaisPanel.setLayout(null);
                                        datNaisPanel.setBackground(new java.awt.Color(255, 247, 234));
                                        {
                                            ComboBoxModel jourNaisComboBoxModel = new DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "24" });
                                            jourNaisComboBox = new JComboBox();
                                            datNaisPanel.add(jourNaisComboBox);
                                            jourNaisComboBox.setModel(jourNaisComboBoxModel);
                                            jourNaisComboBox.setBounds(11, 22, 48, 23);
                                        }
                                        {
                                            ComboBoxModel moisNaisComboBoxModel = new DefaultComboBoxModel(new String[] { "Janvier", "Fevrier", "Mars", "Avril", "Novembre", "Decembre" });
                                            moisNaisComboBox = new JComboBox();
                                            datNaisPanel.add(moisNaisComboBox);
                                            moisNaisComboBox.setModel(moisNaisComboBoxModel);
                                            moisNaisComboBox.setBounds(65, 22, 77, 23);
                                        }
                                        {
                                            ComboBoxModel anneeNaisComboBoxModel = new DefaultComboBoxModel(new String[] { "1945", "1955", "1983", "2010" });
                                            anneeNaisComboBox = new JComboBox();
                                            datNaisPanel.add(anneeNaisComboBox);
                                            anneeNaisComboBox.setModel(anneeNaisComboBoxModel);
                                            anneeNaisComboBox.setBounds(148, 22, 57, 23);
                                        }
                                    }
                                    {
                                        titleTeacherLabel = new JLabel();
                                        titleTeacherLabel.setText("Titre");
                                        titleTeacherLabel.setBounds(475, 20, 71, 20);
                                    }
                                    {
                                        ComboBoxModel jComboBox1Model = new DefaultComboBoxModel(new String[] { "Madame", "Mademoiselle", "Monsieur", "Docteur", "Professeur" });
                                        titleTeacherComboBox = new JComboBox();
                                        titleTeacherComboBox.setModel(jComboBox1Model);
                                        titleTeacherComboBox.setBounds(557, 19, 125, 23);
                                        titleTeacherComboBox.setEditable(true);
                                    }
                                    {
                                        aliasTeacherLabel = new JLabel();
                                        aliasTeacherLabel.setText("Alias");
                                        aliasTeacherLabel.setBounds(215, 19, 22, 14);
                                    }
                                    {
                                        aliasTeacherTextField = new JTextField();
                                        aliasTeacherTextField.setText("PCH");
                                        aliasTeacherTextField.setBounds(308, 16, 125, 23);
                                    }
                                    jPanel6Layout.setHorizontalGroup(jPanel6Layout.createSequentialGroup().addContainerGap(28, 28).addComponent(teacherScrollPane, GroupLayout.PREFERRED_SIZE, 164, GroupLayout.PREFERRED_SIZE).addGap(22).addGroup(jPanel6Layout.createParallelGroup().addGroup(jPanel6Layout.createSequentialGroup().addGroup(jPanel6Layout.createParallelGroup().addComponent(nameTeacherLabel, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE).addGroup(GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup().addComponent(adrTeacherLabel, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE).addGap(11)).addGroup(GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup().addComponent(pcTeacherLabel, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE).addGap(10)).addGroup(GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup().addComponent(cityTeacherLabel, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE).addGap(14)).addGroup(GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup().addComponent(aliasTeacherLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addGap(67))).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel6Layout.createParallelGroup().addComponent(nameTeacherTextField, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE).addComponent(firstnameTeacherTextField, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE).addComponent(adrTeacherTextField, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE).addComponent(pcTeacherTextField, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE).addComponent(cityTeacherTextField, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE).addComponent(aliasTeacherTextField, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE))).addGroup(GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup().addComponent(firstnameTeacherLabel, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE).addGap(125)).addComponent(datNaisPanel, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 219, GroupLayout.PREFERRED_SIZE)).addGap(41).addGroup(jPanel6Layout.createParallelGroup().addGroup(GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup().addGroup(jPanel6Layout.createParallelGroup().addComponent(hObliTeacherLabel, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addGroup(GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup().addComponent(statusTeacherLabel, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE).addGap(17))).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(hTeacherTextField, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(hTeacherLabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).addGap(0, 20, Short.MAX_VALUE)).addGroup(GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup().addComponent(titleTeacherLabel, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE).addGap(11).addGroup(jPanel6Layout.createParallelGroup().addGroup(jPanel6Layout.createSequentialGroup().addComponent(statutTeacherComboBox, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE)).addGroup(jPanel6Layout.createSequentialGroup().addComponent(titleTeacherComboBox, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE))).addGap(0, 23, Short.MAX_VALUE)).addGroup(jPanel6Layout.createSequentialGroup().addPreferredGap(statusTeacherLabel, addTeacherButton, LayoutStyle.ComponentPlacement.INDENT).addGroup(jPanel6Layout.createParallelGroup().addGroup(GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup().addGroup(jPanel6Layout.createParallelGroup().addComponent(addTeacherButton, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE).addComponent(modTeacherButton, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(suppTeacherButton, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)).addGroup(jPanel6Layout.createSequentialGroup().addComponent(teachingManagTeacherButton, GroupLayout.PREFERRED_SIZE, 220, GroupLayout.PREFERRED_SIZE))).addGap(0, 0, Short.MAX_VALUE))).addContainerGap(66, 66));
                                    jPanel6Layout.setVerticalGroup(jPanel6Layout.createSequentialGroup().addContainerGap(15, 15).addGroup(jPanel6Layout.createParallelGroup().addGroup(jPanel6Layout.createSequentialGroup().addGroup(jPanel6Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(titleTeacherComboBox, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE).addComponent(titleTeacherLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE).addComponent(aliasTeacherLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(aliasTeacherTextField, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel6Layout.createParallelGroup().addGroup(jPanel6Layout.createSequentialGroup().addGroup(jPanel6Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(nameTeacherLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE).addComponent(nameTeacherTextField, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel6Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(firstnameTeacherTextField, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE).addComponent(firstnameTeacherLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))).addGroup(jPanel6Layout.createSequentialGroup().addGap(19).addGroup(jPanel6Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(statutTeacherComboBox, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE).addComponent(statusTeacherLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)).addGap(12))).addGroup(jPanel6Layout.createParallelGroup().addGroup(GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup().addGroup(jPanel6Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(hTeacherTextField, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE).addComponent(hObliTeacherLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 12, GroupLayout.PREFERRED_SIZE).addComponent(hTeacherLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 12, GroupLayout.PREFERRED_SIZE)).addGap(24).addComponent(addTeacherButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addComponent(datNaisPanel, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel6Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(adrTeacherLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE).addComponent(modTeacherButton, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(suppTeacherButton, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(adrTeacherTextField, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel6Layout.createParallelGroup().addGroup(jPanel6Layout.createSequentialGroup().addGroup(jPanel6Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(pcTeacherTextField, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE).addComponent(pcTeacherLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel6Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(cityTeacherLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE).addComponent(cityTeacherTextField, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))).addGroup(GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup().addGap(22).addComponent(teachingManagTeacherButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addGap(0, 7, Short.MAX_VALUE)))).addGroup(GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup().addGap(8).addComponent(teacherScrollPane, GroupLayout.PREFERRED_SIZE, 223, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 0, Short.MAX_VALUE))).addContainerGap());
                                }
                            }
                            {
                                southRHPanel = new JPanel();
                                BorderLayout southRHPanelLayout = new BorderLayout();
                                southRHPanel.setLayout(southRHPanelLayout);
                                centerRhPanel.add(southRHPanel, BorderLayout.CENTER);
                                southRHPanel.setPreferredSize(new java.awt.Dimension(772, 87));
                                southRHPanel.setBackground(new java.awt.Color(254, 251, 231));
                                southRHPanel.setBorder(BorderFactory.createTitledBorder(null, "Gestion des salles", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));
                                {
                                    roomPanel = new JPanel();
                                    GroupLayout jPanel7Layout = new GroupLayout((JComponent) roomPanel);
                                    southRHPanel.add(roomPanel, BorderLayout.CENTER);
                                    roomPanel.setBackground(new java.awt.Color(254, 251, 231));
                                    roomPanel.setLayout(jPanel7Layout);
                                    roomPanel.setPreferredSize(new java.awt.Dimension(760, 90));
                                    {
                                        capacityRoomLabel = new JLabel();
                                        capacityRoomLabel.setText("Capacite: ");
                                        capacityRoomLabel.setBounds(390, 13, 75, 22);
                                    }
                                    {
                                        capacityRoomTextField = new JTextField();
                                        capacityRoomTextField.setText("50");
                                        capacityRoomTextField.setBounds(469, 14, 80, 23);
                                    }
                                    {
                                        nameRoomTextField = new JTextField();
                                        nameRoomTextField.setText("101");
                                        nameRoomTextField.setBounds(278, 14, 80, 23);
                                    }
                                    {
                                        nameRoomLabel = new JLabel();
                                        nameRoomLabel.setText("Nom: ");
                                        nameRoomLabel.setBounds(217, 15, 34, 17);
                                    }
                                    {
                                        addRoomButton = new JButton();
                                        addRoomButton.setText("Ajouter");
                                        addRoomButton.setBounds(268, 46, 106, 23);
                                    }
                                    {
                                        supRoomButton = new JButton();
                                        supRoomButton.setText("Supprimer");
                                        supRoomButton.setBounds(513, 46, 106, 23);
                                    }
                                    {
                                        modRoomButton = new JButton();
                                        modRoomButton.setText("Modifier");
                                        modRoomButton.setBounds(390, 46, 106, 23);
                                    }
                                    {
                                        roomScrollPane = new JScrollPane();
                                        roomScrollPane.setBounds(32, 12, 158, 56);
                                        {
                                            ListModel jList4Model = new DefaultComboBoxModel(new String[] { "101", "102", "103", "104", "105" });
                                            roomList = new JList();
                                            roomScrollPane.setViewportView(roomList);
                                            roomList.setModel(jList4Model);
                                            roomList.setVisibleRowCount(-1);
                                            roomList.setAutoscrolls(true);
                                            roomList.setBounds(28, 12, 131, 56);
                                            roomList.setPreferredSize(new java.awt.Dimension(113, 97));
                                        }
                                    }
                                    jPanel7Layout.setHorizontalGroup(jPanel7Layout.createSequentialGroup().addContainerGap(25, 25).addComponent(roomScrollPane, GroupLayout.PREFERRED_SIZE, 158, GroupLayout.PREFERRED_SIZE).addGap(27).addComponent(nameRoomLabel, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE).addGap(17).addGroup(jPanel7Layout.createParallelGroup().addComponent(addRoomButton, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE).addGroup(GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup().addPreferredGap(addRoomButton, nameRoomTextField, LayoutStyle.ComponentPlacement.INDENT).addComponent(nameRoomTextField, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE).addGap(16))).addGap(16).addGroup(jPanel7Layout.createParallelGroup().addGroup(GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup().addComponent(capacityRoomLabel, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(capacityRoomTextField, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE).addGap(0, 70, Short.MAX_VALUE)).addGroup(GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup().addComponent(modRoomButton, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE).addGap(17).addComponent(supRoomButton, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE).addGap(0, 0, Short.MAX_VALUE))).addContainerGap(146, 146));
                                    jPanel7Layout.setVerticalGroup(jPanel7Layout.createSequentialGroup().addContainerGap().addGroup(jPanel7Layout.createParallelGroup().addGroup(jPanel7Layout.createSequentialGroup().addGroup(jPanel7Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(capacityRoomLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE).addComponent(capacityRoomTextField, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE).addComponent(nameRoomTextField, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE).addComponent(nameRoomLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel7Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(addRoomButton, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(supRoomButton, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(modRoomButton, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addGap(11)).addComponent(roomScrollPane, GroupLayout.Alignment.LEADING, 0, 63, Short.MAX_VALUE)).addContainerGap(17, 17));
                                }
                            }
                            {
                                jPanel1 = new JPanel();
                                BorderLayout jPanel1Layout1 = new BorderLayout();
                                jPanel1Layout1.setVgap(50);
                                jPanel1.setLayout(jPanel1Layout1);
                                centerRhPanel.add(jPanel1, BorderLayout.SOUTH);
                                jPanel1.setPreferredSize(new java.awt.Dimension(772, 47));
                                {
                                    shadowPanel = new JPanel();
                                    jPanel1.add(shadowPanel, BorderLayout.CENTER);
                                    shadowPanel.setPreferredSize(new java.awt.Dimension(772, 47));
                                }
                            }
                        }
                    }
                }
            }
            this.subtitleFont = new Font(null, Font.BOLD, 14);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
