package confreg.ejb.tests;

import javax.ejb.Local;

/**
 *
 * @author palesz
 */
@Local
public interface ConfigurationControlLocal {

    void removeConfig(confreg.ejb.domain.Configuration config);
}
