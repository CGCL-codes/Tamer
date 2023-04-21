package be.xios.mobile.project.webservice;

public class GetEventByDateIdResponse {

    protected be.xios.mobile.project.webservice.Afspraak[] _return;

    public GetEventByDateIdResponse() {
    }

    public GetEventByDateIdResponse(be.xios.mobile.project.webservice.Afspraak[] _return) {
        this._return = _return;
    }

    public be.xios.mobile.project.webservice.Afspraak[] get_return() {
        return _return;
    }

    public void set_return(be.xios.mobile.project.webservice.Afspraak[] _return) {
        this._return = _return;
    }
}
