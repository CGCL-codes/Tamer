package org.bresearch.octane.net;

import org.bresearch.octane.parse.ParseHtml;

public interface IHttpConnect {

    public void buildConnectProperties();

    public void connect(final URLConnectAdapter urlAdapter);

    public ConnectSettingsBean getConnectSettings();

    public ConnectResult getLastResult();

    public URLConnectAdapter buildURL();

    public ParseHtml getParser();

    public void setParser(ParseHtml parser);

    public void parse();
}
