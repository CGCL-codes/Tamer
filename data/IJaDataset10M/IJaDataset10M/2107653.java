package pl.ehotelik.portal.web.controller.ajax;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.ehotelik.portal.domain.City;
import pl.ehotelik.portal.domain.Country;
import pl.ehotelik.portal.domain.Province;
import pl.ehotelik.portal.exception.ServiceException;
import pl.ehotelik.portal.service.AddressService;
import pl.ehotelik.portal.web.controller.ControllerConstants;
import pl.ehotelik.portal.web.ui.hotel.CityUI;
import pl.ehotelik.portal.web.ui.hotel.CountryUI;
import pl.ehotelik.portal.web.controller.SimpleViewController;
import pl.ehotelik.portal.web.ui.hotel.ProvinceUI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mkr
 * Date: Aug 19, 2010
 * Time: 11:32:39 PM
 * This is a representation of AddressAjaxController object.
 */
@Controller
@RequestMapping(ControllerConstants.MappingContext.ADDRESS_AJAX_CONTROLLER)
public class AddressAjaxController extends SimpleViewController {

    private static final Logger logger = Logger.getLogger(AddressAjaxController.class);

    @Autowired
    private AddressService addressService;

    @RequestMapping(value = "/countryList", method = RequestMethod.GET)
    @ResponseBody
    public List<CountryUI> getCountryList() {
        List<Country> countryList;
        List<CountryUI> countryUIList;
        if (logger.isDebugEnabled()) {
            logger.debug("<------ getCountryList");
        }
        try {
            countryList = this.addressService.loadAllCountries();
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("country list size: %d", countryList.size()));
            }
        } catch (ServiceException e) {
            logger.warn(String.format("Country list couldn't be load correctly. [%s]", e.getMessage()));
            countryList = new ArrayList<Country>();
        }
        countryUIList = new ArrayList<CountryUI>();
        for (Country country : countryList) {
            countryUIList.add(new CountryUI(String.valueOf(country.getId()), country.getName()));
        }
        if (logger.isDebugEnabled()) {
            logger.debug("------> getCountryList");
        }
        return countryUIList;
    }

    @RequestMapping(value = "/provinceList", method = RequestMethod.GET)
    @ResponseBody
    public List<ProvinceUI> loadProvinceList(@RequestParam String id) {
        Country country;
        List<ProvinceUI> provinceUIList;
        Long countryId;
        if (logger.isDebugEnabled()) {
            logger.debug("<------ loadProvinceList");
        }
        try {
            countryId = Long.parseLong(id);
            country = this.addressService.loadCountry(countryId);
            provinceUIList = new ArrayList<ProvinceUI>();
            for (Province province : country.getProvinces()) {
                provinceUIList.add(new ProvinceUI(String.valueOf(province.getId()), province.getName()));
            }
        } catch (NumberFormatException e) {
            logger.warn(String.format("Number format exception: %s", id));
            provinceUIList = new ArrayList<ProvinceUI>();
        } catch (ServiceException e) {
            logger.warn(String.format("Country couldn't be load: %s", id));
            provinceUIList = new ArrayList<ProvinceUI>();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("------> loadProvinceList");
        }
        return provinceUIList;
    }

    @RequestMapping(value = "/cityList", method = RequestMethod.GET)
    @ResponseBody
    public List<CityUI> loadCityList(@RequestParam String id) {
        Province province;
        List<CityUI> cityUIList;
        Long provinceId;
        if (logger.isDebugEnabled()) {
            logger.debug("<------ loadCityList");
        }
        try {
            provinceId = Long.parseLong(id);
            province = this.addressService.loadProvince(provinceId);
            cityUIList = new ArrayList<CityUI>();
            for (City city : province.getCities()) {
                cityUIList.add(new CityUI(String.valueOf(city.getId()), city.getName()));
            }
        } catch (NumberFormatException e) {
            logger.warn(String.format("Number format exception: %s", id));
            cityUIList = new ArrayList<CityUI>();
        } catch (ServiceException e) {
            logger.warn(String.format("Province couldn't be load: %s", id));
            cityUIList = new ArrayList<CityUI>();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("------> loadCityList");
        }
        return cityUIList;
    }
}
