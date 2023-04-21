package blog;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/CommentEdit.action")
public class CommentEditAction extends Action {

    private int id = 0;

    private Comment comment = null;

    public void setCommentId(int id) {
        this.id = id;
    }

    public Resolution execute() throws Exception {
        comment = commentDao.find(id);
        return new ForwardResolution("/CommentEdit.jsp");
    }

    public Comment getComment() {
        return comment;
    }
}
