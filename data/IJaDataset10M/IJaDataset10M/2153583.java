package assets.basic.webapp;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import assets.basic.model.PublicAsset;
import assets.basic.service.PublicAssetManager;
import assets.company.model.Yard;
import com.systop.core.webapp.struts2.action.DefaultCrudAction;

/**
 * 公共设施Action
 * @author Lunch
 */
@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PublicAssetAction extends DefaultCrudAction<PublicAsset, PublicAssetManager> {

    /** 院落ID */
    private Integer yardId;

    /**
	 * 保存公共设施，如果对应院落不为空则为yardId赋值
	 */
    public String save() {
        if (getModel().getYard() != null) {
            yardId = getModel().getYard().getId();
        }
        return super.save();
    }

    /**
	 * 公共设施列表
	 */
    public String index() {
        if (yardId != null) {
            String hql = "from PublicAsset pa where pa.yard.id = ?";
            items = getManager().query(hql, yardId);
            getRequest().setAttribute("yard", getYardById(yardId));
        } else {
            items = getManager().get();
        }
        return INDEX;
    }

    /**
	 * 返回编辑页面(新建页面)。如果yardId不为空则表示添加该院落的公共设施
	 */
    public String edit() {
        if (yardId != null) {
            getRequest().setAttribute("yard", getYardById(yardId));
            getModel().setYard(getYardById(yardId));
        }
        return INPUT;
    }

    /**
	 * 根据Id得到公司信息
	 * @param id
	 * @return
	 */
    private Yard getYardById(Integer id) {
        return getManager().getDao().get(Yard.class, id);
    }

    public Integer getYardId() {
        return yardId;
    }

    public void setYardId(Integer yardId) {
        this.yardId = yardId;
    }
}
