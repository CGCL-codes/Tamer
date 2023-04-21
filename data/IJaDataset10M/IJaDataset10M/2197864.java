package br.com.srv.componentes.panico.dao;

import java.util.List;
import br.com.srv.model.ConfiguracaoTO;

public interface PanicoAlarmeDAO {

    public List<ConfiguracaoTO> buscarTelefonesPanicoAlarme(String veiculoId) throws Exception;

    public boolean cadastrarConfiguracaoPanicoAlarme(String veiculoId, ConfiguracaoTO configuracaoTO) throws Exception;
}
