package net.disy.ogc.wps.v_1_0_0.converter;

public class LongByteConverter extends LongNumberConverter<Byte> {

    @Override
    public Class<Byte> getDestinationClass() {
        return Byte.class;
    }

    @Override
    protected Byte convertToNotNull(Long source) {
        return Byte.valueOf(source.byteValue());
    }
}
