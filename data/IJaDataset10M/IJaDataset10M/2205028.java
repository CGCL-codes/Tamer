package net.sourceforge.traffiscope.entity.dpl;

import com.sleepycat.persist.model.DeleteAction;
import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;
import net.sourceforge.traffiscope.entity.ServerEntity;
import net.sourceforge.traffiscope.model.ServerTO;

@Entity
public class Server extends AbstractNamedEntity<ServerTO> implements ServerEntity {

    private static final long serialVersionUID = 7958190759025929840L;

    @SecondaryKey(relate = Relationship.MANY_TO_ONE, relatedEntity = Gang.class, onRelatedEntityDelete = DeleteAction.CASCADE)
    private int gangId;

    public int getGangId() {
        return gangId;
    }

    public void setGangId(int value) {
        gangId = value;
    }

    @Override
    public ServerTO buildTO() {
        ServerTO to = new ServerTO();
        writeTO(to);
        return to;
    }

    @Override
    public void writeTO(ServerTO to) {
        super.writeTO(to);
        to.setGangId(getGangId());
    }
}
