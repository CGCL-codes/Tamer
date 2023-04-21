package biz;

import java.util.ArrayList;
import java.util.List;
import models.AnswerOfAttempt;
import models.Attempt;
import models.Choose;
import models.ExaminationOfPaper;
import models.Manager;
import models.Student;
import models.Subject;
import org.junit.Test;
import biz.attemptMager.AttemptMagerBiz;
import biz.attemptMager.AttemptMagerBizImpl;
import biz.login.LoginBiz;
import biz.login.LoginBizImpl;
import biz.manager.ManagerBiz;
import biz.manager.ManagerBizImpl;
import biz.paperMager.PaperMagerBiz;
import biz.paperMager.PaperMagerBizImpl;
import biz.studentDiy.StudentDiyBiz;
import biz.studentDiy.StudentDiyBizImpl;
import biz.studentMager.StudentMagerBiz;
import biz.studentMager.StudentMagerBziImpl;
import biz.subjectMager.SubjectMagerBiz;
import biz.subjectMager.SubjectMagerBizImpl;
import biz.sysService.SysServiceBiz;
import biz.sysService.SysServiceBizImpl;

public class tst {

    @Test
    public void tst1() {
        ManagerBiz magDao = new ManagerBizImpl();
        Manager mag = new Manager();
        mag.setDepartment("���˰");
        mag.setGender(false);
        mag.setName("����ʦ");
        mag.setPassword("1111");
        mag.setPosition("��ʦ");
        magDao.addManager(mag);
    }

    @Test
    public void tst2() {
        ManagerBiz magDao = new ManagerBizImpl();
        Manager mag = magDao.selManagerById(1l);
        mag.setName("����ʦ");
        magDao.updateManager(mag);
    }

    @Test
    public void tst3() {
        StudentMagerBiz stuBiz = new StudentMagerBziImpl();
        Student stu = new Student();
        stu.setId(5l);
        stu.setName("��Ůѧ��");
        stuBiz.updateStudent(stu);
    }

    @Test
    public void tst4() {
        LoginBiz loginBiz = new LoginBizImpl();
        Student stu = loginBiz.selStudentByNAP("��ѧ��", "212321");
        Manager mag = loginBiz.selManagerByNAP("����ʦ", "1111");
        System.out.println(stu.getStuNo() + " " + mag.getDepartment());
    }

    @Test
    public void tst5() {
        StudentDiyBiz stuDiyBiz = new StudentDiyBizImpl();
        List<Choose> l = (List<Choose>) stuDiyBiz.getAllChooseByStuId(6l);
        for (Choose c : l) {
            System.out.println(c.getPaprId() + " " + c.getScoreOfPaper());
            System.out.println(c.getStudent().getName());
        }
    }

    @Test
    public void tst6() {
        List<Long> l = new ArrayList<Long>();
        l.add(1l);
        l.add(2l);
        Object[] ll = l.toArray();
        System.out.println((Long[]) ll);
    }

    @Test
    public void tstSys() {
        SysServiceBiz ssb = new SysServiceBizImpl();
        AnswerOfAttempt aoa = new AnswerOfAttempt();
        aoa.setAttId(1l);
        aoa.setAnswer("dsds sdsd sd�ܵ�");
        Student stu = new Student();
        stu.setId(6l);
        Choose c = new Choose();
        c.addAnswerOfList(aoa);
        c.setExamType(3);
        c.setInaccessibleOfmodulus(0.6);
        c.setPaprId(1l);
        c.setSubjectId(1l);
        c.setScoreOfPaper(93);
        c.setStudent(stu);
        ExaminationOfPaper eop = ssb.getChoosePaper(c);
        System.out.println(eop.getPaperOfName());
        List<Attempt> ll = eop.getAttemptOfList();
        for (Attempt att : ll) {
            System.out.println(att.getExamination() + " " + att.getExamOption());
        }
    }

    @Test
    public void tstSub() {
        SubjectMagerBiz subBiz = new SubjectMagerBizImpl();
        Subject sub = new Subject();
        sub.setId(7l);
        sub.setSubjectOfName("��õ���ѧ");
        subBiz.modifySubject(sub);
    }

    @Test
    public void tstPaper() {
        PaperMagerBiz paperBiz = new PaperMagerBizImpl();
        Subject subject = paperBiz.getSubjectById(12l);
        ExaminationOfPaper paper = paperBiz.getPaperById(31l);
        paper.setSubject(subject);
        paper.setPaperOfName("�δ����e����");
        paperBiz.modifyPaper(paper);
    }

    @Test
    public void tstAttempt() {
        AttemptMagerBiz attMagBiz = new AttemptMagerBizImpl();
        List<Attempt> atts = (List<Attempt>) attMagBiz.getAllAttempts();
        for (Attempt att : atts) {
            System.out.println(att.getExamination() + "  " + att.getExamOfPapr().getPaperOfName());
        }
    }
}
