package it.unipr.ce.dsg.deus.automator;

import it.unipr.ce.dsg.deus.core.Deus;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.InetAddress;
import java.util.ArrayList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * @author Marco Picone (picone.m@gmail.com)
 * 
 */
public class RunnerCommandLine implements Runnable {

    private Document document;

    private Document doc;

    private String originalXML = "";

    private int numSim = 0;

    private String originalXml = "";

    private String automatorXml = "";

    public RunnerCommandLine(String originalXml, String automatorXml) {
        this.originalXml = originalXml;
        this.automatorXml = automatorXml;
    }

    private static boolean DelDir2(File dir) {
        if (dir.isDirectory()) {
            String[] contenuto = dir.list();
            for (int i = 0; i < contenuto.length; i++) {
                if (new File(dir + "/" + contenuto[i]).exists()) new File(dir + "/" + contenuto[i]).delete();
            }
        }
        return true;
    }

    /**
	 * This method starts Deus
	 * 
	 * @param originalXml
	 *            , name of the base configuration file
	 * @param automatorXml
	 *            , name of the file that automates the simulations
	 * @throws ParserConfigurationException
	 */
    public int start(String originalXml, String automatorXml) throws DeusAutomatorException, JAXBException, SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        File f = new File(originalXml);
        DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.parse(f);
        originalXML = originalXml;
        FileInputStream fis = new FileInputStream(originalXML);
        FileOutputStream fos = new FileOutputStream(originalXML + ".temp");
        byte[] dati = new byte[fis.available()];
        fis.read(dati);
        fos.write(dati);
        fis.close();
        fos.close();
        DelDir2(new File("./xml"));
        ArrayList<String> files = new ArrayList<String>();
        ArrayList<MyObjectSimulation> simulations = readXML(automatorXml);
        files = writeXML(simulations, originalXml);
        int numFile = 0;
        ArrayList<String> logFile = new ArrayList<String>();
        File simfile = new File("simulations");
        FileInputStream simfileInputStream = new FileInputStream(simfile);
        InputStreamReader isr = new InputStreamReader(simfileInputStream);
        BufferedReader br = new BufferedReader(isr);
        char[] cbuf = new char[simfileInputStream.available()];
        br.read(cbuf);
        String computerName = "";
        try {
            computerName = InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            System.out.println("Exception caught =" + e.getMessage());
        }
        for (int j = 0; j < simulations.size(); j++) {
            for (int k = 0; k < simulations.get(j).getSimulationNumber(); k++) {
                for (int i = 0; i < new Integer(simulations.get(j).getSimulationNumberSeed()); i++) {
                    new Deus(files.get(numFile), simulations.get(j).getFileLog());
                    File log = new File(simulations.get(j).getFileLog());
                    log.renameTo(new File("./temp/" + computerName + "-" + simulations.get(j).getSimulationName() + "-" + k + "-" + simulations.get(j).getEngine().get(j).getSeed().get(i)));
                    log.delete();
                    logFile.add("./temp/" + computerName + "-" + simulations.get(j).getSimulationName() + "-" + k + "-" + simulations.get(j).getEngine().get(j).getSeed().get(i));
                    numFile++;
                }
                ResultAutomator resultAutomator = new ResultAutomator(logFile);
                String averageFileName = "";
                String varFileName = "";
                String sqrtvarFileName = "";
                try {
                    resultAutomator.readTotalResults();
                    averageFileName = "./results//" + computerName + "_Average_" + simulations.get(j).getSimulationName() + "-" + k;
                    varFileName = "./results//" + computerName + "_Var_" + simulations.get(j).getSimulationName() + "-" + k;
                    sqrtvarFileName = "./results//" + computerName + "_SqrtVar_" + simulations.get(j).getSimulationName() + "-" + k;
                    resultAutomator.resultsAverage(averageFileName);
                    resultAutomator.resultsVar(varFileName, sqrtvarFileName, averageFileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                logFile.clear();
                for (int z = 0; z < simulations.get(j).getGnuplot().size(); z++) {
                    System.out.println("Writing: " + simulations.get(j).getSimulationName() + "-" + simulations.get(j).getGnuplot().get(z).getFileName() + "-" + k);
                    writeGnuPlot(averageFileName, simulations.get(j).getSimulationName() + "-" + simulations.get(j).getGnuplot().get(z).getFileName() + "-" + k, simulations.get(j).getGnuplot().get(z).getAxisX(), simulations.get(j).getGnuplot().get(z).getAxisY());
                }
            }
        }
        if (files.size() == 0) {
            new Deus(originalXml, "deus_log");
        }
        for (int i = 0; i < files.size(); i++) new File(files.get(i)).delete();
        return 0;
    }

    /**
	 * Method for writing gnuplot files
	 * 
	 * @param sourceFile
	 *            , name of the file from which data must be read
	 * @param destinationFile
	 *            , name of the file to which results must be written
	 * @param axisX
	 * @param axisY
	 */
    private void writeGnuPlot(String sourceFile, String destinationFile, String axisX, String axisY) {
        try {
            File f = new File(sourceFile);
            FileInputStream fis = new FileInputStream(f);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String computerName = "";
            try {
                computerName = InetAddress.getLocalHost().getHostName();
            } catch (Exception e) {
                System.out.println("Exception caught =" + e.getMessage());
            }
            FileOutputStream fos = new FileOutputStream("./results/gnuplot/" + computerName + "_" + destinationFile);
            String linea = br.readLine();
            linea = linea.trim();
            axisX = axisX.trim();
            axisY = axisY.trim();
            String app = "";
            boolean assex = false;
            boolean assey = false;
            while (linea != null) {
                String firstString = linea.split("=")[0];
                if (firstString.equals(axisX) && assey == false) {
                    assex = true;
                    String val = linea.substring(linea.indexOf('=') + 1, linea.length());
                    val = val + " ";
                    fos.write(val.getBytes());
                } else if (firstString.equals(axisY) && assex == true) {
                    assey = true;
                    String val = linea.substring(linea.indexOf('=') + 1, linea.length());
                    val = val + " ";
                    fos.write(val.getBytes());
                    fos.write("\n".getBytes());
                    assex = false;
                    assey = false;
                } else if (firstString.equals(axisY) && assex == false) {
                    assey = true;
                    app = linea.substring(linea.indexOf('=') + 1, linea.length());
                    app = app + " ";
                } else if (firstString.equals(axisX) && assey == true) {
                    String val = linea.substring(linea.indexOf('=') + 1, linea.length());
                    val = val + " ";
                    fos.write(val.getBytes());
                    fos.write(app.getBytes());
                    fos.write("\n".getBytes());
                    assex = false;
                    assey = false;
                }
                linea = br.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Method that computes the paramaters of the simulations
	 * 
	 * @param initialValue
	 *            , initila value
	 * @param finalValue
	 *            , final value
	 * @param stepValue
	 *            , step
	 * @return ArrayList<> contains all the values between initialValue and
	 *         finalValue
	 */
    private ArrayList<Float> calculateParameters(String initialValue, String finalValue, String stepValue) {
        ArrayList<Float> parameters = new ArrayList<Float>();
        if (Float.parseFloat(stepValue) != 0 && (Float.parseFloat(initialValue) < Float.parseFloat(finalValue))) for (float i = Float.parseFloat(initialValue); i <= Float.parseFloat(finalValue) + 0.01; i = i + Float.parseFloat(stepValue)) {
            parameters.add(i);
        } else if (Float.parseFloat(stepValue) != 0 && (Float.parseFloat(initialValue) > Float.parseFloat(finalValue))) for (float i = Float.parseFloat(initialValue); i >= Float.parseFloat(finalValue); i = i - Float.parseFloat(stepValue)) {
            parameters.add(i);
        }
        return parameters;
    }

    /**
	 * Method that reads the XML file for the atuomation of simulations
	 * 
	 * @param path
	 *            , path of the file to read
	 * @return an ArrayList<> containing the simulations to perform
	 * @throws DeusAutomatorException
	 * @throws JAXBException
	 * @throws SAXException
	 * @throws IOException
	 */
    private ArrayList<MyObjectSimulation> readXML(String path) throws DeusAutomatorException, JAXBException, SAXException, IOException {
        JAXBContext jc = JAXBContext.newInstance("it.unipr.ce.dsg.deus.schema.automator");
        SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        Schema schema = schemaFactory.newSchema(new File("schema/automator/deusAutomator.xsd"));
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        unmarshaller.setSchema(schema);
        unmarshaller.setEventHandler(new ValidationEventHandler() {

            public boolean handleEvent(ValidationEvent ve) {
                if (ve.getSeverity() == ValidationEvent.FATAL_ERROR || ve.getSeverity() == ValidationEvent.ERROR || ve.getSeverity() == ValidationEvent.WARNING) {
                    ValidationEventLocator locator = ve.getLocator();
                    System.out.println("Invalid configuration file: " + locator.getURL());
                    System.out.println("Error at column " + locator.getColumnNumber() + ", line " + locator.getLineNumber());
                    System.out.println("Error: " + ve.getMessage());
                    return false;
                }
                return true;
            }
        });
        unmarshaller.unmarshal(new File(path));
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            File f = new File(path);
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(f);
            document.getDocumentElement().normalize();
            NodeList simulationLst = document.getElementsByTagName("simulation");
            ArrayList<MyObjectSimulation> simulation = new ArrayList<MyObjectSimulation>();
            for (int w = 0; w < simulationLst.getLength(); w++) {
                MyObjectSimulation sim = new MyObjectSimulation();
                Node fstSimulation = simulationLst.item(w);
                if (fstSimulation.getAttributes().getNamedItem("simulationName").getNodeValue() == null || fstSimulation.getAttributes().getNamedItem("simulationNumberSeed").getNodeValue() == null) throw new DeusAutomatorException("Errore manca simulationNumberSeed e/o simulationName nel tag simulation");
                String resultFolder = null;
                String inputFolder = null;
                String simulationNumberSeed = fstSimulation.getAttributes().getNamedItem("simulationNumberSeed").getNodeValue();
                String simulationName = fstSimulation.getAttributes().getNamedItem("simulationName").getNodeValue();
                if (fstSimulation.getAttributes().getNamedItem("resultFolder") != null) resultFolder = fstSimulation.getAttributes().getNamedItem("resultFolder").getNodeValue();
                if (fstSimulation.getAttributes().getNamedItem("inputFolder") != null) inputFolder = fstSimulation.getAttributes().getNamedItem("inputFolder").getNodeValue();
                sim.setResultFolder(resultFolder);
                sim.setInputFolder(inputFolder);
                sim.setSimulationName(simulationName);
                sim.setSimulationNumberSeed(simulationNumberSeed);
                NodeList nodeLst = document.getElementsByTagName("node");
                ArrayList<ArrayList<MyObjectNode>> nodes2 = new ArrayList<ArrayList<MyObjectNode>>();
                ArrayList<ArrayList<MyObjectProcess>> processes2 = new ArrayList<ArrayList<MyObjectProcess>>();
                for (int s = 0; s < nodeLst.getLength(); s++) {
                    Node fstNode = nodeLst.item(s);
                    if (fstNode.getParentNode().equals(simulationLst.item(w))) {
                        ArrayList<MyObjectNode> nodes = new ArrayList<MyObjectNode>();
                        String messageType = fstNode.getAttributes().getNamedItem("id").getNodeValue();
                        Element fstElmnt = (Element) fstNode;
                        NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("paramName");
                        for (int j = 0; j < fstNmElmntLst.getLength(); j++) {
                            Element paramElement = (Element) fstNmElmntLst.item(j);
                            String paramName = ((Node) fstNmElmntLst.item(j)).getAttributes().getNamedItem("name").getNodeValue();
                            NodeList initialValue = paramElement.getElementsByTagName("initialValue");
                            NodeList finalValue = paramElement.getElementsByTagName("finalValue");
                            NodeList stepValue = paramElement.getElementsByTagName("stepValue");
                            if (initialValue == null || finalValue == null || stepValue == null) {
                                throw new DeusAutomatorException("Errore in initalValue , finalValue e stepValue in " + simulationName + " di Node " + messageType + " in " + paramName);
                            }
                            ArrayList<Float> value = calculateParameters(initialValue.item(0).getTextContent(), finalValue.item(0).getTextContent(), stepValue.item(0).getTextContent());
                            if (value.size() > 0) sim.setStep(value.size());
                            if (Float.parseFloat(stepValue.item(0).getTextContent()) == 0.0) {
                                MyObjectParam paramToWrite = new MyObjectParam();
                                paramToWrite.setObjectParam("paramName");
                                paramToWrite.setObjectName(paramName);
                                paramToWrite.setObjectValue(Float.parseFloat(initialValue.item(0).getTextContent()));
                                MyObjectNode nodeToWrite = new MyObjectNode();
                                nodeToWrite.setObjectName(messageType);
                                nodeToWrite.getObjectParam().add(paramToWrite);
                                writeXmlNodeParam(nodeToWrite);
                            }
                            if (numSim == 0) numSim = value.size();
                            if (numSim != value.size()) {
                                throw new DeusAutomatorException("Errore nel numero di step in " + simulationName + " di Node " + messageType + " in " + paramName);
                            }
                            for (int k = 0; k < value.size(); k++) {
                                MyObjectParam param = new MyObjectParam();
                                param.setObjectParam("paramName");
                                param.setObjectName(paramName);
                                param.setObjectValue(value.get(k));
                                if (nodes.size() > k) nodes.get(k).getObjectParam().add(param); else {
                                    MyObjectNode node = new MyObjectNode();
                                    node.setObjectName(messageType);
                                    node.getObjectParam().add(param);
                                    nodes.add(node);
                                }
                            }
                        }
                        NodeList paramName = fstElmnt.getElementsByTagName("resourceParamName");
                        for (int j = 0; j < paramName.getLength(); j++) {
                            Element paramElement = (Element) paramName.item(j);
                            String handlerName = ((Node) paramName.item(j)).getAttributes().getNamedItem("handlerName").getNodeValue();
                            String resParamValueName = ((Node) paramName.item(j)).getAttributes().getNamedItem("resParamValue").getNodeValue();
                            NodeList initialValue = paramElement.getElementsByTagName("initialValue");
                            NodeList finalValue = paramElement.getElementsByTagName("finalValue");
                            NodeList stepValue = paramElement.getElementsByTagName("stepValue");
                            if (initialValue == null || finalValue == null || stepValue == null) {
                                throw new DeusAutomatorException("Errore in initalValue , finalValue e stepValue in " + simulationName + " di Node" + messageType + " in " + paramName);
                            }
                            ArrayList<Float> value = calculateParameters(initialValue.item(0).getTextContent(), finalValue.item(0).getTextContent(), stepValue.item(0).getTextContent());
                            if (value.size() > 0) sim.setStep(value.size());
                            if (Float.parseFloat(stepValue.item(0).getTextContent()) == 0.0) {
                                MyObjectResourceParam resourceParam = new MyObjectResourceParam();
                                resourceParam.setObjectParam("resourceParamName");
                                resourceParam.setObjectHandlerName(handlerName);
                                resourceParam.setResParamValue(resParamValueName);
                                resourceParam.setObjectValue(Float.parseFloat(initialValue.item(0).getTextContent()));
                                MyObjectNode nodeToWrite = new MyObjectNode();
                                nodeToWrite.setObjectName(messageType);
                                nodeToWrite.getObjectResourceParam().add(resourceParam);
                                writeXmlNodeResource(nodeToWrite);
                            }
                            if (numSim == 0) numSim = value.size();
                            if (numSim != value.size()) {
                                throw new DeusAutomatorException("Errore nel numero di step in " + simulationName + " di Node " + messageType + " in " + paramName);
                            }
                            for (int k = 0; k < value.size(); k++) {
                                MyObjectResourceParam resourceParam = new MyObjectResourceParam();
                                resourceParam.setObjectParam("resourceParamName");
                                resourceParam.setObjectHandlerName(handlerName);
                                resourceParam.setResParamValue(resParamValueName);
                                resourceParam.setObjectValue(value.get(k));
                                if (nodes.size() > k) nodes.get(k).getObjectResourceParam().add(resourceParam); else {
                                    MyObjectNode node = new MyObjectNode();
                                    node.setObjectName(messageType);
                                    node.getObjectResourceParam().add(resourceParam);
                                    nodes.add(node);
                                }
                            }
                        }
                        nodes2.add(nodes);
                    }
                }
                if (nodes2.size() > 0) sim.setNode(nodes2);
                if (new Integer(sim.getNode().size()) > 0) sim.setSimulationNumber(new Integer(sim.getNode().get(0).size()));
                NodeList processLst = document.getElementsByTagName("process");
                for (int s = 0; s < processLst.getLength(); s++) {
                    Node fstNode = processLst.item(s);
                    if (fstNode.getParentNode().equals(simulationLst.item(w))) {
                        ArrayList<MyObjectProcess> processes = new ArrayList<MyObjectProcess>();
                        String messageType = fstNode.getAttributes().getNamedItem("id").getNodeValue();
                        Element fstElmnt = (Element) fstNode;
                        NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("paramName");
                        for (int j = 0; j < fstNmElmntLst.getLength(); j++) {
                            Element paramElement = (Element) fstNmElmntLst.item(j);
                            String paramName = ((Node) fstNmElmntLst.item(j)).getAttributes().getNamedItem("name").getNodeValue();
                            NodeList initialValue = paramElement.getElementsByTagName("initialValue");
                            NodeList finalValue = paramElement.getElementsByTagName("finalValue");
                            NodeList stepValue = paramElement.getElementsByTagName("stepValue");
                            if (initialValue == null || finalValue == null || stepValue == null) {
                                throw new DeusAutomatorException("Errore in initalValue , finalValue e stepValue in " + simulationName + " di Process " + messageType + " in " + paramName);
                            }
                            ArrayList<Float> value = calculateParameters(initialValue.item(0).getTextContent(), finalValue.item(0).getTextContent(), stepValue.item(0).getTextContent());
                            if (value.size() > 0) sim.setStep(value.size());
                            if (Float.parseFloat(stepValue.item(0).getTextContent()) == 0.0) {
                                MyObjectParam param = new MyObjectParam();
                                param.setObjectParam("paramName");
                                param.setObjectName(paramName);
                                param.setObjectValue(Float.parseFloat(initialValue.item(0).getTextContent()));
                                MyObjectProcess processToWrite = new MyObjectProcess();
                                processToWrite.setObjectName(messageType);
                                processToWrite.getObjectParam().add(param);
                                writeXmlProcess(processToWrite);
                            }
                            if (numSim == 0) numSim = value.size();
                            if (numSim != value.size()) {
                                throw new DeusAutomatorException("Errore nel numero di step in " + simulationName + " di Process " + messageType + " in " + paramName);
                            }
                            for (int k = 0; k < value.size(); k++) {
                                MyObjectParam param = new MyObjectParam();
                                param.setObjectParam("paramName");
                                param.setObjectName(paramName);
                                param.setObjectValue(value.get(k));
                                if (processes.size() > k) processes.get(k).getObjectParam().add(param); else {
                                    MyObjectProcess process = new MyObjectProcess();
                                    process.setObjectName(messageType);
                                    process.getObjectParam().add(param);
                                    processes.add(process);
                                }
                            }
                        }
                        if (processes.size() > 0) processes2.add(processes);
                    }
                }
                if (processes2.size() > 0) sim.setProcess(processes2);
                numSim = 0;
                if (sim.getSimulationNumber() == 0) if (sim.getProcess().size() > 0) sim.setSimulationNumber(new Integer(sim.getProcess().get(0).size()));
                NodeList engineLst = document.getElementsByTagName("engine");
                for (int s = 0; s < engineLst.getLength(); s++) {
                    MyObjectEngine engine = new MyObjectEngine();
                    Node fstNode = engineLst.item(s);
                    if (fstNode.getParentNode().equals(simulationLst.item(w))) {
                        String startVt = "";
                        String endVt = "";
                        String stepVt = "";
                        ArrayList<Float> value = new ArrayList<Float>();
                        boolean vt = true;
                        if (fstNode.getAttributes().getNamedItem("startVT") != null) startVt = fstNode.getAttributes().getNamedItem("startVT").getNodeValue(); else vt = false;
                        if (fstNode.getAttributes().getNamedItem("endVT") != null) endVt = fstNode.getAttributes().getNamedItem("endVT").getNodeValue(); else vt = false;
                        if (fstNode.getAttributes().getNamedItem("stepVT") != null) stepVt = fstNode.getAttributes().getNamedItem("stepVT").getNodeValue(); else vt = false;
                        if (vt) value = calculateParameters(startVt, endVt, stepVt);
                        for (int k = 0; k < value.size(); k++) {
                            engine.getVt().add(value.get(k));
                        }
                        Element fstElmnt = (Element) fstNode;
                        NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("seed");
                        for (int j = 0; j < fstNmElmntLst.getLength(); j++) {
                            Element paramElement = (Element) fstNmElmntLst.item(j);
                            NodeList seedValue = paramElement.getElementsByTagName("seedValue");
                            if (seedValue.getLength() > Integer.parseInt(sim.getSimulationNumberSeed())) {
                                throw new DeusAutomatorException("Errore nel numero di seed inseriti, TROPPI rispetto a quelli indicati nel tag simulation in " + simulationName);
                            }
                            if (seedValue.getLength() < Integer.parseInt(sim.getSimulationNumberSeed())) {
                                throw new DeusAutomatorException("Errore nel numero di seed inseriti, POCHI rispetto a quelli indicati nel tag simulation in " + simulationName);
                            }
                            for (int o = 0; o < seedValue.getLength(); o++) {
                                Node seedvalue = seedValue.item(o);
                                engine.getSeed().add(seedvalue.getTextContent());
                            }
                        }
                    }
                    sim.getEngine().add(engine);
                }
                if (sim.getSimulationNumber() == 0) sim.setSimulationNumber(1);
                NodeList resultLogLst = document.getElementsByTagName("resultVT");
                for (int i = 0; i < resultLogLst.getLength(); i++) {
                    Node fileLog = resultLogLst.item(i);
                    if (fileLog.getParentNode().equals(simulationLst.item(w))) {
                        sim.setFileLog(fileLog.getAttributes().getNamedItem("outputLogFile").getNodeValue());
                    }
                }
                NodeList GnuPlotLst = document.getElementsByTagName("resultXYFile");
                for (int i = 0; i < GnuPlotLst.getLength(); i++) {
                    Node GnuPlotNode = GnuPlotLst.item(i);
                    if (GnuPlotNode.getParentNode().equals(simulationLst.item(w))) {
                        MyObjectGnuplot gnuplot = new MyObjectGnuplot();
                        gnuplot.setFileName(GnuPlotNode.getAttributes().getNamedItem("fileName").getNodeValue());
                        gnuplot.setAxisX(GnuPlotNode.getAttributes().getNamedItem("axisX").getNodeValue());
                        gnuplot.setAxisY(GnuPlotNode.getAttributes().getNamedItem("axisY").getNodeValue());
                        sim.getGnuplot().add(gnuplot);
                    }
                }
                simulation.add(sim);
            }
            return simulation;
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void writeXmlNodeParam(MyObjectNode nodeToWrite) throws IOException, ParserConfigurationException, SAXException {
        FileOutputStream fos = new FileOutputStream(originalXML + ".temp");
        NodeList node = doc.getElementsByTagName("aut:node");
        for (int i = 0; i < node.getLength(); i++) {
            if (node.item(i).getAttributes().getNamedItem("id").getNodeValue().equals(nodeToWrite.getObjectName())) {
                for (int l = 0; l < nodeToWrite.getObjectParam().size(); l++) {
                    for (int m = 0; m < node.item(i).getChildNodes().getLength(); m++) for (int b = 0; b < node.item(i).getChildNodes().item(m).getChildNodes().getLength(); b++) if (node.item(i).getChildNodes().item(m).getChildNodes().item(b).getNodeName().equals("aut:param")) if (node.item(i).getChildNodes().item(m).getChildNodes().item(b).getAttributes().getNamedItem("name").getNodeValue().equals(nodeToWrite.getObjectParam().get(l).getObjectName())) {
                        node.item(i).getChildNodes().item(m).getChildNodes().item(b).getAttributes().getNamedItem("value").setNodeValue(((Double) nodeToWrite.getObjectParam().get(l).getObjectValue()).toString());
                    }
                }
            }
        }
        DOMSource domSource = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = tf.newTransformer();
            transformer.transform(domSource, result);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        fos.write(writer.toString().getBytes());
    }

    private void writeXmlProcess(MyObjectProcess processToWrite) throws IOException, ParserConfigurationException, SAXException {
        FileOutputStream fos = new FileOutputStream(originalXML + ".temp");
        NodeList process = doc.getElementsByTagName("aut:process");
        for (int i = 0; i < process.getLength(); i++) {
            if (process.item(i).getAttributes().getNamedItem("id").getNodeValue().equals(processToWrite.getObjectName())) {
                for (int l = 0; l < processToWrite.getObjectParam().size(); l++) for (int m = 0; m < process.item(i).getChildNodes().getLength(); m++) {
                    if (process.item(i).getChildNodes().item(m).getNodeName().equals("aut:params")) for (int b = 0; b < process.item(i).getChildNodes().item(m).getChildNodes().getLength(); b++) if (process.item(i).getChildNodes().item(m).getChildNodes().item(b).getNodeName().equals("aut:param")) {
                        if (process.item(i).getChildNodes().item(m).getChildNodes().item(b).getAttributes().getNamedItem("name").getNodeValue().equals(processToWrite.getObjectParam().get(l).getObjectName())) {
                            process.item(i).getChildNodes().item(m).getChildNodes().item(b).getAttributes().getNamedItem("value").setNodeValue(((Double) processToWrite.getObjectParam().get(l).getObjectValue()).toString());
                        }
                    }
                }
            }
        }
        DOMSource domSource = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = tf.newTransformer();
            transformer.transform(domSource, result);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        fos.write(writer.toString().getBytes());
    }

    private void writeXmlNodeResource(MyObjectNode nodeToWrite) throws IOException, ParserConfigurationException, SAXException {
        FileOutputStream fos = new FileOutputStream(originalXML + ".temp");
        NodeList node = doc.getElementsByTagName("aut:node");
        for (int i = 0; i < node.getLength(); i++) {
            if (node.item(i).getAttributes().getNamedItem("id").getNodeValue().equals(nodeToWrite.getObjectName())) {
                for (int l = 0; l < nodeToWrite.getObjectResourceParam().size(); l++) {
                    for (int m = 0; m < node.item(i).getChildNodes().getLength(); m++) {
                        if (node.item(i).getChildNodes().item(m).getNodeName().equals("aut:resources")) ;
                        for (int v = 0; v < node.item(i).getChildNodes().item(m).getChildNodes().getLength(); v++) if (node.item(i).getChildNodes().item(m).getChildNodes().item(v).getNodeName().equals("aut:resource") && node.item(i).getChildNodes().item(m).getChildNodes().item(v).getAttributes().getNamedItem("handler").getNodeValue().equals(nodeToWrite.getObjectResourceParam().get(l).getObjectHandlerName())) for (int z = 0; z < node.item(i).getChildNodes().item(m).getChildNodes().item(v).getChildNodes().getLength(); z++) if (node.item(i).getChildNodes().item(m).getChildNodes().item(v).getChildNodes().item(z).getNodeName().equals("aut:params")) for (int x = 0; x < node.item(i).getChildNodes().item(m).getChildNodes().item(v).getChildNodes().item(z).getChildNodes().getLength(); x++) if (node.item(i).getChildNodes().item(m).getChildNodes().item(v).getChildNodes().item(z).getChildNodes().item(x).getNodeName().equals("aut:param")) if (node.item(i).getChildNodes().item(m).getChildNodes().item(v).getChildNodes().item(z).getChildNodes().item(x).getAttributes().getNamedItem("name").getNodeValue().equals("amount")) {
                            node.item(i).getChildNodes().item(m).getChildNodes().item(v).getChildNodes().item(z).getChildNodes().item(x).getAttributes().getNamedItem("value").setNodeValue(((Double) nodeToWrite.getObjectResourceParam().get(l).getObjectValue()).toString());
                        }
                    }
                }
            }
        }
        DOMSource domSource = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = tf.newTransformer();
            transformer.transform(domSource, result);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        fos.write(writer.toString().getBytes());
    }

    /**
	 * Method that writes the XML files to be used when Deus is launched
	 * 
	 * @param simulation
	 *            , the list of simulations to perform
	 * @param path
	 *            , path of the base XML file to modify
	 * @return the names of the files to run
	 * @throws JAXBException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
    private ArrayList<String> writeXML(ArrayList<MyObjectSimulation> simulation, String path) throws JAXBException, SAXException, IOException, ParserConfigurationException {
        ArrayList<String> xmlFile = new ArrayList<String>();
        try {
            FileOutputStream simul = new FileOutputStream("simulations");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            doc.getDocumentElement().normalize();
            for (int j = 0; j < simulation.size(); j++) {
                int end = 0;
                File f = new File(path + ".temp");
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(f);
                end = simulation.get(j).getStep();
                if (simulation.get(j).getNode().size() > 0 || simulation.get(j).getProcess().size() > 0) for (int k = 0; k < end; k++) {
                    simul.write(("\n" + simulation.get(j).getSimulationName() + "-" + k + "\n").getBytes());
                    for (int u = 0; u < simulation.get(j).getNode().size(); u++) {
                        NodeList node = doc.getElementsByTagName("aut:node");
                        for (int i = 0; i < node.getLength(); i++) {
                            if (simulation.get(j).getNode().get(u).size() > 0) if (node.item(i).getAttributes().getNamedItem("id").getNodeValue().equals(simulation.get(j).getNode().get(u).get(k).getObjectName())) {
                                simul.write(("Node : " + node.item(i).getAttributes().getNamedItem("id").getNodeValue() + "\n").getBytes());
                                for (int l = 0; l < simulation.get(j).getNode().get(u).get(k).getObjectParam().size(); l++) {
                                    for (int m = 0; m < node.item(i).getChildNodes().getLength(); m++) for (int b = 0; b < node.item(i).getChildNodes().item(m).getChildNodes().getLength(); b++) if (node.item(i).getChildNodes().item(m).getChildNodes().item(b).getNodeName().equals("aut:param")) if (node.item(i).getChildNodes().item(m).getChildNodes().item(b).getAttributes().getNamedItem("name").getNodeValue().equals(simulation.get(j).getNode().get(u).get(k).getObjectParam().get(l).getObjectName())) {
                                        simul.write(("Parameter : " + node.item(i).getChildNodes().item(m).getChildNodes().item(b).getAttributes().getNamedItem("name").getNodeValue() + " ").getBytes());
                                        simul.write((((Double) simulation.get(j).getNode().get(u).get(k).getObjectParam().get(l).getObjectValue()).toString() + "\n").getBytes());
                                        node.item(i).getChildNodes().item(m).getChildNodes().item(b).getAttributes().getNamedItem("value").setNodeValue(((Double) simulation.get(j).getNode().get(u).get(k).getObjectParam().get(l).getObjectValue()).toString());
                                    }
                                }
                                for (int l = 0; l < simulation.get(j).getNode().get(u).get(k).getObjectResourceParam().size(); l++) {
                                    for (int m = 0; m < node.item(i).getChildNodes().getLength(); m++) {
                                        if (node.item(i).getChildNodes().item(m).getNodeName().equals("aut:resources")) ;
                                        for (int v = 0; v < node.item(i).getChildNodes().item(m).getChildNodes().getLength(); v++) if (node.item(i).getChildNodes().item(m).getChildNodes().item(v).getNodeName().equals("aut:resource") && node.item(i).getChildNodes().item(m).getChildNodes().item(v).getAttributes().getNamedItem("handler").getNodeValue().equals(simulation.get(j).getNode().get(u).get(k).getObjectResourceParam().get(l).getObjectHandlerName())) for (int z = 0; z < node.item(i).getChildNodes().item(m).getChildNodes().item(v).getChildNodes().getLength(); z++) if (node.item(i).getChildNodes().item(m).getChildNodes().item(v).getChildNodes().item(z).getNodeName().equals("aut:params")) for (int x = 0; x < node.item(i).getChildNodes().item(m).getChildNodes().item(v).getChildNodes().item(z).getChildNodes().getLength(); x++) if (node.item(i).getChildNodes().item(m).getChildNodes().item(v).getChildNodes().item(z).getChildNodes().item(x).getNodeName().equals("aut:param")) if (node.item(i).getChildNodes().item(m).getChildNodes().item(v).getChildNodes().item(z).getChildNodes().item(x).getAttributes().getNamedItem("name").getNodeValue().equals("amount")) {
                                            simul.write(("Resource Parameter : " + node.item(i).getChildNodes().item(m).getChildNodes().item(v).getChildNodes().item(z).getChildNodes().item(x).getAttributes().getNamedItem("name").getNodeValue() + " ").getBytes());
                                            simul.write((((Double) simulation.get(j).getNode().get(u).get(k).getObjectResourceParam().get(l).getObjectValue()).toString() + "\n").getBytes());
                                            node.item(i).getChildNodes().item(m).getChildNodes().item(v).getChildNodes().item(z).getChildNodes().item(x).getAttributes().getNamedItem("value").setNodeValue(((Double) simulation.get(j).getNode().get(u).get(k).getObjectResourceParam().get(l).getObjectValue()).toString());
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (simulation.get(j).getProcess().size() > 0) {
                        for (int g = 0; g < simulation.get(j).getProcess().size(); g++) {
                            NodeList process = doc.getElementsByTagName("aut:process");
                            for (int i = 0; i < process.getLength(); i++) {
                                if (simulation.get(j).getProcess().get(g).size() > 0) if (process.item(i).getAttributes().getNamedItem("id").getNodeValue().equals(simulation.get(j).getProcess().get(g).get(k).getObjectName())) {
                                    simul.write(("Process : " + process.item(i).getAttributes().getNamedItem("id").getNodeValue() + "\n").getBytes());
                                    for (int l = 0; l < simulation.get(j).getProcess().get(g).get(k).getObjectParam().size(); l++) for (int m = 0; m < process.item(i).getChildNodes().getLength(); m++) {
                                        if (process.item(i).getChildNodes().item(m).getNodeName().equals("aut:params")) for (int b = 0; b < process.item(i).getChildNodes().item(m).getChildNodes().getLength(); b++) if (process.item(i).getChildNodes().item(m).getChildNodes().item(b).getNodeName().equals("aut:param")) {
                                            if (process.item(i).getChildNodes().item(m).getChildNodes().item(b).getAttributes().getNamedItem("name").getNodeValue().equals(simulation.get(j).getProcess().get(g).get(k).getObjectParam().get(l).getObjectName())) {
                                                simul.write(("Parameter : " + process.item(i).getChildNodes().item(m).getChildNodes().item(b).getAttributes().getNamedItem("name").getNodeValue() + " ").getBytes());
                                                simul.write((((Double) simulation.get(j).getProcess().get(g).get(k).getObjectParam().get(l).getObjectValue()).toString() + "\n").getBytes());
                                                process.item(i).getChildNodes().item(m).getChildNodes().item(b).getAttributes().getNamedItem("value").setNodeValue(((Double) simulation.get(j).getProcess().get(g).get(k).getObjectParam().get(l).getObjectValue()).toString());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    for (int seed = 0; seed < simulation.get(j).getEngine().get(j).getSeed().size(); seed++) {
                        NodeList engine = doc.getElementsByTagName("aut:engine");
                        for (int i = 0; i < engine.getLength(); i++) {
                            engine.item(i).getAttributes().getNamedItem("seed").setNodeValue(simulation.get(j).getEngine().get(j).getSeed().get(seed));
                        }
                        DOMSource domSource = new DOMSource(doc);
                        StringWriter writer = new StringWriter();
                        StreamResult result = new StreamResult(writer);
                        TransformerFactory tf = TransformerFactory.newInstance();
                        Transformer transformer;
                        try {
                            transformer = tf.newTransformer();
                            transformer.transform(domSource, result);
                        } catch (TransformerConfigurationException e) {
                            e.printStackTrace();
                        } catch (TransformerException e) {
                            e.printStackTrace();
                        }
                        String filename = "./xml/" + simulation.get(j).getSimulationName() + "_" + k + "_" + simulation.get(j).getEngine().get(j).getSeed().get(seed);
                        FileOutputStream file = new FileOutputStream(filename);
                        file.write(writer.toString().getBytes());
                        file.close();
                        xmlFile.add(filename);
                    }
                }
                if (simulation.get(j).getProcess().size() < 1 && simulation.get(j).getNode().size() < 1) for (int seed = 0; seed < simulation.get(j).getEngine().get(j).getSeed().size(); seed++) {
                    NodeList engine = doc.getElementsByTagName("aut:engine");
                    for (int i = 0; i < engine.getLength(); i++) {
                        engine.item(i).getAttributes().getNamedItem("seed").setNodeValue(simulation.get(j).getEngine().get(j).getSeed().get(seed));
                    }
                    DOMSource domSource = new DOMSource(doc);
                    StringWriter writer = new StringWriter();
                    StreamResult result = new StreamResult(writer);
                    TransformerFactory tf = TransformerFactory.newInstance();
                    Transformer transformer;
                    try {
                        transformer = tf.newTransformer();
                        transformer.transform(domSource, result);
                    } catch (TransformerConfigurationException e) {
                        e.printStackTrace();
                    } catch (TransformerException e) {
                        e.printStackTrace();
                    }
                    String filename = "./xml/" + simulation.get(j).getSimulationName() + "_0" + "_" + simulation.get(j).getEngine().get(j).getSeed().get(seed);
                    FileOutputStream file = new FileOutputStream(filename);
                    file.write(writer.toString().getBytes());
                    xmlFile.add(filename);
                }
            }
            simul.close();
            if (new File(path + ".temp").exists()) new File(path + ".temp").delete();
            return xmlFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (new File(path + ".temp").exists()) new File(path + ".temp").delete();
        return null;
    }

    public void run() {
        try {
            start(originalXml, automatorXml);
        } catch (DeusAutomatorException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
}
