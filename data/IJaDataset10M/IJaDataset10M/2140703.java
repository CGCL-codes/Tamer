package djudge.acmcontester.server.interfaces;

public interface AdminLanguagesCommonInterface {

    public boolean addLanguage(String username, String password, String sid, String shortName, String fullName, String compilationComand, String djudgeID);

    public boolean editLanguage(String username, String password, String id, String sid, String shortName, String fullName, String compilationComand, String djudgeID);

    public boolean deleteLanguage(String username, String password, String id);

    public boolean deleteAllLanguages(String username, String password);
}
