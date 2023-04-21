package org.jactr.tools.async.message.ast;

import java.io.Serializable;
import org.antlr.runtime.tree.CommonTree;

/**
 * @author developer
 *
 */
public interface IASTMessage extends Serializable {

    public CommonTree getAST();
}
