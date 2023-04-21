package com.gjzq.util;

import java.util.Date;

public class DateHelper extends com.thinkive.base.util.DateHelper {

    /**
	 * ���ڼ�ȥ����õ��µ�����
	 * @param date
	 * @param day
	 * @return
	 */
    public static Date getDateDiff(Date date, int day) {
        if (date == null) {
            return null;
        }
        long time = date.getTime();
        time = time - ((long) 1000 * 60 * 60 * 24 * day);
        return new Date(time);
    }
}
