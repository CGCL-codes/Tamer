package annone.local.linker;

import annone.util.AnnoneException;
import annone.util.Const;
import annone.util.Tools;

public class ScriptException extends AnnoneException {

    private static final long serialVersionUID = 8494991056487989609L;

    private final String context;

    private final String source;

    private final int column;

    private final int line;

    public ScriptException(String message, String context, String source, int line, int column) {
        super(message);
        this.context = context;
        this.source = source;
        this.line = line;
        this.column = column;
    }

    public String getContext() {
        return context;
    }

    public String getSource() {
        return source;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String getMessage() {
        String c = String.valueOf(line);
        return super.getMessage() + Const.LINE_SEPARATOR + context + Const.LINE_SEPARATOR + c + ":" + source + Const.LINE_SEPARATOR + Tools.stringOf(' ', c.length()) + ":" + Tools.stringOf(' ', column) + "^";
    }
}
