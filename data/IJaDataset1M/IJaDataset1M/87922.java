package struts2.sample05;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.apache.struts2.config.Result;

/**
 * employeesテーブルからデータを全件取得するアクション
 */
@Result("database.jsp")
public class DatabaseAction {

    /** データベース接続のもと */
    private DataSource dataSource;

    /** 従業員のリスト（検索結果） */
    private List<Employee> employees;

    /**
	 * アクション実行メソッド
	 */
    public String execute() throws SQLException {
        employees = new ArrayList<Employee>();
        Connection conn = dataSource.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select * from employees");
        while (rs.next()) {
            int id = rs.getInt("ID");
            String firstName = rs.getString("FIRST_NAME");
            String lastName = rs.getString("LAST_NAME");
            Employee employee = new Employee();
            employee.setId(id);
            employee.setFirstName(firstName);
            employee.setLastName(lastName);
            employees.add(employee);
        }
        return "success";
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Employee> getEmployees() {
        return employees;
    }
}
