package de.objectcode.time4u.store.fs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import de.objectcode.time4u.store.TimePolicy;
import de.objectcode.time4u.store.ITodoStore;
import de.objectcode.time4u.store.Person;
import de.objectcode.time4u.store.RepositoryException;
import de.objectcode.time4u.store.Team;
import de.objectcode.time4u.store.Todo;
import de.objectcode.time4u.store.UserContext;
import de.objectcode.time4u.store.WeekTimePolicy;
import de.objectcode.time4u.store.impl.TodoRepositoryEvent;
import de.objectcode.time4u.store.meta.MetaType;
import de.objectcode.time4u.store.meta.MetaValue;
import de.objectcode.time4u.util.DateFormat;

public class FSTodoStore implements ITodoStore {

    private final int VERSION = 2;

    final FSRepository m_repository;

    final FSSyncStore m_syncStore;

    final File m_personFile;

    final File m_todoFile;

    final Map<Long, Person> m_personsById;

    final Map<Long, TimePolicy[]> m_timePoliciesById;

    final Map<Long, Team> m_teamsById;

    final Map<String, Person> m_personsByUserId;

    final Map<Long, Todo> m_todos;

    Person m_activePerson;

    long m_personMaxId;

    long m_todoMaxId;

    FSTodoStore(final File baseDir, UserContext userContext, FSRepository repository, FSSyncStore syncStore) throws IOException, RepositoryException {
        m_repository = repository;
        m_syncStore = syncStore;
        m_personFile = new File(baseDir, "persons.xml");
        m_todoFile = new File(baseDir, "todos.xml");
        m_personsById = new TreeMap<Long, Person>();
        m_timePoliciesById = new HashMap<Long, TimePolicy[]>();
        m_personsByUserId = new TreeMap<String, Person>();
        m_teamsById = new TreeMap<Long, Team>();
        m_todos = new TreeMap<Long, Todo>();
        if (m_personFile.exists()) readPersonTeams();
        if (m_todoFile.exists()) readTodos();
        m_activePerson = m_personsByUserId.get(userContext.getUserId());
        if (m_activePerson == null) {
            m_activePerson = new Person();
            m_activePerson.setId(++m_personMaxId);
            m_activePerson.setName(userContext.getUserId());
            m_activePerson.setUserId(userContext.getUserId());
            m_personsByUserId.put(m_activePerson.getUserId(), m_activePerson);
            m_personsById.put(m_activePerson.getId(), m_activePerson);
            storePersons();
        }
    }

    public synchronized List<Todo> getAllAssignedTodos(boolean onlyActive) throws RepositoryException {
        List<Todo> todos = new ArrayList<Todo>();
        if (onlyActive) {
            for (Todo todo : m_todos.values()) {
                if (!todo.isDeleted() && !todo.isCompleted()) todos.add(todo);
            }
        } else {
            for (Todo todo : m_todos.values()) {
                if (!todo.isDeleted()) todos.add(todo);
            }
        }
        return todos;
    }

    public Person getCurrentPerson() throws RepositoryException {
        return m_activePerson;
    }

    public synchronized List<Person> getAllPersons() throws RepositoryException {
        return new ArrayList<Person>(m_personsByUserId.values());
    }

    public List<Team> getAllTeams() throws RepositoryException {
        return new ArrayList<Team>(m_teamsById.values());
    }

    public List<Todo> getAllReportedTodos(boolean onlyActive) throws RepositoryException {
        List<Todo> todos = new ArrayList<Todo>();
        if (onlyActive) {
            for (Todo todo : m_todos.values()) {
                if (!todo.isDeleted() && !todo.isCompleted() && todo.getReporterId() != null && todo.getReporterId().longValue() == m_activePerson.getId()) todos.add(todo);
            }
        } else {
            for (Todo todo : m_todos.values()) {
                if (!todo.isDeleted() && todo.getReporterId() != null && todo.getReporterId().longValue() == m_activePerson.getId()) todos.add(todo);
            }
        }
        return todos;
    }

    public void deleteTodo(Todo todo) throws RepositoryException {
        Todo toDelete = m_todos.get(todo.getId());
        if (toDelete == null) return;
        toDelete.setDeleted(true);
        storeTodos();
        m_repository.fireRepositoryEvent(new TodoRepositoryEvent(todo));
    }

    public synchronized void storeTodo(Todo todo) throws RepositoryException {
        if (todo.getId() <= 0L) todo.setId(++m_todoMaxId);
        todo.setDeleted(false);
        m_todos.put(todo.getId(), todo);
        storeTodos();
        m_repository.fireRepositoryEvent(new TodoRepositoryEvent(todo));
    }

    public synchronized void storeTodos(Collection<Todo> todos) throws RepositoryException {
        for (Todo todo : todos) {
            if (todo.getId() <= 0L) todo.setId(++m_todoMaxId);
            todo.setDeleted(false);
            m_todos.put(todo.getId(), todo);
        }
        storeTodos();
        m_repository.fireRepositoryEvent(new TodoRepositoryEvent(todos));
    }

    public synchronized TimePolicy[] getTimePolicies() {
        return m_timePoliciesById.get(m_activePerson.getId());
    }

    public synchronized void setTimePolicies(TimePolicy[] timePolicies) throws RepositoryException {
        m_timePoliciesById.put(m_activePerson.getId(), timePolicies);
        storePersons();
    }

    protected void readPersonTeams() throws IOException {
        m_personsById.clear();
        m_personsByUserId.clear();
        m_teamsById.clear();
        m_personMaxId = 0L;
        Document document = XMLUtils.read(m_personFile);
        Element root = document.getDocumentElement();
        NodeList children = root.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if ("person".equals(children.item(i).getNodeName())) {
                Element personElement = (Element) children.item(i);
                Person person = new Person();
                person.setId(Long.parseLong(personElement.getAttribute("id")));
                person.setName(personElement.getAttribute("name"));
                person.setUserId(personElement.getAttribute("userId"));
                person.setEmail(personElement.getAttribute("email"));
                m_personsById.put(person.getId(), person);
                m_personsByUserId.put(person.getUserId(), person);
                if (person.getId() > m_personMaxId) m_personMaxId = person.getId();
                NodeList personChildren = personElement.getChildNodes();
                List<TimePolicy> timePolicies = new ArrayList<TimePolicy>();
                for (int j = 0; j < personChildren.getLength(); j++) {
                    if ("weektimepolicy".equals(personChildren.item(j).getNodeName())) {
                        Element timePolicyElement = (Element) personChildren.item(j);
                        WeekTimePolicy weekTimePolicy = new WeekTimePolicy();
                        if (timePolicyElement.hasAttribute("from")) weekTimePolicy.setFrom(DateFormat.parse(timePolicyElement.getAttribute("from")));
                        if (timePolicyElement.hasAttribute("until")) weekTimePolicy.setUntil(DateFormat.parse(timePolicyElement.getAttribute("until")));
                        weekTimePolicy.setMondayTime(Integer.parseInt(timePolicyElement.getAttribute("monday")));
                        weekTimePolicy.setTuesdayTime(Integer.parseInt(timePolicyElement.getAttribute("tuesday")));
                        weekTimePolicy.setWednesdayTime(Integer.parseInt(timePolicyElement.getAttribute("wednesday")));
                        weekTimePolicy.setThursdayTime(Integer.parseInt(timePolicyElement.getAttribute("thursday")));
                        weekTimePolicy.setFridayTime(Integer.parseInt(timePolicyElement.getAttribute("friday")));
                        weekTimePolicy.setSaturdayTime(Integer.parseInt(timePolicyElement.getAttribute("saturday")));
                        weekTimePolicy.setSundayTime(Integer.parseInt(timePolicyElement.getAttribute("sunday")));
                        timePolicies.add(weekTimePolicy);
                    }
                }
                if (timePolicies.size() > 0) {
                    m_timePoliciesById.put(person.getId(), timePolicies.toArray(new TimePolicy[timePolicies.size()]));
                }
            } else if ("team".equals(children.item(i).getNodeName())) {
                Element teamElement = (Element) children.item(i);
                Team team = new Team();
                team.setId(Long.parseLong(teamElement.getAttribute("id")));
                team.setName(teamElement.getAttribute("name"));
                NodeList memberList = teamElement.getChildNodes();
                Set<Long> memberIds = new HashSet<Long>();
                for (int j = 0; j < memberList.getLength(); j++) {
                    if ("member".equals(memberList.item(j).getNodeName())) {
                        Element memberElement = (Element) memberList.item(j);
                        memberIds.add(Long.parseLong(memberElement.getAttribute("id")));
                    }
                }
                team.setMemberIds(memberIds);
                m_teamsById.put(team.getId(), team);
            }
        }
    }

    protected synchronized void storePersons() throws RepositoryException {
        Document document = XMLUtils.newDocument();
        Element root = document.createElement("persons");
        document.appendChild(root);
        for (Person person : m_personsById.values()) {
            Element personElement = document.createElement("person");
            personElement.setAttribute("id", String.valueOf(person.getId()));
            personElement.setAttribute("name", person.getName());
            personElement.setAttribute("userId", person.getUserId());
            personElement.setAttribute("email", person.getEmail());
            root.appendChild(personElement);
            TimePolicy timePolicies[] = m_timePoliciesById.get(person.getId());
            if (timePolicies != null) {
                for (int i = 0; i < timePolicies.length; i++) {
                    if (timePolicies[i] instanceof WeekTimePolicy) {
                        WeekTimePolicy weekTimePolicy = (WeekTimePolicy) timePolicies[i];
                        Element timePolicyElement = document.createElement("weektimepolicy");
                        if (weekTimePolicy.getFrom() != null) timePolicyElement.setAttribute("from", DateFormat.format(weekTimePolicy.getFrom()));
                        if (weekTimePolicy.getUntil() != null) timePolicyElement.setAttribute("until", DateFormat.format(weekTimePolicy.getUntil()));
                        timePolicyElement.setAttribute("monday", String.valueOf(weekTimePolicy.getMondayTime()));
                        timePolicyElement.setAttribute("tuesday", String.valueOf(weekTimePolicy.getTuesdayTime()));
                        timePolicyElement.setAttribute("wednesday", String.valueOf(weekTimePolicy.getWednesdayTime()));
                        timePolicyElement.setAttribute("thursday", String.valueOf(weekTimePolicy.getThursdayTime()));
                        timePolicyElement.setAttribute("friday", String.valueOf(weekTimePolicy.getFridayTime()));
                        timePolicyElement.setAttribute("saturday", String.valueOf(weekTimePolicy.getSaturdayTime()));
                        timePolicyElement.setAttribute("sunday", String.valueOf(weekTimePolicy.getSundayTime()));
                        personElement.appendChild(timePolicyElement);
                    }
                }
            }
        }
        for (Team team : m_teamsById.values()) {
            Element teamElement = document.createElement("team");
            teamElement.setAttribute("id", String.valueOf(team.getId()));
            teamElement.setAttribute("name", team.getName());
            root.appendChild(teamElement);
            for (long memberId : team.getMemberIds()) {
                Element memberElement = document.createElement("member");
                memberElement.setAttribute("id", String.valueOf(memberId));
                teamElement.appendChild(memberElement);
            }
        }
        try {
            XMLUtils.writeSafe(m_personFile, document);
        } catch (Exception e) {
            throw new RepositoryException(e);
        }
    }

    protected void readTodos() throws IOException {
        m_todos.clear();
        m_todoMaxId = 0L;
        Document document = XMLUtils.read(m_todoFile);
        Element root = document.getDocumentElement();
        NodeList children = root.getChildNodes();
        if (!root.hasAttribute("version") || Integer.parseInt(root.getAttribute("version")) < VERSION) return;
        for (int i = 0; i < children.getLength(); i++) {
            if ("todo".equals(children.item(i).getNodeName())) {
                Element todoElement = (Element) children.item(i);
                Todo todo = new Todo();
                todo.setId(Long.parseLong(todoElement.getAttribute("id")));
                todo.setProjectId(Long.parseLong(todoElement.getAttribute("projectId")));
                todo.setTaskId(Long.parseLong(todoElement.getAttribute("taskId")));
                todo.setPriority(Integer.parseInt(todoElement.getAttribute("priority")));
                todo.setHeader(todoElement.getAttribute("header"));
                todo.setDescription(todoElement.getAttribute("description"));
                todo.setCompleted(Boolean.parseBoolean(todoElement.getAttribute("completed")));
                if (todoElement.hasAttribute("deleted")) todo.setDeleted(Boolean.parseBoolean(todoElement.getAttribute("deleted")));
                if (todoElement.hasAttribute("assignedToPersonId")) todo.setAssignedToPersonId(Long.parseLong(todoElement.getAttribute("assignedToPersonId")));
                if (todoElement.hasAttribute("assignedToTeamId")) todo.setAssignedToTeamId(Long.parseLong(todoElement.getAttribute("assignedToTeamId")));
                if (todoElement.hasAttribute("reporterId")) todo.setReporterId(Long.parseLong(todoElement.getAttribute("reporterId")));
                todo.setCreatedOn(DateFormat.parse(todoElement.getAttribute("createdOn")));
                if (todoElement.hasAttribute("deadline")) todo.setDeadline(DateFormat.parse(todoElement.getAttribute("deadline")));
                if (todoElement.hasAttribute("completedAt")) todo.setCompletedAt(DateFormat.parse(todoElement.getAttribute("completedAt")));
                NodeList todoChildren = todoElement.getChildNodes();
                for (int j = 0; j < todoChildren.getLength(); j++) {
                    if ("meta".equals(todoChildren.item(j).getNodeName())) {
                        Element metaProperty = (Element) todoChildren.item(j);
                        String name = metaProperty.getAttribute("name");
                        String type = metaProperty.getAttribute("type");
                        String value = metaProperty.getAttribute("value");
                        todo.getMetaProperties().put(name, new MetaValue(MetaType.valueOf(type), value));
                    }
                }
                m_todos.put(todo.getId(), todo);
                if (todo.getId() > m_todoMaxId) m_todoMaxId = todo.getId();
            }
        }
    }

    protected synchronized void storeTodos() throws RepositoryException {
        Document document = XMLUtils.newDocument();
        Element root = document.createElement("todos");
        root.setAttribute("version", String.valueOf(VERSION));
        document.appendChild(root);
        for (Todo todo : m_todos.values()) {
            Element todoElement = document.createElement("todo");
            todoElement.setAttribute("id", String.valueOf(todo.getId()));
            todoElement.setAttribute("projectId", String.valueOf(todo.getProjectId()));
            todoElement.setAttribute("taskId", String.valueOf(todo.getTaskId()));
            todoElement.setAttribute("priority", String.valueOf(todo.getPriority()));
            todoElement.setAttribute("header", todo.getHeader());
            todoElement.setAttribute("description", todo.getDescription());
            todoElement.setAttribute("completed", String.valueOf(todo.isCompleted()));
            if (todo.isDeleted()) todoElement.setAttribute("deleted", String.valueOf(todo.isDeleted()));
            if (todo.getAssignedToPersonId() != null) todoElement.setAttribute("assignedToPersonId", todo.getAssignedToPersonId().toString());
            if (todo.getAssignedToTeamId() != null) todoElement.setAttribute("assignedToTeamId", todo.getAssignedToTeamId().toString());
            if (todo.getReporterId() != null) todoElement.setAttribute("reporterId", todo.getReporterId().toString());
            todoElement.setAttribute("createdOn", DateFormat.format(todo.getCreatedOn()));
            if (todo.getDeadline() != null) todoElement.setAttribute("deadline", DateFormat.format(todo.getDeadline()));
            if (todo.getCompletedAt() != null) todoElement.setAttribute("completedAt", DateFormat.format(todo.getCompletedAt()));
            root.appendChild(todoElement);
            Map<String, MetaValue> taskProperties = todo.getMetaProperties();
            for (String name : taskProperties.keySet()) {
                MetaValue value = taskProperties.get(name);
                Element metaElement = document.createElement("meta");
                metaElement.setAttribute("name", name);
                metaElement.setAttribute("type", value.getType().toString());
                metaElement.setAttribute("value", value.toString());
                todoElement.appendChild(metaElement);
            }
        }
        try {
            XMLUtils.writeSafe(m_todoFile, document);
        } catch (Exception e) {
            throw new RepositoryException(e);
        }
    }
}
