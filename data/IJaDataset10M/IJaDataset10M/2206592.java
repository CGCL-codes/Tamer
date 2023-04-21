package org.dms.wicket.repository.cxf.service;

import java.util.List;
import javax.jws.WebService;
import org.dms.wicket.repository.db.dao.JcrFileStorageDao;
import org.dms.wicket.repository.db.model.FileDescription;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Emmanuel Nollase - emanux
 * created 2009 10 7 - 13:39:21
 */
@WebService(endpointInterface = "org.dms.wicket.repository.cxf.service.JcrWebServiceAccess")
public class JcrWebServiceAccessImpl implements JcrWebServiceAccess {

    @Autowired
    private JcrFileStorageDao jcrFileStorageDao;

    public void delete(FileDescription file) {
        jcrFileStorageDao.delete(file);
    }

    public List<FileDescription> findDocumentByBranch(String branch, int max) {
        return jcrFileStorageDao.loadAllByBranch(branch, 0, max);
    }

    public FileDescription loadByUUID(String uuid) {
        return jcrFileStorageDao.loadByUUID(uuid);
    }

    public void save(FileDescription file) {
        jcrFileStorageDao.save(file);
    }

    public void update(FileDescription file) {
        jcrFileStorageDao.update(file);
    }
}
