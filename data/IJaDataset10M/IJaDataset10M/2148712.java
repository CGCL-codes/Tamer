package org.ddth.txbb.board.mvc;

import javax.servlet.ServletException;
import org.ddth.panda.BaseLanguage;
import org.ddth.skinengine.freemarker.FMElement;
import org.ddth.txbb.base.TXBBApp;
import org.ddth.txbb.base.TXBBConstants;
import org.ddth.txbb.base.ui.FormRenderer;
import org.ddth.txbb.board.bo.Post;
import org.ddth.txbb.board.ui.TopicRenderer;

public class BoardDeletePostModeller extends AbstractBoardModeller {

    /**
	 * Auto-generated serial version UID.
	 */
    private static final long serialVersionUID = 1737032387958250639L;

    private Post post;

    private FormRenderer formRenderer;

    private static final String MODEL_FORM = "form";

    private static final String MODEL_POST = "post";

    public BoardDeletePostModeller(TXBBApp app, String domain, String action) {
        super(app, domain, action);
    }

    @Override
    public void perform() throws ServletException {
        super.perform();
        BaseLanguage lang = _APP.getLanguage();
        if (post == null) post = (Post) getAttribute(BoardReplyPostController.ATTR_POST);
        String title = _APP.getConfig().getSiteName();
        if (title == null) title = "";
        pageRenderer.setTitle(lang.getText(TXBBConstants.APP_DOMAIN_COMMON, "delete") + ": " + post.getTitle() + " - " + title);
    }

    @Override
    protected void modelCenterColumn() throws ServletException {
        if (formRenderer == null) formRenderer = (FormRenderer) getAttribute(BoardReplyPostController.ATTR_FORM_RENDERER);
        if (post == null) post = (Post) getAttribute(BoardDeletePostController.ATTR_POST);
        TopicRenderer tp;
        try {
            tp = TopicRenderer.getInstance((TXBBApp) _APP, post.getTopic());
        } catch (Exception e) {
            throw new ServletException(e.getMessage(), e);
        }
        TopicRenderer.PostRenderer deletingPost = tp.getPostRenderer(post);
        FMElement skinColumnCenter = pageRenderer.createColumnCenter();
        skinColumnCenter.assignSkin(MODEL_POST, deletingPost);
        skinColumnCenter.assignSkin(MODEL_FORM, formRenderer);
    }
}
