package cn.fantix.gnualbumalpha2;

/**
 * <pre>
 * 还没有注释。
 * </pre>
 *
 * @author fantix
 * @date 2007-7-12 13:43:01
 */
public interface UrlParserListener {

    public void begin();

    public void parsedOne();

    public void parsedAll(Item[] fs);

    public void timedOut();

    public void error(String message);
}
