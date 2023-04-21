package org.spantus.android.service;

import java.net.MalformedURLException;
import org.spantus.logger.Logger;
import java.net.URL;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.spantus.android.dto.CorpusItem;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import android.net.Uri;
import com.giantflyingsaucer.RequestMethod;
import com.giantflyingsaucer.RestClientDOM;
import com.giantflyingsaucer.RestClientString;

public class SpantusApiServiceImpl {

    private static final String FILE_SIZE_NODE = "fileSize";

    private static final String TIME_STAMP_NODE = "timeStamp";

    private static final String FILE_NAME_NAME = "fileName";

    private static final String DESCRIPTION_NAME = "description";

    private static final Logger LOG = Logger.getLogger(SpantusApiServiceImpl.class);

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final String CORPORA_ENTRY_NODE = "CorporaEntry";

    private String spantusServerApi = "http://spantus.cloudfoundry.com/api/corpora";

    public void delete(String id) {
        RestClientString client = new RestClientString(creteEntryUrlString(id));
        client.AddParam("id", id);
        try {
            client.Execute(RequestMethod.DELETE);
            LOG.debug("Delete response code: {0}; {1}", client.getResponseCode(), client.getResponse());
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    public void udpate(String id, String description) {
        RestClientString client = new RestClientString(creteEntryUrlString(id));
        client.AddParam("id", id);
        client.AddParam("description", description);
        try {
            client.Execute(RequestMethod.POST);
            LOG.debug("Delete response code: {0}; {1}", client.getResponseCode(), client.getResponse());
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    /**
	 * 
	 * @return
	 */
    public Map<String, CorpusItem> findCorpusAllEntries() {
        RestClientDOM client = new RestClientDOM(getSpantusServerApi());
        try {
            client.Execute(RequestMethod.GET);
        } catch (Exception e) {
            LOG.error(e);
        }
        Document response = client.getResponseDOM();
        Map<String, CorpusItem> itemsMap = new HashMap<String, CorpusItem>();
        if (response != null) {
            NodeList nl = response.getElementsByTagName(CORPORA_ENTRY_NODE);
            LOG.debug("Entries found {}", nl.getLength());
            for (int i = 0; i < nl.getLength(); i++) {
                CorpusItem item = new CorpusItem();
                Node node = nl.item(i);
                for (int j = 0; j < node.getChildNodes().getLength(); j++) {
                    Node childNode = node.getChildNodes().item(j);
                    if (FILE_NAME_NAME.equals(childNode.getNodeName())) {
                        item.setFileName(childNode.getNodeValue());
                        item.setId(item.getFileName().replaceAll(".wav", ""));
                    } else if (DESCRIPTION_NAME.equals(childNode.getNodeName())) {
                        item.setDescription(childNode.getNodeValue());
                    } else if (TIME_STAMP_NODE.equals(childNode.getNodeName())) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(Long.valueOf(childNode.getNodeValue()));
                        item.setCreated(cal.getTime());
                    } else if (FILE_SIZE_NODE.equals(childNode.getNodeName())) {
                        item.setFileSize(Long.valueOf(childNode.getNodeValue()));
                    }
                }
                itemsMap.put(item.getId(), item);
            }
        }
        SortedMap<String, CorpusItem> sortedItemsMap = new TreeMap<String, CorpusItem>(new ValueComparer(itemsMap));
        sortedItemsMap.putAll(itemsMap);
        return sortedItemsMap;
    }

    /**
	 * 
	 * @param id
	 * @return
	 */
    public String creteEntryUrlString(String id) {
        String url = MessageFormat.format("{0}/entry/{1}", getSpantusServerApi(), id);
        return url;
    }

    /**
	 * 
	 * @param fileName
	 * @return
	 */
    public URL createPlaybackUrl(String fileName) {
        URL url = null;
        try {
            url = new URL(creteEntryUrlString(fileName));
        } catch (MalformedURLException e) {
            LOG.error(e);
        }
        return url;
    }

    /**
	 * 
	 * @param fileName
	 * @return
	 */
    public Uri createPlaybackUri(String fileName) {
        Uri uri = null;
        uri = Uri.parse(creteEntryUrlString(fileName));
        return uri;
    }

    public String getSpantusServerApi() {
        return spantusServerApi;
    }

    public void setSpantusServerApi(String spantusServerApi) {
        this.spantusServerApi = spantusServerApi;
    }

    private static class ValueComparer implements Comparator<String> {

        private Map<String, CorpusItem> data = null;

        public ValueComparer(Map<String, CorpusItem> data) {
            super();
            this.data = data;
        }

        public int compare(String lhs, String rhs) {
            CorpusItem e1 = (CorpusItem) data.get(lhs);
            CorpusItem e2 = (CorpusItem) data.get(rhs);
            return e2.getCreated().compareTo(e1.getCreated());
        }
    }
}
