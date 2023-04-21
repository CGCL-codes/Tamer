package ca.tatham.scheduler;

class Resource implements Prioritizable {

    private final Object m_resource;

    private int m_ceiling;

    private Task m_owner;

    Resource(Object r, int c) {
        m_resource = r;
        m_ceiling = c;
    }

    public int getPriority() {
        return m_ceiling;
    }

    public int getCeiling() {
        return m_ceiling;
    }

    public void setCeiling(int priority) {
        m_ceiling = priority;
    }

    public Task getOwner() {
        return m_owner;
    }

    public void setOwner(Task task) {
        m_owner = task;
    }

    public Object getResource() {
        return m_resource;
    }

    @Override
    public String toString() {
        return m_resource.toString();
    }
}
