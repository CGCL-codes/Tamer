package org.omg.CosTime;

public abstract class UTOPOA extends org.omg.PortableServer.Servant implements org.omg.CORBA.portable.InvokeHandler, UTOOperations {

    static final String[] _ob_ids_ = { "IDL:omg.org/CosTime/UTO:1.0" };

    public UTO _this() {
        return UTOHelper.narrow(super._this_object());
    }

    public UTO _this(org.omg.CORBA.ORB orb) {
        return UTOHelper.narrow(super._this_object(orb));
    }

    public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte[] objectId) {
        return _ob_ids_;
    }

    public org.omg.CORBA.portable.OutputStream _invoke(String opName, org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {
        final String[] _ob_names = { "_get_inaccuracy", "_get_tdf", "_get_time", "_get_utc_time", "absolute_time", "compare_time", "interval", "time_to_interval" };
        int _ob_left = 0;
        int _ob_right = _ob_names.length;
        int _ob_index = -1;
        while (_ob_left < _ob_right) {
            int _ob_m = (_ob_left + _ob_right) / 2;
            int _ob_res = _ob_names[_ob_m].compareTo(opName);
            if (_ob_res == 0) {
                _ob_index = _ob_m;
                break;
            } else if (_ob_res > 0) _ob_right = _ob_m; else _ob_left = _ob_m + 1;
        }
        switch(_ob_index) {
            case 0:
                return _OB_att_get_inaccuracy(in, handler);
            case 1:
                return _OB_att_get_tdf(in, handler);
            case 2:
                return _OB_att_get_time(in, handler);
            case 3:
                return _OB_att_get_utc_time(in, handler);
            case 4:
                return _OB_op_absolute_time(in, handler);
            case 5:
                return _OB_op_compare_time(in, handler);
            case 6:
                return _OB_op_interval(in, handler);
            case 7:
                return _OB_op_time_to_interval(in, handler);
        }
        throw new org.omg.CORBA.BAD_OPERATION();
    }

    private org.omg.CORBA.portable.OutputStream _OB_att_get_inaccuracy(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.TimeBase.ulonglong _ob_r = inaccuracy();
        org.omg.CORBA.portable.OutputStream out = handler.createReply();
        org.omg.TimeBase.InaccuracyTHelper.write(out, _ob_r);
        return out;
    }

    private org.omg.CORBA.portable.OutputStream _OB_att_get_tdf(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {
        short _ob_r = tdf();
        org.omg.CORBA.portable.OutputStream out = handler.createReply();
        org.omg.TimeBase.TdfTHelper.write(out, _ob_r);
        return out;
    }

    private org.omg.CORBA.portable.OutputStream _OB_att_get_time(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.TimeBase.ulonglong _ob_r = time();
        org.omg.CORBA.portable.OutputStream out = handler.createReply();
        org.omg.TimeBase.TimeTHelper.write(out, _ob_r);
        return out;
    }

    private org.omg.CORBA.portable.OutputStream _OB_att_get_utc_time(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.TimeBase.UtcT _ob_r = utc_time();
        org.omg.CORBA.portable.OutputStream out = handler.createReply();
        org.omg.TimeBase.UtcTHelper.write(out, _ob_r);
        return out;
    }

    private org.omg.CORBA.portable.OutputStream _OB_op_absolute_time(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream out = null;
        UTO _ob_r = absolute_time();
        out = handler.createReply();
        UTOHelper.write(out, _ob_r);
        return out;
    }

    private org.omg.CORBA.portable.OutputStream _OB_op_compare_time(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream out = null;
        ComparisonType _ob_a0 = ComparisonTypeHelper.read(in);
        UTO _ob_a1 = UTOHelper.read(in);
        TimeComparison _ob_r = compare_time(_ob_a0, _ob_a1);
        out = handler.createReply();
        TimeComparisonHelper.write(out, _ob_r);
        return out;
    }

    private org.omg.CORBA.portable.OutputStream _OB_op_interval(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream out = null;
        TIO _ob_r = interval();
        out = handler.createReply();
        TIOHelper.write(out, _ob_r);
        return out;
    }

    private org.omg.CORBA.portable.OutputStream _OB_op_time_to_interval(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream out = null;
        UTO _ob_a0 = UTOHelper.read(in);
        TIO _ob_r = time_to_interval(_ob_a0);
        out = handler.createReply();
        TIOHelper.write(out, _ob_r);
        return out;
    }
}
