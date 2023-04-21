package com.zpyr.mvc.controller;

import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import com.zpyr.common.StringUtil;
import com.zpyr.mvc.vo.Carc_info;

public class CarcMultiCont extends MultiActionController {

    private final String viewerRoot = "carc/";
}
