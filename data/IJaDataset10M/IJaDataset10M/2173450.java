package prefwork.method;

import java.util.List;
import org.apache.commons.configuration.XMLConfiguration;
import prefwork.datasource.BasicDataSource;

public interface InductiveMethod {

    /**
	 * Builds the user model for a user and a training set.
	 * @param trainingDataset
	 * @param user
	 * @return Number of acquired objects.
	 */
    public int buildModel(BasicDataSource trainingDataset, Integer user);

    public Double classifyRecord(List<Object> record, Integer targetAttribute);

    public void configClassifier(XMLConfiguration config, String section);

    public String toString();
}
