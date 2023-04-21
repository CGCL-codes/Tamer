package edu.neu.ccs.demeterf.http.classes;

import edu.neu.ccs.demeterf.lib.*;
import java.net.Socket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.InputStream;

/** Representation of ConnectReq */
public class ConnectReq extends HTTPHead {

    /** Construct a(n) ConnectReq Instance */
    public ConnectReq(URL url, HTTPVer ver) {
        super(url, ver);
    }

    /** Is the given object Equal to this ConnectReq? */
    public boolean equals(Object o) {
        if (!(o instanceof ConnectReq)) return false;
        if (o == this) return true;
        ConnectReq oo = (ConnectReq) o;
        return (((Object) url).equals(oo.url)) && (((Object) ver).equals(oo.ver));
    }

    /** Parse an instance of ConnectReq from the given String */
    public static ConnectReq parse(String inpt) throws ParseException {
        return new TheParser(new java.io.StringReader(inpt)).parse_ConnectReq();
    }

    /** Parse an instance of ConnectReq from the given Stream */
    public static ConnectReq parse(java.io.InputStream inpt) throws ParseException {
        return new TheParser(inpt).parse_ConnectReq();
    }

    /** Parse an instance of ConnectReq from the given Reader */
    public static ConnectReq parse(java.io.Reader inpt) throws ParseException {
        return new TheParser(inpt).parse_ConnectReq();
    }

    /** Get the ReqType of this Request */
    public HTTPReq.ReqType getType() {
        return HTTPReq.ReqType.CONNECT;
    }

    /** DGP method from Class PrintHeapToString */
    public String toString() {
        return edu.neu.ccs.demeterf.http.classes.PrintHeapToString.PrintHeapToStringM(this);
    }

    /** Getter for field ConnectReq.url */
    public URL getUrl() {
        return url;
    }

    /** Getter for field ConnectReq.ver */
    public HTTPVer getVer() {
        return ver;
    }
}
