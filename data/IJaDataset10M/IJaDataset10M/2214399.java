package fi.vtt.noen.mfw.mbt.log;

/**
 * @author Teemu Kanstren
 */
public class LogComponent {

    protected int depth = 0;

    protected String start = "";

    public String indent() {
        String indent = "    ";
        for (int i = 0; i < depth; i++) {
            indent += "  ";
        }
        return indent;
    }
}
