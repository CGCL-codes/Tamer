package corpodatrecords;

import corpodatrecordsbackend.Client;
import corpodatrecordsbackend.ClientJpaController;
import corpodatrecordsbackend.Course;
import corpodatrecordsbackend.CourseJpaController;
import corpodatrecordsbackend.CourseTypeJpaController;
import corpodatrecordsbackend.FormativeAction;
import corpodatrecordsbackend.FormativeActionJpaController;
import corpodatrecordsbackend.Student;
import corpodatrecordsbackend.StudentJpaController;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author luis
 */
public class CourseView extends javax.swing.JPanel {

    CourseJpaController JC_course;

    CourseTypeJpaController JC_courseType;

    StudentJpaController JC_student;

    ClientJpaController JC_client;

    FormativeActionJpaController JC_formativeAction;

    ArrayList<String> clientNamesList = new ArrayList<String>();

    public CourseView() {
        this.JC_client = new ClientJpaController();
        this.JC_course = new CourseJpaController();
        this.JC_courseType = new CourseTypeJpaController();
        this.JC_formativeAction = new FormativeActionJpaController();
        this.JC_student = new StudentJpaController();
        initComponents();
        System.out.println("Filling data...");
        System.out.print("\t·CIFs ComboBox... ");
        this.fillCIFs();
        System.out.println("DONE!");
        System.out.print("\t·Courses Table... ");
        this.fillCoursesTable();
        System.out.println("DONE!");
        System.out.print("\t·FormativeActions ComboBox... ");
        this.fillFormativeActions();
        System.out.println("DONE!");
        System.out.print("\t·Students Table... ");
        System.out.println("DONE!");
    }

    /** Creates new form CourseView */
    public CourseView(CourseJpaController JC_course, CourseTypeJpaController JC_courseType, StudentJpaController JC_student, ClientJpaController JC_client, FormativeActionJpaController JC_formativeAction) {
        this.JC_course = JC_course;
        this.JC_client = JC_client;
        this.JC_courseType = JC_courseType;
        this.JC_student = JC_student;
        this.JC_formativeAction = JC_formativeAction;
        initComponents();
        System.out.println("Filling data...");
        System.out.print("\t·CIFs ComboBox... ");
        this.fillCIFs();
        System.out.println("DONE!");
        System.out.print("\t·Courses Table... ");
        this.fillCoursesTable();
        System.out.println("DONE!");
        System.out.print("\t·FormativeActions ComboBox... ");
        this.fillFormativeActions();
        System.out.println("DONE!");
        System.out.print("\t·Students Table... ");
        System.out.println("DONE!");
    }

    private void fillCIFs() {
        this.jComboBox_formativeAction.removeAllItems();
        ArrayList<Client> clients = new ArrayList<Client>(this.JC_client.findClientEntities());
        for (Client c : clients) {
            this.clientNamesList.add(c.getName());
            this.jComboBox_cif.addItem(c.getCif());
        }
    }

    private void fillFormativeActions() {
        this.jComboBox_formativeAction.removeAllItems();
        ArrayList<FormativeAction> fas = new ArrayList<FormativeAction>(this.JC_formativeAction.findFormativeActionEntities());
        for (FormativeAction fa : fas) {
            this.jComboBox_formativeAction.addItem(fa.getName());
        }
    }

    private void addCourseToTable(Course c) {
        Vector v = new Vector();
        v.add(c.getName());
        v.add(this.JC_formativeAction.findFormativeAction(c.getCoursetypeId()).getName());
        v.add(c.getHours());
        v.add(c.getInitDate());
        v.add(c.getFinalDate());
        DefaultTableModel dtm = (DefaultTableModel) this.jTable_courses.getModel();
        dtm.addRow(v);
    }

    private void fillCoursesTable() {
        DefaultTableModel dtm = (DefaultTableModel) this.jTable_courses.getModel();
        while (dtm.getRowCount() > 0) {
            dtm.removeRow(0);
        }
        ArrayList<Course> courses = new ArrayList<Course>(this.JC_course.findCourseEntities(this.JC_client.findClient((String) this.jComboBox_cif.getSelectedItem()).getId()));
        for (Course c : courses) {
            this.addCourseToTable(c);
        }
    }

    private void addStudentToTable(Student s) {
        Vector v = new Vector();
        v.add(s.getDni());
        v.add(s.getSsNumber());
        v.add(s.getSurname());
        v.add(s.getName());
        v.add(s.getBirthDate());
        v.add(s.getTlfno());
        v.add(s.getEmail());
        v.add(s.getDiscapacity());
        v.add(s.getDiscapacityType());
        v.add(s.getInitDate());
        v.add(s.getFinalDate());
        DefaultTableModel dtm = (DefaultTableModel) this.jTable_students.getModel();
        dtm.addRow(v);
    }

    private void fillStudentsTable() {
        DefaultTableModel dtm = (DefaultTableModel) this.jTable_students.getModel();
        while (dtm.getRowCount() > 0) {
            dtm.removeRow(0);
        }
        ArrayList<Student> students = new ArrayList<Student>(this.JC_student.findStudentEntities(this.JC_course.findCourse((String) this.jTable_courses.getValueAt(this.jTable_courses.getSelectedRow(), 0), this.JC_client.findClient(this.jComboBox_cif.getSelectedItem().toString()).getId()).getId()));
        for (Student s : students) {
            this.addStudentToTable(s);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jComboBox_cif = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_courses = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jTextField_courseName = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jComboBox_formativeAction = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jSpinner_numHours = new javax.swing.JSpinner();
        jLabel8 = new javax.swing.JLabel();
        jFormattedTextField_initDate = new javax.swing.JFormattedTextField();
        jLabel9 = new javax.swing.JLabel();
        jFormattedTextField_finalDate = new javax.swing.JFormattedTextField();
        jButton_courseSave = new javax.swing.JButton();
        jButton_courseDelete = new javax.swing.JButton();
        jButton_courseClean = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable_students = new javax.swing.JTable();
        jButton_courseAddAFormative = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jTextField_student_dni = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextField_student_ssNumber = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextField_student_surname = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextField_student_name = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jFormattedTextField_student_birthDate = new javax.swing.JFormattedTextField();
        jLabel15 = new javax.swing.JLabel();
        jTextField_student_phone = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jTextField_student_email = new javax.swing.JTextField();
        jCheckBox_student_disc = new javax.swing.JCheckBox();
        jLabel17 = new javax.swing.JLabel();
        jTextField_student_discType = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jFormattedTextField_student_examDate = new javax.swing.JFormattedTextField();
        jLabel19 = new javax.swing.JLabel();
        jFormattedTextField_student_docDate = new javax.swing.JFormattedTextField();
        jButton_student_save = new javax.swing.JButton();
        jButton_student_remove = new javax.swing.JButton();
        jButton_student_clean = new javax.swing.JButton();
        jLabel_clientName = new javax.swing.JLabel();
        setName("Form");
        setPreferredSize(new java.awt.Dimension(1024, 768));
        jComboBox_cif.setEditable(true);
        jComboBox_cif.setName("jComboBox_cif");
        jComboBox_cif.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox_cifItemStateChanged(evt);
            }
        });
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(corpodatrecords.CorpodatRecordsApp.class).getContext().getResourceMap(CourseView.class);
        jLabel1.setFont(resourceMap.getFont("jLabel1.font"));
        jLabel1.setText(resourceMap.getString("jLabel1.text"));
        jLabel1.setName("jLabel1");
        jLabel2.setFont(resourceMap.getFont("jLabel2.font"));
        jLabel2.setText(resourceMap.getString("jLabel2.text"));
        jLabel2.setName("jLabel2");
        jSeparator1.setName("jSeparator1");
        jLabel4.setFont(resourceMap.getFont("jLabel4.font"));
        jLabel4.setText(resourceMap.getString("jLabel4.text"));
        jLabel4.setName("jLabel4");
        jScrollPane1.setName("jScrollPane1");
        jTable_courses.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] { "Nombre", "Acción Formativa", "Num. Horas", "Fecha Inicio", "Fecha Final" }) {

            Class[] types = new Class[] { java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class };

            boolean[] canEdit = new boolean[] { false, false, false, false, false };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jTable_courses.setName("jTable_courses");
        jTable_courses.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable_coursesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable_courses);
        jTable_courses.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("jTable_courses.columnModel.title0"));
        jTable_courses.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("jTable_courses.columnModel.title1"));
        jTable_courses.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("jTable_courses.columnModel.title2"));
        jTable_courses.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("jTable_courses.columnModel.title3"));
        jTable_courses.getColumnModel().getColumn(4).setHeaderValue(resourceMap.getString("jTable_courses.columnModel.title4"));
        jLabel5.setText(resourceMap.getString("jLabel5.text"));
        jLabel5.setName("jLabel5");
        jTextField_courseName.setText(resourceMap.getString("jTextField_courseName.text"));
        jTextField_courseName.setName("jTextField_courseName");
        jLabel6.setText(resourceMap.getString("jLabel6.text"));
        jLabel6.setName("jLabel6");
        jComboBox_formativeAction.setName("jComboBox_formativeAction");
        jLabel7.setText(resourceMap.getString("jLabel7.text"));
        jLabel7.setName("jLabel7");
        jSpinner_numHours.setName("jSpinner_numHours");
        jLabel8.setText(resourceMap.getString("jLabel8.text"));
        jLabel8.setName("jLabel8");
        jFormattedTextField_initDate.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("dd/MM/yy"))));
        jFormattedTextField_initDate.setName("jFormattedTextField_initDate");
        jLabel9.setText(resourceMap.getString("jLabel9.text"));
        jLabel9.setName("jLabel9");
        jFormattedTextField_finalDate.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("dd/MM/yy"))));
        jFormattedTextField_finalDate.setName("jFormattedTextField_finalDate");
        jButton_courseSave.setText(resourceMap.getString("jButton_courseSave.text"));
        jButton_courseSave.setName("jButton_courseSave");
        jButton_courseSave.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton_courseSaveMouseClicked(evt);
            }
        });
        jButton_courseDelete.setText(resourceMap.getString("jButton_courseDelete.text"));
        jButton_courseDelete.setName("jButton_courseDelete");
        jButton_courseDelete.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton_courseDeleteMouseClicked(evt);
            }
        });
        jButton_courseClean.setText(resourceMap.getString("jButton_courseClean.text"));
        jButton_courseClean.setName("jButton_courseClean");
        jButton_courseClean.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton_courseCleanMouseClicked(evt);
            }
        });
        jSeparator2.setName("jSeparator2");
        jLabel10.setFont(resourceMap.getFont("jLabel10.font"));
        jLabel10.setText(resourceMap.getString("jLabel10.text"));
        jLabel10.setName("jLabel10");
        jScrollPane2.setName("jScrollPane2");
        jTable_students.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] { "DNI", "Número SS", "Apellidos", "Nombre", "F.Examen", "F.Diploma" }) {

            Class[] types = new Class[] { java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class };

            boolean[] canEdit = new boolean[] { false, false, false, false, false, false };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jTable_students.setName("jTable_students");
        jTable_students.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable_studentsMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable_students);
        jButton_courseAddAFormative.setText(resourceMap.getString("jButton_courseAddAFormative.text"));
        jButton_courseAddAFormative.setName("jButton_courseAddAFormative");
        jButton_courseAddAFormative.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton_courseAddAFormativeMouseClicked(evt);
            }
        });
        jLabel3.setText(resourceMap.getString("jLabel3.text"));
        jLabel3.setName("jLabel3");
        jTextField_student_dni.setText(resourceMap.getString("jTextField_student_dni.text"));
        jTextField_student_dni.setName("jTextField_student_dni");
        jLabel11.setText(resourceMap.getString("jLabel11.text"));
        jLabel11.setName("jLabel11");
        jTextField_student_ssNumber.setText(resourceMap.getString("jTextField_student_ssNumber.text"));
        jTextField_student_ssNumber.setName("jTextField_student_ssNumber");
        jLabel12.setText(resourceMap.getString("jLabel12.text"));
        jLabel12.setName("jLabel12");
        jTextField_student_surname.setText(resourceMap.getString("jTextField_student_surname.text"));
        jTextField_student_surname.setName("jTextField_student_surname");
        jLabel13.setText(resourceMap.getString("jLabel13.text"));
        jLabel13.setName("jLabel13");
        jTextField_student_name.setText(resourceMap.getString("jTextField_student_name.text"));
        jTextField_student_name.setName("jTextField_student_name");
        jLabel14.setText(resourceMap.getString("jLabel14.text"));
        jLabel14.setName("jLabel14");
        jFormattedTextField_student_birthDate.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("dd/MM/yyyy"))));
        jFormattedTextField_student_birthDate.setText(resourceMap.getString("jFormattedTextField_student_birthDate.text"));
        jFormattedTextField_student_birthDate.setName("jFormattedTextField_student_birthDate");
        jLabel15.setText(resourceMap.getString("jLabel15.text"));
        jLabel15.setName("jLabel15");
        jTextField_student_phone.setText(resourceMap.getString("jTextField_student_phone.text"));
        jTextField_student_phone.setName("jTextField_student_phone");
        jLabel16.setText(resourceMap.getString("jLabel16.text"));
        jLabel16.setName("jLabel16");
        jTextField_student_email.setText(resourceMap.getString("jTextField_student_email.text"));
        jTextField_student_email.setName("jTextField_student_email");
        jCheckBox_student_disc.setText(resourceMap.getString("jCheckBox_student_disc.text"));
        jCheckBox_student_disc.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jCheckBox_student_disc.setName("jCheckBox_student_disc");
        jCheckBox_student_disc.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBox_student_discStateChanged(evt);
            }
        });
        jLabel17.setText(resourceMap.getString("jLabel17.text"));
        jLabel17.setEnabled(false);
        jLabel17.setName("jLabel17");
        jTextField_student_discType.setText(resourceMap.getString("jTextField_student_discType.text"));
        jTextField_student_discType.setEnabled(false);
        jTextField_student_discType.setName("jTextField_student_discType");
        jLabel18.setText(resourceMap.getString("jLabel18.text"));
        jLabel18.setName("jLabel18");
        jFormattedTextField_student_examDate.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("dd/MM/yy"))));
        jFormattedTextField_student_examDate.setText(resourceMap.getString("jFormattedTextField_student_examDate.text"));
        jFormattedTextField_student_examDate.setName("jFormattedTextField_student_examDate");
        jLabel19.setText(resourceMap.getString("jLabel19.text"));
        jLabel19.setName("jLabel19");
        jFormattedTextField_student_docDate.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("dd/MM/yy"))));
        jFormattedTextField_student_docDate.setName("jFormattedTextField_student_docDate");
        jButton_student_save.setText(resourceMap.getString("jButton_student_save.text"));
        jButton_student_save.setName("jButton_student_save");
        jButton_student_save.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton_student_saveMouseClicked(evt);
            }
        });
        jButton_student_remove.setText(resourceMap.getString("jButton_student_remove.text"));
        jButton_student_remove.setName("jButton_student_remove");
        jButton_student_clean.setText(resourceMap.getString("jButton_student_clean.text"));
        jButton_student_clean.setName("jButton_student_clean");
        jLabel_clientName.setText(resourceMap.getString("jLabel_clientName.text"));
        jLabel_clientName.setName("jLabel_clientName");
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jLabel1).addGap(4, 4, 4).addComponent(jComboBox_cif, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel2)).addComponent(jLabel4).addComponent(jLabel10).addGroup(layout.createSequentialGroup().addGap(243, 243, 243).addComponent(jLabel_clientName, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1004, Short.MAX_VALUE).addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1004, Short.MAX_VALUE).addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1004, Short.MAX_VALUE).addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1004, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addComponent(jLabel3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField_student_dni, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel11).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField_student_ssNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel12).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField_student_surname, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel13).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField_student_name, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel14).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jFormattedTextField_student_birthDate, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addComponent(jLabel15).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField_student_phone, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel16).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField_student_email, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jCheckBox_student_disc).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel17).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField_student_discType, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel18).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jFormattedTextField_student_examDate, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel19).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jFormattedTextField_student_docDate, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addComponent(jButton_student_save).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton_student_remove).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton_student_clean))).addGroup(layout.createSequentialGroup().addComponent(jLabel5).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField_courseName, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel6).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jComboBox_formativeAction, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel7).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jSpinner_numHours, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel8).addGap(12, 12, 12).addComponent(jFormattedTextField_initDate, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel9).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jFormattedTextField_finalDate, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addComponent(jButton_courseSave).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton_courseDelete).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton_courseClean).addGap(18, 18, 18).addComponent(jButton_courseAddAFormative))))).addContainerGap(69, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(jComboBox_cif, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel2).addComponent(jLabel_clientName)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel4).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel5).addComponent(jTextField_courseName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel6).addComponent(jComboBox_formativeAction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel7).addComponent(jSpinner_numHours, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel8).addComponent(jFormattedTextField_initDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel9).addComponent(jFormattedTextField_finalDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButton_courseSave).addComponent(jButton_courseDelete).addComponent(jButton_courseClean).addComponent(jButton_courseAddAFormative)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel10).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(jTextField_student_dni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel11).addComponent(jTextField_student_ssNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel12).addComponent(jTextField_student_surname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel13).addComponent(jTextField_student_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel14).addComponent(jFormattedTextField_student_birthDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel15).addComponent(jTextField_student_phone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel16).addComponent(jTextField_student_email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jCheckBox_student_disc).addComponent(jLabel17).addComponent(jTextField_student_discType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel18).addComponent(jFormattedTextField_student_examDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel19).addComponent(jFormattedTextField_student_docDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButton_student_save).addComponent(jButton_student_remove).addComponent(jButton_student_clean)).addContainerGap()));
    }

    private void jComboBox_cifItemStateChanged(java.awt.event.ItemEvent evt) {
        this.jLabel_clientName.setText(this.clientNamesList.get(this.jComboBox_cif.getSelectedIndex()));
        this.fillCoursesTable();
    }

    private void jCheckBox_student_discStateChanged(javax.swing.event.ChangeEvent evt) {
        this.jLabel17.setEnabled(this.jCheckBox_student_disc.isSelected());
        this.jTextField_student_discType.setEnabled(this.jCheckBox_student_disc.isSelected());
    }

    private void jButton_courseAddAFormativeMouseClicked(java.awt.event.MouseEvent evt) {
        AddFADialog addFAdialog = new AddFADialog(new JFrame(), true, JC_formativeAction);
        addFAdialog.setVisible(true);
    }

    private void jButton_courseSaveMouseClicked(java.awt.event.MouseEvent evt) {
        Course c = new Course(0, this.JC_client.findClient((String) this.jComboBox_cif.getSelectedItem()).getId(), this.jTextField_courseName.getText(), this.JC_formativeAction.findFormativeAction((String) this.jComboBox_formativeAction.getSelectedItem()).getId(), (Integer) this.jSpinner_numHours.getValue(), (Date) this.jFormattedTextField_initDate.getValue(), (Date) this.jFormattedTextField_finalDate.getValue());
        this.JC_course.create(c);
        this.addCourseToTable(c);
    }

    private void jButton_courseDeleteMouseClicked(java.awt.event.MouseEvent evt) {
    }

    private void jButton_courseCleanMouseClicked(java.awt.event.MouseEvent evt) {
        this.jTextField_courseName.setText("");
        this.jComboBox_formativeAction.setSelectedIndex(1);
        this.jSpinner_numHours.setValue(0);
        this.jFormattedTextField_initDate.setValue(new Date());
        this.jFormattedTextField_finalDate.setValue(new Date());
    }

    private void jButton_student_saveMouseClicked(java.awt.event.MouseEvent evt) {
        Student s = new Student();
        s.setCoursesIndexid(this.JC_course.findCourse((String) this.jTable_courses.getValueAt(this.jTable_courses.getSelectedRow(), 0), this.JC_client.findClient(this.jComboBox_cif.getSelectedItem().toString()).getId()).getId());
        s.setDni(this.jTextField_student_dni.getText());
        s.setSsNumber(this.jTextField_student_ssNumber.getText());
        s.setSurname(this.jTextField_student_surname.getText());
        s.setName(this.jTextField_student_name.getText());
        s.setBirthDate((Date) this.jFormattedTextField_student_birthDate.getValue());
        s.setTlfno(this.jTextField_student_phone.getText());
        s.setEmail(this.jTextField_student_email.getText());
        s.setDiscapacity(this.jCheckBox_student_disc.isSelected());
        if (this.jCheckBox_student_disc.isSelected()) {
            s.setDiscapacityType(this.jTextField_student_discType.getText());
        }
        s.setInitDate((Date) this.jFormattedTextField_student_examDate.getValue());
        s.setFinalDate((Date) this.jFormattedTextField_student_docDate.getValue());
        this.JC_student.create(s);
        this.addStudentToTable(s);
    }

    private void jTable_coursesMouseClicked(java.awt.event.MouseEvent evt) {
        this.fillStudentsTable();
        Integer selectedRow = this.jTable_courses.getSelectedRow();
        this.jTextField_courseName.setText((String) this.jTable_courses.getValueAt(selectedRow, 0));
        this.jComboBox_formativeAction.setSelectedItem((String) this.jTable_courses.getValueAt(selectedRow, 1));
        this.jSpinner_numHours.setValue((Integer) this.jTable_courses.getValueAt(selectedRow, 2));
        this.jFormattedTextField_initDate.setValue((Date) this.jTable_courses.getValueAt(selectedRow, 3));
        this.jFormattedTextField_finalDate.setValue((Date) this.jTable_courses.getValueAt(selectedRow, 4));
    }

    private void jTable_studentsMouseClicked(java.awt.event.MouseEvent evt) {
        Integer selectedRow = this.jTable_students.getSelectedRow();
        this.jTextField_student_dni.setText((String) this.jTable_students.getValueAt(selectedRow, 0));
        this.jTextField_student_ssNumber.setText((String) this.jTable_students.getValueAt(selectedRow, 1));
        this.jTextField_student_surname.setText((String) this.jTable_students.getValueAt(selectedRow, 2));
        this.jTextField_student_name.setText((String) this.jTable_students.getValueAt(selectedRow, 3));
        this.jFormattedTextField_student_birthDate.setValue((Date) this.jTable_students.getValueAt(selectedRow, 4));
        this.jTextField_student_phone.setText((String) this.jTable_students.getValueAt(selectedRow, 5));
        this.jTextField_student_email.setText((String) this.jTable_students.getValueAt(selectedRow, 6));
        this.jCheckBox_student_disc.setSelected((Boolean) this.jTable_students.getValueAt(selectedRow, 7));
        this.jTextField_student_discType.setText((String) this.jTable_students.getValueAt(selectedRow, 8));
        this.jFormattedTextField_student_examDate.setValue((Date) this.jTable_students.getValueAt(selectedRow, 9));
        this.jFormattedTextField_student_docDate.setValue((Date) this.jTable_students.getValueAt(selectedRow, 10));
    }

    private javax.swing.JButton jButton_courseAddAFormative;

    private javax.swing.JButton jButton_courseClean;

    private javax.swing.JButton jButton_courseDelete;

    private javax.swing.JButton jButton_courseSave;

    private javax.swing.JButton jButton_student_clean;

    private javax.swing.JButton jButton_student_remove;

    private javax.swing.JButton jButton_student_save;

    private javax.swing.JCheckBox jCheckBox_student_disc;

    private javax.swing.JComboBox jComboBox_cif;

    private javax.swing.JComboBox jComboBox_formativeAction;

    private javax.swing.JFormattedTextField jFormattedTextField_finalDate;

    private javax.swing.JFormattedTextField jFormattedTextField_initDate;

    private javax.swing.JFormattedTextField jFormattedTextField_student_birthDate;

    private javax.swing.JFormattedTextField jFormattedTextField_student_docDate;

    private javax.swing.JFormattedTextField jFormattedTextField_student_examDate;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JLabel jLabel12;

    private javax.swing.JLabel jLabel13;

    private javax.swing.JLabel jLabel14;

    private javax.swing.JLabel jLabel15;

    private javax.swing.JLabel jLabel16;

    private javax.swing.JLabel jLabel17;

    private javax.swing.JLabel jLabel18;

    private javax.swing.JLabel jLabel19;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JLabel jLabel_clientName;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JSeparator jSeparator2;

    private javax.swing.JSpinner jSpinner_numHours;

    private javax.swing.JTable jTable_courses;

    private javax.swing.JTable jTable_students;

    private javax.swing.JTextField jTextField_courseName;

    private javax.swing.JTextField jTextField_student_discType;

    private javax.swing.JTextField jTextField_student_dni;

    private javax.swing.JTextField jTextField_student_email;

    private javax.swing.JTextField jTextField_student_name;

    private javax.swing.JTextField jTextField_student_phone;

    private javax.swing.JTextField jTextField_student_ssNumber;

    private javax.swing.JTextField jTextField_student_surname;
}
