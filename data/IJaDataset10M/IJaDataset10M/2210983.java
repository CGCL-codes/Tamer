package content;

import module.DistributionObject;

public abstract class BaseContent<T> extends DistributionObject {

    private static final long serialVersionUID = 1L;

    protected transient T data;

    protected abstract void Init();

    public T GetData() {
        if (data == null) Init();
        return data;
    }
}
