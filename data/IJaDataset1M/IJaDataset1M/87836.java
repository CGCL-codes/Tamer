package tafat.control;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import tafat.engine.Console;
import tafat.engine.DateParser;
import tafat.engine.Scene;
import tafat.engine.Time;
import tafat.engine.statechart.JavaStatechartUpdater;
import tafat.engine.timeout.TimeoutManager;

public class Main {

    private static Main instance = new Main();

    private String modelFilename;

    public static Scene scene = new Scene();

    public static int time = 0;

    public static void main(String args[]) {
        instance.modelFilename = "F:/single.xml";
        try {
            Time.createInstance(DateParser.parseDateAndTime("02/01/1995 00:00:00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        TimeoutManager.createInstance();
        JavaStatechartUpdater.createInstance();
        instance.execute();
    }

    private void execute() {
        DocumentBuilder builder;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(modelFilename);
            NodeList nodeList = document.getDocumentElement().getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                String nodeName = node.getNodeName().toLowerCase();
                short nodeType = node.getNodeType();
                if ((nodeType == Node.ELEMENT_NODE) && (nodeName.equals("scene"))) scene.load(node);
            }
            Console.out("Scene loaded");
            Date date;
            date = new Date();
            Console.out("Starting simulation" + date.toString());
            TimeoutManager timeoutManagerInstance = TimeoutManager.getInstance();
            JavaStatechartUpdater statechartUpdaterInstance = JavaStatechartUpdater.getInstance();
            for (int i = 0; i < 1 * 24 * 60 * 60; i++) {
                time = i;
                timeoutManagerInstance.tick();
                statechartUpdaterInstance.tick();
                scene.tick(i);
            }
            scene.terminate();
            date = new Date();
            Console.out("Simulation finished" + date.toString());
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
