package com.antlersoft.query;

public class Exists extends CountPreservingFilter {

    public Exists(Transform transform) {
        m_transform = transform;
    }

    protected boolean getCountPreservingFilterValue(DataSource source, Object to_transform) {
        m_transform.startEvaluation(source);
        boolean result = m_transform.transformObject(source, to_transform).hasMoreElements();
        return m_transform.finishEvaluation(source).hasMoreElements() || result;
    }

    public Class appliesClass() {
        return m_transform.appliesClass();
    }

    public void lateBindApplies(Class new_applies) throws BindException {
        m_transform.lateBindApplies(new_applies);
    }

    private Transform m_transform;
}
