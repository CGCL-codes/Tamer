package br.usp.pulga.hiperboloide;

import java.util.ArrayList;
import java.util.List;

public class MultipleIteration<T extends MathIterable> implements MathIterable {

    private final int size;

    private final T obj;

    private List<ChangeProcess<T>> listeners;

    public MultipleIteration(int size, T obj) {
        this.size = size;
        this.obj = obj;
        this.listeners = new ArrayList<ChangeProcess<T>>();
    }

    public void iterate(int qtd) {
        for (int i = 0; i < size; i++) {
            obj.iterate(qtd);
        }
    }

    public void addListener(ChangeProcess<T> listener) {
        this.listeners.add(listener);
    }
}
