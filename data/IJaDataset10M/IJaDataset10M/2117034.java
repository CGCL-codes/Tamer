package gatech.mmpm.sensor.constant;

/**
 * This is a ConstantBoolean sensor always returns false (0.0f).
 * 
 * @author Santi Ontanon Villar and David Llanso
 * @organization: Georgia Institute of Technology
 * @date 29-April-2008
 * 
 * @note Updated according to the new gatech.mmpm.Sensor class. 12-November-2009
 * 
 */
public class False extends ConstantBoolean {

    public False() {
        super(0.0f);
    }

    public False(False f) {
        super(0.0f);
    }

    public Object clone() {
        return new False();
    }
}
