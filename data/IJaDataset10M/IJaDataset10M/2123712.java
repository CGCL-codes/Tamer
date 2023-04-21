package sourceagile.testing.client.serverCalls;

import sourceagile.client.GlobalVariables;
import sourceagile.client.ProjectInitialization;
import sourceagile.shared.entities.entry.ClassFile;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Create a Test Class for the new To Do Class that is being created by the
 * user.
 */
public class AddTestClass {

    private final LoadRemoteTestClassesAsync remoteFunctions = GWT.create(LoadRemoteTestClasses.class);

    public AddTestClass(final ClassFile classFile) {
        this(classFile, null, false);
    }

    public AddTestClass(final ClassFile classFile, String newSubfolderName, final boolean newTask) {
        remoteFunctions.addTestClass(ProjectInitialization.currentProject, GlobalVariables.currentUser, classFile, newSubfolderName, new AsyncCallback<Void>() {

            @Override
            public void onSuccess(Void result) {
                if (!newTask) {
                    new GetRemoteTestClass(classFile);
                }
            }

            public void onFailure(Throwable caught) {
            }
        });
    }
}
