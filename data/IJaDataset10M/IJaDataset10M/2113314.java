package types;

public abstract class MJType {

    public abstract String GetType();

    public abstract MJType SearchType(String name);

    public abstract boolean CheckType(MJType n_type);
}
