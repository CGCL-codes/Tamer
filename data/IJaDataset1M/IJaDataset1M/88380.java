package net.naijatek.myalumni.modules.common.service.impl;

import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import net.naijatek.myalumni.framework.exceptions.MyAlumniException;
import net.naijatek.myalumni.modules.common.domain.MessageFolderVO;
import net.naijatek.myalumni.modules.common.persistence.iface.MessageFolderDao;
import net.naijatek.myalumni.modules.common.service.IMessageFolderService;

public class MessageFolderServiceImpl implements IMessageFolderService {

    private MessageFolderDao mfDao;

    public MessageFolderServiceImpl(MessageFolderDao mfDao) {
        this.mfDao = mfDao;
    }

    public void save(MessageFolderVO entity) {
        mfDao.save(entity);
    }

    public void merge(MessageFolderVO entity) {
        mfDao.mergeObject(entity);
    }

    public List<MessageFolderVO> findAll() {
        return mfDao.findAll();
    }

    public void softDelete(String id, String lastModifiedBy) throws MyAlumniException {
        try {
            mfDao.softDeleteObject(id, lastModifiedBy);
        } catch (Exception e) {
            throw new MyAlumniException("Could not delete MessageFolderVO because " + e.getMessage());
        }
    }

    public void hardDelete(String id) throws MyAlumniException {
        try {
            mfDao.hardDeleteObject(id);
        } catch (Exception e) {
            throw new MyAlumniException("Could not delete MessageFolderVO because " + e.getMessage());
        }
    }

    public MessageFolderVO findById(String id) {
        return mfDao.findById(id);
    }

    public List<MessageFolderVO> findAllByStatus(String status) {
        return mfDao.findAllByStatus(status);
    }

    public List<MessageFolderVO> getMessageFoldersForMemberByUserName(String memberId) {
        return mfDao.getMessageFoldersForMemberByMemberId(memberId);
    }

    public void deleteMemberFolder(String memberId, String folderName) {
        mfDao.deleteMemberFolder(memberId, folderName);
    }

    public void deleteMemberMessageFolders(String memberId) {
        mfDao.deleteMemberMessageFolders(memberId);
    }

    public void createMemberMessageFolders(String memberId, String messageFolders, String lastModifiedBy) {
        StringTokenizer st = new StringTokenizer(messageFolders, ",");
        Date dt = new Date();
        int i = 1;
        while (st.hasMoreTokens()) {
            mfDao.createMemberFolder(((String) st.nextElement()).trim(), memberId, i, 0, 0, 0, dt, dt, lastModifiedBy);
            i++;
        }
    }
}
