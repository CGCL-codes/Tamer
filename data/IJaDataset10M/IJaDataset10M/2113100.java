package org.openremote.controller.exception;

/**
 * The Class NoSuchCommandBuilderException.
 * 
 * @author Dan 2009-4-30
 */
@SuppressWarnings("serial")
public class NoSuchCommandBuilderException extends ControlCommandException {

    /**
    * Instantiates a new no such command builder exception.
    */
    public NoSuchCommandBuilderException() {
        super("Please check the property 'commandBuilders' " + "configuration of bean 'commandFactory' in applicationContext.xml");
        setErrorCode(ControlCommandException.NO_SUCH_COMMAND_BUILDER);
    }

    /**
    * Instantiates a new no such command builder exception.
    * 
    * @param message the message
    * @param cause the cause
    */
    public NoSuchCommandBuilderException(String message, Throwable cause) {
        super(message + ", please check the property 'commandBuilders' " + "configuration of bean 'commandFactory' in applicationContext.xml", cause);
        setErrorCode(ControlCommandException.NO_SUCH_COMMAND_BUILDER);
    }

    /**
    * Instantiates a new no such command builder exception.
    * 
    * @param message the message
    */
    public NoSuchCommandBuilderException(String message) {
        super(message + ", please check the property 'commandBuilders' " + "configuration of bean 'commandFactory' in applicationContext.xml");
        setErrorCode(ControlCommandException.NO_SUCH_COMMAND_BUILDER);
    }

    /**
    * Instantiates a new no such command builder exception.
    * 
    * @param cause the cause
    */
    public NoSuchCommandBuilderException(Throwable cause) {
        super(cause);
        setErrorCode(ControlCommandException.NO_SUCH_COMMAND_BUILDER);
    }
}
