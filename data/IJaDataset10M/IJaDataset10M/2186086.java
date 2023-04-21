package br.ufrj.cad.view.aluno;

import br.ufrj.cad.model.bo.Aluno;
import br.ufrj.cad.model.to.AlunoTO;

public class AlunoHelper {

    public static AlunoTO obtemTOAluno(Aluno aluno) {
        AlunoTO resultado = new AlunoTO();
        if (aluno != null) {
            if (aluno.getId() != null) {
                resultado.setId(String.valueOf(aluno.getId()));
            }
            resultado.setNome(aluno.getNome());
            resultado.setDre(aluno.getDre());
        }
        return resultado;
    }
}
