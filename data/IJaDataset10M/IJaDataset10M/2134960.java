package br.com.insight.consultoria.negocio.bo.interfacebo;

import java.util.List;
import br.com.insight.consultoria.entity.CursoExtraCurricular;
import br.com.insight.consultoria.entity.Usuario;
import br.com.insight.consultoria.erro.exception.InsightException;

public interface CursoExtraCurricularBO {

    public void inserir(CursoExtraCurricular cursoExtraCurricular) throws InsightException;

    public void alterar(CursoExtraCurricular cursoExtraCurricular) throws InsightException;

    public void excluir(CursoExtraCurricular cursoExtraCurricular) throws InsightException;

    public CursoExtraCurricular getCursoExtraCurricular(Long id) throws InsightException;

    public List<CursoExtraCurricular> pesquisar(Usuario usuario) throws InsightException;
}
