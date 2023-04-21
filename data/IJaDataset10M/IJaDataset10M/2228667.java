package com.multimedia.service.counter;

import com.multimedia.model.beans.Counter;
import common.dao.IGenericDAO;
import common.services.generic.GenericServiceImpl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * @author demchuck.dima@gmail.com
 */
@Service(value = "counterService")
public class CounterServiceImpl extends GenericServiceImpl<Counter, Long> {

    @Override
    @Resource(name = "counterDAO")
    public void setDao(IGenericDAO<Counter, Long> dao) {
        super.setDao(dao);
    }
}
