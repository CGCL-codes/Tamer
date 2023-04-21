package swin.metrictool.intentions;

import static org.junit.Assert.assertEquals;
import java.util.Vector;
import junit.framework.JUnit4TestAdapter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import swin.metrictool.ExtractVersionMetrics;
import swin.metrictool.ExtractedVersionData;
import swin.metrictool.Project;
import swin.metrictool.ProjectList;
import swin.metrictool.XStreamUtility;

public class XStreamUtilityIntention {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(XStreamUtilityIntention.class);
    }

    private static Project project;

    private static ProjectList projectList;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        String[] junitJar = new String[] { "lib/xpp3_min-1.1.3.4.O.jar" };
        ExtractVersionMetrics evm = new ExtractVersionMetrics();
        ExtractedVersionData evd = evm.getMetrics(junitJar);
        Vector<ExtractedVersionData> versions = new Vector<ExtractedVersionData>();
        versions.add(evd);
        project = new Project("xpp3", "2005-08-11", "lib/xpp3_min-1.1.3.4.O.jar");
        project.setVersions(versions);
        projectList = new ProjectList();
        Vector<String> projects = new Vector<String>();
        projects.add(project.getShortName());
        projectList.setProjects(projects);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        project = null;
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetXmlProject() {
        String actualProjectXml = XStreamUtility.getXml(project);
        String actual = actualProjectXml.replaceAll("\\s+", "");
        String expected = projectXml.replaceAll("\\s+", "");
        assertEquals("Project xml did not match expected results.", expected, actual);
    }

    @Test
    public void testGetXmlProjectList() {
        String actualProjectListXml = XStreamUtility.getXml(projectList);
        String actual = actualProjectListXml.replaceAll("\\s+", "");
        String expected = projectListXml.replaceAll("\\s+", "");
        assertEquals("Project List xml did not match expected results.", expected, actual);
    }

    @Test
    public void testGetProject() {
        Project actualProject = XStreamUtility.getProject(projectXml);
        assertEquals("Rehydrated Project did not match xml.", project.getDescription(), actualProject.getDescription());
    }

    @Test
    public void testGetProjectList() {
        ProjectList actualProjectList = XStreamUtility.getProjectList(projectListXml);
        assertEquals("Rehydrated Project List did not have the expected line count.", 1, actualProjectList.getProjects().size());
    }

    private static String projectListXml = "<projectlist>" + "  <projects>" + "    <string>xpp3</string>" + "  </projects>" + "</projectlist>";

    private static String projectXml = "<project>" + "  <shortName>xpp3</shortName>" + "  <creationDate>2005-08-11</creationDate>" + "  <description>lib/xpp3_min-1.1.3.4.O.jar</description>" + "  <versions>" + "    <versiondata>" + "      <sequenceNumber>0</sequenceNumber>" + "      <totalInstanceVariables>288</totalInstanceVariables>" + "      <totalByteCode>9765</totalByteCode>" + "      <totalPrivateMethodCount>3</totalPrivateMethodCount>" + "      <totalMethodCount>122</totalMethodCount>" + "      <fanInSummary>" + "        <entry>" + "          <int>1</int>" + "          <int>3</int>" + "        </entry>" + "      </fanInSummary>" + "      <fanOutSummmary>" + "        <entry>" + "          <int>6</int>" + "          <int>2</int>" + "        </entry>" + "        <entry>" + "          <int>16</int>" + "          <int>1</int>" + "        </entry>" + "      </fanOutSummmary>" + "      <classList>" + "        <entry>" + "          <string>org/xmlpull/mxp1/MXParser</string>" + "          <swin.metrictool.ExtractedClassData>" + "            <instanceVariables>276</instanceVariables>" + "            <classSize>9526</classSize>" + "            <privateMethodCount>3</privateMethodCount>" + "            <methodCount>78</methodCount>" + "            <className>org/xmlpull/mxp1/MXParser</className>" + "            <fanInList>" + "              <string>org/xmlpull/mxp1/MXParser</string>" + "            </fanInList>" + "            <fanOutList>" + "              <string>java/io/EOFException</string>" + "              <string>java/io/IOException</string>" + "              <string>java/io/InputStream</string>" + "              <string>java/io/InputStreamReader</string>" + "              <string>java/io/Reader</string>" + "              <string>java/lang/Boolean</string>" + "              <string>java/lang/IllegalArgumentException</string>" + "              <string>java/lang/IndexOutOfBoundsException</string>" + "              <string>java/lang/Integer</string>" + "              <string>java/lang/Object</string>" + "              <string>java/lang/Runtime</string>" + "              <string>java/lang/String</string>" + "              <string>java/lang/StringBuffer</string>" + "              <string>java/lang/System</string>" + "              <string>org/xmlpull/mxp1/MXParser</string>" + "              <string>org/xmlpull/v1/XmlPullParserException</string>" + "            </fanOutList>" + "          </swin.metrictool.ExtractedClassData>" + "        </entry>" + "        <entry>" + "          <string>org/xmlpull/v1/XmlPullParserException</string>" + "          <swin.metrictool.ExtractedClassData>" + "            <instanceVariables>12</instanceVariables>" + "            <classSize>152</classSize>" + "            <privateMethodCount>0</privateMethodCount>" + "            <methodCount>6</methodCount>" + "            <className>org/xmlpull/v1/XmlPullParserException</className>" + "            <fanInList>" + "              <string>org/xmlpull/mxp1/MXParser</string>" + "              <string>org/xmlpull/v1/XmlPullParser</string>" + "            </fanInList>" + "            <fanOutList>" + "              <string>java/io/PrintStream</string>" + "              <string>java/lang/Exception</string>" + "              <string>java/lang/String</string>" + "              <string>java/lang/StringBuffer</string>" + "              <string>java/lang/Throwable</string>" + "              <string>org/xmlpull/v1/XmlPullParser</string>" + "            </fanOutList>" + "          </swin.metrictool.ExtractedClassData>" + "        </entry>" + "        <entry>" + "          <string>org/xmlpull/v1/XmlPullParser</string>" + "          <swin.metrictool.ExtractedClassData>" + "            <instanceVariables>0</instanceVariables>" + "            <classSize>87</classSize>" + "            <privateMethodCount>0</privateMethodCount>" + "            <methodCount>38</methodCount>" + "            <className>org/xmlpull/v1/XmlPullParser</className>" + "            <fanInList>" + "              <string>org/xmlpull/v1/XmlPullParserException</string>" + "            </fanInList>" + "            <fanOutList>" + "              <string>java/io/IOException</string>" + "              <string>java/io/InputStream</string>" + "              <string>java/io/Reader</string>" + "              <string>java/lang/Object</string>" + "              <string>java/lang/String</string>" + "              <string>org/xmlpull/v1/XmlPullParserException</string>" + "            </fanOutList>" + "          </swin.metrictool.ExtractedClassData>" + "        </entry>" + "      </classList>" + "      <packageList>" + "        <string>org/xmlpull/v1</string>" + "        <string>org/xmlpull/mxp1</string>" + "      </packageList>" + "    </versiondata>" + "  </versions>" + "  <directory></directory>" + "</project>";
}
