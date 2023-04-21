package br.com.touchtec.mockspi;

import static com.google.common.base.Preconditions.checkNotNull;

@Untested
public class ThrowerAdapter<E extends Exception> implements Runnable {

    private final Thrower<E> thrower;

    private final Class<E> clazz;

    public ThrowerAdapter(Thrower<E> thrower, Class<E> clazz) {
        this.thrower = checkNotNull(thrower);
        this.clazz = checkNotNull(clazz);
    }

    @Override
    public void run() {
        try {
            thrower.run();
        } catch (Exception e) {
            if (clazz.isInstance(e)) {
                throw new RuntimeWrapperException(clazz.cast(e), clazz);
            } else if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else {
                throw new IllegalStateException(e);
            }
        }
    }

    public static class Factory {

        public <S extends Exception> ThrowerAdapter<S> create(Thrower<S> thrower, Class<S> clazz) {
            checkNotNull(thrower);
            checkNotNull(clazz);
            return new ThrowerAdapter<S>(thrower, clazz);
        }
    }
}
