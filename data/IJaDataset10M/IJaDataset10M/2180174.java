package quantri.xemLog;

public class Table {

    private String stt;

    private String userName;

    private String nameColumn;

    private String valueBefore;

    private String valueAfter;

    private String timeTask;

    private String SoHC_Madoan;

    private String task;

    private String reson;

    public Table() {
    }

    public Table(String stt, String userName, String nameColumn, String valueBefore, String valueAfter, String timeTask, String soHC_Madoan, String task, String reson) {
        this.stt = stt;
        this.userName = userName;
        this.nameColumn = nameColumn;
        this.valueBefore = valueBefore;
        this.valueAfter = valueAfter;
        this.timeTask = timeTask;
        SoHC_Madoan = soHC_Madoan;
        this.task = task;
        this.reson = reson;
    }

    public String getStt() {
        return stt;
    }

    public void setStt(String stt) {
        this.stt = stt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNameColumn() {
        return nameColumn;
    }

    public void setNameColumn(String nameColumn) {
        this.nameColumn = nameColumn;
    }

    public String getValueBefore() {
        return valueBefore;
    }

    public void setValueBefore(String valueBefore) {
        this.valueBefore = valueBefore;
    }

    public String getValueAfter() {
        return valueAfter;
    }

    public void setValueAfter(String valueAfter) {
        this.valueAfter = valueAfter;
    }

    public String getTimeTask() {
        return timeTask;
    }

    public void setTimeTask(String timeTask) {
        this.timeTask = timeTask;
    }

    public String getSoHC_Madoan() {
        return SoHC_Madoan;
    }

    public void setSoHC_Madoan(String soHC_Madoan) {
        SoHC_Madoan = soHC_Madoan;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getReson() {
        return reson;
    }

    public void setReson(String reson) {
        this.reson = reson;
    }
}
