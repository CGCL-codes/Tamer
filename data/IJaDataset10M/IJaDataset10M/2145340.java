package net.diretrix.suporte.dominio.entidade.suporte.requisicao;

import java.io.Serializable;
import net.diretrix.util.calendario.Data;

/**
 *
 * @author Fabrício da Silva Benvenutti
 */
public class Cumprimento implements Serializable {

    private Data data;

    public Cumprimento() {
    }

    public Cumprimento(Data data) {
        setData(data);
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
