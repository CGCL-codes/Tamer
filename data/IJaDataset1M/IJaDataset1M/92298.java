package net.sf.brightside.mobilestock.service;

import net.sf.brightside.mobilestock.core.command.MobileStockException;

public class ShareDoesntExistException extends MobileStockException {

    private static final long serialVersionUID = 1L;

    public ShareDoesntExistException() {
        super("Share doesn't exists");
    }

    public ShareDoesntExistException(String message) {
        super(message);
    }
}
