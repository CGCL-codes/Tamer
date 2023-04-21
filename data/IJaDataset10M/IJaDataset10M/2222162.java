package com.zhongkai.web.control.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.zhongkai.model.book.TDjCcdjxx;
import com.zhongkai.service.book.TransforService;

@Controller
@RequestMapping(value = "/system/ajax/moveout.do")
public class MoveoutAjaxControl {

    private TransforService transforService;

    private Logger log = Logger.getLogger(this.getClass());

    @Resource
    public void setTransforService(TransforService transforService) {
        this.transforService = transforService;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(params = "method=findccpzh")
    public String findccpzh(HttpServletResponse response, String ccpzh) throws IOException {
        try {
            PrintWriter out = response.getWriter();
            if (ccpzh == null || "".equals(ccpzh)) {
                out.write("<font color=\"red\">车牌号码不能为空!</font>");
                return null;
            }
            List<TDjCcdjxx> tDjCcdjxxList = transforService.findByHql("from TDjCcdjxx where ccpzh=?", new Object[] { ccpzh });
            if (tDjCcdjxxList == null || tDjCcdjxxList.size() == 0 || tDjCcdjxxList.isEmpty()) {
                out.write("<font color=\"green\">该车牌号码有效!</font>");
                return null;
            } else {
                out.write("<font color=\"red\">该车牌号码已登记!</font>");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return null;
        }
    }
}
