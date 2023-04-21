package org.ddth.txbb.board.mvc;

import javax.servlet.ServletException;
import org.ddth.panda.BaseLanguage;
import org.ddth.skinengine.freemarker.FMElement;
import org.ddth.txbb.base.TXBBApp;
import org.ddth.txbb.base.TXBBConstants;
import org.ddth.txbb.base.ui.FormRenderer;
import org.ddth.txbb.board.bo.Post;
import org.ddth.txbb.board.bo.Zone;
import org.ddth.txbb.board.ui.TopicRenderer;

public class BoardEditPostModeller extends AbstractBoardModeller {

    /**
	 * Auto-generated serial version UID.
	 */
    private static final long serialVersionUID = 1737032387958250639L;

    private Zone zone;

    private Post post, postPreview;

    private FormRenderer formRenderer;

    private static final String MODEL_PREVIEW_POST = "previewPost";

    private static final String MODEL_FORM = "form";

    private static final String MODEL_POST = "post";

    public BoardEditPostModeller(TXBBApp app, String domain, String action) {
        super(app, domain, action);
    }

    @Override
    public void perform() throws ServletException {
        super.perform();
        BaseLanguage lang = _APP.getLanguage();
        if (post == null) post = (Post) getAttribute(BoardReplyPostController.ATTR_POST);
        String title = _APP.getConfig().getSiteName();
        if (title == null) title = "";
        pageRenderer.setTitle(lang.getText(TXBBConstants.APP_DOMAIN_COMMON, "edit") + ": " + post.getTitle() + " - " + title);
    }

    @Override
    protected void modelCenterColumn() throws ServletException {
        if (formRenderer == null) formRenderer = (FormRenderer) getAttribute(BoardReplyPostController.ATTR_FORM_RENDERER);
        if (zone == null) zone = (Zone) getAttribute(BoardReplyPostController.ATTR_ZONE);
        if (post == null) post = (Post) getAttribute(BoardReplyPostController.ATTR_POST);
        TopicRenderer tp;
        try {
            tp = TopicRenderer.getInstance((TXBBApp) _APP, post.getTopic());
        } catch (Exception e) {
            throw new ServletException(e.getMessage(), e);
        }
        TopicRenderer.PostRenderer editingPost = tp.getPostRenderer(post);
        if (postPreview == null) postPreview = (Post) getAttribute(BoardReplyPostController.ATTR_PREVIEW_POST);
        FMElement skinColumnCenter = pageRenderer.createColumnCenter();
        skinColumnCenter.assignSkin(MODEL_POST, editingPost);
        skinColumnCenter.assignSkin(MODEL_FORM, formRenderer);
        skinColumnCenter.assignSkin(MODEL_PREVIEW_POST, postPreview);
    }
}
