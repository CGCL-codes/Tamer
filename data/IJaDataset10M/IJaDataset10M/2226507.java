package br.com.insight.consultoria.negocio.bo.interfacebo;

import java.util.List;
import br.com.insight.consultoria.entity.RamoAtividade;
import br.com.insight.consultoria.erro.exception.InsightException;

public interface RamoAtividadeBO {

    public List<RamoAtividade> listaRamoDeAtividade() throws InsightException;

    public RamoAtividade getRamoAtividade(Long id) throws InsightException;
}
