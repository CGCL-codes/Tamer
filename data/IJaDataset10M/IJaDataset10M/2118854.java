package jdbframework.tags;

import java.sql.SQLException;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import jdbframework.tags.property.TagColumnProperty;
import jdbframework.tags.property.TagImageProperty;
import jdbframework.action.ParametersManagement;
import jdbframework.common.GeneralSettings;
import jdbframework.common.JavaScriptList;
import jdbframework.tags.property.TagColorProperty;
import oracle.jdbc.OracleResultSet;

public class TableTag extends BodyTagSupport {

    private String property = null;

    private String statement = null;

    private String rowcount = null;

    private String top = null;

    private String left = null;

    private String width = null;

    private String height = null;

    private String onclick = null;

    private String ondblclick = null;

    private String onselectrow = null;

    private Vector columnFieldList = null;

    public void setProperty(String property) {
        this.property = property;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public void setRowcount(String rowcount) {
        this.rowcount = rowcount;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setOnclick(String onclick) {
        this.onclick = onclick;
    }

    public void setOndblclick(String ondblclick) {
        this.ondblclick = ondblclick;
    }

    public void setOnselectrow(String onselectrow) {
        this.onselectrow = onselectrow;
    }

    public void addColumnItem(TagColumnProperty columnFields) {
        if (columnFieldList == null) {
            columnFieldList = new Vector();
        }
        columnFieldList.add(columnFields);
    }

    public int doStartTag() throws JspException {
        return EVAL_BODY_INCLUDE;
    }

    public int doEndTag() throws JspException {
        final int HeightPaging = 22;
        final String containerPostfix = "_container";
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        String HtmlTable = "";
        JspWriter out = pageContext.getOut();
        OracleResultSet rset = null;
        try {
            TagColumnProperty Field;
            String BorderStl = "style=\"position:absolute;";
            BorderStl = BorderStl.concat("top:" + this.top + "px;");
            BorderStl = BorderStl.concat("left:" + this.left + "px;");
            BorderStl = BorderStl.concat("width:" + this.width + "px;");
            BorderStl = BorderStl.concat("height:" + (Integer.parseInt(this.height) - HeightPaging) + "px;");
            BorderStl = BorderStl.concat("\"");
            HtmlTable = HtmlTable.concat("<table class=\"" + GeneralSettings.CSS_TABLE_BORDER + "\" cellpadding=\"0\" cellspacing=\"0\" " + BorderStl + ">");
            HtmlTable = HtmlTable.concat("<tr><td></td></tr></table>");
            String ContainerStl = "style=\"position:absolute;";
            ContainerStl = ContainerStl.concat("top:" + (Integer.parseInt(this.top) + 1) + "px;");
            ContainerStl = ContainerStl.concat("left:" + (Integer.parseInt(this.left) + 1) + "px;");
            ContainerStl = ContainerStl.concat("width:" + (Integer.parseInt(this.width) - 3) + "px;");
            ContainerStl = ContainerStl.concat("height:" + (Integer.parseInt(this.height) - HeightPaging - 3) + "px;");
            ContainerStl = ContainerStl.concat("\"");
            HtmlTable = HtmlTable.concat("<div id=\"" + this.property + containerPostfix + "\" name=\"" + this.property + containerPostfix + "\" class=\"" + GeneralSettings.CSS_TABLE_CONTAINER + "\" " + ContainerStl + ">");
            int TableWidth = 0;
            int HiddenFieldsQnt = 0;
            for (int i = 0; i < columnFieldList.size(); i++) {
                Field = (TagColumnProperty) columnFieldList.get(i);
                if (Field.getHidden() == null) {
                    TableWidth = TableWidth + Integer.parseInt(Field.getWidth());
                    HiddenFieldsQnt = HiddenFieldsQnt + 1;
                } else {
                    if (Field.getHidden().equals("no")) {
                        TableWidth = TableWidth + Integer.parseInt(Field.getWidth());
                        HiddenFieldsQnt = HiddenFieldsQnt + 1;
                    }
                }
            }
            HtmlTable = HtmlTable.concat("<table id=\"" + this.property + "\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:" + TableWidth + "px;\">");
            HtmlTable = HtmlTable.concat("<colgroup>");
            HtmlTable = HtmlTable.concat("<col style=\"width:0%;\">");
            String thField = "";
            String inputCurrRow = "";
            String inputCurrRowScript = "";
            int indexHiddenElement = 0;
            int imgFieldQnt = 0;
            for (int i = 0; i < columnFieldList.size(); i++) {
                Field = (TagColumnProperty) columnFieldList.get(i);
                if (Field.getHidden() == null) {
                    HtmlTable = HtmlTable.concat("<col style=\"width:" + (Float.parseFloat(Field.getWidth()) / TableWidth * 100) + "%;\">");
                    thField = thField.concat("<th>" + Field.getTitle() + "</th>");
                    if (Field.getImagelist() == null) {
                        inputCurrRowScript = inputCurrRowScript.concat("document.getElementById(\"" + this.property + "." + Field.getProperty() + "\").value = (tr.getElementsByTagName(\"td\")[" + (i + 1 - indexHiddenElement) + "].firstChild.nodeValue==\" \"?\"\":tr.getElementsByTagName(\"td\")[" + (i + 1 - indexHiddenElement) + "].firstChild.nodeValue);\n");
                    } else {
                        inputCurrRowScript = inputCurrRowScript.concat("document.getElementById(\"" + this.property + "." + Field.getProperty() + "\").value = tr.getElementsByTagName(\"td\")[" + (HiddenFieldsQnt + 1) + "].getElementsByTagName(\"input\")[" + (indexHiddenElement + imgFieldQnt) + "].value;");
                        imgFieldQnt++;
                    }
                } else {
                    if (Field.getHidden().equals("no")) {
                        HtmlTable = HtmlTable.concat("<col style=\"width:" + (Float.parseFloat(Field.getWidth()) / TableWidth * 100) + "%;\">");
                        thField = thField.concat("<th>" + Field.getTitle() + "</th>");
                        if (Field.getImagelist() == null) {
                            inputCurrRowScript = inputCurrRowScript.concat("document.getElementById(\"" + this.property + "." + Field.getProperty() + "\").value = (tr.getElementsByTagName(\"td\")[" + (i + 1 - indexHiddenElement) + "].firstChild.nodeValue==\" \"?\"\":tr.getElementsByTagName(\"td\")[" + (i + 1 - indexHiddenElement) + "].firstChild.nodeValue);\n");
                        } else {
                            inputCurrRowScript = inputCurrRowScript.concat("document.getElementById(\"" + this.property + "." + Field.getProperty() + "\").value = tr.getElementsByTagName(\"td\")[" + (HiddenFieldsQnt + 1) + "].getElementsByTagName(\"input\")[" + (indexHiddenElement + imgFieldQnt) + "].value;");
                            imgFieldQnt++;
                        }
                    }
                    if (Field.getHidden().equals("yes")) {
                        inputCurrRowScript = inputCurrRowScript.concat("document.getElementById(\"" + this.property + "." + Field.getProperty() + "\").value = tr.getElementsByTagName(\"td\")[" + (HiddenFieldsQnt + 1) + "].getElementsByTagName(\"input\")[" + (indexHiddenElement + imgFieldQnt) + "].value;");
                        indexHiddenElement = indexHiddenElement + 1;
                    }
                }
                inputCurrRow = inputCurrRow.concat("<input type=\"hidden\" id=\"" + this.property + "." + Field.getProperty() + "\" name=\"" + this.property + "." + Field.getProperty() + "\" value=\"\"/>");
            }
            HtmlTable = HtmlTable.concat("</colgroup>");
            HtmlTable = HtmlTable.concat("<thead><tr><th style=\"padding-right:3px;\"><img src=\"" + GeneralSettings.getImagesPath(request.getContextPath().toString()) + GeneralSettings.IMG_CURR_TRANSPARENT + "\"/></th>");
            HtmlTable = HtmlTable + thField;
            HtmlTable = HtmlTable.concat("</tr>");
            HtmlTable = HtmlTable.concat(inputCurrRow);
            HtmlTable = HtmlTable.concat("</thead>");
            HtmlTable = HtmlTable.concat("<tbody>");
            if (request.getAttribute(this.statement + "resultset") != null) {
                rset = (OracleResultSet) request.getAttribute(this.statement + "resultset");
            }
            String trId = "";
            String ScriptSearchCurrTr = "";
            boolean FirstRow = false;
            for (int i = 0; i < columnFieldList.size(); i++) {
                Field = (TagColumnProperty) columnFieldList.get(i);
                request.setAttribute(this.property + "." + Field.getProperty(), "");
            }
            if (this.rowcount == null) {
                this.rowcount = Integer.toString(GeneralSettings.COUNT_ROWS_AT_PAGE);
            } else {
                if (this.rowcount.length() <= 0) {
                    this.rowcount = Integer.toString(GeneralSettings.COUNT_ROWS_AT_PAGE);
                }
            }
            int lastRow = Integer.parseInt(this.rowcount);
            boolean resetPage = false;
            if (request.getSession().getAttribute(this.statement + GeneralSettings.TMP_PRE_PARAMETER_STATUS) != null) {
                if (request.getSession().getAttribute(this.statement + GeneralSettings.TMP_PRE_PARAMETER_STATUS).equals("NEW")) {
                    resetPage = true;
                }
            }
            int allRowsQnt = Integer.parseInt((String) request.getAttribute(this.statement + "rowcount"));
            int countPages = (int) Math.ceil((allRowsQnt == 0 ? 1 : allRowsQnt) / Double.parseDouble(this.rowcount));
            String tmpCurrPage = new ParametersManagement(request).getParameter(this.property + ".property.page");
            int currPage = 1;
            if (tmpCurrPage != null) {
                if (tmpCurrPage.length() > 0) {
                    if (!resetPage) {
                        currPage = Integer.parseInt(tmpCurrPage);
                        if (currPage > countPages) {
                            currPage = countPages;
                        }
                    }
                }
            }
            if (!resetPage) {
                lastRow = Integer.parseInt(this.rowcount) * currPage;
            }
            int firstRow = lastRow - (Integer.parseInt(this.rowcount) - 1);
            int currRow = 0;
            int rowCount = 0;
            String priorRow = new ParametersManagement(request).getParameter(this.property + ".id");
            while (rset.next()) {
                currRow++;
                rowCount++;
                if (currRow >= firstRow) {
                    if (FirstRow == false) {
                        trId = "id=\"" + this.property + ".id." + rset.getString("id") + ".first" + "\"";
                        ScriptSearchCurrTr = "<script>" + GeneralSettings.replace(this.property, ".", "_") + "_click_tr(document.getElementById(\"" + this.property + ".id." + rset.getString("id") + ".first" + "\"), \"onselectrow\")</script>";
                        for (int i = 0; i < columnFieldList.size(); i++) {
                            Field = (TagColumnProperty) columnFieldList.get(i);
                            request.setAttribute(this.property + "." + Field.getProperty(), rset.getString(Field.getProperty()));
                        }
                        FirstRow = true;
                    }
                    if (priorRow != null) {
                        if (rset.getString("id").equals(priorRow)) {
                            trId = "id=\"" + this.property + ".id." + rset.getString("id") + "\"";
                            ScriptSearchCurrTr = "<script>" + GeneralSettings.replace(this.property, ".", "_") + "_click_tr(document.getElementById(\"" + this.property + ".id." + rset.getString("id") + "\"), \"onselectrow\");";
                            ScriptSearchCurrTr = ScriptSearchCurrTr.concat("ShowSelectedRow(document.getElementById(\"" + this.property + ".id." + rset.getString("id") + "\"), \"" + this.property + containerPostfix + "\");");
                            ScriptSearchCurrTr = ScriptSearchCurrTr.concat("</script>");
                            for (int i = 0; i < columnFieldList.size(); i++) {
                                Field = (TagColumnProperty) columnFieldList.get(i);
                                request.setAttribute(this.property + "." + Field.getProperty(), rset.getString(Field.getProperty()));
                            }
                        }
                    }
                    HtmlTable = HtmlTable.concat("<tr " + trId + "><td style=\"border-left:1px solid white;" + "border-right:1px solid gray;" + "border-top:1px solid white;" + "border-bottom:1px solid gray;" + "padding-left:2px;" + "padding-right:3px;" + "background:#D4D0C8;\">");
                    HtmlTable = HtmlTable.concat("<img src=\"" + GeneralSettings.getImagesPath(request.getContextPath().toString()) + GeneralSettings.IMG_CURR_TRANSPARENT + "\"/></td>");
                    String HiddenFields = "<td style=\"border:0px\">";
                    String ValueTd = null;
                    for (int i = 0; i < columnFieldList.size(); i++) {
                        Field = (TagColumnProperty) columnFieldList.get(i);
                        ValueTd = (rset.getString(Field.getProperty()) == null) ? "&nbsp;" : GeneralSettings.replaceHtmlSpecialChars(rset.getString(Field.getProperty()));
                        String tdStl = "style=\"";
                        if (Field.getAlign() != null) {
                            if (Field.getAlign().equals("left")) {
                                tdStl = tdStl.concat("text-align:left;padding-left:2px;");
                            }
                            if (Field.getAlign().equals("center")) {
                                tdStl = tdStl.concat("text-align:center;");
                            }
                            if (Field.getAlign().equals("right")) {
                                tdStl = tdStl.concat("text-align:right;padding-right:2px;");
                            }
                        } else {
                            tdStl = tdStl.concat("text-align:left;padding-left:2px;");
                        }
                        if (Field.getColorlist() != null) {
                            Vector colorFieldList = (Vector) request.getAttribute(Field.getColorlist() + GeneralSettings.TMP_COLOR_LIST);
                            TagColorProperty Value;
                            for (int j = 0; j < colorFieldList.size(); j++) {
                                Value = (TagColorProperty) colorFieldList.get(j);
                                if (Value.getIndex().equals(ValueTd)) {
                                    tdStl += "background:" + Value.getColor() + ";";
                                    break;
                                }
                            }
                        }
                        tdStl = tdStl.concat("\"");
                        if (Field.getHidden() == null) {
                            if (Field.getImagelist() == null) {
                                HtmlTable = HtmlTable.concat("<td " + tdStl + ">" + ValueTd + "</td>");
                            } else {
                                Vector imageFieldList = (Vector) request.getAttribute(Field.getImagelist() + GeneralSettings.TMP_IMAGE_LIST);
                                TagImageProperty Value;
                                String imageName = "";
                                for (int j = 0; j < imageFieldList.size(); j++) {
                                    Value = (TagImageProperty) imageFieldList.get(j);
                                    if (Value.getIndex().equals(ValueTd)) {
                                        imageName = "<img src=\"" + Value.getImg() + "\"/>";
                                        break;
                                    } else {
                                        imageName = "&nbsp;";
                                    }
                                }
                                HtmlTable = HtmlTable.concat("<td " + tdStl + ">" + imageName + "</td>");
                                HiddenFields = HiddenFields.concat("<input type=\"hidden\" value=\"" + GeneralSettings.replace(ValueTd, "&nbsp;", "") + "\"/>");
                            }
                        } else {
                            if (Field.getHidden().equals("no")) {
                                if (Field.getImagelist() == null) {
                                    HtmlTable = HtmlTable.concat("<td " + tdStl + ">" + ValueTd + "</td>");
                                } else {
                                    Vector imageFieldList = (Vector) request.getAttribute(Field.getImagelist() + GeneralSettings.TMP_IMAGE_LIST);
                                    TagImageProperty Value;
                                    String imageName = "";
                                    for (int j = 0; j < imageFieldList.size(); j++) {
                                        Value = (TagImageProperty) imageFieldList.get(j);
                                        if (Value.getIndex().equals(ValueTd)) {
                                            imageName = "<img src=\"" + Value.getImg() + "\"/>";
                                            break;
                                        } else {
                                            imageName = "&nbsp;";
                                        }
                                    }
                                    HtmlTable = HtmlTable.concat("<td " + tdStl + ">" + imageName + "</td>");
                                    HiddenFields = HiddenFields.concat("<input type=\"hidden\" value=\"" + GeneralSettings.replace(ValueTd, "&nbsp;", "") + "\"/>");
                                }
                            }
                            if (Field.getHidden().equals("yes")) {
                                HiddenFields = HiddenFields.concat("<input type=\"hidden\" value=\"" + GeneralSettings.replace(ValueTd, "&nbsp;", "") + "\"/>");
                            }
                        }
                    }
                    HiddenFields = HiddenFields.concat("</td>");
                    HtmlTable = HtmlTable.concat(HiddenFields);
                    HtmlTable = HtmlTable.concat("</tr>");
                    trId = "";
                    if (currRow == lastRow) break;
                }
            }
            HtmlTable = HtmlTable.concat("</tbody>");
            HtmlTable = HtmlTable.concat("</table>");
            HtmlTable = HtmlTable.concat("</div>");
            String PagingBorderStl = "style=\"position:absolute;";
            PagingBorderStl = PagingBorderStl.concat("top:" + (Integer.parseInt(this.height) + Integer.parseInt(this.top) - HeightPaging) + "px;");
            PagingBorderStl = PagingBorderStl.concat("left:" + this.left + "px;");
            PagingBorderStl = PagingBorderStl.concat("width:" + this.width + "px;");
            PagingBorderStl = PagingBorderStl.concat("height:" + HeightPaging + "px;");
            PagingBorderStl = PagingBorderStl.concat("\"");
            HtmlTable = HtmlTable.concat("<table class=\"" + GeneralSettings.CSS_TABLE_PAGIND_BORDER + "\" cellpadding=\"0\" cellspacing=\"0\" " + PagingBorderStl + "><tr>");
            if (currPage != 1 && !resetPage) {
                HtmlTable = HtmlTable.concat("<td style=\"padding-left:2px;\">");
                HtmlTable = HtmlTable.concat("<img onClick=\"SelectTablePage('" + this.property + ".property.page' , '1')\" src=\"" + GeneralSettings.getImagesPath(request.getContextPath().toString()) + GeneralSettings.IMG_FIRST_PAGE + "\"/></td>");
                HtmlTable = HtmlTable.concat("<td style=\"padding-left:2px;padding-right:5px;\">");
                HtmlTable = HtmlTable.concat("<img onClick=\"SelectTablePage('" + this.property + ".property.page' , '" + (currPage - 1) + "')\" src=\"" + GeneralSettings.getImagesPath(request.getContextPath().toString()) + GeneralSettings.IMG_PRIOR_PAGE + "\"/></td>");
            } else {
                HtmlTable = HtmlTable.concat("<td style=\"padding-left:2px;\">");
                HtmlTable = HtmlTable.concat("<img src=\"" + GeneralSettings.getImagesPath(request.getContextPath().toString()) + GeneralSettings.IMG_FIRST_PAGE_DIS + "\"/></td>");
                HtmlTable = HtmlTable.concat("<td style=\"padding-left:2px;padding-right:5px;\">");
                HtmlTable = HtmlTable.concat("<img src=\"" + GeneralSettings.getImagesPath(request.getContextPath().toString()) + GeneralSettings.IMG_PRIOR_PAGE_DIS + "\"/></td>");
            }
            float firstPage = 1;
            float lastPage = (GeneralSettings.COUNT_PAGE_AT_TABLE > countPages ? countPages : GeneralSettings.COUNT_PAGE_AT_TABLE);
            if (allRowsQnt != 0) {
                if (!resetPage) {
                    firstPage = (currPage - (lastPage / 2)) + 1;
                    lastPage = currPage + (lastPage / 2 + (1 / 2));
                    if (lastPage > countPages) {
                        firstPage = firstPage - (lastPage - countPages);
                        lastPage = lastPage - (lastPage - countPages);
                    }
                }
                if ((int) firstPage <= 0) {
                    firstPage = 1;
                    lastPage = (GeneralSettings.COUNT_PAGE_AT_TABLE > countPages ? countPages : GeneralSettings.COUNT_PAGE_AT_TABLE);
                }
                String separator = ", ";
                boolean fp = true;
                for (int i = (int) firstPage; i <= (int) lastPage; i++) {
                    if (i == (int) lastPage) {
                        separator = "";
                    }
                    if (i == currPage) {
                        HtmlTable = HtmlTable.concat("<td><span style=\"font-weight:bold;\">" + i + "</span>" + separator + "</td>");
                    } else {
                        HtmlTable = HtmlTable.concat("<td onClick=\"SelectTablePage('" + this.property + ".property.page" + "' , '" + i + "')\">" + i + separator + "</td>");
                    }
                }
            }
            if (currPage < countPages) {
                HtmlTable = HtmlTable.concat("<td style=\"padding-left:10px;\">");
                HtmlTable = HtmlTable.concat("<img onClick=\"SelectTablePage('" + this.property + ".property.page' , '" + (currPage + 1) + "')\" src=\"" + GeneralSettings.getImagesPath(request.getContextPath().toString()) + GeneralSettings.IMG_NEXT_PAGE + "\"/></td>");
                HtmlTable = HtmlTable.concat("<td style=\"padding-left:2px;\">");
                HtmlTable = HtmlTable.concat("<img onClick=\"SelectTablePage('" + this.property + ".property.page' , '" + countPages + "')\" src=\"" + GeneralSettings.getImagesPath(request.getContextPath().toString()) + GeneralSettings.IMG_LAST_PAGE + "\"/></td>");
            } else {
                HtmlTable = HtmlTable.concat("<td style=\"padding-left:10px;\">");
                HtmlTable = HtmlTable.concat("<img src=\"" + GeneralSettings.getImagesPath(request.getContextPath().toString()) + GeneralSettings.IMG_NEXT_PAGE_DIS + "\"/></td>");
                HtmlTable = HtmlTable.concat("<td style=\"padding-left:2px;\">");
                HtmlTable = HtmlTable.concat("<img src=\"" + GeneralSettings.getImagesPath(request.getContextPath().toString()) + GeneralSettings.IMG_LAST_PAGE_DIS + "\"/></td>");
            }
            HtmlTable = HtmlTable.concat("<td style=\"width:100%;padding-left:10px;\">[ " + (allRowsQnt != 0 ? Integer.toString(countPages) : " 0 ") + " ]</td>");
            HtmlTable = HtmlTable.concat("</tr></table>");
            HtmlTable = HtmlTable.concat("<input type=\"hidden\" id=\"" + this.property + ".property.page" + "\" name=\"" + this.property + ".property.page" + "\" value=\"" + currPage + "\">");
            HtmlTable = HtmlTable.concat("<input type=\"hidden\" id=\"" + this.property + ".property.rowcount" + "\" name=\"" + this.property + ".rowcount" + "\" value=\"" + rowCount + "\">");
            String javaScript = "";
            javaScript = javaScript.concat("<script>");
            javaScript = javaScript.concat("var " + GeneralSettings.replace(this.property, ".", "_") + "_list_items = document.getElementById(\"" + this.property + "\").getElementsByTagName(\"tbody\")[0].getElementsByTagName(\"tr\");");
            javaScript = javaScript.concat("for(var i = 0; i < " + GeneralSettings.replace(this.property, ".", "_") + "_list_items.length; i++){");
            javaScript = javaScript.concat("var tr = " + GeneralSettings.replace(this.property, ".", "_") + "_list_items[i];");
            javaScript = javaScript.concat("tr.onclick = function(){" + GeneralSettings.replace(this.property, ".", "_") + "_click_tr(this, \"\");}\n");
            if (this.ondblclick != null) {
                javaScript = javaScript.concat("tr.ondblclick = function(){" + this.ondblclick + "(this);}\n");
            }
            javaScript = javaScript.concat("}</script>");
            javaScript = javaScript.concat("<script>");
            javaScript = javaScript.concat("var " + GeneralSettings.replace(this.property, ".", "_") + "_prior_tr;" + "var " + GeneralSettings.replace(this.property, ".", "_") + "_prior_img;");
            javaScript = javaScript.concat("function " + GeneralSettings.replace(this.property, ".", "_") + "_click_tr(tr, event){\n");
            javaScript = javaScript.concat("if (" + GeneralSettings.replace(this.property, ".", "_") + "_prior_tr == tr){" + (this.onclick != null ? this.onclick + "(tr);" : "") + " return false;}\n");
            javaScript = javaScript.concat("if (" + GeneralSettings.replace(this.property, ".", "_") + "_prior_tr){" + GeneralSettings.replace(this.property, ".", "_") + "_prior_tr.className = \"" + GeneralSettings.CSS_TABLE_NOT_SELECTED_ROW + "\";" + " " + GeneralSettings.replace(this.property, ".", "_") + "_prior_img.src = \"" + GeneralSettings.getImagesPath(request.getContextPath().toString()) + GeneralSettings.IMG_CURR_TRANSPARENT + "\";}");
            javaScript = javaScript.concat(" " + GeneralSettings.replace(this.property, ".", "_") + "_prior_tr = tr;");
            javaScript = javaScript.concat(" " + GeneralSettings.replace(this.property, ".", "_") + "_prior_img = tr.getElementsByTagName(\"td\")[0].getElementsByTagName(\"img\")[0];");
            javaScript = javaScript.concat("tr.className = \"" + GeneralSettings.CSS_TABLE_SELECTED_ROW + "\";");
            javaScript = javaScript.concat("tr.getElementsByTagName(\"td\")[0].getElementsByTagName(\"img\")[0].src = \"" + GeneralSettings.getImagesPath(request.getContextPath().toString()) + GeneralSettings.IMG_CURR + "\";");
            javaScript = javaScript.concat(inputCurrRowScript);
            if (this.onselectrow != null) {
                javaScript = javaScript.concat(this.onselectrow + "(tr);");
            }
            if (this.onclick != null) {
                javaScript = javaScript.concat("if (event != \"onselectrow\"){" + this.onclick + "(tr);}");
            }
            javaScript = javaScript.concat("}</script>");
            javaScript = javaScript.concat(ScriptSearchCurrTr);
            JavaScriptList javaScriptList = (JavaScriptList) request.getAttribute(GeneralSettings.TMP_JAVA_SCRIPT_LIST);
            javaScriptList.addScript(javaScript);
            rset.getStatement().close();
            rset.close();
            out.println(HtmlTable);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        } catch (java.io.IOException io) {
            io.getMessage();
        } finally {
            columnFieldList = null;
        }
        return EVAL_PAGE;
    }
}
