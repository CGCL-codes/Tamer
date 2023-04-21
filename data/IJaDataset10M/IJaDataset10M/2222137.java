package news_rack.user_interface;

import news_rack.GlobalConstants;
import news_rack.database.User;
import news_rack.database.DB_Interface;
import java.util.List;
import java.util.LinkedList;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

/**
 * class <code>UploadFilesAction</code> implements the functionality
 * of uploading profile files for News Rack.
 */
public class UploadFilesAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        User user = (User) request.getSession().getAttribute(GlobalConstants.USER_KEY);
        if (user == null) {
            ActionErrors errs = new ActionErrors();
            errs.add("error.upload", new ActionMessage("error.message", "Session has expired!  Please sign in again"));
            saveErrors(request, errs);
            return (mapping.findForward("goto.home"));
        }
        request.setAttribute(GlobalConstants.USER_KEY, user);
        List fileList = new LinkedList();
        DB_Interface db = GlobalConstants.GetDBInterface();
        FormFile f = ((UploadFilesForm) form).getFile();
        try {
            if (f != null) {
                String fName = f.getFileName();
                if ((fName != null) && !fName.equals("")) {
                    fileList.add("File: " + fName);
                    user.AddFile(fName);
                    db.UploadFile(fName, f.getInputStream(), user);
                }
            }
        } catch (java.lang.Exception e) {
            ActionErrors errs = new ActionErrors();
            errs.add("error.upload", new ActionMessage("error.file.upload"));
            errs.add("error.upload", new ActionMessage("error.message", e.toString()));
            saveErrors(request, errs);
            return (mapping.findForward("goto.upload"));
        }
        if (!fileList.isEmpty()) {
            request.setAttribute("uploadedFiles", fileList);
            db.UpdateUser(user);
        }
        return (mapping.findForward("goto.upload"));
    }
}
