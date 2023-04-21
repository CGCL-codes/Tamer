package info.mjdenkowski.util;

import java.io.*;
import java.util.*;
import java.sql.*;

public class JDBCSource implements DataSource {

    private final Connection connection;

    public JDBCSource(Connection connection) {
        this.connection = connection;
    }

    public void close() throws SQLException {
        this.connection.close();
    }

    public Object getData() {
        return (Object) new String();
    }

    public void setData(Object data) {
    }

    public void updateData() {
    }

    public void commitData() {
    }
}
