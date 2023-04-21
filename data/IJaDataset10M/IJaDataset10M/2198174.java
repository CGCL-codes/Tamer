package net.mikaboshi.ant;

import static net.mikaboshi.validator.SimpleValidator.validateNotNull;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;
import net.mikaboshi.jdbc.DbUtils;
import net.mikaboshi.jdbc.QueryExecutor;
import net.mikaboshi.jdbc.ResultSetHandler;
import net.mikaboshi.jdbc.ResultSetToXLSHandler;
import net.mikaboshi.jdbc.schema.SchemaUtils;
import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.BuildException;

/**
 * <p>
 * データベースの全て（またはパターンに一致する）テーブルをExcelファイルにエクスポートするAntタスク。
 * </p><p>
 * output 属性に指定したファイルに、「スキーマ名.テーブル名」　というシートが追加される。
 * schema 属性を省略した場合は、テーブル名がシート名となる。
 * </p><p>
 * このクラスは同期化されない。
 * </p>
 * @author Takuma Umezawa
 * @since 1.0.1
 */
public class Db2XlsTask extends Db2FileTask {

    private final M17NTaskLogger logger;

    /**
	 * 本タスクのプロパティをデフォルトに設定する。
	 */
    public Db2XlsTask() {
        super();
        this.logger = new M17NTaskLogger(this, AntConstants.LOG_MESSAGE_BASE_NAME);
    }

    private File output;

    /**
	 * エクスポートファイルを設定する。（必須）
	 * @param path
	 * @throws IOException 
	 */
    public void setOutput(String path) throws IOException {
        this.output = new File(path);
        FileUtils.forceMkdir(new File(this.output.getParent()));
    }

    private String sheetName;

    public void execute() throws BuildException {
        validateNotNull(this.output, "output", BuildException.class);
        if (this.output.exists() && this.output.isFile()) {
            if (isReplace()) {
                this.logger.info("db2file.overwrite", getPatterns().toString(), this.output.getAbsolutePath());
            } else {
                this.logger.warn("db2file.skip", getPatterns().toString(), this.output.getAbsolutePath());
                return;
            }
        }
        logger.info("db2file.file_name", this.output.getAbsolutePath());
        Connection conn = null;
        try {
            conn = getConnection();
            Set<String> allTableNames = SchemaUtils.getAllTableNames(conn, null, getSchema(), null, null);
            for (String tableName : allTableNames) {
                if (!getPatterns().isMatch(tableName)) {
                    continue;
                }
                if (tableName.length() > 30) {
                    logger.warn("db2file.table_name_too_long", tableName);
                    continue;
                }
                logger.info("db2file.table_name", tableName);
                this.sheetName = tableName;
                String sql = "select * from " + tableName;
                try {
                    new QueryExecutor(conn, createHandler()).execute(sql);
                } catch (SQLException e) {
                    if (isHaltOnError()) {
                        throw e;
                    }
                    this.logger.warn(e, "db2file.continue_on_error", tableName);
                    DbUtils.rollbackQuietly(conn);
                } finally {
                }
            }
        } catch (IOException e) {
            throw new BuildException(e);
        } catch (SQLException e) {
            throw new BuildException(e);
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

    private boolean replaceSheet = false;

    /**
	 * @since 1.1.8
	 */
    public void setReplaceSheet(boolean replaceSheet) {
        this.replaceSheet = replaceSheet;
    }

    protected ResultSetHandler createHandler() throws IOException {
        return new ResultSetToXLSHandler(this.output, true, isHeaderNeeded(), false, false, this.replaceSheet, getFormatter(), this.sheetName);
    }
}
