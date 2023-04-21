package com.once.servicescout.matching.qos;

import com.once.servicescout.parser.query.QoSQueryRequest;
import java.util.Map;

/**
 *
 * @author cwchen
 * @Created 2008-1-18
 * @Contact comain@gmail.com
 */
public interface QoSMatch {

    /**
     * 
     * @param req
     * @return a map of matched service ids and corresponding scores.
     */
    Map<String, Float> query(QoSQueryRequest req);
}
