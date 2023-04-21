package hambo.pim;

import java.util.Vector;

public class AddressBook {

    private String formName = null;

    private Vector columnVect = new Vector();

    public AddressBook(String formName) {
        this.formName = formName;
    }

    public void addColumn(String field, String title) {
        columnVect.add(new Column(field, title));
    }

    public int size() {
        return columnVect.size();
    }

    public Column getColumnAt(int index) {
        return (Column) columnVect.elementAt(index);
    }

    public String getFormName() {
        return formName;
    }
}
