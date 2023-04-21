package apollo.datamodel;

import org.apache.log4j.*;

public class Evidence implements EvidenceConstants, java.io.Serializable {

    protected static final Logger logger = LogManager.getLogger(Evidence.class);

    int type;

    String featureId;

    String setId;

    String dbType;

    DbXref dbXref;

    public Evidence(String featureId, String setId, int type) {
        if (featureId == null || featureId.equals("")) {
            throw new NullPointerException("Can't accept empty feature ID as evidence");
        }
        setFeatureId(featureId);
        setSetId(setId);
        setType(type);
    }

    public Evidence(String featureId, int type) {
        this(featureId, "", type);
    }

    public Evidence(String featureId) {
        this(featureId, "", SIMILARITY);
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getDbType() {
        return dbType;
    }

    public String getTypeAsString() {
        if (type < typeStrings.length) {
            return typeStrings[type];
        } else {
            logger.error("Unknown evidence type: " + type);
            return "unknown";
        }
    }

    public void setFeatureId(String featureId) {
        this.featureId = new String(featureId);
    }

    public String getFeatureId() {
        return featureId;
    }

    public void setSetId(String setId) {
        this.setId = new String(setId);
    }

    public String getSetId() {
        return setId;
    }

    public DbXref getDbXref() {
        return dbXref;
    }

    public void setDbXref(DbXref newValue) {
        dbXref = newValue;
    }

    public String toString() {
        return "This is evidence for featureId " + featureId;
    }

    /**
   * General implementation of Visitor pattern. (see apollo.util.Visitor).
  **/
    public void accept(apollo.util.Visitor visitor) {
        visitor.visit(this);
    }
}
