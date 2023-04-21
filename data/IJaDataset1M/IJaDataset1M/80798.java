package pms.common.value;

public class ThemeValue {

    private int themeId;

    private int projectId;

    private String themeName;

    private int themeStatus;

    private int themeProgress;

    private String themeStartDt;

    private String themeInfo;

    private String themeEndDt;

    private int AddUserId;

    private String AddTs;

    private int UpdUserId;

    private String UpdTs;

    private int delUserId;

    private String delTs;

    private int mode = 0;

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getAddTs() {
        return AddTs;
    }

    public void setAddTs(String addTs) {
        AddTs = addTs;
    }

    public int getUpdUserId() {
        return UpdUserId;
    }

    public void setUpdUserId(int updUserId) {
        UpdUserId = updUserId;
    }

    public String getUpdTs() {
        return UpdTs;
    }

    public void setUpdTs(String updTs) {
        UpdTs = updTs;
    }

    public int getDelUserId() {
        return delUserId;
    }

    public void setDelUserId(int delUserId) {
        this.delUserId = delUserId;
    }

    public String getDelTs() {
        return delTs;
    }

    public void setDelTs(String delTs) {
        this.delTs = delTs;
    }

    public int getAddUserId() {
        return AddUserId;
    }

    public void setAddUserId(int addUserId) {
        AddUserId = addUserId;
    }

    public String getThemeInfo() {
        return themeInfo;
    }

    public void setThemeInfo(String themeInfo) {
        this.themeInfo = themeInfo;
    }

    private int editFlag = 0;

    public int getEditFlag() {
        return editFlag;
    }

    public void setEditFlag(int editFlag) {
        this.editFlag = editFlag;
    }

    public String getThemeStartDt() {
        return themeStartDt;
    }

    public void setThemeStartDt(String themeStartDt) {
        this.themeStartDt = themeStartDt;
    }

    public String getThemeEndDt() {
        return themeEndDt;
    }

    public void setThemeEndDt(String themeEndDt) {
        this.themeEndDt = themeEndDt;
    }

    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public int getThemeStatus() {
        return themeStatus;
    }

    public void setThemeStatus(int themeStatus) {
        this.themeStatus = themeStatus;
    }

    public int getThemeProgress() {
        return themeProgress;
    }

    public void setThemeProgress(int themeProgress) {
        this.themeProgress = themeProgress;
    }
}
