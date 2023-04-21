package org.demis.dwarf.database.reader;

import org.demis.dwarf.database.DataBaseSchema;
import org.demis.dwarf.configuration.DatabaseConfiguration;

/**
 * @version 1.0
 * @author <a href="mailto:demis27@demis27.net">Stéphane kermabon</a>
 */
public interface SchemaReader {

    public DataBaseSchema read(DatabaseConfiguration configuration);

    public DataBaseSchema read(String url, String login, String password, String shema);
}
