package net.sf.mxlosgi.mxlosgixmppbundle;

/**
 * @author noah
 * 
 */
public class Stream extends AbstractXMLStanza {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7016807001323179800L;

    private JID to;

    private JID from;

    private String version;

    private String lang;

    public Stream() {
        this.version = "1.0";
    }

    /**
	 * @return the to
	 */
    public JID getTo() {
        return to;
    }

    /**
	 * @param to
	 *                  the to to set
	 */
    public void setTo(JID to) {
        this.to = to;
    }

    /**
	 * @return the from
	 */
    public JID getFrom() {
        return from;
    }

    /**
	 * @param from
	 *                  the from to set
	 */
    public void setFrom(JID from) {
        this.from = from;
    }

    /**
	 * @return the version
	 */
    public String getVersion() {
        return version;
    }

    /**
	 * @param version
	 *                  the version to set
	 */
    public void setVersion(String version) {
        this.version = version;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String toXML() {
        StringBuffer buf = new StringBuffer();
        buf.append("<stream:stream");
        if (getStanzaID() != null) {
            buf.append(" id=\"").append(getStanzaID()).append("\"");
        }
        if (getTo() != null) {
            buf.append(" to=\"").append(getTo().toFullJID()).append("\"");
        }
        if (getFrom() != null) {
            buf.append(" from=\"").append(getFrom().toFullJID()).append("\"");
        }
        if (getVersion() != null) {
            buf.append(" version=\"").append(getVersion()).append("\"");
        }
        if (getLang() != null) {
            buf.append(" xml:lang=\"").append(getLang()).append("\"");
        }
        buf.append(" xmlns:stream=\"http://etherx.jabber.org/streams\" xmlns=\"jabber:client\"");
        buf.append(">");
        return buf.toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Stream stream = (Stream) super.clone();
        stream.to = this.to;
        stream.from = this.from;
        stream.version = this.version;
        stream.lang = this.lang;
        return stream;
    }
}
