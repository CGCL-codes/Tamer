package marten.age.graphics;

import java.util.ArrayList;
import java.util.HashMap;

public class BasicSceneGraphBranch<T extends SceneGraphChild> extends BasicSceneGraphChild implements SceneGraphBranch<T> {

    private ArrayList<T> branches = new ArrayList<T>();

    private HashMap<String, T> lookup = new HashMap<String, T>();

    @Override
    public void addChild(T newBranch) {
        newBranch.setRoot(this);
        this.branches.add(newBranch);
    }

    @Override
    public void addChild(T newBranch, int index) {
        newBranch.setRoot(this);
        this.branches.add(index, newBranch);
    }

    @Override
    public void updateChild(T newBranch, int index) {
        String id = newBranch.getId();
        if (id == null) {
            throw new RuntimeException("Child id can not be null");
        }
        if (this.lookup.containsKey(id)) {
            this.removeChild(lookup.get(id));
        }
        if (index != -1) {
            this.addChild(newBranch, index);
        } else {
            this.addChild(newBranch);
        }
        this.lookup.put(id, newBranch);
    }

    @Override
    public ArrayList<T> getBranches() {
        return this.branches;
    }

    @Override
    public void removeChild(T oldBranch) {
        this.branches.remove(oldBranch);
    }

    @Override
    public boolean hasChild(T child) {
        return branches.contains(child);
    }

    @Override
    public void removeChildren() {
        branches.clear();
    }

    @Override
    public void render() {
        if (!this.isHidden()) {
            activateChildren();
        }
    }

    protected void activateChildren() {
        for (T branch : this.branches) branch.render();
    }
}
