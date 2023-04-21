package com.iv.flash.api.action;

import com.iv.flash.api.FlashFile;
import com.iv.flash.api.FlashItem;
import com.iv.flash.context.Context;
import com.iv.flash.util.*;
import java.io.PrintStream;

/**
 * Program to be executed when certain specified clip events are occured
 *
 * @author Dmitry Skavish
 */
public class ClipAction extends FlashItem {

    public static final int LOAD = 0x00000001;

    public static final int ENTER_FRAME = 0x00000002;

    public static final int UNLOAD = 0x00000004;

    public static final int MOUSE_MOVE = 0x00000008;

    public static final int MOUSE_DOWN = 0x00000010;

    public static final int MOUSE_UP = 0x00000020;

    public static final int KEY_DOWN = 0x00000040;

    public static final int KEY_UP = 0x00000080;

    public static final int DATA = 0x00000100;

    public static final int INITIALIZE = 0x00000200;

    public static final int MOUSE_PRESS = 0x00000400;

    public static final int MOUSE_RELEASE = 0x00000800;

    public static final int MOUSE_RELEASE_OUTSIDE = 0x00001000;

    public static final int MOUSE_ROLLOVER = 0x00002000;

    public static final int MOUSE_ROLLOUT = 0x00004000;

    public static final int MOUSE_DRAGOVER = 0x00008000;

    public static final int MOUSE_DRAGOUT = 0x00010000;

    public static final int KEY_PRESS = 0x00020000;

    private int flags;

    private int keyCode;

    private Program program;

    public ClipAction() {
    }

    /**
     * Creates clip actions
     *
     * @param flags   events to activate the specified program
     * @param program program to be executed when specified events are occured
     */
    public ClipAction(int flags, Program program) {
        this(flags, program, 0);
    }

    /**
     * Creates clip actions
     *
     * @param flags   events to activate the specified program
     * @param program program to be executed when specified events are occured
     */
    public ClipAction(int flags, Program program, int keyCode) {
        this.flags = flags;
        this.program = program;
        this.keyCode = keyCode;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public int getFlags() {
        return flags;
    }

    /**
     * Sets key code to catch
     *
     * @param keyCode key code, see {@link com.iv.flash.api.button.ActionCondition}
     */
    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return this.keyCode;
    }

    public Program getProgram() {
        return program;
    }

    public void process(FlashFile file, Context context) throws IVException {
        file.processObject(program, context);
    }

    public boolean isConstant() {
        return program.isConstant();
    }

    public void write(FlashOutput fob) {
        int version = fob.getFlashFile().getVersion();
        if (version > 5) fob.writeDWord(flags); else fob.writeWord(flags);
        if ((flags & KEY_PRESS) != 0) {
            fob.writeDWord(program.getLength() + 1);
            fob.writeByte(keyCode);
        } else {
            fob.writeDWord(program.getLength());
        }
        program.write(fob);
    }

    public void printContent(PrintStream out, String indent) {
        out.println(indent + "    ClipAction: flags=0x" + Util.d2h(flags) + ", keyCode=" + keyCode);
        program.printContent(out, indent + "        ");
    }

    public FlashItem getCopy(ScriptCopier copier) {
        return new ClipAction(flags, (Program) program.getCopy(copier), keyCode);
    }
}
