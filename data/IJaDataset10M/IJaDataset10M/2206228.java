package org.fantasy.cpp.core.html.query;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.fantasy.cpp.core.bean.ParamContext;
import org.fantasy.cpp.core.html.util.TreeData;
import org.fantasy.cpp.core.pojo.QueryPage;
import org.fantasy.cpp.web.controller.QueryController;
import org.fantasy.common.db.bean.Row;
import org.fantasy.common.util.WebUtil;

/**
 *  普通下拉列表框
 * @author: 王文成
 * @version: 1.0
 * @since 2010-4-16
 */
public class TreeHtml extends AbstractQueryHtml {

    public TreeHtml(HttpServletRequest request, ParamContext param) {
        super(request, param);
    }

    @SuppressWarnings("all")
    public String getHtml() throws Exception {
        List<Row> rows = (List<Row>) getSource();
        StringBuffer html = new StringBuffer(1024);
        html.append("<span class='" + SPAN_CLASS + "'>");
        html.append(getLabelName());
        String comboId = getComboId();
        html.append("<select id='" + comboId + "'" + getClassHtml() + getStyleHtml() + "></select>");
        html.append("<input id='" + code + "' type='hidden'  name='" + code + "' value='" + defValue + "'/>");
        html.append("</span>");
        html.append(async ? getAsyncTreeHtml(rows, defValue) : getTreeHtml(rows, defValue));
        return html.toString();
    }

    /**
	 * 生成树异步树HTML
	 * @param rows
	 * @param defValue
	 * @return
	 */
    protected String getAsyncTreeHtml(List<Row> rows, String defValue) {
        StringBuffer html = new StringBuffer(1024);
        QueryPage page = (QueryPage) WebUtil.findAttribute(request, QueryController.PAGE_INFO);
        String ulId = getTreeUlID();
        String url = ctx + "/query/data.do?pageId=" + page.getPageId() + "&itemId=" + itemId;
        html.append("<div class='hide'>");
        html.append("<ul id='" + ulId + "' url='" + url + "' class='easyui-tree'>");
        html.append("</ul>");
        html.append("</div>");
        String comboId = getComboId();
        html.append("<script type=\"text/javascript\"> \n");
        html.append("$( function(){  \n");
        html.append("	$('#" + comboId + "').combo({ \n");
        html.append("		required:false, \n");
        html.append("		editable:false, \n");
        html.append("		width:150, \n");
        html.append("		panelWidth:200, \n");
        html.append("		panelHeight:200 \n");
        html.append("	}); \n");
        html.append("	$('#" + ulId + "').tree(\"options\").onClick = function( node ){ \n");
        html.append("		$('#" + comboId + "').combo('setValue',node.id).combo('setText', node.text).combo('hidePanel');  \n");
        html.append("		$('#" + code + "').val(node.id); \n");
        html.append("	}; \n");
        html.append("	$('#" + ulId + "').tree(\"options\").onLoadSuccess = function( node ){ \n");
        html.append("		if( !this.load ){ \n");
        html.append("			Tools.eval('" + defValue + "'); \n");
        html.append("		}\n");
        html.append("		this.load = true; \n");
        html.append("	}; \n");
        html.append("	$('#" + ulId + "').appendTo($('#" + comboId + "').combo('panel')); \n");
        html.append("});  \n");
        html.append("</script> \n");
        return html.toString();
    }

    /**
	 * 生成普通树HTML
	 * @param rows
	 * @param defValue
	 * @return
	 */
    protected String getTreeHtml(List<Row> rows, String defValue) {
        StringBuffer html = new StringBuffer(1024);
        TreeData convert = new TreeData(rows);
        String ulId = getTreeUlID();
        html.append("<div class='hide'>");
        html.append("<ul id='" + ulId + "' class='easyui-tree'>");
        html.append(convert.toUlLi());
        html.append("</ul>");
        html.append("</div>");
        String comboId = getComboId();
        html.append("<script type=\"text/javascript\"> \n");
        html.append("$( function(){  \n");
        html.append("	$('#" + comboId + "').combo({ \n");
        html.append("		required:false, \n");
        html.append("		editable:false, \n");
        html.append("		width:150, \n");
        html.append("		panelWidth:200, \n");
        html.append("		panelHeight:200 \n");
        html.append("	}); \n");
        html.append("	$('#" + ulId + "').tree(\"options\").onClick = function( node ){ \n");
        html.append("		$('#" + comboId + "').combo('setValue',node.id).combo('setText', node.text).combo('hidePanel');  \n");
        html.append("		$('#" + code + "').val(node.id); \n");
        html.append("	}; \n");
        html.append("	$('#" + ulId + "').appendTo($('#" + comboId + "').combo('panel')); \n");
        html.append("	Tools.eval('" + defValue + "')\n");
        html.append("});  \n");
        html.append("</script> \n");
        return html.toString();
    }

    protected String getTreeUlID() {
        return code + "Tree";
    }
}
