package com.rapidminer.operator.preprocessing.ie.features.zhou.words;

import java.util.Iterator;
import java.util.List;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.preprocessing.ie.features.tools.ExampleIteration;
import com.rapidminer.operator.preprocessing.ie.features.tools.PreprocessOperatorImpl;
import com.rapidminer.operator.preprocessing.ie.features.tools.RelationPreprocessOperatorImpl;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeString;
import com.rapidminer.tools.OperatorService;
import edu.stanford.nlp.trees.Tree;

/**
 * This Preprocessing-Operator constructs the WBFL-feature for the RCD-task
 * respecting the paper of Zhou etal. 2005.
 * 
 * @author Felix Jungermann
 * @version $Id: WBNULLProcessing.java,v 1.2 2009-03-12 13:30:29 jungerma Exp $
 */
public class WBLProcessing extends RelationPreprocessOperatorImpl {

    public WBLProcessing(OperatorDescription description) {
        super(description);
        this.setParameter(PARAMETER_NAME, "WBL");
    }

    @Override
    protected String newValueToInsert(String w1, String w2, List<String> addAtts, int length, ExampleIteration exIter) {
        int pos1 = new Integer(w1).intValue();
        int pos2 = new Integer(w2).intValue();
        if (pos1 + 1 == pos2 || pos1 - 1 == pos2) {
            return "1";
        } else {
            return "-1";
        }
    }

    @Override
    protected String newValueToInsertTree(Tree t) {
        int pos1 = wbNullE1(t, "E1-", 0, true);
        int pos2 = wbNullE1(t, "E2-", 0, false);
        if (pos1 + 2 <= pos2) {
            return t.getLeaves().get(pos2 - 1).value();
        } else {
            return "null";
        }
    }

    /**
	 * delivers the end of E1 or the start of E2
	 * @param t
	 * @param e
	 * @param start
	 * @return
	 */
    private int wbNullE1(Tree t, String e, int start, boolean e1) {
        if (t.value().startsWith(e)) {
            if (e1) return start + t.getLeaves().size(); else return start;
        } else {
            if (t.isPreTerminal() || t.isLeaf()) {
                return -1;
            } else {
                int i = start;
                for (Tree c : t.getChildrenAsList()) {
                    int out = wbNullE1(c, e, i, e1);
                    if (out != -1) return out;
                    i += c.getLeaves().size();
                }
            }
        }
        return -1;
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> params = super.getParameterTypes();
        ParameterType t;
        Iterator<ParameterType> iter = params.iterator();
        while (iter.hasNext()) {
            t = iter.next();
            if (t.getKey().equals(PARAMETER_NAME)) {
                t = new ParameterTypeString(PreprocessOperatorImpl.PARAMETER_NAME, "WBL", false);
            }
        }
        return params;
    }

    @Override
    public RelationPreprocessOperatorImpl create() throws Exception {
        return (RelationPreprocessOperatorImpl) OperatorService.createOperator("WBLPreprocessing");
    }
}
