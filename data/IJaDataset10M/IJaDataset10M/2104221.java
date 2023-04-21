package org.universa.tcc.gemda.facade;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import java.util.Collection;
import org.junit.Test;
import org.universa.tcc.gemda.entidade.Passo;
import org.universa.tcc.gemda.entidade.Story;
import org.universa.tcc.gemda.entidade.Tarefa;
import org.universa.tcc.gemda.entidade.Usuario;

public class FacadeTarefaTest extends FacadeTestCase {

    public void testInserirTarefaInvalido() throws Exception {
        Tarefa tarefa = new Tarefa();
        facade.inserir(tarefa);
        assertNotNull(tarefa.getId());
    }

    @Test
    public void testInserirTarefa() throws Exception {
        Tarefa tarefa = new Tarefa(null, "Implementar as classes de dom�nio do GeMDA!", criarUsuario(), criarStory(), Passo.TO_DO);
        facade.inserir(tarefa);
        assertNotNull(tarefa.getId());
    }

    public void testAlterarTarefaInvalido() throws Exception {
        Tarefa tarefa = new Tarefa(null, "Teste", criarUsuario(), criarStory(), Passo.TO_DO);
        facade.inserir(tarefa);
        assertNotNull(tarefa.getId());
        tarefa.setUsuario(new Usuario());
        tarefa.setStory(new Story());
        tarefa.setDescricao("");
        tarefa.setPasso(null);
        facade.alterar(tarefa);
        Tarefa tarefaAlterada = facade.recuperar(Tarefa.class, tarefa.getId());
        assertEquals(Passo.DOING, tarefaAlterada.getPasso());
    }

    @Test
    public void testAlterarTarefa() throws Exception {
        Tarefa tarefa = new Tarefa(null, "Teste", criarUsuario(), criarStory(), Passo.TO_DO);
        facade.inserir(tarefa);
        assertNotNull(tarefa.getId());
        tarefa.setPasso(Passo.DOING);
        facade.alterar(tarefa);
        Tarefa tarefaAlterada = facade.recuperar(Tarefa.class, tarefa.getId());
        assertEquals(Passo.DOING, tarefaAlterada.getPasso());
    }

    public void testExcluirTarefaInvalido() throws Exception {
        Tarefa tarefa = new Tarefa(null, "Teste", criarUsuario(), criarStory(), Passo.TO_DO);
        facade.inserir(tarefa);
        assertNotNull(tarefa.getId());
        tarefa.setId(null);
        facade.excluir(tarefa);
    }

    @Test
    public void testExcluirTarefa() throws Exception {
        Tarefa tarefa = new Tarefa(null, "Teste", criarUsuario(), criarStory(), Passo.TO_DO);
        facade.inserir(tarefa);
        assertNotNull(tarefa.getId());
        facade.excluir(tarefa);
        Tarefa tarefaExcluida = facade.recuperar(Tarefa.class, tarefa.getId());
        assertNull(tarefaExcluida);
    }

    public void testRecuperarTarefaInvalido() throws Exception {
        Tarefa terafa = new Tarefa(null, "Teste", criarUsuario(), criarStory(), Passo.DONE);
        facade.inserir(terafa);
        assertNotNull(terafa.getId());
        Tarefa tarefaRecuperada = facade.recuperar(Tarefa.class, null);
        assertNotNull(tarefaRecuperada);
    }

    @Test
    public void testRecuperarTarefa() throws Exception {
        Tarefa terafa = new Tarefa(null, "Teste", criarUsuario(), criarStory(), Passo.DONE);
        facade.inserir(terafa);
        assertNotNull(terafa.getId());
        Tarefa tarefaRecuperada = facade.recuperar(Tarefa.class, terafa.getId());
        assertNotNull(tarefaRecuperada);
    }

    @Test
    public void testListarTarefas() throws Exception {
        Collection<Tarefa> tarefas = facade.listar(Tarefa.class);
        assertFalse(tarefas.isEmpty());
    }

    @Test
    public void testPesquisarTarefa() throws Exception {
        Tarefa tarefa = new Tarefa();
        tarefa.setPasso(Passo.TO_DO);
        Collection<Tarefa> tarefas = facade.pesquisar(tarefa);
        assertFalse(tarefas.isEmpty());
    }

    @Test
    public void testCountAllTarefas() throws Exception {
        long count = facade.count(Tarefa.class);
        assertFalse(count == 0);
    }

    @Test
    public void testCountTarefa() throws Exception {
        long count = facade.count(new Tarefa(null, null, null, null, Passo.DONE));
        assertFalse(count == 0);
    }
}
