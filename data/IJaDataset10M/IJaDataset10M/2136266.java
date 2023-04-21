package com.asoft.common.util.mvc.valid.imp._long;

import org.apache.log4j.Logger;
import com.asoft.common.util.mvc.valid.InitValidatorException;

/**
 * long 比较器 之 大于
 * 
 * created by amon 2005-4-19 20:41
 */
public class MoreThan extends IsLong {

    static Logger logger = Logger.getLogger(MoreThan.class);

    public boolean validing(Object param, Object[] initValues) throws InitValidatorException {
        if (!super.validing(param, initValues)) return false;
        try {
            logger.debug("2.1 数据类型校验 -- initValues");
            long equalValue = this.getEqualValue(initValues);
            logger.debug("2.2 数据类型校验 -- param");
            long paramValue = Long.parseLong((String) param);
            logger.info("3. 开始比较 " + paramValue + " > " + equalValue + " = " + (paramValue > equalValue));
            return (paramValue > equalValue);
        } catch (ClassCastException cce) {
            throw new InitValidatorException("参数传递异常：" + this.help());
        } catch (NumberFormatException nfe) {
            logger.error("非long型格式,return false");
            return false;
        }
    }
}
