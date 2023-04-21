package de.objectcode.time4u.exporter.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import de.objectcode.time4u.exporter.IExporter;
import de.objectcode.time4u.store.Day;
import de.objectcode.time4u.store.DayInfo;
import de.objectcode.time4u.store.IDMapping;
import de.objectcode.time4u.store.IProjectStore;
import de.objectcode.time4u.store.IRepository;
import de.objectcode.time4u.store.ISyncStore;
import de.objectcode.time4u.store.IWorkItemStore;
import de.objectcode.time4u.store.IWorkItemVisitor;
import de.objectcode.time4u.store.Project;
import de.objectcode.time4u.store.RepositoryException;
import de.objectcode.time4u.store.Task;
import de.objectcode.time4u.store.WorkItem;
import de.objectcode.time4u.store.WorkItemFilter;
import de.objectcode.time4u.store.meta.MetaValue;
import de.objectcode.time4u.util.DateFormat;
import de.objectcode.time4u.util.TimeFormat;

public class XMLExporter implements IExporter {

    public void exportFile(IRepository repository, File file, String stdSyncTarget, IProgressMonitor progressMonitor) throws IOException {
        try {
            progressMonitor.beginTask("Export Projects/Tasks", 4);
            final Set<Long> projectIds = new HashSet<Long>();
            final Set<Long> taskIds = new HashSet<Long>();
            SAXTransformerFactory factory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
            final TransformerHandler handler = factory.newTransformerHandler();
            FileOutputStream out = new FileOutputStream(file);
            handler.getTransformer().setOutputProperty(OutputKeys.METHOD, "xml");
            handler.getTransformer().setOutputProperty(OutputKeys.INDENT, "yes");
            handler.getTransformer().setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            handler.setResult(new StreamResult(out));
            handler.startDocument();
            handler.startElement("", "export", "export", new AttributesImpl());
            handler.startElement("", "projects", "projects", new AttributesImpl());
            exportProjects(repository.getProjectStore(), projectIds, taskIds, null, handler, new SubProgressMonitor(progressMonitor, 1));
            handler.endElement("", "projects", "projects");
            handler.startElement("", "syncStatus", "syncStatus", new AttributesImpl());
            exportSyncStatus(repository.getAllSyncStores(), stdSyncTarget, projectIds, taskIds, handler, new SubProgressMonitor(progressMonitor, 1));
            handler.endElement("", "syncStatus", "syncStatus");
            handler.startElement("", "workitems", "workitems", new AttributesImpl());
            progressMonitor.setTaskName("Export Workitems...");
            final IWorkItemStore workItemStore = repository.getWorkItemStore();
            final Set<Integer> monthYears = new TreeSet<Integer>();
            workItemStore.iterate(new WorkItemFilter(), new IWorkItemVisitor() {

                public boolean visit(IRepository repository, WorkItem workItem) {
                    try {
                        monthYears.add(workItem.getDay().getMonth() + (100 * workItem.getDay().getYear()));
                        AttributesImpl attrs = new AttributesImpl();
                        attrs.addAttribute("", "day", "day", "CDATA", DateFormat.format(workItem.getDay()));
                        attrs.addAttribute("", "begin", "begin", "CDATA", TimeFormat.format(workItem.getBegin()));
                        attrs.addAttribute("", "end", "end", "CDATA", TimeFormat.format(workItem.getEnd()));
                        if (projectIds.contains(workItem.getProjectId())) {
                            attrs.addAttribute("", "project", "project", "IDREF", String.valueOf(workItem.getProjectId()));
                        }
                        if (taskIds.contains(workItem.getTaskId())) {
                            attrs.addAttribute("", "task", "task", "IDREF", String.valueOf(workItem.getTaskId()));
                        }
                        attrs.addAttribute("", "comment", "comment", "CDATA", workItem.getComment());
                        handler.startElement("", "workitem", "workitem", attrs);
                        handler.endElement("", "workitem", "workitem");
                    } catch (Exception e) {
                    }
                    return true;
                }
            });
            handler.endElement("", "workitems", "workitems");
            progressMonitor.worked(1);
            handler.startElement("", "days", "days", new AttributesImpl());
            for (int monthYear : monthYears) {
                int month = monthYear % 100;
                int year = monthYear / 100;
                Map<Day, ? extends DayInfo> dayInfos = workItemStore.getMonth(month, year);
                for (Day day : dayInfos.keySet()) {
                    DayInfo dayInfo = dayInfos.get(day);
                    if (dayInfo.getRegularTime() >= 0) {
                        AttributesImpl attrs = new AttributesImpl();
                        attrs.addAttribute("", "day", "day", "CDATA", DateFormat.format(day));
                        attrs.addAttribute("", "regularTime", "regularTime", "CDATA", String.valueOf(dayInfo.getRegularTime()));
                        handler.startElement("", "dayInfo", "dayInfo", attrs);
                        handler.endElement("", "dayInfo", "dayInfo");
                    }
                }
            }
            handler.endElement("", "days", "days");
            handler.endElement("", "export", "export");
            handler.endDocument();
            out.close();
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e.toString());
        } finally {
            progressMonitor.done();
        }
    }

    private void exportProjects(IProjectStore projectStore, Set<Long> projectIds, Set<Long> taskIds, Project project, ContentHandler handler, IProgressMonitor progressMonitor) throws RepositoryException, SAXException {
        try {
            Collection<Project> childProjects;
            if (project == null) {
                childProjects = projectStore.getRootProjects(false);
            } else {
                childProjects = projectStore.getChildProjects(project.getId(), false);
                AttributesImpl attrs = new AttributesImpl();
                attrs.addAttribute("", "id", "id", "ID", String.valueOf(project.getId()));
                attrs.addAttribute("", "name", "name", "CDATA", project.getName());
                attrs.addAttribute("", "active", "active", "CDATA", String.valueOf(project.isActive()));
                handler.startElement("", "project", "project", attrs);
                projectIds.add(project.getId());
                Map<String, MetaValue> projectProperties = project.getMetaProperties();
                for (String name : projectProperties.keySet()) {
                    MetaValue value = projectProperties.get(name);
                    AttributesImpl metaAttrs = new AttributesImpl();
                    metaAttrs.addAttribute("", "name", "name", "CDATA", name);
                    metaAttrs.addAttribute("", "type", "type", "CDATA", value.getType().toString());
                    metaAttrs.addAttribute("", "value", "value", "CDATA", value.toString());
                    handler.startElement("", "meta", "meta", metaAttrs);
                    handler.endElement("", "meta", "meta");
                }
            }
            Collection<Task> tasks;
            if (project == null) {
                tasks = projectStore.getTasks(null, false);
            } else {
                tasks = projectStore.getTasks(project.getId(), false);
            }
            progressMonitor.beginTask("Export project " + ((project == null) ? "ROOT" : project.getName()), ((childProjects != null) ? childProjects.size() : 0) + ((tasks != null) ? tasks.size() : 0));
            if (childProjects != null) {
                for (Project childProject : childProjects) {
                    exportProjects(projectStore, projectIds, taskIds, childProject, handler, new SubProgressMonitor(progressMonitor, 1));
                }
            }
            if (tasks != null) {
                for (Task task : tasks) {
                    if (!task.isGeneric() && (task.getProjectId() == null)) {
                        continue;
                    }
                    AttributesImpl taskAttrs = new AttributesImpl();
                    taskAttrs.addAttribute("", "id", "id", "ID", String.valueOf(task.getId()));
                    taskAttrs.addAttribute("", "name", "name", "CDATA", task.getName());
                    taskAttrs.addAttribute("", "active", "active", "CDATA", String.valueOf(task.isActive()));
                    taskAttrs.addAttribute("", "generic", "generic", "CDATA", String.valueOf(task.isGeneric()));
                    handler.startElement("", "task", "task", taskAttrs);
                    taskIds.add(task.getId());
                    Map<String, MetaValue> taskProperties = task.getMetaProperties();
                    for (String name : taskProperties.keySet()) {
                        MetaValue value = taskProperties.get(name);
                        AttributesImpl metaAttrs = new AttributesImpl();
                        metaAttrs.addAttribute("", "name", "name", "CDATA", name);
                        metaAttrs.addAttribute("", "type", "type", "CDATA", value.getType().toString());
                        metaAttrs.addAttribute("", "value", "value", "CDATA", value.toString());
                        handler.startElement("", "meta", "meta", metaAttrs);
                        handler.endElement("", "meta", "meta");
                    }
                    handler.endElement("", "task", "task");
                    progressMonitor.worked(1);
                }
                if (project != null) {
                    handler.endElement("", "project", "project");
                }
            }
        } finally {
            progressMonitor.done();
        }
    }

    private void exportSyncStatus(ISyncStore[] syncStores, String stdSyncTarget, Set<Long> projectIds, Set<Long> taskIds, ContentHandler handler, IProgressMonitor progressMonitor) throws RepositoryException, SAXException {
        try {
            progressMonitor.beginTask("Expoting sync status", syncStores.length);
            for (ISyncStore syncStore : syncStores) {
                AttributesImpl syncTargetAttrs = new AttributesImpl();
                String syncTarget = syncStore.getSyncTarget();
                if ("Std".equals(syncTarget)) {
                    syncTarget = stdSyncTarget;
                }
                syncTargetAttrs.addAttribute("", "name", "name", "CDATA", syncTarget);
                syncTargetAttrs.addAttribute("", "latestProjectRevision", "latestProjectRevision", "CDATA", String.valueOf(syncStore.getLatestProjectRevision()));
                handler.startElement("", "syncTarget", "syncTarget", syncTargetAttrs);
                IDMapping projectMapping = syncStore.getProjectMapping();
                for (IDMapping.Entry entry : projectMapping.getEntries()) {
                    AttributesImpl mappingAttrs = new AttributesImpl();
                    mappingAttrs.addAttribute("", "clientId", "clientId", "CDATA", String.valueOf(entry.getClientId()));
                    mappingAttrs.addAttribute("", "serverId", "serverId", "CDATA", String.valueOf(entry.getServerId()));
                    mappingAttrs.addAttribute("", "dirty", "dirty", "CDATA", String.valueOf(entry.isDirty()));
                    handler.startElement("", "projectMapping", "projectMapping", mappingAttrs);
                    handler.endElement("", "projectMapping", "projectMapping");
                }
                IDMapping taskMapping = syncStore.getTaskMapping();
                for (IDMapping.Entry entry : taskMapping.getEntries()) {
                    AttributesImpl mappingAttrs = new AttributesImpl();
                    mappingAttrs.addAttribute("", "clientId", "clientId", "CDATA", String.valueOf(entry.getClientId()));
                    mappingAttrs.addAttribute("", "serverId", "serverId", "CDATA", String.valueOf(entry.getServerId()));
                    mappingAttrs.addAttribute("", "dirty", "dirty", "CDATA", String.valueOf(entry.isDirty()));
                    handler.startElement("", "taskMapping", "taskMapping", mappingAttrs);
                    handler.endElement("", "taskMapping", "taskMapping");
                }
                handler.endElement("", "syncTarget", "syncTarget");
                progressMonitor.worked(1);
            }
        } finally {
            progressMonitor.done();
        }
    }
}
