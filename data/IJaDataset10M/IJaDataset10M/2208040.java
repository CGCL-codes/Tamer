package br.upe.dsc.caeto.core.project;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import br.upe.dsc.caeto.core.IRepository;
import br.upe.dsc.caeto.utils.xml.PropertiesXml;
import br.upe.dsc.caeto.utils.xml.ProjectXml;

public class ProjectRepositoryXml implements IRepository<Project>, Iterable<Project> {

    private Project project;

    private PropertiesXml caetoProperties;

    private ProjectXml projectXml;

    private Document document;

    private String userDir;

    private String separator;

    public ProjectRepositoryXml() {
        caetoProperties = new PropertiesXml();
        projectXml = new ProjectXml();
        userDir = System.getProperties().getProperty("user.dir");
        separator = System.getProperties().getProperty("file.separator");
    }

    public void insert(Project project) {
        if (project != null) {
            caetoProperties.write(project);
            projectXml.write(project);
        }
    }

    public void remove(Project project) {
        Document doc;
        try {
            doc = caetoProperties.read();
            Element rootElement = doc.getRootElement();
            List<Element> list = rootElement.getChildren("project");
            for (Element element : list) {
                if (element.getAttributeValue("name").equals(project.getName())) {
                    rootElement.removeContent(element);
                    caetoProperties.write(doc);
                    return;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: file not found.");
        } catch (IOException e) {
            System.out.println("Error: file not found.");
        }
    }

    public void update(Project project) {
        remove(project);
        insert(project);
    }

    public Project search(String name) {
        List<Element> list;
        try {
            list = caetoProperties.read().getRootElement().getChildren("project");
            String projectFile;
            for (Element element : list) {
                if (element.getAttributeValue("name").equals(name)) {
                    projectFile = element.getChildText("path") + separator + element.getAttributeValue("name");
                    return projectXml.getProject(projectXml.read(projectFile));
                }
            }
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    public boolean isElement(String name) {
        List<Element> list;
        try {
            list = caetoProperties.read().getRootElement().getChildren("project");
            for (Element element : list) {
                if (element.getAttributeValue("name").equals(name)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    public void empty() {
    }

    public Iterator<Project> iterator() {
        List<Project> projects = new LinkedList();
        List<Element> list;
        try {
            list = caetoProperties.read().getRootElement().getChildren("project");
            String projectFile;
            for (Element element : list) {
                projectFile = element.getChildText("path") + separator + element.getAttributeValue("name");
                projects.add(projectXml.getProject(projectXml.read(projectFile)));
            }
        } catch (FileNotFoundException e) {
            list = new LinkedList();
        } catch (IOException e) {
            list = new LinkedList();
        }
        return projects.iterator();
    }

    public Iterable<Project> getElements() {
        List<Project> projects = new LinkedList();
        List<Element> list;
        try {
            list = caetoProperties.read().getRootElement().getChildren("project");
        } catch (NullPointerException e) {
            list = new LinkedList();
        } catch (FileNotFoundException e) {
            list = new LinkedList();
        } catch (IOException e) {
            list = new LinkedList();
        }
        String projectFile;
        for (Element element : list) {
            projectFile = element.getChildText("path") + separator + element.getAttributeValue("name");
            projects.add(projectXml.getProject(projectXml.read(projectFile)));
        }
        return projects;
    }
}
