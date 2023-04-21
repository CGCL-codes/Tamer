package CheckPossibleEjbMethods.a;

public abstract class MainBean implements javax.ejb.EntityBean {

    public String ejbCreateSome() {
        return null;
    }

    public String ejbPostCreateSome() {
        return null;
    }

    public String ejbHomeSome() {
        return null;
    }

    public Object ejbFindAll() {
        return null;
    }

    public Object ejbSelect() {
        return null;
    }

    public void remoteBussinessMethod() {
    }

    public void localBussinessMethod() {
    }

    /**
   * @audit MinimizeAccessViolation
   */
    public void notBussinessMethod() {
    }

    public abstract java.lang.String getId();

    public abstract void setId(java.lang.String args);

    public String toString() {
        return null;
    }
}
