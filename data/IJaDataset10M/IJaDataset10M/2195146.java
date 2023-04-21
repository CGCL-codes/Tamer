package er.bugtracker;

import com.webobjects.foundation.*;
import com.webobjects.appserver.*;
import com.webobjects.eocontrol.*;
import com.webobjects.eoaccess.*;
import java.util.*;

public class PushRelease extends WOComponent {

    public PushRelease(WOContext aContext) {
        super(aContext);
    }

    protected EOEnterpriseObject theRelease;

    private NSArray _bugsInBuild = null;

    protected Bug currentBug;

    public NSArray bugsInBuild() {
        if (_bugsInBuild == null) {
            _bugsInBuild = Bug.bugClazz().bugsInBuildWithTargetRelease(session().defaultEditingContext(), null);
        }
        return _bugsInBuild;
    }

    public void setBugsInBuild(NSArray bugs) {
        _bugsInBuild = bugs;
    }

    public WOComponent push() {
        for (Enumeration e = bugsInBuild().objectEnumerator(); e.hasMoreElements(); ) {
            currentBug = (Bug) e.nextElement();
            currentBug.setState(State.VERIFY);
        }
        session().defaultEditingContext().saveChanges();
        return pageWithName("HomePage");
    }

    /** @TypeInfo Release */
    public EOEnterpriseObject targetRelease() {
        NSArray bugsInBuild = bugsInBuild();
        EOEnterpriseObject result = null;
        if (bugsInBuild != null && bugsInBuild.count() > 0) {
            EOEnterpriseObject bug = (EOEnterpriseObject) bugsInBuild.lastObject();
            result = (EOEnterpriseObject) bug.valueForKey("targetRelease");
        }
        return result;
    }

    /** @TypeInfo Release */
    protected NSArray targetReleases() {
        NSMutableArray result = new NSMutableArray();
        NSArray bugsInBuild = bugsInBuild();
        if (bugsInBuild != null && bugsInBuild.count() > 0) {
            for (Enumeration e = bugsInBuild.objectEnumerator(); e.hasMoreElements(); ) {
                Bug bug = (Bug) e.nextElement();
                EOEnterpriseObject r = (EOEnterpriseObject) bug.valueForKey("targetRelease");
                if (!result.containsObject(r)) result.addObject(r);
            }
        }
        return result;
    }
}
