package com.volantis.mcs.policies.impl.variants.text;

import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.policies.impl.variants.metadata.AbstractMetaDataSingleEncodingBuilder;
import com.volantis.mcs.policies.variants.metadata.MetaDataBuilderVisitor;
import com.volantis.mcs.policies.variants.metadata.Encoding;
import com.volantis.mcs.policies.variants.metadata.MetaData;
import com.volantis.mcs.policies.variants.metadata.MetaDataType;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.policies.variants.text.TextMetaData;
import com.volantis.mcs.policies.variants.text.TextMetaDataBuilder;

public class TextMetaDataBuilderImpl extends AbstractMetaDataSingleEncodingBuilder implements TextMetaDataBuilder {

    private TextMetaData textMetaData;

    private TextEncoding textEncoding;

    private String language;

    public TextMetaDataBuilderImpl(TextMetaData textMetaData) {
        if (textMetaData != null) {
            this.textMetaData = textMetaData;
            textEncoding = textMetaData.getTextEncoding();
            language = textMetaData.getLanguage();
        }
    }

    public TextMetaDataBuilderImpl() {
        this(null);
    }

    public MetaData getMetaData() {
        return getTextMetaData();
    }

    public TextMetaData getTextMetaData() {
        if (textMetaData == null) {
            validate();
            textMetaData = new TextMetaDataImpl(this);
        }
        return textMetaData;
    }

    protected void clearBuiltObject() {
        textMetaData = null;
    }

    public void accept(MetaDataBuilderVisitor visitor) {
        visitor.visit(this);
    }

    public Encoding getEncoding() {
        return getTextEncoding();
    }

    public TextEncoding getTextEncoding() {
        return textEncoding;
    }

    public void setTextEncoding(TextEncoding textEncoding) {
        if (!equals(this.textEncoding, textEncoding)) {
            stateChanged();
        }
        this.textEncoding = textEncoding;
    }

    protected void validateSingleEncodingImpl(ValidationContext context) {
    }

    public MetaDataType getMetaDataType() {
        return MetaDataType.TEXT;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        if (!equals(this.language, language)) {
            stateChanged();
        }
        this.language = language;
    }

    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof TextMetaDataBuilderImpl) ? equalsSpecific((TextMetaDataBuilderImpl) obj) : false;
    }

    /**
     * @see #equalsSpecific(com.volantis.mcs.policies.impl.EqualsHashCodeBase)
     */
    protected boolean equalsSpecific(TextMetaDataBuilderImpl other) {
        return super.equalsSpecific(other);
    }

    public int hashCode() {
        return super.hashCode();
    }
}
