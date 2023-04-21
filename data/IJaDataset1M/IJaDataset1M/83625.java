package com.oolong.account.web;

import java.math.BigDecimal;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.oolong.account.dal.OutitemDAO;
import com.oolong.account.dal.data.Outitem;
import com.oolong.account.model.OutItemModel;
import com.oolong.account.model.OutTypeModel;
import com.oolong.account.model.UserModel;
import com.oolong.account.model.factory.TypeFactory;
import com.oolong.account.service.OutManage;
import com.oolong.account.util.ParamterCheck;

@Controller
@RequestMapping("/editOutItem.do")
public class OutItemEditController {

    Logger logger = Logger.getLogger(OutItemEditController.class);

    @Autowired
    private OutManage outManage;

    @Autowired
    private TypeFactory typeFactory;

    @Autowired
    OutitemDAO outitemDAO;

    @Autowired
    private LoadGlobalParameter loadGlobalParameter;

    String message = " ";

    @RequestMapping(method = RequestMethod.GET)
    public String processOutItem(@RequestParam("outid") int outid, ModelMap model, HttpServletResponse response, HttpSession session) {
        message = " ";
        OutItemModel outItemModel;
        response.setContentType("text/html;charset=UTF-8");
        UserModel userModel = (UserModel) session.getAttribute("user");
        Outitem outitem = outitemDAO.selectByPrimaryKey(outid);
        if (outitem != null) {
            if (userModel.getId() != outitem.getUserId()) {
                message = "你无权访问该记录：" + outid;
                model.addAttribute("message", message);
                return "deleteOutItem";
            } else {
                outItemModel = outManage.querybyID(outid);
                model.addAttribute("outitem", outItemModel);
                model.addAttribute("outfathertypes", loadGlobalParameter.globaloutfathertypes);
                model.addAttribute("outsontypesList", loadGlobalParameter.globaloutsontypes);
                model.addAttribute("message", message);
            }
        } else {
            message = "你无权访问该记录:" + outid;
            model.addAttribute("message", message);
            return "deleteOutItem";
        }
        return "editOutItem";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String processOutItem(@RequestParam("outid") int outid, @RequestParam("money") String money, @RequestParam("sontype") String sontype, @RequestParam("memo") String memo, HttpServletResponse response, HttpSession session, ModelMap model) {
        message = " ";
        response.setContentType("text/html;charset=UTF-8");
        UserModel userModel = (UserModel) session.getAttribute("user");
        Outitem outitem = outitemDAO.selectByPrimaryKey(outid);
        if (outitem != null) {
            if (userModel.getId() != outitem.getUserId()) {
                message = "你无权修改该记录：" + outid;
                model.addAttribute("message", message);
                return "deleteOutItem";
            } else {
                OutItemModel outItemModel = new OutItemModel();
                outItemModel.setId(outid);
                BigDecimal bigmoney = null;
                try {
                    bigmoney = new BigDecimal(money);
                    outItemModel.setMoney(bigmoney);
                } catch (NumberFormatException e) {
                    message = "传入的金额不正确";
                    model.addAttribute("message", message);
                    return "deleteOutItem";
                }
                if (ParamterCheck.checkMemo(memo)) {
                    outItemModel.setMemo(memo);
                } else {
                    message = "传入的备注信息不正确，不得大于128个字符";
                    model.addAttribute("message", message);
                    return "deleteOutItem";
                }
                int sonid = Integer.valueOf(sontype).intValue();
                if (!ParamterCheck.towTypeCheck(sonid)) {
                    message = "传入的支出类型不正确，请不要篡改为不存在的支出类型";
                    model.addAttribute("message", message);
                    return "deleteOutItem";
                }
                OutTypeModel outTypeModel = typeFactory.getIOutTypeModel(sonid);
                outItemModel.setUser(userModel);
                outItemModel.setOutType(outTypeModel);
                boolean updateresult = outManage.update(outItemModel);
                outItemModel = outManage.querybyID(outid);
                model.addAttribute("outitem", outItemModel);
                model.addAttribute("outfathertypes", loadGlobalParameter.globaloutfathertypes);
                model.addAttribute("outsontypesList", loadGlobalParameter.globaloutsontypes);
                if (updateresult) {
                    message = "修改成功，ID为：" + outid;
                    model.addAttribute("message", message);
                } else {
                    message = "修改失败，请联系管理员";
                    model.addAttribute("message", message);
                    return "deleteOutItem";
                }
            }
        } else {
            message = "你无权修改该记录:" + outid;
            model.addAttribute("message", message);
            return "deleteOutItem";
        }
        return "editOutItem";
    }
}
