package net.sf.magicmap.client.model.info;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import net.sf.magicmap.client.model.node.InfoObjectNode;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * Pareses HTML and uses all anchors.
 * 
 * @author Jan Friderici
 *
 */
public class SimpleInfoObjectProvider implements InfoObjectProvider {

    public SimpleInfoObjectProvider() {
    }

    public void addEdge(InfoObjectNode source, InfoObjectNode target, String[][] data) {
    }

    public InfoObjectNode createNode() throws Exception {
        return null;
    }

    public void expandNode(InfoObjectNode node) throws Exception {
        String urlToLoad = node.getInfoUrl();
        HttpClient client = new HttpClient();
        GetMethod m = new GetMethod(urlToLoad);
        client.executeMethod(m);
        StringBuffer buffer = new StringBuffer();
        BufferedReader in = new BufferedReader(new InputStreamReader(m.getResponseBodyAsStream()));
        String line = in.readLine();
        while (line != null) {
            buffer.append(line);
            line = in.readLine();
        }
        parseHtml(buffer.toString());
    }

    private void parseHtml(String string) {
    }
}
