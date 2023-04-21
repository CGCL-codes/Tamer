package com.guanghua.brick.html.tag;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.guanghua.brick.html.IContent;
import com.guanghua.brick.html.IControl;
import com.guanghua.brick.html.IDepend;
import com.guanghua.brick.html.IReplace;
import com.guanghua.brick.html.OptionControl;

public class ControlBindOptionTag extends BodyTagSupport implements IControl {

    private static Log logger = LogFactory.getLog(ControlBindOptionTag.class);

    private OptionControl control = new OptionControl();

    public int doEndTag() throws JspException {
        try {
            this.setType("checkbox");
            if (this.getName() != null) this.setName("checkbox");
            pageContext.getOut().println(build((HttpServletRequest) pageContext.getRequest(), (HttpServletResponse) pageContext.getResponse(), pageContext));
        } catch (IOException e) {
            logger.error("io exception on print bind option html", e);
        }
        return BodyTagSupport.EVAL_PAGE;
    }

    public void addOptionField(String field, String value) {
        if (field != null) {
            String html = this.getHtml();
            if (html == null) html = "";
            html += " " + field + "=\"" + ((value != null) ? value : "") + "\"";
            this.setHtml(html);
        }
    }

    public String build(HttpServletRequest request, HttpServletResponse response, PageContext pageContext) {
        return control.build(request, response, pageContext);
    }

    public IContent getContent() {
        return control.getContent();
    }

    public String getData() {
        return control.getData();
    }

    public String getDataSource() {
        return control.getDataSource();
    }

    public List<IDepend> getDepends() {
        return control.getDepends();
    }

    public String getHtml() {
        return control.getHtml();
    }

    public String getId() {
        return control.getId();
    }

    public String getName() {
        return control.getName();
    }

    public int getNewLine() {
        return control.getNewLine();
    }

    public List<IReplace> getReplaces() {
        return control.getReplaces();
    }

    public String getTextProperty() {
        return control.getTextProperty();
    }

    public String getType() {
        return control.getType();
    }

    public String getValueProperty() {
        return control.getValueProperty();
    }

    public boolean isLabelAfter() {
        return control.isLabelAfter();
    }

    public void setContent(IContent content) {
        control.setContent(content);
    }

    public void setData(String data) {
        control.setData(data);
    }

    public void setDataSource(String dataSource) {
        control.setDataSource(dataSource);
    }

    public void setDepends(List<IDepend> depends) {
        control.setDepends(depends);
    }

    public void setHtml(String html) {
        control.setHtml(html);
    }

    public void setId(String id) {
        control.setId(id);
    }

    public void setLabelAfter(boolean labelAfter) {
        control.setLabelAfter(labelAfter);
    }

    public void setName(String name) {
        control.setName(name);
    }

    public void setNewLine(int newLine) {
        control.setNewLine(newLine);
    }

    public void setReplaces(List<IReplace> replaces) {
        control.setReplaces(replaces);
    }

    public void setTextProperty(String textProperty) {
        control.setTextProperty(textProperty);
    }

    public void setType(String type) {
        control.setType(type);
    }

    public void setValueProperty(String valueProperty) {
        control.setValueProperty(valueProperty);
    }

    public String getFunction() {
        return control.getFunction();
    }

    public void setFunction(String function) {
        control.setFunction(function);
    }
}
