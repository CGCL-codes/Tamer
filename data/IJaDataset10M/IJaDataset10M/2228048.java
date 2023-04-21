package br.com.lawoffice.agenda.service;

import java.util.Date;
import java.util.List;
import br.com.lawoffice.dominio.Agenda;
import br.com.lawoffice.dominio.Colaborador;
import br.com.lawoffice.dominio.Evento;

/**
 * 
 * Interface de serviço para realizar operações na {@link Agenda}
 * 
 * do {@link Colaborador}
 * 
 * @author robson
 *
 */
public interface AgendaService {

    /**
	 * Retorna os eventos da {@link Agenda} do {@link Colaborador} entre o periodo das
	 * 
	 * datas passada.
	 * 
	 * <br><br>
	 * 
	 * Se o {@link Colaborador} passado for nullo ou não estiver na base
	 *  
	 * a consulta não será realizada e uma lista vázia será retornada.
	 * 
	 * <br><br>
	 * 
	 * Se as datas estiver nula a consulta não será realizada e uma lista vázia será retornada.
	 * 
	 * @param colaborador que a {@link Agenda} será consultada.
	 * @param dataIncial para consulta.
	 * @param dataFinal para consulta.
	 * @return {@link List}
	 */
    List<Evento> listarEventos(Colaborador colaborador, Date dataIncial, Date dataFinal);

    Evento adicionarEvento(Colaborador colaborador, Evento evento);

    Evento atualizarEvento(Evento evento);
}
