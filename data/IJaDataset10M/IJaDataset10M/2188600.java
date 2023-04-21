package org.ufacekit.ui.viewers;

import java.util.List;

public interface ISelection<ModelElement> extends Iterable<ModelElement> {

    public List<ModelElement> getElements();

    public boolean isEmpty();
}
