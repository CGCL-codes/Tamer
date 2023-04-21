package com.jandan.persistence.iface;

import java.util.List;
import com.jandan.ui.model.WordLib;

/**
 * 词库数据访问接口
 * @author gong
 * @version 1.0
 * @see com.jandan.persistence.sqlmapdao.WordLibSqlMapDao
 */
public interface WordLibDao {

    /**
	 * 获取某个词库
	 * @param wordLibID 词库ID
	 * @return 词库
	 */
    WordLib getWordLibByWordLibID(long wordLibID);

    /**
     * 获取所有用户词库，<strong>此方法已被启用</strong>
     * @return 
     */
    List<WordLib> getAllUserWordLibList();

    /**
     * 插入词库
     * @param wordLib 词库
     * @return 词库ID
     */
    long insertWordLib(WordLib wordLib);

    /**
     * 更新词库
     * @param wordLib 词库
     */
    void updateWordLib(WordLib wordLib);

    /**
     * 删除词库
     * @param wordLibID 词库ID
     */
    void deleteWordLib(long wordLibID);

    /**
     * 获取某部分用户词库列表
     * @param start
     * @param limit
     * @return
     */
    List<WordLib> getAllUserWordLibList(int start, int limit);

    /**
	 * 获取又有词库总数
	 * @return 词库总数
	 */
    int getTotalWordLibCount();
}
