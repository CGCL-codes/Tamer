package flickr.response;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author leon
 */
@XmlRootElement(name = "comments")
public class Comments extends ResponseObject {

    private List<Comment> comments;

    public Comments() {
    }

    @XmlElements(@XmlElement(name = "comment"))
    public List<Comment> getComments() {
        if (comments == null) {
            comments = new ArrayList<Comment>();
        }
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Comment> getUserComments(User user) {
        List<Comment> comments = getComments();
        List<Comment> userComments = new ArrayList<Comment>();
        for (Comment c : comments) {
            if (c.getAuthor().equals(user.getId())) {
                userComments.add(c);
            }
        }
        return userComments;
    }
}
