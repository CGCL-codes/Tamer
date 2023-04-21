package ui;

import logic.Subject;
import logic.Speciality;
import org.hibernate.Session;
import system.HibernateUtil;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * User: Linx
 * Date: 17.09.2006
 * Time: 20:17:31
 */
public class SubjectCard extends JInternalFrame {

    JLabel fullnameL = new JLabel("������ ��������");

    JLabel shortnameL = new JLabel("�������� ��������");

    JLabel acedemicYearL = new JLabel("������� ���");

    JLabel yearL = new JLabel("����");

    JLabel termL = new JLabel("C������");

    JLabel studentCountL = new JLabel("���������� ���������");

    JLabel groupCountL = new JLabel("���������� �����");

    JLabel subGroupCountL = new JLabel("���������� ��������");

    JLabel lecturesL = new JLabel("������");

    JLabel labsL = new JLabel("������������");

    JLabel pedPracticeL = new JLabel("�����������");

    JLabel practiceL = new JLabel("��������");

    JLabel finalExamL = new JLabel("�������");

    JLabel visitingL = new JLabel("���������");

    JLabel specialityL = new JLabel("�������������");

    JComboBox specialitiesCB = new JComboBox();

    JTextField fullnameTF = new JTextField();

    JTextField shortnameTF = new JTextField();

    JTextField acedemicYearTF = new JTextField();

    JTextField yearTF = new JTextField();

    JTextField termTF = new JTextField();

    JTextField studentCountTF = new JTextField();

    JTextField groupCountTF = new JTextField();

    JTextField subGroupCountTF = new JTextField();

    JTextField lecturesTF = new JTextField();

    JTextField labsTF = new JTextField();

    JTextField pedPracticeTF = new JTextField();

    JTextField practiceTF = new JTextField();

    JTextField finalExamTF = new JTextField();

    JTextField visitingTF = new JTextField();

    JPanel buttons = new JPanel();

    JPanel form = new JPanel();

    JButton cancel = new JButton("Cancel");

    JButton ok = new JButton("Ok");

    private boolean isNew = false;

    private Subject subject;

    private List<Subject> list;

    public SubjectCard(int subjectId) {
        super("", false, true, false, true);
        Session s = HibernateUtil.getSessionFactory().getCurrentSession();
        s.beginTransaction();
        subject = (Subject) s.load(Subject.class, subjectId);
        createAndInitializeGUI();
    }

    public SubjectCard(Subject subject, List<Subject> list) {
        super("", false, true, false, true);
        this.subject = subject;
        this.list = list;
        createAndInitializeGUI();
    }

    private void createAndInitializeGUI() {
        if (subject == null) {
            subject = new Subject();
            list.add(subject);
            isNew = true;
        }
        if (subject.getSubjectId() == null) {
            isNew = true;
        }
        String fullName = subject.getFullName();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        java.util.List result = session.createQuery("from Speciality").list();
        for (Object f : result) {
            Speciality speciality = ((Speciality) f);
            specialitiesCB.addItem(speciality.getFullName());
            if (!isNew) if (subject.getSpeciality().getSpecialityId() == speciality.getSpecialityId()) specialitiesCB.setSelectedItem(speciality.getFullName());
        }
        if (fullName == null || fullName.length() == 0) {
            subject.setFullName("����� �������");
        } else {
            fullnameTF.setText(subject.getFullName());
            shortnameTF.setText(subject.getShortName());
            acedemicYearTF.setText(subject.getAcedemicYear());
            yearTF.setText(String.valueOf(subject.getYear()));
            termTF.setText(String.valueOf(subject.getTerm()));
            studentCountTF.setText(String.valueOf(subject.getStudentCount()));
            groupCountTF.setText(String.valueOf(subject.getGroupCount()));
            subGroupCountTF.setText(String.valueOf(subject.getSubGroupCount()));
            lecturesTF.setText(String.valueOf(subject.getLectures()));
            labsTF.setText(String.valueOf(subject.getLabs()));
            pedPracticeTF.setText(String.valueOf(subject.getPedPractice()));
            practiceTF.setText(String.valueOf(subject.getPractice()));
            finalExamTF.setText(String.valueOf(subject.getFinalExam()));
            visitingTF.setText(String.valueOf(subject.getVisiting()));
        }
        setTitle(subject.getFullName());
        form.setLayout(new GridLayout(15, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        form.add(fullnameL);
        form.add(fullnameTF);
        form.add(shortnameL);
        form.add(shortnameTF);
        form.add(acedemicYearL);
        form.add(acedemicYearTF);
        form.add(yearL);
        form.add(yearTF);
        form.add(termL);
        form.add(termTF);
        form.add(studentCountL);
        form.add(studentCountTF);
        form.add(groupCountL);
        form.add(groupCountTF);
        form.add(subGroupCountL);
        form.add(subGroupCountTF);
        form.add(lecturesL);
        form.add(lecturesTF);
        form.add(labsL);
        form.add(labsTF);
        form.add(pedPracticeL);
        form.add(pedPracticeTF);
        form.add(practiceL);
        form.add(practiceTF);
        form.add(finalExamL);
        form.add(finalExamTF);
        form.add(visitingL);
        form.add(visitingTF);
        form.add(specialityL);
        form.add(specialitiesCB);
        getContentPane().add(form, BorderLayout.CENTER);
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.LINE_AXIS));
        buttons.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttons.add(Box.createHorizontalGlue());
        buttons.add(ok);
        ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    subject.setFullName(fullnameTF.getText());
                    subject.setShortName(shortnameTF.getText());
                    subject.setAcedemicYear(acedemicYearTF.getText());
                    subject.setYear((yearTF.getText().equals("")) ? 0 : Integer.parseInt(yearTF.getText()));
                    subject.setTerm((termTF.getText().equals("")) ? 0 : Integer.parseInt(termTF.getText()));
                    subject.setStudentCount((studentCountTF.getText().equals("")) ? 0 : Integer.parseInt(studentCountTF.getText()));
                    subject.setGroupCount((groupCountTF.getText().equals("")) ? 0 : Integer.parseInt(groupCountTF.getText()));
                    subject.setSubGroupCount((subGroupCountTF.getText().equals("")) ? 0 : Integer.parseInt(subGroupCountTF.getText()));
                    subject.setLectures((lecturesTF.getText().equals("")) ? 0 : Integer.parseInt(lecturesTF.getText()));
                    subject.setLabs((labsTF.getText().equals("")) ? 0 : Integer.parseInt(labsTF.getText()));
                    subject.setPedPractice((pedPracticeTF.getText().equals("")) ? 0 : Integer.parseInt(pedPracticeTF.getText()));
                    subject.setPractice((practiceTF.getText().equals("")) ? 0 : Integer.parseInt(practiceTF.getText()));
                    subject.setFinalExam((finalExamTF.getText().equals("")) ? 0 : Integer.parseInt(finalExamTF.getText()));
                    subject.setVisiting((visitingTF.getText().equals("")) ? 0 : Integer.parseInt(visitingTF.getText()));
                    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
                    session.beginTransaction();
                    java.util.List result = session.createQuery("from Speciality where fullName = '" + specialitiesCB.getSelectedItem() + "'").list();
                    subject.setSpeciality((Speciality) result.get(0));
                    saveSubject(subject);
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
                if (isNew) list.remove(subject);
                setVisible(false);
            }
        });
        getContentPane().add(buttons, BorderLayout.PAGE_END);
        pack();
        setSize(300, 480);
        setResizable(false);
        setVisible(true);
    }

    private void saveSubject(Subject subject) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.saveOrUpdate(subject);
        session.getTransaction().commit();
    }
}
