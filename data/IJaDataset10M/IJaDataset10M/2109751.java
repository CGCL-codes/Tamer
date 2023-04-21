package org.mcisb.beacon.pierre;

import java.net.URL;
import java.util.UUID;
import pedro.util.Parameter;
import pierre.reports.HyperLinkObject;
import pierre.reports.ReportFileFormat;

/**
 * 
 * @author Neil Swainston
 */
class HyperLinkObjectFactory {

    /**
	 * 
	 */
    private final URL linkUrl;

    /**
	 * 
	 * @param linkUrl
	 */
    HyperLinkObjectFactory(URL linkUrl) {
        this.linkUrl = linkUrl;
    }

    /**
	 * 
	 * @param parameters
	 * @param hyperlinkObjectName
	 * @param textLink
	 * @return HyperLinkObject
	 * @throws Exception
	 */
    HyperLinkObject getHyperlinkObject(final Parameter[] parameters, final String hyperlinkObjectName, final String textLink) throws Exception {
        final StringBuffer buffer = new StringBuffer();
        buffer.append(linkUrl);
        buffer.append("?");
        buffer.append("id=" + UUID.randomUUID().toString());
        final HyperLinkObject hyperLinkObject = new HyperLinkObject();
        hyperLinkObject.setURL(new URL(buffer.toString()), hyperlinkObjectName);
        hyperLinkObject.setLinkText(textLink, ReportFileFormat.TEXT_FORMAT);
        hyperLinkObject.setParameters(parameters);
        return hyperLinkObject;
    }
}
