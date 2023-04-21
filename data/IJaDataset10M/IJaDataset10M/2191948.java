package net.sf.parser4j.parser.entity.parsenode.data;

/**
 * 
 * @author luc peuvrier
 * 
 */
public abstract class AbstractParserNodeData implements IParseNodeData {

    protected String fileName;

    protected int beginLineNumber;

    protected int beginColumnNumber;

    protected int endLineNumber;

    protected int endColumnNumber;

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    @Override
    public int getBeginLineNumber() {
        return beginLineNumber;
    }

    @Override
    public void setBeginLineNumber(final int beginLineNumber) {
        this.beginLineNumber = beginLineNumber;
    }

    @Override
    public int getBeginColumnNumber() {
        return beginColumnNumber;
    }

    @Override
    public void setBeginColumnNumber(final int beginColumnNumber) {
        this.beginColumnNumber = beginColumnNumber;
    }

    @Override
    public int getEndLineNumber() {
        return endLineNumber;
    }

    @Override
    public void setEndLineNumber(final int endLineNumber) {
        this.endLineNumber = endLineNumber;
    }

    @Override
    public int getEndColumnNumber() {
        return endColumnNumber;
    }

    public void setEndColumnNumber(final int endColumnNumber) {
        this.endColumnNumber = endColumnNumber;
    }
}
