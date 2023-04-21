package br.com.srv.componentes.panico.service;

import java.util.List;
import HTTPClient.HTTPConnection;
import br.com.srv.componentes.panico.dao.PanicoAlarmeDAO;
import br.com.srv.componentes.panico.dao.PanicoAlarmeDAOImpl;
import br.com.srv.model.ConfiguracaoTO;

public class PanicoAlarmeServiceImpl implements PanicoAlarmeService {

    private PanicoAlarmeDAO panicoAlarmeDAO;

    public PanicoAlarmeServiceImpl(HTTPConnection connection) {
        panicoAlarmeDAO = new PanicoAlarmeDAOImpl(connection);
    }

    public ConfiguracaoTO buscarTelefonesPanicoAlarme(String veiculoId) throws Exception {
        List<ConfiguracaoTO> configuracoes = panicoAlarmeDAO.buscarTelefonesPanicoAlarme(veiculoId);
        ConfiguracaoTO configuracaoTO = new ConfiguracaoTO();
        if (!configuracoes.isEmpty()) {
            configuracaoTO = configuracoes.get(0);
        }
        return configuracaoTO;
    }

    public boolean cadastrarConfiguracaoPanicoAlarme(String veiculoId, ConfiguracaoTO configuracaoTO) throws Exception {
        return panicoAlarmeDAO.cadastrarConfiguracaoPanicoAlarme(veiculoId, configuracaoTO);
    }
}
