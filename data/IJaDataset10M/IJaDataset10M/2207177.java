package org.robotframework.javalib.keyword;

public interface DocumentedKeyword extends Keyword {

    String getDocumentation();

    String[] getArgumentNames();
}
