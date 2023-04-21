package ApiScrumClass;

import java.util.Date;
import java.util.List;

/**
 * Main interface, PersistenceMySQL, inherit from this class letting use
 * all its methods.
 * @author David
 */
public interface Persistence {

    void GetValues();

    void GetStatement(final String url, final String login, final String password);

    public boolean SaveProduct(Product p);

    public boolean SaveTheme(Theme t);

    public boolean SaveStory(Story s);

    public boolean SaveRelease(Release r);

    public boolean SaveSprint(Sprint s);

    public boolean SaveTask(Task t);

    public boolean SaveStage(Stage s);

    public boolean SaveStageSequence(StageSequence s);

    public boolean SaveWorkDone(WorkDone w);

    public boolean SavePerson(Person p);

    public boolean SaveReestimation(Reestimation r);

    public List<Product> GetProductList();

    public List<Theme> GetThemeList(final int parent);

    public List<Theme> GetThemeListProduct(final int parentProduct);

    public List<Story> GetStoryListThemes(final int parent);

    public List<Story> GetStoryListSprint(final int parent);

    public List<Story> GetStoryListStage(final int parent);

    public List<Release> GetReleaseList(final int parent);

    public List<Sprint> GetSprintList(final int parent);

    public List<Task> GetTaskList(final int parent);

    public List<WorkDone> GetWorkDoneList(final int parent);

    public List<Person> GetCompletePersonList();

    public List<Person> GetProductPersonList(final int name);

    public List<Person> GetPersonListToSprint(int parent);

    public List<Stage> GetStageList(final int name);

    public Float GetValueReestimation(final int id, final Date date);

    public void DeleteProduct(Product p);

    public void DeleteTheme(Theme t);

    public void DeleteStory(Story s);

    public void DeleteRelease(Release r);

    public void DeleteSprint(Sprint s);

    public void DeleteTask(Task t);

    public void DeleteWorkDone(WorkDone w);

    public void UpdateStory(final Story s);

    public void UpdateStage(final Stage s);

    public void UpdateProduct(final Product p);

    public void UpdateRelease(final Release r);

    public void UpdateTheme(final Theme t);

    public void UpdateStageSequence(final StageSequence sq);

    public void UpdateSprint(final Sprint s);

    public void UpdateTask(final Task t);

    public boolean UpdatePerson(final Person p);

    public void UpdateWorkDone(final WorkDone w);

    public void UpdateReestimation(final Reestimation w);

    public boolean WorkPersonInProduct(int idProduct, int idPerson);

    public boolean UnassignPersonInProduct(int idProduct, int idPerson);

    public boolean AssignedPersonToSprint(int idSprint, int idPerson);

    public boolean UnAssignedPersonToSprint(int idSprint, int idPerson);

    public void DeletePerson(Person p);

    public List<Sprint> getSprintsAssigned(int id);

    public List<Reestimation> GetValueReestimation(int id);

    public Product getProduct(int id);

    public Theme getTheme(int id);

    public Story getStory(int id);

    public Task getTask(int id);

    public Sprint getSprint(int id);

    public Release getRelease(int id);

    public Person getPerson(int id);

    public Reestimation getReestimation(int id);

    public WorkDone getWorkDone(int id);

    public Float GetLastEstimation(int id);
}
