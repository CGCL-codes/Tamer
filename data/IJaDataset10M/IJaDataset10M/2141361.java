package openifctools.com.openifcjavatoolbox.ifc2x3tc1;

/**
 * This is a default implementation of the IFC object IfcMolecularWeightMeasure<br><br>
 * 
 * Copyright: CCPL BY-NC-SA 3.0 (cc) 2008 Eike Tauscher, Jan Tulke <br><br>
 * The OPEN IFC JAVA TOOLBOX package is licensed under <br> <a rel="license"
 * href="http://creativecommons.org/licenses/by-nc-sa/3.0/de/">Creative Commons Attribution-Non-Commercial-Share Alike
 * 3.0 Germany</a>.<br>
 * Please visit <a href="http://www.openifctools.com">http://www.openifctools.com</a> for more information.<br>
 * 
 * <br>
 * If you use this package, please send a message to one of the authors<br>
 * <a href="mailto:eike.tauscher@openifctools.com">eike.tauscher@openifctools.com</a><br>
 * <a href="mailto:jan.tulke@openifctools.com">jan.tulke@openifctools.com</a><br><br>
 **/
public class IfcMolecularWeightMeasure extends DOUBLE implements IfcType, IfcDerivedMeasureValue {

    private static final long serialVersionUID = 8;

    /**
	 * The default constructor for the type object IfcMolecularWeightMeasure.
	 **/
    public IfcMolecularWeightMeasure() {
        super();
    }

    /**
	 * Constructs a new IfcMolecularWeightMeasure type object using the given parameter.
	 **/
    public IfcMolecularWeightMeasure(DOUBLE value) {
        this.setValue(value);
    }

    public IfcMolecularWeightMeasure(double value) {
        this.setValue(value);
    }

    /**
	 * This method sets the value of this type object.
	 * 
	 * @param value value to set
	 **/
    public void setValue(Object value) {
        super.setValue((DOUBLE) value);
    }

    /**
	 * This method clones the object (deep cloning).
	 * 
	 * @return the cloned object
	 **/
    public Object clone() {
        IfcMolecularWeightMeasure fcMolecularWeightMeasure = new IfcMolecularWeightMeasure();
        fcMolecularWeightMeasure.setValue(super.clone());
        return fcMolecularWeightMeasure;
    }

    /**
	 * This method is used internally and should NOT be used for own purposes.
	 **/
    public String getStepParameter(boolean isSelectType) {
        if (isSelectType) return new String("IFCMOLECULARWEIGHTMEASURE(" + super.getStepParameter(false) + ")"); else return super.getStepParameter(false);
    }
}
