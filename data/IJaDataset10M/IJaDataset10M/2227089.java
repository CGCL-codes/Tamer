package jp.narr.reader;

public interface DocumentHandler {

    public String getFileMimeType();

    public String getFilePrettifyClass();

    public String getFileFormattedString(String fileString);

    public String getFileScriptFiles();
}
