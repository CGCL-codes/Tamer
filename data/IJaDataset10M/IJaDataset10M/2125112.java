package com.goodcodeisbeautiful.opensearch.osrss10;

import com.goodcodeisbeautiful.archtea.util.ArchteaUtil;
import com.goodcodeisbeautiful.opensearch.OpenSearchException;

/**
 * @author hata
 *
 */
public class OpenSearchRssException extends OpenSearchException {

    /**
     * <code>serialVersionUID</code> comment.
     */
    private static final long serialVersionUID = 8590703047934699482L;

    /**
     * 
     */
    public OpenSearchRssException() {
        super();
    }

    /**
     * @param message
     */
    public OpenSearchRssException(String message) {
        super(message);
    }

    /**
     * @param messageKey
     * @param params is a parameters for messageKey.
     */
    public OpenSearchRssException(String messageKey, Object[] params) {
        super(ArchteaUtil.getString(messageKey, params));
    }

    /**
     * @param message
     * @param cause
     */
    public OpenSearchRssException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public OpenSearchRssException(Throwable cause) {
        super(cause);
    }

    /**
     * @param messageKey
     * @param params is a parameters for messageKey.
     * @param cause
     */
    public OpenSearchRssException(String messageKey, Object[] params, Throwable cause) {
        super(ArchteaUtil.getString(messageKey, params), cause);
    }
}
