package org.efficientia.cimap.test;

import org.efficientia.cimap.model.Folder;
import org.efficientia.cimap.model.Server;

/**
 * Command-line test application for CIMAP.
 * Shall be replaced by JUnit test(s) later.
 * 
 * @author Ram�n Jim�nez (rjimenezz AT users DOT sourceforge DOT net)
 *
 */
public class ServerTest {

    public static void main(String[] args) {
        Server srv = new Server(args[0]);
        srv.connect();
        srv.login(args[1], args[2]);
        Folder inbox = srv.getFolders();
        new FolderProcessor(srv).saveFolder(inbox, "E:/");
        srv.logout();
    }
}
