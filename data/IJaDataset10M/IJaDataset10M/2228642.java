package com.redhat.gs.mrlogistics.scheduler;

import org.hibernate.criterion.Criterion;
import java.util.Set;
import static org.hibernate.criterion.Restrictions.*;
import com.redhat.gs.mrlogistics.data.Activity;

/**
 * @author arjun
 *
 */
public class UserScheduleInitializer implements IScheduleInitializer {

    private Class myClass;

    private long userID;

    public UserScheduleInitializer(long UID) {
        userID = UID;
        myClass = Activity.class;
    }

    public void Initialize(Set<Criterion> criterionSet) {
        criterionSet.add(eq("ID_Num", userID));
    }

    public Class classType() {
        return myClass;
    }
}
