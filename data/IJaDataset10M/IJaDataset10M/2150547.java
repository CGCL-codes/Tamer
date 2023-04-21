package org.terukusu.ahoomsgr;

/**
 * YMSGプロトコルレイヤでの例外を表すクラスです。
 * 
 * @author Teruhiko Kusunoki&lt;<a href="teru.kusu@gmail.com">teru.kusu@gmail.com</a>&gt;
 *
 */
public class YmsgException extends Exception {

    /**
	 * serialVersionUID
	 */
    private static final long serialVersionUID = 2269814439789979099L;

    /**
	 * 新しいオブジェクトを生成します。
	 */
    public YmsgException() {
        super();
    }

    /**
	 * @param message
	 * @param cause
	 */
    public YmsgException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
	 * @param message
	 */
    public YmsgException(String message) {
        super(message);
    }

    /**
	 * @param cause
	 */
    public YmsgException(Throwable cause) {
        super(cause);
    }
}
