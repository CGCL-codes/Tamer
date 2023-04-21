package pedrociarlini.aecioneto.pesquisa.entity;

import java.io.Serializable;

public interface IResposta extends Serializable {

    void setSimbolo(ISimbolo sim);

    ISimbolo getSimbolo();
}
