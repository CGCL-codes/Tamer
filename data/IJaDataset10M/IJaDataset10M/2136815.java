package com.air.demo.service.imp;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.air.common.exception.OutofPageBoundException;
import com.air.common.service.imp.BaseServiceImp;
import com.air.common.util.QueryCondition;
import com.air.common.vo.PageResultListVO;
import com.air.demo.dao.BookDao;
import com.air.demo.service.IBookService;
import com.air.memcached.annotation.Cached;

@Service("bookService")
public class BookServiceImp extends BaseServiceImp implements IBookService, InitializingBean {

    @Autowired
    BookDao bookMapper;

    @Override
    public void afterPropertiesSet() throws Exception {
        super.baseDao = bookMapper;
    }

    @Override
    @Cached
    public PageResultListVO queryByCondition(QueryCondition condition, int curPageNum, int pageLimit) throws OutofPageBoundException {
        return super.queryByCondition(condition, curPageNum, pageLimit);
    }
}
