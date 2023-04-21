package oqube.patchwork.report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import oqube.patchwork.report.SimpleReporter;
import oqube.patchwork.report.coverage.CoverageObjective;
import oqube.patchwork.report.coverage.ObjectiveVisitor;
import oqube.patchwork.report.source.SourceMapper;
import junit.framework.TestCase;

public class SimpleReporterTest extends MockObjectTestCase {

    private SimpleReporter reporter;

    private Mock mockobj;

    private byte[] data;

    protected void setUp() throws Exception {
        super.setUp();
        this.reporter = new SimpleReporter();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeInt(2);
        dos.writeUTF("toto");
        dos.writeInt(1);
        dos.writeUTF("do1()V");
        dos.writeUTF("tutu");
        dos.writeInt(2);
        dos.writeUTF("dont(I)V");
        dos.writeUTF("dont(Ljava/lang/String;)V");
        write(dos, 1, 0, 0, 0);
        write(dos, 1, 0, 0, 1);
        write(dos, 1, 0, 0, 2);
        write(dos, 1, 0, 0, -1);
        write(dos, 1, 1, 0, 0);
        write(dos, 1, 1, 0, 3);
        write(dos, 1, 1, 1, 0);
        write(dos, 1, 1, 1, 1);
        write(dos, 1, 1, 1, -1);
        write(dos, 1, 1, 0, 4);
        write(dos, 1, 1, 0, -1);
        dos.writeLong(-1L);
        dos.flush();
        this.data = bos.toByteArray();
        this.mockobj = mock(CoverageObjective.class);
    }

    private void write(DataOutputStream dos, int tid, int cix, int mix, int bix) throws IOException {
        long val = ((long) tid << 48) | ((long) cix << 32) | ((long) mix << 16) | (bix & 0x000000000000ffff);
        dos.writeLong(val);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
   * basic test for reading classes and methods names.
   * @throws IOException 
   *
   */
    public void test01ReadClasse() throws IOException {
        InputStream is = getClass().getResourceAsStream("/patchworksample");
        String[][] cms = reporter.readClassesAndMethods(is);
        assertEquals("Invalid class name", "sudoku/SudokuPanel", cms[0][0]);
        assertEquals("Invalid method name", "sudoku/SudokuPanel.getPreferredSize()Ljava/awt/Dimension;", cms[0][3]);
    }

    public void test03Report() throws IOException {
        InputStream is = new ByteArrayInputStream(data);
        mockobj.expects(once()).method("update").with(eq(1), eq("toto.do1()V"), eq(0));
        mockobj.expects(once()).method("update").with(eq(1), eq("toto.do1()V"), eq(1));
        mockobj.expects(once()).method("update").with(eq(1), eq("toto.do1()V"), eq(2));
        mockobj.expects(once()).method("update").with(eq(1), eq("toto.do1()V"), eq(-1));
        mockobj.expects(once()).method("update").with(eq(1), eq("tutu.dont(I)V"), eq(0));
        mockobj.expects(once()).method("update").with(eq(1), eq("tutu.dont(I)V"), eq(3));
        mockobj.expects(once()).method("update").with(eq(1), eq("tutu.dont(Ljava/lang/String;)V"), eq(0));
        mockobj.expects(once()).method("update").with(eq(1), eq("tutu.dont(Ljava/lang/String;)V"), eq(1));
        mockobj.expects(once()).method("update").with(eq(1), eq("tutu.dont(Ljava/lang/String;)V"), eq(-1));
        mockobj.expects(once()).method("update").with(eq(1), eq("tutu.dont(I)V"), eq(4));
        mockobj.expects(once()).method("update").with(eq(1), eq("tutu.dont(I)V"), eq(-1));
        mockobj.expects(once()).method("visit");
        reporter.setObjective((CoverageObjective) mockobj.proxy());
        reporter.analyze(is);
        reporter.report(System.err);
    }
}
