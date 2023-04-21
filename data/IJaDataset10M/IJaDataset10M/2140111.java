package com.headcaselabs.lb.common;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * A NS record.
 * @author Florian Leibert
 */
public class Record implements Serializable {

    private static final long serialVersionUID = 2262389743335838639L;

    public static final int DEFAULT_DYN_TTL = 0;

    public static final String DEFAULT_ID = "1";

    private String name;

    private String zone;

    private String type;

    private String netClass = "IN";

    private List<String> content;

    private Integer ttl;

    private Boolean loadbalanced = false;

    private String pdnsString;

    private String id = DEFAULT_ID;

    public static final String SEP = "\t";

    public static final String DATA = "DATA";

    public static final String EOL = "\n";

    /**
	 * Default Constructor
	 */
    public Record() {
        content = new LinkedList<String>();
    }

    /**
	 * Full Constructor
	 * @param name
	 * @param type
	 */
    public Record(String name, String type) {
        this.name = name;
        this.type = type;
        content = new LinkedList<String>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNetClass() {
        return netClass;
    }

    public void setNetClass(String netClass) {
        this.netClass = netClass;
    }

    public Integer getTtl() {
        return ttl;
    }

    public void setTtl(Integer ttl) {
        this.ttl = ttl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
	 * Returns a NS answer - a String which PowerDNS understands.
	 * @return a String that can be interpreted by PowerDNS
	 */
    public String getPdnsString() {
        if (pdnsString == null) {
            StringBuffer sb = new StringBuffer();
            for (String entry : content) {
                sb.append(DATA);
                sb.append(SEP);
                sb.append(name);
                sb.append(SEP);
                sb.append(netClass);
                sb.append(SEP);
                sb.append(type);
                sb.append(SEP);
                sb.append(ttl);
                sb.append(SEP);
                sb.append(id);
                sb.append(SEP);
                sb.append(entry);
                sb.append(EOL);
            }
            pdnsString = sb.toString();
        }
        return pdnsString;
    }

    /**
	 * @param pdnsString
	 */
    public void setPdnsString(String pdnsString) {
        this.pdnsString = pdnsString;
    }

    /**
	 * @return loadbalanced
	 */
    public Boolean getLoadbalanced() {
        return loadbalanced;
    }

    /**
	 * @param loadbalanced
	 */
    public void setLoadbalanced(Boolean loadbalanced) {
        this.loadbalanced = loadbalanced;
    }

    /**
	 * @return the content
	 */
    public List<String> getContent() {
        return content;
    }

    /**
	 * @param content the content to set
	 */
    public void setContent(List<String> content) {
        this.content = content;
    }

    /**
	 * @return the id
	 */
    public String getId() {
        return id;
    }

    /**
	 * @param id the id to set
	 */
    public void setId(String id) {
        this.id = id;
    }

    /**
	 * @return the zone
	 */
    public String getZone() {
        return zone;
    }

    /**
	 * @param zone the zone to set
	 */
    public void setZone(String zone) {
        this.zone = zone;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = super.hashCode();
        result = PRIME * result + ((content == null) ? 0 : content.hashCode());
        result = PRIME * result + ((loadbalanced == null) ? 0 : loadbalanced.hashCode());
        result = PRIME * result + ((name == null) ? 0 : name.hashCode());
        result = PRIME * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        final Record other = (Record) obj;
        if (content == null) {
            if (other.content != null) return false;
        } else if (!content.equals(other.content)) return false;
        if (loadbalanced == null) {
            if (other.loadbalanced != null) return false;
        } else if (!loadbalanced.equals(other.loadbalanced)) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        if (type == null) {
            if (other.type != null) return false;
        } else if (!type.equals(other.type)) return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("");
        sb.append("Record: hash:");
        sb.append(hashCode());
        sb.append(" [ class: ");
        sb.append(netClass);
        sb.append(" ; name: ");
        sb.append(name);
        sb.append(" ; type: ");
        sb.append(type);
        sb.append(" ; ttl: ");
        sb.append(ttl);
        sb.append(" ; loadbalanced: ");
        sb.append(loadbalanced);
        sb.append(" ; content: [ ");
        for (String c : content) {
            sb.append(c);
            sb.append(",");
        }
        sb.delete(sb.length() - 1, sb.length());
        sb.append(" ]");
        sb.append("]");
        return sb.toString();
    }
}
