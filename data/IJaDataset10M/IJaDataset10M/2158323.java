package com.mysql.jdbc.exceptions;

public class MySQLDataException extends MySQLNonTransientException {

    public MySQLDataException() {
        super();
    }

    public MySQLDataException(String reason, String SQLState, int vendorCode) {
        super(reason, SQLState, vendorCode);
    }

    public MySQLDataException(String reason, String SQLState) {
        super(reason, SQLState);
    }

    public MySQLDataException(String reason) {
        super(reason);
    }
}
