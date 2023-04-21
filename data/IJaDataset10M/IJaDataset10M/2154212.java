package com.khotyn.heresy.dao;

import java.util.List;

/**
 * 关键字DAO
 * @author 黄挺
 *
 */
public interface KeywordDAO {

    /**
	 * 查询所有的关键字
	 * @return 所有的关键字
	 */
    public List<String> selectAllKeywords();
}
