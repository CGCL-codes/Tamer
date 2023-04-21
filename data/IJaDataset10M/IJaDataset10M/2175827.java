package parserVersions;

public abstract class SpreeParser {

    protected String version, body, method, tableName;

    protected int errorCode;

    protected boolean error;

    public SpreeParser() {
        reset();
    }

    public abstract void parseClientMessage(String message);

    public abstract void parseServerMessage(String message);

    public String getVersion() {
        return version;
    }

    public String getMessageBody() {
        return body;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public boolean hasError() {
        return error;
    }

    public String getClientMethod() {
        return method;
    }

    public String getTableName() {
        return tableName;
    }

    protected void reset() {
        error = false;
        errorCode = 0;
        version = "";
        body = "";
        method = "";
        tableName = "";
    }
}
