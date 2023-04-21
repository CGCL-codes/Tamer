package xpusp.modeltest;

import xpusptest.XpuspCase;
import junit.framework.*;
import com.meterware.httpunit.*;
import xpusp.model.*;
import java.util.Properties;
import java.io.*;
import java.net.URL;

public class AllStudentsPreferencesTest extends XpuspCase {

    public AllStudentsPreferencesTest(String t) {
        super(t);
    }

    public void testAllStudentPreferencesCalculate() {
        int maxd = AllStudentsPreferences.MAX_OPTIONS;
        int maxs = 20;
        Discipline d[] = new Discipline[maxd];
        for (int i = 0; i < d.length; i++) d[i] = new Discipline("MAC-XX" + i);
        Student st[] = new Student[maxs];
        for (int i = 0; i < st.length; i++) {
            st[i] = new Student();
            for (int j = 0; j < d.length; j++) {
                StudentPreference sp = new StudentPreference();
                sp.addElective(d[j]);
                st[i].setPreferences(sp);
            }
        }
        AllStudentsPreferences asp = new AllStudentsPreferences(st);
        for (int i = 0; i < d.length; i++) {
            int vtByOpt = asp.getVotesByOption(d[i].getCode(), i, true);
        }
    }

    public static Test suite() {
        return new TestSuite(AllStudentsPreferencesTest.class);
    }
}
