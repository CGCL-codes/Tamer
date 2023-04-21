package server.cd.controller;

import java.net.Socket;
import java.sql.Connection;
import project.cn.dataType.*;
import server.cd.model.*;

public class ServerConnection extends Thread {

    private Connection selfConn;

    private ProjectServer mServer;

    private SocketModel socketModel;

    protected Data data;

    public ServerConnection(ProjectServer server, Socket client, Connection conn) {
        socketModel = new SocketModel(client);
        selfConn = conn;
        mServer = server;
        this.start();
    }

    public void run() {
        data = (Data) socketModel.FromClient();
        if (data.getType() == yarin.loginData) serverLogin();
        if (data.getType() == yarin.registerData) serverRegiste();
        if (data.getType() == yarin.DContactRequest) serverContactRequest();
        if (data.getType() == yarin.DInfoRequest) serverInfoRequest();
        if (data.getType() == yarin.DAdminReuqest) serverAdminRequest();
        if (data.getType() == yarin.DPlanRequest) serverPlanRequest();
        if (data.getType() == yarin.DRefresh) serverRefresh();
    }

    private void serverLogin() {
        LoginData mLoginData = (LoginData) data;
        SLoginModel mLogModel = new SLoginModel(mLoginData, selfConn);
        Data mData = new Data();
        if (mLogModel.isLoginSuccess()) {
            if (mLoginData.getAdmin() == yarin.isAdmin) serverAdminInformation(mLoginData); else serverInformation(mLoginData);
        } else {
            mData.setType(yarin.fail);
            socketModel.ToClient(mData);
            if (mLogModel.isLoginSuccess()) serverInformation(mLoginData);
            serverExit();
        }
    }

    private void serverRegiste() {
        RegisterData mRegisterData = (RegisterData) data;
        SRegisterModel mRegModel = new SRegisterModel(mRegisterData, selfConn);
        Data mData = new Data();
        if (!mRegModel.isSuccess()) {
            mData.setType(yarin.fail);
        } else mData.setType(mRegModel.getId());
        socketModel.ToClient(mData);
        serverExit();
    }

    private void serverInformation(LoginData data) {
        SDPersonModel mSDPersonModel = new SDPersonModel(data, selfConn);
        DPerson mDPerson = new DPerson();
        if (mSDPersonModel.isOpSuccess()) {
            mDPerson = mSDPersonModel.getmDPerson();
            mDPerson.setType(yarin.success);
        } else {
            mDPerson.setType(yarin.fail);
        }
        socketModel.ToClient(mDPerson);
        serverExit();
    }

    private void serverRefresh() {
        DRefresh refreshData = (DRefresh) data;
        LoginData mData = new LoginData();
        mData.setId(refreshData.getMyID());
        if (refreshData.getIsAdmin() == yarin.isAdmin) serverAdminInformation(mData);
        if (refreshData.getIsAdmin() == yarin.notAdmin) serverInformation(mData);
        serverExit();
    }

    private void serverAdminInformation(LoginData data) {
        SDAdminPersonModel mSDAdminPersonModel = new SDAdminPersonModel(data, selfConn);
        DPerson mDPerson = new DPerson();
        if (mSDAdminPersonModel.isOpSuccess()) {
            mDPerson = mSDAdminPersonModel.getmDPerson();
            mDPerson.setType(yarin.success);
        } else {
            mDPerson.setType(yarin.fail);
        }
        socketModel.ToClient(mDPerson);
        serverExit();
    }

    private void serverContactRequest() {
        DContactRequest mDContactRequest = (DContactRequest) data;
        SDContactModel mSDContactModel = new SDContactModel(mDContactRequest, selfConn);
        if (mSDContactModel.isOpSuccess()) {
            switch(mDContactRequest.getRequest()) {
                case yarin.search:
                    DContactList contactData = mSDContactModel.getContactData();
                    contactData.setType(yarin.success);
                    socketModel.ToClient(contactData);
                    break;
                case yarin.insert:
                    Data insertData = new Data();
                    insertData.setType(yarin.success);
                    socketModel.ToClient(insertData);
                    break;
                case yarin.delete:
                    Data deleteData = new Data();
                    deleteData.setType(yarin.success);
                    socketModel.ToClient(deleteData);
                    break;
                case yarin.edit:
                    Data editData = new Data();
                    editData.setType(yarin.success);
                    socketModel.ToClient(editData);
                    break;
                case yarin.contactgiven:
                    Data rightData = new Data();
                    rightData.setType(yarin.success);
                    socketModel.ToClient(rightData);
                    break;
            }
        } else {
            Data contactData = new Data();
            contactData.setType(yarin.fail);
            socketModel.ToClient(contactData);
        }
        serverExit();
    }

    private void serverAdminRequest() {
        DAdminRequest mDAdminRequest = (DAdminRequest) data;
        SDAdminRequestModel mAdminModel = new SDAdminRequestModel(mDAdminRequest, selfConn);
        Data mData = new Data();
        if (mAdminModel.isOpSuccess()) mData.setType(yarin.success); else mData.setType(yarin.fail);
        socketModel.ToClient(mData);
        serverExit();
    }

    private void serverInfoRequest() {
        DInfoRequest mInfoRequest = (DInfoRequest) data;
        SDInfoModel mSDInfoModel = new SDInfoModel(mInfoRequest, selfConn);
        Data mData = new Data();
        if (mSDInfoModel.isOpSuccess()) mData.setType(yarin.success); else mData.setType(yarin.fail);
        socketModel.ToClient(mData);
        serverExit();
    }

    private void serverPlanRequest() {
        DPlanRequest mPlanRequest = (DPlanRequest) data;
        SDPlanModel mSDPlanRequest = new SDPlanModel(mPlanRequest, selfConn);
        Data mData = new Data();
        if (mSDPlanRequest.isOpSuccess()) mData.setType(yarin.success); else mData.setType(yarin.fail);
        socketModel.ToClient(mData);
        serverExit();
    }

    private void serverExit() {
        mServer.recycleDBConnection(selfConn);
    }
}
