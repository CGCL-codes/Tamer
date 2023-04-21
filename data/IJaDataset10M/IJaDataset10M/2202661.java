package com.esa.doxml;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.xml.sax.SAXException;
import com.esa.javabean.Course;
import com.esa.javabean.Student;

public class CreateShareXML {

    private OutPutXML opx = null;

    public File createFile(List<Course> list, String f) throws IOException, SAXException {
        opx = new OutPutXML();
        opx.setFilename("C:\\Users\\LiYan\\Desktop\\xml\\C\\course\\crs.xml");
        Element root = DocumentHelper.createElement("classes");
        Element from = root.addElement("from");
        from.setText("C");
        Element to = root.addElement("to");
        to.setText(f);
        Element order = root.addElement("order");
        order.setText("share");
        Document document = DocumentHelper.createDocument();
        document.setRootElement(root);
        for (Course c : list) {
            root.add(getXML(c));
        }
        return opx.outPut(root);
    }

    public File createFile(List<Course> list) throws IOException, SAXException {
        opx = new OutPutXML();
        opx.setFilename("C:\\Users\\LiYan\\Desktop\\xml\\C\\course\\crs.xml");
        Element root = DocumentHelper.createElement("classes");
        Element from = root.addElement("from");
        from.setText("C");
        Element to = root.addElement("to");
        to.setText("all");
        Element order = root.addElement("order");
        order.setText("share");
        Document document = DocumentHelper.createDocument();
        document.setRootElement(root);
        for (Course c : list) {
            root.add(getXML(c));
        }
        return opx.outPut(root);
    }

    private Element getXML(Course course) {
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("class");
        Element cno = root.addElement("Cno");
        cno.setText(course.getCno());
        Element cname = root.addElement("Cnm");
        cname.setText(course.getCname());
        Element credit = root.addElement("Cpt");
        credit.setText(course.getCredit());
        Element ct = root.addElement("Tec");
        ct.setText(course.getCteacher());
        Element cp = root.addElement("Pla");
        cp.setText(course.getCplace());
        return root;
    }
}
