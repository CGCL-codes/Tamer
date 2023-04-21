package es.ulpgc.dis.heuriskein.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import es.ulpgc.dis.heuriskein.model.readerswriters.XMLHeuristicReader;

public class Script {

    ArrayList<String> operators;

    ArrayList<String> selectors;

    public Script() {
        operators = new ArrayList<String>();
        selectors = new ArrayList<String>();
    }

    public void operate(ArrayList<String> directories) throws ClassNotFoundException {
        Class operatorClass = Class.forName("es.ulpgc.dis.heuriskein.model.solver.Operator");
        Class selectorClass = Class.forName("es.ulpgc.dis.heuriskein.model.solver.Selector");
        String basepath = getClass().getProtectionDomain().getCodeSource().getLocation().getFile().toString();
        for (String packageName : directories) {
            String splitted[] = packageName.split("\\.");
            String separator = System.getProperty("file.separator");
            String fullpath = basepath;
            for (String str : splitted) {
                fullpath += str + separator;
            }
            java.io.File file = new java.io.File(fullpath);
            String fileList[] = file.list();
            for (String element : fileList) {
                String filename = element.toString();
                String className = filename.split("\\.")[0];
                String normalizedName = packageName + "." + className;
                try {
                    Class myClass = Class.forName(normalizedName);
                    myClass.newInstance();
                    if (operatorClass.isAssignableFrom(myClass)) {
                        operators.add(normalizedName);
                    }
                    if (selectorClass.isAssignableFrom(myClass)) {
                        selectors.add(normalizedName);
                    }
                } catch (Exception e) {
                }
            }
        }
        System.out.println(operators);
        System.out.println(selectors);
        escupirloXML();
    }

    private void escupirloXML() {
        Document document = new Document();
        XMLOutputter outp = new XMLOutputter();
        Element root = new Element("Heuristics");
        Element operatorElement = new Element("OperatorsList");
        for (String operator : operators) {
            Element ele = new Element("Operator");
            ele.setText(operator);
            operatorElement.addContent(ele);
        }
        Element selectorElement = new Element("SelectorsList");
        for (String selector : selectors) {
            Element ele = new Element("Selector");
            ele.setText(selector);
            selectorElement.addContent(ele);
        }
        root.addContent(operatorElement);
        root.addContent(selectorElement);
        document.setRootElement(root);
        outp.setFormat(Format.getPrettyFormat());
        try {
            outp.output(document, new FileOutputStream("c:\\proyecto\\workspace\\heuriskein\\configuration\\heuristics.xml"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        XMLHeuristicReader.read("c:\\proyecto\\workspace\\heuriskein\\configuration\\heuristics.xml");
    }

    public static void main(String args[]) {
        ArrayList<String> directories = new ArrayList<String>();
        directories.add("es.ulpgc.dis.heuriskein.model.solver.antz.operators");
        directories.add("es.ulpgc.dis.heuriskein.model.solver.antz.selectors");
        directories.add("es.ulpgc.dis.heuriskein.model.solver.ga.operators");
        directories.add("es.ulpgc.dis.heuriskein.model.solver.ga.selectors");
        Script script = new Script();
        try {
            script.operate(directories);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
