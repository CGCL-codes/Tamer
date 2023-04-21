package com.zhongkai.web.control.book;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import com.zhongkai.model.book.TDjCcdjxx;
import com.zhongkai.model.book.TDjCcywcl;
import com.zhongkai.service.book.TransforService;
import com.zhongkai.tools.CheckWord;

@Controller
public class TransforControll {

    private Logger log = Logger.getLogger(this.getClass());

    private TransforService transforService;

    @Resource
    public void setTransforService(TransforService transforService) {
        this.transforService = transforService;
    }

    @RequestMapping(value = "/system/book/transfor_save.do")
    public String saveTransfor(Integer tDjCcdjxxLsccdjh, TDjCcdjxx tDjCcdjxx, TDjCcywcl tDjCcywcl, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            PrintWriter out = response.getWriter();
            transforService.saveTransfor_xx(tDjCcdjxxLsccdjh, tDjCcdjxx, tDjCcywcl, request);
            out.write(CheckWord.showMsg("过户操作成功!", "/system/search/unisearch.jsp?module=book/transforform.jsp", request));
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            PrintWriter out = response.getWriter();
            out.write(CheckWord.showMsg("过户操作时发生异常，请与管理员联系!", "/system/book/transforform.jsp?ccdjh=" + tDjCcdjxxLsccdjh, request));
        }
        return null;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }
}
