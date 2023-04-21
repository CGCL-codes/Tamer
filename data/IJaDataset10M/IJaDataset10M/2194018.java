package org.adapit.wctoolkit.uml.ext.core;

import java.io.Serializable;
import java.util.Iterator;
import org.adapit.wctoolkit.models.util.ObserverMutableTreeNode;
import org.adapit.wctoolkit.models.util.IParser;
import org.adapit.wctoolkit.models.util.ITreeDisplayable;
import org.adapit.wctoolkit.uml.classes.kernel.PrimitiveType;

@SuppressWarnings({ "serial", "unchecked" })
public class ProgrammingLanguageDataType extends PrimitiveType implements Serializable {

    private TypeExpression expression;

    /**
	 * 
	 */
    public ProgrammingLanguageDataType(IElement parent) {
        super(parent);
        setIcon("icons//package.gif");
    }

    /**
	 * @return Returns the expression.
	 */
    public TypeExpression getExpression() {
        return expression;
    }

    /**
	 * @param expression The expression to set.
	 */
    public void setExpression(TypeExpression expression) {
        this.expression = expression;
    }

    public String exportXMILight(int tab) throws Exception {
        String str = "" + '\n';
        for (int i = 1; i <= tab; i++) str += '\t';
        str += "<ProgrammingLanguageDataType id=\"" + getId() + "\" name=\"" + getName() + "\">";
        Iterator it = getElements().values().iterator();
        while (it.hasNext()) {
            IParser p = (IParser) it.next();
            str += p.exportXMILight(tab + 1);
        }
        str += '\n';
        for (int i = 1; i <= tab; i++) str += '\t';
        str += "</ProgrammingLanguageDataType>";
        return str;
    }

    public void createNodes(ObserverMutableTreeNode root, ITreeDisplayable obj) {
        ObserverMutableTreeNode re = new ObserverMutableTreeNode(this);
        root.add(re);
        Iterator it = getElements().values().iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof IElement) {
                IElement e = (IElement) o;
                e.createNodes(re, null);
            }
        }
    }
}
