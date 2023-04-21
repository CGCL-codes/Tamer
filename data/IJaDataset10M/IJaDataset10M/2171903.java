package epm.admin.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.systop.core.service.BaseGenericsManager;
import epm.admin.model.Source;

@Service
@SuppressWarnings("unchecked")
public class SourceManager extends BaseGenericsManager<Source> {

    /**
	 * 根据项目id得到项目的Source实体
	 * 
	 * @param projectId
	 *          项目id
	 * @return Source 实体
	 */
    public Source getSource(Integer projectId) {
        List<Source> list = getDao().query("from Source s where s.project.id = ?", projectId);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
