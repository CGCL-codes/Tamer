package populadores;

import java.util.Date;
import org.junit.Test;
import teste.TesteBase;
import com.gft.larozanam.shared.entidades.EnfermagemIdoso;
import com.gft.larozanam.shared.entidades.Enfermeiro;
import com.gft.larozanam.shared.entidades.Exame;
import com.gft.larozanam.shared.entidades.ExameIdoso;
import com.gft.larozanam.shared.entidades.HistoricoEnfermagemIdoso;
import com.gft.larozanam.shared.entidades.Idoso;
import com.gft.larozanam.shared.entidades.IdosoRespIdoso;
import com.gft.larozanam.shared.entidades.PrescricaoEnfermagem;
import com.gft.larozanam.shared.entidades.ResponsavelIdoso;
import com.gft.larozanam.shared.entidades.Servico;
import com.gft.larozanam.shared.entidades.TarefaExecutada;
import com.gft.larozanam.shared.entidades.enums.Frequencia;

public class Populador extends TesteBase {

    @Test
    public void popularExame() {
        for (int i = 0; i < 50; i++) {
            begin();
            Exame exame = new Exame();
            exame.setDescricao("exame " + i);
            dao.insert(exame);
            commit();
        }
        assertEquals(50L, contarObjetos(Exame.class));
    }

    @Test
    public void popularIdoso() {
        for (int i = 0; i < 50; i++) {
            begin();
            Idoso idoso = new Idoso();
            Date x = new Date();
            idoso.setNome("idoso " + i);
            idoso.setRg("000" + i);
            idoso.setSexo('M');
            idoso.setDataNascimento(x);
            idoso.setAtivo(true);
            dao.insert(idoso);
            commit();
        }
        assertEquals(50L, contarObjetos(Idoso.class));
    }

    @Test
    public void popularExameIdoso() {
        for (int i = 0; i < 50; i++) {
            begin();
            ExameIdoso exameIdoso = new ExameIdoso();
            Idoso idoso = getObjeto(Idoso.class);
            Exame exame = getObjeto(Exame.class);
            Date data = new Date();
            exameIdoso.setIdoso(idoso.getCodigo());
            exameIdoso.setExame(exame.getCodigo());
            exameIdoso.setDate(data);
            dao.insert(exameIdoso);
            commit();
        }
        assertEquals(50L, contarObjetos(ExameIdoso.class));
    }

    @Test
    public void exibirExameIdoso() {
        ExameIdoso exameIdoso = getObjeto(ExameIdoso.class);
        Idoso idoso = dao.select(exameIdoso.getIdoso(), Idoso.class);
        assertNotNull(idoso);
        assertNotNull(idoso.getNome());
        assertNotNull(idoso.getRg());
        assertNotNull(idoso.getAtivo());
        assertNotNull(idoso.getDataNascimento());
        assertNotNull(idoso.getSexo());
    }

    @Test
    public void popularResponsavelIdoso() {
        for (int i = 0; i < 50; i++) {
            begin();
            ResponsavelIdoso respIdoso = new ResponsavelIdoso();
            respIdoso.setNome("responsavel " + i);
            respIdoso.setTelefone("3333-4444");
            respIdoso.setEndereco("Rua do JAVA");
            respIdoso.setNumero(550);
            respIdoso.setBairro("Jd do Oracle");
            respIdoso.setCidade("Mainframe");
            respIdoso.setCEP("18000-000");
            dao.insert(respIdoso);
            commit();
        }
        assertEquals(50L, contarObjetos(ResponsavelIdoso.class));
    }

    @Test
    public void popularIdosoRespIdoso() {
        for (int i = 0; i < 50; i++) {
            begin();
            IdosoRespIdoso idosoRespIdoso = new IdosoRespIdoso();
            Idoso idoso = getObjeto(Idoso.class);
            idosoRespIdoso.setIdoso(idoso.getCodigo());
            ResponsavelIdoso responsavelIdoso = getObjeto(ResponsavelIdoso.class);
            idosoRespIdoso.setResponsavelIdoso(responsavelIdoso.getCodigo());
            idosoRespIdoso.setParentesco("Pai, M�e, Filho, etc...");
            dao.insert(idosoRespIdoso);
            commit();
        }
        assertEquals(50L, contarObjetos(ResponsavelIdoso.class));
    }

    @Test
    public void exibirIdosoRespIdoso() {
        IdosoRespIdoso idosoRespIdoso = getObjeto(IdosoRespIdoso.class);
        Idoso idoso = dao.select(idosoRespIdoso.getIdoso(), Idoso.class);
        ResponsavelIdoso respIdoso = dao.select(idosoRespIdoso.getResponsavelIdoso(), ResponsavelIdoso.class);
        assertNotNull(idoso);
        assertNotNull(idoso.getNome());
        assertNotNull(idoso.getRg());
        assertNotNull(idoso.getAtivo());
        assertNotNull(idoso.getDataNascimento());
        assertNotNull(idoso.getSexo());
        assertNotNull(respIdoso);
        assertNotNull(respIdoso.getNome());
        assertNotNull(respIdoso.getTelefone());
    }

    @Test
    public void popularEnfermagemIdoso() {
        for (int i = 0; i < 50; i++) {
            begin();
            EnfermagemIdoso enfermagemIdoso = new EnfermagemIdoso();
            Date data = new Date();
            enfermagemIdoso.setAtivo(true);
            enfermagemIdoso.setDataInternacao(data);
            enfermagemIdoso.setEtnia("Branco");
            enfermagemIdoso.setFumante(true);
            enfermagemIdoso.setHabitos("Come bastante!");
            Idoso idoso = getObjeto(Idoso.class);
            enfermagemIdoso.setIdoso(idoso.getCodigo());
            enfermagemIdoso.setIndependente(false);
            enfermagemIdoso.setLeito(10);
            enfermagemIdoso.setMedicamentos("Morfina");
            enfermagemIdoso.setMotivoInternacao("Doen�a");
            enfermagemIdoso.setPeso(75.50);
            enfermagemIdoso.setPressaoAlta(true);
            enfermagemIdoso.setProcedencia("Falar com o M�dico!! Urgente!");
            enfermagemIdoso.setQuarto(301);
            dao.insert(enfermagemIdoso);
            commit();
        }
        assertEquals(50L, contarObjetos(ResponsavelIdoso.class));
    }

    @Test
    public void exibirEnfermagemIdoso() {
        EnfermagemIdoso enfermagemIdoso = getObjeto(EnfermagemIdoso.class);
        Idoso idoso = dao.select(enfermagemIdoso.getIdoso(), Idoso.class);
        assertNotNull(idoso);
        assertNotNull(idoso.getNome());
        assertNotNull(idoso.getRg());
        assertNotNull(idoso.getAtivo());
        assertNotNull(idoso.getDataNascimento());
        assertNotNull(idoso.getSexo());
    }

    @Test
    public void updateEnfermagemIdoso() {
        begin();
        EnfermagemIdoso enfermagemIdoso = getObjeto(EnfermagemIdoso.class);
        Idoso idoso = dao.select(enfermagemIdoso.getIdoso(), Idoso.class);
        idoso.setNome("Alterado o Nome");
        dao.update(idoso);
        commit();
        begin();
        Idoso idoso2 = dao.select(enfermagemIdoso.getIdoso(), Idoso.class);
        assertFalse(idoso.getNome() == idoso2.getNome());
        commit();
    }

    @Test
    public void updateEnfermagemIdoso2() {
        begin();
        Date x = new Date();
        EnfermagemIdoso enfermagemIdoso = getObjeto(EnfermagemIdoso.class);
        HistoricoEnfermagemIdoso historicoEnfermagemIdoso = new HistoricoEnfermagemIdoso(enfermagemIdoso);
        historicoEnfermagemIdoso.setDataAlteracao(x);
        historicoEnfermagemIdoso.setMotivoAlteracao("Testes");
        dao.insert(historicoEnfermagemIdoso);
        commit();
        System.out.println("Enfermagem Original: " + enfermagemIdoso.getHabitos());
        begin();
        enfermagemIdoso.setHabitos("Hist�rico");
        dao.update(enfermagemIdoso);
        commit();
        EnfermagemIdoso enfermagemAlterado = dao.select(enfermagemIdoso.getCodigo(), EnfermagemIdoso.class);
        System.out.println("Enfermagem Alterado: " + enfermagemAlterado.getHabitos());
    }

    @Test
    public void exibirHistoricoEnfermagemIdoso() {
        HistoricoEnfermagemIdoso historicoEnfermagemIdoso = getObjeto(HistoricoEnfermagemIdoso.class);
        System.out.println("Historico Enfermagem: " + historicoEnfermagemIdoso.getHabitos());
        assertNotNull(historicoEnfermagemIdoso);
        assertNotNull(historicoEnfermagemIdoso.getMotivoInternacao());
        assertNotNull(historicoEnfermagemIdoso.getDataAlteracao());
    }

    @Test
    public void popularServico() {
        for (int i = 0; i < 50; i++) {
            begin();
            Servico servico = new Servico();
            servico.setDescricao("descri��o " + i);
            dao.insert(servico);
            commit();
        }
        assertEquals(50L, contarObjetos(ResponsavelIdoso.class));
    }

    @Test
    public void popularPrescricaoEnfermagem() {
        for (int i = 0; i < 50; i++) {
            begin();
            PrescricaoEnfermagem prescricaoEnfermagem = new PrescricaoEnfermagem();
            prescricaoEnfermagem.setAtivo(true);
            prescricaoEnfermagem.setComentarios("comentario " + i);
            prescricaoEnfermagem.setFimPeriodo(new Date());
            prescricaoEnfermagem.setFrequencia(Frequencia.DIARIO);
            prescricaoEnfermagem.setIdoso(getObjeto(Idoso.class).getCodigo());
            prescricaoEnfermagem.setInicioPeriodo(new Date());
            prescricaoEnfermagem.setServico(getObjeto(Servico.class).getCodigo());
            dao.insert(prescricaoEnfermagem);
            commit();
        }
        assertEquals(50L, contarObjetos(ResponsavelIdoso.class));
    }

    @Test
    public void popularEnfermeiro() {
        for (int i = 0; i < 50; i++) {
            begin();
            Enfermeiro enfermeiro = new Enfermeiro();
            enfermeiro.setAtivo(true);
            enfermeiro.setBairro("jd Bairro");
            enfermeiro.setCEP("18050-000");
            enfermeiro.setCidade("Cidadelopoles");
            enfermeiro.setDataDesativacao(new Date());
            enfermeiro.setDataNascimento(new Date());
            enfermeiro.setEndereco("Rua da medicina");
            enfermeiro.setNome("Enfermeiro " + i);
            enfermeiro.setNumero(999);
            enfermeiro.setRg("33.111.789");
            enfermeiro.setSexo('M');
            enfermeiro.setTelefone("(51)3344-5566");
            dao.insert(enfermeiro);
            commit();
        }
        assertEquals(50L, contarObjetos(ResponsavelIdoso.class));
    }

    @Test
    public void popularTarefaExecutada() {
        for (int i = 0; i < 50; i++) {
            PrescricaoEnfermagem prescEnfermagem = getObjeto(PrescricaoEnfermagem.class);
            Servico servico = dao.select(prescEnfermagem.getServico(), Servico.class);
            begin();
            TarefaExecutada tarefaExecutada = new TarefaExecutada();
            tarefaExecutada.setAtivo(true);
            tarefaExecutada.setDataExecucao(new Date());
            tarefaExecutada.setDescricao("descri��o " + i);
            tarefaExecutada.setEnfermeiro(getObjeto(Enfermeiro.class).getCodigo());
            tarefaExecutada.setIdoso(getObjeto(Idoso.class).getCodigo());
            tarefaExecutada.setPrescricaoEnfermagem(prescEnfermagem.getCodigo());
            String descricaoServico = servico.getDescricao();
            tarefaExecutada.setServico(descricaoServico);
            dao.insert(tarefaExecutada);
            commit();
        }
        assertEquals(50L, contarObjetos(ResponsavelIdoso.class));
    }
}
