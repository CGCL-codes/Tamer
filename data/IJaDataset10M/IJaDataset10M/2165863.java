package org.agile.dfs.rpc.serialize;

import java.io.InputStream;
import java.io.Reader;

public interface RpcDeSerializer {

    public Object read(InputStream in);

    public Object read(Reader in);

    public Object read(String s);
}
