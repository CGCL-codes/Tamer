package com.uside.ido.web.admin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springside.modules.web.struts2.Struts2Utils;
import com.uside.core.base.action.BaseAction;
import com.uside.core.constant.SysConstant;
import com.uside.core.util.BeanUtils;
import com.uside.ido.entity.sys.Area;
import com.uside.ido.entity.sys.Country;
import com.uside.ido.entity.sys.Province;
import com.uside.ido.form.admin.base.ContinentForm;
import com.uside.ido.form.admin.base.ProvinceForm;
import com.uside.ido.service.sys.AreaService;
import com.uside.ido.service.sys.CountryService;
import com.uside.ido.service.sys.ProvinceService;

/**
 * <p>
 * 类功能描述:
 * </p>
 * 
 * @author 王桂元(Mike.Wang)
 * @创建日期：2009-6-16 上午10:41:25
 * @copyright (c) 2009-2009 王桂元. 保留所有权利.
 */
@Scope("prototype")
@Results({ @Result(name = "list", location = "/WEB-INF/template/admin/base/province_list.ftl"), @Result(name = "add", location = "/WEB-INF/template/admin/base/province_add.ftl"), @Result(name = "edit", location = "/WEB-INF/template/admin/base/province_edit.ftl") })
public class ProvinceAction extends BaseAction {

    private String id;

    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private AreaService areaService;

    private int pageSize = 10;

    private ProvinceForm bean = new ProvinceForm();

    @Action(value = "listProvince")
    public String listProvince() throws Exception {
        page = provinceService.listProvinceForPage(pageNo, pageSize);
        return "list";
    }

    @Action(value = "addProvince")
    public String addProvince() throws Exception {
        List countryList = countryService.listAllCountry();
        Struts2Utils.getRequest().setAttribute("countryList", countryList);
        List areaList = new ArrayList();
        if (countryList != null && countryList.size() > 0) {
            Country country = (Country) countryList.get(0);
            areaList = areaService.listAreaByCountry(country.getId());
        }
        Struts2Utils.getRequest().setAttribute("areaList", areaList);
        return "add";
    }

    @Action(value = "createProvince")
    public String createProvince() throws Exception {
        Province province = new Province();
        BeanUtils.copyProperties(bean, province);
        String countryId = bean.getCountryId();
        String areaId = bean.getAreaId();
        Country country = countryService.findByPk(countryId);
        Area area = areaService.findByPk(areaId);
        province.setCountry(country);
        province.setArea(area);
        provinceService.createProvince(province);
        return listProvince();
    }

    @Action(value = "editProvince")
    public String editProvince() throws Exception {
        String provinceId = id;
        Province province = provinceService.findByPk(provinceId);
        BeanUtils.copyProperties(province, bean);
        String countryId = province.getCountry().getId();
        bean.setAreaId(province.getArea().getId());
        bean.setCountryId(countryId);
        bean.setProvinceId(id);
        List countryList = countryService.listAllCountry();
        List areaList = areaService.listAreaByCountry(countryId);
        Struts2Utils.getRequest().setAttribute("countryList", countryList);
        Struts2Utils.getRequest().setAttribute("areaList", areaList);
        return "edit";
    }

    @Action(value = "updateProvince")
    public String updateProvince() throws Exception {
        String provinceId = bean.getProvinceId();
        Province province = provinceService.findByPk(provinceId);
        String countryId = bean.getCountryId();
        String areaId = bean.getAreaId();
        Country country = countryService.findByPk(countryId);
        Area area = areaService.findByPk(areaId);
        BeanUtils.copyProperties(bean, province);
        province.setArea(area);
        province.setCountry(country);
        provinceService.updateProvince(province);
        return listProvince();
    }

    @Action(value = "deleteProvince")
    public String deleteProvince() throws Exception {
        String provinceId = id;
        Province province = provinceService.findByPk(provinceId);
        provinceService.deleteProvince(province);
        return listProvince();
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public ProvinceService getProvinceService() {
        return provinceService;
    }

    public void setProvinceService(ProvinceService provinceService) {
        this.provinceService = provinceService;
    }

    public ProvinceForm getBean() {
        return bean;
    }

    public void setBean(ProvinceForm bean) {
        this.bean = bean;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
