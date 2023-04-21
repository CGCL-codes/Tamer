package gov.sns.apps.jeri.data;

/**
 * Provides a class to hold a record from the SCORE_SNAPSHOT_SGNL table.
 * 
 * @author Chris Fowlkes
 */
public class ScoreSnapshot {

    /**
   * Holds the <CODE>ScoreSnapshotGroup</CODE> to which the 
   * <CODE>ScoreSnapshot</CODE> belongs.
   * @attribute 
   */
    private ScoreSnapshotGroup scoreSnapshotGroup;

    /**
   * Holds the value of the set point signal for the snapshot.
   * @attribute 
   */
    private Signal setPointSignal;

    /**
   * Holds the value of the read back signal for the snapshot.
   * @attribute 
   */
    private Signal readBackSignal;

    /**
   * Holds the value of the set point signal value for the snapshot.
   */
    private String setPointSignalValue;

    /**
   * Holds the value of the read back signal value for the snapshot.
   */
    private String readBackSignalValue;

    /**
   * Creates a new <CODE>ScoreSnapshot</CODE>.
   */
    public ScoreSnapshot() {
    }

    /**
   * Gets the <CODE>ScoreSnapshotGroup</CODE> with which the 
   * <CODE>ScoreSnapshot</CODE> is associated.
   * 
   * @return The <CODE>ScoreSnapshotGroup</CODE> to which the <CODE>ScoreSnapshot</CODE> belongs.
   */
    public ScoreSnapshotGroup getScoreSnapshotGroup() {
        return scoreSnapshotGroup;
    }

    /**
   * Sets the <CODE>ScoreSnapshotGroup</CODE> with which the 
   * <CODE>ScoreSnapshot</CODE> is associated.
   * 
   * @param scoreSnapshotGroup The <CODE>ScoreSnapshotGroup</CODE> to which the <CODE>ScoreSnapshot</CODE> belongs.
   */
    public void setScoreSnapshotGroup(ScoreSnapshotGroup scoreSnapshotGroup) {
        this.scoreSnapshotGroup = scoreSnapshotGroup;
        if (!scoreSnapshotGroup.containsScoreSnapshot(this)) scoreSnapshotGroup.addScoreSnapshot(this);
    }

    /**
   * Gets the set point signal for the snapshot.
   * 
   * @return The set point signal for the snapshot.
   */
    public Signal getSetPointSignal() {
        return setPointSignal;
    }

    /**
   * Sets the set point signal for the snapshot.
   * 
   * @param setPointSignal The set point signal for the snapshot.
   */
    public void setSetPointSignal(Signal setPointSignal) {
        this.setPointSignal = setPointSignal;
    }

    /**
   * Gets the read back signal for the snapshot.
   * 
   * @return The read back signal for the snapshot.
   */
    public Signal getReadBackSignal() {
        return readBackSignal;
    }

    /**
   * Sets the read back signal for the snapshot.
   * 
   * @param readBackSignal The read back signal for the snapshot.
   */
    public void setReadBackSignal(Signal readBackSignal) {
        this.readBackSignal = readBackSignal;
    }

    /**
   * Gets the set point signal value for the snapshot.
   * 
   * @return The set point signal value for the snapshot.
   */
    public String getSetPointSignalValue() {
        return setPointSignalValue;
    }

    /**
   * Sets the set point signal value for the snapshot.
   * 
   * @param setPointSignalValue The set point signal value for the snapshot.
   */
    public void setSetPointSignalValue(String setPointSignalValue) {
        this.setPointSignalValue = setPointSignalValue;
    }

    /**
   * Gets the read back signal value for the snapshot.
   * 
   * @return The read back signal value for the snapshot.
   */
    public String getReadBackSignalValue() {
        return readBackSignalValue;
    }

    /**
   * Sets the read back signal value for the snapshot.
   * 
   * @param readBackSignalValue The read back signal value for the snapshot.
   */
    public void setReadBackSignalValue(String readBackSignalValue) {
        this.readBackSignalValue = readBackSignalValue;
    }
}
