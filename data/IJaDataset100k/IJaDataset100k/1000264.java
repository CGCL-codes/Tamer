package com.corratech.ws.sugarcrm.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.corratech.ws.sugarcrm.Get_entry_list_result;
import com.corratech.ws.sugarcrm.Name_value;

public class AccountsService extends BaseService {

    private String MODULE = "Accounts";

    private String[] LIST = { "id", "assigned_user_id", "assigned_user_name", "name", "parent_id", "account_type", "ownership", "industry", "industry", "phone_fax", "email1", "email2", "description", "phone_office", "phone_alternate", "website", "employees", "sic_code", "ticker_symbol", "shipping_address_street", "shipping_address_city", "account_name", "shipping_address_state", "shipping_address_postalcode", "shipping_address_country", "deleted" };

    public AccountsService(String login, String password) throws Exception {
        super(login, password);
    }

    public List<Accounts> getAccountsByEmails(List<String> emails) {
        List<Accounts> rezult = null;
        rezult = new ArrayList<Accounts>();
        String search = "";
        Iterator<String> iterator = emails.iterator();
        while (iterator.hasNext()) {
            String value = (String) iterator.next();
            search += "(accounts.email1 = '" + value + "' OR accounts.email2 = '" + value + "')";
            if (iterator.hasNext()) {
                search += " OR ";
            }
        }
        try {
            Get_entry_list_result rez = getService().get_entry_list(sessionId, MODULE, search, "", 0, LIST, 10, 0);
            for (int i = 0; i < rez.getEntry_list().length; i++) {
                rezult.add(convert(rez.getEntry_list()[i].getName_value_list()));
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return rezult;
    }

    public Accounts getAccountsByContact(Contacts contact) {
        String search = "accounts.id = '" + contact.getAccount_id() + "'";
        Accounts rezult = null;
        try {
            Get_entry_list_result rez = getService().get_entry_list(sessionId, MODULE, search, "", 0, LIST, 10, 0);
            rezult = convert(rez.getEntry_list()[0].getName_value_list());
        } catch (Exception e) {
            e.getMessage();
        }
        return rezult;
    }

    @SuppressWarnings("unchecked")
    private Accounts convert(Name_value[] list) {
        Accounts rez = new Accounts();
        for (int i = 0; i < list.length; i++) {
            Class[] setMethodArgsType = { java.lang.String.class };
            Object[] setMethodArgs = { new String(list[i].getValue().toString()) };
            try {
                String name = list[i].getName();
                name = name.substring(0, 1).toUpperCase().concat(name.substring(1)).toString();
                name = "set" + name;
                Method method = rez.getClass().getMethod(name, setMethodArgsType);
                method.setAccessible(true);
                method.invoke(rez, setMethodArgs);
            } catch (Exception e) {
                e.getMessage();
            }
        }
        return rez;
    }
}
