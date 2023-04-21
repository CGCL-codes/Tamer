package fitService;

public interface ServerCommunicator {

    boolean login(String url, String username, String password);

    String loadWikiPage(String pageName);

    boolean saveWikiPage(String pageName, String pageContent, String author);

    public String getPassword();

    public void setPassword(String password);

    public String getUser();

    public void setUser(String user);
}
