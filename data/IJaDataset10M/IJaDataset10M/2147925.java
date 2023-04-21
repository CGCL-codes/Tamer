package example.chapter5;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 * Define BookDao CRUD operations.
 * 
 * @author Xuefeng
 */
@Transactional
public interface BookDao {

    List<Book> queryAll();

    List<Book> queryByAuthor(String author);

    void create(Book book);

    void update(Book book);

    void delete(String id);
}
