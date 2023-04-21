package logic.addressBook;

/**
 * This exception is thrown if required contact group is not found in database. This can be caused if user has two open sessions and removes contact group on one of them.
 * @author arachne
 */
public class InvalidGroupException extends Exception {
}
