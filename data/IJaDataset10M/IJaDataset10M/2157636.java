package com.anaxima.eslink.scl;

/**
 * Class for holding errors an warnings resulting from a compile.
 * 
 * @author Thomas Vater
 */
public class CompileResultEntry {

    /**
     * Line of the entry.
     */
    private int _line;

    /**
     * Type of the entry.
     */
    private int _type;

    /**
     * Code of the entry.
     */
    private String _code;

    /**
     * Text of the entry.
     */
    private String _text;

    /**
     * Constructor with all parameters.
     * @param argType
     * @param argLine
     * @param argCode
     * @param argText
     */
    public CompileResultEntry(int argType, int argLine, String argCode, String argText) {
        _type = argType;
        _line = argLine;
        _code = argCode;
        _text = argText;
    }

    /**
     * Constructor without code.
     * @param argType
     * @param argLine
     * @param argText
     */
    public CompileResultEntry(int argType, int argLine, String argText) {
        _type = argType;
        _line = argLine;
        _code = "";
        _text = argText;
    }

    /**
     * @return Returns the code.
     */
    public String getCode() {
        return _code;
    }

    /**
     * @param argCode The code to set.
     */
    public void setCode(String argCode) {
        _code = argCode;
    }

    /**
     * @return Returns the line.
     */
    public int getLine() {
        return _line;
    }

    /**
     * @param argLine The line to set.
     */
    public void setLine(int argLine) {
        _line = argLine;
    }

    /**
     * @return Returns the text.
     */
    public String getText() {
        return _text;
    }

    /**
     * @param argText The text to set.
     */
    public void setText(String argText) {
        _text = argText;
    }

    /**
     * @return Returns the type.
     * @see ISclCompileListener
     */
    public int getType() {
        return _type;
    }

    /**
     * @param argType The type to set.
     * @see ISclCompileListener
     */
    public void setType(int argType) {
        _type = argType;
    }
}
