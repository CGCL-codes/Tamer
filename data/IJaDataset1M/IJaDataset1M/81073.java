package net.jadoth.collections.types;

import net.jadoth.lang.functional.Procedure;
import net.jadoth.lang.functional.controlflow.LoopProcedure;

/**
 * @author Thomas M�nz
 *
 */
public interface XSet<E> extends XCollection<E>, XPutGetSet<E>, XProcessingSet<E> {

    public interface Factory<E> extends XCollection.Factory<E>, XPutGetSet.Factory<E>, XProcessingSet.Factory<E> {

        @Override
        public XSet<E> newInstance();
    }

    @Override
    public XSet<E> put(E... elements);

    @Override
    public XSet<E> putAll(E[] elements, int srcStartIndex, int srcLength);

    @Override
    public XSet<E> putAll(XGettingCollection<? extends E> elements);

    @Override
    public XSet<E> add(E... elements);

    @Override
    public XSet<E> addAll(E[] elements, int srcStartIndex, int srcLength);

    @Override
    public XSet<E> addAll(XGettingCollection<? extends E> elements);

    @Override
    public XSet<E> copy();

    @Override
    public XSet<E> execute(Procedure<? super E> procedure);

    @Override
    public XSet<E> execute(LoopProcedure<? super E> procedure);

    @Override
    public XSet<E> process(Procedure<? super E> procedure);

    @Override
    public XSet<E> process(LoopProcedure<? super E> procedure);
}
