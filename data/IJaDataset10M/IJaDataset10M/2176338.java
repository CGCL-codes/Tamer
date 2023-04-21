package droid.lang.invoke;

public abstract class CallSite {

    public abstract MethodHandle dynamicInvoker();

    public abstract MethodHandle getTarget();

    public abstract void setTarget(MethodHandle newTarget);

    public MethodType type() {
        throw new Error("UnimplementedCodeError");
    }
}
