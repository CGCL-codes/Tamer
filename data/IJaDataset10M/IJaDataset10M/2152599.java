package net.disy.ogc.wps.v_1_0_0.model;

import static org.apache.commons.lang.Validate.*;
import java.text.MessageFormat;

public class FormatId {

    private final String mimeType;

    private final String encoding;

    private final String schemaDesignator;

    public FormatId(String mimeType, String encoding, String schemaDesignator) {
        notNull(mimeType);
        notNull(encoding);
        notNull(schemaDesignator);
        this.mimeType = mimeType;
        this.encoding = encoding;
        this.schemaDesignator = schemaDesignator;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getEncoding() {
        return encoding;
    }

    public String getSchemaDesignator() {
        return schemaDesignator;
    }

    @Override
    public int hashCode() {
        return mimeType.hashCode() + encoding.hashCode() + schemaDesignator.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FormatId)) {
            return false;
        }
        FormatId other = (FormatId) o;
        return mimeType.equals(other.mimeType) && encoding.equals(other.encoding) && schemaDesignator.equals(other.schemaDesignator);
    }

    @Override
    public String toString() {
        return MessageFormat.format("FormatId [mimeType={0}, encoding={1}, schemaDesignator={2}]", mimeType, encoding, schemaDesignator);
    }
}
