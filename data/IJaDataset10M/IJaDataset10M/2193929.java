package jade;

/**
   
   This class defines the necessary information for the configuration properties of JADE.
   For every property some information are necessary to show the correctly:
   the name, the type (integer, boolean or string), the default Value, ,
   the string to show as tooltip in the gui at start up and a
   boolean to indicate if the property is mandatory or not.
      
   @author Tiziana Trucco - CSELT S.p.A.
   @author Dominic Greenwood - Whitestein Technologies AG
   @version $Date: 2010-04-08 15:54:18 +0200 (gio, 08 apr 2010) $ $Revision: 6298 $

 */
public class PropertyType {

    public static String INTEGER_TYPE = "int";

    public static String BOOLEAN_TYPE = "boolean";

    public static String STRING_TYPE = "string";

    public static String COMBO_TYPE = "combo";

    String name;

    String type;

    String defaultValue;

    String[] fixedEnum;

    String toolTip;

    boolean mandatory;

    /**
 	 Public constructor for a generic property.
   
  */
    public PropertyType() {
        this.name = null;
        this.type = null;
        this.defaultValue = null;
        this.fixedEnum = null;
        this.toolTip = null;
        this.mandatory = false;
    }

    /**
  Public constructor for a property. 
  @param name Name of the property
  @param type A string representing the type of the property
  @param def Default value of the property
  @param desc Description of the property
  @param mandatory true if the property is mandatory, false otherwise. 
  */
    public PropertyType(String name, String type, String def, String desc, boolean mand) {
        this.name = name;
        this.type = type;
        this.defaultValue = def;
        this.toolTip = desc;
        this.mandatory = mand;
    }

    /**
  Public constructor for a ComboBox specific property. 
  @param type A string representing the type of the property
  @param en A list of the allowed values for the combobox
  @param def Default value of the property
  @param desc Description of the property
  @param mandatory true if the property is mandatory, false otherwise. 
  */
    public PropertyType(String name, String type, String[] en, String def, String desc, boolean mand) {
        this.name = name;
        this.type = type;
        this.fixedEnum = en;
        this.defaultValue = def;
        this.toolTip = desc;
        this.mandatory = mand;
    }

    /**
 	This method returns a string representing the type of the property.
 	*/
    public String getType() {
        return this.type;
    }

    /**
 	Returns the name of the property.
 	*/
    public String getName() {
        return this.name;
    }

    /**
 	Returns the default value of the property.
 	*/
    public String getDefaultValue() {
        return this.defaultValue;
    }

    /**
 	Returns the combo values for the property.
 	*/
    public String[] getComboValues() {
        return this.fixedEnum;
    }

    public String getToolTip() {
        return this.toolTip;
    }

    /**
 	Returns a boolean to indicate if the property is mandatory or not.
 	*/
    public boolean isMandatory() {
        return this.mandatory;
    }

    public void setName(String value) {
        this.name = value;
    }

    public void setDefaultValue(String value) {
        this.defaultValue = value;
    }
}
