package com.bluesky.jwf.component;

import com.bluesky.javawebbrowser.domain.html.tags.TagType;
import com.bluesky.javawebbrowser.domain.html.tags.form.TextArea;

/**
 * TextArea wrapper
 * 
 * @author jack
 * 
 */
public class LongTextField extends Component implements StringField {

    private TextArea textArea;

    public String getText() {
        String s;
        s = textArea.getText();
        if (s == null) s = "";
        return s;
    }

    public void setText(String text) {
        textArea.setText(text);
    }

    @Override
    public void init(String html) {
        super.init(html);
        textArea = (TextArea) loadByTagType(TagType.TEXTAREA);
    }

    @Override
    public void setId(String id) {
        super.setId(id);
        textArea.setName(getId() + "_textbox");
    }

    @Override
    public void setPostBackData(String name, String value) {
        super.setPostBackData(name, value);
        if (name.equals("textbox")) setText(value);
    }

    public Integer getCols() {
        return textArea.getCols();
    }

    public void setCols(Integer cols) {
        textArea.setCols(cols);
    }

    public Integer getRows() {
        return textArea.getRows();
    }

    public void setRows(Integer rows) {
        textArea.setRows(rows);
    }

    @Override
    public String getString() {
        return getText();
    }
}
