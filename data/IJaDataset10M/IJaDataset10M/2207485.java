package halo.util.validator;

import java.util.HashMap;
import java.util.Map;

/**
 * 验证器工厂
 * 
 * @author akwei
 */
public class ValidatorCreator {

    private final Map<String, Validator> map = new HashMap<String, Validator>();

    private static ValidatorCreator validatorCreator;

    static {
        validatorCreator = new ValidatorCreator();
        validatorCreator.register("string", new StringValidator());
        validatorCreator.register("number", new NumberValidator());
        validatorCreator.register("date", new DateValidator());
    }

    /**
	 * 获得默认的验证器工厂
	 * 
	 * @return
	 */
    public static ValidatorCreator getDefaultValidatorCreator() {
        return validatorCreator;
    }

    /**
	 * 可以注册新的验证器
	 * 
	 * @param key
	 * @param validator
	 */
    public void register(String key, Validator validator) {
        map.put(key, validator);
    }

    public Validator getValidator(String key) {
        return map.get(key);
    }
}
