package org.blueoxygen.lotion.correspondence.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.blueoxygen.lotion.Opportunity;

public class DeleteCorrespondence extends CorrespondenceForm {

    private List opportunities = new ArrayList();

    private Opportunity opportunity;

    public String execute() {
        String result = super.execute();
        String query = " FROM opportunity in " + Opportunity.class + " WHERE opportunity.program.id='" + correspondence.getId() + "'";
        opportunities = pm.find(query, null, null);
        if (!opportunities.isEmpty()) {
            Iterator i = opportunities.iterator();
            while (i.hasNext()) {
                opportunity = (Opportunity) i.next();
                opportunity.setCorrespondence(null);
            }
        }
        pm.remove(correspondence);
        return SUCCESS;
    }
}
