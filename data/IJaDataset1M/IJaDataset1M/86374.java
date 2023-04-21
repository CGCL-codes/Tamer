package cx.ath.contribs.internal.xerces.impl.dv.dtd;

import java.util.Enumeration;
import java.util.Hashtable;
import cx.ath.contribs.internal.xerces.impl.dv.DatatypeValidator;

/**
 * the factory to create/return built-in XML 1.1 DVs and create user-defined DVs
 * 
 * @xerces.internal  
 *
 * @author Neil Graham, IBM
 *
 * @version $Id: XML11DTDDVFactoryImpl.java,v 1.2 2007/07/13 07:23:28 paul Exp $
 */
public class XML11DTDDVFactoryImpl extends DTDDVFactoryImpl {

    static Hashtable fXML11BuiltInTypes = new Hashtable();

    /**
     * return a dtd type of the given name
     * This will call the super class if and only if it does not
     * recognize the passed-in name.  
     *
     * @param name  the name of the datatype
     * @return      the datatype validator of the given name
     */
    public DatatypeValidator getBuiltInDV(String name) {
        if (fXML11BuiltInTypes.get(name) != null) {
            return (DatatypeValidator) fXML11BuiltInTypes.get(name);
        }
        return (DatatypeValidator) fBuiltInTypes.get(name);
    }

    /**
     * get all built-in DVs, which are stored in a hashtable keyed by the name
     * New XML 1.1 datatypes are inserted.
     *
     * @return      a hashtable which contains all datatypes
     */
    public Hashtable getBuiltInTypes() {
        Hashtable toReturn = (Hashtable) fBuiltInTypes.clone();
        Enumeration xml11Keys = fXML11BuiltInTypes.keys();
        while (xml11Keys.hasMoreElements()) {
            Object key = xml11Keys.nextElement();
            toReturn.put(key, fXML11BuiltInTypes.get(key));
        }
        return toReturn;
    }

    static {
        fXML11BuiltInTypes.put("XML11ID", new XML11IDDatatypeValidator());
        DatatypeValidator dvTemp = new XML11IDREFDatatypeValidator();
        fXML11BuiltInTypes.put("XML11IDREF", dvTemp);
        fXML11BuiltInTypes.put("XML11IDREFS", new ListDatatypeValidator(dvTemp));
        dvTemp = new XML11NMTOKENDatatypeValidator();
        fXML11BuiltInTypes.put("XML11NMTOKEN", dvTemp);
        fXML11BuiltInTypes.put("XML11NMTOKENS", new ListDatatypeValidator(dvTemp));
    }
}
