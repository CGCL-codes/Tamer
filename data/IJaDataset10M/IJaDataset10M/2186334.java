package net.ko.ksql;

import java.sql.DriverManager;
import java.sql.SQLException;

public class KDBCustom extends KDataBase {

    private String url;

    public void connect(String driver, String url) throws ClassNotFoundException, SQLException {
        this.driver = driver;
        this.url = url;
        _connect();
    }

    @Override
    protected void _connect() throws ClassNotFoundException, SQLException {
        super._connect();
        Class.forName(driver);
        connection = DriverManager.getConnection(this.url);
    }
}
