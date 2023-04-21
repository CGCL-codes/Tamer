package org.sourceforge.zlang.ui;

import org.sourceforge.zlang.model.ZBreak;
import org.sourceforge.zlang.model.ZComment;
import org.sourceforge.zlang.model.ZElement;
import org.sourceforge.zlang.model.ZStatementContainer;

/**
 * TODO: add comment
 */
public class AddCommentAction extends NodeAction {

    /**
     * Constructor
     * 
     * @param tree Zlang tree
     */
    public AddCommentAction(ZlangTree tree) {
        super("Add comment", tree);
    }

    /**
     * TODO: add comment
     */
    public void update(ZElement el) {
    }

    /**
     * TODO: add comment
     */
    public void actionPerformed(ZElement el) {
        ZStatementContainer sc = (ZStatementContainer) el;
        sc.addStatement(new ZComment(sc, "TODO: change comment"));
    }
}
