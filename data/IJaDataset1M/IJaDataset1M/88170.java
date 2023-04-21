package de.uni_leipzig.lots.server.persist;

import de.uni_leipzig.lots.common.exceptions.NoSuchEntityException;
import de.uni_leipzig.lots.common.exceptions.NoSuchTaskException;
import de.uni_leipzig.lots.common.exceptions.NoSuchUserException;
import de.uni_leipzig.lots.common.objects.User;
import de.uni_leipzig.lots.common.objects.task.*;
import de.uni_leipzig.lots.common.xml.DOM4JHelper;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultDocument;
import org.dom4j.tree.DefaultElement;
import org.springframework.test.AbstractTransactionalSpringContextTests;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Alexander Kiel
 * @version $Id: TaskRepositoryTest.java,v 1.11 2007/09/13 09:24:38 mai99bxd Exp $
 */
public class TaskRepositoryTest extends AbstractTransactionalSpringContextTests {

    static {
        Logger.getLogger("").setLevel(Level.WARNING);
        Logger.getLogger("de.uni_leipzig.lots").setLevel(Level.SEVERE);
        Logger.getLogger("org.hibernate.SQL").setLevel(Level.FINE);
        Logger.getLogger("org.hibernate").setLevel(Level.WARNING);
        Logger.getLogger("org.springframework").setLevel(Level.WARNING);
    }

    private TaskRepository taskRepository;

    private UserRepository userRepository;

    private GroupRepository groupRepository;

    private ThemeRepository themeRepository;

    private AuthorRepository authorRepository;

    private String mcTaskDocumentString;

    public void setTaskRepository(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setGroupRepository(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public void setThemeRepository(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public void setAuthorRepository(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public void setMcTaskDocumentString(String mcTaskDocumentString) {
        this.mcTaskDocumentString = mcTaskDocumentString;
    }

    public String getMcTaskDocumentString() {
        return (String) applicationContext.getBean("mcTaskDocumentString");
    }

    public User getMax() {
        return (User) applicationContext.getBean("max");
    }

    public MultipleChoiceTask getMcTask() {
        return ((MultipleChoiceTask) applicationContext.getBean("mcTask")).copyContent();
    }

    @Override
    protected String[] getConfigLocations() {
        return new String[] { "testContext.xml", "de/uni_leipzig/lots/server/persist/taskRepositoryTestContext.xml", "de/uni_leipzig/lots/server/persist/repositoryTestData.xml" };
    }

    public void testLoadFail() {
        try {
            taskRepository.load(1L);
            fail();
        } catch (NoSuchTaskException e) {
        }
    }

    public void testLoadSaveDelete() throws Exception {
        MultipleChoiceTask task1 = getMcTask();
        task1.updateCreator(userRepository.load(userRepository.save(task1.getCreator())));
        Set<Theme> themes = new HashSet<Theme>();
        for (Theme theme : task1.getThemes()) {
            themes.add(themeRepository.get(theme.getName()));
        }
        task1.updateThemes(themes);
        Set<Author> authors = new HashSet<Author>();
        for (Author author : task1.getAuthors()) {
            authors.add(authorRepository.get(author.getName()));
        }
        task1.setAuthors(authors);
        for (Image image : task1.getImages()) {
            User creator = image.getCreator();
            try {
                image.updateCreator(userRepository.loadByUsername(creator.getUsername()));
            } catch (NoSuchUserException e) {
                image.updateCreator(userRepository.load(userRepository.save(creator)));
            }
        }
        taskRepository.save(task1);
        setComplete();
        endTransaction();
        startNewTransaction();
        MultipleChoiceTask task2 = (MultipleChoiceTask) taskRepository.load(task1.getTaskId());
        assertEquals(task1.getTaskId(), task2.getTaskId());
        assertEquals(task1.getTitle(), task2.getTitle());
        assertEquals(task1.getNote(), task2.getNote());
        assertEquals(task1.getQuestion(), task2.getQuestion());
        Set<Theme> themes1 = task1.getThemes();
        Set<Theme> themes2 = task2.getThemes();
        for (Theme subTheme1 : themes1) {
            boolean found = false;
            for (Theme subTheme2 : themes2) {
                if (subTheme1.getName().equals(subTheme2.getName())) {
                    found = true;
                }
            }
            if (!found) fail("assert themes equals");
        }
        for (int i = 0; i < task1.getChoices().size(); i++) {
            task1.getChoices().get(i).equals(task2.getChoices().get(i));
        }
        taskRepository.delete(task2);
        User creator = task2.getCreator();
        userRepository.delete(creator);
        for (Theme theme : task2.getThemes()) {
            themeRepository.delete(theme);
        }
        task2.getThemes().clear();
        for (Author author : task2.getAuthors()) {
            authorRepository.delete(author);
        }
        task2.getAuthors().clear();
        setComplete();
        endTransaction();
    }

    public void testExport() throws NoSuchEntityException, IOException {
        MultipleChoiceTask mcTask = getMcTask();
        mcTask.updateCreator(userRepository.load(userRepository.save(mcTask.getCreator())));
        Set<Theme> themes = new HashSet<Theme>();
        for (Theme theme : mcTask.getThemes()) {
            themes.add(themeRepository.get(theme.getName()));
        }
        mcTask.updateThemes(themes);
        Set<Author> authors = new HashSet<Author>();
        for (Author author : mcTask.getAuthors()) {
            authors.add(authorRepository.get(author.getName()));
        }
        mcTask.setAuthors(authors);
        for (Image image : mcTask.getImages()) {
            User creator = image.getCreator();
            try {
                image.updateCreator(userRepository.loadByUsername(creator.getUsername()));
            } catch (NoSuchUserException e) {
                image.updateCreator(userRepository.load(userRepository.save(creator)));
            }
        }
        Long id = taskRepository.save(mcTask);
        taskRepository.flush();
        Node task = taskRepository.exportDOM4J(id);
        Document document = new DefaultDocument();
        document.setRootElement(new DefaultElement("lots-data"));
        Element rootElement = document.getRootElement();
        DefaultElement tasks = new DefaultElement("tasks");
        rootElement.add(tasks);
        tasks.add(task);
        StringWriter writer = new StringWriter();
        XMLWriter xmlWriter = new XMLWriter(writer, DOM4JHelper.getPrettyPrintOutputFormat());
        xmlWriter.write(document);
        xmlWriter.close();
        System.out.println(writer.toString());
    }

    public void testRecentlyChanged() {
        List<Task> recentlyChanged = taskRepository.getRecentlyChanged();
        for (Task task : recentlyChanged) {
            System.out.println(task);
        }
    }

    public void testQueryByText() throws NoSuchUserException {
        MultipleChoiceTask mcTask = getMcTask();
        mcTask.updateCreator(userRepository.load(userRepository.save(mcTask.getCreator())));
        Set<Theme> themes = new HashSet<Theme>();
        for (Theme theme : mcTask.getThemes()) {
            themes.add(themeRepository.get(theme.getName()));
        }
        mcTask.updateThemes(themes);
        Set<Author> authors = new HashSet<Author>();
        for (Author author : mcTask.getAuthors()) {
            authors.add(authorRepository.get(author.getName()));
        }
        mcTask.setAuthors(authors);
        for (Image image : mcTask.getImages()) {
            User creator = image.getCreator();
            try {
                image.updateCreator(userRepository.loadByUsername(creator.getUsername()));
            } catch (NoSuchUserException e) {
                image.updateCreator(userRepository.load(userRepository.save(creator)));
            }
        }
        taskRepository.save(mcTask);
        assertTrue("result", !taskRepository.queryByText("XML").isEmpty());
        assertTrue("result", !taskRepository.queryByText("Testaufgabe").isEmpty());
    }

    public void testGenerateTaskId() {
        TaskId taskId = taskRepository.generateTaskId(getMax());
        assertTrue("initials", taskId.toString().startsWith("mm"));
    }
}
