package ui;

import logic.Teacher;
import org.hibernate.Session;
import org.jdesktop.swingx.JXDatePicker;
import system.HibernateUtil;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.util.List;

/**
 * User: linx
 * Date: 04.09.2006
 * Time: 13:02:30
 */
public class TeacherCard extends JInternalFrame {

    String[] columnNames = { "�������", "���", "��������", "���� ��������", "����� �������� (���)", "����� �������� (���)", "�������� ��������", "����������� ��������", "�������", "���������", "������� �������", "����� �������������", "�������������", "�����", "���� �������� ��������", "���� ����������� ��������", "���� ��������� ��������" };

    JPanel buttons = new JPanel();

    JPanel form = new JPanel();

    JButton cancel = new JButton("������");

    JButton ok = new JButton("Ok");

    JLabel lfirstName = new JLabel(columnNames[0]);

    JTextField tfirstName = new JTextField();

    JLabel lmiddleName = new JLabel(columnNames[1]);

    JTextField tmiddleName = new JTextField();

    JLabel llastName = new JLabel(columnNames[2]);

    JTextField tlastName = new JTextField();

    JLabel ldateOfBirth = new JLabel(columnNames[3]);

    JXDatePicker dateOfBirth = new JXDatePicker();

    JLabel lphoneMobile = new JLabel(columnNames[4]);

    JTextField tphoneMobile = new JTextField();

    JLabel lphoneHome = new JLabel(columnNames[5]);

    JTextField tphoneHome = new JTextField();

    JLabel lmainLoad = new JLabel(columnNames[6]);

    JTextField tmainLoad = new JTextField();

    JLabel lextLoad = new JLabel(columnNames[7]);

    JTextField textLoad = new JTextField();

    JLabel lpassport = new JLabel(columnNames[8]);

    JTextField tpassport = new JTextField();

    JLabel lappointment = new JLabel(columnNames[9]);

    JTextField tappointment = new JTextField();

    JLabel lacadDegree = new JLabel(columnNames[10]);

    JTextField tacadDegree = new JTextField();

    JLabel lnumSpeciality = new JLabel(columnNames[11]);

    JTextField tnumSpeciality = new JTextField();

    JLabel lspeciality = new JLabel(columnNames[12]);

    JTextField tspeciality = new JTextField();

    JLabel ladress = new JLabel(columnNames[13]);

    JTextField tadress = new JTextField();

    JLabel lmainHour = new JLabel(columnNames[14]);

    JTextField tmainHour = new JTextField();

    JLabel lextHour = new JLabel(columnNames[15]);

    JTextField textHour = new JTextField();

    JLabel lhourlyHour = new JLabel(columnNames[16]);

    JTextField thourlyHour = new JTextField();

    JPanel panel1 = new JPanel();

    JPanel panel2 = new JPanel();

    JPanel panel3 = new JPanel();

    JPanel panel4 = new JPanel();

    JButton loadBtn = new JButton("��������");

    private boolean isNew = false;

    private Teacher teacher;

    private List<Teacher> list;

    public TeacherCard(int teacherId) {
        super("", false, true, false, true);
        Session s = HibernateUtil.getSessionFactory().getCurrentSession();
        s.beginTransaction();
        teacher = (Teacher) s.load(Teacher.class, teacherId);
        createAndInitializeGUI();
    }

    public TeacherCard(Teacher teacher, List<Teacher> list) {
        super("", false, true, false, true);
        this.teacher = teacher;
        this.list = list;
        createAndInitializeGUI();
    }

    private void createAndInitializeGUI() {
        dateOfBirth.setFormats(new String[] { "dd.MM.yyyy" });
        if (teacher == null) {
            teacher = new Teacher();
            list.add(teacher);
            isNew = true;
        }
        if (teacher.getTeacherId() == null) {
            isNew = true;
        }
        loadBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                LoadItemForm lif = new LoadItemForm(teacher);
                ((JDesktopPane) getParent()).add(lif);
                try {
                    lif.setSelected(true);
                } catch (PropertyVetoException e1) {
                    e1.printStackTrace();
                }
            }
        });
        String firstname = teacher.getFirstName();
        if (firstname == null || firstname.length() == 0) {
            tfirstName.setText("������� �������������");
        } else {
            tfirstName.setText(teacher.getFirstName());
            tmiddleName.setText(teacher.getMiddleName());
            tlastName.setText(teacher.getLastName());
            dateOfBirth.setDate(teacher.getDateOfBirth());
            tphoneMobile.setText(teacher.getPhoneMobile());
            tphoneHome.setText(teacher.getPhoneHome());
            tmainLoad.setText(teacher.getMainLoad().toString());
            textLoad.setText(teacher.getExtLoad().toString());
            tpassport.setText(teacher.getPassport().toString());
            tappointment.setText(teacher.getAppointment());
            tacadDegree.setText(teacher.getAcadDegree());
            tnumSpeciality.setText(teacher.getNumSpeciality());
            tspeciality.setText(teacher.getSpeciality());
            tadress.setText(teacher.getAdress());
            tmainHour.setText(teacher.getMainHour().toString());
            textHour.setText(teacher.getExtHour().toString());
            thourlyHour.setText(teacher.getHourlyHour().toString());
        }
        setTitle(teacher.getFirstName());
        form.setLayout(new GridLayout(18, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel1.setLayout(new GridLayout());
        form.add(lfirstName);
        form.add(tfirstName);
        form.add(lmiddleName);
        form.add(tmiddleName);
        form.add(llastName);
        form.add(tlastName);
        form.add(ldateOfBirth);
        form.add(dateOfBirth);
        form.add(lphoneMobile);
        form.add(tphoneMobile);
        form.add(lphoneHome);
        form.add(tphoneHome);
        form.add(lmainLoad);
        form.add(tmainLoad);
        form.add(lextLoad);
        form.add(textLoad);
        form.add(lpassport);
        form.add(tpassport);
        form.add(lappointment);
        form.add(tappointment);
        form.add(lacadDegree);
        form.add(tacadDegree);
        form.add(lnumSpeciality);
        form.add(tnumSpeciality);
        form.add(lspeciality);
        form.add(tspeciality);
        form.add(ladress);
        form.add(tadress);
        form.add(lmainHour);
        form.add(tmainHour);
        form.add(lextHour);
        form.add(textHour);
        form.add(lhourlyHour);
        form.add(thourlyHour);
        form.add(loadBtn);
        getContentPane().add(form, BorderLayout.CENTER);
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.LINE_AXIS));
        buttons.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttons.add(Box.createHorizontalGlue());
        buttons.add(ok);
        ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    teacher.setFirstName(tfirstName.getText());
                    teacher.setMiddleName(tmiddleName.getText());
                    teacher.setLastName(tlastName.getText());
                    teacher.setDateOfBirth(dateOfBirth.getDate());
                    teacher.setPhoneMobile(tphoneMobile.getText());
                    teacher.setPhoneHome(tphoneHome.getText());
                    teacher.setMainLoad((tmainLoad.getText().equals("")) ? 0 : Float.parseFloat(tmainLoad.getText()));
                    teacher.setExtLoad((textLoad.getText().equals("")) ? 0 : Float.parseFloat(textLoad.getText()));
                    teacher.setPassport(tpassport.getText());
                    teacher.setAppointment(tappointment.getText());
                    teacher.setAcadDegree(tacadDegree.getText());
                    teacher.setNumSpeciality(tnumSpeciality.getText());
                    teacher.setSpeciality(tspeciality.getText());
                    teacher.setAdress(tadress.getText());
                    teacher.setMainHour((tmainHour.getText().equals("")) ? 0 : Integer.parseInt(tmainHour.getText()));
                    teacher.setExtHour((textHour.getText().equals("")) ? 0 : Integer.parseInt(textHour.getText()));
                    teacher.setHourlyHour((thourlyHour.getText().equals("")) ? 0 : Integer.parseInt(thourlyHour.getText()));
                    saveTeacher(teacher);
                    setVisible(false);
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(null, exc.toString());
                }
            }
        });
        buttons.add(Box.createRigidArea(new Dimension(10, 0)));
        buttons.add(cancel);
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (isNew) list.remove(teacher);
                setVisible(false);
            }
        });
        getContentPane().add(buttons, BorderLayout.PAGE_END);
        pack();
        setSize(350, 600);
        setResizable(false);
        setVisible(true);
    }

    private void saveTeacher(Teacher teacher) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.saveOrUpdate(teacher);
        session.getTransaction().commit();
    }
}
