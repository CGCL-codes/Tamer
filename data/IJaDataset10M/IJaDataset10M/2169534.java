package com.sivoh.hssftemplates.tags;

import com.sivoh.hssftemplates.HssfTemplateContext;
import javax.servlet.ServletException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * 
 * @author sivoh
 * @version $REVISION
 */
public class HssfSheetTag extends HssfBaseTag {

    private String name = null;

    private String style = null;

    public String getTagName() {
        return "sheet";
    }

    public void render(HssfTemplateContext context) throws ServletException {
        if (name == null) throw new ServletException("Parse Error, sheet tag must have name attribute");
        String parsedName = (String) parseExpression(name, String.class, context);
        HSSFWorkbook workbook = context.getWorkbook();
        HSSFSheet sheet = workbook.getSheet(parsedName);
        if (sheet == null) sheet = workbook.createSheet(parsedName);
        String oldStyleName = context.getCurrentStyle();
        context.setCurrentStyle(parseCurrentStyle(oldStyleName, context));
        context.setSheet(sheet);
        renderChildren(context);
        context.setSheet(null);
        context.setCurrentStyle(oldStyleName);
    }

    private String parseCurrentStyle(String oldStyleName, HssfTemplateContext context) throws ServletException {
        String styleName = oldStyleName;
        if (getStyle() != null) {
            String parsedStyle = (String) parseExpression(getStyle(), String.class, context);
            styleName = (styleName.length() > 0) ? styleName + " " + parsedStyle : parsedStyle;
        }
        return styleName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
