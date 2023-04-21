package org.omg.CosTrading;

public abstract class LinkAttributesPOA extends org.omg.PortableServer.Servant implements org.omg.CORBA.portable.InvokeHandler, LinkAttributesOperations {

    static final String[] _ob_ids_ = { "IDL:omg.org/CosTrading/LinkAttributes:1.0" };

    public LinkAttributes _this() {
        return LinkAttributesHelper.narrow(super._this_object());
    }

    public LinkAttributes _this(org.omg.CORBA.ORB orb) {
        return LinkAttributesHelper.narrow(super._this_object(orb));
    }

    public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte[] objectId) {
        return _ob_ids_;
    }

    public org.omg.CORBA.portable.OutputStream _invoke(String opName, org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {
        final String[] _ob_names = { "_get_max_link_follow_policy" };
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
                return _OB_att_get_max_link_follow_policy(in, handler);
        }
        throw new org.omg.CORBA.BAD_OPERATION();
    }

    private org.omg.CORBA.portable.OutputStream _OB_att_get_max_link_follow_policy(org.omg.CORBA.portable.InputStream in, org.omg.CORBA.portable.ResponseHandler handler) {
        FollowOption _ob_r = max_link_follow_policy();
        org.omg.CORBA.portable.OutputStream out = handler.createReply();
        FollowOptionHelper.write(out, _ob_r);
        return out;
    }
}
