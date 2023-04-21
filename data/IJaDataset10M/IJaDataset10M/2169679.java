package com.core.md;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import javax.servlet.http.HttpSession;
import com.be.bo.GlobalParameter;
import com.be.vo.CountryVO;
import com.eshop.bo.Facade;

public class CountryMD extends BaseMD {

    Hashtable<Integer, CountryVO> ht;

    public CountryMD(HttpSession session) {
        super(session);
    }

    public CountryVO getCurrencyVO(int id) {
        CountryVO[] temp = facade.getCountryVO(id);
        if (temp != null && temp.length == 1) {
            return temp[0];
        }
        return null;
    }

    public List<CountryVO> getCountries() {
        CountryVO[] temp = facade.getCountryVO();
        ArrayList<CountryVO> result = new ArrayList<CountryVO>();
        for (int i = 0; i < temp.length; i++) {
            result.add(temp[i]);
        }
        return result;
    }

    public List<CountryVO> getSpecificCountries() {
        String countryListLimitation = uo.getFacade().getGlobalParameter("com.eshop.param.allowed.countries");
        CountryVO[] temp = facade.getCountrySpecific(countryListLimitation);
        ArrayList<CountryVO> result = new ArrayList<CountryVO>();
        for (int i = 0; i < temp.length; i++) {
            result.add(temp[i]);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public String getIsoCode(int id) {
        ht = (Hashtable<Integer, CountryVO>) session.getAttribute("md.countryHT");
        if (ht == null) {
            ht = new Hashtable<Integer, CountryVO>();
        }
        CountryVO temp = ht.get(new Integer(id));
        if (temp == null) {
            temp = getCurrencyVO(id);
            ht.put(new Integer(id), temp);
        }
        session.setAttribute("md.countryHT", ht);
        return temp.getA2();
    }
}
