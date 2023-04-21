package ar.com.AmberSoft.iEvenTask.utils;

import org.hibernate.Query;
import com.extjs.gxt.ui.client.data.BaseFilterConfig;

public abstract class Wrapper {

    protected BaseFilterConfig filter;

    public BaseFilterConfig getFilter() {
        return filter;
    }

    public void setFilter(BaseFilterConfig filter) {
        this.filter = filter;
    }

    public abstract void setValueOnQuery(Query query, int index);

    public abstract void setValueOnQuery(Query query, String index);
}
