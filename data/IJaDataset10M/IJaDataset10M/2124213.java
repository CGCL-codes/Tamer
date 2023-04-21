package Equipamentos;

import Base.Escudo;

public class EscudoDeMetal extends Escudo {

    public EscudoDeMetal(int level) {
        setNome("Escudo de Metal");
        setDefesaBase(4);
        setPesoBase(20);
        setModificadorDePreco(300);
        setObjetivo(level);
    }
}
