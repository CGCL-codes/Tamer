package com.example.struts.action;

import hibernateDatabase.DbOp;
import hibernateDatabase.DbOperations;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import com.example.struts.form.FileUpload3Form;

/**
 * MyEclipse Struts Creation date: 01-26-2009
 * 
 * XDoclet definition:
 * 
 * @struts.action path="/fileUpload3" name="fileUpload3Form"
 *                input="/jsp/fileUpload3.jsp" scope="request" validate="true"
 */
public class FileUpload3Action extends Action {

    /**
	 * Method execute
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, IOException {
        FileUpload3Form fileUpload3Form = (FileUpload3Form) form;
        FormFile myFile = fileUpload3Form.getTheFile();
        String contentType = myFile.getContentType();
        String fileName = myFile.getFileName();
        int initial = 0;
        int flag = 0;
        int error = 0;
        int second = fileName.indexOf('.', initial);
        String fileType = fileName.substring(second + 1, fileName.length());
        int fileSize = myFile.getFileSize();
        System.out.println("contentType: " + contentType);
        System.out.println("File Name: " + fileName);
        System.out.println("File Size: " + fileSize);
        System.out.println("FileType: " + fileType);
        if (fileSize != 0 && fileType.equalsIgnoreCase("txt") && contentType.equalsIgnoreCase("text/plain")) {
            int subscript = 0;
            int count = 0;
            int linesIndex = 0;
            int first = 0;
            int i;
            boolean notNineAttributes = false;
            String data[] = new String[9];
            Vector<String> vectorObj = new Vector<String>();
            String[] fileLines;
            byte[] fileData = myFile.getFileData();
            String geneStr = null;
            while (subscript < fileData.length) {
                if (fileData[subscript] == 13) {
                    count++;
                }
                subscript++;
            }
            if (count == 0 && fileData.length != 0) {
                fileLines = new String[count];
                fileLines = new String[1];
                fileLines[0] = new String(fileData);
            } else {
                System.out.println("last File data  " + fileData[subscript - 1] + " and subscript " + subscript);
                if (fileData[subscript - 1] != 10) {
                    count = count + 1;
                }
                fileLines = new String[count];
                System.out.println("Filedata length " + fileData.length + " Value of count " + count);
                for (i = 0; i < fileData.length; i++) {
                    if (fileData[i] == 13) {
                        fileLines[linesIndex] = new String(fileData, first, i - first);
                        first = i + 2;
                        linesIndex++;
                    }
                }
                System.out.println("Line Index " + linesIndex);
                System.out.println("i = " + i + " and first = " + first);
                System.out.println("Last data value" + fileData[fileData.length - 1]);
                if (fileData[fileData.length - 1] != 10) {
                    i = i - 1;
                    fileLines[linesIndex] = new String(fileData, first, i - first + 1);
                    linesIndex++;
                }
                System.out.println("Number of strings" + linesIndex);
            }
            for (i = 0; i < fileLines.length; i++) {
                String geneData = fileLines[i];
                int start = 0;
                int First = 0;
                int k = 0;
                String geneDataString = "";
                int counter = 0;
                if (geneData.length() != 0) {
                    while (k < geneData.lastIndexOf(";")) {
                        int Second = geneData.indexOf(';', First);
                        if (Second != -1) {
                            counter++;
                            First = Second + 1;
                        }
                        k++;
                    }
                    if (counter < 9 || counter > 9) {
                        System.out.println("Attributes are less than 9 " + counter);
                        notNineAttributes = true;
                        int end = geneData.indexOf(';', start);
                        geneStr = geneData.substring(start, end);
                        System.out.println("geneStr " + geneStr);
                    } else {
                        String tmp;
                        int geneId = 0, gFun = 0, gPSeq = 0, gDes = 0, gName = 0, gLoc = 0, gPs = 0, gLp = 0, gSs = 0;
                        for (int r = 0; r < 9; r++) {
                            int end = geneData.indexOf(';', start);
                            data[r] = geneData.substring(start, end);
                            tmp = data[r];
                            switch(r) {
                                case 0:
                                    if (tmp.length() > 50) {
                                        geneId = -1;
                                    }
                                    break;
                                case 1:
                                    if (tmp.length() > 65535) {
                                        gFun = -1;
                                    }
                                    break;
                                case 2:
                                    if (tmp.length() > 16777215) {
                                        gPSeq = -1;
                                    }
                                    break;
                                case 3:
                                    if (tmp.length() > 65535) {
                                        gDes = -1;
                                    }
                                    break;
                                case 4:
                                    if (tmp.length() > 255) {
                                        gName = -1;
                                    }
                                    break;
                                case 5:
                                    if (tmp.length() > 20) {
                                        gPs = -1;
                                    }
                                    break;
                                case 6:
                                    if (tmp.length() > 65535) {
                                        gLoc = -1;
                                    }
                                    break;
                                case 7:
                                    if (tmp.length() > 20) {
                                        gLp = -1;
                                    }
                                    break;
                                case 8:
                                    if (tmp.length() > 20) {
                                        gSs = -1;
                                    }
                                    break;
                            }
                            geneDataString = geneDataString + data[r] + ";";
                            start = end + 1;
                        }
                        if (geneId == -1 || gFun == -1 || gPSeq == -1 || gDes == -1 || gName == -1 || gLoc == -1 || gPs == -1 || gLp == -1 || gSs == -1) {
                            error = -1;
                            break;
                        }
                        vectorObj.add(geneDataString);
                    }
                }
            }
            if (error == -1) {
                flag = -5;
                request.setAttribute("flag", flag);
                return mapping.findForward("uploadFailure");
            } else {
                System.out.println("Size of vectorObj" + vectorObj.size());
                System.out.println("geneStr " + geneStr);
                System.out.println("notNineAttributes= " + notNineAttributes);
                if (notNineAttributes == true) {
                    System.out.println("Error message");
                    notNineAttributes = false;
                    flag = -4;
                    request.setAttribute("flag", flag);
                    return mapping.findForward("uploadFailure");
                } else {
                    String[] newfileLines = new String[vectorObj.size()];
                    for (int u = 0; u < vectorObj.size(); u++) {
                        newfileLines[u] = vectorObj.elementAt(u);
                    }
                    HttpSession session2 = request.getSession(true);
                    String strainId = (String) session2.getAttribute("strainId");
                    System.out.println("StrainId :" + strainId);
                    DbOperations DbOperationsObj = DbOp.getDbOperations();
                    flag = DbOperationsObj.deleteMultipleGenes(strainId, fileLines);
                    request.getSession().removeAttribute("strainId");
                    if (flag == 0) {
                        System.out.println("In fileUpload3 " + " " + flag);
                        return mapping.findForward("deleteMultipleGenesSuccess");
                    } else {
                        System.out.println("In fileUpload3 else" + " " + flag);
                        String uploadError = new Integer(flag).toString();
                        request.setAttribute("statusflag", uploadError);
                        return mapping.findForward("deleteMultipleGenesFailure");
                    }
                }
            }
        } else {
            if (!contentType.equalsIgnoreCase("text/plain")) {
                System.out.println("InCorrect Contenttype");
                flag = -2;
            }
            if (!fileType.equalsIgnoreCase("txt")) {
                System.out.println("InCorrect Filetype");
                flag = -2;
            }
            if (fileSize == 0) {
                System.out.println(" File is empty, FileSize is zero");
                flag = -3;
            }
            request.setAttribute("flag", flag);
            return mapping.findForward("uploadFailure");
        }
    }
}
