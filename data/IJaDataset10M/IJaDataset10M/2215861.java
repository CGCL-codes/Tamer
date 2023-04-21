package com.dcivision.framework.taglib.channel;

import com.dcivision.framework.GlobalConstant;
import com.dcivision.framework.MessageResourcesFactory;
import com.dcivision.framework.SessionContainer;

public class ChannelTagFormatter_1 extends AbstractChannelTagFormatter {

    public String getStartContent() {
        SessionContainer sessionContainer = (SessionContainer) tag.getPageContext().getSession().getAttribute(GlobalConstant.SESSION_CONTAINER_KEY);
        String contextPath = (String) tag.getPageContext().getServletContext().getAttribute(GlobalConstant.CONTEXT_PATH_KEY);
        String label = MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), tag.getLabel());
        String link = contextPath + tag.getLink();
        Filter filter = FilterFactory.getFilterByName(tag.getPageContext(), tag.getFilterName());
        StringBuffer content = new StringBuffer();
        content.append("        <div id=\"" + tag.getFilterName() + "\">\n");
        content.append("            <div id=\"listBarHome\">\n");
        content.append("                <div class=\"img_left_with_icon\"></div>\n");
        content.append("                <a href=\"javascript:toggleShowHide('" + tag.getFilterName() + "DATA');toggleStyleClass('" + tag.getFilterName() + "CONTROL', 'img_right_with_button_down', 'img_right_with_button_right' );\"><div id=\"" + tag.getFilterName() + "CONTROL\" class=\"img_right_with_button_down\"></div></a>\n");
        content.append("                <div class=\"icon_" + tag.getFilterName() + "\"></div>\n");
        content.append("                ").append(tag.getSelectFilter(filter)).append("\n");
        content.append("                <div class=\"title\"><a href=\"").append(link).append("\">").append(label).append("</a></div>\n");
        content.append("            </div>\n");
        content.append("            <!-- start table -->\n");
        content.append("            <div id=\"" + tag.getFilterName() + "DATA\" class=\"scrollable\">\n");
        content.append("            <div id=\"" + tag.getFilterName() + "BODY\" class=\"listTable\">\n");
        content.append("                <table>\n");
        content.append("                    <TBODY id=\"" + tag.getFilterName() + "Column\">\n");
        content.append("                      <tr>\n");
        return content.toString();
    }

    public String getEndContent() {
        StringBuffer content = new StringBuffer();
        content.append("                      </tr>\n");
        content.append("                    </TBODY>\n");
        content.append("                </table>\n");
        content.append("            </div>\n");
        content.append("            </div>\n");
        content.append("            <div class=\"listStatusBar\">\n");
        content.append("                <div id=\"" + tag.getFilterName() + "pageInfoLeft\" class=\"statement\"></div>\n");
        content.append("                <div id=\"" + tag.getFilterName() + "pageInfoRight\" class=\"sepBar\"></div>\n");
        if ("true".equals(tag.getIsShowButton())) {
            SessionContainer sessionContainer = (SessionContainer) tag.getPageContext().getSession().getAttribute(GlobalConstant.SESSION_CONTAINER_KEY);
            String buttonLabel = "home.label.message_mark_selected_as_read";
            String buttonName = MessageResourcesFactory.getMessage(sessionContainer.getSessionLocale(), buttonLabel);
            content.append("<div style = \'position: absolute;left:50%;\'>\n");
            content.append("<span class=\'but-standard-disabled\' id=\'markButton\'><div class=\'but_left\'></div><a href=\"#\" onclick=\"exeEventRead(\'basicSelectedID\','markButton');return false;\" ><span class=\'text\'>" + buttonName + "</span></a><div class=\'but_right\'></div></span>\n");
            content.append("</div>\n");
        }
        content.append("                <input type=\"hidden\" name=\"currentPage\" value=\"1\">\n");
        content.append("                <input type=\"hidden\" name=\"updateReq\" value=\"" + tag.getUpdateReq() + "\">\n");
        content.append("                <input type=\"hidden\" name=\"isAjax\" value=\"" + tag.isAjax() + "\">\n");
        content.append("                <input type=\"hidden\" name=\"pageSize\" value=\"" + tag.getPageSize() + "\">\n");
        content.append("                <SCRIPT>//reFreshRequenceData('" + tag.getFilterName() + "'," + tag.getUpdateReq() + ",'" + tag.isAjax() + "')</SCRIPT>\n");
        content.append("                <SCRIPT>\n");
        content.append("                var channel" + tag.getFilterName() + " = new channel.control(\"" + tag.getFilterName() + "\"," + tag.getPageSize() + ",1," + tag.isAjax() + "," + tag.getUpdateReq() + ",\"channel" + tag.getFilterName() + "\",\"" + tag.getDefaultFilterItem() + "\");\n");
        content.append("                channel" + tag.getFilterName() + ".refreshChannel();\n");
        content.append("                </SCRIPT>\n");
        content.append("            </div>\n");
        content.append("        </div>\n");
        return content.toString();
    }
}
