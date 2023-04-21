package it.cilea.osd.jdyna.dao;

import it.cilea.osd.common.dao.PaginableObjectDao;
import it.cilea.osd.jdyna.model.AlberoClassificatorio;
import it.cilea.osd.jdyna.model.Classificazione;
import java.util.List;

public interface AlberoClassificatorioDao extends PaginableObjectDao<AlberoClassificatorio, Integer> {

    public List<AlberoClassificatorio> findAll();

    public AlberoClassificatorio uniqueByNome(String nome);

    public List<Classificazione> findTopClassificazioni(Integer alberoPK);
}
