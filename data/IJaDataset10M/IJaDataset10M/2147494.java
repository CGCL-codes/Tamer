package com.kescom.matrix.core.user;

import com.kescom.matrix.core.directory.ISeriesTemplate;
import com.kescom.matrix.core.series.IPrivacyLevel;

public class ReportingEntity extends ReportingEntityBase implements IReportingEntity {

    private IUser user;

    private String name;

    private String description;

    private ISeriesTemplate template;

    private int privacyLevel = IPrivacyLevel.PUBLIC;

    private int seriesCount;

    private String field0;

    private String field1;

    private String field2;

    private String field3;

    private String field4;

    private String field5;

    private String field6;

    private String field7;

    private String field8;

    private String field9;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IUser getUser() {
        return user;
    }

    public void setUser(IUser user) {
        this.user = user;
    }

    public Object getField(int index) {
        String field = null;
        switch(index) {
            case 0:
                field = field0;
                break;
            case 1:
                field = field1;
                break;
            case 2:
                field = field2;
                break;
            case 3:
                field = field3;
                break;
            case 4:
                field = field4;
                break;
            case 5:
                field = field5;
                break;
            case 6:
                field = field6;
                break;
            case 7:
                field = field7;
                break;
            case 8:
                field = field8;
                break;
            case 9:
                field = field9;
                break;
            default:
                throw new RuntimeException("index out of range: " + index);
        }
        if (field == null || template == null || index >= template.getSchema().getFields().size()) return field; else {
            return fromText(template.getSchema().getFields().get(index).getType(), field);
        }
    }

    public void setField(int index, Object value) {
        String field = null;
        if (value == null || template == null || index >= template.getSchema().getFields().size()) field = (value == null) ? null : value.toString(); else field = toText(template.getSchema().getFields().get(index).getType(), value);
        switch(index) {
            case 0:
                field0 = field;
                break;
            case 1:
                field1 = field;
                break;
            case 2:
                field2 = field;
                break;
            case 3:
                field3 = field;
                break;
            case 4:
                field4 = field;
                break;
            case 5:
                field5 = field;
                break;
            case 6:
                field6 = field;
                break;
            case 7:
                field7 = field;
                break;
            case 8:
                field8 = field;
                break;
            case 9:
                field9 = field;
                break;
            default:
                throw new RuntimeException("index out of range: " + index);
        }
    }

    public String getField0() {
        return field0;
    }

    public void setField0(String field0) {
        this.field0 = field0;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    public String getField3() {
        return field3;
    }

    public void setField3(String field3) {
        this.field3 = field3;
    }

    public String getField4() {
        return field4;
    }

    public void setField4(String field4) {
        this.field4 = field4;
    }

    public String getField5() {
        return field5;
    }

    public void setField5(String field5) {
        this.field5 = field5;
    }

    public String getField6() {
        return field6;
    }

    public void setField6(String field6) {
        this.field6 = field6;
    }

    public String getField7() {
        return field7;
    }

    public void setField7(String field7) {
        this.field7 = field7;
    }

    public String getField8() {
        return field8;
    }

    public void setField8(String field8) {
        this.field8 = field8;
    }

    public String getField9() {
        return field9;
    }

    public void setField9(String field9) {
        this.field9 = field9;
    }

    public ISeriesTemplate getTemplate() {
        return template;
    }

    public void setTemplate(ISeriesTemplate template) {
        this.template = template;
    }

    public int getPrivacyLevel() {
        return privacyLevel;
    }

    public void setPrivacyLevel(int privacyLevel) {
        this.privacyLevel = privacyLevel;
    }

    public int getSeriesCount() {
        return seriesCount;
    }

    public void setSeriesCount(int seriesCount) {
        this.seriesCount = seriesCount;
    }

    public IUser getOwner() {
        return user;
    }
}
