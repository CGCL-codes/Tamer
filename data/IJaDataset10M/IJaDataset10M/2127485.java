package com.mgiandia.library.domain.fines;

import java.util.Calendar;
import com.mgiandia.library.util.SimpleCalendar;

/**
 * � ������� ���������� ����������� ��� ���������.
 * � ������� ��� ������ ��� ������������ �� �������� ��������� ����
 * ��������� ����� ��� ��������� ��� ����������� ����������
 */
class StrictFineStrategy extends LinearFineStrategy {

    @Override
    protected long calculateStrategyOverdue(SimpleCalendar due, SimpleCalendar returnDate) {
        if (due == null || returnDate == null) {
            return 0;
        }
        if (due.equals(returnDate)) {
            return 0;
        }
        if (due.after(returnDate)) {
            return 0;
        }
        Calendar calculationDate = Calendar.getInstance();
        calculationDate.setTimeInMillis(returnDate.getJavaCalendar().getTimeInMillis());
        calculationDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return due.durationInDays(new SimpleCalendar(calculationDate));
    }
}
