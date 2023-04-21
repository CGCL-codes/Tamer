package sqs.model;

/**
 * @author kjleng
 *
 */
public abstract class MCQQuestion extends Question {

    public static final String SORTBY_RESPONSE = "R";

    public static final String SORTBY_ORDERNO = "O";

    public Scale createScale() {
        return null;
    }

    public Option createOption() {
        return new Option();
    }
}
