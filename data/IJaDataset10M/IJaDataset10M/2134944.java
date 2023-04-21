package org.fxplayer.service.cover;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;
import javax.xml.ws.BindingProvider;
import org.fxplayer.service.cover.wsdl.AWSECommerceService;
import org.fxplayer.service.cover.wsdl.AWSECommerceServicePortType;
import org.fxplayer.service.cover.wsdl.Image;
import org.fxplayer.service.cover.wsdl.Item;
import org.fxplayer.service.cover.wsdl.ItemSearch;
import org.fxplayer.service.cover.wsdl.ItemSearchRequest;
import org.fxplayer.service.cover.wsdl.ItemSearchResponse;
import org.fxplayer.service.cover.wsdl.Items;

/**
 * The Class AmazonFetcher.
 */
public class AmazonFetcher extends HttpFetcher {

    /** The Constant ASSOCIATE_TAG. */
    private static final String ASSOCIATE_TAG = "fx-player-20";

    /** The Constant SUBSCRIPTION_ID. */
    private static final String AWS_ACCESS_KEY_ID = "1V6CNPEPVK9TJYM1PY82";

    /** The Constant END_POINT. */
    public static final String END_POINT = "soap.amazon.com";

    private static final String PASSWORD = "fx-player";

    /** The web service. */
    private static AWSECommerceServicePortType WEB_SERVICE;

    /**
	 * The main method.
	 * @param args
	 *          the arguments
	 */
    public static void main(final String[] args) {
        new AmazonFetcher().httpFetch("sting", "");
    }

    /**
	 * @return
	 */
    private AWSECommerceServicePortType getService() {
        if (WEB_SERVICE == null) {
            WEB_SERVICE = new AWSECommerceService().getAWSECommerceServicePort();
            final BindingProvider bp = (BindingProvider) WEB_SERVICE;
            bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "https://" + END_POINT + "/onca/soap?Service=AWSECommerceService");
            final URL certUrl = this.getClass().getClassLoader().getResource("fx_player_amazon_cert.p12");
            if (certUrl != null) try {
                System.setProperty("javax.net.ssl.keyStore", new File(URLDecoder.decode(certUrl.getFile(), "UTF-8")).getCanonicalPath());
                System.setProperty("javax.net.ssl.keyStoreType", "pkcs12");
                System.setProperty("javax.net.ssl.keyStorePassword", PASSWORD);
            } catch (final Exception e) {
                LOG.debug("Error setting Key", e);
            }
        }
        return WEB_SERVICE;
    }

    @Override
    public byte[] httpFetch(final String artist, final String album) {
        if (LOG.isDebugEnabled()) LOG.debug("Fetching cover From Amazon:album=" + album + ", artist=" + artist);
        final ItemSearch search = new ItemSearch();
        search.setSubscriptionId(AWS_ACCESS_KEY_ID);
        search.setAssociateTag(ASSOCIATE_TAG);
        search.setAWSAccessKeyId(AWS_ACCESS_KEY_ID);
        final ItemSearchRequest request = new ItemSearchRequest();
        request.setArtist(artist);
        request.setTitle(album);
        request.setSearchIndex("Music");
        request.getResponseGroup().add("Images");
        search.setShared(request);
        final ItemSearchResponse allItems = getService().itemSearch(search);
        if (allItems != null) for (final Items someItems : allItems.getItems()) {
            final List<Item> items = someItems.getItem();
            if (items != null) for (final Item item : items) {
                if (LOG.isDebugEnabled()) LOG.debug("Fetching Item:ASIN=" + item.getASIN());
                final Image image = item.getLargeImage();
                if (image != null) return wget(image.getURL());
            }
        }
        if (LOG.isDebugEnabled()) LOG.debug("No Cover Found for " + artist + ", " + album);
        return null;
    }
}
