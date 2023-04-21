package br.com.insight.consultoria.integracao.dao.interfacedao;

import java.util.List;
import br.com.insight.consultoria.entity.ConheceuInsight;
import br.com.insight.consultoria.erro.exception.InsightException;

public interface ConheceuInsightDAO {

    public ConheceuInsight getConheceuInsgiht(Long id) throws InsightException;

    public List<ConheceuInsight> listaConheceuInsgiht() throws InsightException;
}
