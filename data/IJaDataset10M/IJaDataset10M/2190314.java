package br.com.insight.consultoria.negocio.fachada.areaatuacao;

import java.util.List;
import br.com.insight.consultoria.entity.AreaAtuacao;
import br.com.insight.consultoria.erro.exception.InsightException;
import br.com.insight.consultoria.negocio.bo.interfacebo.AreaAtuacaoBO;

public class FachadaAreaAtuacaoBean implements FachadaAreaAtuacao {

    private AreaAtuacaoBO areaAtuacaoBO;

    public List<AreaAtuacao> listarAreaAtuacao() throws InsightException {
        return getAreaAtuacaoBO().listarAreaAtuacao();
    }

    public AreaAtuacao getAreaAtuacao(Long id) throws InsightException {
        return getAreaAtuacaoBO().getAreaAtuacao(id);
    }

    public AreaAtuacaoBO getAreaAtuacaoBO() {
        return areaAtuacaoBO;
    }

    public void setAreaAtuacaoBO(AreaAtuacaoBO areaAtuacaoBO) {
        this.areaAtuacaoBO = areaAtuacaoBO;
    }
}
