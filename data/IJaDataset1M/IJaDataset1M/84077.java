package gwtm.server.services.tm;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import gwtm.client.services.tm.RoleService;
import gwtm.client.services.tm.TmException;
import gwtm.client.services.tm.virtual.RoleVirtual;
import gwtm.client.services.tm.virtual.AssociationVirtual;
import gwtm.client.services.tm.virtual.TopicMapVirtual;
import gwtm.client.services.tm.virtual.TopicVirtual;
import gwtm.server.tm.AssociationHelper;
import gwtm.server.tm.RoleHelper;
import gwtm.server.tm.TopicHelper;
import gwtm.server.tm.TopicMapSystemHelper;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.tmapi.core.Topic;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Association;
import org.tmapi.core.AssociationRole;

/**
 *
 * @author User
 */
public class RoleServiceImpl extends RemoteServiceServlet implements RoleService {

    public AssociationVirtual getAssociation(RoleVirtual rV) throws TmException {
        try {
            AssociationRole ar = RoleHelper.getAssociationRole(rV);
            Association a = ar.getAssociation();
            AssociationVirtual av = AssociationHelper.createVirtual(a);
            return av;
        } catch (Exception ex) {
            throw new TmException(ex.getMessage(), getStackTrace(ex), ex);
        }
    }

    public TopicVirtual getPlayer(RoleVirtual rV) throws TmException {
        try {
            AssociationRole ar = RoleHelper.getAssociationRole(rV);
            Topic player = ar.getPlayer();
            TopicVirtual playerV = TopicHelper.createVirtual(player, true);
            return playerV;
        } catch (Exception ex) {
            throw new TmException(ex.getMessage(), getStackTrace(ex), ex);
        }
    }

    public TopicVirtual getReifier(RoleVirtual rV) throws TmException {
        try {
            AssociationRole ar = RoleHelper.getAssociationRole(rV);
            throw new Exception("RoleServiceImpl.getReifier Not implemented yet.");
        } catch (Exception ex) {
            throw new TmException(ex.getMessage(), getStackTrace(ex), ex);
        }
    }

    public TopicVirtual getType(RoleVirtual rV) throws TmException {
        try {
            AssociationRole ar = RoleHelper.getAssociationRole(rV);
            Topic type = ar.getType();
            TopicVirtual typeV = TopicHelper.createVirtual(type, true);
            return typeV;
        } catch (Exception ex) {
            throw new TmException(ex.getMessage(), getStackTrace(ex), ex);
        }
    }

    public void remove(RoleVirtual rV) throws TmException {
        try {
            AssociationRole ar = RoleHelper.getAssociationRole(rV);
            ar.remove();
        } catch (Exception ex) {
            throw new TmException(ex.getMessage(), getStackTrace(ex), ex);
        }
    }

    public void setPlayer(RoleVirtual rV, TopicVirtual playerV) throws TmException {
        try {
            AssociationRole ar = RoleHelper.getAssociationRole(rV);
            Topic player = TopicHelper.getTopic(playerV);
            ar.setPlayer(player);
        } catch (Exception ex) {
            throw new TmException(ex.getMessage(), getStackTrace(ex), ex);
        }
    }

    public void setType(RoleVirtual rV, TopicVirtual typeV) throws TmException {
        try {
            AssociationRole ar = RoleHelper.getAssociationRole(rV);
            Topic type = TopicHelper.getTopic(typeV);
            ar.setType(type);
        } catch (Exception ex) {
            throw new TmException(ex.getMessage(), getStackTrace(ex), ex);
        }
    }

    public String getStackTrace(Exception ex) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ex.printStackTrace(new PrintStream(out));
        return out.toString();
    }
}
