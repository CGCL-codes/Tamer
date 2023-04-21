package net.sourceforge.mipa.predicatedetection;

import static config.Debug.DEBUG;
import org.w3c.dom.Document;

/**
 * Checker parser module.
 * 
 * @author Jianping Yu <jianp.yue@gmail.com>
 */
public class CheckerParser {

    /**
     * parse checker logic from <code>Document</code>.
     * 
     * @param predicate
     *            a document
     * @param callbackID
     *            a String represented application which is waiting for checker
     *            result
     */
    public void parseChecker(Document predicate, String callbackID, PredicateType type) {
        if (DEBUG) {
            System.out.println("\tparsing checker logic...");
        }
    }
}
