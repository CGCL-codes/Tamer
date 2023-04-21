package org.identifylife.descriptlet.store.model;

import java.io.IOException;
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.identifylife.descriptlet.store.protobuf.ProtobufWrapper;
import org.identifylife.descriptlet.store.protobuf.generated.ProtobufMessages.MeasurementMessage;

@XmlType(propOrder = { "type", "value" })
public class Measurement implements ProtobufWrapper<Measurement, MeasurementMessage> {

    private MeasurementType type = MeasurementType.UNKNOWN;

    private Double value;

    public Measurement() {
    }

    public Measurement(Measurement toCopy) {
        this.type = toCopy.getType();
        this.value = toCopy.getValue();
    }

    public MeasurementType getType() {
        return type;
    }

    public void setType(MeasurementType type) {
        this.type = type;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Measurement) {
            Measurement other = (Measurement) obj;
            if (getType().equals(other.getType()) && getValue().equals(other.getValue())) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return new ToStringBuilder(this).append("type", getType()).append("value", getValue()).toString();
    }

    @Override
    public MeasurementMessage createMessage() {
        MeasurementMessage.Builder builder = MeasurementMessage.newBuilder();
        builder.setType(getType().toInt());
        if (getValue() != null) {
            builder.setValue(getValue());
        }
        return builder.build();
    }

    @Override
    public Measurement fromMessage(MeasurementMessage message) {
        this.setType(MeasurementType.fromInt(message.getType()));
        this.setValue(message.getValue());
        return this;
    }

    @Override
    public Measurement fromMessage(byte[] bytes) throws IOException {
        MeasurementMessage.Builder builder = MeasurementMessage.newBuilder();
        builder.mergeFrom(bytes);
        return fromMessage(builder.build());
    }
}
