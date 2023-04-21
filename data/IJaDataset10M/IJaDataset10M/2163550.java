package org.ietr.preesm.core.scenario;

/**
 * A timing links a SDF vertex and an operator definition to a time. Ids are
 * used to make the scenario independent from model implementations.
 * 
 * @author mpelcat
 */
public class Timing {

    public static final Timing UNAVAILABLE = null;

    public static final int DEFAULT_TASK_TIME = 100;

    public static final int DEFAULT_BROADCAST_TIME = 10;

    public static final int DEFAULT_FORK_TIME = 10;

    public static final int DEFAULT_JOIN_TIME = 10;

    public static final int DEFAULT_INIT_TIME = 10;

    /**
	 * related operator
	 */
    private String operatorDefinitionId;

    /**
	 * Definition of the timing
	 */
    private int time;

    /**
	 * related Graph
	 */
    private String sdfVertexId;

    public Timing(String operatorDefinitionId, String sdfVertexId) {
        time = DEFAULT_TASK_TIME;
        this.operatorDefinitionId = operatorDefinitionId;
        this.sdfVertexId = sdfVertexId;
    }

    public Timing(String operatorId, String sdfVertexId, int time) {
        this(operatorId, sdfVertexId);
        this.time = time;
    }

    @Override
    public boolean equals(Object obj) {
        boolean equals = false;
        if (obj instanceof Timing) {
            Timing otherT = (Timing) obj;
            equals = operatorDefinitionId.equals(otherT.getOperatorDefinitionId());
            equals &= sdfVertexId.equals((otherT.getSdfVertexId()));
        }
        return equals;
    }

    public String getOperatorDefinitionId() {
        return operatorDefinitionId;
    }

    public int getTime() {
        return time;
    }

    public String getSdfVertexId() {
        return sdfVertexId;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "{" + sdfVertexId + "," + operatorDefinitionId + "," + time + "}";
    }
}
