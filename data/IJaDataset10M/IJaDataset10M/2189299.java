package com.easyExam.common.exception;

/**
 * @author lgli
 *
 * Created on  2009-12-25
 */
public class BusinessException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String[] arguments = null;

    /**
     * @param errorKey
     */
    public BusinessException(String errorKey) {
        super(errorKey);
    }

    /**
     * @param errorKey
     * @param arguments
     */
    public BusinessException(String errorKey, String[] arguments) {
        super(errorKey);
        setArguments(arguments);
    }

    /**
     * @param errorKey
     * @param arguments
     * @param cause
     */
    public BusinessException(String errorKey, String[] arguments, Throwable cause) {
        super(errorKey, cause);
        setArguments(arguments);
    }

    /**
     * This api will 
     *
     * @param index
     * @return
     * @return String
     * @since TeleNav, Inc 1.0
     */
    public String getArgument(int index) {
        return this.arguments[index];
    }

    /**
     * This api will 
     *
     * @return
     * @return String[]
     * @since TeleNav, Inc 1.0
     */
    public String[] getArguments() {
        return arguments;
    }

    /**
     * This api will 
     *
     * @param arguments
     * @return void
     * @since TeleNav, Inc 1.0
     */
    public void setArguments(String[] arguments) {
        this.arguments = arguments;
    }

    /**
     * This api will 
     *
     * @param index
     * @return boolean
     * @since TeleNav, Inc 1.0
     */
    public boolean isI18nArgument(int index) {
        String arg = arguments[index];
        if (arg.length() >= 2) {
            if (arg.startsWith("{") && arg.endsWith("}")) {
                return true;
            }
        }
        return false;
    }

    /**
     * This api will 
     *
     * @param index
     * @return String
     * @since TeleNav, Inc 1.0
     */
    public String getI18nArgumentKey(int index) {
        if (!isI18nArgument(index)) {
            return null;
        }
        String arg = arguments[index];
        return arg.substring(1, arg.length() - 1);
    }
}
