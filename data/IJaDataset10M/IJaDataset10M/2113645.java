package dtos;

/**
 * @author generated
 */
public class TestDTO extends blomo.dto.BLoMoDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private java.lang.Integer idRenamed;

    private java.lang.Integer test;

    /**
	 * generated method
	 * @param fullpath undefined
	 * @return undefined
	 */
    public java.lang.String getDtoClassForAttribute(java.lang.String fullpath) {
        java.lang.String firstStep;
        int i = fullpath.indexOf('.');
        if (i == -1) {
            firstStep = fullpath;
            if ("idRenamed".equals(firstStep)) return "java.lang.Integer";
            if ("test".equals(firstStep)) return "java.lang.Integer";
        }
        return null;
    }

    /**
	 * generated method
	 * @param test undefined
	 */
    public void copyFrom(model.Test test) {
        blomo.dto.DTOSession dtoSession = getDtoSession();
        java.util.Map<Object, Object> paras = new java.util.Hashtable<Object, Object>();
        paras.put("dtoSession", dtoSession);
        blomo.filter.FilterManager fm = blomo.filter.FilterManager.instance();
        initialized = Boolean.TRUE;
        setIdRenamed(test.getIdRenamed());
        if (!fm.doFilteredAdd(TestDTO.class, "test", test, paras, this)) setTest(test.getTest());
    }

    /**
	 * generated method
	 * @param idRenamed undefined
	 */
    public void setIdRenamed(java.lang.Integer idRenamed) {
        this.idRenamed = idRenamed;
        dirty = true;
    }

    /**
	 * generated method
	 * @return undefined
	 */
    @Override
    public java.io.Serializable getEntityId() {
        return getIdRenamed();
    }

    /**
	 * generated method
	 * @param path undefined
	 * @param session undefined
	 */
    @Override
    public void synchronize(java.lang.String path, blomo.dto.DTOSession session) {
    }

    /**
	 * generated method
	 * @return undefined
	 */
    @Override
    public java.lang.String getEntityTable() {
        return "test";
    }

    /**
	 * generated method
	 * @param test undefined
	 */
    public void setTest(java.lang.Integer test) {
        this.test = test;
        dirty = true;
    }

    /**
	 * generated method
	 * @return undefined
	 */
    @Override
    public boolean containsTranslation() {
        return false;
    }

    /**
	 * generated method
	 * @param test undefined
	 * @param session undefined
	 */
    public void copyTo(model.Test test, org.hibernate.Session session) {
        blomo.dto.DTOSession dtoSession = getDtoSession();
        java.util.Map<Object, Object> paras = new java.util.Hashtable<Object, Object>();
        paras.put("dtoSession", dtoSession);
        blomo.filter.FilterManager fm = blomo.filter.FilterManager.instance();
        dirty = false;
        if (!fm.doFilteredRemove(TestDTO.class, "test", test, paras)) test.setTest(getTest());
    }

    /**
	 * generated method
	 * @param fullpath undefined
	 * @param value undefined
	 * @param key undefined
	 * @return undefined
	 */
    public boolean addToAttribute(java.lang.String fullpath, Object value, Object key) {
        return false;
    }

    /**
	 * generated method
	 * @param test undefined
	 * @param session undefined
	 */
    public void copyOnlyRelationsTo(model.Test test, org.hibernate.Session session) {
    }

    /**
	 * generated method
	 * @param entity undefined
	 */
    @Override
    public void updateTranslations(Object entity) {
        updateTranslations((model.Test) entity);
    }

    /**
	 * generated method
	 * @param session undefined
	 * @param path undefined
	 */
    @Override
    public void initializeAttribute(org.hibernate.Session session, java.lang.String path) {
        if (path == null) return;
        int i = path.indexOf('.');
        if (i == -1) return;
    }

    /**
	 * generated method
	 * @return undefined
	 */
    public java.lang.Integer getTest() {
        return test;
    }

    /**
	 * generated method
	 * @return undefined
	 */
    public java.lang.Integer getIdRenamed() {
        return idRenamed;
    }

    /**
	 * generated method
	 * @param fullpath undefined
	 * @return undefined
	 */
    public Object getAttribute(java.lang.String fullpath) {
        java.lang.String firstStep;
        int i = fullpath.indexOf('.');
        if (i == -1) {
            firstStep = fullpath;
            if ("idRenamed".equals(firstStep)) return getIdRenamed();
            if ("test".equals(firstStep)) return getTest();
        }
        return null;
    }

    /**
	 * generated method
	 * @param id undefined
	 */
    public void setEntityId(java.io.Serializable id) {
        setIdRenamed((java.lang.Integer) id);
    }

    /**
	 * generated method
	 * @param test undefined
	 */
    public void updateTranslations(model.Test test) {
    }

    /**
	 * generated method
	 * @param o undefined
	 * @param session undefined
	 */
    public void copyTo(Object o, org.hibernate.Session session) {
        copyTo((model.Test) o, session);
    }

    /**
	 * generated method
	 * @param fullpath undefined
	 * @param idValue undefined
	 * @param idKey undefined
	 * @return undefined
	 */
    public boolean removeFromAttribute(java.lang.String fullpath, Object idValue, Object idKey) {
        return false;
    }

    /**
	 * generated method
	 * @param o undefined
	 * @param session undefined
	 */
    public void copyOnlyRelationsTo(Object o, org.hibernate.Session session) {
        copyOnlyRelationsTo((model.Test) o, session);
    }

    /**
	 * generated method
	 * @return undefined
	 */
    public Class<?> getEntityClass() {
        return model.Test.class;
    }

    /**
	 * generated method
	 * @return undefined
	 */
    @Override
    public Class<? extends blomo.dto.BLoMoDTO> getDtoClass() {
        return TestDTO.class;
    }

    /**
	 * generated method
	 * @param o undefined
	 */
    public void copyFrom(Object o) {
        copyFrom((model.Test) o);
    }
}
