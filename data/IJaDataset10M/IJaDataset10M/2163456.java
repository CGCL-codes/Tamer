package org.lightmtv.widget.util;

import java.util.ArrayList;
import java.util.List;
import org.lightcommons.util.ReflectionUtils;
import org.lightcommons.util.StringUtils;
import org.lightmtv.widget.Widget;
import org.lightmtv.widget.annotation.Fields;
import org.lightmtv.widget.annotation.FieldsOfPage;
import org.lightmtv.widget.annotation.Hidden;
import org.lightmtv.widget.annotation.Label;
import org.lightmtv.widget.annotation.MultipartContent;
import org.lightmtv.widget.annotation.ShowIn;

public class ModelWrap {

    private Class<?> entityClass;

    private String local;

    private WidgetRegister register = WidgetRegister.getInstance();

    public ModelWrap(Class<?> entityClass) {
        super();
        this.entityClass = entityClass;
    }

    public ModelWrap(Class<?> entityClass, String locale) {
        super();
        this.entityClass = entityClass;
        this.local = locale;
    }

    public String editor(Object entity) {
        return null;
    }

    public String label() {
        Label label = (Label) entityClass.getAnnotation(Label.class);
        if (label != null) {
            return label.value();
        }
        return entityClass.getSimpleName();
    }

    public String viewer(Object entity) {
        return null;
    }

    public String fieldEditor(Object entity, String field) {
        Widget com = register.widgetOf("editor", entityClass, entity, getFieldExp(field));
        return com.toHTML();
    }

    public String fieldLabel(String field) {
        Label n = ReflectionUtils.getAnnotation(entityClass, field, Label.class);
        if (n != null) {
            return n.value();
        }
        return StringUtils.formatFieldName(field);
    }

    public String fieldViewer(Object entity, String field) {
        Widget com = register.widgetOf("viewer", entityClass, entity, getFieldExp(field));
        return com.toHTML();
    }

    public List<String> getFieldsOf(String pageName) {
        String[] fields = new String[] {};
        Fields fieldAnno = entityClass.getAnnotation(Fields.class);
        if (fieldAnno != null) {
            fields = fieldAnno.value();
            for (FieldsOfPage fop : fieldAnno.pages()) {
                if (pageName.equals(fop.page())) {
                    fields = fop.fields();
                }
            }
        } else {
            if (ShowIn.LIST.equals(pageName) || ShowIn.VIEW.equals(pageName)) {
                fields = ReflectionUtils.getFields(entityClass, ReflectionUtils.GETTER).toArray(fields);
            } else if (ShowIn.NEW.equals(pageName) || ShowIn.EDIT.equals(pageName)) {
                fields = ReflectionUtils.getFields(entityClass, ReflectionUtils.SETTER).toArray(fields);
            } else {
                fields = ReflectionUtils.getFields(entityClass).toArray(fields);
            }
        }
        List<String> ret = new ArrayList<String>();
        for (String f : fields) {
            if ("id".equals(f) || ReflectionUtils.getAnnotation(entityClass, f, Hidden.class) != null) continue;
            ShowIn showIn = ReflectionUtils.getAnnotation(entityClass, f, ShowIn.class);
            if (showIn != null) {
                for (String show : showIn.value()) {
                    if (show.equalsIgnoreCase(pageName)) {
                        ret.add(f);
                        break;
                    }
                }
                continue;
            }
            ret.add(f);
        }
        return ret;
    }

    private String getFieldExp(String expression) {
        int i = expression.lastIndexOf('.');
        if (i > 0) {
            return expression.substring(i + 1);
        }
        return expression;
    }

    public boolean isMultpartContent() {
        return entityClass.getAnnotation(MultipartContent.class) != null;
    }

    public String editForm(Object entity) {
        return null;
    }

    public String listView(List<?> list) {
        return null;
    }

    public String getEditHref(Long id) {
        return null;
    }

    public String getViewHref(Long id) {
        return null;
    }

    public String getDeleteHref(Long id) {
        return null;
    }

    public String getPageHref(String uri, int pageIndex) {
        return null;
    }
}
