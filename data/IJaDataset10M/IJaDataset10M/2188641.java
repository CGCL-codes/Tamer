package net.mikaboshi.ant;

import net.mikaboshi.jdbc.ResultDataFormatter;
import net.mikaboshi.jdbc.SimpleFormatter;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.taskdefs.JDBCTask;

/**
 * <p>
 * データベースの全て（またはパターンに一致する）テーブルをファイルにエクスポートするAntタスクの抽象クラス。
 * </p><p>
 * このクラスは同期化されない。
 * </p>
 * @author Takuma Umezawa
 * @since 1.0.1
 */
public abstract class Db2FileTask extends JDBCTask {

    private boolean replace = true;

    private String schema;

    private boolean header = true;

    private String nullString = StringUtils.EMPTY;

    private boolean haltOnError = true;

    private Patterns patterns = new Patterns();

    public Db2FileTask() {
        super();
    }

    /**
	 * エクスポートファイルが既に存在した場合、ファイルを上書きするかどうかを設定する。
	 * trueならば上書きする。falseならば上書きしない。
	 * デフォルトはtrue。
	 * @param replace
	 */
    public void setReplace(boolean replace) {
        this.replace = replace;
    }

    /**
	 * エクスポートファイル（テーブル名.csv）が既に存在した場合、ファイルを上書きするかどうか。
	 * trueならば上書きする。falseならば上書きしない。
	 * @return
	 */
    protected boolean isReplace() {
        return this.replace;
    }

    /**
	 * ファイルにカラム名を出力するかどうかを設定する。
	 * 省略可。（デフォルトはtrue：出力する）
	 * カラム名を出力する場合、SELECT文に指定された列名が使われる。
	 * 
	 * @param header
	 */
    public void setHeader(boolean header) {
        this.header = header;
    }

    /**
	 * ファイルにカラム名を出力するかどうかを取得する。
	 * @return
	 */
    protected boolean isHeaderNeeded() {
        return this.header;
    }

    /**
	 * nullの場合の出力文字を設定する。
	 * 省略時は、空文字が出力される。
	 * 
	 * @param nullString
	 */
    public void setNullString(String nullString) {
        this.nullString = nullString;
    }

    /**
	 * nullの場合の出力文字を取得する。
	 * @return
	 */
    protected String getNullString() {
        return this.nullString;
    }

    /**
	 * スキーマ名を設定する。
	 * 省略時はスキーマ指定なし。
	 * @param schema
	 */
    public void setSchema(String schema) {
        this.schema = schema;
    }

    /**
	 * スキーマ名を取得する。
	 * @return
	 */
    protected String getSchema() {
        return this.schema;
    }

    /**
	 * 出力対象のテーブル名の正規表現パターンを指定する。
	 * 省略時は、全てのテーブルを出力対象とする。
	 * @param pattern
	 */
    public void setPattern(String pattern) {
        if (StringUtils.isBlank(pattern)) {
            return;
        }
        Patterns.Include include = this.patterns.createInclude();
        include.addText(pattern);
    }

    protected Patterns getPatterns() {
        return this.patterns;
    }

    public Patterns createPatterns() {
        return this.patterns;
    }

    /**
	 * <p>
	 * エラー（SQLException）発生時に、エクスポート処理を中断するかどうかを指定する。
	 * </p><p>
	 * 中断する場合はtrue。
	 * 次のテーブルから続ける場合はfalse。
	 * （省略化。デフォルトはtrue。）
	 * </p>
	 * @param haltOnError
	 */
    public void setHaltonerror(boolean haltOnError) {
        this.haltOnError = haltOnError;
    }

    /**
	 * エラー（SQLException）発生時に、エクスポート処理を中断するかを取得する。
	 * @return
	 */
    protected boolean isHaltOnError() {
        return this.haltOnError;
    }

    /**
	 * ResultSetの文字列表現を行うオブジェクトを取得する。
	 * @return
	 */
    protected ResultDataFormatter getFormatter() {
        SimpleFormatter formatter = new SimpleFormatter();
        if (getNullString() != null) {
            formatter.setNullString(getNullString());
        }
        return formatter;
    }
}
