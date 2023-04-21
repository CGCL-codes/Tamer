package org.mitre.mrald.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.mitre.mrald.control.MsgObject;
import org.xml.sax.InputSource;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    August 30, 2001
 */
public class FormUtils {

    MsgObject msg;

    /**
     *  Constructor for the FormUtils object
     *
     *@since
     */
    public FormUtils() {
    }

    /**
     *  Gets the Xml attribute of the FormUtils class
     *
     *@param  text  Description of Parameter
     *@return       The Xml value
     *@since
     */
    public static boolean isXml(String text) {
        try {
            DocumentBuilder doc = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc.parse(new InputSource(new StringReader(text)));
        } catch (org.xml.sax.SAXException e) {
            return false;
        } catch (javax.xml.parsers.ParserConfigurationException e) {
            InterfaceException nu = new InterfaceException(e);
            throw nu;
        } catch (java.io.IOException e) {
            InterfaceException nu = new InterfaceException(e);
            throw nu;
        }
        return true;
    }

    /**
     *  Description of the Method
     *
     *@param  temp             Description of the Parameter
     *@param  output           Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public static void printFile(Writer output, File temp) throws IOException {
        FileInputStream fis = new FileInputStream(temp.toString());
        BufferedReader r = new BufferedReader(new InputStreamReader(fis));
        String currLine = r.readLine();
        while (currLine != null) {
            output.write(currLine);
            currLine = r.readLine();
        }
    }

    /**
     *  Description of the Method
     *
     *@param  temp             Description of the Parameter
     *@return                  The redirect value
     *@exception  IOException  Description of the Exception
     */
    public static String getRedirect(String temp) throws IOException {
        String base = Config.getProperty("BasePath");
        String redirectFile = temp.substring(base.length());
        redirectFile = redirectFile.replace('\\', '/');
        return (Config.getProperty("BaseUrl") + "/" + redirectFile);
    }

    /**
     *  Gets the simpleName attribute of the PersonalFormsListTag object
     *
     *@param  formId  Description of the Parameter
     *@param  userid  Description of the Parameter
     *@return         The simpleName value
     */
    public static String getSimpleJspName(String formId, String userid) {
        return userid + "_" + formId + "-simple.jsp";
    }

    /**
     *  Creates the htmlFileName attribute of the FormUtils object
     *
     *@param  form    The new form ID number
     *@param  userid  The users ID number within the people table
     *@return         The htmlFileName value
     */
    public static String getJspFileName(String form, String userid) {
        return userid + "_" + form + ".jsp";
    }

    /**
     *  Creates the xmlFileName attribute of the FormUtils object
     *
     *@param  formid  The new form ID number
     *@param  userid  The users ID number within the people table
     *@return         The xmlFileName value
     */
    public static String getXmlFileName(String formid, String userid) {
        return userid + "_" + formid + ".xml";
    }

    public static void deleteJspsForForm(String[] formId, String userid) {
        for (int i = 0; i < formId.length; i++) {
            deleteJspsForForm(formId[i], userid);
        }
    }

    /**
     *  Description of the Method
     *
     *@param  formId  Description of the Parameter
     *@param  userid  Description of the Parameter
     */
    public static void deleteJspsForForm(String formId, String userid) {
        String dir_struct = Config.getProperty("customForms");
        File jsp_file = new File(dir_struct + getJspFileName(formId, userid));
        File simple_jsp_file = new File(dir_struct + getSimpleJspName(formId, userid));
        jsp_file.delete();
        simple_jsp_file.delete();
    }

    /**
     *  Passthrough method for copyForm
     *
     *@param  newformid                  Description of Parameter
     *@param  currentUserId              Description of Parameter
     *@param  userId                     Description of the Parameter
     *@exception  IOException            Description of the Exception
     *@exception  FileNotFoundException  Description of the Exception
     *@since
     */
    public void copyForm(String currentUserId, String newformid, String[] userId) throws IOException, FileNotFoundException {
        for (int i = 0; i < userId.length; i++) {
            copyForm(currentUserId, newformid, userId[i]);
        }
    }

    /**
     *  Given a user id and formId of and existing form and a new user id,
     *  copies the existing files (xsml and html) over to a new user.
     *
     *@param  newformid                  Description of Parameter
     *@param  currentUserId              Description of Parameter
     *@param  userId                     Description of the Parameter
     *@exception  IOException            Description of the Exception
     *@exception  FileNotFoundException  Description of the Exception
     *@since
     */
    public void copyForm(String currentUserId, String newformid, String userId) throws IOException, FileNotFoundException {
        String destDir = Config.getProperty("customForms");
        File xmlSourceFile = new File(destDir, getXmlFileName(newformid, currentUserId));
        if (xmlSourceFile.exists()) {
            copyFile(xmlSourceFile, new File(destDir, getXmlFileName(newformid, userId)));
        }
        File jspSourceFile = new File(destDir, getJspFileName(newformid, currentUserId));
        if (jspSourceFile.exists()) {
            copyFile(jspSourceFile, new File(destDir, getJspFileName(newformid, userId)));
        }
        File jspSimpleSourceFile = new File(destDir, getSimpleJspName(newformid, currentUserId));
        if (jspSimpleSourceFile.exists()) {
            copyFile(jspSimpleSourceFile, new File(destDir, getSimpleJspName(newformid, userId)));
        }
    }

    public void deleteForm(String formId[], String userId) throws IOException, FileNotFoundException {
        for (int i = 0; i < formId.length; i++) {
            deleteForm(formId[i], userId);
        }
    }

    public void publishForm(String formId[], String userId) throws IOException, FileNotFoundException {
        for (int i = 0; i < formId.length; i++) {
            publishForm(formId[i], userId);
        }
    }

    private void publishForm(String formItem, String userId) throws IOException, FileNotFoundException {
        String destDir = Config.getProperty("customForms");
        userId = "public";
        String newformid = formItem.substring(formItem.indexOf("_") + 1, formItem.length());
        String sourceUserId = formItem.substring(0, formItem.indexOf("_"));
        File xmlSourceFile = new File(destDir, getXmlFileName(newformid, sourceUserId));
        if (xmlSourceFile.exists()) {
            copyFile(xmlSourceFile, new File(destDir, getXmlFileName(newformid, userId)));
        } else {
            File jspSourceFile = new File(destDir, getJspFileName(newformid, sourceUserId));
            if (jspSourceFile.exists()) {
                copyFile(jspSourceFile, new File(destDir, getJspFileName(newformid, userId)));
            }
            File jspSimpleSourceFile = new File(destDir, getSimpleJspName(newformid, sourceUserId));
            if (jspSimpleSourceFile.exists()) {
                copyFile(jspSimpleSourceFile, new File(destDir, getSimpleJspName(newformid, userId)));
            }
        }
    }

    /**
     *  Moves the xml and/or html files from the customForms directory to a
     *  deleted directory under that and deletes the files from the customForms
     *  directory
     *
     *@param  userid                     Description of Parameter
     *@param  formId                     Description of the Parameter
     *@exception  IOException            Description of the Exception
     *@exception  FileNotFoundException  Description of the Exception
     *@since
     */
    public void deleteForm(String formId, String userid) throws IOException, FileNotFoundException {
        File deletedDir = new File(Config.getProperty("customForms"), "deleted");
        if (!deletedDir.exists()) {
            deletedDir.mkdirs();
        }
        File xmlSourceFile = new File(Config.getProperty("customForms"), getXmlFileName(formId, userid));
        if (xmlSourceFile.exists()) {
            copyFile(xmlSourceFile, new File(deletedDir, xmlSourceFile.getName()));
            xmlSourceFile.delete();
        }
        File jspSourceFile = new File(Config.getProperty("customForms"), getJspFileName(formId, userid));
        if (jspSourceFile.exists()) {
            copyFile(jspSourceFile, new File(deletedDir, jspSourceFile.getName()));
            jspSourceFile.delete();
        }
        jspSourceFile = new File(Config.getProperty("customForms"), getSimpleJspName(formId, userid));
        if (jspSourceFile.exists()) {
            copyFile(jspSourceFile, new File(deletedDir, jspSourceFile.getName()));
            jspSourceFile.delete();
        }
    }

    /**
     *  Returns the XML of the form if avaiable. If only an HTML file is
     *  avaiablem that is returned instead.
     *
     *@param  formid           Description of the Parameter
     *@param  userid           Description of the Parameter
     *@param  response         Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public void downloadForm(HttpServletResponse response, String formid, String userid) throws IOException {
        String dir_struct = Config.getProperty("customForms");
        PrintWriter out = response.getWriter();
        File xml_file = new File(dir_struct + getXmlFileName(formid, userid));
        File jsp_file = new File(dir_struct + getJspFileName(formid, userid));
        if (xml_file.exists()) {
            response.setContentType("text/xml");
            response.setHeader("Content-Disposition", "attachment; filename=" + xml_file.getName() + ";");
            printFile(out, xml_file);
        } else if (jsp_file.exists()) {
            response.setContentType("text/html");
            response.setHeader("Content-Disposition", "attachment; filename=" + jsp_file.getName() + ";");
            printFile(out, jsp_file);
        } else {
            throw new RuntimeException("No form exists for formID=" + formid + " and userID=" + userid);
        }
    }

    /**
     *  Stores the form (xml or html) in the customForms directory
     *
     *@param  form             A String containing the XML that represents the
     *      form.
     *@param  userid           The id of the current user
     *@param  newformid        Description of the Parameter
     *@exception  IOException  General purpose IO Exception
     *@since
     */
    public void storeForm(String newformid, String form, String userid) throws IOException {
        File new_file;
        if (isXml(form)) {
            new_file = new File(Config.getProperty("customForms"), getXmlFileName(newformid, userid));
        } else {
            new_file = new File(Config.getProperty("customForms"), getJspFileName(newformid, userid));
        }
        if (!new_file.exists() && !new_file.createNewFile()) {
            IOException e = new IOException("Could not create new file.  The form content was stored in the log file.");
            e.fillInStackTrace();
            throw e;
        }
        PrintWriter ret = new PrintWriter(new FileWriter(new_file));
        ret.println(form);
        ret.close();
    }

    /**
     *  Description of the Method
     *
     *@param  sourceFile                 Description of the Parameter
     *@param  destFile                   Description of the Parameter
     *@exception  FileNotFoundException  Description of the Exception
     *@exception  IOException            Description of the Exception
     */
    public void copyFile(File sourceFile, File destFile) throws FileNotFoundException, IOException {
        byte[] buffer = new byte[4096];
        FileInputStream in = new FileInputStream(sourceFile);
        FileOutputStream out = new FileOutputStream(destFile);
        int len;
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        out.flush();
        out.close();
        in.close();
        in = null;
        out = null;
    }

    /**
     *  Description of the Method
     *
     *@param  xmlfile  Description of the Parameter
     */
    public static void makeForm(File xmlfile) {
        String strippedXmlFileName = xmlfile.toString().substring(0, xmlfile.toString().indexOf(".xml"));
        String jspFileName = strippedXmlFileName + ".jsp";
        String simpleFileName = strippedXmlFileName + "-simple.jsp";
        String tinyFileName = strippedXmlFileName + "-tiny.jsp";
        File xslFile = new File(Config.getProperty("formStylesheet"));
        File simpleXslFile = new File(Config.getProperty("simpleStylesheet"));
        try {
            StringBuffer jspContents = XSLTranslator.xslTransform(xmlfile, xslFile);
            PrintWriter ret = new PrintWriter(new FileWriter(jspFileName));
            ret.print(jspContents);
            ret.flush();
            ret.close();
            jspContents = XSLTranslator.xslTransform(xmlfile, simpleXslFile);
            if (jspContents.indexOf("html") == -1) {
                return;
            }
            ret = new PrintWriter(new FileWriter(simpleFileName));
            ret.print(jspContents);
            ret.flush();
            ret.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *  Description of the Method
     *
     *@param  xmlfile  Description of the Parameter
     */
    public static void makeTinyForm(File xmlfile) {
        String strippedXmlFileName = xmlfile.toString().substring(0, xmlfile.toString().indexOf(".xml"));
        String tinyFileName = strippedXmlFileName + "-tiny.jsp";
        File tinyXslFile = new File(Config.getProperty("tinyStylesheet"));
        try {
            StringBuffer jspContents = XSLTranslator.xslTransform(xmlfile, tinyXslFile);
            if (jspContents.indexOf("html") == -1) {
                return;
            }
            PrintWriter ret = new PrintWriter(new FileWriter(tinyFileName));
            ret.print(jspContents);
            ret.flush();
            ret.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
