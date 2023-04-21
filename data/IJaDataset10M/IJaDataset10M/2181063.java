package net.diretrix.suporte.dominio.entidade.suporte;

import net.diretrix.util.calendario.Data;
import net.diretrix.persistencia.Entidade;

/**
 *
 * @author Fabrício da Silva Benvenutti
 */
public class Comentario implements Entidade {

    private int id;

    private Data data;

    private String descricao;

    @Deprecated
    public Comentario() {
    }

    public Comentario(Data data, String descricao) {
        setData(data);
        setDescricao(descricao);
    }

    @Override
    public Object getId() {
        return id;
    }

    @Override
    public void setId(Object id) {
        if (id instanceof Integer) {
            this.id = (Integer) id;
        }
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
