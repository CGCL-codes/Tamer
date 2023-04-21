package org.jcr_blog.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.jcr_blog.domain.Author;
import org.jcr_blog.domain.Blog;
import org.jcr_blog.domain.Category;
import org.jcr_blog.domain.Comment;
import org.jcr_blog.domain.Post;
import org.jcr_blog.domain.PostReference;
import org.jcr_blog.domain.Stage;
import org.jcr_blog.persistence.dao.CategoryDao;
import org.jcr_blog.persistence.dao.PostDao;
import org.joda.time.Interval;

/**
 *
 * @author Sebastian Prehn <sebastian.prehn@planetswebdesign.de>
 */
@Named
@ApplicationScoped
@PostServiceQualifier
public class PostServiceImpl implements PostService {

    @Inject
    private PostDao postDao;

    @Inject
    private CategoryDao categoryDao;

    @Override
    public Post findById(final String id) {
        assert (id != null);
        return this.postDao.findById(id);
    }

    @Override
    public Post findByResourceName(final String name) {
        assert (name != null);
        return this.postDao.findByResourceName(name);
    }

    @Override
    public List<Post> findBy(final Blog blog, final Author author, final Category category, final String keywords, final Stage stage, Interval interval) {
        assert (blog != null);
        return this.postDao.findByBlog(blog, author, category, keywords, stage, interval);
    }

    @Override
    public List<PostReference> findPostReferencesBy(final Blog blog, final Author author, final Category category, final String keywords, final Stage stage, Interval interval) {
        assert (blog != null);
        final List<Post> findByBlog = this.postDao.findByBlog(blog, author, category, keywords, stage, interval);
        final List<PostReference> result = new ArrayList<PostReference>(findByBlog.size());
        for (Post p : findByBlog) {
            int numComments = 0;
            final PostReference reference = new PostReference(p.getTitle(), p.getResourceName(), p.getStage(), p.getModificationUser(), p.getCreationDate(), numComments);
            result.add(reference);
        }
        return result;
    }

    @Override
    public Post findByChild(final Comment comment) {
        assert (comment != null);
        return this.postDao.findByChild(comment);
    }

    @Override
    public Post create() {
        final Post p = this.postDao.create();
        p.setCreationDate(new Date());
        return p;
    }

    @Override
    public void delete(final Post p) {
        assert (p != null);
        for (String id : p.getCategoryIds()) {
            Category category = this.categoryDao.findById(id);
            if (category != null) {
                category.getPostIds().remove(id);
                this.categoryDao.save(category);
            }
        }
        this.postDao.delete(p);
    }

    @Override
    public void save(final Blog parent, final Post post, final List<Category> categories, final Author author) {
        assert (parent != null);
        assert (post != null);
        assert (post.getTitle() != null);
        assert (!post.getTitle().isEmpty());
        assert (categories != null);
        assert (author != null);
        assert (author.getUserId() != null);
        assert (post.getCreationDate() != null);
        assert (post.getStage() != null);
        assert (post.getText() != null);
        final Date now = new Date();
        post.setModificationUser(author.getUserId());
        post.setModificationDate(now);
        if (post.getResourceName() == null || post.getResourceName().isEmpty()) {
            post.setResourceName(ResourceHelper.createResourceName(now, post.getTitle()));
        }
        this.postDao.save(parent, post);
        post.getCategoryIds().clear();
        for (Category c : categories) {
            post.getCategoryIds().add(c.getId());
            c.addPostIds(post.getId());
            this.categoryDao.save(c);
        }
    }

    @Override
    public List<Category> findPostCategories(final Post post) {
        final Set<String> categoryIds = post.getCategoryIds();
        final ArrayList<Category> result = new ArrayList<Category>(categoryIds.size());
        for (String categoryId : categoryIds) {
            Category category = this.categoryDao.findById(categoryId);
            if (category != null) {
                result.add(category);
            }
        }
        return result;
    }

    @Override
    public List<Category> findPostCategories(final PostReference p) {
        final Post post = this.postDao.findByResourceName(p.getResourceName());
        return this.findPostCategories(post);
    }
}
