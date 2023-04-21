package net.sf.brightside.xlibrary.tapestry.util;

import java.util.ArrayList;
import java.util.List;
import net.sf.brightside.xlibrary.metamodel.XBook;
import org.apache.tapestry.OptionGroupModel;
import org.apache.tapestry.OptionModel;
import org.apache.tapestry.util.AbstractSelectModel;

public class XBookSelectModel extends AbstractSelectModel {

    private List<XBook> bookList;

    public XBookSelectModel(List<XBook> bookList) {
        this.bookList = bookList;
    }

    @Override
    public List<OptionGroupModel> getOptionGroups() {
        return null;
    }

    @Override
    public List<OptionModel> getOptions() {
        List<OptionModel> list = new ArrayList<OptionModel>();
        for (XBook a : bookList) {
            list.add(new XBookOptionModel(a));
        }
        return list;
    }
}
