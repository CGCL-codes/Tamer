package wa.tosec;

/**
 * Title:        TosecTool
 * Description:  Part of "Weltanschauung" an open source atari database project 
 * Copyright:    GPL
 * Group:        Foundation Two/Germany
 * Created on:   29.05.2004
 * 
 * @author twh
 */
public class TosecParseException extends Exception {

    public static String TOSEC_EXCEPTION_YEAR = "No valid year!";

    public TosecParseException(String message) {
        super(message);
    }
}
