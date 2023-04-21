package org.identifylife.key.engine.core.model.taxonomy;

import java.io.IOException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.identifylife.key.engine.core.model.Model;
import org.identifylife.key.engine.core.protobuf.ProtobufWrapper;
import org.identifylife.key.engine.core.protobuf.generated.ProtobufMessages.TaxonNameMessage;

/**
 * @author dbarnier
 *
 */
@XmlRootElement(name = "taxon_name")
@XmlAccessorType(XmlAccessType.FIELD)
public class TaxonName extends Model implements ProtobufWrapper<TaxonName, TaxonNameMessage> {

    private String name;

    @XmlAttribute
    private boolean vernacular;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVernacular() {
        return vernacular;
    }

    public void setVernacular(boolean vernacular) {
        this.vernacular = vernacular;
    }

    @Override
    public TaxonNameMessage createMessage() {
        TaxonNameMessage.Builder builder = TaxonNameMessage.newBuilder();
        builder.setName(getName());
        builder.setVernacular(isVernacular());
        return builder.build();
    }

    @Override
    public TaxonName fromMessage(TaxonNameMessage message) {
        this.setName(message.getName());
        this.setVernacular(message.getVernacular());
        return this;
    }

    @Override
    public TaxonName fromMessage(byte[] bytes) throws IOException {
        TaxonNameMessage.Builder builder = TaxonNameMessage.newBuilder();
        builder.mergeFrom(bytes);
        return fromMessage(builder.build());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString()).append("name", getName()).append("vernacular", isVernacular()).toString();
    }
}
