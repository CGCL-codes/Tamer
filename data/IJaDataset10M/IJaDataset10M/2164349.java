package edu.DAOImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import edu.DAO.BlogDAO;
import edu.DAO.DAOFactory;
import edu.domains.Blog;
import edu.domains.Comment;
import edu.domains.User;
import edu.servlet.CloseSessionWithCommitFilter;

/**
 * @author xiyu
 *
 */
public class BlogHibernateDAO extends GenericHibernateDAO<Blog, String> implements BlogDAO {

    public List<Comment> getComments(Blog blog) {
        String query = "from Comment c where c.blog = :blog" + " order by c.postTime desc";
        @SuppressWarnings("unchecked") List<Comment> comments = getSession().createQuery(query).setEntity("blog", blog).list();
        return comments;
    }

    public List<Blog> getBlogsByType(String type, int start, int end) {
        int length = end - start + 1;
        String query = "from Blog b where b.type = :type" + " order by b.postTime desc";
        @SuppressWarnings("unchecked") List<Blog> blogs = getSession().createQuery(query).setParameter("type", type).setFirstResult(start).setMaxResults(length).list();
        return blogs;
    }

    public int getCount() {
        String query = "select count(b) from Blog b";
        Integer count = (Integer) getSession().createQuery(query).uniqueResult();
        return count;
    }

    public int getCount(User user) {
        String query = "select count(b) from Blog b where b.user = :user";
        Integer count = (Integer) getSession().createQuery(query).setEntity("user", user).uniqueResult();
        return count;
    }

    public int getCount(String type) {
        String query = "select count(b) from Blog b where b.type =:type";
        Integer count = (Integer) getSession().createQuery(query).setParameter("type", type).uniqueResult();
        return count;
    }

    public Blog getSpecialByTitle(String title) {
        String query = "from Blog b where b.title =:title";
        Blog blog = (Blog) getSession().createQuery(query).setParameter("title", title).uniqueResult();
        return blog;
    }

    public List<Blog> searchBlogs(String title) {
        String query = "from Blog b where b.title like :title order by b.postTime desc";
        String t = "%" + title + "%";
        @SuppressWarnings("unchecked") List<Blog> blogs = getSession().createQuery(query).setParameter("title", t).list();
        return blogs;
    }

    public static void main(String[] args) {
        User s = (User) HibernateUtil.getSession().createQuery("from User").uniqueResult();
        System.out.println(s.getUsername());
        HibernateUtil.commitTransaction();
        HibernateUtil.closeSession();
    }
}
