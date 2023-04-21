package commonapp.datacon;

import common.Utils;
import commonapp.datadef.ItemDef;
import commonapp.gui.GUIProps;
import commonapp.gui.MessageManager;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
   This class implements the {@link IntfUnitItemData} interface for Double
   valued data.  The primary use for this item is to represent numerical data.

   <p>Note that all IHSDM data is maintained internally in the Metric unit
   system, unlike SafetyAnalyst.
*/
public class DoubleItemData extends UnitItemData implements FormatSupport {

    private static final String CM_REQUIRED = "DoubleItemData.required";

    private static final String CM_INVALID = "DoubleItemData.invalid";

    private static final String CM_TOO_LO = "DoubleItemData.tooLo";

    private static final String CM_TOO_GT = "DoubleItemData.greater";

    private static final String CM_TOO_HI = "DoubleItemData.tooHi";

    private static final String CM_TOO_LT = "DoubleItemData.less";

    /** The number formatter. */
    private static NumberFormat ourNumberFormat = DecimalFormat.getInstance();

    /** The value of this object. */
    private Double myValue = null;

    /** Hard minimum limit value. */
    private Double myMinValue = null;

    /** Optional minimum value operator, default validation is 'GE'. */
    protected String myMinOpr = null;

    /** Soft minimum limit value. */
    private Double myMinWarn = null;

    private Double myMaxValue = null;

    /** Optional maximum value operator, default validation is 'LE'. */
    protected String myMaxOpr = null;

    /** Soft maximum limit value. */
    private Double myMaxWarn = null;

    private UnitSystem myFormatterSystem = null;

    private ItemDef myDefinition = null;

    private int myDecimals = -1;

    private boolean myItemIsNumEnum = false;

    /** Warning message, set in getWarningMessage. */
    private String myWarnMessage = null;

    /**
     Constructs a new DoubleItemData.
  */
    public DoubleItemData() {
    }

    /**
     Constructs a new DoubleItemData with the specified value.

     @param theValue the double value to be associated with this object.
     This data is represented in the {@link UnitSystem.Use#INTERNAL} system.
  */
    public DoubleItemData(double theValue) {
        if (Double.isNaN(theValue)) {
            myValue = null;
        } else {
            myValue = new Double(theValue);
        }
    }

    /**
     Returns the double value of the item.

     @return the double value of the item or Double.NaN if the item has no
     value.  This data is represented in the {@link UnitSystem.Use#INTERNAL}
     system.
  */
    public double doubleValue() {
        return (myValue == null) ? Double.NaN : myValue.doubleValue();
    }

    /**
     Returns the double minimum value of the item.

     @return the double minimum value of the item or Double.NaN if the item
     has no minimum.  This data is represented in the {@link
     UnitSystem.Use#INTERNAL} system.
  */
    public double minValue() {
        return (myMinValue == null) ? Double.NaN : myMinValue.doubleValue();
    }

    /**
     Returns the double maximum value of the item.

     @return the double maximum value of the item or Double.NaN if the item
     has no maximum.  This data is represented in the {@link
     UnitSystem.Use#INTERNAL} system.
  */
    public double maxValue() {
        return (myMaxValue == null) ? Double.NaN : myMaxValue.doubleValue();
    }

    /**
     public ItemFormatter getFormatter( UnitSystem theSystem )
     {
       myFormatterSystem = theSystem;

       //return new NumericFormatter( this, theSystem, DoubleItemData.class );
       return new javax.swing.text.NumberFormatter();
     }
  */
    public ItemFormatter getFormatter(UnitSystem theSystem) {
        myFormatterSystem = theSystem;
        return new NumericFormatter(this, theSystem, DoubleItemData.class);
    }

    /**
     Returns a warning message associated with the data.

     @return a warning message associated with the data or null for no
     applicable warnings.
  */
    @Override
    public String getWarningMessage() {
        if ((myWarnMessage == null) && isValid() && ((myMinWarn != null) || (myMaxWarn != null))) {
            if (myMinWarn != null) {
                if (myValue.doubleValue() < myMinWarn.doubleValue()) {
                    myWarnMessage = "specified value, " + messageString(myValue) + ", may be too low";
                }
            }
            if (myMaxWarn != null) {
                if (myValue.doubleValue() > myMaxWarn.doubleValue()) {
                    myWarnMessage = "specified value, " + messageString(myValue) + ", may be too high";
                }
            }
        }
        return myWarnMessage;
    }

    /**
     Sets a warning message to be associated with the data.

     @param theMessage the warning message.
  */
    @Override
    public void setWarningMessage(String theMessage) {
        myWarnMessage = theMessage;
    }

    /**
     Returns a formatting error message.  This method is required by the {@link
     FormatSupport} interface.

     @return a formatting error message or null if the data item is valid.
  */
    @Override
    public String getErrorMessage() {
        String message = null;
        if (!isValid()) {
            if (myMessageKey != null) {
                MessageManager.main.displayMessage(myMessageKey, myMessageArgs);
            }
            message = "invalid data";
        }
        return message;
    }

    /**
     Returns an index associated with the formatting error message.  This method
     is required by the {@link FormatSupport} interface.

     @return a formatting error message index.
  */
    public int getErrorIndex() {
        return 0;
    }

    /**
     Sets the data definition associated with this item.  This method is
     required by the {@link FormatSupport} interface.

     @param theDefinition the data definition for the item.
  */
    public void setDefinition(ItemDef theDefinition) {
        myDefinition = theDefinition;
        myMinValue = null;
        String limit = myDefinition.getAttr(ItemDef.A_MIN_VALUE);
        if (limit != null) {
            myMinValue = fromString(limit);
            String minOpr = myDefinition.getAttr(ItemDef.A_MIN_OPERATOR);
            if (minOpr != null) {
                if (!minOpr.isEmpty()) {
                    myMinOpr = minOpr;
                }
            }
        }
        limit = myDefinition.getAttr(ItemDef.A_MIN_WARN);
        if (limit != null) {
            myMinWarn = fromString(limit);
        }
        myMaxValue = null;
        limit = myDefinition.getAttr(ItemDef.A_MAX_VALUE);
        if (limit != null) {
            myMaxValue = fromString(limit);
            String maxOpr = myDefinition.getAttr(ItemDef.A_MAX_OPERATOR);
            if (maxOpr != null) {
                if (!maxOpr.isEmpty()) {
                    myMaxOpr = maxOpr;
                }
            }
        }
        limit = myDefinition.getAttr(ItemDef.A_MAX_WARN);
        if (limit != null) {
            myMaxWarn = fromString(limit);
        }
        String decimals = myDefinition.getAttr(ItemDef.A_DECIMAL_DIGITS);
        if (decimals != null) {
            decimals = GUIProps.main.replaceProperties(decimals);
            myDecimals = Utils.parseInt(decimals);
        }
        ItemDataType datatype = myDefinition.getDataType();
        if (datatype == ItemDataType.NUMENUM) {
            myItemIsNumEnum = true;
        }
    }

    /**
     Compares this object with the specified object for order.  Returns a
     negative integer, zero, or a positive integer as this object is less
     than, equal to, or greater than the specified object. This method is
     required by the {@link Comparable} interface.

     <p>An invalid object is considered less than a valid object.  The
     comparison of two invalid objects are equal.

     @param theRHS the object to be compared.

     @return a negative integer, zero, or a positive integer as this object
     is less than, equal to, or greater than the specified object.

     @throws ClassCastException - if the specified object's type prevents
     it from being compared to this Object.
  */
    public int compareTo(IntfItemData theRHS) {
        int compare = -1;
        if (theRHS == StrucItems.NULL_ITEM) {
            compare = 1;
        } else {
            if (!(theRHS instanceof DoubleItemData)) {
                throw new ClassCastException("rhs is " + theRHS.getClass());
            }
            DoubleItemData rhs = (DoubleItemData) theRHS;
            if (!isValid()) {
                if (!rhs.isValid()) {
                    compare = 0;
                }
            } else if (!rhs.isValid()) {
                compare = 1;
            } else {
                compare = myValue.compareTo(rhs.myValue);
            }
        }
        return compare;
    }

    /**
     Returns true if the value of this object is valid.

     @return true if the value of this object is valid, else returns false.
  */
    public boolean isValid() {
        return (myValue != null);
    }

    /**
     Copies the attributes of the specified source item to this item.  This
     method is used in StrucItems during a copyItems() to replicate an item
     value.  If the target item is not of the same type as the source, a
     program error message should be written and no attributes should be
     copied.

     @param theSource the source data item of the same class as the target.
  */
    @Override
    public void copy(IntfItemData theSource) {
        if (!(theSource instanceof DoubleItemData)) {
            fault("DoubleItemData.copy() invalid source");
        } else {
            super.copy(theSource);
            DoubleItemData source = (DoubleItemData) theSource;
            myValue = source.myValue;
            myDefinition = source.myDefinition;
            myMinValue = source.myMinValue;
            myMinWarn = source.myMinWarn;
            myMinOpr = source.myMinOpr;
            myMaxValue = source.myMaxValue;
            myMaxOpr = source.myMaxOpr;
            myMaxWarn = source.myMaxWarn;
            myItemIsNumEnum = source.myItemIsNumEnum;
        }
    }

    /**
     Returns a new instance of this object with the same values.
  */
    public final IntfItemData getItemData() {
        DoubleItemData data = new DoubleItemData();
        data.copy(this);
        return data;
    }

    /**
     Sets the value of this item from a String representation in the default
     unit system and specified data format.

     @param theFormat the format of the String data, e.g., {@link
     DataFormat#EXPORT}, {@link DataFormat#GUI}, etc.

     @param theValue the String representing the data value.
  */
    public void setString(DataFormat theFormat, String theValue) {
        setString(UnitSystem.US_CUSTOMARY, theFormat, theValue);
    }

    /**
     Sets the value of this item from a String representation in the specified
     unit system and data format.

     @param theSystem the unit system of the String data, e.g., {@link
     UnitSystem#US_CUSTOMARY}, etc.

     @param theFormat the format of the String data, e.g., {@link
     DataFormat#EXPORT}, {@link DataFormat#GUI}, etc.

     @param theValue the String data value.
  */
    public void setString(UnitSystem theSystem, DataFormat theFormat, String theValue) {
        if (theFormat == DataFormat.FORMATTER) {
            myGUIValue = theValue;
            myGUIUnitSystem = theSystem;
        } else {
            myMessageKey = null;
            if ((myValue != null) && (theFormat != DataFormat.EXPORT) && theValue.equals(getString(theSystem, theFormat))) {
                return;
            } else if ((theValue == null) || theValue.equals("")) {
                myValue = null;
                if ((myDefinition != null) && myDefinition.getAttr(ItemDef.A_OPTIONAL, ItemDef.V_FALSE).equals(ItemDef.V_TRUE)) {
                } else {
                    myMessageKey = CM_REQUIRED;
                    String message = (myDefinition != null) ? myDefinition.getAttr(ItemDef.A_DICT_NAME) : "UNKNOWN";
                    myMessageArgs = new String[] { "", message };
                }
            } else {
                if (myItemIsNumEnum) {
                    theValue = myDefinition.getEnumDefValue(theSystem, theValue);
                }
                myWarnMessage = null;
                myValue = fromString(theValue);
                if (myValue == null) {
                    myMessageKey = CM_INVALID;
                    String dictName = (myDefinition == null) ? "??" : myDefinition.getAttr(ItemDef.A_DICT_NAME);
                    myMessageArgs = new String[] { theValue, dictName };
                } else {
                    if ((theSystem != null) && (myUnits != null) && (theSystem != UnitSystem.getInternal())) {
                        myValue = new Double(myValue.doubleValue() * UnitSystem.getInternal().getFactor(myUnits));
                    }
                    if (myMinValue != null) {
                        if (myValue.doubleValue() < myMinValue.doubleValue()) {
                            setBoundErrorMessage(CM_TOO_LO, theValue, myMinValue, theSystem);
                            myValue = null;
                        } else if (ItemDef.V_MIN_OPERATOR_GT.equals(myMinOpr) && !(myMinValue.doubleValue() < myValue.doubleValue())) {
                            setBoundErrorMessage(CM_TOO_GT, theValue, myMinValue, theSystem);
                            myValue = null;
                        }
                    }
                    if (myValue != null) {
                        if (myMaxValue != null) {
                            if (myMaxValue.doubleValue() < myValue.doubleValue()) {
                                setBoundErrorMessage(CM_TOO_HI, theValue, myMaxValue, theSystem);
                                myValue = null;
                            } else if (ItemDef.V_MAX_OPERATOR_LT.equals(myMaxOpr) && !(myValue.doubleValue() < myMaxValue.doubleValue())) {
                                setBoundErrorMessage(CM_TOO_LT, theValue, myMaxValue, theSystem);
                                myValue = null;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     Returns a String representation of the value for the {@link
     UnitSystem#US_CUSTOMARY} unit system and specified data format.

     @param theFormat the format of the data to be returned, e.g., {@link
     DataFormat#EXPORT}, {@link DataFormat#GUI}, etc.

     @return a String representation in the specified data format or null if
     this item is invalid.
  */
    public String getString(DataFormat theFormat) {
        return getString(UnitSystem.US_CUSTOMARY, theFormat);
    }

    /**
     Returns a string representation of the value for the specified unit system
     and data format.

     @param theSystem the unit system of the returned data, e.g., {@link
     UnitSystem#US_CUSTOMARY}, etc.

     @param theFormat the format of the data to be returned, e.g., {@link
     DataFormat#EXPORT}, {@link DataFormat#GUI}, etc.

     @return a String representation in the specified data format and unit
     system, or null if this item is invalid.
  */
    public String getString(UnitSystem theSystem, DataFormat theFormat) {
        String value = null;
        if (theFormat == DataFormat.FORMATTER) {
            value = myGUIValue;
        } else if (!isValid()) {
        } else {
            if (myUnits == null) {
            }
            double converted = myValue.doubleValue();
            if ((theSystem != null) && (myUnits != null) && (theSystem != UnitSystem.getInternal())) {
                converted = (converted / UnitSystem.getInternal().getFactor(myUnits));
            }
            value = theFormat.format(theSystem, myUnits, converted, myDecimals);
        }
        return value;
    }

    /**
     Compares the specified object with this to determine if they are
     equivalent.

     @return true if the values are the same; otherwise false.
  */
    public boolean same(IntfItemData theData) {
        boolean same = false;
        if ((theData != null) && (myValue != null) && theData instanceof DoubleItemData) {
            DoubleItemData data = (DoubleItemData) theData;
            if ((data.myValue != null) && (data.myValue.doubleValue() == myValue.doubleValue())) {
                same = true;
            }
        }
        return same;
    }

    /**
     Returns a new Double value from the specified String.

     @param theString the String value.

     @return the Double value or null if the String does not parse to a Double.
  */
    private Double fromString(String theString) {
        Double value = null;
        try {
            value = new Double(ourNumberFormat.parse(theString.toUpperCase()).doubleValue());
        } catch (Exception e) {
            value = null;
        }
        return value;
    }

    /**
     Creates a formatted DataFormat.MESSAGE string with US_CUSTOMARY and
     optionally, METRIC, representation of the specified value.

     @param theValue the value to format.

     @return the String representation of the value or 'NaN' is theValue is NaN.
  */
    private String messageString(Double theValue) {
        String value = "NaN";
        if (!Double.isNaN(theValue)) {
            String usMessage = toString(theValue, UnitSystem.US_CUSTOMARY, DataFormat.MESSAGE);
            String metricMessage = toString(theValue, UnitSystem.METRIC, DataFormat.MESSAGE);
            if (usMessage.equals(metricMessage)) {
                value = usMessage;
            } else if (myDefaultSystem == null) {
                value = usMessage + " (" + metricMessage + ")";
            } else if (myDefaultSystem == UnitSystem.US_CUSTOMARY) {
                value = usMessage;
            } else {
                value = metricMessage;
            }
        }
        return value;
    }

    private String toString(Double theValue, UnitSystem theSystem, DataFormat theFormat) {
        double converted = theValue.doubleValue();
        if ((theSystem != null) && (myUnits != null) && (theSystem != UnitSystem.getInternal())) {
            converted = (converted / UnitSystem.getInternal().getFactor(myUnits));
        }
        return theFormat.format(theSystem, myUnits, converted, myDecimals);
    }

    private void setBoundErrorMessage(String theKey, String theValue, Double theBound, UnitSystem theSystem) {
        myMessageKey = theKey;
        myMessageArgs = new String[] { theValue, toString(theBound, theSystem, DataFormat.MESSAGE), myDefinition.getAttr(ItemDef.A_DICT_NAME) };
    }
}
