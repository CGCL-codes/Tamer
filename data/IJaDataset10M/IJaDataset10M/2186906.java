package net.sourceforge.hlagile.hlaabstraction.encoding;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import hla.rti1516e.encoding.ByteWrapper;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.EncoderException;
import hla.rti1516e.encoding.HLAfloat64LE;

/**
 * Implementation class for the data type of a 64-bit little-endian floating point number.
 *  
 * @author franck
 *
 */
public class HLAfloat64LEImpl implements HLAfloat64LE {

    private ByteBuffer _value;

    private ByteWrapper _encodedRep;

    public HLAfloat64LEImpl() {
        _value = ByteBuffer.allocate(8);
        _value.order(ByteOrder.LITTLE_ENDIAN);
    }

    @Override
    public double getValue() {
        return _value.getDouble();
    }

    @Override
    public void setValue(double value) {
        _value.putDouble(value);
        _encodedRep = null;
    }

    @Override
    public void decode(ByteWrapper byteWrapper) throws DecoderException {
        _value.put(byteWrapper.array());
        _encodedRep = null;
    }

    @Override
    public void decode(byte[] bytes) throws DecoderException {
        _value.put(bytes);
        _encodedRep = null;
    }

    private void createEncodedRepresentation() {
        if (_encodedRep == null) {
            byte[] b = _value.array();
            _encodedRep = new ByteWrapper(b);
        }
    }

    @Override
    public void encode(ByteWrapper byteWrapper) throws EncoderException {
        createEncodedRepresentation();
        byteWrapper = _encodedRep.slice();
    }

    @Override
    public int getEncodedLength() {
        createEncodedRepresentation();
        return _encodedRep.array().length;
    }

    @Override
    public int getOctetBoundary() {
        return 0;
    }

    @Override
    public byte[] toByteArray() throws EncoderException {
        return _value.array();
    }
}
