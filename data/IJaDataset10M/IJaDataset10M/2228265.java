package gov.nist.atlas.impl;

import gov.nist.atlas.ATLASClass;
import gov.nist.atlas.ATLASElement;
import gov.nist.atlas.Feature;
import gov.nist.atlas.Parameter;
import gov.nist.atlas.spi.ImplementationDelegate;
import gov.nist.atlas.type.ATLASType;
import gov.nist.atlas.type.FeatureType;
import gov.nist.atlas.util.ATLASElementSet;
import gov.nist.atlas.util.RoleIdentifiedFeature;
import gov.nist.atlas.util.RoleIdentifiedParameter;
import gov.nist.atlas.util.Visitor;

/**
 * @version $Revision: 1.34 $
 * @author Chris Laprun
 * @author Sylvain Pajot
 */
public class FeatureImpl extends ATLASElementImpl implements Feature {

    protected FeatureImpl(ATLASType type, ATLASElement parent, ImplementationDelegate delegate) {
        super(type, parent, delegate);
    }

    public final FeatureType getFeatureType() {
        return (FeatureType) getATLASType();
    }

    public final boolean isTypeValid(ATLASType type) {
        return super.isTypeValid(type) && (type instanceof FeatureType);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void initContainedElementsWith(RoleIdentifiedFeature[] features, RoleIdentifiedParameter[] parameters) {
        int numberOfElements, i;
        RoleIdentifiedParameter iteParameter;
        RoleIdentifiedFeature iteFeature;
        if (parameters != null) {
            numberOfElements = parameters.length;
            for (i = 0; i < numberOfElements; i++) {
                iteParameter = parameters[i];
                setSubordinateWithRole(iteParameter.getElement(), iteParameter.getRole());
            }
        }
        if (features != null) {
            numberOfElements = features.length;
            for (i = 0; i < numberOfElements; i++) {
                iteFeature = features[i];
                setSubordinateWithRole(iteFeature.getElement(), iteFeature.getRole());
            }
        }
    }

    public ATLASClass getATLASClass() {
        return ATLASClass.FEATURE;
    }

    public Feature getFeatureWithRole(String role) {
        return (Feature) getSubordinateWithRole(role);
    }

    public ATLASElementSet getAllFeatures() {
        return getAllChildrenWith(ATLASClass.FEATURE);
    }

    public boolean setFeatureWithRole(Feature feature, String role) {
        return setSubordinateWithRole(feature, role);
    }

    public boolean setValueOfParameterWithRole(String value, String role) {
        Parameter tempParameter = (Parameter) getSubordinateWithRole(role);
        if (tempParameter != null) {
            return tempParameter.setValue(value);
        }
        return false;
    }

    public String getStringValueOfParameterWithRole(String role) {
        return getParameterWithRole(role).getValueAsString();
    }

    public Object getObjectValueOfParameterWithRole(String role) {
        return getParameterWithRole(role).getValueAsObject();
    }

    public ATLASElementSet getAllParameters() {
        return getAllChildrenWith(ATLASClass.PARAMETER);
    }

    public Parameter getParameterWithRole(String role) {
        return (Parameter) getSubordinateWithRole(role);
    }
}
