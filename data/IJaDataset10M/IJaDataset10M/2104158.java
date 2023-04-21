package de.fraunhofer.ipsi.xquery.tree.prolog;

import de.fraunhofer.ipsi.xquery.errors.XQueryException;
import de.fraunhofer.ipsi.xquery.tree.VisitorXQueryModulesAndProlog;
import de.fraunhofer.ipsi.xquery.tree.XQueryNodeAbstract;
import de.fraunhofer.ipsi.xquery.util.PositionInfo;

public class XQueryModuleDecl extends XQueryNodeAbstract implements XQueryPrologDecl {

    private final String name;

    private final String targetNamespace;

    /**
	 * Constructor.
	 */
    public XQueryModuleDecl(PositionInfo pos, String name, String targetNamespace) {
        super(pos);
        this.name = name;
        this.targetNamespace = targetNamespace;
    }

    /**
	 * Method getName
	 *
	 * @return XS_NCName
	 */
    public String getName() {
        return name;
    }

    /**
	 * Method getTargetNamespace
	 *
	 * @return   a String
	 *
	 */
    public String getTargetNamespace() {
        return targetNamespace;
    }

    /**
	 * Method acceptVisitor
	 *
	 * @param    visitor             a  VisitorXQuery
	 *
	 * @throws   QueryException
	 *
	 */
    public void acceptVisitor(VisitorXQueryModulesAndProlog visitor) throws XQueryException {
        visitor.process(this);
    }
}
