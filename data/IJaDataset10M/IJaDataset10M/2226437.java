package test.granite.spring.service;

import java.util.Map;
import org.springframework.security.annotation.Secured;

/**
 * @author Franck WOLFF
 */
@Secured({ "ROLE_USER" })
public interface PeopleService {

    public Map<String, Object> find(Map<String, Object> filter, int first, int max, String order, boolean desc);
}
