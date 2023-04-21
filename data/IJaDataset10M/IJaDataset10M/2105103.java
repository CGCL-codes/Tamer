package forms.table;

import java.beans.IntrospectionException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import forms.AnnotationUtilities;
import forms.field.FieldDescription;
import forms.field.FieldDescriptionFactory;
import forms.field.PropertyUI;
import forms.field.TableUI;

public abstract class TableReflection {

    public static <BEAN> List<ColumnInfo<BEAN>> infos(Class<BEAN> c) throws IntrospectionException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        ArrayList<ColumnInfo<BEAN>> list = new ArrayList<ColumnInfo<BEAN>>();
        List<FieldDescription> fields = FieldDescriptionFactory.createFieldDescriptions(c);
        for (FieldDescription f : fields) {
            if (toTable(f)) {
                ColumnInfo<BEAN> info = new ColumnInfo<BEAN>(f);
                list.add(info);
            }
        }
        Collections.sort(list, new ColumnComperator());
        return list;
    }

    private static class ColumnComperator implements Comparator<ColumnInfo<?>> {

        @Override
        public int compare(ColumnInfo<?> o1, ColumnInfo<?> o2) {
            if (o1.isTableUIset() && o2.isTableUIset()) {
                return o1.getTableUI().index() - o2.getTableUI().index();
            }
            if (o2.isTableUIset()) {
                return 1;
            }
            if (o1.isTableUIset()) {
                return -1;
            }
            return 0;
        }
    }

    private static boolean toTable(FieldDescription f) {
        try {
            return AnnotationUtilities.getAnnotation(TableUI.class, f).visible();
        } catch (Exception e) {
        }
        return true;
    }
}
