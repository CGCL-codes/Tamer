package ca.ucalgary.cpsc.agilePlanner.persister.local;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ca.ucalgary.cpsc.agilePlanner.persister.Backlog;
import ca.ucalgary.cpsc.agilePlanner.persister.ConnectionFailedException;
import ca.ucalgary.cpsc.agilePlanner.persister.CouldNotLoadProjectException;
import ca.ucalgary.cpsc.agilePlanner.persister.ForbiddenOperationException;
import ca.ucalgary.cpsc.agilePlanner.persister.IndexCard;
import ca.ucalgary.cpsc.agilePlanner.persister.IndexCardNotFoundException;
import ca.ucalgary.cpsc.agilePlanner.persister.Iteration;
import ca.ucalgary.cpsc.agilePlanner.persister.Legend;
import ca.ucalgary.cpsc.agilePlanner.persister.Owner;
import ca.ucalgary.cpsc.agilePlanner.persister.Project;
import ca.ucalgary.cpsc.agilePlanner.persister.ProjectNotFoundException;
import ca.ucalgary.cpsc.agilePlanner.persister.StoryCard;
import ca.ucalgary.cpsc.agilePlanner.persister.SynchronousPersister;
import ca.ucalgary.cpsc.agilePlanner.persister.impl.data.BacklogDataObject;
import ca.ucalgary.cpsc.agilePlanner.persister.impl.data.IterationDataObject;
import ca.ucalgary.cpsc.agilePlanner.persister.impl.data.LegendDataObject;
import ca.ucalgary.cpsc.agilePlanner.persister.impl.data.OwnerDataObject;
import ca.ucalgary.cpsc.agilePlanner.persister.impl.data.ProjectDataObject;
import ca.ucalgary.cpsc.agilePlanner.persister.impl.data.StoryCardDataObject;
import ca.ucalgary.cpsc.agilePlanner.persister.util.FileSystemIDGenerator;
import ca.ucalgary.cpsc.agilePlanner.persister.xml.converter.BacklogConverter;
import ca.ucalgary.cpsc.agilePlanner.persister.xml.converter.IterationConverter;
import ca.ucalgary.cpsc.agilePlanner.persister.xml.converter.LegendConverter;
import ca.ucalgary.cpsc.agilePlanner.persister.xml.converter.OwnerConverter;
import ca.ucalgary.cpsc.agilePlanner.persister.xml.converter.ProjectConverter;
import ca.ucalgary.cpsc.agilePlanner.persister.xml.converter.StoryCardConverter;
import com.thoughtworks.xstream.XStream;

public class PersisterToXML extends ca.ucalgary.cpsc.agilePlanner.util.Object implements SynchronousPersister {

    private long maxid = 1;

    private File file;

    private static Project currProject;

    private XStream xstream;

    protected Project project;

    private FileSystemIDGenerator generator;

    private Timestamp start;

    private Timestamp end;

    private File projectDirectory;

    private String currentProjectName;

    private String localProjectDirPath;

    private class ProjectXMLFileFilter extends ca.ucalgary.cpsc.agilePlanner.util.Object implements FilenameFilter {

        public boolean accept(java.io.File f) {
            return f.getName().endsWith(".xml");
        }

        public boolean accept(File dir, String name) {
            return (name.endsWith(".xml"));
        }
    }

    /**
     * 
     * @param localProjectDirPath
     *            path to the directory where the project files are stored
     * @param projectName
     * @throws CouldNotLoadProjectException
     * @throws RemoteException 
     * 
     */
    public PersisterToXML(String localProjectDirPath, String projectName) throws ConnectionFailedException, CouldNotLoadProjectException, RemoteException {
        this.generator = FileSystemIDGenerator.getInstance();
        this.start = new Timestamp(0);
        this.end = new Timestamp(Long.MAX_VALUE);
        this.projectDirectory = new File(localProjectDirPath);
        this.xstream = new XStream();
        xstream.alias("Project", ProjectDataObject.class);
        xstream.alias("StoryCard", StoryCardDataObject.class);
        xstream.alias("Legend", LegendDataObject.class);
        xstream.alias("Iteration", IterationDataObject.class);
        xstream.alias("USERS", OwnerDataObject.class);
        xstream.alias("Backlog", BacklogDataObject.class);
        xstream.registerConverter(new ProjectConverter());
        xstream.registerConverter(new IterationConverter());
        xstream.registerConverter(new BacklogConverter());
        xstream.registerConverter(new StoryCardConverter());
        xstream.registerConverter(new LegendConverter());
        xstream.registerConverter(new OwnerConverter());
        this.projectDirectory.mkdir();
        assert (projectDirectory.isDirectory());
        this.file = new File(localProjectDirPath + File.separatorChar + projectName + ".xml");
        this.load(projectName);
        this.currentProjectName = projectName;
        this.localProjectDirPath = localProjectDirPath;
    }

    public synchronized Project createProject(String name) {
        ProjectDataObject project = new ProjectDataObject(name);
        this.generator.init(1);
        project.setId(this.generator.getNextID());
        this.project = project;
        String filePath = projectDirectory.getPath();
        this.file = new File(filePath + File.separatorChar + name + ".xml");
        this.currentProjectName = name;
        try {
            this.project.createBacklog(0, 0, 0, 0);
            this.project.createLegend("Feature", "Bug", "Rally", "yellow1", "Spike", "pink1", "gray1", "khaki1", "peach2", "aqua3");
        } catch (ForbiddenOperationException e1) {
            e1.printStackTrace();
        }
        this.save();
        return (Project) project.clone();
    }

    /***************************************************************************
     * LOAD AND SAVE *
     * @throws RemoteException 
     * 
     **************************************************************************/
    public Project load(String projectName, Timestamp start, Timestamp end) throws CouldNotLoadProjectException, RemoteException {
        this.start = start;
        this.end = end;
        return (Project) load(projectName).clone();
    }

    public Project load(String projectName) throws CouldNotLoadProjectException, RemoteException {
        this.project = new ProjectDataObject();
        return loadProjectFromLocalFile(projectName);
    }

    private Project loadProjectFromLocalFile(String projectName) throws CouldNotLoadProjectException {
        boolean fileMissing = false;
        this.project.setName(projectName);
        this.generator.init(1);
        this.project.setId(this.generator.getNextID());
        this.start = new Timestamp(0);
        this.end = new Timestamp(Long.MAX_VALUE);
        String filename = projectName + ".xml";
        file = new File(projectDirectory.getPath() + File.separatorChar + filename);
        if (!file.exists()) {
            fileMissing = true;
            File dir = new File(file.getParent());
            if (!dir.exists()) {
                dir.mkdirs();
            }
            createProject(projectName);
        }
        assert (file.isFile());
        assert (file.exists());
        try {
            FileInputStream inputFile = new FileInputStream(this.file.getPath());
            this.project = (Project) xstream.fromXML(inputFile);
            this.generator.init(getMaxIDofProject(project) + 1);
            try {
                inputFile.close();
            } catch (IOException e) {
                ca.ucalgary.cpsc.agilePlanner.util.Logger.singleton().debug(e.getMessage() + "\r\n" + e.getStackTrace());
            }
        } catch (FileNotFoundException e) {
            ca.ucalgary.cpsc.agilePlanner.util.Logger.singleton().debug(e.getMessage() + "\r\n" + e.getStackTrace());
        } catch (IOException e) {
            ca.ucalgary.cpsc.agilePlanner.util.Logger.singleton().debug(e.getMessage() + "\r\n" + e.getStackTrace());
        }
        this.save();
        setCurrProject(project);
        return (Project) this.project.clone();
    }

    public List<String> getProjectNames() {
        String[] projectNames = this.getProjectDirectory().list(new ProjectXMLFileFilter());
        List<String> projectNamesList = new ArrayList<String>();
        for (int i = 0; i < projectNames.length; i++) {
            projectNames[i] = projectNames[i].substring(0, projectNames[i].length() - 4);
            projectNamesList.add(projectNames[i]);
        }
        return projectNamesList;
    }

    /***************************************************************************
     * WRITE TO XML FILE HELPER METHODS *
     **************************************************************************/
    private void createBacklogElements(Document document, Element parent) {
        BacklogDataObject backlog = (BacklogDataObject) this.project.getBacklog();
        if (backlog != null) {
            Element element = document.createElement("Backlog");
            element.setAttribute("ID", String.valueOf(backlog.getId()));
            element.setAttribute("Parent", String.valueOf(backlog.getParent()));
            element.setAttribute("Name", backlog.getName());
            element.setAttribute("XLocation", String.valueOf(backlog.getLocationX()));
            element.setAttribute("YLocation", String.valueOf(backlog.getLocationY()));
            element.setAttribute("Width", String.valueOf(backlog.getWidth()));
            element.setAttribute("Height", String.valueOf(backlog.getHeight()));
            parent.appendChild(element);
            if (backlog.getStoryCardChildren().size() != 0) {
                createStoryCardElement(document, element, backlog.getStoryCardChildren());
            }
        }
    }

    private void createLegendElements(Document document, Element parent) {
        LegendDataObject legend = (LegendDataObject) this.project.getLegend();
        if (legend != null) {
            Element element = document.createElement("Legend");
            element.setAttribute("blue", legend.getBlue());
            element.setAttribute("red", legend.getRed());
            element.setAttribute("green", legend.getGreen());
            element.setAttribute("yellow", legend.getYellow());
            element.setAttribute("white", legend.getWhite());
            element.setAttribute("pink", legend.getPink());
            element.setAttribute("gray", legend.getGray());
            element.setAttribute("khaki", legend.getKhaki());
            element.setAttribute("peach", legend.getPeach());
            element.setAttribute("aqua", legend.getAqua());
            parent.appendChild(element);
        }
    }

    private void createIterationElements(Document document, Element parent) {
        List<Iteration> iterations = this.project.getIterationChildren();
        if (iterations != null) {
            for (Iteration iteration : iterations) {
                Element element = document.createElement("Iteration");
                element.setAttribute("ID", String.valueOf(iteration.getId()));
                element.setAttribute("Parent", String.valueOf(iteration.getParent()));
                element.setAttribute("Name", iteration.getName());
                element.setAttribute("Description", iteration.getDescription());
                element.setAttribute("XLocation", String.valueOf(iteration.getLocationX()));
                element.setAttribute("YLocation", String.valueOf(iteration.getLocationY()));
                element.setAttribute("Width", String.valueOf(iteration.getWidth()));
                element.setAttribute("Height", String.valueOf(iteration.getHeight()));
                element.setAttribute("EndDate", iteration.getEndDate().toString());
                element.setAttribute("StartDate", iteration.getStartDate().toString());
                element.setAttribute("AvailableEffort", String.valueOf(iteration.getAvailableEffort()));
                element.setAttribute("Status", String.valueOf(iteration.getStatus()));
                element.setAttribute("rallyID", String.valueOf(iteration.getRallyID()));
                parent.appendChild(element);
                if (iteration.getStoryCardChildren().size() != 0) {
                    createStoryCardElement(document, element, iteration.getStoryCardChildren());
                }
            }
        }
    }

    private void createOwnerElements(Document document, Element parent) {
        List<Owner> owners = this.project.getOwnerChildren();
        if (owners != null) {
            for (Owner owner : owners) {
                Element element = document.createElement("Owner");
                element.setAttribute("Name", owner.getOwner());
            }
        }
    }

    private void createStoryCardElement(Document document, Element parent, List<StoryCard> children) {
        for (StoryCard card : children) {
            Element element = document.createElement("StoryCard");
            element.setAttribute("ID", String.valueOf(card.getId()));
            element.setAttribute("Parent", String.valueOf(card.getParent()));
            element.setAttribute("Name", card.getName());
            element.setAttribute("Description", card.getDescription());
            element.setAttribute("XLocation", String.valueOf(card.getLocationX()));
            element.setAttribute("YLocation", String.valueOf(card.getLocationY()));
            element.setAttribute("Width", String.valueOf(card.getWidth()));
            element.setAttribute("Height", String.valueOf(card.getHeight()));
            element.setAttribute("BestCase", String.valueOf(card.getBestCaseEstimate()));
            element.setAttribute("MostLikely", String.valueOf(card.getMostlikelyEstimate()));
            element.setAttribute("WorstCase", String.valueOf(card.getWorstCaseEstimate()));
            element.setAttribute("Actual", String.valueOf(card.getActualEffort()));
            element.setAttribute("Status", String.valueOf(card.getStatus()));
            element.setAttribute("Test_URL", card.getAcceptanceTestUrl());
            element.setAttribute("CurrentSideUp", "" + card.getCurrentSideUp());
            element.setAttribute("TestText", card.getAcceptanceTestText());
            element.setAttribute("Color", String.valueOf(card.getColor()));
            element.setAttribute("CardOwner", String.valueOf(card.getCardOwner()));
            element.setAttribute("rallyID", String.valueOf(card.getRallyID()));
            element.setAttribute("FitID", card.getFitId());
            element.setAttribute("RotationAngle", String.valueOf(card.getRotationAngle()));
            if (card.getHandwritingImage() != null) {
                saveTabletPCCardImage(card);
            }
            parent.appendChild(element);
        }
    }

    /***************************************************************************
     * FIND HELPER METHODS *
     **************************************************************************/
    public IndexCard findCard(long id) throws IndexCardNotFoundException {
        return (IndexCard) project.findCard(id).clone();
    }

    public IndexCard updateCard(IndexCard indexCard) throws IndexCardNotFoundException {
        IndexCard ic = project.updateCard(indexCard);
        this.save();
        return (IndexCard) ic.clone();
    }

    public Legend updateLegend(Legend legend) {
        Legend ldo = project.updateLegend(legend);
        this.save();
        return (Legend) ldo.clone();
    }

    public synchronized boolean save() {
        return this.saveAs(this.file.getPath());
    }

    public synchronized boolean saveAs(String path) {
        try {
            FileOutputStream out = new FileOutputStream(path);
            out.flush();
            xstream.toXML(this.project, out);
            out.close();
            return true;
        } catch (TransformerFactoryConfigurationError e) {
            ca.ucalgary.cpsc.agilePlanner.util.Logger.singleton().debug(e.getMessage() + "\r\n" + e.getStackTrace());
            return false;
        } catch (IOException e) {
            ca.ucalgary.cpsc.agilePlanner.util.Logger.singleton().debug(e.getMessage() + "\r\n" + e.getStackTrace());
            return false;
        }
    }

    private Document buildDOMToFile() {
        Document document = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();
            Element root = document.createElement("Project");
            root.setAttribute("ID", String.valueOf(this.project.getId()));
            root.setAttribute("Name", this.project.getName());
            document.appendChild(root);
            this.createIterationElements(document, root);
            this.createBacklogElements(document, root);
            this.createLegendElements(document, root);
            this.createOwnerElements(document, root);
        } catch (Exception e) {
            ca.ucalgary.cpsc.agilePlanner.util.Logger.singleton().debug(e.getMessage() + "\r\n" + e.getStackTrace());
        }
        return document;
    }

    public boolean writeFile(String fileName, byte[] fileContent, int recognizeID) {
        try {
            if (recognizeID == 0) {
                FileOutputStream e = new FileOutputStream(projectDirectory.getPath() + File.separatorChar + fileName);
                e.write(fileContent);
                e.flush();
                e.close();
                return true;
            } else {
                File uploadedFileForCard = new File(projectDirectory.getPath() + File.separatorChar + this.currentProjectName + File.separatorChar + "Uploaded_Files" + File.separatorChar + String.valueOf(recognizeID));
                if (!uploadedFileForCard.exists()) {
                    uploadedFileForCard.mkdirs();
                }
                FileOutputStream e = new FileOutputStream(uploadedFileForCard.getPath() + File.separatorChar + fileName);
                e.write(fileContent);
                e.flush();
                e.close();
                return true;
            }
        } catch (IOException e) {
            ca.ucalgary.cpsc.agilePlanner.util.Logger.singleton().debug(e.getMessage() + "\r\n" + e.getStackTrace());
            return false;
        }
    }

    public byte[] readFile(String filename, int recognizeID) {
        try {
            if (recognizeID == 0) {
                FileInputStream readFile = new FileInputStream(projectDirectory.getPath() + File.separatorChar + filename);
                byte[] originalSpace = new byte[readFile.available()];
                readFile.read(originalSpace);
                return originalSpace;
            } else {
                FileInputStream readFile = new FileInputStream(projectDirectory.getPath() + File.separatorChar + this.currentProjectName + File.separatorChar + "Uploaded_Files" + File.separatorChar + String.valueOf(recognizeID) + File.separatorChar + filename);
                byte[] originalSpace = new byte[readFile.available()];
                readFile.read(originalSpace);
                return originalSpace;
            }
        } catch (IOException e) {
        }
        return null;
    }

    public File getProjectDirectory() {
        return projectDirectory;
    }

    public String[][] getIterationNames(String projectName) {
        List<Iteration> lst = project.getIterationChildren();
        String[][] iterations = new String[lst.size()][2];
        int j = 0;
        for (Iteration i : lst) {
            iterations[j][0] = i.getName();
            iterations[j][1] = Long.toString(i.getId());
            j++;
        }
        return iterations;
    }

    public Project getProject() {
        return (Project) project;
    }

    public synchronized File getFile() {
        return file;
    }

    public synchronized void setFile(File file) {
        this.file = file;
    }

    public Backlog createBacklog(int width, int height, int locationX, int locationY) throws ForbiddenOperationException {
        Backlog backlog = project.createBacklog(width, height, locationX, locationY);
        this.save();
        return (Backlog) backlog.clone();
    }

    public Iteration createIteration(String name, String description, int width, int height, int locationX, int locationY, float availableEffort, Timestamp startDate, Timestamp endDate, float rotationAngle, boolean rallyID) {
        Iteration iteration = project.createIteration(name, description, width, height, locationX, locationY, availableEffort, startDate, endDate, rotationAngle, rallyID);
        this.save();
        return (Iteration) iteration.clone();
    }

    public Owner createOwner(String name) {
        Owner owner = project.createOwner(name);
        this.save();
        return (Owner) owner.clone();
    }

    public Legend createLegend(String blue, String red, String green, String yellow, String white, String pink, String gray, String khaki, String peach, String aqua) {
        Legend legend = project.createLegend(blue, red, green, yellow, white, pink, gray, khaki, peach, aqua);
        this.save();
        return (Legend) legend.clone();
    }

    public StoryCard createStoryCard(String name, String description, int width, int height, int locationX, int locationY, long parentid, float bestCaseEstimate, float mostlikelyEstimate, float worstCaseEstimate, float actualEffort, String status, String color, String cardOwner, float rotationAngle, boolean rallyID, String fitId) throws IndexCardNotFoundException {
        StoryCard storyCard = project.createStoryCard(name, description, width, height, locationX, locationY, parentid, bestCaseEstimate, mostlikelyEstimate, worstCaseEstimate, actualEffort, status, color, cardOwner, rotationAngle, rallyID, fitId);
        this.save();
        return (StoryCard) storyCard.clone();
    }

    public StoryCard createTabletPCStoryCard(String name, String description, int width, int height, int locationX, int locationY, long parentid, float bestCaseEstimate, float mostlikelyEstimate, float worstCaseEstimate, float actualEffort, String status, String color, byte[] image, boolean rallyID) throws IndexCardNotFoundException {
        StoryCard storyCard = project.createTabletPCStoryCard(name, description, width, height, locationX, locationY, parentid, bestCaseEstimate, mostlikelyEstimate, worstCaseEstimate, actualEffort, status, color, image, rallyID);
        this.save();
        return (StoryCard) storyCard.clone();
    }

    public IndexCard deleteCard(long id) throws IndexCardNotFoundException, ForbiddenOperationException {
        IndexCard deletedCard = project.deleteCard(id);
        this.save();
        this.deleteTabletPCCardImage(id);
        return (IndexCard) deletedCard.clone();
    }

    public StoryCard moveStoryCardToNewParent(long id, long oldparentid, long newparentid, int locationX, int locationY, float rotation) throws IndexCardNotFoundException {
        StoryCard movedStoryCard = project.moveStoryCardToNewParent(id, oldparentid, newparentid, locationX, locationY, rotation);
        this.save();
        return (StoryCard) movedStoryCard.clone();
    }

    public IndexCard undeleteCard(IndexCard indexCard) throws IndexCardNotFoundException, ForbiddenOperationException {
        IndexCard undeletedCard = project.undeleteCard(indexCard);
        this.save();
        return (IndexCard) undeletedCard.clone();
    }

    private void saveTabletPCCardImage(StoryCard card) {
        try {
            File handWritingDir = new File(projectDirectory.getPath() + File.separatorChar + this.currentProjectName + File.separatorChar + "Handwritings");
            if (!handWritingDir.exists()) {
                handWritingDir.mkdirs();
            }
            FileOutputStream e = new FileOutputStream(handWritingDir.getPath() + File.separatorChar + String.valueOf(card.getId()) + ".jpg");
            e.write(card.getHandwritingImage());
            e.flush();
            e.close();
        } catch (Exception e) {
            ca.ucalgary.cpsc.agilePlanner.util.Logger.singleton().debug(e.getMessage() + "\r\n" + e.getStackTrace());
        }
    }

    private byte[] loadTabletPCCardImage(String id) {
        try {
            byte[] inputBuffer = new byte[100000];
            FileInputStream readImage = new FileInputStream(projectDirectory.getPath() + File.separatorChar + this.currentProjectName + File.separatorChar + "Handwritings" + File.separatorChar + id + ".jpg");
            int realContent = readImage.read(inputBuffer);
            byte[] returnImage = new byte[realContent];
            System.arraycopy(inputBuffer, 0, returnImage, 0, realContent);
            return returnImage;
        } catch (Exception e) {
            return null;
        }
    }

    private void deleteTabletPCCardImage(long id) {
        new File(projectDirectory.getPath() + File.separatorChar + this.currentProjectName + File.separatorChar + "Handwritings" + File.separatorChar + String.valueOf(id) + ".jpg").delete();
    }

    public Project arrangeProject(Project project2) {
        ((ProjectDataObject) project2).setGenerator(FileSystemIDGenerator.getInstance());
        this.project = project2;
        this.save();
        return (Project) project.clone();
    }

    public void setNewPersister(String projectName) throws RemoteException {
        Project temp = this.project;
        try {
            this.project = this.load(projectName);
        } catch (CouldNotLoadProjectException e) {
            this.project = temp;
            return;
        }
        this.currentProjectName = projectName;
        this.file = new File(projectDirectory.getPath() + File.separatorChar + project.getName() + ".xml");
    }

    public Project synchronizeProject(String projName) throws CouldNotLoadProjectException {
        return null;
    }

    public boolean deleteProject(String projectName) throws ProjectNotFoundException {
        if (checkAvailable(projectName)) {
            if (this.project.getName().equalsIgnoreCase(projectName)) {
                createProject(projectName);
                return true;
            } else {
                File file1 = new File(localProjectDirPath + File.separatorChar + projectName + ".xml");
                return file1.delete();
            }
        } else throw new ProjectNotFoundException();
    }

    public boolean checkAvailable(String projectName) {
        List<String> projectNames = this.getProjectNames();
        for (String projectname : projectNames) {
            if (projectName.equalsIgnoreCase(projectname)) return true;
        }
        return false;
    }

    private long getMaxIDofProject(Project project) {
        long temp = 1;
        if (project.getId() > temp) temp = project.getId();
        if (project.getBacklog() != null && project.getBacklog().getId() > temp) {
            temp = project.getBacklog().getId();
            for (Iterator i = project.getBacklog().getStoryCardChildren().iterator(); i.hasNext(); ) {
                IndexCard child = (IndexCard) i.next();
                temp = (child.getId() > temp) ? child.getId() : temp;
            }
        }
        for (Iterator j = project.getIterationChildren().iterator(); j.hasNext(); ) {
            Iteration child = (Iteration) j.next();
            temp = (child.getId() > temp) ? child.getId() : temp;
            for (Iterator k = child.getStoryCardChildren().iterator(); k.hasNext(); ) {
                IndexCard child2 = (IndexCard) k.next();
                temp = (child2.getId() > temp) ? child2.getId() : temp;
            }
        }
        return temp;
    }

    public Owner deleteOwner(String name) throws IndexCardNotFoundException, ForbiddenOperationException {
        Owner deletedOwner = project.deleteOwner(name);
        this.save();
        return (Owner) deletedOwner.clone();
    }

    public static Project getCurrProject() {
        return currProject;
    }

    public static void setCurrProject(Project currProject) {
        PersisterToXML.currProject = currProject;
    }
}
