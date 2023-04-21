package tr.com.srdc.isurf.xmlbuilders;

import org.w3c.dom.Document;

public interface MessageBuilder {

    int constructHeader();

    int constructMessage();

    int buildMessage();

    int sendMessage(Document stNode);
}
