package pubweb.supernode;

import java.util.HashSet;

public class JobProcessMetaData {

    public boolean migrationInProgress = false;

    public String migrationDestination = null;

    public boolean restorationInProgress = false;

    public boolean restorationChecking = false;

    public int restoringPid = -1;

    public HashSet<Integer> restorationNotifications = new HashSet<Integer>();
}
