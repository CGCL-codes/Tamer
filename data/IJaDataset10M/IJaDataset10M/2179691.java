package net.woodstock.rockapi.jsp.taglib.html.table;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.jsp.JspException;
import net.woodstock.rockapi.jsp.taglib.common.TLD;
import net.woodstock.rockapi.jsp.taglib.common.TLDAttribute;
import net.woodstock.rockapi.jsp.taglib.common.TLD.BodyContent;
import net.woodstock.rockapi.jsp.taglib.html.HtmlAttribute;
import net.woodstock.rockapi.jsp.taglib.html.HtmlBaseTag;
import net.woodstock.rockapi.utils.StringUtils;

@TLD(name = "column", type = BodyContent.SCRIPTLESS)
public class ColumnTag extends HtmlBaseTag {

    public static final String TAG_NAME = "td";

    @TLDAttribute(required = true)
    private String headerTitle;

    @TLDAttribute
    private String headerStyle;

    @TLDAttribute
    private String headerStyleClass;

    @TLDAttribute
    @HtmlAttribute
    private String width;

    @Override
    public void doTag() throws JspException, IOException {
        TableTag table = (TableTag) this.getParent();
        if (table.isAddHeader()) {
            this.addHeader();
        } else if (table.isAddColumn()) {
            this.addColumn();
        }
    }

    private void addHeader() throws JspException, IOException {
        if (!StringUtils.isEmpty(this.headerStyle)) {
            this.setStyle(this.headerStyle);
        }
        if (!StringUtils.isEmpty(this.headerStyleClass)) {
            this.setStyleClass(this.headerStyleClass);
        }
        Writer writer = this.getJspContext().getOut();
        writer.write("<" + ColumnTag.TAG_NAME + " " + this.getAttributesAsString() + ">");
        writer.write(this.headerTitle);
        writer.write("</" + ColumnTag.TAG_NAME + ">");
    }

    private void addColumn() throws JspException, IOException {
        Writer writer = this.getJspContext().getOut();
        writer.write("<" + ColumnTag.TAG_NAME + " " + this.getAttributesAsString() + ">");
        this.getJspBody().invoke(writer);
        writer.write("</" + ColumnTag.TAG_NAME + ">");
    }

    public String getHeaderTitle() {
        return this.headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public String getHeaderStyle() {
        return this.headerStyle;
    }

    public void setHeaderStyle(String headerStyle) {
        this.headerStyle = headerStyle;
    }

    public String getHeaderStyleClass() {
        return this.headerStyleClass;
    }

    public void setHeaderStyleClass(String headerStyleClass) {
        this.headerStyleClass = headerStyleClass;
    }

    public String getWidth() {
        return this.width;
    }

    public void setWidth(String width) {
        this.width = width;
    }
}
