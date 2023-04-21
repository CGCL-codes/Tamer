package de.objectcode.time4u.importer.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.eclipse.core.runtime.IProgressMonitor;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import de.objectcode.time4u.importer.IImporter;
import de.objectcode.time4u.store.Day;
import de.objectcode.time4u.store.IDMapping;
import de.objectcode.time4u.store.IProjectStore;
import de.objectcode.time4u.store.IRepository;
import de.objectcode.time4u.store.ISyncStore;
import de.objectcode.time4u.store.IWorkItemStore;
import de.objectcode.time4u.store.Project;
import de.objectcode.time4u.store.RepositoryException;
import de.objectcode.time4u.store.Task;
import de.objectcode.time4u.store.WorkItem;
import de.objectcode.time4u.store.meta.MetaType;
import de.objectcode.time4u.store.meta.MetaValue;
import de.objectcode.time4u.util.DateFormat;
import de.objectcode.time4u.util.TimeFormat;

public class XMLImporter implements IImporter {

    public void importFile(IRepository repository, File file, IProgressMonitor progressMonitor) throws IOException {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            Counter counter = new Counter();
            parser.parse(file, counter);
            progressMonitor.beginTask("Importing...", counter.getCounter());
            parser.parse(file, new Handler(repository, progressMonitor));
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e.toString());
        } finally {
            progressMonitor.done();
        }
    }

    private static class Counter extends DefaultHandler {

        int m_counter = 0;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            m_counter++;
        }

        public int getCounter() {
            return m_counter;
        }
    }

    private static class Handler extends DefaultHandler {

        IRepository m_repository;

        IProjectStore m_projectStore;

        IWorkItemStore m_workItemStore;

        LinkedList<Object> m_stack = new LinkedList<Object>();

        Map<Long, Project> m_projectById = new HashMap<Long, Project>();

        Map<Long, Task> m_taskById = new HashMap<Long, Task>();

        List<Task> m_tasks = new ArrayList<Task>();

        List<WorkItem> m_workItems = new ArrayList<WorkItem>();

        String m_syncTargetName;

        long m_latestProjectRevision;

        IDMapping m_projectMapping;

        IDMapping m_taskMapping;

        IProgressMonitor m_progressMonitor;

        Project m_invalidProject = null;

        Task m_invalidTask = null;

        public Handler(IRepository repository, IProgressMonitor progressMonitor) {
            m_repository = repository;
            m_projectStore = repository.getProjectStore();
            m_workItemStore = repository.getWorkItemStore();
            m_progressMonitor = progressMonitor;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if ("project".equals(qName)) {
                Project parent = m_stack.isEmpty() ? null : (Project) m_stack.getLast();
                Project project = new Project();
                String id = attributes.getValue("id");
                project.setName(attributes.getValue("name"));
                project.setActive(Boolean.parseBoolean(attributes.getValue("active")));
                if (parent != null) {
                    project.setParentId(parent.getId());
                }
                try {
                    m_projectStore.storeProject(project);
                    m_projectById.put(Long.parseLong(id), project);
                } catch (RepositoryException e) {
                    throw new SAXException(e);
                }
                m_stack.addLast(project);
                m_progressMonitor.subTask("Importing Project " + project.getName());
            } else if ("task".equals(qName)) {
                Project project = m_stack.isEmpty() ? null : (Project) m_stack.getLast();
                Task task = new Task();
                String id = attributes.getValue("id");
                task.setName(attributes.getValue("name"));
                task.setActive(Boolean.parseBoolean(attributes.getValue("active")));
                if (project != null) {
                    task.setProjectId(project.getId());
                }
                m_tasks.add(task);
                m_taskById.put(Long.parseLong(id), task);
                m_stack.addLast(task);
            } else if ("meta".equals(qName)) {
                String name = attributes.getValue("name");
                String type = attributes.getValue("type");
                String value = attributes.getValue("value");
                Object parent = m_stack.getLast();
                if (parent instanceof Project) {
                    Project project = (Project) parent;
                    project.getMetaProperties().put(name, new MetaValue(MetaType.valueOf(type), value));
                    try {
                        m_projectStore.storeProject(project);
                    } catch (RepositoryException e) {
                        throw new SAXException(e);
                    }
                } else if (parent instanceof Task) {
                    Task task = (Task) parent;
                    task.getMetaProperties().put(name, new MetaValue(MetaType.valueOf(type), value));
                }
            } else if ("syncTarget".equals(qName)) {
                m_syncTargetName = attributes.getValue("name");
                m_latestProjectRevision = Long.parseLong(attributes.getValue("latestProjectRevision"));
                m_projectMapping = new IDMapping();
                m_taskMapping = new IDMapping();
            } else if ("projectMapping".equals(qName)) {
                long clientId = Long.parseLong(attributes.getValue("clientId"));
                Project project = m_projectById.get(clientId);
                if (project != null) {
                    m_projectMapping.set(project.getId(), Long.parseLong(attributes.getValue("serverId")), Boolean.getBoolean(attributes.getValue("dirty")));
                }
            } else if ("taskMapping".equals(qName)) {
                long clientId = Long.parseLong(attributes.getValue("clientId"));
                Task task = m_taskById.get(clientId);
                if (task != null) {
                    m_taskMapping.set(task.getId(), Long.parseLong(attributes.getValue("serverId")), Boolean.getBoolean(attributes.getValue("dirty")));
                }
            } else if ("workitem".equals(qName)) {
                WorkItem workItem = new WorkItem();
                Project project = null;
                Task task = null;
                if ((attributes.getValue("project") != null) && (attributes.getValue("task") != null)) {
                    long projectId = Long.parseLong(attributes.getValue("project"));
                    long taskId = Long.parseLong(attributes.getValue("task"));
                    project = m_projectById.get(projectId);
                    task = m_taskById.get(taskId);
                }
                if ((task != null) && (project != null) && !task.isGeneric() && (task.getProjectId() != project.getId())) {
                    project = null;
                    task = null;
                }
                if ((project == null) || (task == null)) {
                    if (m_invalidProject == null) {
                        m_invalidProject = new Project();
                        m_invalidProject.setName("INVALID");
                        m_invalidProject.setActive(false);
                        m_invalidProject.setDeleted(true);
                        m_invalidProject.setDescription("Invalid Project from import");
                        try {
                            m_projectStore.storeProject(m_invalidProject);
                        } catch (RepositoryException e) {
                            throw new SAXException(e);
                        }
                    }
                    project = m_invalidProject;
                    if (m_invalidTask == null) {
                        m_invalidTask = new Task();
                        m_invalidTask.setName("INVALID");
                        m_invalidTask.setActive(false);
                        m_invalidTask.setDeleted(true);
                        m_invalidTask.setDescription("Invalid Task from import");
                        m_invalidTask.setProjectId(m_invalidProject.getId());
                        try {
                            m_projectStore.storeTask(m_invalidTask);
                        } catch (RepositoryException e) {
                            throw new SAXException(e);
                        }
                    }
                    task = m_invalidTask;
                }
                workItem.setDay(DateFormat.parse(attributes.getValue("day")));
                workItem.setBegin(TimeFormat.parse(attributes.getValue("begin")));
                workItem.setEnd(TimeFormat.parse(attributes.getValue("end")));
                workItem.setComment(attributes.getValue("comment"));
                workItem.setProjectId(project.getId());
                workItem.setTaskId(task.getId());
                m_workItems.add(workItem);
                if (m_workItems.size() > 100) {
                    try {
                        m_workItemStore.storeWorkItems(m_workItems, true);
                        m_workItems.clear();
                    } catch (RepositoryException e) {
                        throw new SAXException(e);
                    }
                }
                m_progressMonitor.subTask("Importing WorkItem " + workItem.getDay().toString());
            } else if ("dayInfo".equals(qName)) {
                Day day = DateFormat.parse(attributes.getValue("day"));
                int regularTime = Integer.parseInt(attributes.getValue("regularTime"));
                try {
                    m_workItemStore.setRegularTime(day, day, regularTime);
                } catch (RepositoryException e) {
                    throw new SAXException(e);
                }
            }
            super.startElement(uri, localName, qName, attributes);
            m_progressMonitor.worked(1);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if ("project".equals(qName)) {
                m_stack.removeLast();
            } else if ("task".equals(qName)) {
                m_stack.removeLast();
            } else if ("projects".equals(qName)) {
                try {
                    m_projectStore.storeTasks(m_tasks);
                    m_tasks.clear();
                } catch (RepositoryException e) {
                    throw new SAXException(e);
                }
            } else if ("syncTarget".equals(qName)) {
                if (m_syncTargetName != null) {
                    ISyncStore syncStore = m_repository.getSyncStore(m_syncTargetName);
                    try {
                        syncStore.markProjectSynchronized(m_latestProjectRevision, m_projectMapping, m_taskMapping);
                    } catch (RepositoryException e) {
                        throw new SAXException(e);
                    }
                    m_syncTargetName = null;
                    m_projectMapping = null;
                    m_taskMapping = null;
                }
            } else if ("workitems".equals(qName)) {
                if (m_workItems.size() > 0) {
                    try {
                        m_workItemStore.storeWorkItems(m_workItems, true);
                        m_workItems.clear();
                    } catch (RepositoryException e) {
                        throw new SAXException(e);
                    }
                }
            }
            super.endElement(uri, localName, qName);
        }
    }
}
