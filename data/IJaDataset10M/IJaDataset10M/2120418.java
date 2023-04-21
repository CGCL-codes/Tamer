package physis.core.triggers;

import physis.core.PHYSIS;
import physis.core.DigitalOrganism;
import physis.core.Configuration;
import physis.log.Log;
import java.io.*;
import java.util.StringTokenizer;

public class OrganismExtractTrigger extends DataExtractTrigger {

    private static final String MAX_FITNESS = "max_fitness";

    private static final String MAX_MERIT = "max_merit";

    private static final String MAX_AGE = "max_age";

    private String type;

    private String dir;

    OrganismExtractTrigger() {
        population = PHYSIS.getInstance().getPopulation();
    }

    public void setParameters(StringTokenizer params) {
        type = params.nextToken();
        dir = Configuration.getPhysisHome() + System.getProperties().getProperty("file.separator") + params.nextToken();
    }

    public void execute() {
        try {
            String file = "";
            DigitalOrganism dorg = null;
            if (MAX_FITNESS.equals(type)) {
                file += MAX_FITNESS;
                dorg = population.getMaxFitnessOrganism();
            } else if (MAX_MERIT.equals(type)) {
                file += MAX_MERIT;
                dorg = population.getMaxMeritOrganism();
            } else if (MAX_AGE.equals(type)) {
                file += MAX_AGE;
                dorg = population.getMaxAgeOrganism();
            }
            FileWriter fw = new FileWriter(dir + System.getProperties().getProperty("file.separator") + PHYSIS.getInstance().getUpdateCount() + "_" + file);
            fw.write(dorg.toString());
            fw.close();
        } catch (IOException ioe) {
            Log.error(ioe);
        }
        if (is_periodical) {
            adjustPeriodicalSchedule();
        }
    }

    public String toString() {
        return "Extract " + type + " organism to directory " + dir + ".";
    }
}
