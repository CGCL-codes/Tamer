package com.javector.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;
import com.amazon.webservices.awsecommerceservice._2007_02_22.Image;
import com.example.retail.CurrencyType;
import com.example.retail.OfferType;
import com.example.retail.PictureType;
import com.example.retail.PriceType;
import com.example.retail.SourceType;
import com.javector.soashopper.Category;
import com.javector.soashopper.Offer;
import com.javector.soashopper.Picture;
import com.javector.soashopper.Price;
import com.javector.soashopper.Store;
import com.javector.soashopper.amazon.AmazonStoreCategory;
import com.javector.soashopper.ebay.EBayCategory;
import com.javector.soashopper.yahoo.YahooShoppingTopLevelCategory;
import com.javector.soashopper.yahoo.YahooShoppingDepartment;
import ebay.apis.eblbasecomponents.AmountType;
import ebay.apis.eblbasecomponents.CurrencyCodeType;

/**
 * Utility methods for converting from schema compiled types (e.g., eBay schema
 * types) to SOA Shopper API types. NOT THREAD SAFE.
 */
public class TypeConverter {

    public Integer toInteger(BigInteger bi) {
        if (bi == null) {
            return null;
        }
        return new Integer(bi.intValue());
    }

    public Currency toCurrency(CurrencyCodeType cc) {
        if (cc == null) {
            return null;
        }
        return Currency.getInstance(cc.name());
    }

    public CurrencyCodeType toCurrencyCodeType(Currency c) {
        if (c == null) {
            return null;
        }
        return CurrencyCodeType.fromValue(c.getCurrencyCode());
    }

    public Price toPrice(AmountType amt) {
        if (amt == null) {
            return null;
        }
        Currency c = toCurrency(amt.getCurrencyID());
        if (c == null) {
            return null;
        }
        return new Price(c, amt.getValue());
    }

    public AmountType toAmountType(Price price) {
        if (price == null) {
            return null;
        }
        AmountType amt = new AmountType();
        amt.setCurrencyID(toCurrencyCodeType(price.getCurrencyID()));
        amt.setValue(price.getValue());
        return amt;
    }

    /**
   * @param price
   * @return a US Dollar price.
   */
    public Price toUSDPrice(BigDecimal price) {
        if (price == null) {
            return null;
        }
        return new Price(Currency.getInstance("USD"), price.doubleValue());
    }

    public EBayCategory toEBayCategory(Category category) {
        if (category == null) {
            return null;
        }
        switch(category) {
            case COMPUTERS:
                return EBayCategory.COMPUTERS;
            case CELLPHONES:
                return EBayCategory.CELLPHONES;
            case MOVIES:
                return EBayCategory.MOVIES;
            default:
                throw new RuntimeException("Category " + category.name() + " can not be processed.  Additional runtime logic is " + "needed in the code.");
        }
    }

    public AmazonStoreCategory toAmazonStoreCategory(Category category) {
        if (category == null) {
            return null;
        }
        switch(category) {
            case COMPUTERS:
                return AmazonStoreCategory.COMPUTERS_AND_PC_HARDWARE;
            case CELLPHONES:
                return AmazonStoreCategory.CELLPHONES;
            case MOVIES:
                return AmazonStoreCategory.MOVIES;
            default:
                throw new RuntimeException("Category " + category.name() + " can not be processed.  Additional runtime logic is " + "needed in the code.");
        }
    }

    public YahooShoppingTopLevelCategory toYahooShoppingTopLevelCategory(Category category) {
        if (category == null) {
            return null;
        }
        switch(category) {
            case COMPUTERS:
                return YahooShoppingTopLevelCategory.COMPUTERS_AND_SOFTWARE;
            case CELLPHONES:
                return YahooShoppingTopLevelCategory.ELECTRONICS_AND_CAMERA;
            case MOVIES:
                return null;
            default:
                throw new RuntimeException("Category " + category.name() + " can not be processed.  Additional runtime logic is " + "needed in the code.");
        }
    }

    public YahooShoppingDepartment toYahooShoppingDepartment(Category category) {
        if (category == null) {
            return null;
        }
        switch(category) {
            case COMPUTERS:
                return null;
            case CELLPHONES:
                return null;
            case MOVIES:
                return YahooShoppingDepartment.DVD_AND_VIDEO;
            default:
                throw new RuntimeException("Category " + category.name() + " can not be processed.  Additional runtime logic is " + "needed in the code.");
        }
    }

    public Double toDouble(Price price) {
        if (price == null) {
            return null;
        }
        return Double.valueOf(price.getValue());
    }

    public Category toCategory(String c) {
        if (c == null) {
            return null;
        }
        return Category.valueOf(c);
    }

    public Price toPrice(String currencyId, Double priceVal) {
        return Price.getPrice(currencyId, priceVal);
    }

    /**
   * Convert the SOAShopper API Offer type to the JAXB Generated OfferType used
   * by the SOAP and REST endpoints.
   * 
   * @param o
   * @return
   */
    public OfferType toOfferType(Offer o) {
        if (o == null) {
            return null;
        }
        OfferType ot = new OfferType();
        ot.setOfferId(o.getSourceSpecificOfferId());
        ot.setProductId(o.getSourceSpecificProductId());
        ot.setSource(toSourceType(o.getSource()));
        ot.setThumbnail(toPictureType(o.getThumbnail()));
        ot.setPrice(toPriceType(o.getPrice()));
        ot.setMerchantName(o.getMerchantName());
        ot.setSummary(o.getSummary());
        ot.setOfferUrl(o.getUrl().toString());
        return ot;
    }

    public PictureType toPictureType(Picture p) {
        if (p == null) {
            return null;
        }
        PictureType pt = new PictureType();
        pt.setUrl(p.getUrl() == null ? null : p.getUrl().toString());
        pt.setPixelHeight(toBigInteger(p.getPixelHeight()));
        pt.setPixelWidth(toBigInteger(p.getPixelWidth()));
        return pt;
    }

    public Picture toPicture(Image img) {
        if (img == null) {
            return null;
        }
        URL picUrl;
        try {
            picUrl = new URL(img.getURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Amazon sent bad image URL.", e);
        }
        return new Picture(picUrl, toInteger(img.getWidth().getValue().toBigInteger()), toInteger(img.getHeight().getValue().toBigInteger()));
    }

    public BigInteger toBigInteger(Integer i) {
        BigInteger bi = null;
        if (i != null) {
            bi = BigInteger.valueOf(i.longValue());
        }
        return bi;
    }

    public PriceType toPriceType(Price price) {
        if (price == null) {
            return null;
        }
        PriceType pt = new PriceType();
        pt.setCurrencyId(toCurrencyType(price.getCurrencyID()));
        pt.setValue(BigDecimal.valueOf(price.getValue()));
        return pt;
    }

    public CurrencyType toCurrencyType(Currency c) {
        if (c == null) {
            return null;
        }
        return CurrencyType.fromValue(c.toString());
    }

    public SourceType toSourceType(Store source) {
        if (source == null) {
            return null;
        }
        return SourceType.fromValue(source.getName());
    }

    public Price toPrice(com.amazon.webservices.awsecommerceservice._2007_02_22.Price p) {
        Price price = null;
        if (p != null) {
            String curStr = p.getCurrencyCode();
            if (curStr == null) {
                curStr = "USD";
            }
            if (p.getAmount() == null) {
                return Price.getPrice(curStr, Double.valueOf(0.0));
            }
            price = Price.getPrice(curStr, Double.valueOf(p.getAmount().doubleValue() / 100));
        }
        return price;
    }
}
