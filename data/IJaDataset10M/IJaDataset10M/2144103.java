package org.grailrtls.libworldmodel.solver.protocol.messages;

import java.io.UnsupportedEncodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TypeAnnounceMessage {

    /**
	 * Logging facility for this class.
	 */
    private static final Logger log = LoggerFactory.getLogger(TypeAnnounceMessage.class);

    public static final byte MESSAGE_TYPE = 1;

    private TypeSpecification[] typeSpecifications = null;

    private String origin = null;

    public int getMessageLength() {
        int length = 1 + 4;
        if (this.typeSpecifications != null) {
            for (TypeSpecification spec : this.typeSpecifications) {
                length += (4 + 4 + 1);
                if (spec.getUriName() != null) {
                    try {
                        length += spec.getUriName().getBytes("UTF-16BE").length;
                    } catch (UnsupportedEncodingException e) {
                        log.error("Unable to encode UTF-16BE strings.");
                    }
                }
            }
        }
        if (this.origin != null) {
            try {
                length += this.origin.getBytes("UTF-16BE").length;
            } catch (UnsupportedEncodingException e) {
                log.error("Unable to encode UTF-16 strings.");
                e.printStackTrace();
            }
        }
        return length;
    }

    public TypeSpecification[] getTypeSpecifications() {
        return typeSpecifications;
    }

    public void setTypeSpecifications(TypeSpecification[] typeSpecifications) {
        this.typeSpecifications = typeSpecifications;
    }

    public static class TypeSpecification {

        private int typeAlias;

        private String uriName;

        private boolean isTransient = false;

        public int getTypeAlias() {
            return typeAlias;
        }

        public void setTypeAlias(int typeAlias) {
            this.typeAlias = typeAlias;
        }

        public String getUriName() {
            return uriName;
        }

        public void setUriName(String uriName) {
            this.uriName = uriName;
        }

        public boolean getIsTransient() {
            return isTransient;
        }

        public void setIsTransient(boolean isTransient) {
            this.isTransient = isTransient;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof TypeSpecification) {
                return this.equals((TypeSpecification) o);
            }
            return false;
        }

        public boolean equals(TypeSpecification o) {
            if (this.isTransient != o.isTransient) {
                return false;
            }
            if (this.uriName == null && o.uriName == null) {
                return true;
            }
            return this.uriName.equals(o.uriName);
        }

        public String toString() {
            StringBuffer sb = new StringBuffer();
            if (this.isTransient) {
                sb.append("[T] ");
            }
            if (this.uriName != null) {
                sb.append(this.uriName).append("->");
            }
            sb.append(this.typeAlias);
            return sb.toString();
        }
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("Type Announce ");
        if (this.origin != null) {
            sb.append('@').append(this.origin);
        }
        sb.append('\n');
        if (this.typeSpecifications != null) {
            log.debug("{} type specs.", this.typeSpecifications.length);
            for (TypeSpecification spec : this.typeSpecifications) {
                sb.append('\t').append(spec.toString()).append('\n');
            }
        }
        return sb.toString();
    }
}
