package jframe.quartz.dao;

import java.util.List;
import java.util.Map;

/**
 * @描述:<p> </p>
 *
 * @作者: 叶平平(yepp)
 *
 * @时间: 2011-12-29 下午11:20:04
 */
public interface QuartzDao {

    public List<Map<String, Object>> getQrtzTriggers();
}
