package commonapp.datadef;

import commonapp.datacon.ItemDataType;
import java.util.ArrayList;

public class ResultDef extends BaseDef {

    /** Element: base element name. */
    public static final String E_RESULT = "Result";

    public static final String A_ENUMERATION = "enumeration";

    public static final char NAME_SEPARATOR = '^';

    private EnumerationDef myEnumeration = null;

    private String myEnumerationName = null;

    /**
     Constructs a new ResultDef.
  */
    public ResultDef() {
        super(E_RESULT);
    }

    /**
     Initializes the definition.  Obtains any specified A_EXTENDS/A_REF
     definition.  Obtains the subelements specified by an E_FROM element
     and replaces any elements that have an A_REF attribute with the
     element with the associated A_NAME attribute.

     @return true if this definition is valid, else returns false.
  */
    @Override
    public boolean init(DataDict theDictionary) {
        if (!myHaveInit) {
            super.init(theDictionary);
            setEnumeration();
        }
        return myValidFlag;
    }

    /**
     Gets the enumeration associated with this Result definition.

     @return the EnumerationDef associated with this definition or return
     null if no EnumerationDef is associated with this Result definition.
  */
    public EnumerationDef getEnumeration() {
        return myEnumeration;
    }

    /**
     Gets the list of EnumDef objects associated with this Result definition.

     @return the list of EnumDef objects associated with this definition or
     return null if no EnumerationDef is associated with this Result
     definition.
  */
    public ArrayList<EnumDef> getEnumDefs() {
        ArrayList<EnumDef> enumDefs = null;
        if (myEnumeration != null) {
            enumDefs = myEnumeration.getEnumDefs();
        }
        return enumDefs;
    }

    /**
     Gets the EnumDef object associated with the specified value.

     @return the EnumDef objects associated with the specified value or
     return null.
  */
    public final EnumDef getEnumDef(String theValue) {
        EnumDef enumDef = null;
        if (theValue != null) {
            ArrayList<EnumDef> enumDefs = getEnumDefs();
            if (enumDefs != null) {
                for (EnumDef def : enumDefs) {
                    if (def.getDataValue().equals(theValue)) {
                        enumDef = def;
                        break;
                    }
                }
            }
        }
        return enumDef;
    }

    protected ItemDef addItemDef(DataDict theDataDict, String theBaseName) {
        ItemDef itemDef = DefFactory.getDef(ItemDef.class);
        String type = getAttr(A_TYPE);
        itemDef.putAttr(A_NAME, theBaseName + NAME_SEPARATOR + type);
        itemDef.putAttr(ItemDef.A_DATATYPE, ItemDataType.ENUM.getName());
        itemDef.putAttr(A_ENUMERATION, myEnumerationName);
        itemDef.putAttr(A_TITLE, getAttr(A_TITLE));
        itemDef.putAttr(ItemDef.A_COL_TITLE, getAttr(ItemDef.A_COL_TITLE));
        itemDef.putAttr(A_TIP, getAttr(A_TIP));
        theDataDict.addDef(itemDef, theBaseName);
        return itemDef;
    }

    /**
     Sets myEnumeration.
  */
    private void setEnumeration() {
        if (myReference != null) {
            myEnumeration = ((ResultDef) myReference).getEnumeration();
            myEnumerationName = ((ResultDef) myReference).myEnumerationName;
        } else {
            String enumerationName = getAttr(A_ENUMERATION);
            if (enumerationName != null) {
                myEnumerationName = enumerationName;
                myEnumeration = (EnumerationDef) getDictionary().find(EnumerationDef.E_ENUMERATION, enumerationName);
                if (myEnumeration != null) {
                    removeDefs(EnumerationDef.E_ENUMERATION);
                    addDef(myEnumeration);
                    putAttr(A_ENUMERATION, null);
                }
            }
            if (myEnumeration == null) {
                ArrayList<EnumerationDef> def = getDefs(EnumerationDef.class);
                if ((def != null) && (def.size() == 1)) {
                    myEnumeration = def.get(0);
                }
            }
        }
    }
}
