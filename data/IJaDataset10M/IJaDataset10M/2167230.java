package br.com.sisacad.dao;

import java.io.Serializable;

interface Dao<T, ID extends Serializable> {

    public void salva(T t);

    public void deleta(T t);

    public void atualiza(T t);

    public T busca(ID id);
}
