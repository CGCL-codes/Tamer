package Domein.StudentPackage;

import Persistentie.SqlXMLProvider;
import java.util.List;

/**
 *
 * @author Kristof
 */
public class StudentWebserviceBeheerder {

    /** Creates a new instance of StudentWebserviceBeheerder */
    private StudentWebserviceBeheerder() {
    }

    private StudentWebserviceBeheerder thisje;

    public StudentWebserviceBeheerder getInstance() {
        if (thisje == null) {
            thisje = new StudentWebserviceBeheerder();
        }
        return thisje;
    }

    public void InsertStudenten(List<managedBean.StudentInfo> lijst) {
        for (int i = 0; i < lijst.size(); i++) {
            SqlXMLProvider.getInstance().insertStudent(lijst.get(i));
        }
    }
}
