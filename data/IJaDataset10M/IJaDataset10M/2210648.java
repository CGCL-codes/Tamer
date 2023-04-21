package com.eyealike.client.rest;

import static com.eyealike.client.ServiceMapping.*;
import static com.eyealike.client.util.IterableUtils.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.eyealike.client.EmptyResponseQueueException;
import com.eyealike.client.EyealikeAPI;
import com.eyealike.client.EyealikePlatformException;
import com.eyealike.client.ServiceMapping;
import com.eyealike.client.util.HttpClientMarshaller;
import com.eyealike.client.util.SecurityUtils;
import com.eyealike.vsp.bindings.BatchIndexRequest;
import com.eyealike.vsp.bindings.BatchIndexResponse;
import com.eyealike.vsp.bindings.ClassificationRequest;
import com.eyealike.vsp.bindings.ClassificationResponse;
import com.eyealike.vsp.bindings.ComparisonResponse;
import com.eyealike.vsp.bindings.ErrorResponse;
import com.eyealike.vsp.bindings.ImageCollection;
import com.eyealike.vsp.bindings.IndexRequest;
import com.eyealike.vsp.bindings.IndexResponse;
import com.eyealike.vsp.bindings.ProductSimilarityRequest;
import com.eyealike.vsp.bindings.ProductSimilarityResponse;
import com.eyealike.vsp.bindings.ProductSimilarityResult;
import com.eyealike.vsp.bindings.QueueStatusResponse;
import com.eyealike.vsp.bindings.RegionOfInterestRequest;
import com.eyealike.vsp.bindings.RegionOfInterestResponse;
import com.eyealike.vsp.bindings.SearchRequest;
import com.eyealike.vsp.bindings.SearchResponse;
import com.eyealike.vsp.bindings.BatchIndexRequest.Image;
import com.eyealike.vsp.bindings.ProductSimilarityRequest.ProductImage;
import com.eyealike.vsp.bindings.SearchRequest.QueryImage;

/**
 * A thread-safe client to Eyealike's visual search platform.
 */
public class RestEyealikeClient implements EyealikeAPI {

    private final String hostName;

    private final boolean useSslConnections;

    private final String accessId;

    private final String secretKey;

    private final MultiThreadedHttpConnectionManager connectionManager;

    protected final HttpClient client;

    protected final HttpClientMarshaller marshaller;

    protected RestEyealikeClient(final String bindingPackages, final boolean useSslConnections, final String hostName, final String accessId, final String secretKey) {
        this.accessId = accessId;
        this.secretKey = secretKey;
        this.useSslConnections = useSslConnections;
        this.hostName = hostName;
        connectionManager = new MultiThreadedHttpConnectionManager();
        client = new HttpClient(connectionManager);
        marshaller = new HttpClientMarshaller(bindingPackages);
    }

    public static final String BINDINGS_PACKAGE = "com.eyealike.vsp.bindings";

    public RestEyealikeClient(final boolean useSslConnections, final String hostName, final String accessId, final String secretKey) {
        this(BINDINGS_PACKAGE, useSslConnections, hostName, accessId, secretKey);
    }

    public RestEyealikeClient(final String hostName, final String accessId, final String secretKey) {
        this(false, hostName, accessId, secretKey);
    }

    public RestEyealikeClient(final String accessId, final String secretKey) {
        this(false, "self.eyealike.com", accessId, secretKey);
    }

    public void setSetMaxConnections(final int maxConnections) {
        connectionManager.getParams().setDefaultMaxConnectionsPerHost(maxConnections);
        connectionManager.getParams().setMaxTotalConnections(maxConnections);
    }

    protected String getHostName() {
        return hostName;
    }

    protected String serviceUrl(final String hostName, final String endpoint) {
        return (useSslConnections ? "https" : "http") + "://" + hostName + "/vsp/" + endpoint;
    }

    protected String serviceUrl(final String hostName, final ServiceMapping mapping) {
        return serviceUrl(hostName, mapping.getEndpoint());
    }

    /**
	 * Used to support test utility. We might need to refactor this a bit.
	 */
    public String createSecurityHeader(final String targetUrl, final String requestMethod, final String serviceId, final Document xmlDoc, final String dateHeader) {
        final StringBuilder signedMessage = new StringBuilder();
        signedMessage.append(accessId);
        signedMessage.append("\n");
        signedMessage.append(serviceId);
        signedMessage.append("\n");
        if (requestMethod.equals("DELETE") || requestMethod.equals("GET")) {
            final String uri = StringUtils.split(targetUrl, "?")[0];
            final String[] uriTokens = StringUtils.split(uri, "/");
            if (uriTokens.length == 5) {
                signedMessage.append(uriTokens[4]);
                signedMessage.append("\n");
            }
        } else {
            NodeList nodes = xmlDoc.getElementsByTagName("url");
            Node node;
            for (int i = 0; i < nodes.getLength(); i++) {
                node = nodes.item(i);
                signedMessage.append(node.getFirstChild().getNodeValue());
                signedMessage.append("\n");
            }
            nodes = xmlDoc.getElementsByTagName("roi-id");
            for (int i = 0; i < nodes.getLength(); i++) {
                node = nodes.item(i);
                signedMessage.append(node.getFirstChild().getNodeValue());
                signedMessage.append("\n");
            }
            nodes = xmlDoc.getElementsByTagName("collection-id");
            for (int i = 0; i < nodes.getLength(); i++) {
                node = nodes.item(i);
                signedMessage.append(node.getFirstChild().getNodeValue());
                signedMessage.append("\n");
            }
            nodes = xmlDoc.getElementsByTagName("classifier-code");
            for (int i = 0; i < nodes.getLength(); i++) {
                node = nodes.item(i);
                signedMessage.append(node.getFirstChild().getNodeValue());
                signedMessage.append("\n");
            }
        }
        signedMessage.append(dateHeader.trim());
        final String digest = SecurityUtils.computeDigest(signedMessage.toString(), secretKey);
        return "EYEALIKE " + accessId + " " + digest;
    }

    protected void populateSecurityHeaders(final HttpMethod method, final String serviceId, final Iterable<String> queryUrls, final Iterable<String> roiIds, final Iterable<String> collectionIds, final Iterable<String> classifierCodes) {
        final String requestDate = SecurityUtils.getUTCString();
        method.addRequestHeader("Date", requestDate);
        method.addRequestHeader("Authorization", SecurityUtils.createAuthorizationHeader(accessId, secretKey, serviceId, queryUrls, roiIds, collectionIds, classifierCodes, requestDate));
    }

    protected void populateSecurityHeaders(final HttpMethod method, final ServiceMapping service) {
        populateSecurityHeaders(method, service, null, null, null, null);
    }

    protected void populateSecurityHeaders(final HttpMethod method, final ServiceMapping service, final Iterable<String> queryUrls, final Iterable<String> roiIds, final Iterable<String> collectionIds, final Iterable<String> classifierCodes) {
        populateSecurityHeaders(method, service.getServiceId(), queryUrls, roiIds, collectionIds, classifierCodes);
    }

    protected boolean executeBooleanMethod(final HttpMethod method) {
        try {
            client.executeMethod(method);
            final String response = new String(method.getResponseBody());
            return "OK".equals(response.trim());
        } catch (final HttpException e) {
            throw new RuntimeException(e);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected <D> D executeMethod(final HttpMethod method, Class<D> clazz) throws EyealikePlatformException {
        try {
            client.executeMethod(method);
            if (method.getStatusCode() != 200) {
                System.out.println(method.getResponseBodyAsString());
                throw new HttpException(method.getStatusText());
            }
            final D result = marshaller.unmarshal(method, clazz);
            if (result instanceof ErrorResponse) {
                throw new EyealikePlatformException((ErrorResponse) result);
            } else {
                return result;
            }
        } catch (final HttpException e) {
            throw new RuntimeException(e);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean unindex(final String imageUrl) {
        try {
            final String url = URLEncoder.encode(imageUrl, "UTF-8");
            final HttpMethod method = new DeleteMethod(serviceUrl(hostName, UNINDEX) + "/" + url);
            populateSecurityHeaders(method, UNINDEX, one(imageUrl), null, null, null);
            return executeBooleanMethod(method);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClassificationResponse classifyRealTime(final ClassificationRequest request) throws EyealikePlatformException {
        final PostMethod method = new PostMethod(serviceUrl(hostName, CLASSIFY));
        populateSecurityHeaders(method, CLASSIFY, null, one(request.getTargetId()), null, request.getClassifierCode());
        marshaller.marshal(request, method);
        return executeMethod(method, ClassificationResponse.class);
    }

    @Override
    public ImageCollection createOrUpdateCollection(final ImageCollection collection) throws EyealikePlatformException {
        final PutMethod method = new PutMethod(serviceUrl(hostName, COLLECTION_PUT));
        populateSecurityHeaders(method, COLLECTION_PUT, null, null, any(collection.getCollectionId()), null);
        marshaller.marshal(collection, method);
        return executeMethod(method, ImageCollection.class);
    }

    @Override
    public ComparisonResponse compare(final String roiA, final String roiB, final String profileCode) throws EyealikePlatformException {
        final GetMethod method = new GetMethod(serviceUrl(hostName, COMPARE));
        method.setQueryString(new NameValuePair[] { new NameValuePair("roiA", roiA), new NameValuePair("roiB", roiB), new NameValuePair("profileCode", profileCode) });
        populateSecurityHeaders(method, COMPARE, Arrays.asList(roiA, roiB), null, null, null);
        return executeMethod(method, ComparisonResponse.class);
    }

    @Override
    public boolean isCached(final String id) {
        final GetMethod method = new GetMethod(serviceUrl(hostName, MEDIA_CACHE_READ) + "/" + id);
        method.setQueryString(new NameValuePair[] { new NameValuePair("cacheCheck", "true") });
        populateSecurityHeaders(method, MEDIA_CACHE_READ, null, one(id), null, null);
        try {
            client.executeMethod(method);
            final String response = new String(method.getResponseBody());
            return Boolean.parseBoolean(response.trim());
        } catch (final HttpException e) {
            throw new RuntimeException(e);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InputStream findCachedMedia(final String id) {
        final GetMethod method = new GetMethod(serviceUrl(hostName, MEDIA_CACHE_READ) + "/" + id);
        populateSecurityHeaders(method, MEDIA_CACHE_READ, null, one(id), null, null);
        try {
            client.executeMethod(method);
            return method.getResponseBodyAsStream();
        } catch (final HttpException e) {
            throw new RuntimeException(e);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteCollection(final String collectionId, boolean flush) {
        final DeleteMethod method = new DeleteMethod(serviceUrl(hostName, COLLECTION_DELETE) + "/" + collectionId);
        method.setQueryString(new NameValuePair[] { new NameValuePair("flush", String.valueOf(flush)) });
        populateSecurityHeaders(method, COLLECTION_DELETE, null, null, one(collectionId), null);
        return executeBooleanMethod(method);
    }

    @Override
    public QueueStatusResponse batchIndexQueueStatus() throws EyealikePlatformException {
        final GetMethod method = new GetMethod(serviceUrl(hostName, BATCH_INDEX_RESULTS) + "?operation=status");
        populateSecurityHeaders(method, BATCH_INDEX_RESULTS);
        return executeMethod(method, QueueStatusResponse.class);
    }

    @Override
    public BatchIndexResponse indexBatch(final BatchIndexRequest request) throws EyealikePlatformException {
        final PutMethod method = new PutMethod(serviceUrl(hostName, BATCH_INDEX_SUBMIT));
        final List<Image> images = request.getImage();
        final List<String> urls = new ArrayList<String>(images.size());
        for (final Image img : images) {
            urls.add(img.getUrl());
        }
        populateSecurityHeaders(method, BATCH_INDEX_SUBMIT, urls, null, one(request.getCollectionId()), request.getClassifierCode());
        marshaller.marshal(request, method);
        return executeMethod(method, BatchIndexResponse.class);
    }

    @Override
    public IndexResponse indexRealTime(final IndexRequest request) throws EyealikePlatformException {
        final PostMethod method = new PostMethod(serviceUrl(hostName, REAL_TIME_INDEX));
        populateSecurityHeaders(method, REAL_TIME_INDEX, one(request.getUrl()), null, request.getCollectionId(), request.getClassifierCode());
        marshaller.marshal(request, method);
        return executeMethod(method, IndexResponse.class);
    }

    @Override
    public IndexResponse nextBatchIndexResult() throws EyealikePlatformException, EmptyResponseQueueException {
        final GetMethod method = new GetMethod(serviceUrl(hostName, BATCH_INDEX_RESULTS));
        populateSecurityHeaders(method, BATCH_INDEX_RESULTS);
        final Object response = executeMethod(method, null);
        if (response instanceof QueueStatusResponse) {
            throw new EmptyResponseQueueException();
        }
        return (IndexResponse) response;
    }

    @Override
    public ProductSimilarityResult nextProductSimilarityResult() throws EyealikePlatformException, EmptyResponseQueueException {
        final GetMethod method = new GetMethod(serviceUrl(hostName, PRODUCT_SIMILARITY_RESULTS));
        populateSecurityHeaders(method, PRODUCT_SIMILARITY_RESULTS);
        final Object response = executeMethod(method, null);
        if (response instanceof QueueStatusResponse) {
            throw new EmptyResponseQueueException();
        }
        return (ProductSimilarityResult) response;
    }

    @Override
    public QueueStatusResponse productSimilarityQueueStatus() throws EyealikePlatformException {
        final GetMethod method = new GetMethod(serviceUrl(hostName, PRODUCT_SIMILARITY_RESULTS) + "?operation=status");
        populateSecurityHeaders(method, PRODUCT_SIMILARITY_RESULTS);
        return executeMethod(method, QueueStatusResponse.class);
    }

    @Override
    public ProductSimilarityResponse queueProductSimilarityJob(final ProductSimilarityRequest request) throws EyealikePlatformException {
        final PutMethod method = new PutMethod(serviceUrl(hostName, PRODUCT_SIMILARITY_SUBMIT));
        final List<ProductImage> images = request.getProductImage();
        final List<String> urls = new ArrayList<String>(images.size());
        for (final ProductImage img : images) {
            urls.add(img.getUrl());
        }
        populateSecurityHeaders(method, PRODUCT_SIMILARITY_SUBMIT, urls, null, one(request.getCollectionId()), null);
        marshaller.marshal(request, method);
        return executeMethod(method, ProductSimilarityResponse.class);
    }

    @Override
    public SearchResponse search(final SearchRequest request) throws EyealikePlatformException {
        final PostMethod method = new PostMethod(serviceUrl(hostName, REAL_TIME_SEARCH));
        final List<QueryImage> queryImages = request.getQueryImage();
        final List<String> queryIds = new ArrayList<String>(queryImages.size() + 1);
        for (final QueryImage img : queryImages) {
            queryIds.add(img.getRoiId());
        }
        if (request.getEntityId() != null) queryIds.add(request.getEntityId());
        queryIds.addAll(request.getRoiId());
        populateSecurityHeaders(method, REAL_TIME_SEARCH, null, queryIds, any(request.getCollectionId()), null);
        marshaller.marshal(request, method);
        return executeMethod(method, SearchResponse.class);
    }

    @Override
    public RegionOfInterestResponse updateRoi(String roiId, RegionOfInterestRequest request) throws EyealikePlatformException {
        final PostMethod method = new PostMethod(serviceUrl(hostName, ROI_POST) + "/" + roiId);
        populateSecurityHeaders(method, ROI_POST, null, any(roiId), request.getCollectionId(), null);
        marshaller.marshal(request, method);
        return executeMethod(method, RegionOfInterestResponse.class);
    }
}
