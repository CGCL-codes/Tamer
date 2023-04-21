package model.task;

public abstract class TypeTask {

    private Long id;

    public abstract Task createTask();

    public abstract Task getFather();

    abstract void setFather(Task father);

    public abstract boolean isRoot();

    private Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }
}
