package blog;

import java.util.List;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/PostView.action")
public class PostViewAction extends Action {

    private int postId = 0;

    private Post post;

    private List<Category> cates;

    private List<Comment> comments;

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public Resolution execute() throws Exception {
        post = postDao.find(postId);
        cates = categoryDao.findAll();
        comments = commentDao.findAll(postId);
        return new ForwardResolution("/PostView.jsp");
    }

    public List<Comment> getComments() {
        return comments;
    }

    public List<Category> getCates() {
        return cates;
    }

    public int getPostId() {
        return postId;
    }

    public Post getPost() {
        return post;
    }
}
