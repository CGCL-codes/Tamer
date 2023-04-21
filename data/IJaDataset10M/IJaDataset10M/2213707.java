package pl.voidsystems.yajf.dao;

@SuppressWarnings("serial")
public class DbEntityError extends DbException {

    public DbEntityError(String message) {
        super(message);
    }
}
