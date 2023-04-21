package org.verus.ngl.sl.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import org.verus.ngl.utilities.NGLXMLUtility;

/**
 *
 * @author root
 */
public class NGLSLUtility implements NGLLibConstants, NGLDBConstants {

    private static NGLSLUtility sLUtility = null;

    private String userId = "1";

    private String libraryId = "1";

    private String databaseId = "Test";

    private String iPAddress = "localhost";

    private String port = "8080";

    public static NGLSLUtility getInstance() {
        if (sLUtility == null) {
            sLUtility = new NGLSLUtility();
        }
        return sLUtility;
    }

    private NGLSLUtility() {
    }

    public String getSystemVariable(String databaseId, String varConstant) {
        NewGenLibRoot newGenLibRoot = (NewGenLibRoot) org.verus.ngl.sl.utilities.NGLBeanFactory.getInstance().getBean("newGenLibRoot");
        String newgenlibRoot = newGenLibRoot.getRoot();
        String xmlResp = "";
        try {
            File file1 = new File(newgenlibRoot + "/" + databaseId + "/DB_ENV_VAR.xml");
            if (file1.exists() && file1.isFile()) {
                BufferedReader bReader = new BufferedReader(new FileReader(file1));
                while (bReader.ready()) {
                    xmlResp += bReader.readLine();
                }
                System.out.println("File is found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        org.jdom.Element root = NGLXMLUtility.getInstance().getRootElementFromXML(xmlResp);
        String value = root.getChildText(varConstant);
        if (value == null) {
            return "";
        }
        return value.replace("\\", "/");
    }

    public String getSystemVariable(String libraryId, String databaseId, String varConstant) {
        NewGenLibRoot newGenLibRoot = (NewGenLibRoot) org.verus.ngl.sl.utilities.NGLBeanFactory.getInstance().getBean("newGenLibRoot");
        String newgenlibRoot = newGenLibRoot.getRoot();
        String xmlResp = "";
        System.out.println("========================: " + varConstant);
        System.out.println("DBID===========: " + databaseId);
        System.out.println("LIBID========================: " + libraryId);
        System.out.println("Root is ===============: " + newgenlibRoot);
        try {
            File file1 = new File(newgenlibRoot + "/" + databaseId + "/" + "LIB_" + libraryId + "/" + "SystemFiles" + "/" + "ENV_VAR.xml");
            System.out.println("File=: " + file1);
            if (file1.exists() && file1.isFile()) {
                BufferedReader bReader = new BufferedReader(new FileReader(file1));
                while (bReader.ready()) {
                    xmlResp += bReader.readLine();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        org.jdom.Element root = NGLXMLUtility.getInstance().getRootElementFromXML(xmlResp);
        String value = root.getChildText(varConstant);
        if (value == null) {
            return "";
        }
        return value.replace("\\", "/");
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(String libraryId) {
        this.libraryId = libraryId;
    }

    public String getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(String databaseId) {
        this.databaseId = databaseId;
    }

    public String getIPAddress() {
        return iPAddress;
    }

    public void setIPAddress(String iPAddress) {
        this.iPAddress = iPAddress;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
