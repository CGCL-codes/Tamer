package org.june.stat;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Fabien DUMINY
 */
public class ConfigResults {

    /**
	 * 
	 */
    public ConfigResults(Object config) {
        this.config = config;
    }

    /**
	 * 
	 * @param result
	 */
    public void addResult(FixtureResult result) {
        nbRun++;
        if (result.isSucceed()) nbSucceed++; else nbFailed++;
        results.add(result);
    }

    /**
	 * 
	 * @return
	 */
    public FixtureResult[] getResults() {
        return (FixtureResult[]) results.toArray(new FixtureResult[results.size()]);
    }

    /**
	 * 
	 * @return
	 */
    public Object getConfig() {
        return config;
    }

    /**
	 * 
	 * @return
	 */
    public int getNbFailed() {
        return nbFailed;
    }

    /**
	 * 
	 * @return
	 */
    public int getNbSucceed() {
        return nbSucceed;
    }

    /**
	 * 
	 * @return
	 */
    public int getNbRun() {
        return nbRun;
    }

    private Object config;

    private List results = new ArrayList();

    private int nbFailed = 0;

    private int nbRun = 0;

    private int nbSucceed = 0;
}
