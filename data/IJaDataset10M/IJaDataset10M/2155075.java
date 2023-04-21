package failure.core.access;

public class PlayerVo {

    private String[] unitsIds;

    private String name;

    private String password;

    public PlayerVo(String name) {
        this.name = name;
    }

    public String[] getUnitsIds() {
        return unitsIds;
    }

    public void setUnitsIds(String[] unitsIds) {
        this.unitsIds = unitsIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
