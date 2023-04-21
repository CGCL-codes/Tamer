package uk.ac.city.soi.everestplus.impl;

import java.util.ArrayList;
import java.util.Hashtable;
import org.apache.log4j.Logger;
import org.slaatsoi.prediction.schema.ComparisonOperatorType;
import org.slaatsoi.prediction.schema.PredictionPolicyType;
import org.slaatsoi.prediction.schema.ThresholdType;
import org.slasoi.slamodel.sla.SLA;
import org.xml.sax.SAXException;
import uk.ac.city.soi.database.EntityManagerCommons;
import uk.ac.city.soi.everestplus.communication.PredictionResultPublisher;
import uk.ac.city.soi.everestplus.core.FrameworkContext.DatabaseManagers;
import uk.ac.city.soi.everestplus.core.FrameworkContext.PropertiesFileNames;
import uk.ac.city.soi.everestplus.core.FrameworkContextInitialisationException;
import uk.ac.city.soi.everestplus.core.FrameworkContextManager;
import uk.ac.city.soi.everestplus.core.Prediction;
import uk.ac.city.soi.everestplus.database.DistributionEntityManager;
import uk.ac.city.soi.everestplus.database.PredictionEntity;
import uk.ac.city.soi.everestplus.database.PredictionEntityManager;
import uk.ac.city.soi.everestplus.database.PredictionPolicyEntityManager;
import uk.ac.city.soi.everestplus.extension.umontreal.UMontrealDistributionCalculator;
import uk.ac.city.soi.everestplus.extension.umontreal.UMontrealDistributionFactory;
import uk.ac.city.soi.everestplus.model.ModelCalculator;
import uk.ac.city.soi.everestplus.parser.PredictionPolicyParser;
import uk.ac.city.soi.everestplus.predictor.Predictor;
import uk.ac.city.soi.everestplus.predictor.PredictorFactory;
import uk.ac.city.soi.everestplus.service.EverestPlusService;

/**
 * @author Davide Lorenzoli
 * 
 * @date Jun 1, 2011
 */
public class EverestPlusImpl implements EverestPlusService {

    private static Logger logger = Logger.getLogger(EverestPlusImpl.class);

    private PredictionPolicyEntityManager predictionPolicyEM;

    private PredictionEntityManager predictionEM;

    private DistributionEntityManager distributionEM;

    private PredictionResultPublisher predictionResultPublisher;

    private ArrayList<ModelCalculator> modelCalculators;

    private Hashtable<String, Predictor> predictors;

    /**
	 * Initialised the prediction framework reading the default configuration
	 * properties file everestplus.database.properties from /conf folder
	 */
    public EverestPlusImpl() {
        logger.info("EverestPlus is starting components initilisation.");
        if (!initComponents()) {
            logger.error("EverestPlus failed during initialisation phase. Exiting.");
            return;
        }
        if (!verifyDatabase()) {
            logger.error("EverestPlus failed initialising persistence sub-system. Please verify properties files. Exiting.");
            return;
        }
        logger.debug("Subscribing to the XMPP channel");
        predictionResultPublisher = new PredictionResultPublisher();
        predictionResultPublisher.subscribe();
        logger.debug("Populating distributions table with SSJ library distributions.");
        populateDistributionsDatabaseTable();
        logger.info("EverestPlus successfully initialised.");
    }

    /**
	 * @see uk.ac.city.soi.everestplus.service.EverestPlusService#getPrediction(org.slasoi.slamodel.sla.SLA)
	 */
    public ArrayList<Prediction> getPrediction(SLA sla) {
        ArrayList<Prediction> predictions = new ArrayList<Prediction>();
        try {
            PredictionPolicyType[] predictionPolicies = PredictionPolicyParser.parseSLA(sla, null);
            for (PredictionPolicyType predictionPolicy : predictionPolicies) {
                predictions.add(getPrediction(predictionPolicy));
            }
        } catch (SAXException e) {
            logger.error(e.getMessage(), e);
        }
        return predictions;
    }

    /**
	 * @see uk.ac.city.soi.everestplus.service.EverestPlusService#getPrediction(org.slaatsoi.prediction.schema.PredictionPolicyType)
	 */
    public Prediction getPrediction(PredictionPolicyType predictionPolicy) {
        return getPrediction(predictionPolicy, System.currentTimeMillis());
    }

    /**
	 * This method is used for testing purpose only. It allows to pass to the
	 * prediction framework the time moment in which a prediction request is
	 * made.
	 * 
	 * @param predictionPolicy
	 * @param timestamp
	 * @return
	 */
    public Prediction getPrediction(PredictionPolicyType predictionPolicy, long timestamp) {
        if (predictionPolicy == null) return null;
        predictionPolicyEM.insert(predictionPolicy);
        for (ModelCalculator modelCalculator : modelCalculators) {
            modelCalculator.updateModels(predictionPolicy);
        }
        Predictor predictor = predictors.get(predictionPolicy.getPredictor().getPredictorId());
        if (predictor == null) {
            predictor = PredictorFactory.getPredictor(predictionPolicy.getPredictor().getPredictorId());
            if (!predictors.containsKey(predictor.getClass().getName())) {
                predictors.put(predictor.getClass().getName(), predictor);
            }
        }
        Prediction prediction = predictor.getPrediction(predictionPolicy, timestamp);
        predictionEM.insert(new PredictionEntity(prediction));
        publishPrediction(prediction);
        return prediction;
    }

    /**
	 * Publish prediction to the communication infrastructure
	 * @param prediction
	 */
    private void publishPrediction(Prediction prediction) {
        boolean isToBePublished = false;
        for (ThresholdType notificationThreshold : prediction.getPredictionSpecification().getPredictionSettings().getNotificationThresholds().getNotificationThreshold()) {
            if (notificationThreshold.getOperator().value().equals(ComparisonOperatorType.LESS_EQUAL_THAN.value())) {
                if (prediction.getValue() <= notificationThreshold.getValue()) {
                    isToBePublished = true;
                }
            } else if (notificationThreshold.getOperator().value().equals(ComparisonOperatorType.LESS_THAN.value())) {
                if (prediction.getValue() < notificationThreshold.getValue()) {
                    isToBePublished = true;
                }
            } else if (notificationThreshold.getOperator().value().equals(ComparisonOperatorType.GREATER_EQUAL_THAN.value())) {
                if (prediction.getValue() >= notificationThreshold.getValue()) {
                    isToBePublished = true;
                }
            } else if (notificationThreshold.getOperator().value().equals(ComparisonOperatorType.GREATER_THAN.value())) {
                if (prediction.getValue() > notificationThreshold.getValue()) {
                    isToBePublished = true;
                }
            }
            if (isToBePublished) {
                boolean result = predictionResultPublisher.publish(prediction);
                logger.info("Prediction " + prediction.getPredictionSpecification().getPredictionPolicyId() + " published to the channel: " + result);
                break;
            }
        }
    }

    /**
	 * Initialises framework components
	 */
    private boolean initComponents() {
        modelCalculators = new ArrayList<ModelCalculator>();
        predictors = new Hashtable<String, Predictor>();
        try {
            FrameworkContextManager.initialiseFrameworkContext();
        } catch (FrameworkContextInitialisationException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
        predictionPolicyEM = (PredictionPolicyEntityManager) FrameworkContextManager.getFrameworkContext().getDatabaseManager(DatabaseManagers.PREDICTION_POLICY);
        predictionEM = (PredictionEntityManager) FrameworkContextManager.getFrameworkContext().getDatabaseManager(DatabaseManagers.PREDICTIONS);
        distributionEM = (DistributionEntityManager) FrameworkContextManager.getFrameworkContext().getDatabaseManager(DatabaseManagers.DISTRIBUTIONS);
        modelCalculators.add(new UMontrealDistributionCalculator(UMontrealDistributionCalculator.class.getName()));
        return true;
    }

    /**
	 * Populate distribution database table with available distributions
	 */
    private void populateDistributionsDatabaseTable() {
        for (Class distributionClass : UMontrealDistributionFactory.getSupportedDistributions()) {
            String className = distributionClass.getName();
            if (distributionEM.selectDistributionByClassName(className) == null) {
                distributionEM.insert(className, className.substring(className.lastIndexOf(".") + 1, className.lastIndexOf("Dist")), "University of Montreal distribution implementation", className);
            }
        }
    }

    /**
	 * Checks whether both the database and its tables exist
	 */
    private boolean verifyDatabase() {
        String schemaName = FrameworkContextManager.getFrameworkContext().getFrameworkProperties(PropertiesFileNames.MYSQL).getProperty("database.schema");
        String tableName = null;
        for (DatabaseManagers databaseManager : DatabaseManagers.values()) {
            EntityManagerCommons entityManagerCommons = (EntityManagerCommons) FrameworkContextManager.getFrameworkContext().getDatabaseManager(databaseManager);
            tableName = entityManagerCommons.getDatabaseTable();
            logger.debug("Checking database tabe: " + schemaName + "." + tableName);
            if (!entityManagerCommons.existsTable(schemaName, tableName)) {
                return false;
            }
        }
        return true;
    }
}
