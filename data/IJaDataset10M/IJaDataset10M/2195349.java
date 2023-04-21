package org.incava.doctorj;

import java.util.List;
import org.incava.analysis.Report;
import org.incava.java.*;
import org.incava.log.Log;

/**
 * Analyzes Javadoc and code for fields.
 */
public class FieldDocAnalyzer extends ItemDocAnalyzer {

    public static final String MSG_SERIALFIELD_WITHOUT_NAME_TYPE_AND_DESCRIPTION = "@serialField without field name, type, and description.";

    public static final String MSG_SERIALFIELD_WITHOUT_TYPE_AND_DESCRIPTION = "@serialField without field type and description.";

    public static final String MSG_SERIALFIELD_WITHOUT_DESCRIPTION = "@serialField without description.";

    private ASTFieldDeclaration field;

    public FieldDocAnalyzer(Report r, ASTFieldDeclaration field) {
        super(r, field);
        this.field = field;
    }

    public String getItemType() {
        return "field";
    }

    protected void checkJavadoc(JavadocNode javadoc) {
        super.checkJavadoc(javadoc);
        if (javadoc != null && isCheckable(field, CHKLVL_TAG_CONTENT)) {
            JavadocTaggedNode[] taggedComments = javadoc.getTaggedComments();
            for (int ti = 0; ti < taggedComments.length; ++ti) {
                JavadocTag tag = taggedComments[ti].getTag();
                Log.log("checking tag: " + tag);
                if (tag.text.equals(JavadocTags.SERIALFIELD)) {
                    JavadocElement desc = taggedComments[ti].getDescription();
                    if (desc == null) {
                        addViolation(MSG_SERIALFIELD_WITHOUT_NAME_TYPE_AND_DESCRIPTION, tag.start, tag.end);
                    } else {
                        JavadocElement nontgt = taggedComments[ti].getDescriptionNonTarget();
                        if (nontgt == null) {
                            addViolation(MSG_SERIALFIELD_WITHOUT_TYPE_AND_DESCRIPTION, desc.start, desc.end);
                        } else {
                            String text = nontgt.text;
                            int pos = 0;
                            int len = text.length();
                            boolean gotAnotherWord = false;
                            while (!gotAnotherWord && pos < len) {
                                if (Character.isWhitespace(text.charAt(pos))) {
                                    gotAnotherWord = true;
                                }
                                ++pos;
                            }
                            if (!gotAnotherWord) {
                                addViolation(MSG_SERIALFIELD_WITHOUT_DESCRIPTION, desc.start, desc.end);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns the valid tags, as strings, for fields.
     */
    protected List getValidTags() {
        return JavadocTags.getValidFieldTags();
    }

    /**
     * Adds a violation for a field, with the violation pointing to the field
     * name.
     */
    protected void addUndocumentedViolation(String desc) {
        int nChildren = field.jjtGetNumChildren();
        Log.log("nChildren: " + nChildren);
        SimpleNode declarator = (SimpleNode) field.jjtGetChild(1);
        SimpleNode vdid = (SimpleNode) declarator.getChildren().get(0);
        Log.log("vdid: " + vdid);
        Token begin = vdid.getFirstToken();
        declarator = (SimpleNode) field.jjtGetChild(nChildren - 1);
        vdid = (SimpleNode) declarator.getChildren().get(0);
        Token end = vdid.getFirstToken();
        Log.log("begin: " + begin);
        Log.log("end  : " + end);
        addViolation(desc, begin, end);
    }
}
