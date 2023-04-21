package org.gcreator.pineapple.pinec.compiler.api;

import java.util.List;
import java.util.Vector;

/**
 *
 * @author Luís Reis
 */
public class APIClass {

    public String name = null;

    public List<String> base = new Vector<String>();

    public List<APIConstructor> constructors = new Vector<APIConstructor>();

    public List<APIField> fields = new Vector<APIField>();

    public boolean internal = false;

    @Override
    public String toString() {
        final StringBuilder strBuild = new StringBuilder("class ");
        strBuild.append(name);
        if (internal) {
            strBuild.append(" /*internal*/");
        } else {
            strBuild.append(" /*user-defined*/");
        }
        boolean first = true;
        for (String c : base) {
            if (first) {
                strBuild.append(" : ");
                first = false;
            } else {
                strBuild.append(", ");
            }
            strBuild.append(c);
        }
        strBuild.append("{\n");
        for (APIField field : fields) {
            strBuild.append("\t");
            strBuild.append(field.toString());
            strBuild.append("\n");
        }
        strBuild.append("}");
        return strBuild.toString();
    }
}
