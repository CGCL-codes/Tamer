package dms.core.document.doctype.logic;

import dms.core.logic.BizModel;

public interface DocType extends BizModel {

    public static final String FLD_NAME = "name";

    public static final String FLD_DESCR = "descr";

    public Long getId();

    public void setId(Long id);

    public abstract String getName();

    public abstract void setName(String name);

    public abstract String getDescr();

    public abstract void setDescr(String descr);
}
