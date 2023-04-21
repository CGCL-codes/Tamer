package cn.vlabs.duckling.dct.services.plugin.impl.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import cn.vlabs.duckling.dct.services.dml.html2dml.Html2Dml;
import cn.vlabs.duckling.dct.services.dml.html2dml.Html2DmlEngine;
import cn.vlabs.duckling.dct.services.dpage.DPageService;
import cn.vlabs.duckling.vwb.VWBContext;

public class HtmlFilterImpl implements BaseFilter {

    private List<FilterColumn> metas = null;

    private String tableName = "mytable";

    private String srcSQL = null;

    private String title = null;

    private boolean isAllFlag = false;

    private int _tbSize = 0;

    private int[] _tbSelected;

    private DPageService dpageService;

    public HtmlFilterImpl(VWBContext context) {
        ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(context.getHttpRequest().getSession().getServletContext());
        dpageService = (DPageService) ac.getBean("dpageService");
    }

    private List<String> getSourceHtmlSet() {
        String getAllPageSQL = buildGetAllPagesSQL();
        return dpageService.getPagesContent(getAllPageSQL);
    }

    private List<String> getTempSQLOperation(List<String> pages) {
        Map<String, List<String>> tempData = getTempTableData(pages);
        String tempTableName = getTableName();
        String createSQL = buildCreateTableSQL(tempTableName);
        String insertSQL = buildInsertDataSQL(tempData);
        String querySQL = buildQuerySQL(srcSQL);
        setSelectedColumns(querySQL, metas);
        return dpageService.runAtTemporaryTable(createSQL, insertSQL, querySQL, tempTableName, isAllFlag);
    }

    private List<FilterColumn> handleHTMLParams(Map<String, String> params) {
        List<FilterColumn> columns_meta = new ArrayList<FilterColumn>();
        for (Iterator itr = params.keySet().iterator(); itr.hasNext(); ) {
            String key = (String) itr.next();
            String[] _a1 = params.get(key).split("#");
            _tbSize = Integer.parseInt(_a1[0]);
            String[] _array = _a1[1].split(",");
            _tbSelected = new int[_array.length];
            for (int ix = 0; ix < _array.length; ix++) {
                FilterColumn tfc = new FilterColumn();
                tfc.setPath(_array[ix]);
                _tbSelected[ix] = Integer.parseInt(_array[ix]);
                tfc.setName("C" + ix);
                tfc.setType("VARCHAR(50)");
                columns_meta.add(tfc);
            }
        }
        return columns_meta;
    }

    private void setSelectedColumns(String src, List<FilterColumn> meta) {
        System.out.println(src);
        String query = src.substring(src.indexOf(" ") + 1, src.indexOf("from") - 1);
        String _candidates[] = query.split(",");
        if ("*".equals(_candidates[0])) {
            isAllFlag = true;
            for (FilterColumn _column : meta) {
                _column.setFlag(true);
            }
        } else {
            isAllFlag = false;
            for (FilterColumn _column : meta) _column.setFlag(false);
            for (int i = 0; i < _candidates.length; i++) {
                System.out.println("Selected:" + _candidates[i]);
                for (int ix = 0; ix < meta.size(); ix++) {
                    if (meta.get(ix).getName().equals(_candidates[i])) {
                        meta.get(ix).setFlag(true);
                        System.out.println(meta.get(ix).getName());
                    }
                }
            }
        }
    }

    private boolean equalColumnSize(int cz, Element root) {
        if (root.getChild("TBODY") == null) {
            System.out.println("This is not a table tag");
            return false;
        }
        int currentColumn = 0;
        List<Element> tdlist = root.getChild("TBODY").getChild("TR").getChildren();
        for (Element td : tdlist) {
            if (td.getAttributeValue("colspan") != null) {
                currentColumn += Integer.parseInt(td.getAttributeValue("colspan"));
            } else {
                currentColumn++;
            }
        }
        return cz == currentColumn;
    }

    private void addTableHeaderName(Element tableNode, List<FilterColumn> metas) {
        Element firstRow = tableNode.getChild("TBODY").getChild("TR");
        if (firstRow != null) {
            List<Element> thlist = firstRow.getChildren("TH");
            if (thlist != null && thlist.size() != 0) {
                for (FilterColumn fc : metas) {
                    int ix = Integer.parseInt(fc.getPath());
                    fc.setHeadName(thlist.get(ix - 1).getTextNormalize());
                }
            } else {
                List<Element> tdlist = firstRow.getChildren("TD");
                for (FilterColumn fc : metas) {
                    int ix = Integer.parseInt(fc.getPath());
                    fc.setHeadName(tdlist.get(ix - 1).getTextNormalize());
                }
            }
        }
    }

    private List<Element> findAllTableNode(Element pageNode) {
        List<Element> allTableList = new ArrayList<Element>();
        Queue<Element> equeue = new LinkedList<Element>();
        equeue.add(pageNode);
        while (!equeue.isEmpty()) {
            Element _current = equeue.poll();
            List<Element> _tlist = _current.getChildren();
            if (_current.getChild("TBODY") != null) {
                allTableList.add(_current);
            }
            for (Element _node : _tlist) {
                equeue.add(_node);
            }
        }
        return allTableList;
    }

    private List<Element> findCandidateElement(int columnSize, Element pageNode) {
        List<Element> candList = new ArrayList<Element>();
        for (Element node : findAllTableNode(pageNode)) {
            if (equalColumnSize(columnSize, node)) candList.add(node);
        }
        return candList;
    }

    private List<String> filterTableTagData(int columnSize, int[] selected, Element pageNode) {
        List<String> dataList = new ArrayList<String>();
        List<Element> candList = findCandidateElement(columnSize, pageNode);
        List<HtmlCache> cacheList = new ArrayList<HtmlCache>();
        Element tableNode = candList.get(0);
        List<Element> trlist = tableNode.getChild("TBODY").getChildren("TR");
        addTableHeaderName(tableNode, metas);
        for (int ir = 1; ir < trlist.size(); ir++) {
            List<Element> tdlist = trlist.get(ir).getChildren("TD");
            if (tdlist.size() == columnSize) {
                for (int iy = 0; iy < tdlist.size(); iy++) {
                    if (tdlist.get(iy).getAttributeValue("rowspan") != null) {
                        HtmlCache newCache = new HtmlCache();
                        newCache.setIndex(iy);
                        newCache.setTime(Integer.parseInt(tdlist.get(iy).getAttributeValue("rowspan")) - 1);
                        newCache.setCellContent(tdlist.get(iy).getTextNormalize());
                        cacheList.add(newCache);
                    }
                }
                for (int ix = 0; ix < selected.length; ix++) {
                    dataList.add(tdlist.get(selected[ix] - 1).getTextNormalize());
                }
            } else {
                int _cpnt = 0;
                int _tpnt = 0;
                int _spnt = 0;
                String _currentData = null;
                for (int ix = 0; ix < columnSize; ix++) {
                    if (_cpnt < cacheList.size() && ix == cacheList.get(_cpnt).getIndex()) {
                        _currentData = cacheList.get(_cpnt).getCellContent();
                        cacheList.get(_cpnt).updateAfterUsed();
                        _cpnt++;
                    } else {
                        if (_tpnt < tdlist.size()) {
                            _currentData = tdlist.get(_tpnt).getTextTrim();
                            _tpnt++;
                        }
                    }
                    if (ix == (selected[_spnt] - 1)) {
                        dataList.add(_currentData);
                        _spnt++;
                        if (_spnt == selected.length) break;
                    }
                }
                for (int ix = 0; ix < cacheList.size(); ix++) {
                    if (cacheList.get(ix).isRemovable()) {
                        cacheList.removeAll(cacheList);
                        break;
                    }
                }
            }
        }
        return dataList;
    }

    private Map<String, List<String>> getTempTableData(List<String> pages) {
        List result = new ArrayList();
        for (String src : pages) {
            try {
                Html2DmlEngine engine = new Html2DmlEngine();
                Html2Dml domBuilder = new Html2Dml(src, engine);
                Element root = domBuilder.getDMLDomTree();
                result.add(filterTableTagData(_tbSize, _tbSelected, root));
            } catch (JDOMException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Map<String, List<String>> mapdata = new HashMap<String, List<String>>();
        for (FilterColumn tfc : metas) {
            mapdata.put(tfc.getName(), new ArrayList<String>());
        }
        for (Object _page : result) {
            List<String> pageData = (List<String>) _page;
            for (int ix = 0; ix < pageData.size(); ix++) {
                List _currentList = mapdata.get(metas.get(ix % metas.size()).getName());
                _currentList.add(pageData.get(ix));
            }
        }
        return mapdata;
    }

    private String buildGetAllPagesSQL() {
        String sql = "SELECT content FROM page_version_detail WHERE pageid in " + "(SELECT id FROM page_meta_info p where page_title like '%" + title + "%') GROUP BY pageid DESC";
        return sql;
    }

    private String getTableName() {
        return "mytable";
    }

    private String buildCreateTableSQL(String tabName) {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("CREATE TEMPORARY TABLE IF NOT EXISTS ");
        sqlBuffer.append(tabName + " (");
        sqlBuffer.append("id INTEGER NOT NULL AUTO_INCREMENT,");
        int ix = 0;
        for (ix = 0; ix < metas.size(); ix++) {
            sqlBuffer.append(metas.get(ix).getName());
            sqlBuffer.append(" ");
            sqlBuffer.append(metas.get(ix).getType());
            sqlBuffer.append(",");
        }
        sqlBuffer.append("PRIMARY KEY (id)) ENGINE=HEAP DEFAULT CHARSET=UTF8");
        System.out.println(sqlBuffer.toString());
        return sqlBuffer.toString();
    }

    private String buildQuerySQL(String sql) {
        String tempSQL = sql.replace('@', '\'');
        tempSQL = tempSQL.replace('$', '=');
        tempSQL = tempSQL.replace("#temp", tableName);
        System.out.println(tempSQL);
        return tempSQL;
    }

    private String buildInsertDataSQL(Map<String, List<String>> records) {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("INSERT INTO ");
        sqlBuffer.append(tableName);
        sqlBuffer.append(" (");
        for (Iterator<FilterColumn> mItr = metas.iterator(); mItr.hasNext(); ) {
            sqlBuffer.append(mItr.next().getName());
            if (mItr.hasNext()) sqlBuffer.append(","); else sqlBuffer.append(")");
        }
        sqlBuffer.append(" VALUES ");
        int rowCount = records.get(metas.get(0).getName()).size();
        for (int ix = 0; ix < rowCount; ix++) {
            sqlBuffer.append("(");
            for (Iterator<FilterColumn> _aItr = metas.iterator(); _aItr.hasNext(); ) {
                sqlBuffer.append("'");
                sqlBuffer.append(records.get(_aItr.next().getName()).get(ix));
                if (_aItr.hasNext()) sqlBuffer.append("',"); else sqlBuffer.append("'");
            }
            if (ix != (rowCount - 1)) sqlBuffer.append("),"); else sqlBuffer.append(")");
        }
        System.out.println(sqlBuffer.toString());
        return sqlBuffer.toString();
    }

    private String getDisplayHtml(List<String> records) {
        StringBuffer html = new StringBuffer();
        if (records.size() == 0) {
            return "<div>没有符合要求的结果</div>";
        }
        if (records != null && records.size() > 0) {
            html.append("<ul class=").append("combine").append(">");
            html.append("<table width='100%' cellspacing='0' cellpadding='0' border='1'>");
            html.append("<tr>");
            for (int ix = 0; ix < metas.size(); ix++) {
                if (metas.get(ix).isFlag()) {
                    html.append("<th>");
                    html.append(metas.get(ix).getHeadName());
                    html.append("</th>");
                }
            }
            html.append("</tr>");
            for (Iterator itr = records.iterator(); itr.hasNext(); ) {
                html.append("<tr>");
                for (int ix = 0; ix < metas.size(); ix++) {
                    if (metas.get(ix).isFlag()) {
                        html.append("<td>");
                        html.append(itr.next());
                        html.append("</td>");
                    }
                }
                html.append("</tr>");
            }
            html.append("</table>");
            html.append("</ul>");
        }
        return html.toString();
    }

    private void initFilter(Map params) {
        title = (String) params.get(TITLE_ATTR);
        srcSQL = (String) params.get(QUERY_ATTR);
        tableName = getTableName();
        Iterator mite = params.keySet().iterator();
        Map<String, String> queryCondition = new HashMap<String, String>();
        while (mite.hasNext()) {
            String key = (String) mite.next();
            if ((!key.equals(CLASS_NAME_ATTR)) && (!key.equals(META_TYPE)) && (!key.equals(TITLE_ATTR)) && (!key.equals(QUERY_ATTR))) queryCondition.put(key, (String) params.get(key));
        }
        metas = handleHTMLParams(queryCondition);
    }

    public String runFilter(Map params) {
        initFilter(params);
        List<String> pages = getSourceHtmlSet();
        List<String> queryResult = getTempSQLOperation(pages);
        return getDisplayHtml(queryResult);
    }
}
