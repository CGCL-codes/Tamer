package mposms.Test;

import java.util.ArrayList;
import java.util.List;
import mposms.DAO.ObjectTO;
import mposms.DAO.TableDAO;

/**
 * @author de Landsheer Thierry
 *
 */
public class MPosMS_Test_DAO {

    public boolean testInsertOK(TableDAO table, ObjectTO object) {
        try {
            table.insert(object);
            return true;
        } catch (Exception e) {
            System.err.println(e.getStackTrace());
            return false;
        }
    }

    public boolean testDeleteOK(TableDAO table, ObjectTO object) {
        try {
            table.delete(object);
            return true;
        } catch (Exception e) {
            System.err.println(e.getStackTrace());
            return false;
        }
    }

    public boolean testUpdateOK(TableDAO table, ObjectTO object) {
        try {
            table.update(object);
            return true;
        } catch (Exception e) {
            System.err.println(e.getStackTrace());
            return false;
        }
    }

    public boolean testSelectOK(TableDAO table, String field, String research) {
        List list = new ArrayList();
        try {
            list = table.select(field, research);
            for (int i = 0; i < list.size(); i++) {
                System.out.println(list.get(i));
            }
            return true;
        } catch (Exception e) {
            System.err.println(e.getStackTrace());
            return false;
        }
    }

    public boolean testSelectAllOK(TableDAO table) {
        List list = new ArrayList();
        try {
            list = table.selectAll();
            for (int i = 0; i < list.size(); i++) {
                System.out.println(list.get(i));
            }
            return true;
        } catch (Exception e) {
            System.err.println(e.getStackTrace());
            return false;
        }
    }
}
