package ar.com.larreta.controlador.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import ar.com.larreta.intercambio.client.Respuesta;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class RetornoDeLlamada implements AsyncCallback {

    private Boolean errores = Boolean.FALSE;

    private Collection retornosRegistrados = new ArrayList();

    protected Map<Object, Respuesta> respuestas = new HashMap<Object, Respuesta>();

    public Map<Object, Respuesta> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(Map<Object, Respuesta> respuestas) {
        this.respuestas = respuestas;
    }

    public void agregar(AsyncCallback retornoDeLlamada) {
        if (retornoDeLlamada != null) {
            retornosRegistrados.add(retornoDeLlamada);
        }
    }

    public Boolean getErrores() {
        return errores;
    }

    public void setErrores(Boolean errores) {
        this.errores = errores;
    }

    public void onFailure(Throwable error) {
        errores = Boolean.TRUE;
        Iterator it = retornosRegistrados.iterator();
        while (it.hasNext()) {
            AsyncCallback retorno = (AsyncCallback) it.next();
            retorno.onFailure(error);
        }
        fallo(error);
    }

    public void onSuccess(Object respuestas) {
        errores = Boolean.FALSE;
        setRespuestas((Map<Object, Respuesta>) respuestas);
        procesado(getRespuestas());
        Iterator it = retornosRegistrados.iterator();
        while (it.hasNext()) {
            AsyncCallback retorno = (AsyncCallback) it.next();
            retorno.onSuccess(respuestas);
        }
    }

    public abstract void fallo(Throwable error);

    public abstract void procesado(Map<Object, Respuesta> respuestas);
}
