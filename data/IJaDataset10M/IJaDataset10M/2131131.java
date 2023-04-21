package org.swxjava.examples;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Map;

public class Simple {

    public Simple() {
        super();
    }

    public Object echoData(Object data) {
        return data;
    }

    public User echoUser(User user) {
        Address address = new Address();
        address.setCity("Setagaya");
        address.setCountry("jp");
        address.setPostalCode("123-2345");
        address.setPrefecture("Tokyo");
        user.setAge(20);
        user.setAddress(address);
        return user;
    }

    public Object[] getNestedArray() {
        Object[] nestedArray = new Object[3];
        nestedArray[0] = "A";
        nestedArray[1] = "B";
        nestedArray[2] = "C";
        Object[] param = new Object[3];
        param[0] = "test";
        param[1] = nestedArray;
        param[2] = null;
        return param;
    }

    public Map getSimpleMap() {
        Map<String, Object> param = new LinkedHashMap<String, Object>();
        param.put("x", 10);
        param.put("y", new Date());
        param.put("z", 20);
        return param;
    }

    public Object[] getSimpleArray() {
        Object[] param = new Object[3];
        param[0] = new Date();
        param[1] = 10;
        param[2] = 20;
        return param;
    }

    /**
	 * {familyName:"Matsui",givenName:"Ken",address:{zipCode:"###-####",country:"Japan",prefecture:"Tokyo"}}
	 * @return
	 */
    public Map getNestedMap() {
        Map<String, Object> address = new LinkedHashMap<String, Object>();
        address.put("zipCode", "###-####");
        address.put("country", "Japan");
        address.put("prefecture", "Tokyo");
        Map<String, Object> param = new LinkedHashMap<String, Object>();
        param.put("familyName", "Matsui");
        param.put("givenName", "Ken");
        param.put("address", address);
        return param;
    }

    public Date getCurrentDate() {
        Calendar cal = new GregorianCalendar();
        Date date = new Date();
        cal.setTime(date);
        cal.set(2007, 0, 1, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
