package com.model;

import java.util.Hashtable;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import controller.Auth;

/**
 * This class is extends the server model's Auth
 * in order to get a serializable Auth in this application
 */
public class KAuth extends Auth implements KvmSerializable {

    public KAuth() {
    }

    @Override
    public Object getProperty(int arg0) {
        switch(arg0) {
            case 0:
                return this.getSession();
            case 1:
                return this.getUserID();
        }
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 2;
    }

    @Override
    public void getPropertyInfo(int arg0, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo arg2) {
        switch(arg0) {
            case 0:
                arg2.type = PropertyInfo.INTEGER_CLASS;
                arg2.name = "session";
                break;
            case 1:
                arg2.type = PropertyInfo.INTEGER_CLASS;
                arg2.name = "userID";
                break;
        }
    }

    @Override
    public void setProperty(int arg0, Object arg1) {
        switch(arg0) {
            case 0:
                this.setSession(Integer.parseInt(arg1.toString()));
                break;
            case 1:
                this.setUserID(Integer.parseInt(arg1.toString()));
                break;
        }
    }
}
