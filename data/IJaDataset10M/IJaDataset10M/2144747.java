package org.micthemodel.asciiIO;

import org.micthemodel.elements.Material;
import org.micthemodel.elements.Product;
import org.micthemodel.elements.Reactant;
import org.micthemodel.elements.Reaction;
import org.micthemodel.elements.Reactor;
import org.micthemodel.factory.Parameters;
import org.micthemodel.elements.ModelGrain;
import org.micthemodel.comparators.StringCaseIndependentComparator;
import org.micthemodel.imageIO.Images;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.micthemodel.plugins.MicPlugin;
import org.micthemodel.plugins.packer.FlocPacker;

/**
 * This class reads the XML input to the model.
 *
 * The XML files contain the settings and parameters for
 * simulations.
 * @author bishnoi
 */
public class XMLInputReader {

    public NodeList pluginNodeList;

    private LinkedList<String> resourceFolders;

    /** Creates a new instance of XMLInputReader */
    public XMLInputReader() {
    }

    private Reactor createReactor(Element document) {
        Parameters parameters = new Parameters();
        this.readCommands(document.getElementsByTagName("command"), parameters);
        Parameters.getOut().println("Doing for hydration = " + parameters.start());
        Reactor reactor = this.readReactor(document.getElementsByTagName("reactor"), parameters);
        if (reactor == null) {
            return null;
        }
        this.readMaterials(document.getElementsByTagName("material"), reactor);
        this.readModelGrains(document.getElementsByTagName("modelGrain"), reactor);
        this.readReactions(document.getElementsByTagName("reaction"), reactor);
        this.readPlugins(document.getElementsByTagName("plugin"), reactor);
        reactor.initialise();
        this.readComments(document.getElementsByTagName("comments"));
        return reactor;
    }

    private void readCommands(NodeList commandNodes, Parameters parameters) {
        parameters.setDoBurning(false);
        String[] commands = new String[commandNodes.getLength()];
        for (int i = 0; i < commandNodes.getLength(); i++) {
            commands[i] = ((Element) commandNodes.item(i)).getTextContent();
        }
        Comparator<String> stringComparator = new StringCaseIndependentComparator();
        Arrays.sort(commands, stringComparator);
        if (Arrays.binarySearch(commands, "doBurning", stringComparator) >= 0) {
            parameters.setDoBurning(true);
            parameters.setVectorBurningAxis(0);
            parameters.setSaveElements(true);
        }
        if (Arrays.binarySearch(commands, "doBurningX", stringComparator) >= 0) {
            parameters.setDoBurning(true);
            parameters.setVectorBurningAxis(0);
            parameters.setSaveElements(true);
        }
        if (Arrays.binarySearch(commands, "doBurningY", stringComparator) >= 0) {
            parameters.setDoBurning(true);
            parameters.setVectorBurningAxis(1);
            parameters.setSaveElements(true);
        }
        if (Arrays.binarySearch(commands, "doBurningZ", stringComparator) >= 0) {
            parameters.setDoBurning(true);
            parameters.setVectorBurningAxis(2);
            parameters.setSaveElements(true);
        }
        if (Arrays.binarySearch(commands, "noBurning", stringComparator) >= 0) {
            parameters.setDoBurning(false);
            parameters.setSavePixels(true);
        }
        if (Arrays.binarySearch(commands, "markPores", stringComparator) >= 0) {
            parameters.setMarkPores(true);
            parameters.setSavePixels(true);
        }
        if (Arrays.binarySearch(commands, "markNoPores", stringComparator) >= 0) {
            parameters.setMarkPores(false);
            parameters.setSavePixels(true);
        }
        if (Arrays.binarySearch(commands, "doContacts", stringComparator) >= 0) {
            parameters.setCalculateSurface(true);
        }
        if (Arrays.binarySearch(commands, "noContacts", stringComparator) >= 0) {
            parameters.setCalculateSurface(false);
        }
        if (Arrays.binarySearch(commands, "doContact", stringComparator) >= 0) {
            parameters.setCalculateSurface(true);
        }
        if (Arrays.binarySearch(commands, "noContact", stringComparator) >= 0) {
            parameters.setCalculateSurface(false);
        }
        if (Arrays.binarySearch(commands, "saveElements", stringComparator) >= 0) {
            parameters.setSaveElements(true);
        }
        if (Arrays.binarySearch(commands, "noElements", stringComparator) >= 0) {
            parameters.setSaveElements(false);
        }
        if (Arrays.binarySearch(commands, "savePixels", stringComparator) >= 0) {
            parameters.setSavePixels(true);
        }
        if (Arrays.binarySearch(commands, "noPixels", stringComparator) >= 0) {
            parameters.setSavePixels(false);
        }
        if (Arrays.binarySearch(commands, "pixelsNoHeaders", stringComparator) >= 0) {
            parameters.setSavePixels(true);
            parameters.setPixelsNoHeader(true);
        }
        if (Arrays.binarySearch(commands, "redoPixels", stringComparator) >= 0) {
            parameters.setRedoPixels(true);
            parameters.setStart(false);
            parameters.setRestart(false);
        }
        if (Arrays.binarySearch(commands, "hydrate", stringComparator) >= 0 || Arrays.binarySearch(commands, "start", stringComparator) >= 0) {
            parameters.setRedoPixels(false);
            parameters.setStart(true);
            parameters.setRestart(false);
        }
        if (Arrays.binarySearch(commands, "restartHydration", stringComparator) >= 0 || Arrays.binarySearch(commands, "restart", stringComparator) >= 0) {
            parameters.setRedoPixels(false);
            parameters.setStart(false);
            parameters.setRestart(true);
        }
        if (Arrays.binarySearch(commands, "doVectorBurning", stringComparator) >= 0) {
            parameters.setDoVectorBurning(true);
        }
        if (Arrays.binarySearch(commands, "noVectorBurning", stringComparator) >= 0) {
            parameters.setDoVectorBurning(false);
        }
        if (Arrays.binarySearch(commands, "readStep", stringComparator) >= 0) {
            for (int i = 0; i < commandNodes.getLength(); i++) {
                Element command = ((Element) commandNodes.item(i));
                String commandName = command.getTextContent();
                if (commandName.equalsIgnoreCase("readStep") && parameters.getReadStep() <= 0) {
                    parameters.setReadStep((int) Integer.valueOf(command.getAttribute("step")));
                    break;
                }
            }
        }
        if (Arrays.binarySearch(commands, "restartHydration", stringComparator) >= 0 || Arrays.binarySearch(commands, "restart", stringComparator) >= 0) {
            for (int i = 0; i < commandNodes.getLength(); i++) {
                Element command = ((Element) commandNodes.item(i));
                String commandName = command.getTextContent();
                if (commandName.startsWith("restart") && parameters.getRestartFromStep() <= 0 && command.hasAttribute("fromStep")) {
                    parameters.setRestartFromStep((int) Integer.valueOf(command.getAttribute("fromStep")));
                }
                break;
            }
        }
    }

    public Reactor readFile(String fileName) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(new File(fileName));
            NodeList setupElements = document.getElementsByTagName("micinput");
            Element setupElement;
            if (setupElements.getLength() == 0) {
                setupElements = document.getElementsByTagName("setup");
                if (setupElements.getLength() == 0) {
                    return null;
                }
                setupElement = (Element) setupElements.item(0);
            } else {
                setupElement = (Element) setupElements.item(0);
                NodeList def1 = setupElement.getElementsByTagName("setup");
                if (def1.getLength() > 0) {
                    setupElement = (Element) def1.item(0);
                }
            }
            return this.createReactor(setupElement);
        } catch (IOException ex) {
            Parameters.getOut().println("Error reading input file");
            ex.printStackTrace();
        } catch (ParserConfigurationException ex) {
            Parameters.getOut().println("Error reading input file");
            ex.printStackTrace();
        } catch (SAXException ex) {
            Parameters.getOut().println("Error reading input file");
            ex.printStackTrace();
        }
        return null;
    }

    private void readMaterials(NodeList materialNodes, Reactor reactor) {
        Parameters.getOut().println("There are " + materialNodes.getLength() + " materials");
        for (int i = 0; i < materialNodes.getLength(); i++) {
            Element materialElement = (Element) materialNodes.item(i);
            Material material = new Material(reactor);
            reactor.setName(material, materialElement.getAttribute("name"));
            if (materialElement.hasAttribute("class")) {
                material.setMaterialClass(materialElement.getAttribute("class"));
            }
            if (materialElement.hasAttribute("id")) {
                reactor.setIndex(material, Integer.parseInt(materialElement.getAttribute("id")));
            }
            material.setDensity(Double.parseDouble(materialElement.getElementsByTagName("density").item(0).getTextContent()));
            if (materialElement.getElementsByTagName("userDefinedParameter").getLength() > 0) {
                material.setUserDefinedParameter(Double.parseDouble(materialElement.getElementsByTagName("userDefinedParameter").item(0).getTextContent()));
            } else {
                if (materialElement.getElementsByTagName("diffusivity").getLength() > 0) {
                    material.setUserDefinedParameter(Double.parseDouble(materialElement.getElementsByTagName("diffusivity").item(0).getTextContent()));
                } else {
                    material.setUserDefinedParameter(0.0);
                }
            }
            NodeList list = materialElement.getElementsByTagName("inner");
            if (list.getLength() > 0) {
                material.setInner(Boolean.parseBoolean(list.item(0).getTextContent()));
            }
            list = materialElement.getElementsByTagName("universal");
            if (list.getLength() > 0) {
                material.setUniversal(Boolean.parseBoolean(list.item(0).getTextContent()));
            }
            list = materialElement.getElementsByTagName("discrete");
            if (list.getLength() > 0) {
                material.setDiscrete(Boolean.parseBoolean(list.item(0).getTextContent()));
            }
            list = materialElement.getElementsByTagName("hasVariant");
            if (list.getLength() > 0) {
                material.setHasVariant(Boolean.parseBoolean(list.item(0).getTextContent()));
            }
            list = materialElement.getElementsByTagName("initialVolumeFraction");
            if (list.getLength() == 0) {
                list = materialElement.getElementsByTagName("initialAmount");
            }
            if (list.getLength() > 0) {
                String value = list.item(0).getTextContent();
                if (!value.equalsIgnoreCase("remaining")) {
                    material.setInitialFraction(Double.parseDouble(list.item(0).getTextContent()));
                }
            }
        }
        reactor.generateMaterialIds();
        for (int i = 0; i < materialNodes.getLength(); i++) {
            Element materialElement = (Element) materialNodes.item(i);
            String name = materialElement.getAttribute("name");
            Material material = reactor.getMaterial(name);
            Parameters.getOut().println("name = " + name);
            if (!material.isDiscrete()) {
                NodeList list = materialElement.getElementsByTagName("hostName");
                if (list.getLength() > 0) {
                    material.setHostMaterial(reactor.getMaterial(list.item(0).getTextContent()));
                }
            }
            if (material.hasVariant()) {
                NodeList list = materialElement.getElementsByTagName("variantName");
                if (list.getLength() > 0) {
                    material.setVariant(reactor.getMaterial(list.item(0).getTextContent()));
                }
            }
            NodeList list = materialElement.getElementsByTagName("color");
            if (list.getLength() > 0) {
                material.setColor(list.item(0).getTextContent(), reactor);
            }
            list = materialElement.getElementsByTagName("replace");
            for (int j = 0; j < list.getLength(); j++) {
                String replaceName = list.item(j).getTextContent();
                if ("0".equalsIgnoreCase(replaceName)) {
                    if (material.getReplaceOriginalMaterials() == null) {
                        material.setReplaceOriginalMaterials(new LinkedList<Material>());
                    }
                } else {
                    if (material.getReplaceOriginalMaterials() == null) {
                        material.setReplaceOriginalMaterials(new LinkedList<Material>());
                    }
                    material.getReplaceOriginalMaterials().add(reactor.getMaterial(replaceName));
                }
            }
            list = materialElement.getElementsByTagName("initialAmount");
            if (list.getLength() > 0) {
                String value = list.item(0).getTextContent();
                if (value.equalsIgnoreCase("remaining")) {
                    double total = 1.0;
                    for (Material otherMaterial : reactor.getMaterials()) {
                        total -= otherMaterial.getInitialFraction();
                    }
                    material.setInitialFraction(total);
                }
            }
        }
    }

    private void readModelGrains(NodeList modelGrainNodes, Reactor reactor) {
        Parameters.getOut().println("There are " + modelGrainNodes.getLength() + " models available");
        for (int i = 0; i < modelGrainNodes.getLength(); i++) {
            ModelGrain model = new ModelGrain(reactor);
            Element grainElement = (Element) modelGrainNodes.item(i);
            reactor.setName(model, grainElement.getAttribute("name"));
            NodeList layerList = grainElement.getElementsByTagName("layer");
            for (int j = 0; j < layerList.getLength(); j++) {
                model.addLayer(reactor.getMaterial(layerList.item(j).getTextContent()));
            }
            NodeList psdFileList = grainElement.getElementsByTagName("filePSD");
            if (psdFileList.getLength() > 0) {
                String psdFileName = psdFileList.item(0).getTextContent();
                File psdFile = new File(psdFileName);
                if (!psdFile.exists()) {
                    psdFile = new File(reactor.getParameters().getFileFolder() + psdFileName);
                    if (psdFile.exists()) {
                        psdFileName = reactor.getParameters().getFileFolder() + psdFileName;
                    } else {
                        for (String resourceFolder : this.resourceFolders) {
                            psdFile = new File(resourceFolder + psdFileName);
                            if (psdFile.exists()) {
                                psdFileName = resourceFolder + psdFileName;
                                System.out.println("Found " + resourceFolder);
                                break;
                            }
                        }
                    }
                }
                model.setFilePSD(psdFileName);
            } else {
                model.setFilePSD(null);
            }
        }
        Parameters.getOut().println(reactor.getModels().size() + " models loaded!");
    }

    private Reactor readReactor(NodeList reactorNodes, Parameters parameters) {
        if (reactorNodes.getLength() < 1) {
            return null;
        }
        Element reactorElement = (Element) reactorNodes.item(0);
        double side = Double.parseDouble(reactorElement.getElementsByTagName("size").item(0).getTextContent());
        NodeList list = reactorElement.getElementsByTagName("pixelSize");
        if (list.getLength() > 0) {
            parameters.setPixelSize(Double.parseDouble(list.item(0).getTextContent()));
        }
        Reactor reactor = new Reactor(side, parameters);
        reactor.setName(reactorElement.getAttribute("name"));
        list = reactorElement.getElementsByTagName("fileFolder");
        if (list.getLength() > 0) {
            parameters.setFileFolder(list.item(0).getTextContent());
        }
        list = reactorElement.getElementsByTagName("resourceFolder");
        this.resourceFolders = new LinkedList<String>();
        for (int i = 0; i < list.getLength(); i++) {
            String resourceFolder = list.item(i).getTextContent();
            char lastChar = resourceFolder.charAt(resourceFolder.length() - 1);
            if (lastChar != '/' && lastChar != '\\' && lastChar != File.separatorChar) {
                resourceFolder += File.separator;
            }
            File folder = new File(resourceFolder);
            if (!folder.canRead()) {
                Parameters.getOut().println("Resource folder " + resourceFolder + " not found");
                continue;
            }
            resourceFolders.add(resourceFolder);
        }
        list = reactorElement.getElementsByTagName("copyFromFolder");
        if (list.getLength() > 0) {
            String copyFromPath = list.item(0).getTextContent();
            File copyFromPathFile = new File(copyFromPath);
            if (copyFromPathFile.canRead() || copyFromPathFile.getParentFile().canRead()) {
                parameters.setCopyFromFolder(copyFromPath);
                Parameters.createFolders(copyFromPath, reactor.noOfPixels);
            } else {
                parameters.setCopyFromFolder(null);
            }
        } else {
            parameters.setCopyFromFolder(null);
        }
        list = reactorElement.getElementsByTagName("minTime");
        NodeList list2 = reactorElement.getElementsByTagName("maxTime");
        NodeList list3 = reactorElement.getElementsByTagName("step");
        if (list.getLength() > 0 && Double.parseDouble(list.item(0).getTextContent()) >= 0) {
            int max = Math.min(list.getLength(), Math.min(list2.getLength(), list3.getLength()));
            for (int i = 0; i < max; i++) {
                Parameters.getOut().println("Getting times");
                double minTime = Double.parseDouble(list.item(i).getTextContent());
                double maxTime = Double.parseDouble(list2.item(i).getTextContent());
                double step = Double.parseDouble(list3.item(i).getTextContent());
                reactor.setTimeSteps(minTime, maxTime, step);
            }
        } else {
            list = reactorElement.getElementsByTagName("timeStepFile");
            if (list.getLength() > 0) {
                String timeStepFileName = list.item(0).getTextContent();
                File timeStepFile = new File(timeStepFileName);
                if (!timeStepFile.exists()) {
                    timeStepFile = new File(parameters.getFileFolder() + timeStepFileName);
                    if (timeStepFile.exists()) {
                        timeStepFileName = parameters.getFileFolder() + timeStepFileName;
                    } else {
                        for (String resourceFolder : this.resourceFolders) {
                            timeStepFile = new File(resourceFolder + timeStepFileName);
                            if (timeStepFile.exists()) {
                                timeStepFileName = resourceFolder + timeStepFileName;
                                break;
                            }
                        }
                    }
                }
                reactor.setTimeStepFile(timeStepFileName);
            }
        }
        list = reactorElement.getElementsByTagName("backgroundColor");
        if (list.getLength() > 0) {
            reactor.getImages().setBgColor(Images.getColor(list.item(0).getTextContent()));
        }
        list = reactorElement.getElementsByTagName("maxOccupancyPixels");
        if (list.getLength() > 0) {
            parameters.setMaxNumberOfOccupancyPixels(Integer.parseInt(list.item(0).getTextContent()));
        }
        list = reactorElement.getElementsByTagName("minOccupancyPixels");
        if (list.getLength() > 0) {
            parameters.setMinNumberOfOccupancyPixels(Integer.parseInt(list.item(0).getTextContent()));
        }
        Parameters.getOut().println("Max number of occupancy pixels set to " + parameters.getMaxNumberOfOccupancyPixels());
        list = reactorElement.getElementsByTagName("flocculate");
        if (list.getLength() > 0) {
            Element flocculateElement = (Element) list.item(0);
            String att = flocculateElement.getAttribute("value");
            parameters.setFlocculate(Boolean.parseBoolean(att));
            if (parameters.flocculate()) {
                list2 = flocculateElement.getElementsByTagName("duringDistribution");
                if (list2.getLength() > 0) {
                    parameters.setFlocculateDuringDistribution(Boolean.parseBoolean(list2.item(0).getTextContent()));
                }
                list2 = flocculateElement.getElementsByTagName("range");
                if (list2.getLength() > 0) {
                    parameters.setFlocculationRange(Double.parseDouble(list2.item(0).getTextContent()));
                }
                list2 = flocculateElement.getElementsByTagName("factor");
                if (list2.getLength() > 0) {
                    parameters.setFlocculationFactor(Double.parseDouble(list2.item(0).getTextContent()));
                }
                list2 = flocculateElement.getElementsByTagName("cycles");
                if (list2.getLength() > 0) {
                    parameters.setFlocculationCycles(Integer.parseInt(list2.item(0).getTextContent()));
                }
                list2 = flocculateElement.getElementsByTagName("maxTrials");
                if (list2.getLength() > 0) {
                    parameters.setMaxTrials(Integer.parseInt(list2.item(0).getTextContent()));
                }
                new FlocPacker(reactor, parameters.getMaxTrials(), parameters.getFlocculationCycles(), parameters.getFlocculationFactor(), parameters.getFlocculationRange(), parameters.flocculateDuringDistribution());
            }
        }
        list = reactorElement.getElementsByTagName("sphereSamplingPoints");
        if (list.getLength() > 0) {
            reactor.setSphereSamplingPoints(Integer.parseInt(list.item(0).getTextContent()));
        }
        list = reactorElement.getElementsByTagName("checkIndividualArea");
        if (list.getLength() > 0) {
            reactor.setCheckIndividualArea(Boolean.parseBoolean(list.item(0).getTextContent()));
        }
        list = reactorElement.getElementsByTagName("checkNoArea");
        if (list.getLength() > 0) {
            reactor.setCheckNoArea(Boolean.parseBoolean(list.item(0).getTextContent()));
        }
        list = reactorElement.getElementsByTagName("findCoveringSphere");
        if (list.getLength() > 0) {
            reactor.setFindCoveringSphere(Boolean.parseBoolean(list.item(0).getTextContent()));
        }
        list = reactorElement.getElementsByTagName("saveHydout");
        if (list.getLength() > 0) {
            parameters.setSaveHydout(Boolean.parseBoolean(list.item(0).getTextContent()));
        }
        list = reactorElement.getElementsByTagName("savePorein");
        if (list.getLength() > 0) {
            parameters.setSavePorein(Boolean.parseBoolean(list.item(0).getTextContent()));
        }
        list = reactorElement.getElementsByTagName("saveGrains");
        if (list.getLength() > 0) {
            parameters.setSaveGrains(Boolean.parseBoolean(list.item(0).getTextContent()));
        }
        list = reactorElement.getElementsByTagName("saveDeepImages");
        if (list.getLength() > 0) {
            Element imageElement = (Element) list.item(0);
            if (imageElement.hasAttribute("depth")) {
                parameters.setImageDepth(Integer.parseInt(imageElement.getAttribute("depth")));
            }
            parameters.setSaveDeepImages(Boolean.parseBoolean(list.item(0).getTextContent()));
        }
        list = reactorElement.getElementsByTagName("useRelativePaths");
        if (list.getLength() > 0) {
            parameters.setUseRelativePaths(Boolean.parseBoolean(list.item(0).getTextContent()));
        }
        return reactor;
    }

    private void readReactions(NodeList reactionNodes, Reactor reactor) {
        Parameters.getOut().println("There are " + reactionNodes.getLength() + " reactions available.");
        for (int i = 0; i < reactionNodes.getLength(); i++) {
            Element reactionElement = (Element) reactionNodes.item(i);
            Reaction reaction = new Reaction(reactor);
            reactor.setName(reaction, reactionElement.getAttribute("name"));
            Parameters.getOut().println("Setting up reaction " + reaction);
            if (reactionElement.getElementsByTagName("order").getLength() > 0) {
                reaction.setIndex(Integer.parseInt(reactionElement.getElementsByTagName("order").item(0).getTextContent()));
            }
            if (reactionElement.getElementsByTagName("index").getLength() > 0) {
                reaction.setIndex(Integer.parseInt(reactionElement.getElementsByTagName("index").item(0).getTextContent()));
            }
            NodeList reactantNodes = reactionElement.getElementsByTagName("reactant");
            for (int j = 0; j < reactantNodes.getLength(); j++) {
                Element reactantElement = (Element) reactantNodes.item(j);
                String materialName = reactantElement.getElementsByTagName("materialName").item(0).getTextContent();
                double ratio = Double.parseDouble(reactantElement.getElementsByTagName("ratio").item(0).getTextContent());
                reaction.addReactant(new Reactant(reactor.getMaterial(materialName), ratio, reaction));
                Parameters.getOut().println("Material " + materialName + " added as Reactant with ratio " + ratio);
            }
            NodeList productNodes = reactionElement.getElementsByTagName("product");
            for (int j = 0; j < productNodes.getLength(); j++) {
                Element productElement = (Element) productNodes.item(j);
                String materialName = productElement.getElementsByTagName("materialName").item(0).getTextContent();
                double ratio = Double.parseDouble(productElement.getElementsByTagName("ratio").item(0).getTextContent());
                reaction.addProduct(new Product(reactor.getMaterial(materialName), ratio, reaction));
                Parameters.getOut().println("Material " + materialName + " added as Product with ratio " + ratio);
            }
            NodeList list = reactionElement.getElementsByTagName("discrete");
            if (list.getLength() > 0) {
                reaction.setDiscrete(Boolean.parseBoolean(list.item(0).getTextContent()));
            }
        }
        for (int i = 0; i < reactionNodes.getLength(); i++) {
            Element reactionElement = (Element) reactionNodes.item(i);
            Reaction reaction = reactor.getReaction(reactionElement.getAttribute("name"));
            NodeList successorNodes = reactionElement.getElementsByTagName("successor");
            for (int j = 0; j < successorNodes.getLength(); j++) {
                reaction.addSuccesor(reactor.getReaction(successorNodes.item(j).getTextContent()));
            }
        }
    }

    private void readPlugins(NodeList pluginNodes, Reactor reactor) {
        this.pluginNodeList = pluginNodes;
        for (int i = 0; i < pluginNodes.getLength(); i++) {
            Element pluginElement = (Element) pluginNodes.item(i);
            String type = pluginElement.getAttribute("type");
            String classPath = pluginElement.getElementsByTagName("classPath").item(0).getTextContent();
            String className = pluginElement.getElementsByTagName("className").item(0).getTextContent();
            Parameters.getOut().println("Trying to load plugin " + className + " of type " + type);
            NodeList parameterNodes = pluginElement.getElementsByTagName("parameter");
            try {
                URL[] urls = { new File(classPath).toURI().toURL() };
                ClassLoader loader = new URLClassLoader(urls);
                Class pluginClass;
                try {
                    pluginClass = loader.loadClass(className);
                } catch (ClassNotFoundException e) {
                    try {
                        pluginClass = loader.loadClass("org.micthemodel." + className);
                    } catch (ClassNotFoundException ex) {
                        try {
                            pluginClass = loader.loadClass("org.micthemodel.plugins." + type + "." + className);
                        } catch (ClassNotFoundException e1) {
                            pluginClass = loader.loadClass(type + "." + className);
                        }
                    }
                }
                Object pluginInstance;
                pluginInstance = pluginClass.newInstance();
                Constructor[] pluginConstructors = pluginClass.getConstructors();
                Constructor pluginConstructor = pluginConstructors[0];
                if (!(pluginInstance instanceof MicPlugin)) {
                    Parameters.getOut().println("The plugin " + className + " is not a valid MicPlugin");
                    throw new RuntimeException();
                }
                MicPlugin pluginEmptyInstance = (MicPlugin) pluginInstance;
                Class[] parametersNeeded = pluginEmptyInstance.constructorParameterClasses();
                if (pluginEmptyInstance.pluginType().compareTo(type) != 0) {
                    Parameters.getOut().println("Plugin types don't match. Needed " + type + " found " + pluginEmptyInstance.pluginType());
                    throw new RuntimeException();
                }
                for (int j = 0; j < pluginConstructors.length; j++) {
                    if (pluginConstructors[j].getParameterTypes().length != parametersNeeded.length) {
                        if (j == pluginConstructors.length - 1) {
                            Parameters.getOut().println("Constructor length = " + parametersNeeded.length + " given length = " + pluginConstructors[j].getParameterTypes().length);
                            Parameters.getOut().println("Parameters for plugin " + className + " do not match the required parameters");
                            throw new RuntimeException();
                        } else {
                            continue;
                        }
                    }
                    pluginConstructor = pluginConstructors[j];
                    break;
                }
                Object[] parameters;
                parameters = this.getObjects(parameterNodes, reactor);
                pluginConstructor.newInstance(parameters);
                Parameters.getOut().println("The plugin " + pluginClass.getName() + " loaded successfully");
            } catch (MalformedURLException ex) {
                Parameters.getOut().println("The path " + classPath + " for the plugin " + className + " is not valid");
                ex.printStackTrace();
                throw new RuntimeException();
            } catch (ClassNotFoundException ex) {
                Parameters.getOut().println("The plugin " + className + " is not precompiled and cannot be found at the path " + classPath);
                ex.printStackTrace();
                throw new RuntimeException();
            } catch (InstantiationException ex) {
                Parameters.getOut().println("An instance of the plugin " + className + " could not be created" + "\n" + "Please check that the plugin has a no-arg constructor and is an object." + "\n" + "Also check if the correct constructor is defined.");
                ex.printStackTrace();
                throw new RuntimeException();
            } catch (IllegalAccessException ex) {
                Parameters.getOut().println("An instance of the plugin " + className + " could not be created" + "\n" + "Please check that the plugin has a no-arg constructor and is an object." + "\n" + "Also check if the correct constructor is defined.");
                ex.printStackTrace();
                throw new RuntimeException();
            } catch (InvocationTargetException ex) {
                Parameters.getOut().println("An instance of the plugin " + className + " could not be loaded.\n" + "Please check the input arguments.");
                ex.printStackTrace();
                throw new RuntimeException();
            }
        }
    }

    private Object[] getObjects(NodeList parameterNodes, Reactor reactor) {
        ArrayList parameters = new ArrayList(parameterNodes.getLength());
        for (int i = 0; i < parameterNodes.getLength(); i++) {
            Element parameterElement = (Element) parameterNodes.item(i);
            parameters.add(this.getObject(parameterElement, reactor));
        }
        return parameters.toArray();
    }

    private Object getObject(Element parameterElement, Reactor reactor) {
        String type = parameterElement.getAttribute("type");
        if (type.equals("array")) {
            return this.getArray(parameterElement, reactor);
        }
        if (type.equals("boolean")) {
            return this.getBoolean(parameterElement, reactor);
        }
        if (type.equals("file")) {
            return this.getFile(parameterElement, reactor);
        }
        if (type.equals("material")) {
            return this.getMaterial(parameterElement, reactor);
        }
        if (type.equals("modelGrain")) {
            return this.getModelGrain(parameterElement, reactor);
        }
        if (type.equals("number")) {
            return this.getNumber(parameterElement, reactor);
        }
        if (type.equals("product")) {
            return this.getProduct(parameterElement, reactor);
        }
        if (type.equals("reactant")) {
            return this.getReactant(parameterElement, reactor);
        }
        if (type.equals("reaction")) {
            return this.getReaction(parameterElement, reactor);
        }
        if (type.equals("reactor")) {
            return reactor;
        }
        return null;
    }

    private File getFile(Element parameterElement, Reactor reactor) {
        String fileName = parameterElement.getElementsByTagName("name").item(0).getTextContent();
        return new File(fileName);
    }

    private Reaction getReaction(Element parameterElement, Reactor reactor) {
        return reactor.getReaction(parameterElement.getElementsByTagName("name").item(0).getTextContent());
    }

    private Reactant getReactant(Element parameterElement, Reactor reactor) {
        Reaction reaction = reactor.getReaction(parameterElement.getElementsByTagName("hostReaction").item(0).getTextContent());
        return reaction.getReactant(parameterElement.getElementsByTagName("name").item(0).getTextContent());
    }

    private Product getProduct(Element parameterElement, Reactor reactor) {
        Reaction reaction = reactor.getReaction(parameterElement.getElementsByTagName("hostReaction").item(0).getTextContent());
        return reaction.getProduct(parameterElement.getElementsByTagName("name").item(0).getTextContent());
    }

    private ModelGrain getModelGrain(Element parameterElement, Reactor reactor) {
        return reactor.getModel(parameterElement.getElementsByTagName("name").item(0).getTextContent());
    }

    private Material getMaterial(Element parameterElement, Reactor reactor) {
        return reactor.getMaterial(parameterElement.getElementsByTagName("name").item(0).getTextContent());
    }

    private boolean getBoolean(Element parameterElement, Reactor reactor) {
        return Boolean.parseBoolean(parameterElement.getElementsByTagName("value").item(0).getTextContent());
    }

    private Object getNumber(Element parameterElement, Reactor reactor) {
        double number = Double.parseDouble(parameterElement.getElementsByTagName("value").item(0).getTextContent());
        NodeList typeNodes = parameterElement.getElementsByTagName("class");
        if (typeNodes.getLength() < 1) {
            return number;
        }
        String type = typeNodes.item(0).getTextContent();
        if (type.compareTo("int") == 0) {
            return (int) number;
        }
        if (type.compareTo("float") == 0) {
            return (float) number;
        }
        if (type.compareTo("byte") == 0) {
            return (byte) number;
        }
        if (type.compareTo("short") == 0) {
            return (short) number;
        }
        if (type.compareTo("long") == 0) {
            return (long) number;
        }
        return number;
    }

    private Object getArray(Element parameterElement, Reactor reactor) {
        NodeList parameterNodes = parameterElement.getElementsByTagName("element");
        if (parameterNodes.getLength() == 0) {
            return new Object[0];
        }
        Element firstElement = (Element) parameterNodes.item(0);
        Object array = Array.newInstance(getClass(firstElement), parameterNodes.getLength());
        for (int i = 0; i < parameterNodes.getLength(); i++) {
            Element parameter = (Element) parameterNodes.item(i);
            Array.set(array, i, this.getObject(parameter, reactor));
        }
        return array;
    }

    private Class getClass(Element element) {
        String type = element.getAttribute("type");
        if (type.compareToIgnoreCase("number") == 0) {
            String classType = element.getElementsByTagName("class").item(0).getTextContent();
            if (classType.compareToIgnoreCase("float") == 0) {
                return float.class;
            }
            if (classType.compareToIgnoreCase("int") == 0) {
                return int.class;
            }
            if (classType.compareToIgnoreCase("byte") == 0) {
                return byte.class;
            }
            if (classType.compareToIgnoreCase("double") == 0) {
                return double.class;
            }
        }
        if (type.compareToIgnoreCase("boolean") == 0) {
            return boolean.class;
        }
        if (type.compareToIgnoreCase("file") == 0) {
            return File.class;
        }
        if (type.compareToIgnoreCase("reaction") == 0) {
            return Reaction.class;
        }
        if (type.compareToIgnoreCase("material") == 0) {
            return Material.class;
        }
        if (type.compareToIgnoreCase("modelgrain") == 0) {
            return ModelGrain.class;
        }
        if (type.compareToIgnoreCase("reactant") == 0) {
            return Reactant.class;
        }
        if (type.compareToIgnoreCase("product") == 0) {
            return Product.class;
        }
        return Object.class;
    }

    private void readComments(NodeList commentNodes) {
        Parameters.getOut().println(commentNodes.getLength() + " Comments!");
        for (int i = 0; i < commentNodes.getLength(); i++) {
            Parameters.getOut().println("Comment " + (i + 1));
            Parameters.getOut().println(commentNodes.item(i).getTextContent());
        }
    }
}
