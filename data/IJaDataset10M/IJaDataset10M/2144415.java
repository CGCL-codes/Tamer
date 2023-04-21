package com.ibm.wala.dila.data.results.cg;

import com.ibm.wala.dila.data.graphs.IEdgedGraph;
import com.ibm.wala.dila.data.graphs.cg.INode;
import com.ibm.wala.dila.data.java.IType;
import com.ibm.wala.dila.data.results.IDataStructure;

/**
 *
 * @author Jan Wloka
 * @version $Id: ICallGraph.java,v 1.2 2008/10/08 21:21:02 jwloka Exp $
 */
public interface ICallGraph extends IEdgedGraph<INode>, IDataStructure {

    public static final String COUNT_LABEL = "#";

    public static final String KIND_SEPARATOR = "_;_";

    public static final String ELEMENT_SEPARATOR = "_,_";

    public abstract String getMethodRunName();

    public boolean hasEdge(INode source, String label, INode target);
}
