package org.xith3d.utility.commands.impl;

import org.xith3d.input.InputHandler;
import org.xith3d.utility.commands.Command;
import org.xith3d.utility.commands.CommandException;
import org.xith3d.utility.commands.KeyBoundCommand;

/**
 * This {@link Command} handles disabling and enabling of an
 * {@link InputHandler} on input events.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class InputHandlerSuspendCommand extends KeyBoundCommand {

    public static final String SUCCESS = "ok";

    private final InputHandler<?> ih;

    private final int suspendMask;

    public final int getSuspendMask() {
        return (suspendMask);
    }

    public String execute(Boolean inputInfo) throws CommandException {
        if (inputInfo == null) return (null);
        if (inputInfo.booleanValue()) ih.setSuspendMask(ih.getSuspendMask() - (ih.getSuspendMask() & suspendMask)); else ih.setSuspendMask(ih.getSuspendMask() | suspendMask);
        return (SUCCESS);
    }

    public InputHandlerSuspendCommand(String commandKey, String text, InputHandler<?> ih, int suspendMask) {
        super(commandKey, text);
        this.ih = ih;
        this.suspendMask = suspendMask;
    }

    public InputHandlerSuspendCommand(String commandKey, InputHandler<?> ih, int suspendMask) {
        this(commandKey, "Suspend InputHandler", ih, suspendMask);
    }

    public InputHandlerSuspendCommand(String commandKey, String text, InputHandler<?> ih) {
        this(commandKey, text, ih, InputHandler.KEYBOARD_SUSPENDED | InputHandler.MOUSE_MOVEMENT_SUSPENDED | InputHandler.MOUSE_BUTTONS_SUSPENDED | InputHandler.MOUSE_WHEEL_SUSPENDED);
    }

    public InputHandlerSuspendCommand(String commandKey, InputHandler<?> ih) {
        this(commandKey, "Suspend InputHandler", ih);
    }
}
