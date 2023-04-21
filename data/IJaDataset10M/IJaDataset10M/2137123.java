package xpusptest;

import junit.framework.*;
import com.meterware.httpunit.*;
import xpusp.controller.*;
import java.util.*;
import java.io.*;
import java.net.URL;
import xpusp.*;
import xpusp.model.*;
import org.apache.log4j.Category;

public class DataSourceTest extends XpuspCase {

    static Category logger = Category.getInstance(DataSourceTest.class.getName());

    public DataSourceTest(String t) {
        super(t);
    }

    private DataSource getDs() {
        return DataSource.getInstance();
    }

    public void testInstance() {
        DataSource ds1, ds2;
        ds1 = DataSource.getInstance();
        ds2 = DataSource.getInstance();
        assertTrue(ds1 == ds2);
    }

    public void testAdmin() {
        Admin ad = new Admin();
        ad.setLogin("teste");
        ad.setPassword("abcdef");
        Person p = new Person();
        p.setEmail("teste@xpusp.com.yp");
        p.setName("Senhor excelentissimo teste");
        ad.setData(p);
        DataSource ds = getDs();
        try {
            ds.marshal(ad);
        } catch (Exception e) {
            fail("Exception " + e);
        }
        Admin ad2 = null;
        try {
            ad2 = (Admin) ds.getUser("teste");
        } catch (Exception e) {
            fail("Exception " + e);
        }
        assertEquals("Admins iguais nao sao iguais", ad, ad2);
    }

    public void testProfessor() {
        DataSource ds = getDs();
        try {
            Professor ad = new Professor();
            ad.setLogin("teste1");
            ad.setPassword("abcdef");
            Person p = new Person();
            p.setEmail("teste1@xpusp.com.yp");
            p.setName("Senhor excelentissimo teste professor");
            ad.setData(p);
            Capability capa = new Capability();
            Discipline cannot = new Discipline();
            cannot.setPreferredSemester(2);
            cannot.setName("Programacao Linear");
            cannot.setCode("MAC-215");
            ds.marshal(cannot);
            capa.addCannot(cannot);
            cannot = new Discipline();
            cannot.setPreferredSemester(1);
            cannot.setName("Prgramacao Extream");
            cannot.setCode("MAC-XXX");
            ds.marshal(cannot);
            capa.addCannot(cannot);
            ad.setCapability(capa);
            ProfessorPreferences pref = new ProfessorPreferences();
            pref.setNumberOfGroups(ProfessorPreferences.POLI, 10);
            pref.setNumberOfGroups(ProfessorPreferences.PRIMEIRO_SEMESTRE, 2);
            pref.setNumberOfGroups(ProfessorPreferences.SEGUNDO_SEMESTRE, 902);
            cannot = new Discipline();
            cannot.setPreferredSemester(1);
            cannot.setName("Matematica Aplicada");
            cannot.setCode("MAP-123");
            ds.marshal(cannot);
            pref.addWantToTeach(cannot);
            cannot = new Discipline();
            cannot.setPreferredSemester(1);
            cannot.setName("Anatomia em Braile");
            cannot.setCode("ANATO-69");
            ds.marshal(cannot);
            pref.addWantToTeach(cannot);
            Period notavailable = new Period();
            notavailable.setStart(new Date(0));
            notavailable.setEnd(new Date(60 * 60 * 24 * 365));
            pref.setNotAvailable(notavailable);
            Schedule sched = new Schedule();
            Period pr = new Period();
            pr.setStart(new Date(25));
            pr.setEnd(new Date(30));
            sched.addPeriod(pr);
            pr = new Period();
            pr.setStart(new Date(35));
            pr.setEnd(new Date(40));
            sched.addPeriod(pr);
            pref.setTimeRestrictions(sched);
            ad.setPreferences(pref);
            ds.marshal(ad);
            Professor ad2 = null;
            logger.debug("Getting user teste1");
            ad2 = (Professor) ds.getUser("teste1");
            assertEquals("Professores iguais nao sao iguais", ad, ad2);
        } catch (Exception e) {
            fail("Exception fetching user teste1" + e);
        }
    }

    public void testStudent() {
        DataSource ds = getDs();
        try {
            Student s = new Student();
            s.setStudentNr(2927800);
            s.setProbableGraduate(true);
            s.setCourse(45051);
            Person p = new Person();
            p.setEmail("aluno1@xpusp.com.yp");
            p.setName("Senhor excelentissimo teste aluno");
            s.setData(p);
            StudentPreference sp = new StudentPreference();
            sp.setNumOptionals(3);
            sp.setObs("Eu odeio grafos com o Paulo Feofiloff!");
            Vector elective = new Vector();
            Discipline disc = new Discipline();
            disc.setPreferredSemester(2);
            disc.setName("Programacao Linear");
            disc.setCode("MAC-215");
            ds.marshal(disc);
            elective.add(disc);
            disc = new Discipline();
            disc.setPreferredSemester(1);
            disc.setName("Prgramacao Extream");
            disc.setCode("MAC-XXX");
            ds.marshal(disc);
            elective.add(disc);
            disc = new Discipline();
            disc.setPreferredSemester(1);
            disc.setName("Matematica Aplicada");
            disc.setCode("MAP-123");
            ds.marshal(disc);
            elective.add(disc);
            Vector required = new Vector();
            disc.setPreferredSemester(2);
            disc.setName("Programacao Nao Linear");
            disc.setCode("MAC-315");
            ds.marshal(disc);
            required.add(disc);
            disc = new Discipline();
            disc.setPreferredSemester(1);
            disc.setName("Prgramacao Nao Extream");
            disc.setCode("MAC-YYY");
            ds.marshal(disc);
            required.add(disc);
            disc = new Discipline();
            disc.setPreferredSemester(1);
            disc.setName("Matematica Nao Aplicada");
            disc.setCode("MAP-223");
            ds.marshal(disc);
            required.add(disc);
            sp.setElective(elective);
            sp.setRequired(required);
            s.setPreferences(sp);
            ds.marshal(s);
            Student s2 = null;
            s2 = ds.getStudent(2927800);
            assertEquals("Alunos iguais nao sao iguais", s, s2);
        } catch (Exception e) {
            fail("Exception " + e);
        }
    }

    public void testDiscipline() {
        Discipline disc = null;
        disc = new Discipline();
        disc.setPreferredSemester(1);
        disc.setRequired(false);
        disc.setName("Prgramacao Nao Extream");
        disc.setCode("MAC-YYY");
        DataSource ds = getDs();
        Discipline disc2 = null;
        try {
            ds.marshal(disc);
            disc2 = ds.getDiscipline(disc.getCode());
        } catch (Exception e) {
            fail("EX: " + e);
        }
        assertEquals("Disciplinas iguais mas diferentes", disc, disc2);
        disc = new Discipline();
        disc.setPreferredSemester(2);
        disc.setRequired(true);
        disc.setName("Estruturas de Dados");
        disc.setCode("MAC-323");
        try {
            ds.marshal(disc);
            disc2 = ds.getDiscipline(disc.getCode());
        } catch (Exception e) {
            fail("EX: " + e);
        }
        assertEquals("Disciplinas iguais mas diferentes", disc, disc2);
        disc = new Discipline();
        disc.setPreferredSemester(4);
        disc.setRequired(true);
        disc.setName("Introducao a Computacao");
        disc.setCode("MAC-110");
        try {
            ds.marshal(disc);
            disc2 = ds.getDiscipline(disc.getCode());
        } catch (Exception e) {
            fail("EX: " + e);
        }
        assertEquals("Disciplinas iguais mas diferentes", disc, disc2);
    }

    public void testAllStudentsPreferences() {
        try {
            DataSource ds = DataSource.getInstance();
            AllStudentsPreferences p = ds.getAllStudentsPreferences();
            Student stds[] = ds.getAllStudents();
            assertTrue("Same number", p.getAllStudents().length == stds.length);
            for (int i = 0; i < stds.length; i++) assertEquals("Preferences are not equal", stds[i], p.getAllStudents()[i]);
        } catch (Exception e) {
            fail("EX: " + e);
        }
    }

    public static Test suite() {
        return new TestSuite(DataSourceTest.class);
    }
}
