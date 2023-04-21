package org.lazaro.jirc.irc;

/**
 * @author Lazaro Brito
 * 
 */
public class User extends Target {

    private String realName = null;

    private String prefix = null;

    public User(String name) {
        super(name);
        if (super.name.startsWith("@")) {
            prefix = name.substring(0, 1);
            super.name = name.substring(1);
        }
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
