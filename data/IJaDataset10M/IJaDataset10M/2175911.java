package life.dao;

import java.util.List;
import life.domain_model.Discussion;
import life.domain_model.ForumCategory;
import life.domain_model.Post;
import life.domain_model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ForumDaoImpl {

    @Autowired
    ForumDao forumDao;

    public ForumDaoImpl() {
    }

    public Discussion getDiscussion(Long id) {
        return forumDao.findDiscussion(id);
    }

    public Post mergePost(Object o) {
        return (Post) forumDao.merge(o);
    }

    public Discussion mergeDiscussion(Object o) {
        return (Discussion) forumDao.merge(o);
    }

    public void savePost(Post post) {
        forumDao.save(post);
    }

    public void saveNewDiscussion(Discussion d) {
        User owner = d.getPosts().get(0).getOwner();
        owner.incrementMessageCounter();
        StaticBean.getUserBean().mergeUser(owner);
        forumDao.save(d);
    }

    public List<Discussion> getDiscussionByCategory(ForumCategory cat) {
        return forumDao.getDiscussionByCategory(cat);
    }

    public void saveNewPostForDiscussion(Discussion discussion) {
        User userToInc = discussion.getPosts().get(discussion.getPosts().size() - 1).getOwner();
        userToInc.incrementMessageCounter();
        StaticBean.getUserBean().mergeUser(userToInc);
        mergeDiscussion(discussion);
    }

    public Long getNumberOfDiscussionByCategory(ForumCategory category) {
        return forumDao.countDiscussionByCategory(category);
    }
}
