package clear.data;

import java.util.Collection;
import java.util.List;

public interface IDataBean {

    public Collection fill(List fp);

    public List sync(List changes);

    public List executeTransactionBatch(List batch) throws Throwable;
}
