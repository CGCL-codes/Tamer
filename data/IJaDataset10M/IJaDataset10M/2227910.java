package br.com.insight.consultoria.negocio.bo.interfacebo;

import java.util.List;
import br.com.insight.consultoria.entity.Tipo;
import br.com.insight.consultoria.entity.TipoEspecifico;
import br.com.insight.consultoria.erro.exception.InsightException;

public interface TipoEspecificoBO {

    public List<TipoEspecifico> listarTipoEspecifico(Tipo tipo) throws InsightException;

    public TipoEspecifico getTipoEspecifico(Long id) throws InsightException;
}
