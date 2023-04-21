package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.Link;
import at.rc.tacos.platform.model.Lockable;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.exception.NoSuchCommandException;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.message.AbstractMessage;
import at.rc.tacos.platform.net.message.UpdateMessage;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.LinkService;
import at.rc.tacos.platform.services.dbal.LockableService;
import at.rc.tacos.platform.services.exception.ServiceException;

public class LinkHandler implements Handler<Link> {

    @Service(clazz = LinkService.class)
    private LinkService linkService;

    @Service(clazz = LockableService.class)
    private LockableService lockableService;

    @Override
    public void add(MessageIoSession session, Message<Link> message) throws ServiceException, SQLException {
        List<Link> linkList = message.getObjects();
        for (Link link : linkList) {
            int id = linkService.addLink(link);
            if (id == -1) throw new ServiceException("Failed to add the link " + link);
            link.setId(id);
        }
        session.writeResponseBrodcast(message, linkList);
    }

    @Override
    public void get(MessageIoSession session, Message<Link> message) throws ServiceException, SQLException {
        Map<String, String> params = message.getParams();
        if (params.containsKey(IFilterTypes.ID_FILTER)) {
            int linkId = Integer.parseInt(params.get(IFilterTypes.ID_FILTER));
            final Link link = linkService.getLinkById(linkId);
            if (link == null) {
                throw new ServiceException("No link found with the id " + linkId);
            }
            if (lockableService.containsLock(link)) {
                Lockable lockable = lockableService.getLock(link);
                link.setLocked(lockable.isLocked());
                link.setLockedBy(lockable.getLockedBy());
            }
            session.writeResponse(message, link);
            return;
        }
        List<Link> linkList = linkService.listLinks();
        if (linkList == null) throw new ServiceException("Failed to list the links");
        for (Link link : linkList) {
            if (!lockableService.containsLock(link)) {
                continue;
            }
            Lockable lockable = lockableService.getLock(link);
            link.setLocked(lockable.isLocked());
            link.setLockedBy(lockable.getLockedBy());
        }
        session.writeResponse(message, linkList);
    }

    @Override
    public void remove(MessageIoSession session, Message<Link> message) throws ServiceException, SQLException {
        List<Link> linkList = message.getObjects();
        for (Link link : linkList) {
            if (!linkService.removeLink(link.getId())) throw new ServiceException("Failed to remove the link:" + link);
            lockableService.removeLock(link);
        }
        session.writeResponseBrodcast(message, linkList);
    }

    @Override
    public void update(MessageIoSession session, Message<Link> message) throws ServiceException, SQLException {
        List<Link> linkList = message.getObjects();
        for (Link link : linkList) {
            if (!linkService.updateLink(link)) throw new ServiceException("Failed to update the link:" + link);
            lockableService.updateLock(link);
        }
        session.writeResponseBrodcast(message, linkList);
    }

    @Override
    public void execute(MessageIoSession session, Message<Link> message) throws ServiceException, SQLException {
        String command = message.getParams().get(AbstractMessage.ATTRIBUTE_COMMAND);
        String handler = getClass().getSimpleName();
        if ("doLock".equalsIgnoreCase(command)) {
            lockableService.addAllLocks(message.getObjects());
            UpdateMessage<Link> updateMessage = new UpdateMessage<Link>(message.getObjects());
            session.brodcastMessage(updateMessage);
            return;
        }
        if ("doUnlock".equalsIgnoreCase(command)) {
            lockableService.removeAllLocks(message.getObjects());
            UpdateMessage<Link> updateMessage = new UpdateMessage<Link>(message.getObjects());
            session.brodcastMessage(updateMessage);
            return;
        }
        throw new NoSuchCommandException(handler, command);
    }

    @Override
    public Link[] toArray() {
        return null;
    }
}
