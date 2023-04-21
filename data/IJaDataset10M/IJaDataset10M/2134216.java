package com.assignyourtime.repository.jpa.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.synyx.hades.dao.GenericDao;
import org.synyx.hades.domain.AbstractPersistable;
import org.synyx.hades.domain.Order;
import org.synyx.hades.domain.PageRequest;
import com.assignyourtime.domain.User;
import com.assignyourtime.repository.AssignyourtimeService;
import com.assignyourtime.repository.jpa.UserDao;

/**
 * @author Henning Teek
 */
@Service(value = "AssignyourtimeService")
@Transactional
public class AssignyourtimeServiceImpl implements AssignyourtimeService {

    @Resource
    private UserDao userDao;

    @SuppressWarnings("unchecked")
    private <T extends AbstractPersistable<Long>> GenericDao<T, Long> getDao(Class<T> type) {
        if (type == User.class) {
            return (GenericDao<T, Long>) userDao;
        }
        throw new RuntimeException("no dao found for type: " + type.getName());
    }

    @Transactional(readOnly = true)
    @Override
    public <T extends AbstractPersistable<Long>> T load(Class<T> type, Long id) {
        return getDao(type).readByPrimaryKey(id);
    }

    @Transactional(readOnly = true)
    @Override
    public <T extends AbstractPersistable<Long>> List<T> loadAll(Class<T> type, final int from, final int count, final String sortProperty, final boolean sortAsc) {
        final PageRequest pageable;
        int page;
        if (from > 0) {
            page = from / count;
        } else {
            page = 0;
        }
        if (sortProperty == null || "".equals(sortProperty.trim())) {
            pageable = new PageRequest(page, count);
        } else {
            pageable = new PageRequest(page, count, sortAsc ? Order.ASCENDING : Order.DESCENDING, sortProperty);
        }
        return getDao(type).readAll(pageable).asList();
    }

    @Transactional(readOnly = true)
    @Override
    public <T extends AbstractPersistable<Long>> List<T> loadAll(Class<T> type) {
        return getDao(type).readAll();
    }

    @Transactional(readOnly = true)
    @Override
    public <T extends AbstractPersistable<Long>> int getCount(Class<T> type) {
        return getDao(type).count().intValue();
    }

    @Override
    public <T extends AbstractPersistable<Long>> void save(Class<T> type, T object) {
        getDao(type).save(object);
    }

    @Override
    public <T extends AbstractPersistable<Long>> void delete(Class<T> type, T object) {
        getDao(type).delete(getDao(type).readByPrimaryKey(object.getId()));
    }
}
