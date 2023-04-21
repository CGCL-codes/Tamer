package banking.db;

public interface UserDatabase {

    String getSubjectId(String login, String password) throws DatabaseException;
}
