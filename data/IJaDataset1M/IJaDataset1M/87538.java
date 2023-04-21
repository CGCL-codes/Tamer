package xprint;

/**
 * XPrintException
 * @author Kazuhiko Arase
 */
public class XPrintException extends Exception {

    /**
     * コンストラクタ
     * @param message メッセージ
     */
    public XPrintException(String message) {
        super(message);
    }

    /**
     * コンストラクタ
     * @param e 例外
     */
    public XPrintException(Exception e) {
        super(e);
    }
}
