package org.personalsmartspace.spm.access.impl.repo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.personalsmartspace.log.impl.PSSLog;
import org.personalsmartspace.spm.access.api.platform.IAccessControlDecision;
import org.personalsmartspace.spm.access.api.repo.AccessControlRepoException;
import org.personalsmartspace.spm.access.api.repo.IDecisionRepo;
import org.personalsmartspace.sre.api.pss3p.IDigitalPersonalIdentifier;

/**
 * Stub implementation of the <code>IDecisionRepo</code> interface that uses a
 * hash map.
 * 
 * @author <a href="mailto:nliampotis@users.sourceforge.net">Nicolas
 *         Liampotis</a> (ICCS)
 * @see IAccessControlDecision
 * @since 0.4.0
 */
public class StubDecisionRepo implements IDecisionRepo {

    /** The Access Control Decisions. */
    private final Map<IDigitalPersonalIdentifier, IAccessControlDecision> decisions;

    /** The PSS logging facility. */
    private final PSSLog log = new PSSLog(this);

    public StubDecisionRepo() {
        this.log.info("Connecting to Access Control Decision repository");
        this.decisions = new ConcurrentHashMap<IDigitalPersonalIdentifier, IAccessControlDecision>();
    }

    @Override
    public IAccessControlDecision retrieve(final IDigitalPersonalIdentifier requestor) throws AccessControlRepoException {
        this.log.debug("Retrieving decision for requestor " + requestor);
        IAccessControlDecision decision = this.decisions.get(requestor);
        return (decision != null) ? copy(decision) : null;
    }

    @Override
    public void store(final IAccessControlDecision decision) throws AccessControlRepoException {
        this.log.debug("Storing decision " + decision);
        IAccessControlDecision copy = copy(decision);
        this.decisions.put(copy.getRequestor(), copy);
    }

    @Override
    public boolean remove(final IDigitalPersonalIdentifier requestor) throws AccessControlRepoException {
        this.log.debug("Removing decision for requestor " + requestor);
        return (this.decisions.remove(requestor) != null) ? true : false;
    }

    @Override
    public void clear() throws AccessControlRepoException {
        this.log.debug("Clearing all decisions");
        this.decisions.clear();
    }

    private static IAccessControlDecision copy(IAccessControlDecision orig) {
        IAccessControlDecision copy = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream ous = new ObjectOutputStream(baos);
            ous.writeObject(orig);
            ous.flush();
            ous.close();
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
            copy = (IAccessControlDecision) ois.readObject();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        return copy;
    }
}
