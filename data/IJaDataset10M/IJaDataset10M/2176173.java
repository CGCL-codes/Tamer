package net.rptools.dicetool.model;

import java.util.ArrayList;
import java.util.List;
import net.rptools.dicetool.model.xstream.ButtonConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@XStreamAlias("button")
@XStreamConverter(ButtonConverter.class)
public class Button {

    private String name;

    private List<String> expressions = new ArrayList<String>();

    public Button() {
    }

    public Button(String name) {
        this.name = name;
    }

    public Button(String name, String... expressions) {
        this.name = name;
        for (String x : expressions) this.expressions.add(x);
    }

    public List<String> getExpressions() {
        return expressions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
