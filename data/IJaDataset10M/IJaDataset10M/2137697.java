package org.cmsuite2.business.handler;

import it.ec.commons.web.ValidateException;
import java.io.IOException;
import java.util.Date;
import org.apache.log4j.Logger;
import org.cmsuite2.business.form.ConfigForm;
import org.cmsuite2.business.validator.ConfigValidator;
import org.cmsuite2.enumeration.ActivityType;
import org.cmsuite2.model.activity.Activity;
import org.cmsuite2.model.activity.ActivityDAO;
import org.cmsuite2.model.config.Config;
import org.cmsuite2.model.config.ConfigDAO;
import org.cmsuite2.model.country.Country;
import org.cmsuite2.model.country.CountryDAO;
import org.springframework.transaction.annotation.Transactional;

public class ConfigHandler {

    private static Logger logger = Logger.getLogger(ConfigHandler.class);

    private ActivityDAO activityDao;

    private ConfigDAO configDao;

    private CountryDAO countryDao;

    private ConfigForm configForm;

    private ConfigValidator configValidator;

    @Transactional(value = "main")
    public ConfigForm fillFormFields(long idToLoad) {
        if (logger.isInfoEnabled()) logger.info("fillFormFields[" + idToLoad + "]");
        Config dbConfig = configDao.findById(idToLoad);
        if (dbConfig != null) {
            configForm.setConfig(dbConfig);
            Country dbCountry = dbConfig.getCountry();
            if (dbCountry != null) configForm.setCountryId(dbCountry.getId());
        }
        configForm.setCountries(countryDao.findAll("name"));
        return configForm;
    }

    @Transactional(value = "main")
    public Config step1(long idToLoad, Config config, long countryId) throws ValidateException {
        if (logger.isInfoEnabled()) logger.info("step1[" + idToLoad + ", " + config + ", " + countryId + "]");
        Country dbCountry = countryDao.findById(countryId);
        if (dbCountry != null) config.setCountry(dbCountry);
        configValidator.validateStep1(config);
        if (idToLoad == 0) {
            activityDao.persistRequiresNew(new Activity(ActivityType.CREATE_Config, "", "", new Date()));
            configDao.persist(config);
            return config;
        } else {
            Config dbConfig = configDao.findById(idToLoad);
            if (dbConfig == null) {
                dbConfig = new Config();
                configDao.persist(dbConfig);
            }
            activityDao.persistRequiresNew(new Activity(ActivityType.UPDATE_Config, "", "", new Date()));
            dbConfig.setFirstName(config.getFirstName());
            dbConfig.setMiddleName(config.getMiddleName());
            dbConfig.setLastName(config.getLastName());
            dbConfig.setName(config.getName());
            dbConfig.setVatCode(config.getVatCode());
            dbConfig.setAddress(config.getAddress());
            dbConfig.setCity(config.getCity());
            dbConfig.setZipCode(config.getZipCode());
            dbConfig.setMail(config.getMail());
            dbConfig.setPhone(config.getPhone());
            dbConfig.setBankCode(config.getBankCode());
            dbConfig.setCountry(config.getCountry());
            dbConfig.setMailAddress(config.getMailAddress());
            dbConfig.setMailHost(config.getMailHost());
            dbConfig.setMailPort(config.getMailPort());
            dbConfig.setMailUsername(config.getMailUsername());
            dbConfig.setMailPassword(config.getMailPassword());
            dbConfig.setCurrencyType(config.getCurrencyType());
            dbConfig.setPriceDecimal(config.getPriceDecimal());
            dbConfig.setQuantityDecimal(config.getQuantityDecimal());
            dbConfig.setPercentageDecimal(config.getPercentageDecimal());
            dbConfig.setWeightDecimal(config.getWeightDecimal());
            return dbConfig;
        }
    }

    @Transactional(value = "main")
    public Config read(long idToLoad) {
        if (logger.isInfoEnabled()) logger.info("read[" + idToLoad + "]");
        activityDao.persistRequiresNew(new Activity(ActivityType.READ_Config, "", "", new Date()));
        return configDao.findById(idToLoad);
    }

    @Transactional(value = "main")
    public void delete(long idToLoad) throws IOException {
        if (logger.isInfoEnabled()) logger.info("delete[" + idToLoad + "]");
        Config dbConfig = configDao.findById(idToLoad);
        if (dbConfig != null) cleanRelated(dbConfig);
        activityDao.persistRequiresNew(new Activity(ActivityType.DELETE_Config, "", "", new Date()));
    }

    @Transactional(value = "main")
    private void cleanRelated(Config config) {
        if (logger.isInfoEnabled()) logger.info("cleanRelated[" + config + "]");
        configDao.remove(config);
    }

    public ConfigDAO getConfigDao() {
        return configDao;
    }

    public void setConfigDao(ConfigDAO configDao) {
        this.configDao = configDao;
    }

    public ConfigForm getConfigForm() {
        return configForm;
    }

    public void setConfigForm(ConfigForm configForm) {
        this.configForm = configForm;
    }

    public ConfigValidator getConfigValidator() {
        return configValidator;
    }

    public void setConfigValidator(ConfigValidator configValidator) {
        this.configValidator = configValidator;
    }

    public void setCountryDao(CountryDAO countryDao) {
        this.countryDao = countryDao;
    }

    public CountryDAO getCountryDao() {
        return countryDao;
    }

    public ActivityDAO getActivityDao() {
        return activityDao;
    }

    public void setActivityDao(ActivityDAO activityDao) {
        this.activityDao = activityDao;
    }
}
