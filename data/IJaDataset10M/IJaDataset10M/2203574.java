package co.in.wexdas.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

/**
 *
 * @author vats
 */
public class DataRegistrar {

    co.in.wexdas.db.DBDetails db_details;

    public DataRegistrar() {
        db_details = new co.in.wexdas.db.DBDetails();
    }

    public boolean saveData(String table_name, List<Data> data_list) {
        boolean registration_status = false;
        String query_string = "INSERT INTO " + table_name;
        String field_list = "(";
        String field_values = "(";
        int count_of_fields = data_list.size();
        for (int i = 0; i < count_of_fields; i++) {
            Data data_temp = new Data();
            data_temp = data_list.get(i);
            field_list += data_temp.parameter_name;
            if ((i + 1) < count_of_fields) {
                field_list += ",";
            }
            if ((i + 1) == count_of_fields) {
                field_list += ")";
            }
        }
        for (int i = 0; i < count_of_fields; i++) {
            Data data_temp = new Data();
            data_temp = data_list.get(i);
            String field_type = data_temp.parameter_type;
            field_values += "'" + data_temp.parameter_value.toString() + "'";
            if ((i + 1) < count_of_fields) {
                field_values += ",";
            }
            if ((i + 1) == count_of_fields) {
                field_values += ")";
            }
        }
        query_string += field_list + " values " + field_values;
        System.out.println("\n Query String");
        System.out.println("\n" + query_string);
        try {
            Class.forName(db_details.getDriverName());
            Connection con = DriverManager.getConnection(db_details.getDataSource() + db_details.getDatabaseName(), db_details.getUsername(), db_details.getPassword());
            Statement st = con.createStatement();
            st.execute(query_string);
            registration_status = true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return registration_status;
    }
}
