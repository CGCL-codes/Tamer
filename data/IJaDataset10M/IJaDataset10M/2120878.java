package com.javector.soashopper.yahoo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import com.javector.soashopper.Offer;
import com.javector.soashopper.Picture;
import com.javector.soashopper.Price;
import com.javector.soashopper.ProductImp;
import com.javector.soashopper.Store;
import com.javector.util.TypeConverter;
import yahoo.prods.CatalogType;
import yahoo.prods.ThumbnailType;

public class YahooProductImpl extends ProductImp {

    private CatalogType delegate;

    public YahooProductImpl(CatalogType delegate) {
        this.delegate = delegate;
    }

    @Override
    public String getSourceSpecificId() {
        return delegate.getId().toString();
    }

    @Override
    public Store getSource() {
        return Store.YAHOO;
    }

    @Override
    public Picture getThumbnail() {
        ThumbnailType tt = delegate.getThumbnail();
        if (tt == null) {
            return null;
        }
        String thumbUrlString = tt.getUrl();
        URL thumbURL;
        try {
            thumbURL = new URL(thumbUrlString);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        TypeConverter tc = new TypeConverter();
        return new Picture(thumbURL, tc.toInteger(tt.getWidth()), tc.toInteger(tt.getHeight()));
    }

    @Override
    public String getSummary() {
        return delegate.getSummary();
    }

    @Override
    public URL getUrl() {
        String url = delegate.getUrl();
        if (url == null) {
            return null;
        }
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Offer> getSourceSpecificOffers() {
        return null;
    }
}
