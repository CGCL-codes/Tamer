package project.cn.dataType;

public class DAdminRequest extends DRequest {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3472181229943741248L;

    private DContactPerson targetperson;

    public void setTargetperson(DContactPerson targetperson) {
        this.targetperson = targetperson;
    }

    public DContactPerson getTargetperson() {
        return targetperson;
    }
}
