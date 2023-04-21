package server.cd.model;

import java.sql.*;
import java.util.Calendar;
import project.cn.dataType.*;

public class SDPlanModel {

    private DPlanRequest mRequestData;

    private Connection planConn;

    private boolean opSuccess = true;

    private String errReport;

    public SDPlanModel(DPlanRequest data, Connection conn) {
        mRequestData = data;
        planConn = conn;
        switch(mRequestData.getRequest()) {
            case yarin.insert:
                dealDBInsert();
                break;
            case yarin.delete:
                dealDBDelete();
                break;
            case yarin.edit:
                dealDBEdit();
                break;
            case yarin.contactgiven:
                dealDBRight();
                break;
        }
    }

    private void dealDBRight() {
        int myID = mRequestData.getMyID();
        int planID = mRequestData.getPlan().getPlanid();
        try {
            PreparedStatement rightStmt = planConn.prepareStatement(yarin.setPlanVisible);
            for (int i = 0; i < mRequestData.getTargetlist().size(); i++) {
                rightStmt.setInt(1, myID);
                rightStmt.setInt(2, planID);
                rightStmt.setInt(3, mRequestData.getTargetlist().get(i));
                rightStmt.execute();
            }
            rightStmt.close();
        } catch (SQLException e) {
            errReport = "���ù����ƻ��ɼ���ʧ��";
            opSuccess = false;
            System.out.println(errReport + e + " SDPlanModel dealDBRight");
        }
    }

    private void dealDBEdit() {
        int myID = mRequestData.getMyID();
        DPlan mPlan = mRequestData.getPlan();
        try {
            PreparedStatement editStmt = planConn.prepareStatement(yarin.updatePlan);
            editStmt.setString(1, mPlan.getSubject());
            long datestart = mPlan.getDateStart().getTime();
            editStmt.setLong(2, datestart);
            long dateend = mPlan.getDateEnd().getTime();
            editStmt.setLong(3, dateend);
            editStmt.setString(4, mPlan.getPlace());
            editStmt.setString(5, mPlan.getContext());
            editStmt.setInt(6, mPlan.getState());
            editStmt.setInt(7, mPlan.getFeature1());
            editStmt.setInt(8, mPlan.getFeature2());
            editStmt.setInt(9, myID);
            editStmt.setInt(10, mPlan.getPlanid());
            editStmt.execute();
            editStmt.close();
        } catch (SQLException e) {
            errReport = "�޸Ĺ����ƻ�ʧ��ʧ��";
            opSuccess = false;
            System.out.println(errReport + e + "SDPlanModel dealDBEdit");
        }
        if (opSuccess) addLog(myID, "�޸Ĺ����ƻ���" + mRequestData.getPlan().getSubject());
    }

    private void dealDBDelete() {
        int myID = mRequestData.getMyID();
        int planID = mRequestData.getPlan().getPlanid();
        try {
            PreparedStatement deleteStmt = planConn.prepareStatement(yarin.deletePlan);
            deleteStmt.setInt(1, myID);
            deleteStmt.setInt(2, planID);
            deleteStmt.execute();
            deleteStmt.close();
        } catch (SQLException e) {
            errReport = "ɾ�����ƻ�ʧ��ʧ��";
            opSuccess = false;
            System.out.println(errReport + e + "SDPlanModel dealDBDelete");
        }
        if (opSuccess) addLog(myID, "ɾ�����ƻ���" + mRequestData.getPlan().getState());
    }

    private void dealDBInsert() {
        int myID = mRequestData.getMyID();
        int planID = findPlanID(myID) + 1;
        if (planID <= 0) opSuccess = false;
        DPlan mPlan = mRequestData.getPlan();
        if (opSuccess) {
            try {
                PreparedStatement insertStmt = planConn.prepareStatement(yarin.insertPlan);
                insertStmt.setInt(1, myID);
                insertStmt.setInt(2, planID);
                insertStmt.setString(3, mPlan.getSubject());
                long datestart = mPlan.getDateStart().getTime();
                insertStmt.setLong(4, datestart);
                long dateend = mPlan.getDateEnd().getTime();
                insertStmt.setLong(5, dateend);
                insertStmt.setString(6, mPlan.getPlace());
                insertStmt.setString(7, mPlan.getContext());
                insertStmt.setInt(8, mPlan.getState());
                insertStmt.setInt(9, mPlan.getFeature1());
                insertStmt.setInt(10, mPlan.getFeature2());
                insertStmt.execute();
                insertStmt.close();
            } catch (SQLException e) {
                errReport = "�½������ƻ�ʧ��ʧ��";
                opSuccess = false;
                System.out.println(errReport + e + "SDPlanModel dealDBInsert");
            }
        }
        if (opSuccess) addLog(myID, "���������ƻ���" + mRequestData.getPlan().getSubject());
    }

    private int findPlanID(int myID) {
        int planID = 0;
        try {
            PreparedStatement planidStmt = planConn.prepareStatement(yarin.coutPlan);
            planidStmt.setInt(1, myID);
            ResultSet planidSet = planidStmt.executeQuery();
            if (planidSet.next()) planID = planidSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return planID;
    }

    private void addLog(int logid, String worklog) {
        long logtime = Calendar.getInstance().getTimeInMillis();
        try {
            PreparedStatement logStmt = planConn.prepareStatement(yarin.insertLog);
            logStmt.setInt(1, logid);
            logStmt.setLong(2, logtime);
            logStmt.setString(3, worklog);
            logStmt.execute();
            logStmt.close();
        } catch (SQLException e) {
            errReport = "������־����ʧ��";
            System.out.println(this.getClass().getName() + worklog);
        }
    }

    public void setOpSuccess(boolean opSuccess) {
        this.opSuccess = opSuccess;
    }

    public boolean isOpSuccess() {
        return opSuccess;
    }
}
