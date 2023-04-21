package nl.hajari.wha.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nl.hajari.wha.domain.Constants;
import nl.hajari.wha.service.ConstantsService;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;

/**
 * 
 * 
 * @author Behrooz Nobakht [behrooz dot nobakht at gmail dot com]
 */
@Service
public class ConstantsServiceImpl extends AbstractService implements ConstantsService, ServletContextAware {

    private static final Map<String, String> defaults = new HashMap<String, String>();

    static {
        defaults.put(CONST_KEY_SALARY_BONUS_RATIO_PER_HOUR, "1.5");
        defaults.put(CONST_KEY_SALARY_TAX_RATIO, "0.6");
        defaults.put(CONST_KEY_EXPENSE_GAS_SUBSIDY_PER_KILOMETER, "0.19");
        defaults.put(CONST_KEY_EXPENSE_VAT, "0.19");
    }

    @Override
    public void save(Constants constants) {
        constants.persist();
    }

    @Override
    public void save(String key, String value) {
        Constants c = new Constants();
        c.setKey(key);
        c.setValue(value);
        save(c);
    }

    @Override
    public void update(Constants constants) {
        constants.merge();
    }

    @Override
    public List<Constants> loadAll() {
        return Constants.findAllConstantses();
    }

    @Override
    public Constants load(Long id) {
        return Constants.findConstants(id);
    }

    @Override
    public void delete(Long id) {
        load(id).remove();
    }

    @Override
    public boolean exists(String key) {
        return findByKey(key) != null;
    }

    @Override
    public Constants findByKey(String key) {
        List<Constants> all = Constants.findConstantsesByKeyEquals(key).getResultList();
        if (null == all || all.isEmpty()) {
            logger.debug("No constant found with key=" + key);
            return null;
        }
        if (all.size() > 1) {
            logger.error("More than one constants found with key=" + key);
            return null;
        }
        return all.get(0);
    }

    @Override
    public String findValue(String key) {
        Constants c = findByKey(key);
        if (null == c) {
            return null;
        }
        return c.getValue();
    }

    public Map<String, String> getDefaultValues() {
        return defaults;
    }

    public void setServletContext(javax.servlet.ServletContext servletContext) {
        for (String key : getDefaultValues().keySet()) {
            if (!exists(key)) {
                save(key, getDefaultValues().get(key));
            }
        }
    }

    @Override
    public Float findFloatValueByKey(String key) {
        String value = findValue(key);
        value = (value == null ? "0" : value);
        return Float.valueOf(value);
    }
}
