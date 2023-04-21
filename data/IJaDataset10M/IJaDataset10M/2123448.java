package net.sf.mytoolbox.jdbc.io;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exports a result set rows as SQL INSERT statements. <br/>
 * @author ggrussenmeyer
 */
public class DataExporter implements ResultSetHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String tableName;

    private final PrintWriter writer;

    /**
     * Creates a new exporter that will generates INSERTs to the table with
     * the given name to the given stream. <br/>
     * @param tableName
     * @param stream
     */
    public DataExporter(String tableName, OutputStream stream) {
        this(tableName, new PrintWriter(stream, true));
    }

    /**
     * Creates a new exporter that will generates INSERTs to the table with
     * the given name to the given writer. <br/>
     * @param tableName
     * @param writer
     */
    public DataExporter(String tableName, Writer writer) {
        this.tableName = tableName;
        this.writer = writer instanceof PrintWriter ? (PrintWriter) writer : new PrintWriter(writer, true);
    }

    /**
     * Gets the INSERT statement template for the given metadata. <br/>
     * @param metaData
     * @return The template to be used
     * @throws SQLException if metadata cannot be used
     */
    protected String getTemplate(ResultSetMetaData metaData) throws SQLException {
        StringBuilder template = new StringBuilder("INSERT INTO ");
        template.append(this.tableName);
        template.append(" ( ");
        int columnCount = metaData.getColumnCount();
        for (int index = 1; index <= columnCount; index++) {
            template.append(metaData.getColumnLabel(index));
            if (index < columnCount) {
                template.append(", ");
            }
        }
        template.append(" ) VALUES ( ");
        for (int index = 1; index <= columnCount; index++) {
            template.append("%s");
            if (index < columnCount) {
                template.append(", ");
            }
        }
        template.append(" );");
        return template.toString();
    }

    /**
     * Iterates on the given result set and for each row, generates
     * an INSERT statement to the table given at creation time. <br/>
     * Statements are written to the output stream or writer given at
     * creation time. <br/>
     * @return null
     */
    public Object handle(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        final String template = this.getTemplate(metaData);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Template is {}", template);
        }
        this.writer.println(String.format("-- Insert into table '%s'", this.tableName));
        final int columnCount = metaData.getColumnCount();
        while (resultSet.next()) {
            String[] values = new String[columnCount];
            for (int index = 1; index <= columnCount; index++) {
                Object value = resultSet.getObject(index);
                if (resultSet.wasNull()) {
                    values[index - 1] = "NULL";
                } else {
                    values[index - 1] = String.format("'%s'", value.toString().replace("'", "''"));
                }
            }
            String statement = String.format(template, (Object[]) values);
            if (this.logger.isDebugEnabled()) {
                this.logger.debug(statement);
            }
            this.writer.println(statement);
        }
        return null;
    }
}
