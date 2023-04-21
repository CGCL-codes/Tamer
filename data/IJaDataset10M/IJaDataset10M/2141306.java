package quickorm.demo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.torque.Torque;
import org.apache.torque.TorqueException;
import quickorm.core.FeederTemplate;
import quickorm.core.FieldFeeder;

/**
 *
 * @author Rovers
 */
public class DemoMain3 {

    public static void main(String[] args) {
        Connection con = null;
        try {
            Torque.init("Torque.properties");
            con = Torque.getConnection("CECI");
            Statement st = con.createStatement();
            FeederTemplate<Employee> ft = FieldFeeder.getFeederTemplate(Employee.class);
            ArrayList<Employee> list = ft.select("Where Employee_ID='Rovers'", st);
            System.out.println("first record = " + list.get(0).getEmployee_Name());
            System.out.println("total size = " + list.size());
            ft.feedSubItems(list.get(0), st);
            System.out.println(list.get(0).getProjectList().get(0).getProjectName() + "," + list.get(0).getProjectList().get(0).getProjectRole());
        } catch (SQLException ex) {
            Logger.getLogger(DemoMain3.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TorqueException ex) {
            Logger.getLogger(DemoMain3.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DemoMain3.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
