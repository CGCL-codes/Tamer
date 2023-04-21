package net.sprd.sampleapps.common.dataaccess;

import net.sprd.sampleapps.common.http.HttpCallCommand;
import net.sprd.sampleapps.common.http.HttpCallCommandFactory;
import net.sprd.sampleapps.common.http.HttpMethod;
import net.sprd.sampleapps.common.http.HttpUrlConnectionFactory;
import net.sprd.sampleapps.customshop.Configuration;
import net.spreadshirt.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.xml.bind.JAXBElement;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

/**
 * @author mbs
 */
class SpreadshirtAPI {

    private static final Logger log = LoggerFactory.getLogger(SpreadshirtAPI.class);

    private ShopDTO shop;

    private ObjectFactory objectFactory;

    private HttpUrlConnectionFactory urlConnectionFactory;

    private HttpCallCommandFactory factory;

    private static final SpreadshirtAPI INSTANCE = new SpreadshirtAPI();

    public static SpreadshirtAPI getInstance() {
        return INSTANCE;
    }

    private SpreadshirtAPI() {
        try {
            objectFactory = new ObjectFactory();
            urlConnectionFactory = new HttpUrlConnectionFactory(Configuration.getInstance().getAPIKey(), Configuration.getInstance().getSecret(), "none");
            factory = new HttpCallCommandFactory(urlConnectionFactory, "net.spreadshirt.api");
            shop = getShop();
        } catch (Exception e) {
            log.error("Could not start SpreadshirtAPI!", e);
        }
    }

    public ArticleDTO getArticle(String articleId) {
        HttpCallCommand command = factory.createJaxbHttpCallCommand(shop.getArticles().getHref() + "/" + articleId, HttpMethod.GET, null);
        command.execute();
        if (command.getStatus() != 200) throw new IllegalArgumentException("Articles data not available"); else return (ArticleDTO) ((JAXBElement) command.getOutput()).getValue();
    }

    public ProductDTO getProduct(String productId) {
        HttpCallCommand command = factory.createJaxbHttpCallCommand(shop.getProducts().getHref() + "/" + productId, HttpMethod.GET, null);
        command.execute();
        if (command.getStatus() != 200) throw new IllegalArgumentException("Products data not available"); else return (ProductDTO) ((JAXBElement) command.getOutput()).getValue();
    }

    public ArticleDTOList getArticles() {
        HttpCallCommand command = factory.createJaxbHttpCallCommand(shop.getArticles().getHref() + "?fullData=true&limit=200", HttpMethod.GET, null);
        command.execute();
        if (command.getStatus() != 200) throw new IllegalArgumentException("Articles data not available"); else return (ArticleDTOList) ((JAXBElement) command.getOutput()).getValue();
    }

    public ProductTypeDTOList getProductTypes() {
        HttpCallCommand command = factory.createJaxbHttpCallCommand(shop.getProductTypes().getHref() + "?fullData=true&limit=1000", HttpMethod.GET, null);
        command.execute();
        if (command.getStatus() != 200) throw new IllegalArgumentException("ProductTypes data not available"); else return (ProductTypeDTOList) ((JAXBElement) command.getOutput()).getValue();
    }

    public PrintTypeDTOList getPrintTypes() {
        HttpCallCommand command = factory.createJaxbHttpCallCommand(shop.getPrintTypes().getHref() + "?fullData=true&limit=1000", HttpMethod.GET, null);
        command.execute();
        if (command.getStatus() != 200) throw new IllegalArgumentException("PrintTypes data not available"); else return (PrintTypeDTOList) ((JAXBElement) command.getOutput()).getValue();
    }

    public FontFamilyDTOList getFontFamilies() {
        HttpCallCommand command = factory.createJaxbHttpCallCommand(shop.getFontFamilies().getHref() + "?fullData=true&limit=1000", HttpMethod.GET, null);
        command.execute();
        if (command.getStatus() != 200) throw new IllegalArgumentException("FontFamilies data not available"); else return (FontFamilyDTOList) ((JAXBElement) command.getOutput()).getValue();
    }

    public ShopDTO getShop() {
        HttpCallCommand command = factory.createJaxbHttpCallCommand(Configuration.getInstance().getShopUrl(), HttpMethod.GET, null);
        command.execute();
        if (command.getStatus() != 200) throw new IllegalArgumentException("Shop data not available"); else {
            return (ShopDTO) ((JAXBElement) command.getOutput()).getValue();
        }
    }

    public CurrencyDTO getShopCurrency() {
        HttpCallCommand command = factory.createJaxbHttpCallCommand(getShop().getCurrency().getHref(), HttpMethod.GET, null);
        command.execute();
        if (command.getStatus() != 200) throw new IllegalArgumentException("Shop data not available"); else {
            return (CurrencyDTO) ((JAXBElement) command.getOutput()).getValue();
        }
    }

    public DesignDTOList getDesigns() {
        HttpCallCommand command = factory.createJaxbHttpCallCommand(shop.getDesignCategories().getHref() + "/b1000000/designs?fullData=true&offset=0&limit=100", HttpMethod.GET, null);
        command.execute();
        if (command.getStatus() != 200) throw new IllegalArgumentException("Desgisn data not available"); else return (DesignDTOList) ((JAXBElement) command.getOutput()).getValue();
    }

    public CurrencyDTOList getCurrencies() {
        HttpCallCommand command = factory.createJaxbHttpCallCommand(getShop().getCurrencies().getHref() + "?fullData=true", HttpMethod.GET, null);
        command.execute();
        if (command.getStatus() != 200) throw new IllegalArgumentException("Shop data not available"); else {
            return (CurrencyDTOList) ((JAXBElement) command.getOutput()).getValue();
        }
    }

    public BasketDTO createBasket() {
        BasketDTO basket = objectFactory.createBasketDTO();
        basket.setToken(UUID.randomUUID().toString());
        HttpCallCommand command = factory.createJaxbHttpCallCommand(shop.getBaskets().getHref(), HttpMethod.POST, null);
        command.setApiKeyProtected(true);
        command.setInput(objectFactory.createBasket(basket));
        command.execute();
        log.info(command.getLocation());
        log.info("" + command.getStatus());
        log.info(command.getErrorMessage());
        if (command.getStatus() != 201) throw new IllegalArgumentException("Could not create Basket!");
        log.info("Basket location is: " + command.getLocation());
        return getBasket(command.getLocation().substring(command.getLocation().lastIndexOf("/") + 1));
    }

    public BasketDTO getBasket(String basketId) {
        HttpCallCommand command = factory.createJaxbHttpCallCommand(shop.getBaskets().getHref() + "/" + basketId, HttpMethod.GET, null);
        command.setApiKeyProtected(true);
        command.execute();
        if (command.getStatus() != 200) throw new IllegalArgumentException("Could not retrieve basket!");
        return (BasketDTO) ((JAXBElement) command.getOutput()).getValue();
    }

    public void updateBasket(BasketDTO basket) {
        HttpCallCommand command = factory.createJaxbHttpCallCommand(shop.getBaskets().getHref() + "/" + basket.getId(), HttpMethod.PUT, null);
        command.setApiKeyProtected(true);
        command.setInput(objectFactory.createBasket(basket));
        command.execute();
        if (command.getStatus() != 200) throw new IllegalArgumentException("Could not create Basket!");
    }

    public String getBasketCheckoutUrl(String id) {
        HttpCallCommand command = factory.createJaxbHttpCallCommand(shop.getBaskets().getHref() + "/" + id + "/checkout", HttpMethod.GET, null);
        command.setApiKeyProtected(true);
        command.execute();
        if (command.getStatus() != 200) throw new IllegalArgumentException("Could not retrieve checkout reference!");
        Reference reference = (Reference) ((JAXBElement) command.getOutput()).getValue();
        return reference.getHref();
    }
}
