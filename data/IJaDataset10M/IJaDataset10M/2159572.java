package com.dsp.web;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.core.util.DspUtil;
import com.dsp.bean.Area;
import com.dsp.services.AreaService;

@Controller("AreasController")
public class AreasController {

    Logger logger = Logger.getLogger(DspUtil.Log4J);

    @RequestMapping(value = "/areas_sss")
    @ResponseBody
    public List<Area> sss(HttpServletRequest request, HttpServletResponse response) {
        String code = (String) request.getParameter("code");
        DetachedCriteria dc = DetachedCriteria.forClass(Area.class);
        if (DspUtil.isEmpty(code) == false) {
            dc.add(Restrictions.eq("parent", code));
        }
        List<Area> list = this.areaService.likeBy(dc);
        return list;
    }

    @Autowired
    private AreaService areaService;

    /**
	 * @return the areaService
	 */
    public AreaService getAreaService() {
        return areaService;
    }

    /**
	 * @param areaService the areaService to set
	 */
    public void setAreaService(AreaService areaService) {
        this.areaService = areaService;
    }
}
