package com.nubotech.gwt.oss.client.s3;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import com.nubotech.gwt.oss.client.Bucket;
import com.nubotech.gwt.oss.client.ObjectHolderResult;
import com.nubotech.gwt.oss.client.ObjectHolderResultCallback;
import com.nubotech.gwt.oss.client.ObjectHolder;
import com.nubotech.gwt.oss.client.OnlineStorageServiceError;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author jonnakkerud
 */
public class ObjectHolderListHandler extends AwsS3CallbackHandler {

    public static final String LASTMODIFIED_PATTERN = "yyyy-MM-ddTHH:mm:ss";

    private ObjectHolderResultCallback cb;

    private String bucketName;

    protected List<ObjectHolder> objectHolderResultList = new ArrayList<ObjectHolder>();

    public ObjectHolderListHandler(ObjectHolderResultCallback cb) {
        this.cb = cb;
    }

    public void onResponseReceived(Request request, Response response) {
        ObjectHolderResult objectHolderResult;
        if (hasError(response)) {
            objectHolderResult = new ObjectHolderResult(objectHolderResultList, createError(response));
        } else {
            Document doc = getXml(response);
            if (doc != null) {
                createContent(doc);
            }
            objectHolderResult = new ObjectHolderResult(objectHolderResultList, null);
        }
        cb.onObjectHolderResult(objectHolderResult);
    }

    public void onError(Request request, Throwable ex) {
        cb.onObjectHolderResult(new ObjectHolderResult(objectHolderResultList, new OnlineStorageServiceError(ex)));
    }

    public void createContent(Document doc) {
        Element contentsElement = doc.getDocumentElement();
        XMLParser.removeWhitespace(contentsElement);
        bucketName = getElementTextValue(contentsElement, "Name");
        NodeList list = contentsElement.getElementsByTagName("Contents");
        for (int i = 0; i < list.getLength(); i++) {
            Node n = list.item(i);
            objectHolderResultList.add(createContent(n));
        }
        list = contentsElement.getElementsByTagName("CommonPrefixes");
        for (int i = 0; i < list.getLength(); i++) {
            Node n = list.item(i);
            objectHolderResultList.add(createPrefix(n));
        }
    }

    private ObjectHolder createContent(Node n) {
        ObjectHolder c = new ObjectHolder();
        c.setBucket(new Bucket(bucketName));
        String key = getElementTextValue((Element) n, "Key");
        c.setKey(key);
        String size = getElementTextValue((Element) n, "Size");
        c.setSize(Integer.parseInt(size));
        String lastModified = getElementTextValue((Element) n, "LastModified");
        Date d = parseDate(lastModified);
        c.setLastModified(d);
        String etag = getElementTextValue((Element) n, "ETag");
        c.setETag(etag);
        return c;
    }

    private ObjectHolder createPrefix(Node n) {
        ObjectHolder c = new ObjectHolder();
        c.setBucket(new Bucket(bucketName));
        String key = getElementTextValue((Element) n, "Prefix");
        c.setKey(key);
        return c;
    }

    private Date parseDate(String lastModified) {
        DateTimeFormat formatter = DateTimeFormat.getFormat(LASTMODIFIED_PATTERN);
        String s = lastModified.substring(0, lastModified.lastIndexOf(":") + 3);
        return formatter.parse(s);
    }
}
