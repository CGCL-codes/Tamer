package org.codeandmagic.affected.service.impl;

import java.util.List;
import org.codeandmagic.affected.persistence.SvnProjectDao;
import org.codeandmagic.affected.service.SvnProjectService;
import org.codeandmagic.affected.svn.SvnException;
import org.codeandmagic.affected.svn.SvnProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SvnProjectServiceImpl implements SvnProjectService {

    private SvnProjectDao dao;

    @Required
    @Autowired
    public void setDao(SvnProjectDao dao) {
        this.dao = dao;
    }

    @Transactional(readOnly = true)
    public SvnProject get(String name) throws SvnException {
        return dao.get(name);
    }

    @Transactional(readOnly = true)
    public List<SvnProject> getAll() {
        return dao.getAll();
    }

    @Transactional(readOnly = false)
    public SvnProject create(String name, String url, long lastCheckedVersion) throws SvnException {
        SvnProject proj = new SvnProject();
        proj.setName(name);
        proj.setUrl(url);
        proj.setLastCheckedVersion(lastCheckedVersion);
        save(proj);
        return proj;
    }

    @Transactional(readOnly = false)
    public boolean save(SvnProject project) {
        return dao.save(project);
    }

    @Transactional(readOnly = false)
    public boolean delete(String name) throws SvnException {
        return dao.delete(dao.get(name));
    }
}
