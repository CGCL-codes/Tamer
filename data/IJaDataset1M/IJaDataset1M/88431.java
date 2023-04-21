package de.fu_berlin.inf.dpp.activities.serializable;

import java.util.ArrayList;
import java.util.List;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import de.fu_berlin.inf.dpp.activities.SPath;
import de.fu_berlin.inf.dpp.activities.SPathDataObject;
import de.fu_berlin.inf.dpp.activities.business.ChecksumErrorActivity;
import de.fu_berlin.inf.dpp.activities.business.IActivity;
import de.fu_berlin.inf.dpp.net.JID;
import de.fu_berlin.inf.dpp.project.ISarosSession;

/**
 * A Checksum Error is an notification send to the host and peers by a user, who
 * wants inconsistencies to be recovered.
 */
@XStreamAlias("ChecksumError")
public class ChecksumErrorActivityDataObject extends AbstractActivityDataObject {

    @XStreamAsAttribute
    protected String recoveryID;

    @XStreamImplicit
    protected List<SPathDataObject> paths;

    public List<SPathDataObject> getPaths() {
        return paths;
    }

    /**
     * Each ChecksumError has a unique ID, which should be used to identify a
     * recovery session
     */
    public String getRecoveryID() {
        return recoveryID;
    }

    public ChecksumErrorActivityDataObject(JID source, List<SPathDataObject> paths, String recoveryID) {
        super(source);
        this.paths = paths;
        this.recoveryID = recoveryID;
    }

    public IActivity getActivity(ISarosSession sarosSession) {
        ArrayList<SPath> sPaths = new ArrayList<SPath>();
        if (this.paths != null) for (SPathDataObject path : this.paths) {
            sPaths.add(path.toSPath(sarosSession));
        }
        return new ChecksumErrorActivity(sarosSession.getUser(getSource()), sPaths, recoveryID);
    }

    @Override
    public String toString() {
        return "ChecksumErrorDO(src:" + this.getSource() + ", paths:" + this.paths + ", recoveryID:" + recoveryID + ")";
    }
}
