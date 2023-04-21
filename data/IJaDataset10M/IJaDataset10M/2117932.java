package Server;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import TransmitterS.Group;
import TransmitterS.Job;
import TransmitterS.Pupil;
import TransmitterS.Solution;
import TransmitterS.Statistic;

/**
 * @author LK13
 */
public class GroupImp implements Group {

    private static final long serialVersionUID = 1635973410881974752L;

    public final ServerImp myServer;

    public Set<Pupil> myPupil;

    public SolutionImp mySolution;

    public Job myJob;

    public Vector<Statistic> myStatistic;

    public String lastVersion;

    public GroupImp(Job _myJob, ServerImp myServer) {
        this.myServer = myServer;
        myJob = _myJob;
        mySolution = new SolutionImp();
        myStatistic = new Vector<Statistic>();
        myPupil = new HashSet<Pupil>();
        lastVersion = "";
    }

    public Integer getGroupID() {
        return myServer.allGroups.indexOf(this);
    }

    public Set<Pupil> getPupils() {
        return myPupil;
    }

    public Job getJob() {
        return myJob;
    }

    public Solution getSolution() {
        return mySolution;
    }

    public String getLastVersion() {
        return lastVersion;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return false;
    }
}
