package com.rise.rois.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import com.rise.rois.server.db.util.DBManager;
import com.rise.rois.server.db.util.DBUtils;
import com.rise.rois.server.model.Computer;
import com.rise.rois.server.model.ILoadComputers;

public class DBComputerLoader implements ILoadComputers {

    @Override
    public Set<Computer> load() {
        Set<Computer> computers = new HashSet<Computer>();
        Connection connection = DBManager.getConnection();
        String query = "SELECT * FROM  " + DBUtils.COMPUTERS;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                Computer computer = createFromResultSet(rs);
                if (computer != null) {
                    computers.add(computer);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return computers;
    }

    private Computer createFromResultSet(ResultSet rs) throws SQLException {
        int computer_id = rs.getInt(1);
        String computer_name = rs.getString(2);
        String computer_ip = rs.getString(3);
        String serial = rs.getString(4);
        int available = rs.getInt(5);
        Computer computer = new Computer(computer_id);
        computer.setComputer_name(computer_name);
        computer.setComputer_ip(computer_ip);
        computer.setSerial_number(serial);
        if (available == 1) {
            computer.setAvailable(true);
        } else {
            computer.setAvailable(false);
        }
        return computer;
    }
}
