package sm.ejb;

/**
 *
 * @author LeilibPC
 */
public class StaffInformation {

    private int departmentID;

    private int workType;

    private int workerLevel;

    private String name;

    private String sex;

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(int departmentID) {
        this.departmentID = departmentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getWorkType() {
        return workType;
    }

    public void setWorkType(int workType) {
        this.workType = workType;
    }

    public int getWorkerLevel() {
        return workerLevel;
    }

    public void setWorkerLevel(int workerLevel) {
        this.workerLevel = workerLevel;
    }
}
