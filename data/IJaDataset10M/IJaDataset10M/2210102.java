package net.jfipa.http;

public interface HttpObjectI {

    public void clear();

    public String toString();

    public void setEmpty();

    public void setNotEmpty();

    public void parse(HttpParserA parser);
}
