package org.piuframework.service.test.pojo;

import java.io.Serializable;
import org.piuframework.service.ServiceException;
import org.piuframework.service.test.TestService;

/**
 * TODO
 * 
 * @author Dirk Mascher
 */
public class TestServiceImpl implements TestService, Serializable {

    private static final long serialVersionUID = 1L;

    private int dummyInt = 0;

    private String dummyString = "default";

    public TestServiceImpl() {
        super();
    }

    public String test() {
        return "test";
    }

    public int getDummyInt() {
        return dummyInt;
    }

    public String getDummyString() {
        return dummyString;
    }

    public void setDummyInt(int i) {
        this.dummyInt = i;
    }

    public void setDummyString(String s) {
        this.dummyString = s;
    }

    public void setDummyString(String s, String exceptionClassName) throws Exception {
        this.dummyString = s;
        Exception exception = null;
        try {
            Class exceptionClass = Class.forName(exceptionClassName);
            exception = (Exception) exceptionClass.newInstance();
        } catch (Throwable t) {
            throw new ServiceException(t);
        }
        throw exception;
    }
}
