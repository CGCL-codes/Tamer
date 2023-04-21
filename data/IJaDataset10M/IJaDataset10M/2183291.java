package com.ma_la.myRunning.hibernateDao;

import com.ma_la.myRunning.dao.RunnerSpeedDao;
import com.ma_la.myRunning.domain.RunnerSpeed;

public class HibernateRunnerSpeedDao extends HibernateGenericDao implements RunnerSpeedDao {

    public HibernateRunnerSpeedDao() {
        super(RunnerSpeed.class);
    }
}
