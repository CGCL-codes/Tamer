package ca.uhn.hunit.util;

import ca.uhn.hunit.ex.ConfigurationException;
import ca.uhn.hunit.xsd.JavaArgument;
import ca.uhn.hunit.xsd.NamedJavaArgument;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author James
 */
public class TypedValueListTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    private final ArrayList<Class<?>> myArgTypes = new ArrayList<Class<?>>();

    private final ArrayList<Object> myArgs = new ArrayList<Object>();

    private final ArrayList<String> myNames = new ArrayList<String>();

    private final String[] myNamedColumnNames = { "Name", "Value", "Type" };

    private final boolean myNamed;

    public TypedValueListTableModel(boolean theNamed) {
        myNamed = theNamed;
    }

    /**
     * Imports the values for this list from an XML config
     */
    public void exportValuesToXml(List<? extends JavaArgument> theArgDefinitions) {
        for (int i = 0; i < myArgTypes.size(); i++) {
            Class<?> nextArgType = myArgTypes.get(i);
            Object nextArg = myArgs.get(i);
            JavaArgument nextJavaArg = new JavaArgument();
            if (String.class.equals(nextArgType)) {
                nextJavaArg.setType("java.lang.String");
                nextJavaArg.setValue((String) nextArg);
            } else if (Integer.class.equals(nextArgType)) {
                nextJavaArg.setType("java.lang.Integer");
                nextJavaArg.setValue(((Integer) nextArg).toString());
            } else if (int.class.equals(nextArgType)) {
                nextJavaArg.setType("int");
                nextJavaArg.setValue(((Integer) nextArg).toString());
            }
            if (myNamed) {
                ((NamedJavaArgument) nextJavaArg).setName(myNames.get(i));
            }
        }
    }

    public Object getArg(int theIndex) {
        return myArgs.get(theIndex);
    }

    public Object[] getArgArray() {
        return myArgs.toArray(new Object[myArgs.size()]);
    }

    public Class<?> getArgType(int theIndex) {
        return myArgTypes.get(theIndex);
    }

    public Class<?>[] getArgTypeArray() {
        return myArgTypes.toArray(new Class<?>[myArgTypes.size()]);
    }

    @Override
    public int getColumnCount() {
        return myNamed ? 3 : 2;
    }

    @Override
    public String getColumnName(int column) {
        if (!myNamed) {
            column++;
        }
        return myNamedColumnNames[column];
    }

    public String getName(int theIndex) {
        return myNames.get(theIndex);
    }

    public int getRowCount() {
        return myArgTypes.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (!myNamed) {
            columnIndex++;
        }
        switch(columnIndex) {
            case 0:
                return myNames.get(rowIndex);
            case 1:
                return myArgs.get(rowIndex);
            case 2:
                return myArgTypes.get(rowIndex);
            default:
                throw new IllegalStateException("Col " + columnIndex);
        }
    }

    /**
     * Imports the values for this list from an XML config
     */
    public void importValuesFromXml(List<? extends JavaArgument> theArgDefinitions) throws ConfigurationException {
        for (JavaArgument next : theArgDefinitions) {
            if ("java.lang.String".equals(next.getType())) {
                myArgTypes.add(String.class);
                myArgs.add(next.getValue());
            } else if ("java.lang.Integer".equals(next.getType())) {
                myArgTypes.add(Integer.class);
                myArgs.add(Integer.parseInt(next.getValue()));
            } else if ("int".equals(next.getType())) {
                myArgTypes.add(int.class);
                myArgs.add(Integer.parseInt(next.getValue()));
            } else {
                throw new ConfigurationException("Unknown arg type: " + next.getType());
            }
            if (myNamed) {
                myNames.add(((NamedJavaArgument) next).getName());
            }
        }
    }

    /**
     * Replaces the existing type/data combinations with new entries matching the given
     * types. Where possible, existing data is preserved.
     */
    public void setTypes(List<Class<?>> theTypes) {
        if (myNamed) {
            throw new IllegalStateException();
        }
        for (int i = 0; i < theTypes.size(); i++) {
            if (i >= myArgTypes.size()) {
                myArgTypes.add(theTypes.get(i));
                myArgs.add(null);
            } else if (!myArgTypes.get(i).equals(theTypes.get(i))) {
                myArgTypes.set(i, theTypes.get(i));
                myArgs.set(i, null);
            }
        }
        while (myArgTypes.size() > theTypes.size()) {
            myArgTypes.remove(theTypes.size());
            myArgs.remove(theTypes.size());
        }
        fireTableDataChanged();
    }
}
