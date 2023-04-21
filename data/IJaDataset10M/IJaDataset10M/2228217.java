package org.axed.user.client.axessory;

import java.util.ArrayList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import org.axed.user.client.Anchor;
import org.axed.user.client.AxedListener;
import org.axed.user.client.AxedEvent;
import org.axed.user.client.AxedInterval;
import org.axed.user.client.DOM;
import org.axed.user.client.Selection;
import org.axed.user.client.InputBase;
import org.axed.user.client.InputCatcher;
import org.axed.user.client.axessory.VimBar;
import org.axed.user.client.axessory.VimInput;

/**
 * Handles the commandline editor.
 */
public class VimCommand {

    VimInput vimInput;

    public AxedInterval cmdRange;

    /**	
	 * Constructor.
	 */
    public VimCommand(VimInput vimInput) {
        this.vimInput = vimInput;
        cmdRange = new AxedInterval(vimInput.axed);
    }

    /**
	 * Interface for all commands
	 */
    public abstract class Command {

        public abstract String name();

        public abstract void execute(String param);
    }

    ;

    /**
	 * /s[ubstitute]/pre/post/flags
	 */
    public class CommandSubstitute extends Command {

        public String name() {
            return "substitute";
        }

        public void execute(String param) {
            int p = 0;
            int p0 = 0;
            String from = "";
            String to = "";
            String flags = "";
            for (p = 0; p <= param.length(); p++) {
                if (p == param.length() || (param.charAt(p) == '/' && (p == 0 || param.charAt(p - 1) != '\\'))) {
                    from = param.substring(0, p);
                    p++;
                    break;
                }
            }
            for (p0 = p; p <= param.length(); p++) {
                if (p == param.length() || (param.charAt(p) == '/' && (p == 0 || param.charAt(p - 1) != '\\'))) {
                    to = param.substring(p0, p);
                    p++;
                    break;
                }
            }
            if (p < param.length()) {
                flags = param.substring(p, param.length());
            }
            boolean flagGlobal = false;
            if (flags.indexOf('g') >= 0) {
                flagGlobal = true;
            }
            int r = vimInput.axed.replaceExpr(vimInput.axed.createAtomicID(), from, to, cmdRange, flagGlobal);
            if (r == 0) {
                vimInput.vimBar.showMessage("Pattern not found:" + from, vimInput.vimBar.MESSAGE_VERY_RED);
                return;
            } else {
                vimInput.vimBar.showMessage(r + " replacements.", vimInput.vimBar.MESSAGE_NORMAL);
                return;
            }
        }
    }

    /**
	 * /set flags
	 */
    public class CommandSet extends Command {

        public String name() {
            return "set";
        }

        public void execute(String param) {
            if (param.equals("wrap")) {
                vimInput.axed.setWrap(true);
            } else if (param.equals("nowrap")) {
                vimInput.axed.setWrap(false);
            } else {
                vimInput.vimBar.showMessage("Unknown Option:" + param, vimInput.vimBar.MESSAGE_VERY_RED);
            }
        }
    }

    /**
	 * All commands suported, order gives priority by ambigous matches.
	 */
    public Command[] cmdList = { new CommandSubstitute(), new CommandSet() };

    /**
	 * the command
	 */
    public StringBuffer commandLine = new StringBuffer();

    /**
	 * the position the parser is in the commandLine
	 */
    private int parsePos;

    /**
	 * Resets/starts the command to be entered.
	 */
    public void resetCmd(String cmd) {
        commandLine.delete(0, commandLine.length());
        commandLine.append(cmd);
        vimInput.vimBar.cursesMessage(cmd, cmd.length());
        vimInput.axed.setCaretEnabled(false);
        vimInput.vimBar.setHighlight(true);
    }

    /**
	 * Cancels the commandbar edit
	 */
    public void cancelCmdBar() {
        vimInput.vimBar.setHighlight(false);
        vimInput.vimBar.setCaret(-1);
        vimInput.axed.setCaretEnabled(true);
        vimInput.cmdMode = 0;
    }

    public void backspace() {
        int cp = vimInput.vimBar.caretPos;
        if (cp > 1) {
            commandLine.deleteCharAt(vimInput.vimBar.caretPos - 1);
        }
        if (!vimInput.vimBar.backspace()) {
            cancelCmdBar();
        }
    }

    public void del() {
        int cp = vimInput.vimBar.caretPos;
        if (cp < commandLine.length()) {
            commandLine.deleteCharAt(vimInput.vimBar.caretPos);
        }
        vimInput.vimBar.del();
    }

    public void caretLeft() {
        vimInput.vimBar.caretLeft();
    }

    public void caretPos1() {
        vimInput.vimBar.caretPos1();
    }

    public void caretEnd() {
        vimInput.vimBar.caretEnd();
    }

    public void caretRight() {
        vimInput.vimBar.caretRight();
    }

    /**
	 * parses the range part which can be put for each command.
	 */
    public void parseCmdRange() {
        if (commandLine.charAt(parsePos) == '%') {
            cmdRange.setAll();
            parsePos++;
        } else if (vimInput.hasSelection()) {
            cmdRange.set(vimInput.sel);
        } else {
            cmdRange.setLine(vimInput.getCursorLine(), true);
        }
    }

    /**
	 * parses the count commands can be prefixed with.
	 */
    public void parseCmdCount() {
    }

    /**
	 * finds the command specified and gives over control
	 */
    public void parseCmdFinder() {
        int cmdBegin = parsePos;
        int cl = commandLine.length();
        while (parsePos < cl && Character.isLetter(commandLine.charAt(parsePos))) {
            parsePos++;
        }
        String command = commandLine.substring(cmdBegin, parsePos);
        String param = parsePos == cl ? "" : commandLine.substring(parsePos + 1, cl);
        for (int i = 0; i < cmdList.length; i++) {
            if (((Command) cmdList[i]).name().startsWith(command)) {
                ((Command) cmdList[i]).execute(param);
                return;
            }
        }
        vimInput.vimBar.showMessage("command invalid or not supported :-(", VimBar.MESSAGE_RED);
        cancelCmdBar();
    }

    public void parseCommand() {
        parsePos = 1;
        parseCmdRange();
        parseCmdCount();
        parseCmdFinder();
    }

    public void interprete() {
        switch(vimInput.cmdMode) {
            case '/':
            case '?':
                if (vimInput.command.cmd == 0) {
                    vimInput.vimFind(commandLine.substring(1, commandLine.length()));
                } else {
                    vimInput.command.dst = vimInput.cmdMode;
                    vimInput.command.str1 = commandLine.substring(1, commandLine.length());
                    vimInput.exec(vimInput.command);
                }
                cancelCmdBar();
                vimInput.vimCursorMoved();
                return;
            case ':':
                parseCommand();
                cancelCmdBar();
                return;
        }
        vimInput.vimBar.showMessage("internal error 1 in interprete()", VimBar.MESSAGE_RED);
        cancelCmdBar();
    }

    /**
	 * Handles keypresses
	 */
    public void keyPress(AxedEvent.KeyPress ev) {
        switch(ev.keycode) {
            case -InputCatcher.KEY_BACKSPACE:
                backspace();
                return;
            case -InputCatcher.KEY_RETURN:
                interprete();
                return;
            case -InputCatcher.KEY_END:
                caretEnd();
                return;
            case -InputCatcher.KEY_POS1:
                caretPos1();
                return;
            case -InputCatcher.KEY_LEFT_ARROW:
                caretLeft();
                return;
            case -InputCatcher.KEY_RIGHT_ARROW:
                caretRight();
                return;
            case -InputCatcher.KEY_DEL:
                del();
                return;
            case -InputCatcher.KEY_ESC:
                cancelCmdBar();
                return;
            default:
                if (ev.keycode > 0) {
                    commandLine.insert(vimInput.vimBar.caretPos, ev.ch);
                    vimInput.vimBar.insertChar(ev.ch);
                }
        }
    }
}
