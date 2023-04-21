package cn.nkjobsearch.convert;

import java.sql.*;
import java.util.ArrayList;
import cn.nkjobsearch.mysql.MysqlConn;
import cn.nkjobsearch.publicClass.Print;
import cn.nkjobsearch.publicClass.SearchAndSort;

public class KeywordIndex {

    public static final int keywordNum = 154;

    private MysqlConn mysql = null;

    public int keywordIdInt[] = { 151, 149, 152, 147, 137, 155, 154, 148, 161, 160, 159, 162, 163, 145, 136, 166, 167, 135, 142, 143, 138, 164, 156, 140, 165, 144, 141, 123, 122, 121, 116, 120, 119, 118, 117, 114, 113, 111, 112, 110, 109, 108, 107, 106, 105, 104, 103, 102, 101, 100, 99, 95, 94, 93, 92, 98, 97, 130, 91, 96, 90, 89, 87, 88, 86, 85, 84, 80, 79, 78, 83, 82, 81, 76, 75, 74, 73, 72, 71, 70, 69, 67, 66, 65, 64, 68, 63, 62, 61, 55, 54, 53, 52, 51, 128, 60, 50, 49, 58, 57, 129, 56, 48, 47, 46, 127, 45, 44, 42, 43, 126, 41, 40, 39, 36, 38, 37, 33, 35, 34, 125, 32, 27, 26, 31, 30, 29, 25, 24, 23, 28, 21, 22, 20, 19, 18, 17, 16, 15, 10, 9, 8, 7, 124, 14, 13, 12, 6, 11, 5, 3, 4, 2, 1 };

    public int keywordCount[] = { 501, 401, 0, 432, 412, 401, 19, 372, 401, 230, 48, 69, 373, 197, 393, 264, 219, 119, 750, 264, 262, 239, 147, 117, 363, 411, 310, 907, 361, 593, 867, 247, 646, 760, 486, 238, 548, 867, 867, 716, 487, 329, 515, 268, 779, 599, 495, 744, 271, 433, 434, 946, 806, 207, 702, 447, 566, 198, 756, 245, 563, 922, 748, 792, 878, 482, 626, 580, 819, 352, 324, 522, 966, 741, 182, 779, 652, 720, 743, 779, 369, 589, 657, 568, 716, 310, 797, 246, 730, 695, 883, 393, 673, 608, 819, 1000, 707, 585, 349, 946, 689, 177, 812, 569, 310, 907, 977, 589, 302, 445, 329, 638, 938, 644, 659, 291, 543, 308, 345, 556, 661, 922, 907, 800, 748, 895, 516, 548, 441, 643, 407, 318, 570, 907, 222, 343, 222, 706, 654, 530, 858, 381, 421, 509, 966, 532, 907, 426, 826, 568, 752, 682, 503, 261 };

    public String keywordName[] = { "驱动", "面向对象", "软件", "路由器", "调试", "设计师", "设计", "美工", "网页设计", "网页", "网络", "网站", "维护", "经理", "程序员", "硬件", "游戏", "测试", "汇编", "架构", "服务器", "文档", "数据库", "工程师", "协议", "交换机", "互联网", "zend", "xml", "xhtml", "wml", "windows", "wince", "win", "weblogic", "web", "wap", "vs", "visual studio", "vbscript", "vb", "unix", "tomcat", "test", "tcp", "symbian", "sybase", "swing", "support", "struts", "sqlserver", "sql2005", "sql2000", "sql", "sqa", "spring", "solaris", "software", "soap", "server", "seo", "ruby", "ria", "redhat", "qt", "qa", "python", "pm", "pl", "php", "photoshop", "perl", "pascal", "p2p", "oracle", "opengl", "oop", "ood", "ooad", "ooa", "mysql", "mvc", "mssql", "mfc", "maya", "manager", "mac", "linux", "lamp", "junit", "jstl", "jsp", "jsf", "js", "jquery", "joomla", "jms", "jboss", "javascript", "javaee", "javabean", "java", "j2se", "j2me", "j2ee", "iphone", "infomix", "iis", "html", "hibernate", "hardware", "gui", "gtk", "gsm", "flex", "flash", "fireworks", "erp", "engineer", "eclipse", "driver", "dreamweaver", "dos", "dom", "directx", "direct3d", "delphi", "dba", "db2", "db", "database", "css", "coreldraw", "cgi", "c++", "c#", "c", "b2c", "b2b", "autocad", "atl", "asp.net", "asp", "arm", "applet", "apache", "android", "ajax", "activex", "access", "3g", "3dmax", "3d", ".net" };

    public KeywordIndex(MysqlConn m) {
        mysql = m;
    }

    public KeywordIndex() {
    }

    public boolean putKeywordt2KeywordIndex(String jobinfoId, String title, String jobIntro) {
        int id = Integer.parseInt(jobinfoId);
        jobIntro = jobIntro.toLowerCase();
        title = title.toLowerCase();
        for (int i = 0; i < keywordNum; i++) {
            if (title.indexOf(keywordName[i]) != -1) {
                title = title.replaceAll(keywordName[i], "");
                jobIntro = jobIntro.replaceAll(keywordName[i], "");
                mysql.executeInsert("insert into keywordindex(jobinfoId,keywordId,isTitle) values ('" + id + "','" + keywordIdInt[i] + "','1');");
            } else if (jobIntro.indexOf(keywordName[i]) != -1) {
                jobIntro = jobIntro.replaceAll(keywordName[i], "");
                mysql.executeInsert("insert into keywordindex(jobinfoId,keywordId,isTitle) values ('" + id + "','" + keywordIdInt[i] + "','0');");
            }
        }
        return true;
    }

    /**
	 * @param keywordFromUI 用户输入的查询语句<br/>
	 * 与词库匹配后返回所属词库中词汇的id号
	 * */
    public int[] getKeywordFromUI(String keywordFromUI) {
        ArrayList<int[]> keywordList = new ArrayList<int[]>();
        keywordFromUI = keywordFromUI.toLowerCase();
        for (int i = 0; i < keywordNum; i++) {
            int indexOfKeywordFromUI = keywordFromUI.indexOf(keywordName[i]);
            if (indexOfKeywordFromUI != -1) {
                String replacement = "";
                for (int j = 0; j < keywordName[i].length(); j++) {
                    replacement += "?";
                }
                keywordFromUI = keywordFromUI.replaceAll(keywordName[i], replacement);
                keywordList.add(new int[] { keywordIdInt[i], indexOfKeywordFromUI });
            }
        }
        if (keywordList.size() == 0) {
            return null;
        }
        int[] idSorted = new int[keywordList.size()];
        int[] index = new int[keywordList.size()];
        for (int i = 0; i < keywordList.size(); i++) {
            idSorted[i] = keywordList.get(i)[0];
            index[i] = keywordList.get(i)[1];
        }
        SearchAndSort.QuickSort(index, idSorted, 0, index.length - 1, true);
        return idSorted;
    }
}
