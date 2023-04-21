package org.easybi.chart.impl.chartdir;

import org.easybi.chart.impl.chartdir.util.TextBoxFormat;

public class TitleFormat {

    String position = "";

    String text = "";

    TextBoxFormat textBoxFormat = null;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public TextBoxFormat getTextBoxFormat() {
        return textBoxFormat;
    }

    public void setTextBoxFormat(TextBoxFormat textBoxFormat) {
        this.textBoxFormat = textBoxFormat;
    }
}
