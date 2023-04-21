package br.ufrj.cad.view.ds;

import static org.junit.Assert.*;
import org.junit.Test;

public class PrioridadeDisciplinaEscolhidaDSTest {

    @Test
    public void DS() {
        PrioridadeDisciplinaEscolhidaDS ds = new PrioridadeDisciplinaEscolhidaDS();
        ds.montaOpcoes();
        assertEquals("0", ds.getLista().get(0).getId());
        assertEquals("1", ds.getLista().get(1).getId());
        assertEquals("2", ds.getLista().get(2).getId());
        assertEquals("Alta", ds.getLista().get(0).getValue());
        assertEquals("Média", ds.getLista().get(1).getValue());
        assertEquals("Baixa", ds.getLista().get(2).getValue());
    }
}
