package com.bones.query.sysexpert_new.dao;

import com.bones.core.utils.exception.DaoAccessException;
import com.bones.query.sysexpert_new.dao.entity.AlarmReceiver;
import com.bones.core.web.tags.IPagination;

/** InnoDB free: 5120 kB访问接口定义 */
public interface IAlarmReceiverDao {

    /** 分页查询——InnoDB free: 5120 kB ALARM_RECEIVER列表 */
    public IPagination queryAlarmReceiverList(AlarmReceiver alarmReceiver, int first, int max) throws DaoAccessException;

    /** 查询单条——InnoDB free: 5120 kB ALARM_RECEIVER详情 */
    public AlarmReceiver viewAlarmReceiver(AlarmReceiver alarmReceiver) throws DaoAccessException;

    /** 删除单条——InnoDB free: 5120 kB ALARM_RECEIVER记录 */
    public void delAlarmReceiver(AlarmReceiver alarmReceiver) throws DaoAccessException;
}
