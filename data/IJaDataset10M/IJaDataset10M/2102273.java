package com.googlecode.akangatu.combo.servico.localidade;

import java.util.ArrayList;
import com.googlecode.akangatu.combo.negocio.localidade.EstadoDao;

/**
 * @see com.googlecode.akangatu.combo.servico.localidade.LocalidadeService
 */
public class LocalidadeServiceImpl extends com.googlecode.akangatu.combo.servico.localidade.LocalidadeServiceBase {

    /**
	 * @see com.googlecode.akangatu.combo.servico.localidade.LocalidadeService#recuperaEstados()
	 */
    protected java.util.List handleRecuperaEstados() throws java.lang.Exception {
        return new ArrayList(getEstadoDao().loadAll(EstadoDao.TRANSFORM_ESTADOVO));
    }

    /**
	 * @see com.googlecode.akangatu.combo.servico.localidade.LocalidadeService#recuperaCidades(java.lang.Long)
	 */
    protected java.util.List handleRecuperaCidades(java.lang.Long idEstado) throws java.lang.Exception {
        return getCidadeDao().recuperaCidades(idEstado);
    }
}
