package net.sourceforge.magex.mobile.GPS;

import net.sourceforge.magex.mobile.MaGeXException;
import net.sourceforge.magex.mobile.Texts;

/**
 * 
 * represents exceptions thrown by GPS modules
 */
public class GPSException extends MaGeXException {

    public static final int CAN_NOT_INIT_GPSPROVIDER = 1;

    public static final int MISSING_SYSTEM_SUPPORT = 2;

    public static final int COM_TIMEOUT = 3;

    public static final int COM_INPUT_ERROR = 4;

    public static final int COM_HALTED = 5;

    /** Constructor
     *  @param code internal Exception code
     */
    public GPSException(int code) {
        super(code);
    }

    /** Returns exception text
     *  @return exception text, in user-selected language
     */
    public String getText() {
        switch(code) {
            case CAN_NOT_INIT_GPSPROVIDER:
                return Texts.GPE_CAN_NOT_INIT;
            case MISSING_SYSTEM_SUPPORT:
                return Texts.GPE_MISSING_SYSTEM_SUPPORT;
            case COM_TIMEOUT:
                return Texts.GPE_COM_TIMEOUT;
            case COM_INPUT_ERROR:
                return Texts.GPE_COM_INPUT_ERROR;
            case COM_HALTED:
                return Texts.GPE_COM_HALTED;
            default:
                return "";
        }
    }
}
