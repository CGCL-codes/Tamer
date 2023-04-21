package common;

import com.metanology.mde.core.metaModel.*;

public class StereotypeProvider implements IStereotypes {

    /**
	 * Based on the given meta-object returns a list of 
	 * stereotype
	 * The list will be picked up by the modeler to pre-populate 
	 * the stereotype combo box for any metaobject.
	 * 
	 * @param mobj - the meta object instance can be (Package|MetaClass|Attribute|Operation|Subsystem|Component)
	 * 
	 * @see com.metanology.mde.core.metaModel.IStereotypes#getStereotypes(com.metanology.mde.core.metaModel.MetaObject)
	 */
    public String[] getStereotypes(MetaObject mobj) {
        return new String[] { "Form", "Page", "DAO", "root" };
    }
}
