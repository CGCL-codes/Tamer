package br.org.ged.direto.model.repository;

import java.util.List;
import br.org.ged.direto.model.entity.Historico;

public interface HistoricoRepository {

    public void save(Historico historico);

    public List<Historico> getHistoricoByDocumento(Integer idDocumentoDetalhes);

    public List<Historico> getHistoricoByUsuario(Integer idUsuario);

    public List<Historico> getHistoricoByCarteira(Integer idCarteira);
}
