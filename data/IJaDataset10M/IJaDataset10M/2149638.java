package org.hip.vif.member.tasks;

import java.util.Locale;
import org.hip.vif.exc.VIFWarningException;
import org.hip.vif.member.Activator;
import org.hip.vif.search.NoHitsException;
import org.hip.vif.servlets.VIFContext;
import org.hip.vif.util.MemberQueryStrategy;

/**
 * Task to displays the result of a quick member search query in a lookup window.
 *
 * @author Luthiger
 * Created: 19.01.2008
 */
public class SelectionList1Task extends SelectionListTask {

    @Override
    protected MemberQueryStrategy getQueryStrategy(VIFContext inContext) throws Exception {
        Locale lLocale = inContext.getLocale();
        String lQuery = inContext.getParameterValue("fldQuick");
        if (lQuery.length() < 2) {
            throw new VIFWarningException(Activator.getMessages().getMessage("org.hip.vif.errmsg.search.noInput", lLocale));
        }
        try {
            return new QueryHitsList(lQuery);
        } catch (NoHitsException exc) {
            throw new VIFWarningException(Activator.getMessages().getFormattedMessage(lLocale, "org.hip.vif.errmsg.search.noHits", lQuery));
        }
    }
}
