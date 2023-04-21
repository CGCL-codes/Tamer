package bw.search;

import bw.util.*;
import bw.net.*;

public class MapQuestSearchQuery implements SearchQuery {

    private Geocoder _geocoder = null;

    private Location _startLocation = null;

    public MapQuestSearchQuery(Geocoder geocoder) {
        _geocoder = geocoder;
    }

    public void setStartLocation(Location loc) {
        _startLocation = loc;
    }

    public String getSearchURL(String query) {
        Location endLocation = _geocoder.geocode(query);
        NetURI mq = null;
        if (_startLocation != null) {
            mq = new NetURI("http://www.mapquest.com/directions/main.adp");
            mq.addParam("go", "1");
            mq.addParam("do", "nw");
            mq.addParam("rmm", "1");
            mq.addParam("un", "m");
            mq.addParam("cl", "EN");
            mq.addParam("ct", "NA");
            mq.addParam("rsres", "1");
            mq.addParam("1ffi", "1");
            mq.addParam("1l", "");
            mq.addParam("1g", "");
            mq.addParam("1n", "");
            mq.addParam("1pl", "");
            mq.addParam("1v", "ADDRESS");
            mq.addParam("1pn", _startLocation.place);
            mq.addParam("1a", _startLocation.street);
            mq.addParam("1c", _startLocation.city);
            mq.addParam("1s", _startLocation.state);
            mq.addParam("1z", _startLocation.zip);
            if (endLocation.isAddress()) {
                mq.addParam("2ffi", "1");
            } else {
                mq.addParam("2ffi", "");
            }
            mq.addParam("2l", "");
            mq.addParam("2g", "");
            mq.addParam("2pl", "");
            if (endLocation.isAddress()) {
                mq.addParam("2v", "ADDRESS");
            } else {
                mq.addParam("2v", "");
            }
            mq.addParam("2n", "");
            mq.addParam("2pn", endLocation.place);
            mq.addParam("2a", endLocation.street);
            mq.addParam("2c", endLocation.city);
            mq.addParam("2s", endLocation.state);
            mq.addParam("2z", endLocation.zip);
            mq.addParam("r", "f");
        } else {
            mq = new NetURI("http://www.mapquest.com/maps/map.adp");
            mq.addParam("searchtype", "address");
            mq.addParam("country", "US");
            mq.addParam("addtohistory", "");
            mq.addParam("searchtab", "home");
            mq.addParam("formtype", "address");
            mq.addParam("popflag", "0");
            mq.addParam("latitude", "");
            mq.addParam("longitude", "");
            mq.addParam("name", "");
            mq.addParam("phone", "");
            mq.addParam("level", "");
            mq.addParam("cat", endLocation.place);
            mq.addParam("address", endLocation.street);
            mq.addParam("city", endLocation.city);
            mq.addParam("state", endLocation.state);
            mq.addParam("zipcode", endLocation.zip);
        }
        return mq.toString();
    }
}
