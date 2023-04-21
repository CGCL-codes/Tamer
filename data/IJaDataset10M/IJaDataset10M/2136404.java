package org.jcr_blog.service;

import java.util.Date;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.jcr_blog.domain.Admin;
import org.jcr_blog.domain.Author;
import org.jcr_blog.domain.Blog;
import org.jcr_blog.domain.Category;
import org.jcr_blog.domain.Page;
import org.jcr_blog.domain.Post;
import org.jcr_blog.persistence.dao.BlogDao;

/**
 *
 * @author Sebastian Prehn <sebastian.prehn@planetswebdesign.de>
 */
@Named
@ApplicationScoped
@BlogServiceQualifier
public class BlogServiceImpl implements BlogService {

    @Inject
    private BlogDao blogDao;

    @Override
    public Blog findById(final String id) {
        assert (id != null);
        return blogDao.findById(id);
    }

    @Override
    public Blog findByResourceName(final String blogResourceName) {
        assert (blogResourceName != null);
        return blogDao.findByResourceName(blogResourceName);
    }

    @Override
    public List<Blog> findBlogs() {
        return this.blogDao.findAll();
    }

    @Override
    public String getDefaultBlogResourceName() {
        final Blog defaultBlog = this.blogDao.findDefaultBlog();
        return defaultBlog == null ? null : defaultBlog.getResourceName();
    }

    @Override
    public Blog findByChild(final Category category) {
        assert (category != null);
        return this.blogDao.findByChild(category);
    }

    @Override
    public Blog findByChild(final Page page) {
        assert (page != null);
        return this.blogDao.findByChild(page);
    }

    @Override
    public Blog findByChild(final Post post) {
        assert (post != null);
        return this.blogDao.findByChild(post);
    }

    @Override
    public Blog create() {
        return this.blogDao.create();
    }

    @Override
    public void delete(final Blog t) {
        assert (t != null);
        this.blogDao.delete(t);
    }

    @Override
    public void save(final Blog t, final Author author) {
        assert (t != null);
        assert (t.getTitle() != null);
        assert (!t.getTitle().isEmpty());
        assert (author != null);
        assert (author.getUserId() != null);
        assert (!author.getUserId().isEmpty());
        this.save(t, author.getUserId());
    }

    @Override
    public void save(final Blog t, final Admin admin) {
        assert (t != null);
        assert (t.getTitle() != null);
        assert (!t.getTitle().isEmpty());
        assert (admin != null);
        assert (admin.getUserId() != null);
        assert (!admin.getUserId().isEmpty());
        this.save(t, admin.getUserId());
    }

    private void save(final Blog t, final String userId) {
        assert (t != null);
        assert (t.getTitle() != null);
        assert (!t.getTitle().isEmpty());
        assert (userId != null);
        assert (!userId.isEmpty());
        final Date now = new Date();
        t.setModificationDate(now);
        t.setModificationUser(userId);
        if (t.getResourceName() == null || t.getResourceName().isEmpty()) {
            t.setResourceName(ResourceHelper.createResourceName(now, t.getTitle()));
        }
        if (t.isDefaultBlog()) {
            final Blog defaultBlog = this.blogDao.findDefaultBlog();
            if (defaultBlog != null) {
                defaultBlog.setDefaultBlog(false);
                defaultBlog.setModificationDate(now);
                defaultBlog.setModificationUser(userId);
                this.blogDao.save(defaultBlog);
            }
        }
        this.blogDao.save(t);
    }
}
