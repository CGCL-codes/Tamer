package net.cioppino.fita.core.model;

import java.util.ArrayList;
import java.util.List;

public class TrainingBookList extends ParentObject {

    private List<TrainingBook> trainingBookList;

    public TrainingBookList() {
        trainingBookList = new ArrayList<TrainingBook>();
    }

    @Override
    public boolean contains(ModelObject modelObject) {
        return trainingBookList.contains(modelObject);
    }

    @Override
    public Object[] getChildren() {
        return trainingBookList.toArray();
    }

    @Override
    public boolean hasChildren() {
        return (trainingBookList != null) ? (trainingBookList.size() > 0) : false;
    }

    public boolean add(TrainingBook trainingBook) {
        if (!contains(trainingBook)) {
            trainingBookList.add(trainingBook);
            firePropertyChange("removechild", trainingBook, null);
            setDirty(true);
            return true;
        }
        return false;
    }

    public boolean remove(TrainingBook trainingBook) {
        if (contains(trainingBook)) {
            trainingBookList.remove(trainingBook);
            firePropertyChange("removechild", trainingBook, null);
            setDirty(true);
            return true;
        }
        return false;
    }
}
