package framework.libraries.serialization;

import uka.transport.Transportable;

public class TString implements Transportable {

    private char[] value;

    public TString() {
        this.value = new char[0];
    }

    public TString(String string) {
        this.value = string.toCharArray();
    }

    public TString(char[] value) {
        this.value = value;
    }

    private int hash;

    public int hashCode() {
        int h = hash;
        int len = value.length;
        if (h == 0 && len != 0) {
            int off = 0;
            char val[] = value;
            for (int i = 0; i < len; i++) {
                h = 31 * h + val[off++];
            }
            hash = h;
        }
        return h;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof TString) {
            TString str = (TString) obj;
            int n = value.length;
            if (n == str.value.length) {
                char v1[] = value;
                char v2[] = str.value;
                int i = 0;
                int j = 0;
                while (n-- != 0) {
                    if (v1[i++] != v2[j++]) return false;
                }
                return true;
            }
        }
        return false;
    }

    public int compareTo(Object obj) {
        throw new InternalError("TString not implementing compareTo");
    }

    public String toString() {
        return new String(value);
    }

    protected static final int _SIZE = uka.transport.BasicIO.SIZEOF_int;

    private transient int _length;

    /** Used by uka.transport.UnmarshalStream to unmarshal the object */
    public TString(uka.transport.UnmarshalStream _stream) throws java.io.IOException, ClassNotFoundException {
        this(_stream, _SIZE);
        _stream.accept(_SIZE);
    }

    protected TString(uka.transport.UnmarshalStream _stream, int _size) throws java.io.IOException, ClassNotFoundException {
        _stream.request(_size);
        byte[] _buffer = _stream.getBuffer();
        int _pos = _stream.getPosition();
        _length = uka.transport.BasicIO.extractInt(_buffer, _pos);
        _pos += uka.transport.BasicIO.SIZEOF_int;
    }

    /** Method of interface Transportable, it must be declared public.
   It is called from within UnmarshalStream after creating the 
   object and assigning a stream reference to it. */
    public void unmarshalReferences(uka.transport.UnmarshalStream _stream) throws java.io.IOException, ClassNotFoundException {
        int _size = _length * uka.transport.BasicIO.SIZEOF_char;
        if (_size > uka.transport.BasicIO.REQUEST_MAX) {
            value = new String((byte[]) _stream.readObject()).toCharArray();
        } else {
            _stream.request(_size);
            byte[] _buffer = _stream.getBuffer();
            int _pos = _stream.getPosition();
            value = new char[_length];
            _pos = uka.transport.BasicIO.extract(_buffer, _pos, value);
            _stream.accept(_size);
        }
    }

    /** Called directly by uka.transport.MarshalStream */
    public void marshal(uka.transport.MarshalStream _stream) throws java.io.IOException {
        _stream.reserve(_SIZE);
        byte[] _buffer = _stream.getBuffer();
        int _pos = _stream.getPosition();
        marshalPrimitives(_buffer, _pos);
        _stream.deliver(_SIZE);
        marshalReferences(_stream);
    }

    protected void marshalPrimitives(byte[] _buffer, int _pos) throws java.io.IOException {
        _length = value.length;
        _pos = uka.transport.BasicIO.insert(_buffer, _pos, _length);
    }

    protected void marshalReferences(uka.transport.MarshalStream _stream) throws java.io.IOException {
        int _size = _length * uka.transport.BasicIO.SIZEOF_char;
        if (_size > uka.transport.BasicIO.REQUEST_MAX) {
            _stream.writeObject((new String(value)).getBytes());
        } else {
            _stream.reserve(_size);
            byte[] _buffer = _stream.getBuffer();
            int _pos = _stream.getPosition();
            _pos = uka.transport.BasicIO.insert(_buffer, _pos, value);
            _stream.deliver(_size);
        }
    }

    public final Object deepClone(uka.transport.DeepClone _helper) throws CloneNotSupportedException {
        Object _copy = clone();
        _helper.add(this, _copy);
        ((TString) _copy).deepCloneReferences(_helper);
        return _copy;
    }

    /** Clone all references to other objects. Use the 
   DeepClone to resolve cycles */
    protected void deepCloneReferences(uka.transport.DeepClone _helper) throws CloneNotSupportedException {
        this.value = _helper.doDeepClone(this.value);
    }
}
