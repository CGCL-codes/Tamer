package org.jcr_blog.business.be;

import java.util.Collection;
import java.util.List;
import org.jcr_blog.business.be.types.PagesLineItem;
import org.jcr_blog.business.be.types.PostsLineItem;
import org.jcr_blog.business.be.types.Template;
import org.jcr_blog.domain.Admin;
import org.jcr_blog.domain.Author;
import org.jcr_blog.domain.Blog;
import org.jcr_blog.domain.Category;
import org.jcr_blog.domain.Page;
import org.jcr_blog.domain.Post;
import org.jcr_blog.domain.Stage;

/**
 *
 * @author Sebastian Prehn <sebastian.prehn@planetswebdesign.de>
 */
public interface BackendManager {

    /**
     * @param blog must not be <code>null</code>
     * @return all authors with write permissions to the provided blog
     */
    List<Author> getAuthors(Blog blog);

    Author findAuthorByUserId(String userId);

    /**
     * Returns the default blog for the provided user.
     * The default blog is the preferred blog set in the
     * user settings or (if that is not the case) any random blog the user has
     * write permissions for.
     *
     * @param author must not be <code>null</code>
     * @return the default blog of the provided author
     */
    Blog findDefaultBlogByAuthor(Author author);

    /**
     * Creates and initializes a new Blog.
     * No resourceName or id will be set.
     * @return a new post instance
     */
    Blog createBlog();

    /**
     * Persists the provided blog.
     * @param blog must not be <code>null</code>
     * @param author must not be <code>null</code>
     */
    void saveBlog(Blog blog, Author author);

    /**
     * Persists the provided blog.
     * @param blog must not be <code>null</code>
     * @param admin must not be <code>null</code>
     */
    void saveBlog(Blog blog, Admin admin);

    /**
     * @return all available templates
     */
    Collection<Template> getTemplates();

    /**
     * Finds a template by its id.
     * @param id template id
     * @return a template instance or <code>null</code> if no template with the
     *         provided id exists
     */
    Template findTemplateById(String id);

    /**
     * Finds all categories the provided post is assigned to.
     * @param post a post
     * @return all categories the provided post is assigned to
     */
    List<Category> findPostCategories(Post post);

    /**
     * Removes the provided post from persistent storage.
     * @param post must not be <code>null</code>
     */
    void deletePost(Post post);

    /**
     * Creates and initializes a new Post.
     * No resourceName or id will be set.
     * @return a new post instance
     */
    Post createPost();

    /**
     * Persists post under blog and assigns it to the provided categories.
     * @param blog parent of post, must not be <code>null</code>
     * @param post the must not be <code>null</code>
     * @param categories must not be <code>null</code> but may be an empty list
     * @param author must not be <code>null</code>
     */
    void savePost(Blog blog, Post post, List<Category> categories, Author author);

    /**
     * Finds all items in the posts listing under <code>blog</code> filtered by the provided criteria.
     * A <code>null</code> value on any criteria leaves that criterion to be ignored.
     *
     * @param blog must not be <code>null</code>
     * @param author filter criteria, may be <code>null</code>
     * @param category filter criteria, may be <code>null</code>
     * @param keywords filter criteria, may be <code>null</code>
     * @param stage filter criteria, may be <code>null</code>
     * @return
     */
    List<PostsLineItem> getPosts(Blog blog, Author author, Category category, String keywords, Stage stage);

    /**
     * Finds all items in the pages listing under <code>blog</code> filtered by the provided criteria.
     * A <code>null</code> value on any criteria leaves that criterion to be ignored.
     *
     * @param blog must not be <code>null</code>
     * @param author filter criteria, may be <code>null</code>
     * @param keywords filter criteria, may be <code>null</code>
     * @param stage filter criteria, may be <code>null</code>
     * @return
     */
    List<PagesLineItem> getPages(Blog blog, Author author, String keywords, Stage stage);

    /**
     * Removes the provided page from persistent storage.
     * @param page must not be <code>null</code>
     */
    void deletePage(Page page);

    /**
     * Persists page under blog and assigns the provided author.
     * @param blog parent of post, must not be <code>null</code>
     * @param page the must not be <code>null</code>
     * @param author must not be <code>null</code>
     */
    void savePage(Blog blog, Page page, Author author);

    /**
     * Creates and initializes a new Page.
     * No resourceName or id will be set.
     * @return a new page instance
     */
    Page createPage();

    /**
     * Returns all categories under the provided blog.
     * @param blog must not be <code>null</code>
     * @return all categories unter <code>blog</code>
     */
    List<Category> getCategories(Blog blog);

    /**
     * Removes the provided category from persistent storage.
     * @param category must not be <code>null</code>
     */
    void deleteCategory(Category category);

    /**
     * Creates and initializes a new Category.
     * No resourceName or id will be set.
     * @return a new category instance
     */
    Category createCategory();

    /**
     * Persists category under parentCategory in the provided blog.
     * @param blog parent of post, must not be <code>null</code>
     * @param category the must not be <code>null</code>
     * @param parentCategory may be <code>null</code>. if <code>null</code> the
     *                       category is persisted as root category
     * @param author must not be <code>null</code>
     */
    void saveCategory(Blog blog, Category category, Category parentCategory, Author author);

    /**
     * Finds all authors in this system.
     * @param keywords may be <code>null</code>
     * @return list of all authors. In case keywords is other than
     *         <code>null</code> the list is filtered by keywords on userid.
     */
    List<Author> getAuthors(String keywords);

    /**
     * Creates and initializes a new Author.
     * No resourceName or id will be set.
     * @return a new author instance
     */
    Author createAuthor();

    /**
     * Removes the provided author from persistent storage.
     * @param author must not be <code>null</code>
     */
    void deleteAuthor(Author author);

    /**
     * 
     * @param author
     */
    void saveAuthor(Author author);

    /**
     * Finds all blogs in the system.
     * @return a list of all blogs
     */
    List<Blog> getBlogs();

    /**
     * Finds a blog by its id.
     * @param id the blog id
     * @return the blog or <code>null</code> iff the blog was not found.
     */
    Blog findBlogById(String id);

    /**
     * Removes the provided blog from persistent storage.
     * @param blog must not be <code>null</code>
     */
    void deleteBlog(Blog blog);

    /**
     * Finds all blogs the given author has access to.
     * @param author must not be <code>null</code>
     * @return a list of blogs
     */
    List<Blog> findBlogsForAuthor(Author author);
}
