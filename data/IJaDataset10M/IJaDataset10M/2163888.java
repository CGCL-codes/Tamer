package com.aaron.server;

import java.util.ArrayList;
import com.aaron.client.ProjectService;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author aaron
 *
 */
public class ProjectServiceImpl extends RemoteServiceServlet implements ProjectService {

    static final long serialVersionUID = 456789;

    public void deleteProject(String name, String auth) {
        try {
            SessionServer session_server = new SessionServer();
            if (session_server.confirmAndSync(auth)) {
                ProjectServer project_server = new ProjectServer();
                String username = session_server.getUsername(auth);
                project_server.deleteProject(name, username);
            }
        } catch (DatabaseException e) {
            System.err.println("deleteProject:DatabaseException " + e.getMessage());
        }
    }

    public void renameProject(String name, String new_name, String auth) {
        try {
            SessionServer session_server = new SessionServer();
            if (session_server.confirmAndSync(auth)) {
                ProjectServer project_server = new ProjectServer();
                project_server.renameProject(name, new_name, session_server.getUsername(auth));
            }
        } catch (DatabaseException e) {
            System.err.println("renameProject:DatabaseException " + e.getMessage());
        }
    }

    public ArrayList getCollaborators(String project_name, String auth) {
        try {
            SessionServer session_server = new SessionServer();
            if (session_server.confirmAndSync(auth)) {
                ProjectServer project_server = new ProjectServer();
                String username = session_server.getUsername(auth);
                String path = project_server.getProjectPath(project_name, username);
                return project_server.getCollaborators(path);
            }
        } catch (DatabaseException e) {
            System.err.println("getCollaborators:DatabaseException " + e.getMessage());
        }
        return new ArrayList();
    }

    public void setCollaborators(ArrayList collaborators, String project_name, String auth) {
        try {
            SessionServer session_server = new SessionServer();
            if (session_server.confirmAndSync(auth)) {
                ProjectServer project_server = new ProjectServer();
                String username = session_server.getUsername(auth);
                String project_path = project_server.getProjectPath(project_name, username);
                project_server.setCollaborators(project_path, collaborators);
            }
        } catch (DatabaseException e) {
            System.err.println("setCollaborators:DatabaseException " + e.getMessage());
        }
    }

    public ArrayList listProjects(String key) {
        ArrayList projects = new ArrayList();
        return projects;
    }

    public String getProjectStats(String project_name, String auth) {
        String result = "";
        try {
            SessionServer session_server = new SessionServer();
            if (session_server.confirmAndSync(auth)) {
                ProjectServer project_server = new ProjectServer();
                String username = session_server.getUsername(auth);
                String project_path = project_server.getProjectPath(project_name, username);
                result = project_server.getProjectStats(project_path);
            }
        } catch (DatabaseException e) {
            System.err.println("setCollaborators:DatabaseException " + e.getMessage());
        }
        return result;
    }
}
