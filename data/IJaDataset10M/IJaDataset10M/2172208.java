package org.jcvi.vics.web.gwt.download.server;

import junit.framework.TestCase;
import org.jcvi.vics.web.gwt.common.client.model.download.DownloadableDataNode;
import org.jcvi.vics.web.gwt.common.client.model.download.DownloadableDataNodeImpl;
import org.jcvi.vics.web.gwt.download.client.model.Project;
import org.jcvi.vics.web.gwt.download.client.model.Publication;
import java.util.List;

/**
 * User: Lfoster
 * Date: Oct 4, 2006
 * Time: 3:07:09 PM
 *
 * JUnit tester for the XML-reading implementation of a Helper Test for
 * 'Download' server-side-GWT.
 */
public class XmlPublicationHelperTest extends TestCase {

    public XmlPublicationHelperTest() {
        super("org.jcvi.vics.web.gwt.download.server.XmlPublicationHelperTest");
    }

    public XmlPublicationHelperTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * This method does not look at files on disk.  This can be useful if the full repository of files is not
     * available, but the XML files describing them are.
     *
     * @throws Exception
     */
    public void testExerciseXMLMetaData() throws Exception {
        testExercisePublicationHelper(false);
    }

    /**
     * This method checks files on disk.  The full repository of files must be
     * available, along with the XML files describing them.
     *
     * @throws Exception
     */
    public void testExerciseDownloadableFiles() throws Exception {
        testExercisePublicationHelper(true);
    }

    /**
     * First configure this thing, as Spring would do;  then
     * call it via the Interface.
     *
     * @throws Exception any thrown in processing.
     */
    private void testExercisePublicationHelper(boolean testDataFiles) throws Exception {
        XmlPublicationHelper xmlPublicationHelper = new XmlPublicationHelper();
        PublicationHelper helper = xmlPublicationHelper;
        xmlPublicationHelper.setProjectLocationMap("[GOS;Viral][GOS;Viral]");
        xmlPublicationHelper.setProjectBaseLocation("c:\\\\test_and_learn\\\\download\\\\");
        String projectOfInterest = "GOS";
        Project project = helper.getProjectByName(projectOfInterest);
        if (project == null) {
            fail("Found no project for: " + projectOfInterest);
        }
        String projectName = project.getProjectName();
        if (projectName == null) {
            fail("Found no project name for project: " + projectOfInterest);
        }
        List<Publication> publications = project.getPublications();
        failOnEmptyList(publications, "Found no publications in project: " + projectOfInterest);
        for (Publication pub : publications) {
            failOnNullValue(pub.getAbstract(), "No abstract in publication: " + pub);
            failOnNullValue(pub.getSummary(), "No summary in publication: " + pub);
            failOnNullValue(pub.getTitle(), "No title in publication: " + pub);
            failOnEmptyList(pub.getAuthors(), "No authors found for publication: " + pub);
            DownloadableDataNode node = pub.getSubjectDocument();
            failOnNullValue(node, "No subject document found for: " + pub);
            List<DownloadableDataNodeImpl> dataFiles = pub.getDataFiles();
            failOnEmptyList(dataFiles, "Empty top-level data files list for: " + pub);
            if (testDataFiles) testDataFiles(dataFiles);
        }
    }

    public void testHasNew() {
        XmlPublicationHelper xmlPublicationHelper = new XmlPublicationHelper();
        PublicationHelper helper = xmlPublicationHelper;
        xmlPublicationHelper.setProjectLocationMap("[GOS;Viral][GOS;Viral]");
        xmlPublicationHelper.setProjectBaseLocation("c:\\\\test_and_learn\\\\download\\\\");
        String projectOfInterest = "GOS";
        Project project = helper.getProjectByName(projectOfInterest);
        if (project == null) {
            fail("Found no project for: " + projectOfInterest);
        }
        List newFiles = helper.getNewFiles();
        if (newFiles == null || newFiles.size() == 0) {
            fail("Found no new files in helper.");
        }
    }

    public void testHasCombined() {
        XmlPublicationHelper xmlPublicationHelper = new XmlPublicationHelper();
        PublicationHelper helper = xmlPublicationHelper;
        xmlPublicationHelper.setProjectLocationMap("[GOS;Viral][GOS;Viral]");
        xmlPublicationHelper.setProjectBaseLocation("c:\\\\test_and_learn\\\\download\\\\");
        String projectOfInterest = "GOS";
        Project project = helper.getProjectByName(projectOfInterest);
        if (project == null) {
            fail("Found no project for: " + projectOfInterest);
        }
        List rolledUpArchives = project.getRolledUpArchivesOfPublications();
        if (rolledUpArchives == null || rolledUpArchives.size() == 0) {
            fail("Found no rolled-up archives for project: " + projectOfInterest);
        }
    }

    /**
     * Recursive method, to allow descent through all data files, and application
     * of same test to all of them.
     *
     * @param dataFiles list of nodes to check.  Each may have children.
     */
    private void testDataFiles(List<DownloadableDataNodeImpl> dataFiles) {
        if (dataFiles == null) return;
        for (DownloadableDataNode dataFile : dataFiles) {
            String attributes[] = dataFile.getAttributeNames();
            if (attributes == null) {
                fail("No attributes on dataFile: " + dataFile);
            }
            failOnNullValue(dataFile.getLocation(), "No location for dataFile: " + dataFile.getText());
            if (dataFile.getChildren() != null && dataFile.getChildren().size() == 0 && dataFile.getSize() <= 0) {
                fail("Found no size for dataFile: " + dataFile.getLocation());
            }
            failOnNullValue(dataFile.getText(), "No short description for dataFile: " + dataFile.getLocation());
            List<DownloadableDataNodeImpl> children = dataFile.getChildren();
            testDataFiles(children);
        }
    }

    /**
     * Convenience method to help with failing stuff.  Common test: empty list.
     *
     * @param list what might be empty or null.
     * @param msg what to say if so.
     */
    private void failOnEmptyList(List list, String msg) {
        if (list == null || list.size() == 0) fail(msg);
    }

    /**
     * Convenience method to help with failing stuff.  Common test: null object.
     *
     * @param val what might be null.
     * @param msg what to say if so.
     */
    private void failOnNullValue(Object val, String msg) {
        if (val == null) fail(msg);
    }
}
