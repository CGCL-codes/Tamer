package net.sf.javareveng.reverse;

import java.io.Serializable;
import java.util.Comparator;
import org.eclipse.jdt.core.IType;

class ITypeComparator implements Comparator<IType>, Serializable {

    /**
	 * Serial Version UID
	 * 
	 * @see Serializable
	 */
    private static final long serialVersionUID = 7005208883679598106L;

    public int compare(IType o1, IType o2) {
        return o1.getElementName().compareTo(o2.getElementName());
    }
}
