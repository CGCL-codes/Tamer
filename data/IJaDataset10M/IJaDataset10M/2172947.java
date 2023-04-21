package org.incava.doctorj;

import java.util.Iterator;
import java.util.List;
import org.incava.analysis.Report;
import org.incava.java.*;
import org.incava.log.Log;

/**
 * Analyzes Javadoc and code for methods and constructors, AKA functions.
 */
public abstract class FunctionDocAnalyzer extends ItemDocAnalyzer {

    public static final String MSG_SERIALDATA_WITHOUT_DESCRIPTION = "@serialData without description";

    private SimpleNode node;

    public FunctionDocAnalyzer(Report r, SimpleNode node) {
        super(r, node);
        this.node = node;
    }

    protected void checkJavadoc(JavadocNode javadoc) {
        super.checkJavadoc(javadoc);
        ExceptionDocAnalyzer eda = new ExceptionDocAnalyzer(getReport(), javadoc, getNode());
        eda.run();
        ASTFormalParameters params = getParameterList();
        ParameterDocAnalyzer pda = new ParameterDocAnalyzer(getReport(), javadoc, getNode(), params);
        pda.run();
        if (javadoc != null && isCheckable(node, CHKLVL_TAG_CONTENT)) {
            JavadocTaggedNode[] taggedComments = javadoc.getTaggedComments();
            for (int ti = 0; ti < taggedComments.length; ++ti) {
                JavadocTag tag = taggedComments[ti].getTag();
                Log.log("checking tag: " + tag);
                if (tag.text.equals(JavadocTags.SERIALDATA)) {
                    checkForTagDescription(taggedComments[ti], MSG_SERIALDATA_WITHOUT_DESCRIPTION);
                }
            }
        }
    }

    /**
     * Returns the parameter list for the function.
     */
    protected abstract ASTFormalParameters getParameterList();
}
