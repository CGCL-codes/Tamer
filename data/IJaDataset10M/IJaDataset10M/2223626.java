package net.homelinux.chaoswg.io.makejavauseful.higherorderfunctions;

import java.util.concurrent.Callable;

public interface ConstantFunction<R> extends NAryFunction<R> {

    public R apply();

    public Callable<R> toCallable();
}
