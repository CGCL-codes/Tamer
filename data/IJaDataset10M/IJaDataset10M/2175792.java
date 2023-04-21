package corner.orm.tapestry.component.propertyselection;

import org.apache.tapestry.form.IPropertySelectionModel;

/**
 * 一个对枚举类型进行选择的model
 * @author <a href="mailto:xf@bjmaxinfo.com">xiafei</a>
 * @version $Revision$
 * @since 2.5.1
 */
public class EnumSelectionModel<T extends Enum> implements IPropertySelectionModel {

    private T[] list;

    public EnumSelectionModel(T[] list) {
        this.list = list;
    }

    public String getLabel(int index) {
        return list[index].toString();
    }

    public Object getOption(int index) {
        return list[index];
    }

    public int getOptionCount() {
        return list.length;
    }

    public String getValue(int index) {
        return list[index].toString();
    }

    public Object translateValue(String value) {
        if (list.length > 0) {
            return Enum.valueOf(list[0].getClass(), value);
        }
        return value;
    }

    public boolean isDisabled(int arg0) {
        return false;
    }
}
