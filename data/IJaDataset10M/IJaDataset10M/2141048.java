package javax.jmdns;

/**
 * DNS entry with a name, type, and class. This is the base
 * class for questions and records.
 *
 * @author	Arthur van Hoff
 * @version 	%I%, %G%
 */
abstract class DNSEntry extends DNSConstants {

    String key;

    String name;

    int type;

    int clazz;

    boolean unique;

    /**
     * Create an entry.
     */
    DNSEntry(String name, int type, int clazz) {
        this.key = name.toLowerCase();
        this.name = name;
        this.type = type;
        this.clazz = clazz & CLASS_MASK;
        this.unique = (clazz & CLASS_UNIQUE) != 0;
    }

    /**
     * Check if two entries have exactly the same name, type, and class.
     */
    public boolean equals(Object obj) {
        if (obj instanceof DNSEntry) {
            DNSEntry other = (DNSEntry) obj;
            return name.equals(other.name) && type == other.type && clazz == other.clazz;
        }
        return false;
    }

    /**
     * Get a string given a clazz.
     */
    static String getClazz(int clazz) {
        switch(clazz & CLASS_MASK) {
            case CLASS_IN:
                return "in";
            case CLASS_CS:
                return "cs";
            case CLASS_CH:
                return "ch";
            case CLASS_HS:
                return "hs";
            case CLASS_NONE:
                return "none";
            case CLASS_ANY:
                return "any";
            default:
                return "?";
        }
    }

    /**
     * Get a string given a type.
     */
    static String getType(int type) {
        switch(type) {
            case TYPE_A:
                return "a";
            case TYPE_NS:
                return "ns";
            case TYPE_MD:
                return "md";
            case TYPE_MF:
                return "mf";
            case TYPE_CNAME:
                return "cname";
            case TYPE_SOA:
                return "soa";
            case TYPE_MB:
                return "mb";
            case TYPE_MG:
                return "mg";
            case TYPE_MR:
                return "mr";
            case TYPE_NULL:
                return "null";
            case TYPE_WKS:
                return "wks";
            case TYPE_PTR:
                return "ptr";
            case TYPE_HINFO:
                return "hinfo";
            case TYPE_MINFO:
                return "minfo";
            case TYPE_MX:
                return "mx";
            case TYPE_TXT:
                return "txt";
            case TYPE_SRV:
                return "srv";
            case TYPE_ANY:
                return "any";
            default:
                return "?";
        }
    }

    public String toString(String hdr, String other) {
        return hdr + "[" + getType(type) + "," + getClazz(clazz) + (unique ? "-unique," : ",") + name + ((other != null) ? "," + other + "]" : "]");
    }
}
