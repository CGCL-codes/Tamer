package net.sf.xplanner.dao;

import java.io.Serializable;
import java.util.List;
import net.sf.xplanner.domain.view.UserStoryView;
import com.technoetic.xplanner.domain.Identifiable;

public interface ViewDao {

    Object getById(Class<? extends Identifiable> objectClass, Serializable id);

    public List<UserStoryView> getUserStories(Integer iterationId);
}
