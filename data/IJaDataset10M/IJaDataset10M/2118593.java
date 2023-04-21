package org.blueoxygen.komodo.type.actions;

import java.sql.Timestamp;
import org.blueoxygen.cimande.LogInformation;
import org.blueoxygen.cimande.security.SessionCredentials;
import org.blueoxygen.cimande.security.SessionCredentialsAware;
import org.blueoxygen.komodo.Type;

/**
 * @author harry
 * email :  harry@intercitra.com
 */
public class AddType extends TypeForm implements SessionCredentialsAware {

    private SessionCredentials sess;

    private String id;

    public String execute() {
        if (getTypeCategory().equalsIgnoreCase("")) {
            addActionError("Please fill the category type");
            return INPUT;
        }
        type = new Type();
        type.setTypeCategory(getTypeCategory());
        type.setTypeFunction(getTypeFunction());
        type.setTypeGenre(getTypeGenre());
        type.setTypeAggregationContent(getTypeAggregationContent());
        logInfo = new LogInformation();
        logInfo.setCreateBy(sess.getCurrentUser().getId());
        logInfo.setCreateDate(new Timestamp(System.currentTimeMillis()));
        logInfo.setActiveFlag(getActiveFlag());
        type.setLogInformation(logInfo);
        pm.save(type);
        id = type.getId();
        return SUCCESS;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSessionCredentials(SessionCredentials sessionCredentials) {
        this.sess = sessionCredentials;
    }
}
