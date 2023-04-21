package quamj.iiop_rsvpd.upcall;

import quamj.iiop_rsvpd.*;

public class UpcallListener_impl extends UpcallListenerPOA {

    private org.omg.PortableServer.POA poa_;

    private Session session_;

    public UpcallListener_impl(org.omg.PortableServer.POA poa) {
        poa_ = poa;
    }

    public void set_session(Session the_session) {
        session_ = the_session;
    }

    public org.omg.PortableServer.POA _default_POA() {
        if (poa_ != null) return poa_; else return super._default_POA();
    }

    public void path(TSpec sender, AdSpec adspc) {
        System.out.println("RSVP upcall: path");
    }

    public void resv(FilterStyle style, FlowSpec flowspc) {
        System.out.println("RSVP upcall: resv");
    }

    public void pathError() {
        System.out.println("RSVP upcall: pathError");
    }

    public void resvError() {
        System.out.println("RSVP upcall: resvError");
    }

    public void confirm() {
        System.out.println("RSVP upcall: confirm");
    }
}
