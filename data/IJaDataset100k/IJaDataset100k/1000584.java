package org.slasoi.businessManager.productSLA.productDiscovery.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.ggf.schemas.graap.x2007.x03.wsAgreement.AgreementTemplateType;
import org.ggf.schemas.graap.x2007.x03.wsAgreement.TemplateDocument;
import org.slasoi.bslamanager.main.context.BusinessContextService;
import org.slasoi.businessManager.common.util.Constants;
import org.slasoi.businessManager.common.model.EmServiceSpecification;
import org.slasoi.businessManager.common.model.EmSpProducts;
import org.slasoi.businessManager.common.model.EmSpProductsOffer;
import org.slasoi.businessManager.common.model.EmSpServices;
import org.slasoi.businessManager.common.service.ProductManager;
import org.slasoi.businessManager.common.service.ProductOfferManager;
import org.slasoi.businessManager.common.service.ServiceManager;
import org.slasoi.businessManager.productSLA.productDiscovery.ProductDiscovery;
import org.slasoi.businessManager.common.ws.types.Product;
import org.slasoi.businessManager.common.ws.types.ProductOffer;
import org.slasoi.businessManager.common.ws.types.RatingType;
import org.slasoi.businessManager.common.ws.types.Service;
import org.slasoi.gslam.syntaxconverter.WSAgreementTemplateRenderer;
import org.slasoi.slamodel.primitives.UUID;
import org.slasoi.slamodel.sla.SLATemplate;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service(value = "productDiscovery")
public class ProductDiscoveryImpl implements ProductDiscovery {

    private static final Logger log = Logger.getLogger(ProductDiscoveryImpl.class);

    @Autowired
    protected ProductManager productService;

    @Autowired
    protected ServiceManager serviceService;

    @Autowired
    protected ProductOfferManager productOfferService;

    @Autowired
    private BusinessContextService businessContextService;

    /**
     *    
     * @param customerId
     * @param productId
     * @return
     */
    public List<SLATemplate> getTemplatesSLAModel(long customerId, long productId) {
        log.debug("Inside getTemplates method");
        log.debug("CustomerID: " + customerId);
        log.debug("ProductID: " + productId);
        try {
            EmSpProducts product = this.productService.getProductOffersLoaded(new Long(productId));
            if (product == null) {
                log.error("Product with ID: " + productId + " not found");
                return null;
            } else if (!Constants.STATUS_ACTIVE.equalsIgnoreCase(product.getTxStatus())) {
                log.error("Product with ID: " + productId + " is not active to be purchased");
                return null;
            }
            Set<EmSpProductsOffer> offers = product.getEmSpProductsOffers();
            Iterator<EmSpProductsOffer> it = offers.iterator();
            UUID uuid;
            SLATemplate template;
            List<SLATemplate> list = new ArrayList<SLATemplate>();
            EmSpProductsOffer offer;
            while (it.hasNext()) {
                offer = it.next();
                if (offer.getTxBslatid() != null && !"".equals(offer.getTxBslatid())) {
                    uuid = new UUID(offer.getTxBslatid());
                    log.debug("templateID: " + uuid.getValue());
                    if (this.businessContextService == null) {
                        log.debug("Context nullllllll");
                    } else if (this.businessContextService.getBusinessContext().getSLATemplateRegistry() == null) {
                        log.debug("Registry nullllllll");
                    }
                    try {
                        template = this.businessContextService.getBusinessContext().getSLATemplateRegistry().getSLATemplate(uuid);
                        if (template != null) {
                            log.debug("Template found");
                            list.add(template);
                        }
                    } catch (Exception ex) {
                        log.error(ex.toString());
                        ex.printStackTrace();
                    }
                } else {
                    log.error("The offer has not a valid id template associated");
                }
            }
            return list;
        } catch (Exception e) {
            log.error(e.toString());
            e.printStackTrace();
            return null;
        }
    }

    /**
        *    
        * @param customerId
        * @param productId
        * @return
        */
    public List<AgreementTemplateType> getTemplates(long customerId, long productId) {
        log.debug("Inside getTemplates method");
        log.debug("CustomerID: " + customerId);
        log.debug("ProductID: " + productId);
        try {
            EmSpProducts product = this.productService.getProductOffersLoaded(new Long(productId));
            if (product == null) {
                log.error("Product with ID: " + productId + " not found");
                return null;
            } else if (!Constants.STATUS_ACTIVE.equalsIgnoreCase(product.getTxStatus())) {
                log.error("Product with ID: " + productId + " is not active to be purchased");
                return null;
            }
            Set<EmSpProductsOffer> offers = product.getEmSpProductsOffers();
            Iterator<EmSpProductsOffer> it = offers.iterator();
            UUID uuid;
            SLATemplate template;
            List<AgreementTemplateType> list = new ArrayList<AgreementTemplateType>();
            while (it.hasNext()) {
                uuid = new UUID(it.next().getTxBslatid());
                log.debug("templateID: " + uuid.getValue());
                try {
                    template = this.businessContextService.getBusinessContext().getSLATemplateRegistry().getSLATemplate(uuid);
                    if (template != null) {
                        log.debug("Template found");
                        WSAgreementTemplateRenderer wsagRenderer = new WSAgreementTemplateRenderer();
                        String stringTemplate = wsagRenderer.renderSLATemplate(template);
                        TemplateDocument wsagTemplate = TemplateDocument.Factory.parse(stringTemplate);
                        list.add(wsagTemplate.getTemplate());
                    }
                } catch (Exception ex) {
                    log.error(ex.toString());
                    ex.printStackTrace();
                }
            }
            return list;
        } catch (Exception e) {
            log.error(e.toString());
            e.printStackTrace();
            return null;
        }
    }

    /**
        * Get Products from the repository
        * @param customerID
        * @param parameters
        * @return
        */
    public List<EmSpProducts> getProducts(String customerID, String[] parameters) {
        log.debug("Inside getProducts method");
        log.debug("CustomerID:" + customerID);
        List<EmSpProducts> products = null;
        if (parameters != null && parameters.length > 0) {
            log.debug("Searching with parameter: ");
            List<String> filters = new ArrayList<String>();
            for (String filter : parameters) {
                log.debug(filter);
                if (filter != null) filters.add("%" + filter + "%");
            }
            products = productService.findProductsActive(filters);
        } else {
            log.debug("Searching without parameters");
            products = productService.getAllInitActive();
        }
        if (products != null && products.size() > 0) {
            log.debug("Product found:" + products.size());
        } else log.debug("No Products found");
        List<EmSpProducts> listProducts = new ArrayList<EmSpProducts>();
        for (EmSpProducts product : products) listProducts.add(productService.getProductWithOffersById(product.getNuProductid()));
        return listProducts;
    }

    public Service[] getWsPartyServices(Long partyId, String filter) {
        log.debug("Inside getWsPartyServices method");
        log.debug("partyID: " + partyId);
        List<EmSpServices> emServices = serviceService.findPartyServices(partyId, filter);
        log.info("Number of services found : " + ((emServices != null) ? emServices.size() : 0));
        Service[] listServices = new Service[emServices.size()];
        int serviceIndex = 0;
        for (EmSpServices emService : emServices) {
            Service service = new Service();
            service.setId(emService.getNuServiceid());
            service.setName(emService.getTxServicename());
            service.setDescription(emService.getTxServicedesc());
            if (emService.getRating() != null) {
                log.info(">>> Num Rates: " + emService.getRating().getNuRates() + " - Average: " + emService.getRating().getNuRating());
                service.setRating(new RatingType(emService.getRating().getNuRates().intValue(), emService.getRating().getNuRating().doubleValue()));
            } else {
                service.setRating(new RatingType(0, 0));
                log.error("No Tating Info for Service: " + emService.getTxServicename());
            }
            listServices[serviceIndex] = service;
            serviceIndex++;
        }
        return listServices;
    }

    public Product[] getWsProducts(String customerID, String[] parameters) {
        List<EmSpProducts> emSpProducts = getProducts(customerID, parameters);
        return emSpProductToWsProduct(emSpProducts);
    }

    public Product[] getWsPartyProducts(Long partyId, String filter) {
        if (filter != null) filter = "%" + filter + "%";
        List<EmSpProducts> emSpProducts = new ArrayList<EmSpProducts>();
        for (EmSpProducts product : productService.findPartyProducts(partyId, filter)) emSpProducts.add(productService.getProductWithOffersById(product.getNuProductid()));
        return emSpProductToWsProduct(emSpProducts);
    }

    public ProductOffer[] getWsPartyProductOffers(Long partyId, String filter) {
        List<EmSpProductsOffer> emProductOffers = productOfferService.getPartyProductOffers(partyId, filter);
        ProductOffer[] listOffers = null;
        if (emProductOffers != null && !emProductOffers.isEmpty()) {
            listOffers = new ProductOffer[emProductOffers.size()];
            log.info("Number of offers : " + emProductOffers.size());
            int index = 0;
            for (EmSpProductsOffer offer : emProductOffers) {
                log.info("offer:" + offer.getTxName());
                ProductOffer productOffer = new ProductOffer();
                productOffer.setId(offer.getNuIdproductoffering());
                productOffer.setName(offer.getTxName());
                productOffer.setDescription(offer.getTxDescription());
                log.info("Number of services of offer " + offer.getTxName() + ": " + offer.getEmSpServiceses().size());
                String[] listServices = new String[offer.getEmSpServiceses().size()];
                int serviceIndex = 0;
                for (EmSpServices service : offer.getEmSpServiceses()) {
                    listServices[serviceIndex] = service.getTxServicename();
                }
                productOffer.setServices(listServices);
                if (offer.getRating() != null) {
                    log.info(">>> Num Rates: " + offer.getRating().getNuRates() + " - Average: " + offer.getRating().getNuRating());
                    productOffer.setRating(new RatingType(offer.getRating().getNuRates().intValue(), offer.getRating().getNuRating().doubleValue()));
                } else {
                    productOffer.setRating(new RatingType(0, 0));
                    log.error("No Tating Info for Product: " + offer.getTxName());
                }
                listOffers[index] = productOffer;
                index++;
            }
        }
        return listOffers;
    }

    public Product[] emSpProductToWsProduct(List<EmSpProducts> emSpProducts) {
        Product[] products = null;
        if (emSpProducts != null && !emSpProducts.isEmpty()) {
            products = new Product[emSpProducts.size()];
            int i = 0;
            for (EmSpProducts product : emSpProducts) {
                HashSet<String> categories = new HashSet<String>();
                Product productnew = new Product();
                productnew.setId(product.getNuProductid());
                productnew.setDescription(product.getTxProductdesc());
                productnew.setName(product.getTxProductname());
                productnew.setBrand(product.getTxBrand());
                RatingType rating = null;
                if (product.getRating() != null) {
                    log.info(">>> Num Rates: " + product.getRating().getNuRates() + " - Average: " + product.getRating().getNuRating());
                    rating = new RatingType(product.getRating().getNuRates().intValue(), product.getRating().getNuRating().doubleValue());
                } else {
                    rating = new RatingType(0, 0);
                    log.error("No Tating Info for Product: " + product.getTxProductname());
                }
                productnew.setRating(rating);
                ProductOffer[] listOffers = new ProductOffer[product.getEmSpProductsOffers().size()];
                log.info("Number of offers of product " + product.getTxProductname() + ": " + product.getEmSpProductsOffers().size());
                int index = 0;
                for (EmSpProductsOffer offer : product.getEmSpProductsOffers()) {
                    log.info("offer:" + offer.getTxName());
                    ProductOffer productOffer = new ProductOffer();
                    productOffer.setId(offer.getNuIdproductoffering());
                    productOffer.setName(offer.getTxName());
                    productOffer.setDescription(offer.getTxDescription());
                    log.info("Number of services of offer " + offer.getTxName() + ": " + offer.getEmSpServiceses().size());
                    String[] listServices = new String[offer.getEmSpServiceses().size()];
                    int serviceIndex = 0;
                    for (EmSpServices service : offer.getEmSpServiceses()) {
                        listServices[serviceIndex] = service.getTxServicename();
                        for (EmServiceSpecification category : service.getEmServiceSpecifications()) {
                            if (!categories.contains(category.getTxName())) categories.add(category.getTxName());
                        }
                    }
                    productOffer.setServices(listServices);
                    listOffers[index] = productOffer;
                    index++;
                }
                productnew.setProductOffers(listOffers);
                if (categories.size() > 0) {
                    log.info("Number of Categories: " + categories.size());
                    productnew.setCategory(categories.toArray(new String[categories.size()]));
                }
                products[i] = productnew;
                i++;
            }
        }
        return products;
    }
}
