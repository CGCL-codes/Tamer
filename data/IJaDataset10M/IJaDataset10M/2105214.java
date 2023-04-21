package br.ufmg.lcc.pcollecta.model;

import java.util.List;
import br.ufmg.lcc.arangi.commons.BasicException;
import br.ufmg.lcc.arangi.dto.BasicDTO;
import br.ufmg.lcc.arangi.model.IPersistenceObject;
import br.ufmg.lcc.arangi.model.StandardBusinessObject;
import br.ufmg.lcc.pcollecta.dto.Agendamento;
import br.ufmg.lcc.pcollecta.dto.Horario;
import br.ufmg.lcc.pcollecta.dto.Processo;

public class AgendamentoBO extends StandardBusinessObject {

    public List<Agendamento> recuperaAgendamentosProcesso(IPersistenceObject dao) throws BasicException {
        List<Agendamento> listaAgendamentos = null;
        String hql = "select new Agendamento(agendamento.id, agendamento.situacao," + " agendamento.tipoRepeticao, agendamento.dataProximaExecucao, agendamento.processamento, " + " processo.id, processo.name) " + " from agendamento in class br.ufmg.lcc.pcollecta.dto.Agendamento, " + " processo in class br.ufmg.lcc.pcollecta.dto.Processo" + " where agendamento.processo.id = processo.id and agendamento.situacao <> 'F'" + " and agendamento.situacao <> 'C'";
        listaAgendamentos = this.executeQuery(dao, hql, null);
        hql = "select new Horario(horario.id, horario.horaInicio, horario.horaTermino) " + " from horario in class br.ufmg.lcc.pcollecta.dto.Horario " + " where horario.master.id = :id";
        List<Horario> listaHorarios = null;
        for (Agendamento agendamento : listaAgendamentos) {
            listaHorarios = this.executeQuery(dao, hql, agendamento);
            agendamento.setDetalheHorariosExecucao(listaHorarios);
        }
        return listaAgendamentos;
    }

    public List<Agendamento> recuperaAgendamentos(IPersistenceObject dao, Processo processo) throws BasicException {
        List<Agendamento> listaAgendamentos = null;
        String hql = "select new Agendamento(agendamento.id, agendamento.situacao," + " agendamento.tipoRepeticao, agendamento.dataProximaExecucao, agendamento.processamento, " + " processo.id, processo.name) " + " from agendamento in class br.ufmg.lcc.pcollecta.dto.Agendamento, " + " processo in class br.ufmg.lcc.pcollecta.dto.Processo" + " where agendamento.processo.id = :id ";
        listaAgendamentos = this.executeQuery(dao, hql, processo);
        return listaAgendamentos;
    }

    public Agendamento recuperaAgendamento(IPersistenceObject dao, Agendamento agendamento) throws BasicException {
        String hql = "select agendamento from agendamento in class br.ufmg.lcc.pcollecta.dto.Agendamento" + " where agendamento.id = :id";
        List lista = this.executeQuery(dao, hql, agendamento);
        if (lista != null && lista.size() > 0) {
            return (Agendamento) lista.get(0);
        }
        return null;
    }

    public List<Agendamento> verificaProcessoEmExecucao(IPersistenceObject dao, Processo processo) throws BasicException {
        List<Agendamento> listaAgendamentos = null;
        Agendamento agendamento = new Agendamento();
        agendamento.setProcesso(processo);
        String hql = "select new Agendamento(agendamento.id, agendamento.situacao, processo.id, " + " processo.name, agendamento.dataProximaExecucao, agendamento.processamento) " + " from agendamento in class br.ufmg.lcc.pcollecta.dto.Agendamento, " + " processo in class br.ufmg.lcc.pcollecta.dto.Processo " + " where agendamento.processo.id = processo.id and " + " agendamento.processo = :processo and agendamento.situacao = 'E' ";
        listaAgendamentos = this.executeQuery(dao, hql, agendamento);
        return listaAgendamentos;
    }

    private void verificaAgendamentoCorreto(Agendamento dto) throws BasicException {
        if (dto.getTipoRepeticao().equals(Agendamento.MINUTOS)) {
            if (dto.getMinutosEntreExecucoes() == null || dto.getMinutosEntreExecucoes() <= 0) {
                throw new BasicException("Repeti��o por minutos requer intervalo entre execu��es", "erroMinutosIndefinidos");
            }
        }
    }

    public List<Agendamento> buscaProcessoAgendados(IPersistenceObject dao, Processo processo) throws BasicException {
        List<Agendamento> listaAgendamentos = null;
        Agendamento agendamento = new Agendamento();
        agendamento.setProcesso(processo);
        String hql = "select agendamento from agendamento in class br.ufmg.lcc.pcollecta.dto.Agendamento" + " where agendamento.processo = :processo";
        listaAgendamentos = this.executeQuery(dao, hql, agendamento);
        return listaAgendamentos;
    }

    public List<Agendamento> verificaAgendamentosEmExecucao(IPersistenceObject dao) throws BasicException {
        List<Agendamento> listaAgendamentos = null;
        Agendamento agendamento = new Agendamento();
        String hql = "select agendamento from agendamento in class br.ufmg.lcc.pcollecta.dto.Agendamento" + " where agendamento.situacao = 'E' ";
        listaAgendamentos = this.executeQuery(dao, hql, agendamento);
        return listaAgendamentos;
    }

    @Override
    protected BasicDTO beforeInsert(IPersistenceObject persistenceObject, BasicDTO dto) throws BasicException {
        verificaAgendamentoCorreto((Agendamento) dto);
        return dto;
    }

    @Override
    protected BasicDTO beforeUpdate(IPersistenceObject persistenceObject, BasicDTO dto, BasicDTO oldDto) throws BasicException {
        verificaAgendamentoCorreto((Agendamento) dto);
        return dto;
    }
}
