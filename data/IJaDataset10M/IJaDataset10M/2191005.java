package library.corba;

/**
* library/corba/AddedDocumentHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ../idl/CORBALibrary.idl
* Monday, July 12, 2010 5:38:18 PM CEST
*/
public final class AddedDocumentHolder implements org.omg.CORBA.portable.Streamable {

    public library.corba.AddedDocument value = null;

    public AddedDocumentHolder() {
    }

    public AddedDocumentHolder(library.corba.AddedDocument initialValue) {
        value = initialValue;
    }

    public void _read(org.omg.CORBA.portable.InputStream i) {
        value = library.corba.AddedDocumentHelper.read(i);
    }

    public void _write(org.omg.CORBA.portable.OutputStream o) {
        library.corba.AddedDocumentHelper.write(o, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return library.corba.AddedDocumentHelper.type();
    }
}
