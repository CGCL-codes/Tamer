package piramide.interaction.reasoner.db;

public class WrappedDatabaseManager extends DatabaseManager {

    private static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/PiramideTrendsFullTest";

    public WrappedDatabaseManager() throws Exception {
        super();
    }

    @Override
    protected String getConnectionUrl() {
        return CONNECTION_URL;
    }
}
