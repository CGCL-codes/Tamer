package net.techwatch.commons.backup;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import net.techwatch.commons.DbTool;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BackupTool extends DbTool {

    private static final String ENCODING = "UTF-8";

    private Log log = LogFactory.getLog(BackupTool.class);

    private String tablename;

    private Writer writer = null;

    private DynaProperty[] dynaProperties;

    public String getTablename() {
        return this.tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    void setDynaProperties(DynaProperty[] dynaProperties) {
        this.dynaProperties = dynaProperties;
    }

    @Override
    public boolean process() {
        QueryRunner runner = new QueryRunner(dataSource);
        BeanWriterHandler handler = new BeanWriterHandler(this);
        try {
            File f = new File(filename);
            if (!f.createNewFile()) throw new IOException("Cannot create file " + filename);
            writer = new FileWriterWithEncoding(f, ENCODING);
            runner.query("select * from " + tablename, handler);
            return true;
        } catch (SQLException e) {
            log.fatal("Error while executing SQL statement", e);
            return false;
        } catch (IOException e) {
            log.fatal("Error while opening writer", e);
            return false;
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }

    void writeData(DynaBean dBean) throws IOException {
        for (DynaProperty dProperty : dynaProperties) {
            writer.write(dBean.get(dProperty.getName()).toString());
            if (!dProperty.equals(dynaProperties[dynaProperties.length - 1])) {
                writer.write(",");
            }
        }
        writer.write("\n");
    }
}
