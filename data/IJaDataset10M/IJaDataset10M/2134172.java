package org.yafra.wicket.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.apache.cayenne.access.DataContext;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.yafra.model.MYafraUser;
import org.yafra.modelhandler.MHYafraUser;
import org.yafra.utils.Logging;

/**
 * description
 * 
 * @author mwn
 * @version
 * @since 1.0
 */
public class YafraUserDP extends SortableDataProvider {

    class SortableDataProviderComparator implements Comparator<MYafraUser>, Serializable {

        public int compare(final MYafraUser o1, final MYafraUser o2) {
            PropertyModel<Comparable> model1 = new PropertyModel<Comparable>(o1, getSort().getProperty());
            PropertyModel<Comparable> model2 = new PropertyModel<Comparable>(o2, getSort().getProperty());
            int result = model1.getObject().compareTo(model2.getObject());
            if (!getSort().isAscending()) {
                result = -result;
            }
            return result;
        }
    }

    private IModel<List<MYafraUser>> list;

    private SortableDataProviderComparator comparator = new SortableDataProviderComparator();

    public YafraUserDP() {
        this.setSort("name", SortOrder.ASCENDING);
        list = new YafraUserDM();
    }

    public Iterator<MYafraUser> iterator(final int first, final int count) {
        List<MYafraUser> newList = new ArrayList<MYafraUser>(list.getObject());
        Collections.sort(newList, comparator);
        return newList.subList(first, first + count).iterator();
    }

    public int size() {
        return list.getObject().size();
    }

    /**
	 * @see org.apache.wicket.markup.repeater.data.IDataProvider#model(java.lang.Object)
	 */
    @Override
    public IModel<MYafraUser> model(final Object object) {
        return new AbstractReadOnlyModel<MYafraUser>() {

            @Override
            public MYafraUser getObject() {
                return (MYafraUser) object;
            }
        };
    }
}
