package br.com.rmt.ganttprogramming.java.lang;

import br.com.rmt.ganttprogramming.utils.Serializavel;
import java.io.Serializable;
import org.json.JSONObject;

public final class GPByte extends GPBase<Byte> implements Serializable, Serializavel {

    public GPByte() {
    }

    public GPByte(Byte valor) {
        this.valor = valor;
    }

    public Byte deserializar(String valor) throws Exception {
        JSONObject jsonObject = new JSONObject(valor);
        return Byte.parseByte(jsonObject.getString("valor"));
    }
}
