package cn.ac.ntarl.umt.app;

public class VO {

    private String assingedGroup;

    private int id;

    private String name;

    private String displayName;

    private String desc;

    private int rigisterNeedAuthen = 1;

    private int rigisterShowVOProperty = 0;

    public void setAssignedGroup(String assingedGroup) {
        this.assingedGroup = assingedGroup;
    }

    public String getAssignedGroup() {
        return assingedGroup;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getRigisterNeedAuthen() {
        return rigisterNeedAuthen;
    }

    public void setRigisterNeedAuthen(int rigisterNeedAuthen) {
        this.rigisterNeedAuthen = rigisterNeedAuthen;
    }

    public int getRigisterShowVOProperty() {
        return rigisterShowVOProperty;
    }

    public void setRigisterShowVOProperty(int rigisterShowVOProperty) {
        this.rigisterShowVOProperty = rigisterShowVOProperty;
    }
}
