package com.monad.homerun.rmictrl;

import java.rmi.RemoteException;
import com.monad.homerun.action.Plan;

public interface PlanCtrl extends ServerCtrl {

    String[] getPlanNames() throws RemoteException;

    String[] getActivePlanNames() throws RemoteException;

    Plan getPlan(String planName) throws RemoteException;

    boolean runPlan(String planName) throws RemoteException;

    void stopPlan(String planName) throws RemoteException;

    boolean addPlan(Plan plan) throws RemoteException;

    boolean updatePlan(Plan plan) throws RemoteException;

    boolean removePlan(String planName) throws RemoteException;
}
