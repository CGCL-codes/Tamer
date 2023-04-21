package org.slasoi.businessManager.common.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.slasoi.businessManager.common.util.Constants;
import org.slasoi.businessManager.common.dao.CustomerProductDAO;
import org.slasoi.businessManager.common.dao.PartyDAO;
import org.slasoi.businessManager.common.dao.PriceVariationDAO;
import org.slasoi.businessManager.common.dao.PriceVariationTypeDAO;
import org.slasoi.businessManager.common.dao.ProductOfferDAO;
import org.slasoi.businessManager.common.drools.DroolsManager;
import org.slasoi.businessManager.common.drools.Rule;
import org.slasoi.businessManager.common.drools.RuleParam;
import org.slasoi.businessManager.common.drools.Template;
import org.slasoi.businessManager.common.model.EmCharacteristic;
import org.slasoi.businessManager.common.model.EmCustomersProducts;
import org.slasoi.businessManager.common.model.EmParty;
import org.slasoi.businessManager.common.model.EmPriceVariation;
import org.slasoi.businessManager.common.model.EmProductPromotion;
import org.slasoi.businessManager.common.model.EmPromotionType;
import org.slasoi.businessManager.common.model.EmPromotionValues;
import org.slasoi.businessManager.common.model.EmPromotions;
import org.slasoi.businessManager.common.model.EmRuleTemplates;
import org.slasoi.businessManager.common.model.EmSpProducts;
import org.slasoi.businessManager.common.model.EmSpProductsOffer;
import org.slasoi.businessManager.common.model.EmSpServices;
import org.slasoi.businessManager.common.model.EmSpServicesCharacteristic;
import org.slasoi.businessManager.common.service.CharacteristicManager;
import org.slasoi.businessManager.common.service.CustomerProductManager;
import org.slasoi.businessManager.common.service.ServiceManager;
import org.slasoi.businessManager.common.util.SimpleDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "customerProductService")
public class CustomerProductManagerImpl implements CustomerProductManager {

    static Logger log = Logger.getLogger(CustomerProductManagerImpl.class);

    @Autowired
    private CustomerProductDAO cpDAO;

    @Autowired
    private PriceVariationDAO priceVariationDAO;

    @Autowired
    private ProductOfferDAO productOfferDAO;

    @Autowired
    private PartyDAO partyDAO;

    @Autowired
    private PriceVariationTypeDAO priceVariationTypeDAO;

    @Autowired
    private ServiceManager serviceService;

    @Autowired
    private CharacteristicManager characteristicService;

    @Transactional(propagation = Propagation.REQUIRED)
    public List<EmCustomersProducts> getCustomerProducts() {
        log.info("getCustomerProducts()");
        return cpDAO.getList();
    }

    /**
     * 
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<EmCustomersProducts> getCustomerProductsByDateAndState(Date date, String typeDate) {
        log.info("getCustomerProductsByDateAndState()");
        return cpDAO.getCustomerProductsByDateAndState(date, typeDate);
    }

    /**
     * Get Customer products active that have not ended before the given Date
     * @param date
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<EmCustomersProducts> getCustomerProductsActiveByDate(Date date) {
        log.info("getCustomerProductsActiveByDate()");
        return cpDAO.getCustomerProductsActiveByDate(date);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public EmCustomersProducts getCustomerProductById(String id) {
        log.info("getCustomerProductById(" + id + ")");
        EmCustomersProducts cp = cpDAO.load(id);
        cpDAO.getHibernateTemplate().initialize(cp.getEmPriceVariations());
        cpDAO.getHibernateTemplate().initialize(cp.getEmSpProductsOffer().getEmComponentPrices());
        cpDAO.getHibernateTemplate().initialize(cp.getEmSpProductsOffer().getEmGeographicalAreases());
        for (EmSpServices service : cp.getEmSpProductsOffer().getEmSpServiceses()) cpDAO.getHibernateTemplate().initialize(service.getEmParty());
        return cp;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<EmCustomersProducts> getCustomerProductsOfParty(EmParty party) {
        log.info("getCustomerProductsByParty()");
        return cpDAO.getCustomerProductOfParty(party);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<EmCustomersProducts> getCustomerProductsByPublisher(EmParty party) {
        log.info("getCustomerProductsByPublisher()");
        return cpDAO.getCustomerProductsByPublisher(party);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<EmCustomersProducts> getCustomerProductsForRating(EmParty party) {
        log.info("getCustomerProductsByPublisher()");
        return cpDAO.getCustomerProductsAndServicesAttributes(party);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<EmCustomersProducts> getCustomerProductsByCategory(EmParty party, String category) {
        log.info("getCustomerProductsByPublisher()");
        return cpDAO.getCustomerProductsAndServicesAttributes(party, category);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void contractProductsOffer(EmParty party, List<Long> productsOffer) throws Exception {
        for (Long productOfferId : productsOffer) {
            EmSpProductsOffer offer = productOfferDAO.load(productOfferId);
            EmCustomersProducts cp = new EmCustomersProducts();
            cp.setEmParty(party);
            cp.setEmSpProductsOffer(offer);
            cp.setState(Constants.SLA_STATE_OBS);
            cp.setDtInsertDate(new SimpleDate());
            cp.setDtDateBegin(offer.getDtValidFrom());
            cp.setDtDateEnd(offer.getDtValidTo());
            cp.setTxBslaid(java.util.UUID.randomUUID().toString());
            log.info("ID >>>>>>>>>>>>>>>>> " + cp.getTxBslaid());
            cpDAO.save(cp);
            applyPromotions(cp);
            log.info("Prices Variation Resultantes >>>>>>>>>>>>>>>> " + cp.getEmPriceVariations().size());
            for (EmPriceVariation pv : cp.getEmPriceVariations()) {
                log.info(">>>>>>>>>>>> PriceVarition : " + pv.getTxDescription());
                priceVariationDAO.save(pv);
            }
        }
    }

    /**
 * 
 */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public EmCustomersProducts getVariationsByPromotion(Long party, Long productOfferId) throws Exception {
        EmSpProductsOffer offer = productOfferDAO.load(productOfferId);
        EmCustomersProducts cp = new EmCustomersProducts();
        cp.setEmParty(partyDAO.load(party));
        cp.setEmSpProductsOffer(offer);
        cp.setDtInsertDate(new SimpleDate());
        cp.setDtDateBegin(offer.getDtValidFrom());
        cp.setDtDateEnd(offer.getDtValidTo());
        applyPromotions(cp);
        Set<EmPriceVariation> variations = cp.getEmPriceVariations();
        if (variations != null && variations.size() > 0) {
            Set<EmPriceVariation> newVariations = new HashSet();
            Iterator<EmPriceVariation> ite = variations.iterator();
            EmPriceVariation variation;
            while (ite.hasNext()) {
                variation = ite.next();
                variation.setEmPriceVariationType(priceVariationTypeDAO.load(variation.getEmPriceVariationType().getNuIdVariationType()));
                newVariations.add(variation);
            }
            cp.setEmPriceVariations(newVariations);
        }
        return cp;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void applyPromotions(EmCustomersProducts customer_product) throws Exception {
        EmSpProductsOffer product_offer = customer_product.getEmSpProductsOffer();
        EmSpProducts product = product_offer.getEmSpProducts();
        log.info(">>>> Num Promotions: " + product.getEmProductPromotions().size());
        Iterator<EmProductPromotion> product_promos = product.getEmProductPromotions().iterator();
        ArrayList<Object> arrayFacts = new ArrayList<Object>(product_offer.getEmComponentPrices());
        arrayFacts.add(customer_product);
        HashMap<Long, Template> templates = new HashMap<Long, Template>();
        while (product_promos.hasNext()) {
            EmProductPromotion product_promo = product_promos.next();
            if (product_promo.getTxStatus().equals(Constants.STATE_ACTIVE)) {
                EmPromotions promo = product_promo.getEmPromotions();
                log.info(">>> Promotion:  " + promo.getTxName());
                EmPromotionType promoType = promo.getEmPromotionType();
                log.info(">>> Tipo : " + promoType.getTxName());
                EmRuleTemplates ruleTplt = promoType.getEmRuleTemplates();
                log.info(">>> Template : " + ruleTplt.getTxName());
                Template template = templates.get(ruleTplt.getNuRuleTemplateId());
                if (template == null) {
                    template = new Template(ruleTplt.getNuRuleTemplateId(), promo.getTxName(), ruleTplt.getTxDescription(), ruleTplt.getTxTemplateDrt());
                    templates.put(template.getId(), template);
                }
                ArrayList<RuleParam> ruleParams = new ArrayList<RuleParam>();
                Iterator<EmPromotionValues> promoValues = promo.getEmPromotionValueses().iterator();
                while (promoValues.hasNext()) {
                    EmPromotionValues promoValue = promoValues.next();
                    log.info(promoValue.getEmRuleParams().getNuParamRuleOrder() + " - " + promoValue.getEmRuleParams().getEmParams().getTxParamName() + ": " + promoValue.getTxParamValue());
                    RuleParam rp = new RuleParam(promoValue.getEmRuleParams().getNuParamRuleOrder(), promoValue.getTxParamValue());
                    ruleParams.add(rp);
                }
                Rule rule = new Rule(promo.getNuPromotionId(), promo.getTxName(), promo.getTxDescription(), ruleParams);
                template.getRules().add(rule);
            }
        }
        if (templates.isEmpty()) {
            log.info(">>>> Not Exist Active Promotions. ");
            return;
        }
        log.debug("Array Facts >>> " + arrayFacts.size());
        DroolsManager dm = new DroolsManager(templates.values(), arrayFacts);
        dm.processRules();
        log.debug("<<<<<<<< Rules Executed >>>>>>>>>");
        log.debug("Array Facts After >>> " + arrayFacts.size());
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void saveCustomer(EmCustomersProducts customer_product) throws Exception {
        cpDAO.save(customer_product);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updateCustomer(EmCustomersProducts customer_product) throws Exception {
        cpDAO.update(customer_product);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public EmCustomersProducts getCustomerProductByIdLoaded(String id) {
        return cpDAO.getCustomerProductByIdLoaded(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<EmCustomersProducts> getCustomerProductsByProductOfferAndParty(EmSpProductsOffer productOffer, EmParty party) {
        return cpDAO.getCustomerProductsByProductOfferAndParty(productOffer, party);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public String getServiceProvisioningName(EmCustomersProducts customerProduct) {
        log.debug("Into getServiceProvisioningName method ");
        EmCharacteristic characterType = characteristicService.getCharacteristicByName(Constants.SERVICE_CHARACTERISTIC_NAME);
        if (characterType != null) {
            log.debug("CharacteristicBaseID: " + characterType.getNuCharacteristicId());
            EmSpProductsOffer offer = productOfferDAO.getByBslat(customerProduct.getEmSpProductsOffer().getTxBslatid());
            if (offer != null) {
                Set<EmSpServices> services = offer.getEmSpServiceses();
                if (services != null && services.size() > 0) {
                    for (EmSpServices service : services) {
                        EmSpServices serviceAux = serviceService.getServiceById(service.getNuServiceid());
                        Set<EmSpServicesCharacteristic> characteristics = serviceAux.getEmSpServicesCharacteristics();
                        if (characteristics != null && characteristics.size() > 0) {
                            for (EmSpServicesCharacteristic character : characteristics) {
                                if (characterType.getNuCharacteristicId().longValue() == character.getEmCharacteristic().getNuCharacteristicId().longValue()) {
                                    log.debug("Value Returned: " + character.getTxValue());
                                    return character.getTxValue();
                                }
                            }
                        }
                    }
                }
            }
        }
        return "";
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<String> getBslatIds(EmParty party) {
        ArrayList<String> bslatIds = new ArrayList<String>();
        for (EmCustomersProducts cp : cpDAO.getCustomerProductOfParty(party)) bslatIds.add(cp.getTxBslaid());
        return bslatIds;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<EmCustomersProducts> getCustomerProductsByProduct(EmSpProducts product) {
        return cpDAO.getCustomerProductsByProduct(product);
    }
}
