package com.uro.pola.codedd;

import java.util.Enumeration;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.uro.common.base.CiHashMap;
import egovframework.rte.fdl.property.EgovPropertyService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class CodeDataController {

    private static final Logger logger = LoggerFactory.getLogger(CodeDataController.class);

    @Resource(name = "codeDataService")
    private CodeDataService codeDataService;

    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;

    @RequestMapping(value = "/CodeDataList", method = RequestMethod.GET)
    public String jobListForm(Model model) throws Exception {
        return "codedata/codelist";
    }

    /**
	 * Simply selects the home view to render by returning its name.
	 */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @RequestMapping(value = "/CodeDataList", method = RequestMethod.POST)
    @ResponseBody
    public Map jobList(@RequestParam("page") String page, HttpServletRequest request, Model model) throws Exception {
        Enumeration<?> enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String key = (String) enumeration.nextElement();
            String[] values = request.getParameterValues(key);
            if (values != null) {
                System.out.println("======================\n\n" + key);
                System.out.println((values.length > 1) ? values : values[0]);
            }
        }
        Map<String, Object> param = new CiHashMap();
        if (page == null || "".equals(page)) page = "0";
        int pageNo = Integer.parseInt(page);
        param.put("pageSize", propertiesService.getInt("pageSize"));
        param.put("pageStart", propertiesService.getInt("pageSize") * pageNo);
        Map ret = codeDataService.getCodeList(param);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(System.out, ret);
        return ret;
    }

    @RequestMapping(value = "/insertcode", method = RequestMethod.POST)
    public String insertCode(@Valid CodeData codeData, BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println("this has eeor");
        }
        for (Object object : result.getAllErrors()) {
            if (object instanceof FieldError) {
                FieldError fieldError = (FieldError) object;
                System.out.println(fieldError.getField() + ":" + fieldError.getCode());
            }
            if (object instanceof ObjectError) {
                ObjectError objectError = (ObjectError) object;
            }
        }
        model.addAttribute("the", "22");
        return "codedata/insertcode";
    }
}
