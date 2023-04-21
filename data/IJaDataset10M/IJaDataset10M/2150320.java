package org.jdmp.jetty.html.elements;

import org.jdmp.core.dataset.DataSet;
import org.jdmp.core.variable.Variable;
import org.jdmp.jetty.html.tags.DivTag;
import org.jdmp.jetty.html.tags.PTag;

public class ResultDiv extends DivTag {

    private static final long serialVersionUID = -8988055309556601716L;

    public ResultDiv(DataSet dataSet, int start, int count) {
        setParameter("class", "result");
        PTag p = new PTag();
        if (dataSet == null || dataSet.getSamples().isEmpty()) {
            p.add("no results found.");
        } else {
            p.add("Results " + (start + 1) + " - " + (start + count) + " of " + dataSet.getVariables().getAsInt(Variable.TOTAL));
        }
        add(p);
    }
}
