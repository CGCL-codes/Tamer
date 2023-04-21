package sgep.services;

import java.util.List;
import sgep.po.Application;

/**
 *
 * @author mg
 */
public interface ApplicationService {

    List<Application> getAppList() throws Exception;

    Application getApplication(String id);
}
