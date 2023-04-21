package jumpingnotes.dao;

import java.util.List;
import jumpingnotes.model.entity.BookVersion;

public interface BookVersionDao extends GenericDao<BookVersion> {

    List<BookVersion> findValueByBookIdAndProperty(int bookId, String property);

    List<BookVersion> findBookVersionByBookId(int bookId, int firstResult, int maxResults);
}
