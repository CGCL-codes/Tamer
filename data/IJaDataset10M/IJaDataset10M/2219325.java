package model;

import java.util.ArrayList;
import java.util.List;
import model.entities.Post;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Expression;

public class PostModel {

    private static Configuration cfg = null;

    private static SessionFactory factory = null;

    private static PostModel instance = null;

    public static PostModel getInstance() {
        if (instance == null) {
            try {
                cfg = new Configuration().configure();
                factory = cfg.buildSessionFactory();
            } catch (HibernateException e) {
                System.out.println("Error PostModel getInstance : " + e.toString());
            }
            instance = new PostModel();
        }
        return instance;
    }

    public PostModel() {
    }

    public Post create(Post post) {
        Session session = null;
        try {
            session = factory.openSession();
            Transaction tx = session.beginTransaction();
            session.save(post);
            tx.commit();
        } catch (HibernateException e) {
            System.out.println("Error PostsModel create : " + e.toString());
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (HibernateException e) {
                    System.out.println("Error PostsModel create : " + e.toString());
                }
            }
        }
        return post;
    }

    public Post create(String id, java.util.Date date, String title, String link, String content, boolean status, java.util.Date modified, int parent, int order) {
        Session session = null;
        Post post = null;
        try {
            session = factory.openSession();
            Transaction tx = session.beginTransaction();
            post = new Post();
            post.setId(Integer.parseInt(id));
            post.setDate(date);
            post.setTitle(title);
            post.setLink(link);
            post.setContent(content);
            post.setStatus(status);
            post.setModified(modified);
            post.setParent(parent);
            post.setOrder(order);
            session.save(post);
            tx.commit();
        } catch (HibernateException e) {
            System.out.println("Error PostsModel create : " + e.toString());
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (HibernateException e) {
                    System.out.println("Error PostsModel create : " + e.toString());
                }
            }
        }
        return post;
    }

    public Post read(int id) {
        Session session = null;
        Post post = null;
        Transaction tx = null;
        try {
            session = factory.openSession();
            tx = session.beginTransaction();
            Criteria criteria = session.createCriteria(Post.class);
            List list = criteria.add(Expression.eq("id", id)).list();
            if (list.isEmpty()) {
                throw new Exception("No user found for id: " + id);
            }
            post = (Post) list.get(0);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (HibernateException e1) {
                    System.out.println("Error PostsModel read : " + e.toString());
                }
            }
            System.out.println("Error PostsModel read : " + e.toString());
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (HibernateException e) {
                    System.out.println("Error PostsModel read : " + e.toString());
                }
            }
        }
        return post;
    }

    public void update(Post post) {
        Session session = null;
        Transaction tx = null;
        try {
            session = factory.openSession();
            tx = session.beginTransaction();
            session.update(post);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (HibernateException e1) {
                    System.out.println("Error PostsModel update : " + e.toString());
                }
            }
            System.out.println("Error PostsModel update : " + e.toString());
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (HibernateException e) {
                    System.out.println("Error PostsModel update : " + e.toString());
                }
            }
        }
    }

    public void delete(Post post) {
        Session session = null;
        Transaction tx = null;
        try {
            session = factory.openSession();
            tx = session.beginTransaction();
            session.delete(post);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (HibernateException e1) {
                    System.out.println("Error PostsModel delete : " + e.toString());
                }
            }
            System.out.println("Error PostsModel delete : " + e.toString());
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (HibernateException e) {
                    System.out.println("Error PostsModel delete : " + e.toString());
                }
            }
        }
    }

    public ArrayList<Post> find() {
        Session session = null;
        Transaction tx = null;
        try {
            session = factory.openSession();
            tx = session.beginTransaction();
            return (ArrayList<Post>) session.createQuery("from Post").list();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.out.println("Error PostsModel find : " + e.toString());
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (HibernateException e) {
                    System.out.println("Error PostsModel find : " + e.toString());
                }
            }
        }
        return null;
    }
}
