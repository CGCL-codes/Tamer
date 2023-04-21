package net.sourceforge.mededis.dataaccess;

import net.sourceforge.mededis.central.MDException;
import java.util.List;

/**
 *    Access to fields of for a given root class.  Fields define the
 *    hierarchy of FieldDefinitions available for a particular class.
 */
public interface FieldAccess extends BasicAccess {

    public List findByRootClass(String className) throws MDException;
}
