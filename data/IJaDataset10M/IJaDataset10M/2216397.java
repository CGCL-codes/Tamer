package com.petecapra.cashflow.model.dao;

import org.springframework.stereotype.Service;
import com.petecapra.cashflow.model.Schedule;

@Service
public class ScheduleManager extends CashflowManager<Schedule> {

    public ScheduleManager() {
        super(Schedule.class);
    }

    @Override
    public void delete(Schedule s) {
        s.getAccount().getSchedules().remove(s);
        super.delete(s);
    }
}
