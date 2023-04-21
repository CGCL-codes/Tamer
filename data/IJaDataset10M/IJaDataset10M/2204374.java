package nl.gridshore.samples.bundles.trainingservice.impl;

import nl.gridshore.samples.bundles.trainingservice.api.TrainingService;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: jettro
 * Date: Feb 9, 2008
 * Time: 10:16:33 PM
 * Implementation for training service
 */
public class TrainingServiceImpl implements TrainingService {

    public List<String> obtainTrainings() {
        List<String> trainings = new ArrayList<String>();
        trainings.add("Spring Core");
        trainings.add("OSGi Core");
        return trainings;
    }
}
