package org.identifylife.taxonomy.store.service.impl;

import java.util.List;
import org.identifylife.taxonomy.store.model.TaxonName;
import org.identifylife.taxonomy.store.repository.TaxonNameRepository;
import org.identifylife.taxonomy.store.service.TaxonNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author dbarnier
 *
 */
@Service("taxonNameService")
@Transactional
public class TaxonNameServiceImpl implements TaxonNameService {

    @Autowired
    private TaxonNameRepository repository;

    public void setRepository(TaxonNameRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public TaxonName getByUuid(String uuid) {
        return repository.getByUuid(uuid);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaxonName> getByName(String name) {
        return repository.getByName(name);
    }
}
