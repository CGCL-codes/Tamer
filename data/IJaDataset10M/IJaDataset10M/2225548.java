package org.dyno.visual.swing.types.endec;

import java.awt.Insets;
import org.dyno.visual.swing.plugin.spi.ICodeGen;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

/**
 * 
 * @author William Chen
 */
public class InsetsWrapper implements ICodeGen {

    public String getJavaCode(Object value, ImportRewrite imports) {
        if (value == null) return "null";
        Insets insets = (Insets) value;
        String str = imports.addImport("java.awt.Insets");
        return "new " + str + "(" + insets.top + ", " + insets.left + ", " + insets.bottom + ", " + insets.right + ")";
    }

    public String getInitJavaCode(Object value, ImportRewrite imports) {
        return null;
    }
}
