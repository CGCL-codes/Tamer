package ejb3.session;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import ejb3.entity.BookEJB3;
import ejb3.entity.StudentBook;

@Stateless
public class BookSession implements BookRemote {

    @PersistenceContext(unitName = "bookSystem")
    private EntityManager manager;

    public Collection SimpleQuery(String name) throws Exception {
        List books = manager.createQuery("from BookEJB3 where name like :name").setParameter("name", "%" + name + "%").getResultList();
        System.out.println("by ejb3");
        return books;
    }

    public void addBook(BookEJB3 book) throws Exception {
        int generatedId = (int) System.currentTimeMillis();
        book.setId(new BigDecimal(generatedId));
        System.out.println("by ejb3");
        manager.persist(book);
    }

    public Collection advancedQuery(String name, String author, String minPrice, String maxPrice, String publisher) throws Exception {
        StringBuffer sql = new StringBuffer("from BookEJB3 ");
        int pqn = 0;
        if (name != null && name.length() > 0) {
            sql.append(" where name like '%").append(name).append("%' ");
            pqn++;
        }
        if (author != null && author.length() > 0) {
            if (pqn == 0) {
                sql.append(" where author='").append(author);
            } else {
                sql.append(" and author='").append(author);
            }
            pqn++;
            sql.append("' ");
        }
        if (minPrice != null && minPrice.length() > 0) {
            if (pqn == 0) {
                sql.append(" where price >=").append(minPrice);
            } else {
                sql.append(" and price >=").append(minPrice);
            }
            pqn++;
        }
        if (maxPrice != null && maxPrice.length() > 0) {
            if (pqn == 0) {
                sql.append(" where price <=").append(maxPrice);
            } else {
                sql.append(" and price <=").append(maxPrice);
            }
            pqn++;
        }
        if (publisher != null && publisher.length() > 0) {
            if (pqn == 0) {
                sql.append(" where publisher like '%").append(publisher).append("%' ");
            } else {
                sql.append(" and publisher like '%").append(publisher).append("%'");
            }
        }
        List books = manager.createQuery(sql.toString()).getResultList();
        System.out.println("by ejb3");
        return books;
    }

    public void backBook(String id, String studentName) throws Exception {
        BookEJB3 book = manager.find(BookEJB3.class, new BigDecimal(id));
        Set student = book.getLendStudent();
        System.out.println("by ejb3");
        for (Object o : student) {
            StudentBook sb = (StudentBook) o;
            if (sb.getStudentName().equals(studentName)) {
                student.remove(o);
                return;
            }
        }
    }

    public void delete(String id) throws Exception {
        System.out.println("by ejb3");
        BookEJB3 book = manager.find(BookEJB3.class, new BigDecimal(id));
        manager.remove(book);
    }

    public Object getBookDetail(String id) throws Exception {
        System.out.println("by ejb3");
        BookEJB3 book = (BookEJB3) manager.find(BookEJB3.class, new BigDecimal(id));
        Map lendBookDetail = new LinkedHashMap();
        Set student = book.getLendStudent();
        int number = book.getNumber().intValue();
        Iterator is = student.iterator();
        for (int i = 0; i < number; i++) {
            if (is.hasNext()) {
                lendBookDetail.put("book" + i, ((StudentBook) is.next()).getStudentName());
            } else {
                lendBookDetail.put("book" + i, "�ڹ�");
            }
        }
        book.setBookLendDetail(lendBookDetail);
        return book;
    }

    public Object getById(BigDecimal id) throws Exception {
        System.out.println("by ejb3");
        return manager.find(BookEJB3.class, id);
    }

    public void lendBook(String id, String studentName) throws Exception {
        System.out.println("by ejb3");
        StudentBook sb = new StudentBook();
        sb.setStudentName(studentName);
        manager.persist(sb);
        BookEJB3 book = manager.find(BookEJB3.class, new BigDecimal(id));
        book.getLendStudent().add(sb);
    }

    public void update(BookEJB3 book) throws Exception {
        System.out.println("by ejb3");
        BookEJB3 b = manager.find(BookEJB3.class, book.getId());
        b.setName(book.getName());
        b.setAuthor(book.getAuthor());
        b.setPublisher(book.getPublisher());
        b.setPublishDate(book.getPublishDate());
        b.setFeature(book.getFeature());
        b.setPrice(book.getPrice());
        b.setNumber(book.getNumber());
    }
}
