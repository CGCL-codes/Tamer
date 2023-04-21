package ca.sqlpower.architect.enterprise;

import java.util.ArrayList;
import java.util.List;
import ca.sqlpower.architect.ArchitectProject;
import ca.sqlpower.architect.ArchitectSession;
import ca.sqlpower.object.AbstractPoolingSPListener;
import ca.sqlpower.object.SPChildEvent;
import ca.sqlpower.object.SPListener;
import ca.sqlpower.sqlobject.SQLDatabase;
import ca.sqlpower.sqlobject.SQLObjectException;
import ca.sqlpower.sqlobject.SQLObjectUtils;
import ca.sqlpower.sqlobject.UserDefinedSQLType;
import ca.sqlpower.sqlobject.UserDefinedSQLTypeSnapshot;

/**
 * This {@link SPListener} listens for newly added
 * {@link UserDefinedSQLTypeSnapshot}s on an {@link ArchitectProject}. When a
 * {@link UserDefinedSQLTypeSnapshot} is added, all the
 * {@link UserDefinedSQLType}s upstream type previously referencing the
 * snapshot's original UUID needs to be updated to the snapshot's
 * {@link UserDefinedSQLType} UUID.
 */
public class UpstreamTypeUpdaterListener extends AbstractPoolingSPListener {

    /**
     * The {@link ArchitectSession} where its workspace's
     * {@link UserDefinedSQLTypeSnapshot}s and the references to the
     * {@link UserDefinedSQLType} upstream types need to be updated.
     */
    private final ArchitectSession session;

    /**
     * Creates a new {@link UpstreamTypeUpdaterListener}.
     * 
     * @param session
     *            The {@link ArchitectSession} where its workspace's
     *            {@link UserDefinedSQLTypeSnapshot}s and the references to
     *            {@link UserDefinedSQLType} upstream types need to be updated.
     */
    public UpstreamTypeUpdaterListener(ArchitectSession session) {
        this.session = session;
    }

    @Override
    protected void childAddedImpl(SPChildEvent evt) {
        if (evt.getChild() instanceof UserDefinedSQLTypeSnapshot) {
            try {
                UserDefinedSQLTypeSnapshot snapshot = (UserDefinedSQLTypeSnapshot) evt.getChild();
                if (!snapshot.isDomainSnapshot()) {
                    String originalUUID = snapshot.getOriginalUUID();
                    List<UserDefinedSQLType> types = new ArrayList<UserDefinedSQLType>();
                    SQLDatabase targetDatabase = session.getWorkspace().getTargetDatabase();
                    SQLObjectUtils.findDescendentsByClass(targetDatabase, UserDefinedSQLType.class, types);
                    for (UserDefinedSQLType type : types) {
                        if (type.getUpstreamType() != null && type.getUpstreamType().getUUID().equals(originalUUID)) {
                            type.setUpstreamType(snapshot.getSPObject());
                        }
                    }
                }
            } catch (SQLObjectException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
