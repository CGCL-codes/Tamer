package com.corratech.ws.sugarcrm.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import com.corratech.ws.sugarcrm.Name_value;
import com.corratech.ws.sugarcrm.Set_entry_result;
import com.corratech.ws.sugarcrm.Set_relationship_value;

public class EmailsService extends BaseService {

    private String MODULE = "Emails";

    public EmailsService(String login, String password) throws Exception {
        super(login, password);
    }

    public String saveMessage(Emails emailMessage, Contacts contact) throws Exception {
        Set_entry_result rezult = null;
        Name_value[] valueList = convert(emailMessage);
        rezult = getService().set_entry(sessionId, MODULE, valueList);
        Set_relationship_value arg = new Set_relationship_value();
        arg.setModule1("Contacts");
        arg.setModule1_id(contact.getId());
        arg.setModule2("Emails");
        arg.setModule2_id(rezult.getId());
        getService().set_relationship(sessionId, arg);
        return rezult.getId();
    }

    private Name_value[] convert(Emails mail) {
        Method[] method = Emails.class.getMethods();
        Name_value valueList = null;
        List<Name_value> list = new ArrayList<Name_value>();
        for (int i = 0; i < method.length; i++) {
            String value = "";
            if (method[i].getName().substring(0, 3).equals("get") && (!method[i].getName().equals("getClass"))) {
                method[i].setAccessible(true);
                try {
                    value = (String) method[i].invoke(mail, null);
                } catch (Exception e) {
                    e.getMessage();
                }
                if (value != null) {
                    String name = method[i].getName().toString();
                    name = name.substring(3).toString();
                    name = name.substring(0, 1).toLowerCase().concat(name.substring(1).toString()).toString();
                    valueList = new Name_value();
                    valueList.setName(name);
                    valueList.setValue(value);
                    list.add(valueList);
                }
            }
        }
        Name_value[] rezult = new Name_value[list.size()];
        return list.toArray(rezult);
    }
}
