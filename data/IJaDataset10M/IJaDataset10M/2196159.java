package com.dotmarketing.portlets.wysiwyg.action;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.servlet.jsp.PageContext;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import com.dotmarketing.beans.Identifier;
import com.dotmarketing.cache.IdentifierCache;
import com.dotmarketing.cache.LiveCache;
import com.dotmarketing.cache.WorkingCache;
import com.dotmarketing.factories.InodeFactory;
import com.dotmarketing.portal.struts.DotPortletAction;
import com.dotmarketing.portlets.files.factories.FileFactory;
import com.dotmarketing.portlets.files.model.File;
import com.dotmarketing.portlets.folders.model.Folder;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.UtilMethods;
import com.dotmarketing.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.util.Constants;
import com.liferay.util.servlet.SessionMessages;

/**
 * @author Maria
 */
public class EditTextAction extends DotPortletAction {

    public void processAction(ActionMapping mapping, ActionForm form, PortletConfig config, ActionRequest req, ActionResponse res) throws Exception {
        String cmd = req.getParameter(Constants.CMD);
        Logger.debug(this, "Inside EditTextAction cmd=" + cmd);
        try {
            Logger.debug(this, "Going to run SaveTextAction");
            _retrieveTextAction(req, res, config, form);
        } catch (Exception ae) {
            Logger.error(this, ae.toString(), ae);
            req.setAttribute(PageContext.EXCEPTION, ae);
            setForward(req, Constants.COMMON_ERROR);
        }
        if ((cmd != null) && cmd.equals(Constants.SAVE)) {
            try {
                Logger.debug(this, "Going to run SaveTextAction");
                _saveTextAction(req, res, config, form);
                setForward(req, "portlet.ext.wysiwyg.window_close");
            } catch (Exception ae) {
                Logger.error(this, ae.toString(), ae);
                req.setAttribute(PageContext.EXCEPTION, ae);
                setForward(req, Constants.COMMON_ERROR);
            }
        } else {
            setForward(req, "portlet.ext.wysiwyg.edit_text");
        }
    }

    private void _saveTextAction(ActionRequest req, ActionResponse res, PortletConfig config, ActionForm form) throws Exception {
        FileOutputStream os = null;
        try {
            File file = (File) InodeFactory.getInode(req.getParameter("inode"), File.class);
            Folder parentFolder = (Folder) InodeFactory.getParentOfClass(file, Folder.class);
            Identifier identifier = IdentifierCache.getIdentifierFromIdentifierCache(file);
            User user = _getUser(req);
            File newFile = new File();
            BeanUtils.copyProperties(newFile, file);
            newFile.setWorking(true);
            newFile.setLive(false);
            newFile.setInode(0);
            WorkingCache.removeAssetFromCache(file);
            LiveCache.removeAssetFromCache(file);
            String text = req.getParameter("text");
            java.io.File temp = java.io.File.createTempFile("dotcms_", ".txt");
            os = new FileOutputStream(temp);
            OutputStreamWriter out = new OutputStreamWriter(os, UtilMethods.getCharsetConfiguration());
            out.write(text);
            out.flush();
            out.close();
            os.close();
            os = null;
            File workingFile = FileFactory.saveFile(newFile, temp, parentFolder, identifier, user);
            if (workingFile.isLive()) LiveCache.addToLiveAssetToCache(workingFile);
            if (newFile.isLive()) LiveCache.addToLiveAssetToCache(newFile);
            WorkingCache.addToWorkingAssetToCache(workingFile);
            SessionMessages.add(req, "message", "message.file.text.save");
        } catch (Exception e) {
            Logger.error(this, "An error ocurred trying to save text");
            throw e;
        } finally {
            if (os != null) os.close();
        }
    }

    private void _retrieveTextAction(ActionRequest req, ActionResponse res, PortletConfig config, ActionForm form) throws Exception {
        File file = (File) InodeFactory.getInode(req.getParameter("inode"), File.class);
        String fileExtension = com.dotmarketing.util.UtilMethods.getFileExtension(file.getFileName());
        String filePath = FileFactory.getRealAssetsRootPath();
        String fileInodePath = String.valueOf(file.getInode());
        if (fileInodePath.length() == 1) {
            fileInodePath = fileInodePath + "0";
        }
        try {
            fileInodePath = fileInodePath.substring(0, 1) + java.io.File.separator + fileInodePath.substring(1, 2);
            java.io.File currF = new java.io.File(filePath + java.io.File.separator + fileInodePath + java.io.File.separator + file.getInode() + "." + fileExtension);
            Logger.debug(this, "Opening file: " + filePath + java.io.File.separator + fileInodePath + java.io.File.separator + file.getInode() + "." + fileExtension);
            java.io.FileInputStream is = new java.io.FileInputStream(currF);
            java.io.BufferedInputStream bin = new java.io.BufferedInputStream(is);
            byte[] strText = new byte[bin.available()];
            bin.read(strText);
            String strFinal = new String(strText);
            req.setAttribute(WebKeys.WYSIWYG_EDIT_TEXT, UtilMethods.escapeHTMLSpecialChars(strFinal));
        } catch (IOException e) {
            Logger.error(this, "error opening file in EditTextAction", e);
        }
    }
}
