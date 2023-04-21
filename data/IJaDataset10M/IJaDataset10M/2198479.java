package Workspace;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * This Handler manages the list where all Workspaces (of all kinds) are added. It provides the
 * access to list ready for Threads
 * @author Johannes Putzke
 */
public interface WorkspaceHandler {

    List<Workspace> wsList = Collections.synchronizedList(new LinkedList<Workspace>());

    /**
	 * Add a Workspace to the handler. All Workspaces which are added to the handler are visible
	 * for other Clients. Only a workspace in the WorkspaceHandler share the entered Objects
	 * (draw, text etc) 
	 *
	 * @param workspace: A new Workspace to the handler
	 * @throws WorkspaceIDExistException It the WorkspaceID of the Workspace already exist in the Handler
	 * @throws ClassCastException If the argument was not a Workspace
	 * @throws NullPointerException If the argument was a null pointer
	 */
    public void addWorspace(Workspace workspace, int clientID) throws WorkspaceIDExistException, ClassCastException, NullPointerException;

    /**
	 * Remove a workspace (of any kind) from the WorkspaceHandler (intern List)
	 * 
	 * @param workspace The to removed Workspace
	 * @param clientID The clientID which tries to remove the workspace from WorkspaceList
	 * @throws WorkspaceIsLockedException If a user has locked the workspace
	 * @throws ClassCastException If the argument was not a Workspace
	 * @throws NullPointerException if the argument was a null pointer
	 * @throws noPermissionToRemoveWorkspaceException If the client with the clientID (param)
	 *  has not the permissions to remove the workspace
	 */
    public void removeWorkspace(Workspace workspace, int clientID) throws WorkspaceIsLockedException, noPermissionToRemoveWorkspaceException, ClassCastException, NullPointerException;

    /**
	 * Select a Workspace by its ID and return it. The Workspace must in the list.
	 * If the workspace can not be found, the return value is null;
	 * 
	 * @param ID The ID of the Workspace
	 * @return The Workspace for the ID (parameter). If doesn't exist returns null
	 */
    public Workspace getWorkspace(int ID);

    /**
	 * This Exception is thrown , if an new Workspace is going to add to an
	 * WorkspaceHandler and the ID of the new Workspace is already used by
	 * an other Workspace in the handled list
	 * 
	 * @author Johannes Putzke
	 */
    public class WorkspaceIDExistException extends Exception {

        private static final long serialVersionUID = 1L;

        public WorkspaceIDExistException() {
            System.err.println("The Workspace has an ID which is already used by another Workspace");
        }
    }

    /**
	 * This Exception is thrown, if a workspace is going to removed from a 
	 * the list in the WorkspaceHandler, but is still locked by a user.
	 * 
	 * @author Johannes Putzke
	 */
    public class WorkspaceIsLockedException extends Exception {

        private static final long serialVersionUID = 1L;

        public WorkspaceIsLockedException() {
            System.err.println("The Workspace is locked and because of that can't be removed");
        }
    }

    /**
	 * This Exception is thrown, if someone tries to remove a workspace but has to 
	 * the permissions to do that (not the owner or admin).
	 * @author Johannes Putzke
	 */
    public class noPermissionToRemoveWorkspaceException extends Exception {

        private static final long serialVersionUID = 1L;

        public noPermissionToRemoveWorkspaceException() {
            System.err.println("You are not allowed to remove this workspace." + "Please get the permissions from your admin");
        }
    }

    /**
	 * This Exception is thrown, if someone tries to name a workspace with a 
	 * name, which is already used by an other workspace
	 * @author Johannes Putzke
	 */
    public class WorkspaceNameExistException extends Exception {

        private static final long serialVersionUID = 1L;

        public WorkspaceNameExistException() {
            System.err.println("The workspace name is already used by an other " + "workspace");
        }
    }
}
