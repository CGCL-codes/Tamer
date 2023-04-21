package br.ufrj.cad.view.ds;

import static org.junit.Assert.*;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import br.ufrj.cad.model.bo.UsuarioComum;
import br.ufrj.cad.unittest.util.DatabaseUtil;

public class EscolherDisciplinasDSTest {

    @Before
    public void setUp() {
        DatabaseUtil.downUpAllFixtures();
    }

    /**
   * Verifica se as disciplinas estao ordenadas por codigo e agrupadas por tipo.
   */
    @Test
    public void montaOpcoes() {
        UsuarioComum usuarioComum = new UsuarioComum();
        usuarioComum.setLogin("dalton");
        EscolherDisciplinaDS ds = new EscolherDisciplinaDS();
        ds.setUsuario(usuarioComum.find());
        ds.montaOpcoes();
        List<DataSuplierItem> listaDisciplinas = ds.getLista();
        assertEquals(104, listaDisciplinas.size());
        assertEquals("61", listaDisciplinas.get(97).getId());
        assertEquals("MAB602 - Topicos Especias em Automacao - Obrigatória", listaDisciplinas.get(97).getValue());
        assertEquals("71", listaDisciplinas.get(98).getId());
        assertEquals("52", listaDisciplinas.get(100).getId());
        assertEquals("22", listaDisciplinas.get(101).getId());
        assertEquals("19", listaDisciplinas.get(103).getId());
    }
}
