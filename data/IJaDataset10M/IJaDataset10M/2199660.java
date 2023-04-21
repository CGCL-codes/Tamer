package openifctools.com.openifcjavatoolbox.ifc2x3tc1;

/**
 * This is a default implementation of the IFC enumeration type IfcMemberTypeEnum<br><br>
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
public class IfcMemberTypeEnum extends ENUM implements IfcType {

    private static final long serialVersionUID = 8;

    /**
	 * The default constructor for the enumeration object IfcMemberTypeEnum.
	 **/
    public IfcMemberTypeEnum() {
    }

    /**
	 * Constructs a new IfcMemberTypeEnum enumeration object using the given parameter.
	 **/
    public IfcMemberTypeEnum(java.lang.String value) {
        this.value = IfcMemberTypeEnum_internal.valueOf(value);
    }

    public void setValue(Object value) {
        this.value = (IfcMemberTypeEnum_internal) value;
    }

    /**
	 * This method clones the enumeration object (deep cloning).
	 * 
	 * @return the cloned object
	 **/
    public Object clone() {
        IfcMemberTypeEnum fcMemberTypeEnum = new IfcMemberTypeEnum();
        fcMemberTypeEnum.setValue(this.value);
        return fcMemberTypeEnum;
    }

    public enum IfcMemberTypeEnum_internal {

        BRACE, CHORD, COLLAR, MEMBER, MULLION, PLATE, POST, PURLIN, RAFTER, STRINGER, STRUT, STUD, USERDEFINED, NOTDEFINED
    }

    /**
	 * This method is used internally and should NOT be used for own purposes.
	 **/
    public String getStepParameter(boolean isSelectType) {
        if (isSelectType) return new String("IFCMEMBERTYPEENUM(" + super.getStepParameter(false) + ")"); else return super.getStepParameter(false);
    }
}
