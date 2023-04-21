package oxygen.forum.view;

import java.io.File;
import oxygen.forum.ForumEngine;
import oxygen.forum.ForumLocal;
import oxygen.web.GenericWebAction;
import oxygen.web.ViewContext;
import oxygen.web.WebInteractionContext;
import oxygen.web.WebLocal;

public class UploadFileAction extends GenericWebAction {

    {
        setFlag(FLAG_WRITE_ACTION);
    }

    public int processAction() throws Exception {
        ForumEngine engine = ForumLocal.getForumEngine();
        WebInteractionContext wctx = WebLocal.getWebInteractionContext();
        File uploadDir = engine.getUploadDirectory();
        File[] files = wctx.getUploadedFiles();
        String subpath = wctx.getParameter("upload.subpath");
        String nextview = wctx.getParameter("upload.nextview");
        File fbase = new File(uploadDir, subpath);
        fbase.mkdirs();
        for (int i = 0; i < files.length; i++) {
            File f = new File(fbase, files[i].getName());
            files[i].renameTo(f);
        }
        ViewContext vctx = WebLocal.getViewContext();
        vctx.setAction(nextview);
        return ACTION_PROCESSING_COMPLETED | REDIRECT_AFTER_POST;
    }
}
