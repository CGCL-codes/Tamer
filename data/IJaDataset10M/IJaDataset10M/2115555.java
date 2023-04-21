package yarfraw.core.datamodel;

import yarfraw.utils.ValidationUtils;

/**
 * <b>This is only used by Rss 2.0.</b>
 * 
 * <p>
 * It specifies a web service that supports the rssCloud interface which can be implemented in HTTP-POST, XML-RPC or SOAP 1.1.
 * Its purpose is to allow processes to register with a cloud to be notified of updates to the channel, implementing a lightweight publish-subscribe protocol for RSS feeds.
 * <cloud domain="rpc.sys.com" port="80" path="/RPC2" registerProcedure="myCloud.rssPleaseNotify" protocol="xml-rpc" />
 * In this example, to request notification on the channel it appears in, you would send an XML-RPC message to rpc.sys.com on port 80, with a path of /RPC2. The procedure to call is myCloud.rssPleaseNotify.
 * <br/>
 * A full explanation of this element and the rssCloud interface is here.
 * <br/>
 * http://cyber.law.harvard.edu/rss/soapMeetsRss.html#rsscloudInterface
 * <p/>
 * Example: &lt;cloud domain="rpc.sys.com" port="80" path="/RPC2" registerProcedure="pingMe" protocol="soap"/>
 * @author jliang
 *
 */
public class Cloud extends AbstractBaseObject {

    private String _domain;

    private String _port;

    private String _path;

    private String _registerProcedure;

    private String _protocol;

    public Cloud() {
    }

    public static Cloud create() {
        return new Cloud();
    }

    public Cloud(String domain, String port, String path, String registerProcedure, String protocol) {
        super();
        setDomain(domain);
        setPath(path);
        setPort(port);
        setRegisterProcedure(registerProcedure);
        setProtocol(protocol);
    }

    public String getDomain() {
        return _domain;
    }

    public Cloud setDomain(String domain) {
        _domain = domain;
        return this;
    }

    public String getPort() {
        return _port;
    }

    public void setPort(String port) {
        _port = port;
    }

    public String getPath() {
        return _path;
    }

    public Cloud setPath(String path) {
        _path = path;
        return this;
    }

    public String getRegisterProcedure() {
        return _registerProcedure;
    }

    public Cloud setRegisterProcedure(String registerProcedure) {
        _registerProcedure = registerProcedure;
        return this;
    }

    public String getProtocol() {
        return _protocol;
    }

    public Cloud setProtocol(String protocol) {
        _protocol = protocol;
        return this;
    }

    @Override
    public void validate(FeedFormat format) throws ValidationException {
        if (format != FeedFormat.RSS20) {
            return;
        }
        ValidationUtils.validateNotNull("Cloud: All fields in the cloud object should be not null", _domain, _path, _port, _protocol, _registerProcedure);
        if (!_protocol.equals("xml-rpc") && !_protocol.equals("http-post") && !_protocol.equals("soap")) {
            throw new ValidationException("Cloud: Protocol should be one of the following: xml-rpc, soap, http-post");
        }
        if (_port != null && (Integer.parseInt(_port) < 0 || Integer.parseInt(_port) > 65535)) {
            throw new ValidationException("Cloud: Invalid port number");
        }
    }
}
