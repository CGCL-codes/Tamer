package tudresden.ocl20.core.jmi.uml15.impl.commonbehavior;

import tudresden.ocl20.core.jmi.uml15.impl.core.ModelElementImpl;
import tudresden.ocl20.core.jmi.uml15.core.*;
import tudresden.ocl20.core.jmi.uml15.commonbehavior.*;
import org.netbeans.mdr.handlers.InstanceHandler;
import org.netbeans.mdr.storagemodel.StorableObject;
import java.util.*;

public abstract class SignalImpl extends tudresden.ocl20.core.jmi.uml15.impl.core.ClassifierImpl implements Signal {

    /** Creates a new instance of SignalImpl */
    public SignalImpl(StorableObject storable) {
        super(storable);
    }

    public boolean hasMatchingSignature(java.util.List paramTypes) {
        int pos = 0;
        Iterator featuresIt = getFeature().iterator();
        while (featuresIt.hasNext()) {
            Feature feature = (Feature) featuresIt.next();
            if (feature instanceof Attribute) {
                if (paramTypes == null || pos >= paramTypes.size()) {
                    return false;
                }
                if (!((Classifier) paramTypes.get(pos)).conformsTo(((Attribute) feature).getType())) {
                    return false;
                }
                pos++;
            }
        }
        if (paramTypes != null && pos < paramTypes.size()) {
            return false;
        }
        return true;
    }
}
