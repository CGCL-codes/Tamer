package com.companyname.common.util.mvc.valid;

import org.apache.log4j.Logger;
import org.jdom.*;
import org.jdom.output.*;
import org.jdom.input.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.companyname.common.util.mvc.config.*;

/**
 * 校验器之间的逻辑关系操作
 * <bc operator=""> operator = "and" "or" "not"
 *    <validator />
 *    <bc operator=""></bc> ...
 * </bc>
 * created by amon 2005-4-6 22:29
 */
public class BooleanCalculator {

    static Logger logger = Logger.getLogger(BooleanCalculator.class);

    private Element fieldEle;

    private Object fieldValue;

    private ValidatorConfigReader valiConfiger;

    private DataSource ds;

    private Map utils;

    private static Map cacheOfValidator;

    static {
        cacheOfValidator = new HashMap();
    }

    public static Object getValidatorInst(String key) throws ClassNotFoundException, ClassCastException, InstantiationException, IllegalAccessException {
        Object vo = cacheOfValidator.get(key);
        if (vo == null) {
            Class cl = Class.forName(key);
            logger.debug("实例化" + key);
            vo = cl.newInstance();
            synchronized (cacheOfValidator) {
                cacheOfValidator.put(key, vo);
            }
        } else {
            logger.debug("取得缓存" + key);
        }
        return vo;
    }

    public BooleanCalculator(Element fieldEle, Object fieldValue, DataSource ds, Map utils) throws XmlFormatException {
        this.fieldEle = fieldEle;
        this.fieldValue = fieldValue;
        this.ds = ds;
        this.utils = utils;
        this.valiConfiger = new ValidatorConfigReader();
    }

    /**
         * 开始运算
         */
    public boolean start() throws ConfigFileFormatException, ClassNotFoundException, ClassCastException, InstantiationException, XmlFormatException, NoSuchElementException, IllegalAccessException, InitValidatorException {
        logger.debug("1.  field根格式检查");
        if (this.fieldEle.getChild("bcs") == null) {
            return true;
        }
        int bcCount = 0;
        int vaCount = 0;
        Element bcEle = null;
        List subEles = this.fieldEle.getChild("bcs").getChildren();
        for (int i = 0; i < subEles.size(); i++) {
            Element ele = (Element) subEles.get(i);
            bcEle = ele;
            if (ele.getName().toLowerCase().equals("bc")) {
                bcCount++;
                continue;
            }
            if (ele.getName().toLowerCase().equals("validator")) {
                vaCount++;
                continue;
            }
        }
        if (bcCount > 1) {
            throw new ConfigFileFormatException("<bcs>下面的<bc>标签个数不能超过1个");
        }
        if (vaCount > 1) {
            throw new ConfigFileFormatException("<bcs>下面的<validator>标签个数不能超过1个,<validator>必须独立存在或者内嵌在<bc>");
        }
        if (bcCount > 0 && vaCount > 0) {
            throw new ConfigFileFormatException("<bcs>下面不能同时出现<bc>、<validator>这两个标签,<validator>必须独立存在或者内嵌在<bc>");
        }
        logger.debug("2. 开始运算");
        return this.eleBC(bcEle);
    }

    /**
         * <bc> or <validator> 逻辑运算
         */
    private boolean eleBC(Element ele) throws ConfigFileFormatException, ClassNotFoundException, ClassCastException, InstantiationException, XmlFormatException, NoSuchElementException, IllegalAccessException, InitValidatorException {
        if (ele.getName().toLowerCase().equals("bc")) {
            logger.debug("bc运算");
            return this.bcBC(ele);
        }
        if (ele.getName().toLowerCase().equals("validator")) {
            String validatorId = ele.getAttributeValue("ref");
            logger.debug("查找校验器： " + validatorId);
            Element valiEle = this.valiConfiger.getValidatorEle(validatorId);
            logger.debug("查找校验类");
            AbstractValidator av = (AbstractValidator) this.getValidatorInst(valiEle.getChild("class").getText());
            logger.debug("初始化校验器");
            List params = ele.getChildren("param");
            Object[] initValues = new Object[params.size()];
            for (int i = 0; i < params.size(); i++) {
                Element e = (Element) params.get(i);
                String from = e.getAttributeValue("from");
                String key = e.getAttributeValue("key");
                if (!(from == null || key == null)) {
                    initValues[i] = this.ds.getValue(from, key);
                } else {
                    initValues[i] = e.getText();
                }
                Object utilObj = this.utils.get(initValues[i]);
                if (utilObj != null) {
                    logger.info("取到util" + initValues[i] + "对象啦");
                    initValues[i] = utilObj;
                }
            }
            logger.debug("开始校验");
            boolean rs = av.validing(this.fieldValue, initValues);
            logger.debug("校验结果：　" + rs);
            return rs;
        }
        throw new ConfigFileFormatException("非法定义标签：" + ele.getName() + " 只允许<bc> or <validator>");
    }

    /**
         * <bc>逻辑运算
         */
    private boolean bcBC(Element bcEle) throws ConfigFileFormatException, ClassNotFoundException, ClassCastException, InstantiationException, XmlFormatException, NoSuchElementException, IllegalAccessException, InitValidatorException {
        boolean[] bcrs = new boolean[bcEle.getChildren().size()];
        List subEles = bcEle.getChildren();
        for (int i = 0; i < subEles.size(); i++) {
            Element ele = (Element) subEles.get(i);
            bcrs[i] = this.eleBC(ele);
        }
        return this.bc(bcrs, bcEle.getAttributeValue("operator"));
    }

    /**
         * bc运算
         *
         */
    private boolean bc(boolean[] bcrs, String bcOperator) throws ConfigFileFormatException {
        if (bcOperator.toLowerCase().equals("and")) {
            return this.and(bcrs);
        }
        if (bcOperator.toLowerCase().equals("or")) {
            return this.or(bcrs);
        }
        if (bcOperator.toLowerCase().equals("not")) {
            return this.not(bcrs);
        }
        throw new ConfigFileFormatException("非法逻辑运算符<bc operator='" + bcOperator + "'>.目前只支持'or'、'and'、'not'");
    }

    private boolean and(boolean[] bcrs) throws ConfigFileFormatException {
        boolean br = true;
        for (int i = 0; i < bcrs.length; i++) {
            logger.debug(br + " and " + bcrs[i]);
            br = br & bcrs[i];
        }
        return br;
    }

    private boolean or(boolean[] bcrs) throws ConfigFileFormatException {
        boolean br = false;
        for (int i = 0; i < bcrs.length; i++) {
            logger.debug(br + " or " + bcrs[i]);
            br = br | bcrs[i];
        }
        return br;
    }

    private boolean not(boolean[] bcrs) throws ConfigFileFormatException {
        if (bcrs.length != 1) throw new ConfigFileFormatException("<bc operator='not'>子元素有且只有1个");
        return !bcrs[0];
    }
}
