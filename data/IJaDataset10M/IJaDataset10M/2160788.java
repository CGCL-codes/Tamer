package com.modelmetrics.cloudconverter.describe;

import java.util.ArrayList;
import java.util.Collection;
import ognl.Ognl;
import com.sforce.soap.partner.Field;
import com.sforce.soap.partner.PicklistEntry;

public class DisplayableSobjectFieldMetadataBeanBuilder {

    public Collection<DisplayableFieldMetadataBean> build(Collection<SobjectFieldPropertyBean> sobjectFieldBeans, Collection<Field> fields) {
        Collection<DisplayableFieldMetadataBean> metadata = new ArrayList<DisplayableFieldMetadataBean>();
        for (Field element : fields) {
            DisplayableFieldMetadataBean ret = new DisplayableFieldMetadataBean();
            Collection<DisplayableSobjectFieldPropertyBean> properties = new ArrayList<DisplayableSobjectFieldPropertyBean>();
            ret.setField(element);
            ret.setProperties(properties);
            metadata.add(ret);
            for (SobjectFieldPropertyBean current : sobjectFieldBeans) {
                DisplayableSobjectFieldPropertyBean bean = new DisplayableSobjectFieldPropertyBean();
                bean.setField(element);
                bean.setSobjectFieldBean(current);
                properties.add(bean);
                Object o = null;
                try {
                    o = Ognl.getValue(current.getName(), element);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                String cellContents = null;
                boolean multiline = false;
                if (o instanceof PicklistEntry[]) {
                    PicklistEntry[] pe = (PicklistEntry[]) o;
                    bean.setMultilineValue(this.getValues(pe));
                    bean.setMultiline(true);
                } else if (o instanceof String[]) {
                    String[] pe = (String[]) o;
                    bean.setMultilineValue(pe);
                    bean.setMultiline(true);
                } else if (o != null) {
                    if (current.getName() == "type" && element.getExternalId() != null && element.getExternalId().booleanValue()) {
                        bean.setValue(o.toString() + " (ext id.)");
                    } else {
                        bean.setValue(o.toString());
                    }
                } else {
                    bean.setValue("");
                }
            }
        }
        return metadata;
    }

    private String[] getValues(PicklistEntry[] target) {
        ArrayList<String> ret = new ArrayList<String>();
        if (target != null) {
            for (int i = 0; i < target.length; i++) {
                ret.add(target[i].getValue());
            }
        }
        return ret.toArray(new String[] {});
    }
}
