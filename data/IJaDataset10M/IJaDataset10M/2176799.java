package _FACTORY;

public class _SerialServerFactoryStub extends org.omg.CORBA.portable.ObjectImpl implements _FACTORY.SerialServerFactory {

    public int create(String url_ref, int serial_id) throws OBJECTS.CreationFailed {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("create", true);
            OBJECTS.TYPE_STRINGHelper.write($out, url_ref);
            OBJECTS.TYPE_OIDHelper.write($out, serial_id);
            $in = _invoke($out);
            int $result = OBJECTS.TYPE_OIDHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            if (_id.equals("IDL:OBJECTS/CreationFailed:1.0")) throw OBJECTS.CreationFailedHelper.read($in); else throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return create(url_ref, serial_id);
        } finally {
            _releaseReply($in);
        }
    }

    public int remove(int server_id) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("remove", true);
            OBJECTS.TYPE_OIDHelper.write($out, server_id);
            $in = _invoke($out);
            int $result = OBJECTS.ErrorCodeTypeHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return remove(server_id);
        } finally {
            _releaseReply($in);
        }
    }

    private static String[] __ids = { "IDL:FACTORY/SerialServerFactory:1.0" };

    public String[] _ids() {
        return (String[]) __ids.clone();
    }

    private void readObject(java.io.ObjectInputStream s) throws java.io.IOException {
        String str = s.readUTF();
        String[] args = null;
        java.util.Properties props = null;
        org.omg.CORBA.Object obj = org.omg.CORBA.ORB.init(args, props).string_to_object(str);
        org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate();
        _set_delegate(delegate);
    }

    private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
        String[] args = null;
        java.util.Properties props = null;
        String str = org.omg.CORBA.ORB.init(args, props).object_to_string(this);
        s.writeUTF(str);
    }
}
