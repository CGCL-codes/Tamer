package com.fantosoft.admin.meetingroom.rule;

import com.fantosoft.admin.AdminException;
import com.fantosoft.admin.meetingroom.IMeetingRoom;
import com.fantosoft.admin.meetingroom.IReserveInfo;
import com.fantosoft.admin.meetingroom.IReserveRule;

/**
 * @author fanto.
 * Ԥ���Ŀ�ʼʱ�䲻�ܹ�С�ڵ�ǰ��ʱ��
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BeforeCurrentRule implements IReserveRule {

    public boolean isValid(final IMeetingRoom room, final IReserveInfo reserveInfo) throws AdminException {
        long tStart = System.currentTimeMillis();
        if (reserveInfo.getBeginDate().getTime() < tStart) throw new AdminException("����Ԥ����ǰʱ����ǰ�Ļ�����");
        return true;
    }
}
