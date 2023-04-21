package cc.w3d.jawos.jinn.xjawosdata.xjawosdata.engine.structure;

import cc.w3d.jawos.jinn.xdata.xdata.engine.structure.QueryResult;

public interface JawosQueryResult extends QueryResult {

    public JawosRowData next();
}
