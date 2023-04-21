package sourceagile.server.classRepositories.subversionClassRepository.repositoryTest;

import sourceagile.shared.entities.User;
import sourceagile.shared.entities.entry.ClassFile;
import sourceagile.shared.entities.project.Project;
import sourceagile.testing.client.serverCalls.LoadRemoteTestClasses;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class RepositoryClassTestImplementation extends RemoteServiceServlet implements LoadRemoteTestClasses {

    @Override
    public ClassFile getTestClass(Project project, User user, ClassFile entry) {
        return GetRepositoryTestClass.getTestClassFile(project, user, entry);
    }

    @Override
    public void addTestClass(Project project, User user, ClassFile entry, String newSubfolderName) {
        new CreateTestClass(project, user, entry, newSubfolderName);
    }
}
