package de.fraunhofer.ipsi.xquery.tree;

import de.fraunhofer.ipsi.xquery.errors.XQueryException;
import de.fraunhofer.ipsi.xquery.context.StaticContext;
import de.fraunhofer.ipsi.xquery.enums.NamespaceEnum;
import de.fraunhofer.ipsi.xquery.errors.DynamicError;
import de.fraunhofer.ipsi.xquery.errors.StaticError;
import de.fraunhofer.ipsi.xquery.tree.XQueryNodeAbstract;
import de.fraunhofer.ipsi.xquery.util.PositionInfo;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import org.apache.xerces.util.XMLChar;

public class XQueryQNameExpr extends XQueryNodeAbstract {

    private static final int DEFAULT = 0;

    private static final int ANY = 1;

    private static final int ANYNS = 2;

    private static final int ANYLOCAL = 3;

    private final String prefix;

    private final String localPart;

    private final int kind;

    /**
	 * Constructor
	 *
	 * @param    s                   a  String
	 *
	 */
    public XQueryQNameExpr(String s, PositionInfo pos) {
        super(pos);
        int index = s.indexOf(":");
        if (index != -1) {
            prefix = s.substring(0, index);
            localPart = s.substring(index + 1);
        } else {
            prefix = XMLConstants.DEFAULT_NS_PREFIX;
            localPart = s;
        }
        if (localPart.equals("*")) {
            if (prefix == XMLConstants.DEFAULT_NS_PREFIX) {
                kind = ANY;
            } else {
                kind = ANYLOCAL;
            }
        } else if (prefix.equals("*")) {
            kind = ANYNS;
        } else {
            kind = DEFAULT;
        }
    }

    /**
	 * Method getPrefix
	 *
	 * @return   a String
	 *
	 */
    public String getPrefix() {
        return prefix;
    }

    /**
	 * Method getLocalName
	 *
	 * @return   a String
	 *
	 */
    public String getLocalPart() {
        return localPart;
    }

    /**
	 * Method isAny
	 *
	 * @return   a boolean
	 *
	 */
    public boolean isAny() {
        return kind == ANY;
    }

    /**
	 * Method isAnyNamespace
	 *
	 * @return   a boolean
	 *
	 */
    public boolean isAnyNamespace() {
        return kind == ANYNS;
    }

    /**
	 * Method isAnyLocalName
	 *
	 * @return   a boolean
	 *
	 */
    public boolean isAnyLocalName() {
        return kind == ANYLOCAL;
    }

    /**
	 * Method expand
	 *
	 * @param    context             a  StaticContext
	 * @param    source              a  NamespaceEnum
	 *
	 * @return   a QName
	 *
	 */
    public QName expand(StaticContext context, NamespaceEnum source) throws XQueryException {
        QName result = null;
        String pre = prefix;
        if (prefix.equals("xdt")) pre = "xs";
        String uri = XMLConstants.NULL_NS_URI;
        if (pre != XMLConstants.DEFAULT_NS_PREFIX) {
            if (!context.statically_known_namespaces.contains(pre) || context.statically_known_namespaces.lookup(pre) == XMLConstants.NULL_NS_URI) {
                throw new XQueryException(getPosition(), StaticError.PREFIX_NOT_DECLARED);
            }
            uri = context.statically_known_namespaces.lookup(pre);
        } else if (source == NamespaceEnum.element) {
            uri = context.default_elem_namespace;
        } else if (source == NamespaceEnum.function) {
            uri = context.default_function_namespace;
        }
        if (!XMLChar.isValidNCName(localPart)) throw new XQueryException(getPosition(), DynamicError.CANNOT_CONVERT_TO_QNAME);
        result = new QName(uri, localPart, pre);
        return result;
    }

    /**
	 * Method matches
	 *
	 * @param    context             a  StaticContext
	 * @param    name                a  QName
	 *
	 * @return   a boolean
	 *
	 */
    public boolean matches(StaticContext statContext, NamespaceEnum source, QName name) throws XQueryException {
        boolean result = false;
        if (this.isAny()) {
            result = true;
        } else if (this.isAnyLocalName()) {
            if (!statContext.statically_known_namespaces.contains(prefix)) {
                throw new XQueryException(getPosition(), StaticError.PREFIX_NOT_FOUND);
            } else if (name.getNamespaceURI().equals(statContext.statically_known_namespaces.lookup(this.getPrefix()))) {
                result = true;
            }
        } else if (name.getLocalPart().equals(this.getLocalPart())) {
            if (this.isAnyNamespace() || name.equals(this.expand(statContext, source))) {
                result = true;
            }
        }
        return result;
    }

    /**
	 * Method hashCode
	 *
	 * @return   an int
	 *
	 */
    public int hashCode() {
        if (hashCode == -1) {
            hashCode = this.toString().hashCode();
        }
        return hashCode;
    }

    private int hashCode = -1;

    /**
	 * Method toString
	 *
	 * @return   a String
	 *
	 */
    public String toString() {
        if (prefix != XMLConstants.DEFAULT_NS_PREFIX) {
            return prefix + ":" + localPart;
        } else {
            return localPart;
        }
    }
}
