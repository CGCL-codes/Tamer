package org.piuframework.service.test;

/**
 * TODO
 * 
 * @author Dirk Mascher
 */
public interface TestService {

    public String test();

    public int getDummyInt();

    public String getDummyString();

    public void setDummyInt(int i);

    public void setDummyString(String s);

    public void setDummyString(String s, String exceptionClassName) throws Exception;
}
