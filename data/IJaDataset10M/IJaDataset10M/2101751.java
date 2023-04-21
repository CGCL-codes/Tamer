package net.sf.mxlosgi.xmpp;

/**
 * SASL <response/> element
 * @author noah
 * 
 */
public class Response implements XmlStanza {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8747434940677963887L;

    private String content;

    public Response() {
    }

    /**
	 * @return the content
	 */
    public String getContent() {
        return content;
    }

    /**
	 * @param content
	 *                  the content to set
	 */
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toXML() {
        StringBuffer buf = new StringBuffer();
        buf.append("<response xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\"");
        if (getContent() != null) {
            buf.append(">");
            buf.append(getContent());
            buf.append("</response>");
        } else {
            buf.append("/>");
        }
        return buf.toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Response response = (Response) super.clone();
        response.content = this.content;
        return response;
    }
}
