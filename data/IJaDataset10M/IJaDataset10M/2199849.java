package br.com.srv.componentes.relatorio.velocidademaxima.dao;

import java.util.List;
import br.com.srv.model.VelocidadeMaximaTO;

public interface VelocidadeMaximaDAO {

    public List<VelocidadeMaximaTO> consultaVelocidadeMaxima(VelocidadeMaximaTO velocidadeMaximaParam) throws Exception;
}
