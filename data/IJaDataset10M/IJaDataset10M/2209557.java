package com.bluesky.jwf.component;

import com.bluesky.javawebbrowser.domain.html.tags.Tag;
import com.bluesky.javawebbrowser.domain.html.tags.TagType;
import com.bluesky.javawebbrowser.domain.html.tags.table.TD;
import com.bluesky.javawebbrowser.domain.html.tags.table.TR;
import com.bluesky.javawebbrowser.domain.html.tags.table.Table;

public class DataTable extends Component {

    private String[] fieldNames;

    private Object[][] data;

    public DataTable() {
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public void setFieldNames(String[] fieldNames) {
        this.fieldNames = fieldNames;
    }

    public Object[][] getData() {
        return data;
    }

    public void setData(Object[][] data) {
        this.data = data;
    }

    private Table table;

    private TR header;

    private TR oddRowTemplate;

    private TR evenRowTemplate;

    @Override
    public void init(String html) {
        super.init(html);
        table = (Table) loadByJwfClass("table");
        header = (TR) table.loadByJwfClass("header");
        oddRowTemplate = (TR) table.loadByJwfClass("odd");
        evenRowTemplate = (TR) table.loadByJwfClass("even");
        table.removeChild(oddRowTemplate);
        table.removeChild(evenRowTemplate);
    }

    public void DataBind() {
        header.expandColumns(fieldNames.length - 1);
        evenRowTemplate.expandColumns(fieldNames.length - 1);
        oddRowTemplate.expandColumns(fieldNames.length - 1);
        for (int j = 0; j < fieldNames.length; j++) {
            TD td = (TD) header.getChildren().get(j);
            Tag tag = new Tag(TagType.TEXT_BLOCK);
            tag.setBody(fieldNames[j]);
            td.removeAllChildren();
            td.addChild(tag);
        }
        for (int i = 0; i < data.length; i++) {
            TR row;
            if (i % 2 == 0) row = (TR) evenRowTemplate.clone(); else row = (TR) oddRowTemplate.clone();
            table.addRow(row);
            for (int k = 0; k < data[i].length; k++) {
                TD td = (TD) row.getChildren().get(k);
                Tag tag = new Tag(TagType.TEXT_BLOCK);
                tag.setBody(data[i][k].toString());
                td.removeAllChildren();
                td.addChild(tag);
            }
        }
    }
}
