package br.com.cinefilmes.dao;

import javax.ejb.Local;
import br.com.cinefilmes.domain.TipoFilme;
import br.com.cinefilmes.persistence.GenericDAO;

@Local
public interface TipoFilmeDAO extends GenericDAO<TipoFilme, Long> {
}
