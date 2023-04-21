package org.pubcurator.importers.docs.parsers;

import java.io.InputStream;
import java.util.Stack;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.eclipse.core.runtime.IProgressMonitor;

public class EutilsESearchParser {

    private IProgressMonitor monitor;

    private XMLInputFactory factory;

    private StringBuffer buffer;

    private Stack<String> inside;

    private int depth = 0;

    private int count;

    private String webEnv;

    private String queryKey;

    public EutilsESearchParser() {
        inside = new Stack<String>();
        factory = XMLInputFactory.newFactory("com.ctc.wstx.stax.WstxInputFactory", EutilsESearchParser.class.getClassLoader());
        buffer = new StringBuffer();
    }

    public void checkIfUserCanceled() throws InterruptedException {
        if (monitor != null && monitor.isCanceled()) {
            throw new InterruptedException();
        }
    }

    public void setMonitor(IProgressMonitor monitor) {
        this.monitor = monitor;
    }

    public void parse(InputStream input) throws XMLStreamException, InterruptedException {
        XMLStreamReader parser = factory.createXMLStreamReader(input);
        while (parser.hasNext()) {
            checkIfUserCanceled();
            switch(parser.getEventType()) {
                case XMLStreamConstants.START_ELEMENT:
                    buffer.setLength(0);
                    inside.push(parser.getLocalName());
                    depth++;
                    break;
                case XMLStreamConstants.CHARACTERS:
                    buffer.append(parser.getText());
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (!parser.isWhiteSpace()) {
                        if (inside.contains("Count") && depth == 2) {
                            count = Integer.parseInt(buffer.toString());
                        } else if (inside.contains("WebEnv") && depth == 2) {
                            webEnv = buffer.toString();
                        } else if (inside.contains("QueryKey") && depth == 2) {
                            queryKey = buffer.toString();
                        }
                    }
                    inside.pop();
                    depth--;
                    break;
                default:
                    break;
            }
            parser.next();
        }
    }

    /**
	 * @return the count
	 */
    public int getCount() {
        return count;
    }

    /**
	 * @param count the count to set
	 */
    public void setCount(int count) {
        this.count = count;
    }

    /**
	 * @return the webEnv
	 */
    public String getWebEnv() {
        return webEnv;
    }

    /**
	 * @param webEnv the webEnv to set
	 */
    public void setWebEnv(String webEnv) {
        this.webEnv = webEnv;
    }

    /**
	 * @return the queryKey
	 */
    public String getQueryKey() {
        return queryKey;
    }

    /**
	 * @param queryKey the queryKey to set
	 */
    public void setQueryKey(String queryKey) {
        this.queryKey = queryKey;
    }
}
