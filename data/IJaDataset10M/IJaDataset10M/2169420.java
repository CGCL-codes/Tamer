package dt.basic68.plugin;

import dt.basic68.lexer.Lexer;
import dt.basic68.node.Node;
import dt.basic68.parser.Parser;
import dt.basic68.tool.LineNumbers;
import dt.basic68.tool.Label;
import dt.basic68.tool.PrintWalker;
import java.io.*;
import java.util.*;
import org.gjt.sp.jedit.EditPlugin;
import org.gjt.sp.jedit.EBPlugin;
import org.gjt.sp.jedit.GUIUtilities;
import org.gjt.sp.jedit.EBMessage;
import errorlist.*;

public class Basic68Plugin extends EBPlugin {

    private DefaultErrorSource errorSource;

    public static final String NAME = "basic68";

    /** Trivial private constructor to enforce usage through main(). */
    public Basic68Plugin() {
        super();
    }

    public void createMenuItems(Vector menuItems) {
        menuItems.addElement(GUIUtilities.loadMenu("basic68.menu"));
    }

    public void handleMessage(EBMessage ebMessage) {
        super.handleMessage(ebMessage);
    }

    public void start() {
        errorSource = new DefaultErrorSource("MyPlugin");
        ErrorSource.registerErrorSource(errorSource);
    }

    public void tokenizesrc() {
    }

    public void downloadbc() {
    }

    public void executebc() {
    }

    public void step() {
    }

    public ErrorSource getErrorSource() {
        return errorSource;
    }
}
