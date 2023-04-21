package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.List;
import at.rc.tacos.platform.model.Lockable;
import at.rc.tacos.platform.model.MobilePhoneDetail;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.exception.NoSuchCommandException;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.message.AbstractMessage;
import at.rc.tacos.platform.net.message.UpdateMessage;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.LockableService;
import at.rc.tacos.platform.services.dbal.MobilePhoneService;
import at.rc.tacos.platform.services.exception.ServiceException;

public class MobilePhoneHandler implements Handler<MobilePhoneDetail> {

    @Service(clazz = MobilePhoneService.class)
    private MobilePhoneService phoneService;

    @Service(clazz = LockableService.class)
    private LockableService lockableService;

    @Override
    public void add(MessageIoSession session, Message<MobilePhoneDetail> message) throws ServiceException, SQLException {
        List<MobilePhoneDetail> phoneList = message.getObjects();
        for (MobilePhoneDetail phone : phoneList) {
            int id = phoneService.addMobilePhone(phone);
            if (id == -1) throw new ServiceException("Failed to add the mobile phone:" + phone);
            phone.setId(id);
        }
        session.writeResponseBrodcast(message, phoneList);
    }

    @Override
    public void get(MessageIoSession session, Message<MobilePhoneDetail> message) throws ServiceException, SQLException {
        List<MobilePhoneDetail> phoneList = phoneService.listMobilePhones();
        if (phoneList == null) throw new ServiceException("Failed to list the mobile phones");
        for (MobilePhoneDetail detail : phoneList) {
            if (!lockableService.containsLock(detail)) {
                continue;
            }
            Lockable lockable = lockableService.getLock(detail);
            detail.setLocked(lockable.isLocked());
            detail.setLockedBy(lockable.getLockedBy());
        }
        session.writeResponse(message, phoneList);
    }

    @Override
    public void remove(MessageIoSession session, Message<MobilePhoneDetail> message) throws ServiceException, SQLException {
        List<MobilePhoneDetail> phoneList = message.getObjects();
        for (MobilePhoneDetail phone : phoneList) {
            if (!phoneService.removeMobilePhone(phone.getId())) throw new ServiceException("Failed to remove the mobile phone:" + phone);
            lockableService.removeLock(phone);
        }
        session.writeResponseBrodcast(message, phoneList);
    }

    @Override
    public void update(MessageIoSession session, Message<MobilePhoneDetail> message) throws ServiceException, SQLException {
        List<MobilePhoneDetail> phoneList = message.getObjects();
        for (MobilePhoneDetail phone : phoneList) {
            if (!phoneService.updateMobilePhone(phone)) throw new ServiceException("Failed to update the mobile phone:" + phone);
            lockableService.removeLock(phone);
        }
        session.writeResponseBrodcast(message, phoneList);
    }

    @Override
    public void execute(MessageIoSession session, Message<MobilePhoneDetail> message) throws ServiceException, SQLException {
        String command = message.getParams().get(AbstractMessage.ATTRIBUTE_COMMAND);
        String handler = getClass().getSimpleName();
        if ("doLock".equalsIgnoreCase(command)) {
            lockableService.addAllLocks(message.getObjects());
            UpdateMessage<MobilePhoneDetail> updateMessage = new UpdateMessage<MobilePhoneDetail>(message.getObjects());
            session.brodcastMessage(updateMessage);
            return;
        }
        if ("doUnlock".equalsIgnoreCase(command)) {
            lockableService.removeAllLocks(message.getObjects());
            UpdateMessage<MobilePhoneDetail> updateMessage = new UpdateMessage<MobilePhoneDetail>(message.getObjects());
            session.brodcastMessage(updateMessage);
            return;
        }
        throw new NoSuchCommandException(handler, command);
    }

    @Override
    public MobilePhoneDetail[] toArray() {
        return null;
    }
}
