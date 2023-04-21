package br.usp.ime.forum.ic;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.classic.Session;
import br.usp.ime.forum.dao.TopicoDAO;
import br.usp.ime.forum.hibernate.HibernateUtil;
import br.usp.ime.forum.model.Topico;

public class TagDAO {

    private TagDAO() {
    }

    private static final TagDAO INSTANCE = new TagDAO();

    public static TagDAO getInstance() {
        return INSTANCE;
    }

    public List<Tag> findTagByName(String tagName) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Tag> tags = (List<Tag>) session.createQuery("from Tag tag where tag.name like :piece").setParameter("piece", "%" + tagName + "%").list();
        session.getTransaction().commit();
        return tags;
    }

    public Tag findTagById(long tagid) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Tag tag = (Tag) session.get(Tag.class, tagid);
        session.getTransaction().commit();
        return tag;
    }

    public void addTag(String name) {
        Tag tag = new Tag();
        tag.setName(name);
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.save(tag);
        session.getTransaction().commit();
    }

    public void addTag(Tag tag) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.save(tag);
        session.getTransaction().commit();
    }

    public void removeTagById(long id) {
        List<Topico> topicos = TopicoDAO.getInstance().listarTodos();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Tag tag = (Tag) session.get(Tag.class, id);
        List<Topico> result = new ArrayList<Topico>();
        for (Topico top : topicos) {
            if (top.getTags().contains(tag)) {
                top.getTags().remove(tag);
                session.update(top);
            }
        }
        session.flush();
        session.delete(tag);
        session.getTransaction().commit();
    }

    public List<Tag> getAllTags() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Tag> result = session.createQuery("from Tag").list();
        session.getTransaction().commit();
        return result;
    }
}
