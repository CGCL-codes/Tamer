package jp.locky.research.locdep;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class HtmlMaker {

    public static void writeHTML(LDISortList[] list) {
        int infoLeng = LocationDependentData.locDepData.length;
        String file = "�ʒu�ˑ��f�[�^/loc.html";
        try {
            File htmlFile = new File(file);
            FileWriter writer = new FileWriter(htmlFile);
            writer.write("<HTML>\n<HEAD>\n</HEAD>\n<BODY>\n");
            writer.write("<script>\n" + "function ReloadAddr(){\n" + "location.reload();\n" + "}\n" + "</script>\n");
            writer.write("<table border=4 width=400 align=center>\n<caption>�y�ʒu�ˑ����z</caption>\n");
            writer.write("<tr bgcolor=\"#cccccc\">\n");
            writer.write("<th>����</th>\n");
            writer.write("<th>�Ώ�</th>\n");
            writer.write("<th>img</th>\n</tr>");
            for (int i = 0; i < 30; i++) {
                for (int k = 0; k < list.length; k++) {
                    if (list[k].listNum == i) {
                        writer.write("<tr align=center>\n");
                        writer.write("<td>" + (int) (list[k].dis * 100000) + "m</td>\n");
                        writer.write("<td>" + LocationDependentData.locDepData[list[k].locNum].objectName + "<br>��ʁF" + LocationDependentData.locDepData[list[k].locNum].objectType + "<br>�Ŋ��w�F" + LocationDependentData.locDepData[list[k].locNum].nSta + "</td>\n");
                        writer.write("<td><a href=\"http://www.google.co.jp/maps?q=" + LocationDependentData.locDepData[list[k].locNum].latitude + "," + LocationDependentData.locDepData[list[k].locNum].longitude + "(" + LocationDependentData.locDepData[list[k].locNum].objectName + ")&z=19\" target=\"_blank\"><img src=\"" + LocationDependentData.defdir.getAbsolutePath().split("�ʒu�ˑ��f�[�^")[0] + LocationDependentData.locDepData[list[k].locNum].defPicName + "\"width=70 height=70></td>\n");
                        writer.write("</tr>\n");
                    }
                }
            }
            writer.write("</table>\n<br>\n");
            writer.write("<script>\n" + "setTimeout(\"ReloadAddr()\",5000);\n" + "</script>\n");
            writer.write("</BODY>\n</HTML>");
            writer.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
