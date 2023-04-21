package org.apache.myfaces.trinidaddemo;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.apache.myfaces.trinidad.context.RequestContext;

public class DataBean implements java.io.Serializable {

    public DataBean() {
        _int = _sCount++;
        _string = "String " + _int;
        _boolean = ((_int % 2) == 0);
    }

    public String action() {
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage message = new FacesMessage("CLICKED ON ROW " + _int + ", " + _string);
        context.addMessage(null, message);
        return null;
    }

    public String showDetail() {
        RequestContext.getCurrentInstance().getPageFlowScope().put("detail", this);
        return "showDetail";
    }

    public boolean getBoolean() {
        return _boolean;
    }

    public void setBoolean(boolean aBoolean) {
        _boolean = aBoolean;
    }

    public int getInt() {
        return _int;
    }

    public void setInt(int anInt) {
        _int = anInt;
    }

    public String getString() {
        return _string;
    }

    public void setString(String aString) {
        _string = aString;
    }

    private int _int;

    private boolean _boolean;

    private String _string;

    private static int _sCount = 0;
}
