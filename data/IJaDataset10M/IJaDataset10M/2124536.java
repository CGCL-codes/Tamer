package net.xm;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BackpackTodoListManager extends AbstractTodoListManager implements TodoListManager {

    public BackpackTodoListManager(Configuration conf) throws ApplicationException {
        this.configuration = conf;
        if (conf.getProxyHost() != null) {
            props.put("http.proxyHost", conf.getProxyHost());
        }
        if (conf.getProxyPort() != null) {
            props.put("http.proxyPort", conf.getProxyPort());
        }
    }

    public TodoList addItem(TodoListItem item) throws ApplicationException {
        post(configuration.getBackpackUrl() + "ws/page/" + configuration.getBackpackPage() + "/items/add", item.getContent());
        Document doc = post(configuration.getBackpackUrl() + "ws/page/" + configuration.getBackpackPage() + "/items/list", null);
        return rebuildList(doc);
    }

    public TodoList completeItem(TodoListItem item) throws ApplicationException {
        post(configuration.getBackpackUrl() + "ws/page/" + configuration.getBackpackPage() + "/items/toggle/" + item.getId(), null);
        Document doc = post(configuration.getBackpackUrl() + "ws/page/" + configuration.getBackpackPage() + "/items/list/", null);
        return rebuildList(doc);
    }

    public TodoList deleteItem(TodoListItem item) throws ApplicationException {
        post(configuration.getBackpackUrl() + "ws/page/" + configuration.getBackpackPage() + "/items/destroy/" + item.getId(), null);
        Document doc = post(configuration.getBackpackUrl() + "ws/page/" + configuration.getBackpackPage() + "/items/list/", null);
        return rebuildList(doc);
    }

    public TodoList getAllItems() throws ApplicationException {
        Document doc = post(configuration.getBackpackUrl() + "ws/page/" + configuration.getBackpackPage() + "/items/list", null);
        return rebuildList(doc);
    }

    public TodoListItem getItem(String id) throws ApplicationException {
        return null;
    }

    public TodoList uncompleteItem(TodoListItem item) throws ApplicationException {
        post(configuration.getBackpackUrl() + "ws/page/" + configuration.getBackpackPage() + "/items/toggle/" + item.getId(), null);
        Document doc = post(configuration.getBackpackUrl() + "ws/page/" + configuration.getBackpackPage() + "/items/list", null);
        return rebuildList(doc);
    }

    private TodoList rebuildList(Document doc) throws ApplicationException {
        TodoList resultList = new TodoList();
        try {
            NodeList list = XmlUtils.selectNodes(doc, "/response/items/item");
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                String content = XmlUtils.extractSingleElementValue(node, ".");
                TodoListItem item = new TodoListItem(content);
                item.setId(XmlUtils.extractSingleAttributeValue(node, "id"));
                item.setCompleted(Boolean.valueOf(XmlUtils.extractSingleAttributeValue(node, "completed")));
                resultList.add(item);
            }
        } catch (DOMException e) {
            throw new ApplicationException(e);
        }
        return resultList;
    }

    private Document post(String location, String content) throws ApplicationException {
        Document doc = null;
        try {
            URL url = new URL(location);
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();
            uc.setRequestMethod("POST");
            uc.setRequestProperty("Content-Type", "application/xml");
            uc.setRequestProperty("X-POST_DATA_FORMAT", "xml");
            uc.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(uc.getOutputStream());
            out.write("<request>");
            out.write("<token>" + configuration.getBackpackPassword() + "</token>");
            if (content != null) {
                out.write("<item><content>" + content + "</content></item>");
            }
            out.write("</request>");
            out.close();
            doc = XmlUtils.readDocumentFromInputStream(uc.getInputStream());
            System.out.println(XmlUtils.toString(doc));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApplicationException(e);
        }
        return doc;
    }
}
