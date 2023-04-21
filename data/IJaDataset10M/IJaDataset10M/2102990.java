package yajcp.core.exceptions;

public class UsersException extends YaJCPException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final int Unknown = 0;

    public static final int NoUsername = 1;

    public static final int NoPassword = 2;

    public static final int NoPasswordConfirm = 4;

    public static final int NoEmail = 8;

    public static final int PasswordsDoNotMatch = 16;

    public static final int UsernameInUse = 32;

    public static final int EmailInUse = 64;

    public static final int EmailInvalid = 128;

    public static final int IdDoesNotExist = 256;

    public static final int UsernameDoesNotExist = 512;

    private String message = "";

    public UsersException() {
    }

    public UsersException(String message) {
        super(message);
    }

    public UsersException(int ExceptionType) {
        message = "";
        switch(ExceptionType) {
            case PasswordsDoNotMatch:
                message = "The Passwords do not match";
                break;
            case UsernameInUse:
                message = "The username is already in use";
                break;
            case EmailInUse:
                message = "The email-address is already in use";
                break;
            case NoUsername:
                message = "No username given";
                break;
            case NoPassword:
                message = "No password given";
                break;
            case NoPasswordConfirm:
                message = "No passwordconfirmation given";
                break;
            case NoEmail:
                message = "No email-address given";
                break;
            case EmailInvalid:
                message = "The email-address is not valid";
                break;
            case IdDoesNotExist:
                message = "No user with this ID found";
                break;
            case UsernameDoesNotExist:
                message = "No user with this username found";
                break;
            default:
                message = "Unknown error";
                break;
        }
        new YaJCPException().SetMessage(message);
    }

    public String getMessage() {
        return message;
    }
}
