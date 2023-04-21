package csiebug.web.webapp.example.taglib.treeview;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.naming.NamingException;
import csiebug.service.ServiceException;
import csiebug.util.AssertUtility;
import csiebug.web.webapp.BasicAction;

public class TreeViewAction extends BasicAction {

    private static final long serialVersionUID = 1L;

    public String main() throws Exception {
        makeControl();
        return FORMLOAD;
    }

    private void makeControl() throws UnsupportedEncodingException, NamingException {
        try {
            setRequestAttribute("FunctionName", getFunctionName());
        } catch (ServiceException se) {
            writeErrorLog(se);
            if (AssertUtility.isNotNullAndNotSpace(getFunctionId())) {
                setRequestAttribute("FunctionName", getFunctionId());
            } else {
                setRequestAttribute("FunctionName", getMessage("taglibdemo.TreeViewDoc.FunctionName"));
            }
        }
        makeTree();
    }

    private void makeTree() {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        makeFormFolder(list);
        makeGridFolder(list);
        makeTreeViewFolder(list);
        makeTabFolder(list);
        makeProgressBarFolder(list);
        makeChartFolder(list);
        setRequestAttribute("treeView", list);
    }

    private void makeDoc(List<Map<String, String>> list, String parentId, String id, String name, String url, String order) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("ParentId", parentId);
        map.put("Id", id);
        map.put("Name", name);
        map.put("URL", url);
        map.put("SortOrder", order);
        map.put("ImageName", "");
        list.add(map);
    }

    private void makeFormFolder(List<Map<String, String>> list) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("ParentId", "");
        map.put("Id", "form");
        map.put("Name", "Form");
        map.put("URL", "");
        map.put("SortOrder", "1");
        map.put("ImageName", "");
        list.add(map);
        makeFormDocList(list);
    }

    private void makeFormDocList(List<Map<String, String>> list) {
        makeDoc(list, "form", "text", "Text", "", "1");
        makeDoc(list, "form", "textArea", "Text Area", "textAreaAction", "2");
        makeDoc(list, "form", "textInterval", "Text Interval", "textIntervalAction", "3");
        makeDoc(list, "form", "select", "Select", "selectAction", "4");
        makeDoc(list, "form", "multiSelect", "Multi Select", "multiSelectAction", "5");
        makeDoc(list, "form", "editableSelect", "Editable Select", "editableSelectAction", "6");
        makeDoc(list, "form", "radio", "Radio", "radioAction", "7");
        makeDoc(list, "form", "radioGroup", "Radio Group", "radioGroupAction", "8");
        makeDoc(list, "form", "checkbox", "Checkbox", "checkboxAction", "9");
    }

    private void makeGridFolder(List<Map<String, String>> list) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("ParentId", "");
        map.put("Id", "grid");
        map.put("Name", "Grid");
        map.put("URL", "");
        map.put("SortOrder", "2");
        map.put("ImageName", "");
        list.add(map);
        makeGridDocList(list);
    }

    private void makeGridDocList(List<Map<String, String>> list) {
        makeDoc(list, "grid", "table", "Table", "tableAction", "1");
        makeDoc(list, "grid", "row", "Row", "rowAction", "2");
        makeDoc(list, "grid", "column", "Column", "columnAction", "3");
        makeDoc(list, "grid", "columns", "Columns", "columnsAction", "4");
    }

    private void makeTreeViewFolder(List<Map<String, String>> list) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("ParentId", "");
        map.put("Id", "treeViewFolder");
        map.put("Name", "Tree View");
        map.put("URL", "");
        map.put("SortOrder", "3");
        map.put("ImageName", "");
        list.add(map);
        makeTreeViewDocList(list);
    }

    private void makeTreeViewDocList(List<Map<String, String>> list) {
        makeDoc(list, "treeViewFolder", "treeView", "Tree View", "treeViewAction", "1");
        makeDoc(list, "treeViewFolder", "checkTreeView", "Check Tree View", "checkTreeViewAreaAction", "2");
        makeDoc(list, "text", "menu", "Menu", "menuAction", "3");
    }

    private void makeTabFolder(List<Map<String, String>> list) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("ParentId", "");
        map.put("Id", "tabFolder");
        map.put("Name", "Tab");
        map.put("URL", "");
        map.put("SortOrder", "4");
        map.put("ImageName", "");
        list.add(map);
        makeTabDocList(list);
    }

    private void makeTabDocList(List<Map<String, String>> list) {
        makeDoc(list, "tabFolder", "tab", "Tab", "tabAction", "1");
    }

    private void makeProgressBarFolder(List<Map<String, String>> list) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("ParentId", "");
        map.put("Id", "progressBarFolder");
        map.put("Name", "Progress Bar");
        map.put("URL", "");
        map.put("SortOrder", "5");
        map.put("ImageName", "");
        list.add(map);
        makeProgressBarDocList(list);
    }

    private void makeProgressBarDocList(List<Map<String, String>> list) {
        makeDoc(list, "progressBarFolder", "progressBar", "Progress Bar", "progressBarAction", "1");
    }

    private void makeChartFolder(List<Map<String, String>> list) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("ParentId", "");
        map.put("Id", "chart");
        map.put("Name", "Chart");
        map.put("URL", "");
        map.put("SortOrder", "6");
        map.put("ImageName", "");
        list.add(map);
        makeChartDocList(list);
    }

    private void makeChartDocList(List<Map<String, String>> list) {
        makeDoc(list, "chart", "xmlSwfChart", "XML-SWF Chart", "xmlSwfChartAction", "1");
    }
}
