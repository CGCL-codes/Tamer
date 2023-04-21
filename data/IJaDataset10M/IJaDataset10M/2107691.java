package com.jxml.regexp;

import org.apache.regexp.*;

public class REContainer {

    public String name = "";

    public RE re = null;

    public RE getRE() throws RESyntaxException {
        if (re == null) re = new RE(name);
        return re;
    }

    public void setRE(RE re) {
        this.re = re;
    }
}
