package pl.edu.agh.sius.ws.mail.stubs;

public class ArrayOfAddress {

    private pl.edu.agh.sius.ws.mail.stubs.Address[] address;

    public ArrayOfAddress() {
    }

    public pl.edu.agh.sius.ws.mail.stubs.Address[] getAddress() {
        return address;
    }

    public void setAddress(pl.edu.agh.sius.ws.mail.stubs.Address[] address) {
        this.address = address;
    }

    public pl.edu.agh.sius.ws.mail.stubs.Address getAddress(int i) {
        return address[i];
    }

    public void setAddress(int i, pl.edu.agh.sius.ws.mail.stubs.Address value) {
        this.address[i] = value;
    }

    public static javax.microedition.xml.rpc.Type populateTypeMap(javax.xml.namespace.QName targetName, java.util.Hashtable typeMap) {
        if (typeMap == null) {
            typeMap = new java.util.Hashtable();
        }
        String complexTypeName = "pl_edu_agh_sius_ws_mail_stubs_ArrayOfAddress";
        if (typeMap.containsKey(complexTypeName)) {
            return (javax.microedition.xml.rpc.Type) typeMap.get(complexTypeName);
        }
        javax.microedition.xml.rpc.ComplexType pl_edu_agh_sius_ws_mail_stubs_ArrayOfAddress = new javax.microedition.xml.rpc.ComplexType();
        pl_edu_agh_sius_ws_mail_stubs_ArrayOfAddress.elements = new javax.microedition.xml.rpc.Element[1];
        {
            javax.xml.namespace.QName __QNAME_address = new javax.xml.namespace.QName("http://mail.javax", "Address");
            javax.microedition.xml.rpc.Element __ELEM_address = new javax.microedition.xml.rpc.Element(__QNAME_address, pl.edu.agh.sius.ws.mail.stubs.Address.populateTypeMap(null, typeMap), 0, -1, true);
            pl_edu_agh_sius_ws_mail_stubs_ArrayOfAddress.elements[0] = __ELEM_address;
        }
        typeMap.put(complexTypeName, pl_edu_agh_sius_ws_mail_stubs_ArrayOfAddress);
        return pl_edu_agh_sius_ws_mail_stubs_ArrayOfAddress;
    }

    private boolean recursionCheck = false;

    public Object serialize() throws javax.xml.rpc.JAXRPCException {
        if (recursionCheck) {
            throw new javax.xml.rpc.JAXRPCException("An instance of " + getClass().getName() + " exists in a circular reference.  Serialization of this instance will fail.");
        } else {
            recursionCheck = true;
        }
        Object[] objs = new Object[] { serialize(getAddress()) };
        recursionCheck = false;
        return (Object) objs;
    }

    public void deserialize(Object value) {
        Object[] values = (Object[]) value;
        if ((values != null) && (values.length == 1)) {
            if (values[0] != null) {
                Object objs = (Object) values[0];
                setAddress(deserialize_pl_edu_agh_sius_ws_mail_stubs_Address(objs));
            }
        }
    }

    protected Object[] serialize(pl.edu.agh.sius.ws.mail.stubs.Address[] obj) throws javax.xml.rpc.JAXRPCException {
        Object[] data = null;
        if (obj != null) {
            data = new Object[obj.length];
            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null) {
                    data[i] = obj[i].serialize();
                }
            }
        }
        return data;
    }

    protected pl.edu.agh.sius.ws.mail.stubs.Address[] deserialize_pl_edu_agh_sius_ws_mail_stubs_Address(Object inputObjs) {
        pl.edu.agh.sius.ws.mail.stubs.Address[] data = null;
        Object[] values = (Object[]) inputObjs;
        if (values != null) {
            data = new pl.edu.agh.sius.ws.mail.stubs.Address[values.length];
            for (int i = 0; i < values.length; i++) {
                if (values[i] != null) {
                    Object objs = (Object) values[i];
                    if ((objs != null)) {
                        data[i] = new pl.edu.agh.sius.ws.mail.stubs.Address();
                        data[i].deserialize(objs);
                    }
                }
            }
        }
        return data;
    }
}
