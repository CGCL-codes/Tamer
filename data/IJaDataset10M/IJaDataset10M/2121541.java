package epm.admin.service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.systop.common.modules.security.user.model.User;
import com.systop.core.service.BaseGenericsManager;
import epm.project.model.Project;

/**
 * ProjectManager管理类
 * 
 * @author delphi
 * 
 */
@Service
@SuppressWarnings("unchecked")
public class ProjectMembersManager extends BaseGenericsManager<User> {

    /**
	 * 根据id得到Project实体
	 * 
	 * @param projectId
	 *          Project id
	 * @return Project实体
	 */
    public Project getProjectById(Integer projectId) {
        String hql = "select p from Project p where p.id = ?";
        List<Project> list = getDao().query(hql, projectId);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public User getUserById(Integer userId) {
        User user = getDao().get(User.class, userId);
        return user;
    }

    /**
	 * 添加项目成员方法
	 * 
	 * @param idSet
	 *          项目成员idSet集合
	 * @param projectId
	 *          工程id
	 */
    @Transactional
    public void addMembers(Set<Integer> idSet, Integer projectId) {
        Project project = getDao().get(Project.class, projectId);
        User user = null;
        for (Integer id : idSet) {
            user = getDao().get(User.class, id);
            project.getMembers().add(user);
            user.getFavorites().add(project);
        }
        getDao().save(project);
        getDao().save(user);
    }

    /**
	 * 删除项目成员方法
	 * 
	 * @param projectId
	 *          项目id
	 * @param idSet
	 *          成员id的Set集合
	 */
    @Transactional
    public void removeMembers(Integer projectId, Set<Integer> idSet) {
        Project project = getDao().get(Project.class, projectId);
        for (Integer id : idSet) {
            User user = getDao().get(User.class, id);
            project.getMembers().remove(user);
        }
        getDao().save(project);
    }

    /**
	 * 得到当前工程下所有成员方法
	 * 
	 * @param projectId
	 *          工程id
	 * @return 保存所有工程成员搭Set集合
	 */
    public Set<User> getProjectMembers(Serializable projectId) {
        Set<User> members = getDao().get(Project.class, projectId).getMembers();
        return members;
    }

    /**
	 * 得到当前工程owner方法
	 * 
	 * @param projectId
	 *          当前工程id
	 * @return 返回当前工程owner
	 */
    public User getProjectOwner(Serializable projectId) {
        User owner = getDao().get(Project.class, projectId).getOwner();
        return owner;
    }
}
