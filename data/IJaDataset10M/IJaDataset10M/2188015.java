package ch.almana.mcclient.rcp.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import ch.almana.mcclient.rcp.application.Activator;
import ch.almana.mcclient.rcp.model.util.DateHelper;
import ch.almana.mcclient.rcp.model.util.NodeXmlHelper;
import ch.almana.mcclient.rcp.model.util.XmlHelper;

public class Project {

    private static final String PROJECT_NAME = "tagesschau";

    protected static final String XML_FILE_NAME = "mcclient.xml";

    private static Project instance;

    private IProject proj;

    private Document doc;

    public static Project getDefault() throws ParserConfigurationException, SAXException, IOException, InvocationTargetException, InterruptedException, CoreException {
        if (instance == null) {
            instance = new Project();
        }
        return instance;
    }

    public Project() throws InvocationTargetException, InterruptedException, CoreException, ParserConfigurationException, SAXException, IOException {
        this.proj = getProject();
        this.doc = getDocument();
    }

    private IProject getProject() throws InvocationTargetException, InterruptedException {
        if (proj == null) {
            proj = ResourcesPlugin.getWorkspace().getRoot().getProject(PROJECT_NAME);
            Activator.getDefault().getWorkbench().getProgressService().busyCursorWhile(new IRunnableWithProgress() {

                @Override
                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    try {
                        proj.refreshLocal(4, monitor);
                    } catch (CoreException e) {
                        throw new InvocationTargetException(e);
                    }
                }
            });
            if (!proj.exists()) {
                Activator.getDefault().log("Creating project " + proj.getFullPath(), Status.INFO);
                Activator.getDefault().getWorkbench().getProgressService().busyCursorWhile(new IRunnableWithProgress() {

                    @Override
                    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                        try {
                            proj.create(monitor);
                        } catch (CoreException e) {
                            throw new InvocationTargetException(e);
                        }
                    }
                });
            }
            if (!proj.isOpen()) {
                Activator.getDefault().getWorkbench().getProgressService().busyCursorWhile(new IRunnableWithProgress() {

                    @Override
                    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                        try {
                            proj.open(monitor);
                        } catch (CoreException e) {
                            throw new InvocationTargetException(e);
                        }
                    }
                });
            }
        }
        return proj;
    }

    private IFile getFile(String name) {
        IFile file = proj.getFile(name);
        if (!file.exists()) {
            Activator.getDefault().log("File " + name + " does not yet exist", Status.INFO);
        }
        return file;
    }

    private void writeFile(String name, byte[] data) throws IOException, InvocationTargetException, InterruptedException {
        PipedInputStream in = new PipedInputStream();
        OutputStream out = new PipedOutputStream(in);
        out.write(data);
        out.close();
        writeFile(name, in);
    }

    void writeFile(final String name, final InputStream in) throws InvocationTargetException, InterruptedException {
        Activator.getDefault().getWorkbench().getProgressService().run(true, false, new IRunnableWithProgress() {

            @Override
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                IFile ifile = getFile(name);
                try {
                    if (ifile.exists()) {
                        ifile.setContents(in, true, true, monitor);
                    } else {
                        ifile.create(in, true, monitor);
                    }
                } catch (CoreException e) {
                    throw new InvocationTargetException(e);
                }
            }
        });
    }

    public void save() throws IOException, CoreException, InvocationTargetException, InterruptedException {
        PipedInputStream in = new PipedInputStream();
        XmlHelper.saveDocument(doc, new PipedOutputStream(in));
        writeFile(XML_FILE_NAME, in);
    }

    public Node getUnwatched() throws CoreException, ParserConfigurationException, SAXException, IOException {
        Node unwatched = NodeXmlHelper.getUnwatched(doc);
        String lastDay = NodeXmlHelper.getDate(unwatched);
        String today = DateHelper.calculateCurrentDate();
        if (StringUtils.isNotEmpty(lastDay)) {
            try {
                while (!today.equals(lastDay)) {
                    NodeXmlHelper.createUnwatchedItem(doc, lastDay);
                    lastDay = DateHelper.getNextDay(lastDay);
                }
            } catch (ParseException e) {
                Activator.getDefault().log("Cannot get next day", e, Status.ERROR);
            }
        }
        NodeXmlHelper.setDate(unwatched, today);
        return unwatched;
    }

    public Node getWatched() throws CoreException, ParserConfigurationException, SAXException, IOException {
        return NodeXmlHelper.getWatched(doc);
    }

    private Document getDocument() throws CoreException, ParserConfigurationException, SAXException, IOException {
        if (doc != null) {
            return doc;
        }
        IFile file = getFile(XML_FILE_NAME);
        if (file.exists()) {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = builder.parse(file.getContents(true));
        } else {
            doc = XmlHelper.createDocument();
        }
        return doc;
    }

    public Node getCurrentNode() {
        return NodeXmlHelper.createCurrentNode(doc);
    }

    public void setWatched(Node node, boolean watched) {
        NodeXmlHelper.setWatched(doc, node, watched);
    }
}
