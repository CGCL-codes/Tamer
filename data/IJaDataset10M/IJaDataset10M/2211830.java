package com.gever.goa.dailyoffice.bbs.dao;

import java.util.List;
import com.gever.exception.DefaultException;
import com.gever.vo.VOInterface;

public interface ViewTopiclistDAO {

    public List queryAll(VOInterface vo) throws DefaultException;

    public VOInterface queryByPK(VOInterface vo) throws DefaultException;
}
