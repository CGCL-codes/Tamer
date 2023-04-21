package com.poltman.dspace.db.service.pm.impl;

import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.poltman.dspace.db.entity.pm.LanguagesEntity;
import com.poltman.dspace.db.exceptions.RepositoryAccessException;
import com.poltman.dspace.db.exceptions.RepositoryException;
import com.poltman.dspace.db.repository.pm.LanguagesRepository;
import com.poltman.dspace.db.service.pm.LanguagesService;

/**
 * 
 * @author Zbigniew Ciok; z.ciok@poltman.com
 *
 */
@Service("languagesService")
public class LanguagesServiceImpl implements LanguagesService {

    private static Logger log = Logger.getLogger(LanguagesServiceImpl.class);

    @Autowired
    private LanguagesRepository repository;

    @Override
    public List<LanguagesEntity> findAll() throws RepositoryAccessException, RepositoryException {
        return repository.findAll();
    }

    @Override
    public List<LanguagesEntity> findByImplemented(Boolean implemented) throws RepositoryAccessException, RepositoryException {
        return repository.findByImplemented(implemented);
    }

    @Override
    public LanguagesEntity findByName(String name) throws RepositoryAccessException, RepositoryException {
        return repository.findByName(name);
    }
}
