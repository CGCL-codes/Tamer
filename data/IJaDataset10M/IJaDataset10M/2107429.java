package se.jayway.millionaire.dao;

import java.io.File;
import se.jayway.millionaire.dao.impl.SolutionDAOImpl;
import se.jayway.millionaire.engine.Configuration;

public abstract class SolutionDAO {

    private static SolutionDAOImpl instance;

    public static SolutionDAO getInstance() {
        if (instance == null) {
            String path = Configuration.getInstance().getDatabaseFileName();
            File file = new File(path);
            if (!file.exists()) {
                throw new IllegalArgumentException("File " + path + " does not exist");
            }
            if (!file.isFile()) {
                throw new IllegalArgumentException("File " + path + " is not a file");
            }
            instance = new SolutionDAOImpl(file);
            instance.populate();
        }
        return instance;
    }

    public abstract Solution nextSolution();
}
