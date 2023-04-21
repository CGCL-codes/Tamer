package index;

import compiler.absyn.Constant;
import storage.record.RID;

public interface Index {

    public void beforeFirst(Constant searchkey);

    public boolean next();

    public RID getDataRid();

    public void insert(Constant dataval, RID rid);

    public void delete(Constant dataval, RID rid);

    public void close();
}
