package ensino2.dao;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import br.ufpr.biblioteca.bean.UsuarioBean;
import deletaresubstituir.cursos.DisciplinaCurso;
import deletaresubstituir.secretaria.Secretaria;
import ensino2.Disciplina;
import ensino2.comum.Transicao;
import ensino2.comum.ensinoGeral;

public abstract class TurmaDao {

    public static ArrayList<Disciplina> carregarDisciplina(HttpServletRequest req) {
        UsuarioBean ub = ensinoGeral.getUsuarioLogged(req);
        ArrayList<Disciplina> arDis = new ArrayList<Disciplina>();
        int[] disciplinas = Secretaria.getDisciplinas(Transicao.getCodLogado());
        for (int i = 0; i < disciplinas.length - 1; i++) {
            DisciplinaCurso disC = new DisciplinaCurso(disciplinas[i]);
            disC.obterDisciplina(disC);
            Disciplina dis = new Disciplina(disC.getCodDisciplina(), ub.getNome(), disC.getNomeDisciplina());
            arDis.add(dis);
        }
        return arDis;
    }
}
