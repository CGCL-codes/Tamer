package edu.nus.iss.ejava.team4.ejb.exception;

@SuppressWarnings("serial")
public class DAOException extends RuntimeException {

    public DAOException() {
    }

    public DAOException(String msg) {
        super(msg);
    }
}
