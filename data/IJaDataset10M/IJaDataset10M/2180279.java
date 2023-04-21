package fireteam.orb.server.stub;

public class _FTDRobotStub extends org.omg.CORBA.portable.ObjectImpl implements fireteam.orb.server.stub.FTDRobot {

    public org.omg.CORBA.Any request(org.omg.CORBA.Any params) throws fireteam.orb.server.stub.StandardException {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("request", true);
            $out.write_any(params);
            $in = _invoke($out);
            org.omg.CORBA.Any $result = $in.read_any();
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            if (_id.equals("IDL:stub/StandardException:1.0")) throw fireteam.orb.server.stub.StandardExceptionHelper.read($in); else throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return request(params);
        } finally {
            _releaseReply($in);
        }
    }

    private static String[] __ids = { "IDL:stub/FTDRobot:1.0" };

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
