package com.nyandu.weboffice.edit.action;

import com.nyandu.weboffice.common.action.PrivateAction;
import com.nyandu.weboffice.common.action.PublicForm;
import com.nyandu.weboffice.common.action.TimeStampUpdaterAction;
import com.nyandu.weboffice.common.business.BusinessSession;
import com.nyandu.weboffice.common.database.DAOException;
import com.nyandu.weboffice.common.database.IFileUserActionDAO;
import com.nyandu.weboffice.common.util.Consts;
import com.nyandu.weboffice.file.business.File;
import com.nyandu.weboffice.file.business.Folder;
import com.nyandu.weboffice.file.database.IFileDAO;
import com.nyandu.weboffice.file.database.IFolderDAO;
import com.nyandu.weboffice.file.util.DisplayMessage;
import com.nyandu.weboffice.file.util.Security;
import com.nyandu.weboffice.common.business.Sharing;
import com.nyandu.weboffice.common.business.TextFile;
import com.nyandu.weboffice.common.action.TimeStampUpdaterAction;
import com.nyandu.weboffice.site.business.User;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.Word2Html;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;

/**
 * 
 *  The contents of this file are subject to the Nandu Public License
 * Version 1.1 ("License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.nyandu.com
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and limitations
 * under the License.
 * 
 * The Initial Developer of the Original Code is User.
 * Portions created by User are Copyleft (C) www.nyandu.com. 
 * All Rights Reserved.
 * 
 * Contributor(s): ______________________________________.
 * User: alvaro
 * Date: 16/12/2004
 * Time: 04:20:29 PM
 */
public class OpenFileAction extends TimeStampUpdaterAction {

    public ActionForward execute(ActionMapping map, ActionForm form, HttpServletRequest req, HttpServletResponse res, BusinessSession bSession, Long timeStamp) throws Exception {
        String name = "";
        String msg = "";
        int code = Consts.UNEXPECTED_ERROR;
        if (form instanceof PublicForm) {
            PublicForm pubForm = (PublicForm) form;
            User user = bSession.getUser();
            IFileDAO fileDAO = bSession.getIFileDAO();
            IFolderDAO folderDAO = bSession.getIFolderDAO();
            try {
                int fileId = Integer.parseInt((String) pubForm.getParam(0));
                File file = fileDAO.selectFile(fileId);
                if (file != null) {
                    if (this.hasPermissions(user, file, folderDAO, fileDAO)) {
                        TextFile weFile = new TextFile(file);
                        weFile.setTimeStamp(timeStamp.longValue());
                        int index_point = file.getName().indexOf('.');
                        if (index_point != -1) {
                            if (file.getName().substring(index_point).equals(".doc")) {
                                OutputStream out = new ByteArrayOutputStream();
                                InputStream in = file.getInputStream();
                                in.reset();
                                HWPFDocument doc = new HWPFDocument(in);
                                Word2Html word = new Word2Html(doc, out);
                                weFile.setData(out.toString());
                            }
                        }
                        req.setAttribute("webEditFile", weFile);
                        name = "file_opened";
                        IFileUserActionDAO ifileUserActionDAO = bSession.getFileUserActionDAO();
                        ifileUserActionDAO.insertOrUpdateFileUserAction(file.getId(), user.getId(), Sharing.EDIT_FILE, timeStamp.longValue(), file.getName());
                        code = Consts.FILE_OPENED;
                    } else {
                        name = "user_has_no_permissions";
                        code = Consts.USER_HAS_NO_PERMISSIONS;
                        msg = "error, user " + user.getUsername() + " does not have read permission on the file " + file.getName();
                        System.out.println("El usuario NO TIENE " + name);
                    }
                } else {
                    name = "file_does_not_exist";
                    msg = "error, file with id " + fileId + " does not exist";
                }
                fileDAO.commit();
            } catch (DAOException daoe1) {
                daoe1.printStackTrace();
                try {
                    fileDAO.rollback();
                } catch (DAOException daoe2) {
                    daoe2.printStackTrace();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
                try {
                    fileDAO.rollback();
                } catch (DAOException daoe2) {
                    daoe2.printStackTrace();
                }
            }
        }
        req.setAttribute(DisplayMessage.KEY, new DisplayMessage(msg));
        req.setAttribute(Consts.RS_RESPONSE_CODE, new Integer(code));
        return map.findForward(name);
    }

    public int getActionCode() {
        return Sharing.EDIT_FILE;
    }

    private boolean hasPermissions(User user, File file, IFolderDAO folderDAO, IFileDAO fileDAO) throws DAOException {
        int userPer = fileDAO.getFileUserPermissions(file.getId(), user.getId());
        int groupsPer[] = fileDAO.getGroupsPermissions(file.getId(), user.getId());
        if (userPer == Consts.NO_PERMISSION_FOUND) {
            try {
                Folder folder = folderDAO.selectFolder(file.getParentId());
                while (userPer == Consts.NO_PERMISSION_FOUND) {
                    userPer = folderDAO.getFolderUserPermissions(folder.getId(), user.getId());
                    folder = folderDAO.selectFolder(folder.getParentId());
                }
            } catch (Exception ee) {
                ee.printStackTrace();
                throw new DAOException(ee);
            }
        }
        int flags = userPer;
        for (int i = 0; i < groupsPer.length; i++) {
            flags = flags | groupsPer[i];
        }
        return Security.canDoFolderAction(flags, Security.FILE_READ_PERMISSION);
    }
}
