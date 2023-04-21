package net.sf.brightside.gymcalendar.tapestry.utils;

import java.util.ArrayList;
import java.util.List;
import net.sf.brightside.gymcalendar.core.beans.Identifiable;
import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.util.AbstractSelectModel;

public class BeanSelectModel extends AbstractSelectModel {

    private List<Identifiable> list;

    public BeanSelectModel(List<Identifiable> list) {
        super();
        this.list = list;
    }

    @Override
    public List<OptionGroupModel> getOptionGroups() {
        return null;
    }

    @Override
    public List<OptionModel> getOptions() {
        List<OptionModel> l = new ArrayList<OptionModel>();
        for (Identifiable bean : list) {
            l.add(new BeanOptionModel(bean));
        }
        return l;
    }
}
