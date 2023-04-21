package com.nhncorp.cubridqa.utils.html;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.nhncorp.cubridqa.console.util.FileUtil;
import com.nhncorp.cubridqa.model.Case;
import com.nhncorp.cubridqa.replication.util.SystemConst;

/**
 * Change function test results to HTML type. 
 * @ClassName: SenerioHtmlGenerator
 * @date 2009-9-4
 * @version V1.0 
 * Copyright (C) www.nhn.com
 */
public class SenerioHtmlGenerator {

    List<String> dirList = new ArrayList();

    private static final String BASE_LOCATION = SystemConst.QA_PATH + "/scenario/html";

    private static String[] paths = new String[] { SystemConst.QA_PATH + "/scenario/sql", SystemConst.QA_PATH + "/scenario/shell", SystemConst.QA_PATH + "/scenario/site", SystemConst.QA_PATH + "/scenario/medium" };

    public static void main(String[] args) {
        if (!new File(BASE_LOCATION).exists()) {
            new File(BASE_LOCATION).mkdirs();
        }
        new SenerioHtmlGenerator().getHtml(paths);
    }

    public void generateTopIndex(String[] paths) {
        StringBuilder sb = new StringBuilder();
        sb.append("<table border='1' cellpadding='5' bordercolor='black' cellspacing='0' style='width:100%'>");
        for (String path : paths) {
            sb.append("<tr><td><a href='").append(getRelativePath(path)).append(".html'>").append(getRelativePath(path));
            sb.append("</a></td></tr>");
        }
        sb.append("</table>");
        FileUtil.writeToFile(BASE_LOCATION + "/index.html", sb.toString());
    }

    public void getHtml(String[] paths) {
        generateTopIndex(paths);
        for (String path : paths) {
            getHtml(new ArrayList(), path);
        }
    }

    public void getHtml(List dirList, String filePath) {
        Case sqlCase = new Case(filePath);
        if (sqlCase.isLeaf()) {
            generateLeafHtml(sqlCase, dirList);
            return;
        }
        dirList.add(getRelativePath(filePath));
        generateDirHtml(dirList, sqlCase);
        for (String subFileOrDir : sqlCase.listSubCategory()) {
            if (subFileOrDir.indexOf(".svn") < 0) {
                getHtml(dirList, subFileOrDir);
            }
        }
        dirList.remove(dirList.size() - 1);
    }

    public void generateDirHtml(List<String> dirList, Case fileCase) {
        String[] fileNames = fileCase.listSubCategory();
        StringBuilder sb = new StringBuilder();
        sb.append("<table border='1' cellpadding='5' bordercolor='black' cellspacing='0' style='width:100%'>");
        sb.append("<tr><td colspan='4'>total: <font color:black>").append(fileCase.getTotal()).append("</font></td></tr>");
        sb.append("<tr style='font-size:15;font-weight:bold' align='left'><td style='width:25%'>case name</td><td style='width:25%'>path</td><td style='width:25%'>description</td><td style='width:25%'>answer/total</td></tr>");
        for (String fileName : fileNames) {
            if (fileName.toLowerCase().indexOf(".svn") < 0) {
                Case childCase = new Case(fileName);
                sb.append("<tr><td>");
                sb.append("<a href='").append(getHtmlFileName(fileName, dirList)).append("'>").append(getRelativePath(fileName)).append("</a>");
                sb.append("</td><td>").append(childCase.getName());
                sb.append("</td><td>").append(childCase.getDescription() == null ? "&nbsp" : childCase.getDescription());
                sb.append("</td><td>").append(childCase.getAnswerRate());
                sb.append("</td></tr>");
            }
        }
        sb.append("</table>");
        FileUtil.writeToFile(BASE_LOCATION + "/" + getHtmlFileName("", dirList), sb.toString());
    }

    public void generateLeafHtml(Case fileCase, List<String> dirList) {
        StringBuilder sb = new StringBuilder();
        List<Case> leafDetails = fileCase.getLeafDetails();
        sb.append("<table border='1' cellpadding='5' bordercolor='black' cellspacing='0' style='width:100%'>");
        sb.append("<tr style='font-size:15;font-weight:bold' align='left'><td style='width:25%'>case name</td><td style='width:25%'>path</td><td style='width:25%'>description</td><td style='width:25%'>answer/total</td></tr>");
        for (Case leafCase : leafDetails) {
            sb.append("<tr><td>");
            sb.append(leafCase.getFileName()).append("</td><td>").append(leafCase.getName()).append("</td><td>").append(leafCase.getDescription()).append("</td><td>").append(leafCase.getAnswerRate()).append("</td>");
            sb.append("</tr>");
        }
        sb.append("</table>");
        FileUtil.writeToFile(BASE_LOCATION + "/" + getHtmlFileName(getRelativePath(fileCase.getName()), dirList), sb.toString());
    }

    public String getHtmlFileName(String fileName, List<String> dirList) {
        StringBuilder sb = new StringBuilder();
        for (String str : dirList) {
            sb.append(str);
        }
        sb.append(getRelativePath(fileName)).append(".html");
        return sb.toString();
    }

    public String getRelativePath(String dir) {
        int lastDashPosition = dir.lastIndexOf("/");
        if (lastDashPosition < 0) return dir;
        return dir.substring(lastDashPosition + 1);
    }
}
