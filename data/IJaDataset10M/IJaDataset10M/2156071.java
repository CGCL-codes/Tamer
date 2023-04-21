package com.aaron.server;

import com.aaron.client.Document;
import java.io.*;
import org.tmatesoft.svn.core.SVNException;

/**
 * @author aaron
 *
 */
public class DocumentServer {

    public String getFileContents(String path) {
        String result = "";
        try {
            File file = new File(path);
            if (file != null && file.exists() && file.canRead() && file.isFile()) {
                FileInputStream fis = new FileInputStream(file);
                DataInputStream in = new DataInputStream(fis);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String strLine;
                while ((strLine = br.readLine()) != null) {
                    result += strLine + "\n";
                }
            } else if (file == null) {
                System.err.println("getFileContents: file is null");
            } else if (!file.exists()) {
                System.err.println("getFileContents: file does not exist");
            } else if (!file.canRead()) {
                System.err.println("getFileContents: file is not readable");
            } else if (!file.isFile()) {
                System.err.println("getFileContents: file is not a file - directory perhaps?");
            }
        } catch (FileNotFoundException e) {
            System.err.println("getFileContents: FileNotFoundException: " + e.getLocalizedMessage());
        } catch (IOException e) {
            System.err.println("getFileContents: IOException: " + e.getLocalizedMessage() + ": " + path);
            e.printStackTrace();
        }
        System.out.println("getFileContents: " + path);
        return result;
    }

    public void setFileContents(Document document) {
        try {
            String path = document.getPath();
            String contents = document.getText();
            File file = new File(path);
            if (file.exists() && file.canWrite() && file.isFile()) {
                FileOutputStream fis = new FileOutputStream(file);
                fis.close();
                fis = new FileOutputStream(file);
                DataOutputStream dis = new DataOutputStream(fis);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(dis));
                bw.write(contents);
                bw.close();
                dis.close();
                fis.close();
            }
        } catch (FileNotFoundException e) {
            System.err.println("getFileContents: FileNotFoundException: " + e.getLocalizedMessage());
        } catch (IOException e) {
            System.err.println("getFileContents: IOException: " + e.getLocalizedMessage());
        }
    }

    public void mergeFileContents(Document document, String auth) {
        try {
            String file_path = document.getPath();
            File file = new File(file_path);
            if (file.exists() && file.canWrite() && file.isFile()) {
                try {
                    SubversionServer svnServer = new SubversionServer();
                    SessionServer session_server = new SessionServer();
                    String username = session_server.getUsername(auth);
                    ProjectServer project_server = new ProjectServer();
                    String project_name = project_server.getProjectName(document);
                    String svn_username = project_server.getSvnUsername(project_name, username);
                    String svn_password = project_server.getSvnPassword(project_name, username);
                    String svn_url = project_server.getSvnUrl(project_name, username);
                    String contents = document.getText();
                    svnServer.modifyFile(svn_url, svn_username, svn_password, file_path, contents);
                } catch (DatabaseException e) {
                    System.err.println("mergeFileContents: IOException: " + e.getLocalizedMessage());
                }
            }
        } catch (SVNException e) {
            System.err.println("mergeFileContents: SVNException: " + e.getLocalizedMessage());
        }
    }
}
