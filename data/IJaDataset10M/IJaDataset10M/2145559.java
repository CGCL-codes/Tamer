package cf.e_commerce.evaluator.newEval.log;

import java.io.FileWriter;
import java.io.IOException;
import org.apache.mahout.cf.taste.model.Preference;
import cf.e_commerce.evaluator.newEval.factory.ClusterSimilarityFactory;
import cf.e_commerce.evaluator.newEval.factory.ItemSimilarityFactory;
import cf.e_commerce.evaluator.newEval.factory.KNNOptimizerFactory;
import cf.e_commerce.evaluator.newEval.factory.UserNeighborhoodFactory;
import cf.e_commerce.evaluator.newEval.factory.UserSimilarityFactory;
import com.uplexis.idealize.base.utils.IdealizeDateTime;

public final class LogWriter {

    /**
	 * Logs the precision and recall
	 * 
	 * @param precision
	 *            double with precision
	 * @param recall
	 *            double with recall
	 * @param fMeasure
	 *            double with fMeasure
	 * @param at
	 *            int with values got at this value
	 * @param writer
	 *            FileWriter with output stream
	 * @throws IOException
	 *             thrown when problems arise during file system access
	 */
    public final void logPrecisionRecall(double precision, double recall, double fMeasure, int at, FileWriter writer) throws IOException {
        StringBuilder builder = new StringBuilder("At:");
        builder.append(2);
        builder.append("\nPrecision:");
        builder.append(precision);
        builder.append("\nRecall:");
        builder.append(recall);
        builder.append("\nfMeasure:");
        builder.append(fMeasure);
        builder.append("\n");
        writer.write(builder.toString());
        writer.flush();
    }

    /**
	 * Logs the similarity being used
	 * 
	 * @param simIndex
	 *            int with similarity index
	 * @param writer
	 *            FileWriter with log output stream
	 * @throws IOException
	 *             thrown if data cannot be used
	 */
    public final void logUserSimilarity(int simIndex, FileWriter writer) throws IOException {
        StringBuilder builder = new StringBuilder(UserSimilarityFactory.getSimilarityName(simIndex));
        builder.append(";");
        writer.write(builder.toString());
        writer.flush();
    }

    /**
	 * Logs the item similarity being used
	 * 
	 * @param simIndex
	 *            int with similarity index=
	 * @param writer
	 *            FileWriter with log output stream
	 * @throws IOException
	 *             thrown if data cannot be used
	 */
    public final void logItemSimilarity(int simIndex, FileWriter writer) throws IOException {
        StringBuilder builder = new StringBuilder(ItemSimilarityFactory.getSimilarityName(simIndex));
        builder.append(";");
        writer.write(builder.toString());
        writer.flush();
    }

    /**
	 * Logs the similarity
	 * 
	 * @param simIndex
	 *            int with similarity index=
	 * @param writer
	 *            FileWriter with log output stream
	 * @throws IOException
	 *             thrown if data cannot be used
	 */
    public final void logClusterSimilarity(int simIndex, FileWriter writer) throws IOException {
        StringBuilder builder = new StringBuilder(ClusterSimilarityFactory.getSimilarityName(simIndex));
        builder.append(";");
        writer.write(builder.toString());
        writer.flush();
    }

    /**
	 * Logs the neighborhood being used
	 * 
	 * @param neigIndex
	 *            int with index
	 * @param writer
	 *            FileWriter with output stream
	 * @throws IOException
	 */
    public final void logNeighborhood(int neigIndex, FileWriter writer) throws IOException {
        StringBuilder builder = new StringBuilder(UserNeighborhoodFactory.getNeighborhoodName(neigIndex));
        builder.append(";");
        writer.write(builder.toString());
        writer.flush();
    }

    /**
	 * Logs the neighborhood being used
	 * 
	 * @param neigIndex
	 *            int with index
	 * @param writer
	 *            FileWriter with output stream
	 * @throws IOException
	 */
    public final void logOptimizer(int optIndex, FileWriter writer) throws IOException {
        StringBuilder builder = new StringBuilder(KNNOptimizerFactory.getOptimizerName(optIndex));
        builder.append(";");
        writer.write(builder.toString());
        writer.flush();
    }

    /**
	 * Logs the number of features being used by machine learning methods
	 * 
	 * @param features
	 *            int with number of features
	 * @param writer
	 *            FileWriter with output stream
	 * @throws IOException
	 *             thrown when problems occur with access to file system
	 */
    public final void logFeaturesCount(int features, FileWriter writer) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append(features);
        builder.append(";");
        writer.write(builder.toString());
        writer.flush();
    }

    /**
	 * Logs the number of training steps being used by machine learning methods
	 * 
	 * @param steps
	 *            int with number of steps
	 * @param writer
	 *            FileWriter with output stream
	 * @throws IOException
	 *             thrown when problems occur with access to file system
	 */
    public final void logStepsCount(int steps, FileWriter writer) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append(steps);
        builder.append(";");
        writer.write(builder.toString());
        writer.flush();
    }

    /**
	 * Writes the log for a scorer
	 * 
	 * @param train
	 *            float with amount of base to be used to train
	 * @param score
	 *            double with resulting score
	 * @param writer
	 *            FileWriter with stream output
	 * @throws IOException
	 */
    public final void writeScoreLog(float train, double score, FileWriter writer) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append(train);
        builder.append(";");
        builder.append(score);
        builder.append(";");
        writer.write(builder.toString());
        writer.flush();
    }

    /**
	 * Write an error log
	 * 
	 * @param message
	 *            String with message
	 * @param writer
	 *            FileWriter with stream output to log file
	 * @throws IOException
	 */
    public final void writeErrorLog(String message, FileWriter writer) throws IOException {
        writer.write("PROBLEM:" + message + "\n");
        writer.flush();
    }

    /**
	 * Logs the amount of memory being used up to the moment
	 * 
	 * @param writer
	 *            FileWriter with output stream
	 * @throws IOException
	 *             thrown when problems occur during writing
	 */
    public final void writeMemoryUsageLog(FileWriter writer) throws IOException {
        long totalMemory = Runtime.getRuntime().totalMemory();
        long memory = totalMemory - Runtime.getRuntime().freeMemory();
        StringBuilder builder = new StringBuilder();
        builder.append(memory / 1000000L);
        builder.append("/");
        builder.append(totalMemory / 1000000L);
        builder.append(" MB\n");
        writer.write(builder.toString());
        writer.flush();
    }

    /**
	 * Logs the current time
	 * 
	 * @param writer
	 * @throws IOException
	 */
    public final void logTime(FileWriter writer) throws IOException {
        writer.write(IdealizeDateTime.getNow() + "\n");
        writer.flush();
    }

    /**
	 * Writes a generic log
	 * 
	 * @param key
	 *            String with key to log entry
	 * @param value
	 *            String with value to log entry
	 * @param writer
	 *            FileWriter with output stream
	 * @throws IOException
	 *             thrown when problems occur during file writing
	 */
    public final void logGenericData(String key, String value, FileWriter writer) throws IOException {
        String line = String.format("%s:%s\n", key, value);
        writer.write(line);
        writer.flush();
    }

    /**
	 * Logs a free user-defined text
	 * 
	 * @param message
	 *            String with message to be logged
	 * @param writer
	 *            FileWriter with output stream
	 * @throws IOException
	 *             thrown when problems occur during file writing
	 */
    public final void logFreeMessage(String message, FileWriter writer) throws IOException {
        writer.write(message);
        writer.flush();
    }

    /**
	 * Logs the estimated preference for a given user to a given item
	 * 
	 * @param estimatedPreference
	 *            float with estimated preference
	 * @param realPref
	 *            real rating
	 * @param writer FileWriter output stream to file
	 * @throws IOException
	 *             thrown when problems occur due to file system access problems
	 */
    public final void logEstimate(float estimatedPreference, Preference realPref, FileWriter writer) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append(realPref.getUserID());
        builder.append(";");
        builder.append(realPref.getItemID());
        builder.append(";");
        builder.append(realPref.getValue());
        builder.append(";");
        builder.append(estimatedPreference);
        builder.append(";");
        builder.append(Math.abs(realPref.getValue() - estimatedPreference));
        builder.append("\n");
        writer.write(builder.toString());
        writer.flush();
    }
}
