package org.openxava.school.model.xejb;

/**
 * CMP layer for Teacher.
 */
public abstract class TeacherCMP extends org.openxava.school.model.xejb.TeacherBean implements javax.ejb.EntityBean {

    public org.openxava.school.model.TeacherData getData() {
        org.openxava.school.model.TeacherData dataHolder = null;
        try {
            dataHolder = new org.openxava.school.model.TeacherData();
            dataHolder.set_Name(get_Name());
            dataHolder.setId(getId());
        } catch (RuntimeException e) {
            throw new javax.ejb.EJBException(e);
        }
        return dataHolder;
    }

    public void setData(org.openxava.school.model.TeacherData dataHolder) {
        try {
            set_Name(dataHolder.get_Name());
        } catch (Exception e) {
            throw new javax.ejb.EJBException(e);
        }
    }

    public void ejbLoad() {
        super.ejbLoad();
    }

    public void ejbStore() {
        super.ejbStore();
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
        TeacherValue = null;
    }

    public void setEntityContext(javax.ejb.EntityContext ctx) {
        super.setEntityContext(ctx);
    }

    public void unsetEntityContext() {
        super.unsetEntityContext();
    }

    public void ejbRemove() throws javax.ejb.RemoveException {
    }

    private org.openxava.school.model.TeacherValue TeacherValue = null;

    public org.openxava.school.model.TeacherValue getTeacherValue() {
        TeacherValue = new org.openxava.school.model.TeacherValue();
        try {
            TeacherValue.setName(getName());
            TeacherValue.setId(getId());
        } catch (Exception e) {
            throw new javax.ejb.EJBException(e);
        }
        return TeacherValue;
    }

    public void setTeacherValue(org.openxava.school.model.TeacherValue valueHolder) {
        try {
            setName(valueHolder.getName());
        } catch (Exception e) {
            throw new javax.ejb.EJBException(e);
        }
    }

    public abstract java.lang.String get_Name();

    public abstract void set_Name(java.lang.String _Name);

    public abstract java.lang.String getId();

    public abstract void setId(java.lang.String id);
}
