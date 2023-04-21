package org.silicolife.humanrelations.data;

import org.silicolife.data.SLPojo;
import org.silicolife.exceptions.SystemSLException;
import org.silicolife.exceptions.UserSLException;

public class HumanPanelPojo extends SLPojo {

    private String name;

    private int x;

    private int y;

    public HumanPanelPojo(String key, String name, int x, int y) throws SystemSLException {
        super(key);
        setName(name);
        setX(x);
        setY(y);
    }

    public String getName() throws SystemSLException, UserSLException {
        return name;
    }

    public String getDescription() throws SystemSLException, UserSLException {
        return getKey();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String toXMLTag() {
        return "HumanPanel";
    }
}
