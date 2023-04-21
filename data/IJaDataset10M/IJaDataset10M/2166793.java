package songgao.papers.iEC;

import java.util.ArrayList;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Population {

    public static Population generatePopulation() {
        Population pop = new Population();
        pop.generation = 0;
        pop.currentSlaveKey = null;
        pop.predecessorKey = null;
        pop.waitingForMigration = false;
        pop.individualKeys = new ArrayList<String>();
        for (int i = 0; i < DataManager.INSTANCE.getParameters().getPopulationSize(); i++) {
            pop.individualKeys.add(DataManager.INSTANCE.newIndividual(Individual.generateIndividual()));
        }
        return pop;
    }

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String encodedKey;

    @Persistent(defaultFetchGroup = "true")
    private Boolean waitingForMigration;

    @Persistent(defaultFetchGroup = "true")
    private String predecessorKey;

    @Persistent(defaultFetchGroup = "true")
    private ArrayList<String> individualKeys;

    @Persistent(defaultFetchGroup = "true")
    private int generation;

    @Persistent(defaultFetchGroup = "true")
    private String currentSlaveKey;

    /**
	 * @param encodedKey
	 *            the encodedKey to set
	 */
    public void setEncodedKey(String encodedKey) {
        this.encodedKey = encodedKey;
    }

    /**
	 * @return the encodedKey
	 */
    public String getEncodedKey() {
        return encodedKey;
    }

    /**
	 * @param generation
	 *            the generation to set
	 */
    public void setGeneration(int generation) {
        this.generation = generation;
    }

    /**
	 * @return the generation
	 */
    public int getGeneration() {
        return generation;
    }

    /**
	 * @param currentSlave
	 *            the currentSlave to set
	 */
    public void setCurrentSlaveKey(String currentSlaveKey) {
        this.currentSlaveKey = currentSlaveKey;
    }

    /**
	 * @return the currentSlave
	 */
    public String getCurrentSlaveKey() {
        return currentSlaveKey;
    }

    /**
	 * @param waitingForMigration
	 *            the waitingForMigration to set
	 */
    public void setWaitingForMigration(Boolean waitingForMigration) {
        this.waitingForMigration = waitingForMigration;
    }

    /**
	 * @return the waitingForMigration
	 */
    public Boolean getWaitingForMigration() {
        return waitingForMigration;
    }

    /**
	 * @param individualKeys
	 *            the individualKeys to set
	 */
    public void setIndividualKeys(ArrayList<String> individualKeys) {
        this.individualKeys = individualKeys;
    }

    /**
	 * @return the individualKeys
	 */
    public ArrayList<String> getIndividualKeys() {
        return individualKeys;
    }

    /**
	 * @param predecessorKey the predecessorKey to set
	 */
    public void setPredecessorKey(String predecessorKey) {
        this.predecessorKey = predecessorKey;
    }

    /**
	 * @return the predecessorKey
	 */
    public String getPredecessorKey() {
        return predecessorKey;
    }
}
