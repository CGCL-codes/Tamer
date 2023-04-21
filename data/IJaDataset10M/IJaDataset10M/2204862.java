package model.task;

public class Root extends TypeTask {

    public Root() {
    }

    public Task createTask() {
        return new Task(this);
    }

    public Task getFather() {
        return null;
    }

    void setFather(Task father) {
    }

    public boolean isRoot() {
        return true;
    }
}
