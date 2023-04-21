package openifctools.com.openifcjavatoolbox.ifc2x3tc1;

/**
 * This is a default implementation of the IFC enumeration type IfcAirToAirHeatRecoveryTypeEnum<br><br>
 * 
 * Copyright: CCPL BY-NC-SA 3.0 (cc) 2008 Eike Tauscher, Jan Tulke <br><br>
 * The OPEN JAVA TOOLBOX package is licensed under <br> <a rel="license"
 * href="http://creativecommons.org/licenses/by-nc-sa/3.0/de/">Creative Commons Attribution-Non-Commercial-Share Alike
 * 3.0 Germany</a>.<br>
 * Please visit <a href="http://www.openifctools.com">http://www.openifctools.com</a> for more information.<br>
 * 
 * <br>
 * If you use this package, please send a message to one of the authors<br>
 * <a href="mailto:eike.tauscher@openifctools.com">eike.tauscher@openifctools.com</a><br>
 * <a href="mailto:jan.tulke@openifctools.com">jan.tulke@openifctools.com</a><br><br>
 **/
public class IfcAirToAirHeatRecoveryTypeEnum extends ENUM implements IfcType {

    private static final long serialVersionUID = 8;

    /**
	 * The default constructor for the enumeration object IfcAirToAirHeatRecoveryTypeEnum.
	 **/
    public IfcAirToAirHeatRecoveryTypeEnum() {
    }

    /**
	 * Constructs a new IfcAirToAirHeatRecoveryTypeEnum enumeration object using the given parameter.
	 **/
    public IfcAirToAirHeatRecoveryTypeEnum(java.lang.String value) {
        this.value = IfcAirToAirHeatRecoveryTypeEnum_internal.valueOf(value);
    }

    public void setValue(Object value) {
        this.value = (IfcAirToAirHeatRecoveryTypeEnum_internal) value;
    }

    /**
	 * This method clones the enumeration object (deep cloning).
	 * 
	 * @return the cloned object
	 **/
    public Object clone() {
        IfcAirToAirHeatRecoveryTypeEnum fcAirToAirHeatRecoveryTypeEnum = new IfcAirToAirHeatRecoveryTypeEnum();
        fcAirToAirHeatRecoveryTypeEnum.setValue(this.value);
        return fcAirToAirHeatRecoveryTypeEnum;
    }

    public enum IfcAirToAirHeatRecoveryTypeEnum_internal {

        FIXEDPLATECOUNTERFLOWEXCHANGER, FIXEDPLATECROSSFLOWEXCHANGER, FIXEDPLATEPARALLELFLOWEXCHANGER, ROTARYWHEEL, RUNAROUNDCOILLOOP, HEATPIPE, TWINTOWERENTHALPYRECOVERYLOOPS, THERMOSIPHONSEALEDTUBEHEATEXCHANGERS, THERMOSIPHONCOILTYPEHEATEXCHANGERS, USERDEFINED, NOTDEFINED
    }

    /**
	 * This method is used internally and should NOT be used for own purposes.
	 **/
    public String getStepParameter(boolean isSelectType) {
        if (isSelectType) return new String("IFCAIRTOAIRHEATRECOVERYTYPEENUM(" + super.getStepParameter(false) + ")"); else return super.getStepParameter(false);
    }
}
