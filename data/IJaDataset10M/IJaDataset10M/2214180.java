package pe.bn.WSServiceHost.serHost.model;

import javax.xml.rpc.holders.StringHolder;
import pe.bn.WSServiceHost.serHost.interactive.WSConnect;
import org.apache.log4j.Logger;

public class WSConsultaIngreso {

    private static Logger log = Logger.getLogger(WSConsultaIngreso.class.getName());

    private HeadRequest cabeceraRequest;

    private BodyConsultaIngreso body;

    public WSConsultaIngreso(HeadRequest cab, BodyConsultaIngreso input) {
        super();
        cabeceraRequest = cab;
        body = input;
    }

    public int EjecutarTramaHost(HeadRespose cabeceraRespose, BodyConsultaIngreso outPut) throws Exception {
        String cadenaHost = new String();
        StringHolder cadResultHolder = new StringHolder();
        String cadenaResult = "";
        WSConnect wsConnect = new WSConnect();
        cadenaHost = cabeceraRequest.toString() + body.toString();
        int result = 9999;
        result = wsConnect.EjecutarTramaHost(cadenaHost, cadResultHolder);
        cadenaResult = cadResultHolder.value.trim();
        int longitudHead = cabeceraRequest.LongitudTrama();
        int longitudresult = cadenaResult.length();
        if (longitudresult >= longitudHead) {
            String HeadResult = cadenaResult.substring(0, longitudHead);
            cabeceraRespose.FillCabecera(HeadResult);
        } else {
            cabeceraRespose = null;
        }
        ;
        int longitudBody = body.LongitudTrama();
        if (longitudresult >= longitudHead + longitudBody) {
            String BodyResult = cadenaResult.substring(longitudHead, longitudresult);
            outPut.FillBoby(BodyResult);
        } else {
            outPut = null;
        }
        ;
        return result;
    }

    public HeadRequest getCabeceraRequest() {
        return cabeceraRequest;
    }

    public void setCabeceraRequest(HeadRequest cabeceraRequest) {
        this.cabeceraRequest = cabeceraRequest;
    }

    public BodyConsultaIngreso getBody() {
        return body;
    }

    public void setBody(BodyConsultaIngreso body) {
        this.body = body;
    }
}
