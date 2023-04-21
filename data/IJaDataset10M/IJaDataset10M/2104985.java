package csiebug.web.html.treeview;

import java.util.List;
import java.util.Map;
import javax.naming.NamingException;
import csiebug.util.AssertUtility;
import csiebug.util.ListUtility;
import csiebug.util.StringUtility;
import csiebug.util.WebUtility;
import csiebug.web.html.HtmlBuilder;

/**
 * 產生HTML Sidebar
 * @author George_Tsai
 * @version 2009/8/6
 */
public class HtmlSidebar {

    private String htmlId;

    private List<Map<String, String>> listTreeView;

    private String imagePath;

    private String targetFrame = "mainFrame";

    private WebUtility webutil = new WebUtility();

    /**
	 * Sidebar建構子
	 * @param 由LinkedHashMap(ParentId, Id, Name, URL, ImageName, SortOrder)組成的ArrayList
	 * @author George_Tsai
	 * @version 2009/8/6
	 * @throws NamingException 
	 */
    public HtmlSidebar(List<Map<String, String>> list) throws NamingException {
        listTreeView = list;
        imagePath = webutil.getBasePathForHTML() + "images";
    }

    /**
	 * Sidebar建構子
	 * @param 由LinkedHashMap(ParentId, Id, Name, URL, ImageName, SortOrder)組成的ArrayList
	 * @param css的位置
	 * @param 圖檔的位置
	 * @param js的位置
	 * @param TreeView的Target Frame
	 * @author George_Tsai
	 * @version 2009/1/19
	 * @throws NamingException 
	 */
    public HtmlSidebar(String id, List<Map<String, String>> list, String imagePath, String targetFrame) throws NamingException {
        this.htmlId = id;
        this.listTreeView = list;
        if (imagePath != null) {
            this.imagePath = StringUtility.removeStartEndSlash(imagePath);
        } else {
            this.imagePath = webutil.getBasePathForHTML() + "images";
        }
        if (targetFrame != null) {
            this.targetFrame = targetFrame;
        }
    }

    /**
	 * 取得Sidebar的HTML碼
	 * @return Sidebar的HTML碼
	 * @author George_Tsai
	 * @version 2009/8/6
	 */
    public String getSidebar() {
        HtmlBuilder htmlBuilder = new HtmlBuilder();
        List<Map<String, String>> rootList = ListUtility.getSubListByName(listTreeView, "ParentId", "");
        rootList = ListUtility.sortByName(rootList, "SortOrder");
        for (int i = 0; i < rootList.size(); i++) {
            Map<String, String> treeNode = rootList.get(i);
            String strId = treeNode.get("Id");
            String strName = treeNode.get("Name");
            String strURL = treeNode.get("URL");
            String strImgName = treeNode.get("ImageName");
            if (strURL.equals("")) {
                htmlBuilder.appendString(getFolder(strId, strName, strImgName) + "\n");
            }
        }
        return htmlBuilder.toString();
    }

    /**
	 * 取得程式連結
	 * @param 程式名稱
	 * @param 程式連結
	 * @param icon名稱
	 * @param 層次
	 * @return HTML碼
	 * @author George_Tsai
	 * @version 2009/1/10
	 */
    private String getLeaf(String strName, String strURL, String strImgName) {
        AssertUtility.notNull(strName);
        AssertUtility.notNull(strURL);
        AssertUtility.notNull(strImgName);
        HtmlBuilder htmlBuilder = new HtmlBuilder();
        makeStart(htmlBuilder, "sidebarLeaf");
        if (strImgName.trim().equals("")) {
            htmlBuilder.imgStart().src(imagePath + "/Blank.gif").title(strName).style("CURSOR:pointer").align("absmiddle").onClick("parent." + targetFrame + ".location.href='" + strURL + "'").tagClose().appendString("\n");
        } else {
            htmlBuilder.imgStart().src(imagePath + "/" + strImgName).title(strName).style("CURSOR:pointer").align("absmiddle").onClick("parent." + targetFrame + ".location.href='" + strURL + "'").tagClose().appendString("\n");
        }
        htmlBuilder.aStart().href(strURL).target(targetFrame).tagClose().text(strName).aEnd().space().appendString("\n");
        makeEnd(htmlBuilder);
        return htmlBuilder.toString();
    }

    /**
	 * 取得Folder
	 * @param root id
	 * @param root name
	 * @param icon名稱
	 * @param 層次
	 * @return HTML碼
	 * @author George_Tsai
	 * @version 2009/1/10
	 */
    private String getFolder(String strId, String strName, String strImgName) {
        AssertUtility.notNull(strId);
        AssertUtility.notNull(strName);
        AssertUtility.notNull(strImgName);
        HtmlBuilder htmlBuilder = new HtmlBuilder();
        makeStart(htmlBuilder, "sidebar");
        makeFolderBody(htmlBuilder, strImgName, strId, strName);
        makeEnd(htmlBuilder);
        return htmlBuilder.toString();
    }

    private void makeStart(HtmlBuilder htmlBuilder, String className) {
        AssertUtility.notNull(htmlBuilder);
        AssertUtility.notNull(className);
        htmlBuilder.divStart().className(className).onMouseMove("this.className='" + className + "hover'").onMouseOut("this.className='" + className + "'").tagClose().appendString("\n");
    }

    private void makeFolderBody(HtmlBuilder htmlBuilder, String strImgName, String strId, String strName) {
        AssertUtility.notNull(htmlBuilder);
        AssertUtility.notNull(strImgName);
        AssertUtility.notNull(strId);
        AssertUtility.notNull(strName);
        makeFolderBodyStart(htmlBuilder, strImgName, strId, strName);
        List<Map<String, String>> subList = (List<Map<String, String>>) ListUtility.getSubListByName(listTreeView, "ParentId", strId);
        subList = (List<Map<String, String>>) ListUtility.sortByName(subList, "SortOrder");
        for (int i = 0; i < subList.size(); i++) {
            Map<String, String> treeNode = subList.get(i);
            String strChildName = treeNode.get("Name");
            String strChildURL = treeNode.get("URL");
            String strChildImgName = treeNode.get("ImageName");
            if (!strChildURL.equals("")) {
                htmlBuilder.appendString(getLeaf(strChildName, strChildURL, strChildImgName) + "\n");
            }
        }
        makeFolderBodyEnd(htmlBuilder);
    }

    private void makeFolderBodyStart(HtmlBuilder htmlBuilder, String strImgName, String strId, String strName) {
        AssertUtility.notNull(htmlBuilder);
        AssertUtility.notNull(strImgName);
        AssertUtility.notNull(strId);
        AssertUtility.notNull(strName);
        if (htmlId != null) {
            strId = strId + "_" + htmlId;
        }
        if (strImgName.trim().equals("")) {
            htmlBuilder.imgStart().src(imagePath + "/Blank.gif").title(strName).style("CURSOR:pointer").onClick("openSidebarFolder('" + strId + "')").align("absmiddle").tagClose().appendString("\n");
        } else {
            htmlBuilder.imgStart().src(imagePath + "/" + strImgName).title(strName).style("CURSOR:pointer").onClick("openSidebarFolder('" + strId + "')").align("absmiddle").tagClose().appendString("\n");
        }
        htmlBuilder.spanStart().id(strId + "_folderSidebar").name(strId + "_folderSidebar").title(strName).style("CURSOR:pointer").onClick("openSidebarFolder('" + strId + "')").tagClose().text(strName).spanEnd().appendString("\n");
        htmlBuilder.divStart().id(strId + "_sidebar").name(strId + "_sidebar").style("display:none;line-height:0").tagClose().appendString("\n");
    }

    private void makeFolderBodyEnd(HtmlBuilder htmlBuilder) {
        AssertUtility.notNull(htmlBuilder);
        htmlBuilder.divEnd().appendString("\n");
    }

    private void makeEnd(HtmlBuilder htmlBuilder) {
        AssertUtility.notNull(htmlBuilder);
        htmlBuilder.divEnd().appendString("\n");
    }
}
