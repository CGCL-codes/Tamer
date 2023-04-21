package org.codemon.tool;

import java.io.Serializable;

public class MetaInfo implements Serializable {

    private int number;

    private String fileName;

    private String functionName;

    private int beginLine;

    private int beginColumn;

    private int endLine;

    private int endColumn;

    private String type;

    private int beginStream;

    private int endStream;

    public int getBeginStream() {
        return beginStream;
    }

    public void setBeginStream(int beginStream) {
        this.beginStream = beginStream;
    }

    public void setEndStream(int endStream) {
        this.endStream = endStream;
    }

    public int getEndStream() {
        return endStream;
    }

    public int getBeginColumn() {
        return beginColumn;
    }

    public void setBeginColumn(int beginColumn) {
        this.beginColumn = beginColumn;
    }

    public int getBeginLine() {
        return beginLine;
    }

    public void setBeginLine(int beginLine) {
        this.beginLine = beginLine;
    }

    public int getEndColumn() {
        return endColumn;
    }

    public void setEndColumn(int endColumn) {
        this.endColumn = endColumn;
    }

    public int getEndLine() {
        return endLine;
    }

    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void printMetaInfo() {
        System.out.println("number: " + getNumber());
        System.out.println("filePath: " + getFileName());
        System.out.println("typer: " + getType());
        System.out.println("begin stream: " + String.valueOf(getBeginStream()));
        System.out.println("--------------------------");
    }
}
