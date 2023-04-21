package javax.sql.rowset.spi;

import java.io.Reader;
import java.sql.SQLException;
import javax.sql.RowSetReader;
import javax.sql.rowset.WebRowSet;

public interface XmlReader extends RowSetReader {

    void readXML(WebRowSet caller, Reader reader) throws SQLException;
}
