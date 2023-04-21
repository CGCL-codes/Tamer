package test.hibernate20;

/**
* @hibernate.joined-subclass table="workreqerror"
* @hibernate.joined-subclass-key column="oid"
*
*/
public class WorkRequestError extends Error {

    protected String workRequest;

    /**
* @hibernate.many-to-one column="workrequestoid"
*
* @return
*/
    public String getWorkRequest() {
        return this.workRequest;
    }

    public void setWorkRequest(String wreq) {
        this.workRequest = wreq;
    }
}
