package pl.xperios.rdk.shared.validators;

/**
 *
 * @param <T>
 * @author Praca
 */
public interface ValidatorCondition<T> {

    /**
     *
     */
    public static int ValidDateMax = 1;

    /**
     *
     */
    public static int ValidDateMin = 2;

    /**
     *
     */
    public static int ValidIsNotNull = 3;

    /**
     *
     */
    public static int ValidLengthMax = 4;

    /**
     *
     */
    public static int ValidLengthMin = 5;

    /**
     *
     */
    public static int ValidPESEL = 6;

    /**
     *
     */
    public static int ValidRegex = 7;

    /**
     *
     */
    public static int Validator = 8;

    /**
     *
     */
    public static int ValidPassword = 9;

    /**
     *
     * @param value
     * @return
     */
    public boolean isValid(T value);

    /**
     *
     * @return
     */
    public String getErrorLabel();

    /**
     *
     * @return
     */
    public int getType();
}
