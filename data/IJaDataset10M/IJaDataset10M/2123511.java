package de.grobmeier.jjson.convert;

import de.grobmeier.jjson.convert.JSON;

@JSON
public class AnnotatedNestedSetClass {

    private String mystring = null;

    /**
     * @return the mystring
     */
    public String getMystring() {
        return mystring;
    }

    /**
     * @param mystring the mystring to set
     */
    public void setMystring(String mystring) {
        this.mystring = mystring;
    }
}
