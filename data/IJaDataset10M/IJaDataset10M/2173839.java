package jvc.web.action.file;

import jvc.web.action.*;
import jvc.web.action.lucene.HtmlReader;
import jvc.web.module.*;
import jvc.util.DateUtils;
import jvc.util.comparator.FileDateTimeComparator;
import jvc.util.db.MyDB;
import jvc.util.db.page.CustomPage;
import java.util.*;
import java.io.*;
import jvc.util.io.FileNameFilter;
import jvc.util.log.Logger;

public class HtlmFileListAction implements BaseAction {

    private static Logger logger = Logger.getLogger(HtlmFileListAction.class.getName());

    public String Excute(ActionContent input, ActionContent output, MyDB mydb) {
        JVCResult vResult = new JVCResult();
        vResult.AddColumn(new Field(Field.FT_STRING, "name", "name"));
        vResult.AddColumn(new Field(Field.FT_STRING, "size", "size"));
        vResult.AddColumn(new Field(Field.FT_STRING, "ext", "ext"));
        vResult.AddColumn(new Field(Field.FT_STRING, "title", "title"));
        vResult.AddColumn(new Field(Field.FT_STRING, "date", "date"));
        try {
            String FilePath = input.getParam("files.path");
            String CurPath = input.getParam("files.curpath");
            if (!CurPath.equals("")) {
                CurPath = CurPath.replaceAll("[.][.]", "");
                CurPath = CurPath.replaceAll("//", "/");
                if (CurPath.endsWith("/")) CurPath = CurPath.substring(0, CurPath.length() - 1);
            }
            if (CurPath.equals("")) CurPath = "/";
            logger.debug(FilePath + CurPath);
            output.setParam("files.curpath", CurPath);
            File f = new File(FilePath + CurPath);
            File[] fs = f.listFiles(new FileNameFilter(input.getParam("files.filter")));
            if (fs.length > 1) Arrays.sort(fs, new FileDateTimeComparator());
            int recordsperpage = input.getInt("recordsperpage", 10);
            int curpage = input.getInt(input.getParam("name") + ".curpage", 1);
            CustomPage cp = new CustomPage();
            cp.init(fs.length, recordsperpage, curpage);
            cp.setParamMap(input.getParamMapFromrequest(), input.getParamNamesFromRequest());
            cp.setResultName(input.getParam("name"));
            cp.setPageUrl(input.getParam("jvcpagename") + ".page?cmd=" + input.getParam("cmd"));
            if (fs.length > 0) {
                for (int i = cp.getStartIndex() - 1; i < cp.getEndIndex(); i++) {
                    if (!fs[i].isDirectory()) {
                        Map map = new LinkedHashMap();
                        map.put("name", fs[i].getName());
                        map.put("size", String.valueOf(fs[i].length()));
                        int iDot = fs[i].getName().lastIndexOf("[.]");
                        String ext = "";
                        if (iDot > 0) ext = fs[i].getName().substring(iDot, fs[i].getName().length());
                        map.put("ext", ext);
                        HtmlReader hb = new HtmlReader(new FileInputStream(fs[i]));
                        map.put("title", hb.getTitle());
                        map.put("date", DateUtils.toTimestamp(fs[i].lastModified()));
                        vResult.AddResult(map);
                    }
                }
            }
            output.setParam(input.getParam("name"), vResult);
            output.setParam(input.getParam("name") + ".page", cp);
            return input.getParam("success");
        } catch (Exception e) {
            e.printStackTrace();
            return input.getParam("fault");
        }
    }

    public static void main(String[] args) {
        ActionContent input = new ActionContent();
        input.setParam("files.path", "D:\\eclipse\\workspace\\itil\\kmsdata\\temp");
        input.setParam("files.filter", ".htm");
        input.setParam("name", "res");
        ActionContent output = new ActionContent();
        new HtlmFileListAction().Excute(input, output, new MyDB("defaultdb"));
        Object obj = output.getParamObj("res");
        System.out.println(obj);
        jvc.web.component.Table table = new jvc.web.component.Table(obj);
        System.out.println(table);
        while (table.next()) {
            System.out.println(table.getField("title"));
        }
    }
}
