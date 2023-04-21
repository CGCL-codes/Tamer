package net.sourceforge.cathcart.taskdefs;

import static org.mockito.Mockito.*;
import java.io.File;
import net.sourceforge.cathcart.typedefs.PropertyType;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.taskdefs.Mkdir;
import org.apache.tools.ant.taskdefs.Move;
import org.apache.tools.ant.taskdefs.XSLTProcess;
import org.apache.tools.ant.taskdefs.XmlProperty;
import org.apache.tools.ant.util.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CheckPreviousBackupTest {

    @Mock
    private Mkdir mkdir;

    @Mock
    private XSLTProcess xslt;

    @Mock
    private XmlProperty xmlProperty;

    @Mock
    private Copy copy;

    @Mock
    private Move move;

    @Mock
    private Project project;

    private final CheckPrevious check = new CheckPrevious();

    File out;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        check.setMkdir(mkdir);
        check.setXslt(xslt);
        check.setXmlProperty(xmlProperty);
        check.setCopy(copy);
        check.setMove(move);
        check.setTool("jmeter");
        check.parseConfig();
        out = FileUtils.getFileUtils().createTempFile("CATHCART", ".xml", null, true, true);
        check.setProject(project);
        check.setInXml("testdata/jmeter.jtl");
        when(xslt.getProject()).thenReturn(project);
        when(xmlProperty.getProject()).thenReturn(project);
        when(mkdir.getProject()).thenReturn(project);
        when(copy.getProject()).thenReturn(project);
        when(move.getProject()).thenReturn(project);
    }

    @After
    public void tearDown() throws Exception {
        out = null;
    }

    @Test
    public void shouldntBackup() {
        project.setProperty("failurecount", "54");
        check.setBackup(false);
        PropertyType pt = new PropertyType();
        pt.setName("failurecount");
        pt.setLeeway(10);
        check.addConfigured(pt);
        check.execute();
        verifyZeroInteractions(move);
    }
}
