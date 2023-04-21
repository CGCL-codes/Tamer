package net.sf.opengroove.realmserver.data.model;

public class ComputerSetting {

    private String name;

    private String username;

    private String computername;

    private String value;

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getComputername() {
        return computername;
    }

    public String getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setComputername(String computername) {
        this.computername = computername;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
