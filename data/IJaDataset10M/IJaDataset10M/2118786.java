package sourceagile.server.classRepositories;

import java.io.File;
import junit.framework.TestCase;
import org.approvaltests.Approvals;
import org.approvaltests.UseReporter;
import sourceagile.ProjectsData;
import sourceagile.shared.entities.User;
import sourceagile.shared.entities.UserData;
import sourceagile.shared.entities.entry.ClassFile;
import sourceagile.shared.entities.project.Project;
import com.spun.util.io.FileUtils;

@UseReporter(ViDiffReporter.class)
public class RepositoryClassLoaderTest extends TestCase {

    public void testFeaturesList() throws Exception {
        User user = UserData.load()[0];
        Project project = ProjectsData.load()[1];
    }

    public void testGetEntryFeature() throws Exception {
        ClassFile entryFeature = new ClassFile();
        File file = new File("test/sourceagile/server/repositoryClassLoader/RepositoryClassLoaderTest.java");
        entryFeature.setSourceCode(FileUtils.readFile(file));
        Approvals.approve(ClassDocumetationUtilities.toString(entryFeature.getClassDoc()));
    }
}
