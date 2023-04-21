package octopus.presentation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.w3c.dom.NodeList;
import hambo.util.StringUtil;
import hambo.app.util.DOMUtil;
import hambo.app.util.MultipartRequestFactory;
import hambo.app.util.MultipartRequest;
import hambo.app.util.UploadedFile;
import com.lutris.appserver.server.httpPresentation.HttpPresentationException;
import octopus.OctopusApplication;
import octopus.tools.Util.Unzip;
import octopus.tools.Util.ParserToolXML;
import octopus.requests.OctopusRequestFactory;
import octopus.tools.Messages.OctopusErrorMessages;
import octopus.requests.OctopusRequest;
import octopus.tools.Objects.ObjectTranslation;

/**
 * Page Used to Import XML files from the Translator to the Database (XML sinlge file or ZIP file containing Folder with XML files)<BR>
 * <B>XML FILE MUST BE UTF-8 ENCODED</B>
 */
public class importXML extends octoPage {

    /**
     * Default constructor. Does nothing except calling the constructor
     * of the superclass with the page id as an argument. The page id 
     * is set in the page_id variable that is inherited from PortalPage.
     */
    public importXML() {
        super("importXML");
    }

    /**
    * The over-ridden method that is called automatically 
    * by {@link PortalPage}.run().
    */
    public void processPage() throws HttpPresentationException, IOException {
        DOMUtil.setFirstNodeText(getElement(skeleton, "pagetitle"), "Import XML Files");
        initialisationOctopus("importXML.po", true);
        DOMUtil.setAttribute(getElement(skeleton, "SkeletonForm"), "enctype", "multipart/form-data");
        DOMUtil.setAttribute(getElement(skeleton, "SkeletonForm"), "method", "POST");
        String result_text = "";
        if (comms != null && comms.request != null && comms.request.getHttpServletRequest() != null && comms.request.getContentType() != null && comms.request.getContentType().startsWith("multipart/form-data")) {
            MultipartRequest mulReq = MultipartRequestFactory.createMultipartRequest(comms.request.getHttpServletRequest());
            if (mulReq != null && mulReq.getParameter("upload") != null) {
                UploadedFile theUploadedFile = mulReq.getUploadedFile("uploadFile");
                if (theUploadedFile != null) {
                    if (theUploadedFile.getSize() != 0) {
                        String fileName = getFileName(theUploadedFile.getName());
                        if (fileName != null) {
                            fileName = fileName.toLowerCase();
                        }
                        Calendar calendar = new GregorianCalendar();
                        String folderDate = calendar.get(Calendar.YEAR) + "_" + (calendar.get(Calendar.MONTH) + 1) + "_" + calendar.get(Calendar.DAY_OF_MONTH) + "_" + calendar.get(Calendar.HOUR_OF_DAY) + "_" + calendar.get(Calendar.MINUTE) + "_" + calendar.get(Calendar.SECOND);
                        if (fileName != null && (fileName.toUpperCase().endsWith(".XML"))) {
                            InputStream fis = theUploadedFile.getInputStream();
                            String file_xml_path = OctopusApplication.PATH_IMPORT_FOLDER + folderDate + "_" + fileName;
                            File file_import_folder = new File(OctopusApplication.PATH_IMPORT_FOLDER);
                            if (!file_import_folder.exists()) file_import_folder.mkdirs();
                            File file_xml = new File(file_xml_path);
                            FileOutputStream fos = new FileOutputStream(file_xml);
                            byte[] buf = new byte[1024];
                            int i = 0;
                            while ((i = fis.read(buf)) != -1) {
                                fos.write(buf, 0, i);
                            }
                            fis.close();
                            fos.close();
                            result_text = importFile(file_xml_path, theUploadedFile.getName());
                            file_xml.delete();
                        } else if (fileName != null && (fileName.toUpperCase().endsWith(".ZIP"))) {
                            InputStream fis = theUploadedFile.getInputStream();
                            String file_zip_path = OctopusApplication.PATH_IMPORT_FOLDER + folderDate + "_" + fileName;
                            File file_zip = new File(file_zip_path);
                            FileOutputStream fos = new FileOutputStream(file_zip);
                            byte[] buf = new byte[1024];
                            int i = 0;
                            while ((i = fis.read(buf)) != -1) {
                                fos.write(buf, 0, i);
                            }
                            fis.close();
                            fos.close();
                            File f = new File(OctopusApplication.PATH_IMPORT_FOLDER + "/" + folderDate);
                            if (!f.exists()) f.mkdirs();
                            Unzip.unzipIt(file_zip_path, folderDate);
                            file_zip.delete();
                            result_text = listage(OctopusApplication.PATH_IMPORT_FOLDER + "/" + folderDate, null);
                        }
                    }
                }
            }
        }
        if (result_text == null || result_text.trim().equals("")) {
            removeElement("result_text");
        } else {
            DOMUtil.setFirstNodeText(getElement("result_text"), result_text);
        }
    }

    /**
     * Method Used to Parse a Folder and His Subfolders
     */
    private String listage(String file_name, String client_file_name) {
        StringBuffer msg = new StringBuffer("");
        if (file_name == null || file_name.trim().equals("")) {
            msg.append(OctopusErrorMessages.PATH_CANT_BE_EMPTY);
            return msg.toString();
        }
        File f = new File(file_name);
        if (f.isDirectory()) {
            File file[] = f.listFiles();
            for (int i = 0; i < file.length; i++) {
                if (file[i].isDirectory()) {
                    msg.append(listage(file[i].getAbsolutePath(), client_file_name));
                } else {
                    if (file[i].toString().toUpperCase().endsWith(".XML")) {
                        if (client_file_name == null && file[i].toString().startsWith(OctopusApplication.PATH_IMPORT_FOLDER)) {
                            client_file_name = file[i].toString().substring((OctopusApplication.PATH_IMPORT_FOLDER).length(), file[i].toString().length());
                        }
                        msg.append(importFile(file[i].toString(), client_file_name));
                    }
                }
            }
        } else if (f.exists()) {
            importFile(file_name, client_file_name);
        }
        return msg.toString();
    }

    private String importFile(String file_name, String client_file_name) {
        StringBuffer msg = new StringBuffer("");
        String FinalMsg = "";
        if (file_name != null && !file_name.trim().equals("")) {
            ParserToolXML parser = new ParserToolXML(file_name);
            if (parser.doc != null) {
                NodeList notenode = parser.doc.getElementsByTagName("note");
                String targetLanguage = parser.SearchElementValue(notenode.item(0));
                targetLanguage = targetLanguage.substring(targetLanguage.indexOf(":") + 1, targetLanguage.length());
                targetLanguage = targetLanguage.trim();
                targetLanguage = StringUtil.replace(targetLanguage, "\n", "");
                targetLanguage = StringUtil.replace(targetLanguage, "\r", "");
                targetLanguage = StringUtil.replace(targetLanguage, "\t", "");
                NodeList bodynode = parser.doc.getElementsByTagName("body");
                for (int i = 0; i < bodynode.getLength(); i++) {
                    NodeList listNodeTu = bodynode.item(i).getChildNodes();
                    for (int j = 0; j < listNodeTu.getLength(); j++) {
                        String tagId = parser.SearchAttributValue(listNodeTu.item(j), "tuid");
                        if (tagId != null && !tagId.trim().equals("")) {
                            tagId = tagId.trim();
                            tagId = StringUtil.replace(tagId, "\n", "");
                            tagId = StringUtil.replace(tagId, "\r", "");
                            tagId = StringUtil.replace(tagId, "\t", "");
                            ObjectTranslation otReference = OctopusRequest.getTranslation(tagId, OctopusApplication.MASTER_LANGUAGE);
                            if (otReference != null) {
                                String tagDescription = otReference.getInfo();
                                String MainLanguage_version_when_we_send_to_the_translators = parser.SearchElementValue(listNodeTu.item(j), "prop", "type", OctopusApplication.MASTER_LANGUAGE + "_Version");
                                MainLanguage_version_when_we_send_to_the_translators = MainLanguage_version_when_we_send_to_the_translators.trim();
                                MainLanguage_version_when_we_send_to_the_translators = StringUtil.replace(MainLanguage_version_when_we_send_to_the_translators, "\n", "");
                                MainLanguage_version_when_we_send_to_the_translators = StringUtil.replace(MainLanguage_version_when_we_send_to_the_translators, "\r", "");
                                MainLanguage_version_when_we_send_to_the_translators = StringUtil.replace(MainLanguage_version_when_we_send_to_the_translators, "\t", "");
                                int oldVersion = (new Integer(MainLanguage_version_when_we_send_to_the_translators.trim())).intValue();
                                String MainLanguage_version_now = otReference.getVersion();
                                if (MainLanguage_version_now == null) MainLanguage_version_now = "-1";
                                int currentVersion = (new Integer(MainLanguage_version_now)).intValue();
                                String oldTranslation = otReference.getTranslation();
                                String tagAuthor = otReference.getLUN();
                                String tagDate = otReference.getLUD();
                                String tagApplication = otReference.getApplicationId();
                                String tooltip = "<U><B>Desc:</B></U><BR>" + StringUtil.replace(tagDescription, "'", "&#x27;");
                                tooltip += "<BR>-----------<BR><B><U>" + OctopusApplication.MASTER_LANGUAGE + ":</U></B><BR>" + StringUtil.replace(oldTranslation, "'", "&#x27;") + "(" + MainLanguage_version_now + ")";
                                tooltip += "<BR>-----------<BR> " + tagAuthor + " " + tagDate;
                                String js = "show_window('manage','" + tagId + "','" + tagApplication + "','" + targetLanguage + "','false');";
                                String textToPrint = "&nbsp;<A class='text' href=\"#\" onmouseover=\"return overlib('" + tooltip + "',CAPTION,'" + tagId + "',VAUTO,HAUTO);\" onmouseout=\"return nd();\">" + tagId + "</A>";
                                if (oldVersion < currentVersion) {
                                    FinalMsg += OctopusErrorMessages.WE_CHANGE + " " + textToPrint + "<BR>";
                                } else {
                                    String translation = parser.SearchElementValue(listNodeTu.item(j), "seg");
                                    if (translation != null) {
                                        translation = translation.trim();
                                        translation = StringUtil.replace(translation, "\n", "");
                                        translation = StringUtil.replace(translation, "\r", "");
                                        translation = StringUtil.replace(translation, "\t", "");
                                        translation = StringUtil.replace(translation, "&#x26;", "&");
                                    }
                                    if (oldTranslation != null && translation != null) {
                                        if (!oldTranslation.equals(translation)) {
                                            String so = OctopusRequestFactory.updateTranslation(tagId, targetLanguage, translation, "Import tool");
                                            if (so.equals(OctopusErrorMessages.ACTION_DONE)) {
                                                FinalMsg = FinalMsg + OctopusErrorMessages.UPDATE_TAG + ": " + tagId + "<BR>";
                                            } else {
                                                FinalMsg = FinalMsg + OctopusErrorMessages.PROBLEM_UPDATING + ": " + textToPrint + " " + so + "<BR>";
                                            }
                                        } else {
                                            FinalMsg += OctopusErrorMessages.TRANSLATION_OF_THE_TAG + " " + textToPrint + OctopusErrorMessages.SEEMS_TO_BE_THE_SAME_IN + "  " + targetLanguage + ": <A HREF=\"javascript:" + js + "\"><FONT COLOR=\"RED\">" + OctopusErrorMessages.CONFIRM + "!</FONT></A><BR>";
                                        }
                                    } else if (translation != null) {
                                        String so = OctopusRequestFactory.updateTranslation(tagId, targetLanguage, translation, "Import tool");
                                        if (so.equals(OctopusErrorMessages.ACTION_DONE)) {
                                            FinalMsg = FinalMsg + OctopusErrorMessages.UPDATE_TAG + ": " + tagId + "<BR>";
                                        } else {
                                            FinalMsg = FinalMsg + OctopusErrorMessages.PROBLEM_UPDATING + ": " + tagId + " " + so + "<BR>";
                                        }
                                    } else {
                                        FinalMsg += OctopusErrorMessages.TRANSLATOR_DIDNT_WORK_ON_TAG + ": " + textToPrint + "<BR>";
                                    }
                                }
                            } else {
                                FinalMsg = FinalMsg + OctopusErrorMessages.PROBLEM_UPDATING + ": " + tagId + OctopusErrorMessages.DONT_FIND_THE_TAG_ID + " <BR>";
                            }
                        }
                    }
                }
                msg.append("<p><b>" + client_file_name + "</b> - " + OctopusErrorMessages.IMPORTED + "! <BR>" + FinalMsg + "</p>");
            } else {
                msg.append("<p><b>" + client_file_name + "</b> - " + OctopusErrorMessages.NOT_CORRECT_XML + " </p>");
            }
        } else {
            msg.append("<p><b>" + file_name + "</b> - " + OctopusErrorMessages.FILE_CANT_BE_NULL + "!</p>");
        }
        return msg.toString();
    }

    /**
     * Takes out the name of the file from a path.
     */
    private String getFileName(String path) {
        String fileName = path;
        int index = path.lastIndexOf("\\");
        if (index == -1) {
            index = path.lastIndexOf("/");
        }
        if (index != -1) {
            fileName = fileName.substring(index + 1);
        }
        return fileName;
    }
}
