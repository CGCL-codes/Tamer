package com.nhncorp.usf.core.result;

import java.util.List;
import java.util.Map;
import com.nhncorp.usf.core.config.runtime.ResultPageInfo;

/**
 * 디버깅용으로 RAW데이터를 보여주는 Result {@link Result}.
 *
 * @author Web Platform Development Team
 */
@SuppressWarnings("serial")
public class HTMLDataModelResult extends AbstractDataModelResult {

    /**
     * 전달된 {@link DataMap} 정보를 {@link ResultPageInfo} 에 적용하여 결과 페이지 내보냄.
     * 
     * @param resultPage the ResultPageInfo
     * @param dataMap the data map
     * @throws Exception the Exception
     */
    public void execute(ResultPageInfo resultPage, Map<String, Object> dataMap) throws Exception {
        Map<String, Object> usfEmbeddedData = generateResultData("text/html; charset=", dataMap);
        StringBuffer sb = new StringBuffer();
        sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\">\n<html>\n<head>\n\t" + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n\t" + "<title>USF DataStructure Page</title>\n");
        sb.append("\t<style>\n" + "\t<!-- BODY {\tmargin: auto;\twidth: 100%;\tmargin-top: 50px;\tmargin-bottom: 35px;}\n" + "\tBODY,TD,A {\tcolor: #202020;\tfont-size: 12px;\tfont-family: tahoma;\tline-height: 20px;\ttext-decoration: none;}\n" + "\tA:hover {\tcolor: #1133dd;}" + "IMG {\tborder: 0;}" + "h1 {    padding: 7px;}\n" + "\tTH \t{\tbackground-Color: #dedede;}\n" + "\tTD.dotbottom {	border-bottom-style: dashed;	border-bottom-width: 1px;	border-color: #888888;	padding-left: 5px;	padding-right: 5px;}\n" + "\tDIV.contents \t\t{\twidth: 800px;\tmargin: auto auto;\ttext-align: center;}\n" + "\tDIV.component \t\t{\tmargin-top: 15px;}\n" + "\tDIV.component_left\t{\tmargin-top: 45px;    text-align: left;}\n" + "\tDIV.component_right {\tmargin-top: 15px;\ttext-align: right;\tcolor: #aaa;}\n" + "\tDIV.chart \t\t\t{\tmargin-top: 10px;}\n" + "\tDIV.chart_left 		{	float: left;	padding-right: 10px;} -->\n");
        sb.append("</style>\n" + "</head>\n<body>\n<p/>\n");
        sb.append("<h1 align=\"center\">[USF 수행 결과]</h1><br/>\n<div class=\"component\">");
        sb.append(generateMapTable(dataMap, "RETURN-ID", "CONTENT"));
        sb.append("</div>\n<br/><br/>\n<div class=\"component\"><h1 align=\"center\">[Input Parameter]</h1><br/>\n");
        sb.append(generateMap2Table(usfEmbeddedData, "default"));
        sb.append("</div>\n</body>\n</html>\n");
        out.println(sb.toString());
        out.flush();
    }

    /**
     * Generate map table.
     *
     * @param dataMap the data map
     * @return the string buffer
     */
    private StringBuffer generateMapTable(Map<String, Object> dataMap) {
        StringBuffer sb = new StringBuffer();
        sb.append(generateMapTable(dataMap, "KEY", "VALUE"));
        return sb;
    }

    /**
     * Generate map table.
     *
     * @param dataMap     the data map
     * @param keyString   the key string
     * @param valueString the value string
     * @return the string buffer
     */
    @SuppressWarnings("unchecked")
    private StringBuffer generateMapTable(Map<String, Object> dataMap, String keyString, String valueString) {
        StringBuffer sb = new StringBuffer();
        sb.append("<table border=\"1px\" border=\"0\" cellspacing=\"1\" cellpadding=\"2\" align=\"center\">\n");
        sb.append("<thead align=\"center\"><tr><td width=\"150\">\n<b>");
        sb.append(keyString);
        if (keyString.equals("RETURN-ID")) {
            sb.append("</b>\n</td>\n<td align=\"center\">\n<b>");
        } else {
            sb.append("</b>\n</td>\n<td width=\"300\" align=\"center\"><b>");
        }
        sb.append(valueString);
        sb.append("</b></td>\n<td width=\"100\"><b>TYPE</b></td>\n</tr>\n</thead>\n<tbody>\n");
        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
            sb.append("<tr><th>\n").append(entry.getKey()).append("</th><td>\n");
            Object obj = entry.getValue();
            if (obj instanceof Map) {
                sb.append(generateMapTable((Map<String, Object>) obj));
            } else if (obj instanceof List) {
                sb.append(generateListTable((List) obj));
            } else {
                sb.append(obj);
            }
            sb.append("</td>\n<td align=\"center\">\n").append(getType(obj));
            sb.append("</td>\n</tr>\n");
        }
        sb.append("</tbody></table>\n");
        return sb;
    }

    /**
     * Generate list table.
     *
     * @param list the list
     * @return the string buffer
     */
    @SuppressWarnings("unchecked")
    private StringBuffer generateListTable(List list) {
        StringBuffer sb = new StringBuffer();
        sb.append("<table border=\"1px\" border=\"0\" cellspacing=\"1\" cellpadding=\"2\">\n" + "<thead align=\"center\">\n<tr>\n<td><b>VALUE</b></td>\n" + "<td width=\"100\"><b>TYPE</b></td>\n</tr>\n</thead><tbody>\n");
        for (Object obj : list) {
            sb.append("<tr>\n<td>\n");
            if (obj instanceof Map) {
                sb.append(generateMapTable((Map<String, Object>) obj, "KEY", "VALUE"));
            } else if (obj instanceof List) {
                sb.append(generateListTable((List) obj));
            } else {
                sb.append(obj);
            }
            sb.append("</td>\n<td align=\"center\">").append(getType(obj));
            sb.append("</td>\n</tr>\n");
        }
        sb.append("</tbody></table>");
        return sb;
    }

    /**
     * Generate map2 table.
     *
     * @param dataMap the data map
     * @param option  the option
     * @return the string buffer
     */
    private StringBuffer generateMap2Table(Map<String, Object> dataMap, Object option) {
        StringBuffer sb = new StringBuffer();
        sb.append(generateMap2Table(dataMap, "KEY", "VALUE", option));
        return sb;
    }

    /**
     * Generate map2 table.
     *
     * @param dataMap     the data map
     * @param keyString   the key string
     * @param valueString the value string
     * @param option      the option
     * @return the string buffer
     */
    @SuppressWarnings("unchecked")
    private StringBuffer generateMap2Table(Map<String, Object> dataMap, String keyString, String valueString, Object option) {
        StringBuffer sb = new StringBuffer();
        sb.append("<table border=\"1px\" border=\"0\" cellspacing=\"1\" cellpadding=\"2\" align=\"center\">\n<thead align=\"center\"><tr><td width=\"100\">\n<b>");
        sb.append(keyString);
        if (option == null) {
            sb.append("</b></td>\n<td width=\"300\" align=\"center\"><b>");
        } else {
            sb.append("</b></td>\n<td align=\"center\"><b>");
        }
        sb.append(valueString);
        sb.append("</b></td>\n<td width=\"100\"><b>TYPE</b></td>\n</tr>\n</thead><tbody>\n");
        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
            if (entry.getKey().equals(checkDataModel)) {
                continue;
            }
            sb.append("<tr><th>").append(entry.getKey()).append("</th><td>");
            Object obj = entry.getValue();
            if (obj instanceof Map) {
                sb.append(generateMap2Table((Map<String, Object>) obj, null));
            } else if (obj instanceof List) {
                sb.append(generateList2Table((List) obj));
            } else {
                sb.append(obj);
            }
            sb.append("</td>\n<td align=\"center\">\n").append(getType(obj));
            sb.append("</td>\n</tr>\n");
        }
        sb.append("</tbody></table>\n");
        return sb;
    }

    /**
     * Generate list2 table.
     *
     * @param list the list
     * @return the string buffer
     */
    @SuppressWarnings("unchecked")
    private StringBuffer generateList2Table(List list) {
        StringBuffer sb = new StringBuffer();
        sb.append("\n<table border=\"1px\" border=\"0\" cellspacing=\"1\" cellpadding=\"2\">\n" + "<thead align=\"center\">\n<tr>\n<td><b>VALUE</b></td>\n" + "<td width=\"100\"><b>TYPE</b></td>\n</tr>\n</thead><tbody>\n");
        for (Object obj : list) {
            sb.append("<tr>\n<td>\n");
            if (obj instanceof Map) {
                sb.append(generateMap2Table((Map<String, Object>) obj, "KEY", "VALUE", null));
            } else if (obj instanceof List) {
                sb.append(generateList2Table((List) obj));
            } else {
                sb.append(obj);
            }
            sb.append("</td>\n<td align=\"center\">").append(getType(obj));
            sb.append("</td>\n</tr>\n");
        }
        sb.append("</tbody></table>\n");
        return sb;
    }

    /**
     * Gets the type.
     *
     * @param obj the obj
     * @return the type
     */
    private String getType(Object obj) {
        if (obj == null) {
            return "NULL";
        }
        return obj.getClass().getSimpleName();
    }
}
