package org.hip.vif.admin.member.internal;

import org.hip.vif.admin.member.tasks.LookupSearchTask;
import org.hip.vif.core.util.LinkButtonHelper.LookupType;
import org.hip.vif.web.interfaces.ILookupWindow;
import org.hip.vif.web.util.UseCaseHelper;

/**
 * Service provider component for the <code>IContentLookup</code> interface.<br />
 * This component provides the view for the member search lookup.
 * 
 * @author Luthiger
 * Created: 14.11.2011
 */
public class LookupSearchComponent implements ILookupWindow {

    public int getWidth() {
        return 690;
    }

    public int getHeight() {
        return 598;
    }

    public String getTaskName() {
        return UseCaseHelper.createFullyQualifiedTaskName(LookupSearchTask.class);
    }

    public boolean isForum() {
        return false;
    }

    public LookupType getType() {
        return LookupType.MEMBER_SEARCH;
    }
}
