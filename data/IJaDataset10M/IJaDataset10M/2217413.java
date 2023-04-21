package br.ufrj.cad.view.disciplina;

import java.util.List;
import br.ufrj.cad.model.bo.AnoBase;
import br.ufrj.cad.model.bo.Departamento;
import br.ufrj.cad.model.bo.Disciplina;
import br.ufrj.cad.model.disciplina.DisciplinaService;
import br.ufrj.cad.model.to.AnoBaseTO;
import br.ufrj.cad.model.to.DisciplinaTO;

public class DisciplinaActionHelper {

    public static AnoBase obtemAnoBase(AnoBaseTO anoBaseTO, Departamento departamento) {
        AnoBase anoBase = new AnoBase(anoBaseTO);
        AnoBase anoBaseNoBanco = DisciplinaService.getInstance().obtemAnoBasePorId(anoBase.getId(), departamento);
        return anoBaseNoBanco;
    }

    @SuppressWarnings("unchecked")
    public static List<Disciplina> executaPesquisaDisciplinas(DisciplinaTO filtro, AnoBaseTO anoBaseTO, Departamento departamento) {
        List<Disciplina> disciplinas = DisciplinaService.getInstance().obtemListaDisciplinas(filtro, anoBaseTO, departamento);
        return disciplinas;
    }
}
