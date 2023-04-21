package ru.amse.tsyganov.jumleditor.view;

import java.lang.ref.SoftReference;
import ru.amse.tsyganov.jumleditor.model.Model;

public abstract class View<T extends Model> {

    private SoftReference<T> model;

    public final T getModel() {
        assert model.get() != null;
        return model.get();
    }

    public final void setModel(T model) {
        if (model == null) {
            throw new IllegalArgumentException();
        }
        this.model = new SoftReference<T>(model);
    }
}
