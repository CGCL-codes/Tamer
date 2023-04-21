package org.jactr.io.antlr3.misc;

import java.net.URL;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DetailedCommonErrorNode extends DetailedCommonTree {

    /**
   * Logger definition
   */
    private static final transient Log LOGGER = LogFactory.getLog(DetailedCommonErrorNode.class);

    public DetailedCommonErrorNode(CommonTree arg0, URL source) {
        super(arg0, source);
    }

    public DetailedCommonErrorNode(Token arg0, URL source) {
        super(arg0, source);
    }
}
