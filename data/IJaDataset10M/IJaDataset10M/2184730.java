package com.faithbj.shop.web.controller.admin;

import java.util.Set;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.faithbj.shop.model.configuration.Pager;
import com.faithbj.shop.model.entity.PresentCard;
import com.faithbj.shop.model.entity.PresentCardCategory;
import com.faithbj.shop.service.PresentCardCategoryService;
import com.faithbj.shop.service.PresentCardService;

@Controller
@RequestMapping("/faith/presentcard_category")
public class AdminPresentCardCategoryController extends BaseAdminController {

    private static final long serialVersionUID = -13900679721036437L;

    @Resource
    PresentCardService presentCardService;

    private final String HAS_CHILDREN_CATEGORY = "此礼品卡分类下存在礼品卡，删除失败!";

    @Resource
    PresentCardCategoryService presentCardCategoryService;

    private final String OPERRATE_RETURN_URL = "faith/presentcard_category/list";

    @RequestMapping("/list")
    public String list(ModelMap map) {
        Pager pager = presentCardCategoryService.findByPager(super.pager);
        map.put("pager", pager);
        return "admin/presentcard_category_list";
    }

    @RequestMapping("/{id}/delete")
    public String delete(@PathVariable String id, ModelMap map) {
        PresentCardCategory pc = presentCardCategoryService.load(id);
        Set<PresentCard> presentCardSet = pc.getPresentCardSet();
        if (presentCardSet != null && presentCardSet.size() > 0) {
            map.put("errorMessages", HAS_CHILDREN_CATEGORY);
            return ERROR;
        }
        presentCardCategoryService.delete(id);
        map.put("redirectUrl", OPERRATE_RETURN_URL);
        return SUCCESS;
    }
}
