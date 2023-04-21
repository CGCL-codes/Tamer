package ca.sqlpower.architect.ddl.critic.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ca.sqlpower.architect.ddl.critic.CriticAndSettings;
import ca.sqlpower.architect.ddl.critic.Criticism;
import ca.sqlpower.sqlobject.SQLRelationship;
import ca.sqlpower.sqlobject.SQLRelationship.UpdateDeleteRule;

public class SQLServer2000UnsupportedFeaturesCritic extends CriticAndSettings {

    public SQLServer2000UnsupportedFeaturesCritic() {
        super(StarterPlatformTypes.SQL_SERVER_2000.getName(), Messages.getString("UnsupportedFeaturesCritic.name", StarterPlatformTypes.SQL_SERVER_2000.getName()));
    }

    public List<Criticism> criticize(Object subject) {
        if (!(subject instanceof SQLRelationship)) return Collections.emptyList();
        List<Criticism> criticisms = new ArrayList<Criticism>();
        SQLRelationship r = (SQLRelationship) subject;
        if ((r.getDeleteRule() != UpdateDeleteRule.CASCADE) && (r.getDeleteRule() != UpdateDeleteRule.NO_ACTION)) {
            criticisms.add(new Criticism(subject, Messages.getString("UnsupportedFeaturesCritic.deleteRuleNotSupported", getPlatformType(), r.getName()), this));
        }
        if ((r.getUpdateRule() != UpdateDeleteRule.CASCADE) && (r.getUpdateRule() != UpdateDeleteRule.NO_ACTION)) {
            criticisms.add(new Criticism(subject, Messages.getString("UnsupportedFeaturesCritic.updateRuleNotSupported", getPlatformType(), r.getName()), this));
        }
        return criticisms;
    }
}
