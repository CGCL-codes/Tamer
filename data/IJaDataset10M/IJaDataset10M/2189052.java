package br.ufrj.cad.functionaltest.disciplina;

import static org.junit.Assert.*;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import br.ufrj.cad.model.bo.AnoBase;
import br.ufrj.cad.model.bo.Departamento;
import br.ufrj.cad.model.disciplina.DisciplinaService;
import br.ufrj.cad.unittest.util.DatabaseUtil;

public class CriarAnoBaseTest {

    private static final Long ID_DEPTO_DMA = new Long(2);

    private String[] FIXTURES = new String[] { DatabaseUtil.FIXTURE_ANO_BASE_2007, DatabaseUtil.FIXTURE_MATERIAS_INFORMATICA_2007, DatabaseUtil.FIXTURE_ANO_BASE_SEM_2007, DatabaseUtil.FIXTURE_MATERIAS_INFORMATICA_2006, DatabaseUtil.FIXTURE_DEPARTAMENTO };

    @Before
    public void setUp() throws Exception {
        DatabaseUtil.fixtureDown(FIXTURES);
        DatabaseUtil.fixtureUp(FIXTURES);
    }

    @Test
    public void criaAnoBaseExistente() {
        try {
            Departamento departamento = criaDepartamentoDCC();
            DisciplinaService.getInstance().criaAnoBase(new Integer(2007), departamento, null);
            fail("nao pode criar ano base que ja existe");
        } catch (RuntimeException e) {
            assertEquals("anobase.ja.criado", e.getMessage());
        }
    }

    private Departamento criaDepartamentoDCC() {
        Departamento departamento = new Departamento();
        departamento.setId(new Long(1));
        return departamento;
    }

    @Test
    public void criaAnoBaseVazio() {
        Integer valorAnoBase = 2008;
        AnoBase anoBaseCriado = DisciplinaService.getInstance().criaAnoBase(valorAnoBase, criaDepartamentoDCC(), null);
        verificaAnoBase2008Criado(valorAnoBase, anoBaseCriado);
        assertTrue(anoBaseCriado.getDisciplinas().isEmpty());
        List<String[]> resultado = DatabaseUtil.executeQuery("SELECT * FROM ANO_BASE WHERE ANBA_SQ_ANO_BASE = " + anoBaseCriado.getId());
        verificaResultadoNaoVazio(resultado);
        String[] anoBaseAtributos = resultado.get(0);
        assertEquals(String.valueOf(anoBaseCriado.getId()), anoBaseAtributos[0]);
        assertEquals("1", anoBaseAtributos[1]);
        assertEquals(String.valueOf(valorAnoBase), anoBaseAtributos[2]);
    }

    private void verificaResultadoNaoVazio(List<String[]> resultado) {
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
    }

    private void verificaAnoBase2008Criado(Integer valorAnoBase, AnoBase anoBaseCriado) {
        assertNotNull(anoBaseCriado);
        assertEquals(valorAnoBase, anoBaseCriado.getValorAnoBase());
        assertEquals(new Long(1), anoBaseCriado.getDepartamento().getId());
        assertNotNull(anoBaseCriado.getDisciplinas());
    }

    @Test
    public void criaAnoBaseComDisciplinas() {
        Integer valorAnoBase = new Integer(2008);
        AnoBase anoBaseCriado = DisciplinaService.getInstance().criaAnoBase(valorAnoBase, criaDepartamentoDCC(), new Long(4));
        verificaAnoBase2008Criado(valorAnoBase, anoBaseCriado);
        assertFalse(anoBaseCriado.getDisciplinas().isEmpty());
        assertEquals(6, anoBaseCriado.getDisciplinas().size());
        List<String[]> resultado = DatabaseUtil.executeQuery("SELECT * FROM DISCIPLINA WHERE ANBA_SQ_ANO_BASE = " + anoBaseCriado.getId() + " order by DISC_CD_DISCIPLINA asc");
        verificaResultadoNaoVazio(resultado);
        assertEquals(6, resultado.size());
        String[] disciplinaCriada = resultado.get(0);
        assertEquals(String.valueOf(anoBaseCriado.getId()), disciplinaCriada[1]);
        assertEquals("MAB618", disciplinaCriada[2]);
        assertEquals("Telefonia Ip", disciplinaCriada[3]);
        assertEquals("O", disciplinaCriada[4]);
        assertEquals("G", disciplinaCriada[5]);
        assertEquals("0009-9", disciplinaCriada[6]);
        assertEquals("40", disciplinaCriada[7]);
        assertEquals("8:00 - 10:00", disciplinaCriada[8]);
        assertEquals("Seg - Qua", disciplinaCriada[9]);
    }

    @Test
    public void criaAnoBaseBaseadoEmAnoBaseQueNAoExiste() {
        try {
            DisciplinaService.getInstance().criaAnoBase(new Integer(2008), criaDepartamentoDCC(), new Long(10));
            fail("nao pode criar ano com base em ano que nao existe");
        } catch (RuntimeException e) {
            assertEquals("anobase.id.nao.existe", e.getMessage());
        }
    }

    @Test
    public void criaAnoBaseBaseadoEmAnoDeOutroDepartamento() {
        try {
            Departamento departamento = new Departamento();
            departamento.setId(ID_DEPTO_DMA);
            DisciplinaService.getInstance().criaAnoBase(new Integer(2008), departamento, new Long(4));
            fail("nao pode criar ano base com base em ano de outro departamento");
        } catch (RuntimeException e) {
            assertEquals("anobase.outro.departamento", e.getMessage());
        }
    }
}
