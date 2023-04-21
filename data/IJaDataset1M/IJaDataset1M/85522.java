package uk.ac.ed.rapid.button;

import junit.framework.TestCase;
import org.apache.commons.vfs.FileObject;
import org.mockito.Mockito;
import uk.ac.ed.rapid.button.mock.MockedObjectsFactory;
import uk.ac.ed.rapid.data.impl.RapidDataImpl;
import uk.ac.ed.rapid.jobdata.JobDataImpl;
import uk.ac.ed.rapid.jobdata.VariableResolver;
import uk.ac.ed.rapid.value.impl.SingleValue;

public class LoadFileTest extends TestCase {

    public void testLoadFile() {
        try {
            RapidDataImpl rapidData = MockedObjectsFactory.createRapidData();
            MockedObjectsFactory.addFileSystem(rapidData);
            MockedObjectsFactory.addJobData(rapidData, new JobDataImpl());
            FileObject mockedFileObject = MockedObjectsFactory.addFileObject(rapidData);
            MockedObjectsFactory.addFileContent(mockedFileObject);
            JobDataImpl jobData = (JobDataImpl) rapidData.getJobDataTable().getJobData();
            jobData.setVariable("destination", new SingleValue(""));
            jobData.setVariable("substitute", new SingleValue("path/to"));
            jobData.setVariable("filesystem", new SingleValue(MockedObjectsFactory.FILESYSTEMNAME));
            LoadFileAction action = new LoadFileAction();
            action.setFileSystem("$(filesystem)");
            action.setPath("/$(substitute)/file");
            action.setVariable("destination");
            action.performAction(rapidData);
            Mockito.verify(mockedFileObject).resolveFile("/path/to/file");
            assertEquals(MockedObjectsFactory.FILECONTENTS, VariableResolver.getVariable("destination", rapidData).get());
        } catch (Exception ex) {
            fail();
        }
    }
}
