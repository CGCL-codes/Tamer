package net.jadoth.collections.types;

public interface XAddingBag<E> extends XAddingCollection<E> {

    public interface Factory<E> extends XAddingCollection.Factory<E> {

        @Override
        public XAddingBag<E> newInstance();
    }

    @Override
    public XAddingCollection<E> add(E... elements);

    @Override
    public XAddingCollection<E> addAll(E[] elements, int srcStartIndex, int srcLength);

    @Override
    public XAddingCollection<E> addAll(XGettingCollection<? extends E> elements);
}
