package org.swana.dao;

import java.util.Calendar;
import java.util.List;
import org.swana.pojo.StatByOrigin;
import org.swiftdao.KeyedCrudDao;

/**
 * @author Wang Yuxing
 *
 */
public interface StatByOriginDao extends KeyedCrudDao<StatByOrigin> {

    /**
	 * 根据条件查询区域的统计信息，按来源排序
	 * 统计前50个来源，剩余的归为其它
	 * OriginID为0表示自行输入的网址，置于最后第二列
	 * 其它数据的总和归为最后一列
	 * @param pid 页面ID
	 * @param start 起始时间，格式为yy-MM-dd
	 * @param end 结束时间，格式为yy-MM-dd
	 * @return
	 */
    List<StatByOrigin> findOrginStatistics(int pid, Calendar start, Calendar end);
}
