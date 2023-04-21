package edu.teacmore.lenddoyg;

import java.util.*;
import javax.xml.transform.*;
import edu.webteach.practice.data.*;
import edu.webteach.util.*;
import edu.math.*;
import edu.trees.*;
import org.w3c.dom.*;

/**
 * Created by IntelliJ IDEA.
 * User: SHELEST
 * Date: 13/11/2006
 * Time: 15:43:30
 * To change this template use File | Settings | File Templates.
 */
public class ShowResultLendDoyg implements IShow {

    public String show(Step step) {
        StringBuilder out = new StringBuilder();
        Map in = step.getInputs();
        Map resultMap = (Map) in.get("resultMap");
        Fraction m = (Fraction) in.get("m");
        Map firstStartPoint = (Map) in.get("firstStartPoint");
        out.append("======³������=======<br>");
        ArrayList list = new ArrayList(firstStartPoint.keySet());
        if (resultMap == null) {
            out.append("<br>������ ���� ��������.");
        } else {
            for (int i = 0; i < firstStartPoint.size(); i++) {
                out.append(list.get(i) + " = " + resultMap.get(list.get(i)) + "<br>");
            }
        }
        out.append("�������� �������: " + m + "<br>");
        out.append("======³������=======");
        String XMLString = "";
        Tree tree = (Tree) step.getInputsCopy().get("tree");
        Document document = BinaryTreeUtils.treeToXMLDocument(tree);
        try {
            XMLString = BinaryTreeUtils.parseXMLDocumentToString(document.getDocumentElement());
            System.out.append(XMLString);
        } catch (TransformerException e1) {
            e1.printStackTrace();
        }
        String xmlCodeng = StrUtil.encodeHtml(XMLString);
        System.out.append(xmlCodeng);
        String htmlAppelt = "<br>";
        htmlAppelt += "<script type=\"text/javascript\" language=\"javascript\" src=\"/webteach/js/lenddoyg.js\">\n" + "</script>";
        htmlAppelt += "<applet code=\"main.TreeApplet.class\" MAYSCRIPT archive=\"/webteach/applets/testApplet.jar\" width=\"300\" height=\"100\">";
        htmlAppelt += "<param name=\"xdiameter\" value=\"15\">";
        htmlAppelt += "<param name=\"ydiameter\" value=\"15\">";
        htmlAppelt += "<param name=\"yinterval\" value=\"35\">";
        htmlAppelt += "<param name=\"xmlstring\" value=\"" + xmlCodeng + "\">";
        htmlAppelt += "</applet>";
        htmlAppelt += "<input type=\"hidden\" ID=\"nodeId\" name=\"nodeId\" value=\"\"/>";
        htmlAppelt += "<br>";
        out.append(htmlAppelt);
        return out.toString();
    }
}
