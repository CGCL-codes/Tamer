package epm.admin.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.systop.core.service.BaseGenericsManager;
import epm.admin.model.Label;

/**
 * Label管理类
 * 
 * @author LQ
 * 
 */
@Service
@SuppressWarnings("unchecked")
public class LabelManager extends BaseGenericsManager<Label> {

    /**
	 * 根据Lable所属的模块，得到Lable实例集合
	 * 
	 * @param forWhat
	 *          Lable所属模块名
	 * @return 符合查询条件的实体集合
	 */
    public List<Label> getLabelsByForWhat(String forWhat) {
        List<Label> labelList = getDao().query("from Label l where l.forWhat = ?", forWhat);
        return labelList;
    }

    /**
	 * 查询所有Label实体
	 * @return Label 实体集合
	 */
    public List<Label> getAllLabels() {
        List<Label> labelList = getDao().query("from Label");
        return labelList;
    }
}
