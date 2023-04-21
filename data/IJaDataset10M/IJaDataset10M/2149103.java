package net.sourceforge.hlm.gui.generic;

public interface ObjectSelectionListener<T> {

    void objectSelected(T object, boolean open, boolean inserted);
}
