package passport.android.webservice.client;

import java.util.Hashtable;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

public class Passport implements KvmSerializable {

    public static Class PASSPORT_CLASS = Passport.class;

    private String _name = "";

    private String _user_name = "";

    private String _eMaiL = "";

    private String _password = "";

    public Passport() {
    }

    public Passport(SoapObject obj) {
        this._name = obj.getProperty("Name").toString();
        this._user_name = obj.getProperty("UserName").toString();
        this._eMaiL = obj.getProperty("eMaiL").toString();
        this._password = obj.getProperty("Password").toString();
    }

    public String get_name() {
        return _name;
    }

    public String get_user_name() {
        return _user_name;
    }

    public String get_eMaiL() {
        return _eMaiL;
    }

    public String get_pasword() {
        return _password;
    }

    public void set_name(String name) {
        _name = name;
    }

    public void set_user_name(String user_name) {
        _user_name = user_name;
    }

    public void set_eMaiL(String eMaiL) {
        _eMaiL = eMaiL;
    }

    public void set_password(String password) {
        _password = password;
    }

    @Override
    public Object getProperty(int index) {
        Object object = null;
        switch(index) {
            case 0:
                {
                    object = this._name;
                    break;
                }
            case 1:
                {
                    object = this._user_name;
                    break;
                }
            case 2:
                {
                    object = this._eMaiL;
                    break;
                }
            case 3:
                {
                    object = this._password;
                    break;
                }
        }
        return object;
    }

    @Override
    public int getPropertyCount() {
        return 4;
    }

    @Override
    public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo propertyInfo) {
        switch(index) {
            case 0:
                {
                    propertyInfo.name = "Name";
                    propertyInfo.type = PropertyInfo.STRING_CLASS;
                    break;
                }
            case 1:
                {
                    propertyInfo.name = "UserName";
                    propertyInfo.type = PropertyInfo.STRING_CLASS;
                    break;
                }
            case 2:
                {
                    propertyInfo.name = "eMaiL";
                    propertyInfo.type = PropertyInfo.STRING_CLASS;
                    break;
                }
            case 3:
                {
                    propertyInfo.name = "Password";
                    propertyInfo.type = PropertyInfo.STRING_CLASS;
                    break;
                }
        }
    }

    @Override
    public void setProperty(int index, Object obj) {
        switch(index) {
            case 0:
                {
                    this._name = obj.toString();
                    break;
                }
            case 1:
                {
                    this._user_name = obj.toString();
                    break;
                }
            case 2:
                {
                    this._eMaiL = obj.toString();
                    break;
                }
            case 3:
                {
                    this._password = obj.toString();
                    break;
                }
        }
    }
}
