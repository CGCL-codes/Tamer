package edu.upmc.opi.caBIG.caTIES.client.vr.utils.glazedlists;

public class ListSizeFunction implements ca.odell.glazedlists.FunctionList.Function {

    public Object evaluate(Object x0) {
        java.util.List sourceValue = (java.util.List) x0;
        return new Integer(sourceValue.size());
    }

    public ListSizeFunction() {
    }
}
