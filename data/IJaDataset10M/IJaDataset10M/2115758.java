package net.solarnetwork.central.instructor.domain;

import java.util.ArrayList;
import java.util.List;
import net.solarnetwork.central.domain.BaseEntity;
import org.joda.time.DateTime;

/**
 * Domain object for an individual instruction.
 * 
 * @author matt
 * @version $Revision: 1810 $
 */
public class Instruction extends BaseEntity {

    private static final long serialVersionUID = 4799093764907658857L;

    private String topic;

    private DateTime instructionDate;

    private InstructionState state = InstructionState.Unknown;

    private List<InstructionParameter> parameters;

    /**
	 * Default constructor.
	 */
    public Instruction() {
        super();
    }

    /**
	 * Construct with data.
	 * 
	 * @param topic the topic
	 * @param instructionDate the instruction date
	 */
    public Instruction(String topic, DateTime instructionDate) {
        super();
        this.topic = topic;
        this.instructionDate = instructionDate;
    }

    /**
	 * Remove all parameters.
	 */
    public void clearParameters() {
        parameters.clear();
    }

    /**
	 * Add a parameter value.
	 * 
	 * @param key the key
	 * @param value the value
	 */
    public void addParameter(String key, String value) {
        if (parameters == null) {
            parameters = new ArrayList<InstructionParameter>(5);
        }
        parameters.add(new InstructionParameter(key, value));
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public DateTime getInstructionDate() {
        return instructionDate;
    }

    public void setInstructionDate(DateTime instructionDate) {
        this.instructionDate = instructionDate;
    }

    public InstructionState getState() {
        return state;
    }

    public void setState(InstructionState state) {
        this.state = state;
    }

    public List<InstructionParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<InstructionParameter> parameters) {
        this.parameters = parameters;
    }
}
