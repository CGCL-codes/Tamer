package Controller;

import Dao.AlunoDao;
import Dao.Conexao;
import Dao.GrupoHistoricoDao;
import Dao.JDBCAlunoDao;
import Dao.JDBCGrupoHistoricoDao;
import Dao.JDBCTutorDao;
import Dao.TutorDao;
import Model.Aluno;
import Model.Grupo;
import Model.GrupoHistorico;
import Model.Petiano;
import Model.Tutor;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Esta é uma classe de controle que realiza a interação entre a view,
 * os javabeans "Tutor" e "Grupo" e o banco de dados.
 *
 * @author Pedro Augusto
 * @version 3.0
 * @since 3.0
 */
@ManagedBean(name = "GrupoHistoricoController")
@RequestScoped
public class GrupoHistoricoController {

    private ArrayList<GrupoHistorico> historicoAtivacao;

    private ArrayList<GrupoHistorico> historicoDesativacao;

    private GrupoHistoricoDao histdao;

    private ArrayList<Aluno> petianos;

    private ArrayList<Aluno> exPetianos;

    private ArrayList<Tutor> tutores;

    private Tutor tutorVigente;

    private AlunoDao alunoDao;

    private TutorDao tutorDao;

    private Grupo grupo;

    /**
     * Esta é uma classe interna que organiza um par de "desativação" e "reativação" de um grupo.
     *
     * @author Pedro Augusto
     * @version 3.0
     * @since 3.0
     */
    public class AtivacaoDesativacao {

        private GrupoHistorico desativacao;

        private GrupoHistorico reativacao;

        /**
         *
         * @author Pedro Augusto
         * @return uma operação de desativação de um grupo
         * @version 3.0
         * @since 3.0
         */
        public GrupoHistorico getDesativacao() {
            return desativacao;
        }

        /**
         * Define uma operação de desativação de um grupo no atributo "desativação"
         *
         * @author Pedro Augusto
         * @version 3.0
         * @since 3.0
         * @param desativacao operação de desativação de um grupo
         */
        public void setDesativacao(GrupoHistorico desativacao) {
            this.desativacao = desativacao;
        }

        /**
         * Define uma operação de reativação de um grupo no atributo "reativação"
         *
         * @author Pedro Augusto
         * @version 3.0
         * @since 3.0
         * @param reativacao operação de reativação de um grupo
         */
        public void setReativacao(GrupoHistorico ativacao) {
            this.reativacao = ativacao;
        }

        /**
         *
         * @author Pedro Augusto
         * @return uma operação de desativação de um grupo
         * @version 3.0
         * @since 3.0
         */
        public GrupoHistorico getReativacao() {
            return reativacao;
        }
    }

    private ArrayList<AtivacaoDesativacao> ativacaoDesativacao;

    /**
     * Classe interna que implementa um comparator para Petiano, necessária para
     * ordenação dos petianos dentro do ArrayList de acordo com o nome.
     * 
     * @author Yassin
     */
    private class ComparadorNomePetiano implements Comparator<Petiano> {

        @Override
        public int compare(Petiano p1, Petiano p2) {
            String s1, s2;
            s1 = Normalizer.normalize(p1.getNome(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
            s2 = Normalizer.normalize(p2.getNome(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
            return s1.compareToIgnoreCase(s2);
        }
    }

    /**
     * Construtor padrão da classe. Inicializa as variáveis.
     *
     * @author Pedro Augusto
     * @throws SQLException
     * @version 3.0
     * @since 3.0
     */
    public GrupoHistoricoController() throws SQLException {
        histdao = new JDBCGrupoHistoricoDao();
        petianos = new ArrayList<Aluno>();
        exPetianos = new ArrayList<Aluno>();
        historicoDesativacao = new ArrayList<GrupoHistorico>();
        alunoDao = new JDBCAlunoDao();
        tutorDao = new JDBCTutorDao();
        ativacaoDesativacao = new ArrayList<AtivacaoDesativacao>();
        grupo = LoginController.getGrupo();
        carregarHistoricoGrupo();
        carregarPetianos();
        carregarTutores();
    }

    /**
     * Busca do banco de dados todas as operações de desativação e reativação de um grupo e
     * carrega no atributo "historicoAtivacao".
     *
     * @author Pedro Augusto
     * @throws SQLException
     * @version 3.0
     * @since 3.0
     */
    private void carregarHistoricoGrupo() throws SQLException {
        histdao.setConexao(Conexao.getConnection());
        historicoAtivacao = histdao.listarTodos(LoginController.getGrupo().getId());
        histdao.getConexao().close();
        separarHistorico();
        ordenarHistorico();
    }

    /**
     * Separa as operações de desativação e reativação de um grupo, armazenando-as nos atributos
     * "historicoDesativacao" e "historicoAtivacao", respectivamente.
     *
     * @author Pedro Augusto
     * @version 3.0
     * @since 3.0
     */
    private void separarHistorico() {
        for (int i = 0; i < historicoAtivacao.size(); i++) {
            if (historicoAtivacao.get(i).getOperacao() == GrupoHistorico.DESATIVACAO) {
                historicoDesativacao.add(historicoAtivacao.get(i));
                historicoAtivacao.remove(i);
            }
        }
    }

    /**
     * Organiza as operações de desativação e reativação de um grupo, criando uma lista
     * de objetos "AtivacaoDesativacao", os quais guardam uma dupla de desativação e reativação consecutivas.
     *
     * @author Pedro Augusto
     * @version 3.0
     * @since 3.0
     */
    private void ordenarHistorico() {
        for (int i = 0; i < historicoDesativacao.size(); i++) {
            AtivacaoDesativacao ativaDesativa = new AtivacaoDesativacao();
            ativaDesativa.setDesativacao(historicoDesativacao.get(i));
            ativaDesativa.setReativacao(historicoAtivacao.get(i));
            ativacaoDesativacao.add(ativaDesativa);
        }
    }

    /**
     * Busca do banco de dados todos os petianos e ex-petianos do grupo ao qual pertence o tutor ou aluno logado
     * no sistema, passando o id do grupo como parâmetro para a busca, essa lista é carregada no atributo "petianos".
     *
     * @author Pedro Augusto
     * @throws SQLException
     * @version 3.0
     * @since 3.0
     */
    private void carregarPetianos() throws SQLException {
        alunoDao.setConexao(Conexao.getConnection());
        petianos = alunoDao.listarTodos(LoginController.getGrupo().getId());
        Collections.sort(petianos, new ComparadorNomePetiano());
        alunoDao.getConexao().close();
        separarPetianos();
    }

    /**
     * Separa os petianos dos ex-petianos do grupo ao qual pertence o tutor ou aluno logado no sistema,
     * mantendo os petianos no atributo "petianos" e os ex-petianos são movidos
     * para o atributo "exPetianos".
     *
     * @author Pedro Augusto
     * @version 3.0
     * @since 3.0
     */
    private void separarPetianos() {
        int tamanho = petianos.size();
        int i = 0;
        int j = tamanho - 1;
        while (i < tamanho) {
            if (!(petianos.get(j).getDataSaidaPet() == null)) {
                exPetianos.add(petianos.remove(j));
            }
            i++;
            j--;
        }
        Collections.sort(exPetianos, new ComparadorNomePetiano());
    }

    /**
     * Busca do banco de dados o atual tutor e os ex-tutores do grupo ao qual pertence o tutor ou aluno logado
     * no sistema, passando o id do grupo como parâmetro para a busca, essa lista é carregada no atributo "tutores".
     *
     * @author Pedro Augusto
     * @throws SQLException
     * @version 3.0
     * @since 3.0
     */
    private void carregarTutores() throws SQLException {
        tutorDao.setConexao(Conexao.getConnection());
        tutores = tutorDao.listarTodos(LoginController.getGrupo().getId());
        Collections.sort(tutores, new ComparadorNomePetiano());
        tutorDao.getConexao().close();
        separaTutorVigente();
    }

    /**
     * Separa o tutor vigente dos ex-tutores do grupo ao qual pertence o tutor ou aluno logado no sistema,
     * mantendo os ex-tutores no atributo "tutores" e movendo o tutor vigente para o atributo "tutorVigente".
     *
     * @author Pedro Augusto
     * @version 3.0
     * @since 3.0
     */
    private void separaTutorVigente() {
        int tamanho = tutores.size();
        int i = 0;
        int j = tamanho - 1;
        while (i < tamanho) {
            if (tutores.get(j).getDataSaidaPet() == null) {
                tutorVigente = tutores.remove(j);
            }
            i++;
            j--;
        }
    }

    /**
     *
     * @author Pedro Augusto
     * @return lista de ex-petianos do grupo ao qual pertence o tutor ou aluno logado no sistema.
     * @version 3.0
     * @since 3.0
     */
    public ArrayList<Aluno> getExPetianos() {
        return exPetianos;
    }

    /**
     *
     * @author Pedro Augusto
     * @return lista de petianos do grupo ao qual pertence o tutor ou aluno logado no sistema.
     * @version 3.0
     * @since 3.0
     */
    public ArrayList<Aluno> getPetianos() {
        return petianos;
    }

    /**
     *
     * @author Pedro Augusto
     * @return lista de ex-tutores do grupo ao qual pertence o tutor ou aluno logado no sistema.
     * @version 3.0
     * @since 3.0
     */
    public ArrayList<Tutor> getTutores() {
        Tutor aux;
        for (int i = 0; i < tutores.size(); i++) {
            aux = tutores.get(i);
            tutores.add(aux);
            tutores.remove(i);
        }
        return tutores;
    }

    /**
     *
     * @author Pedro Augusto
     * @return o tutor vigente do grupo ao qual pertence o tutor ou aluno logado no sistema.
     * @version 3.0
     * @since 3.0
     */
    public Tutor getTutor() {
        return tutorVigente;
    }

    /**
     *
     * @author Pedro Augusto
     * @return grupo ao qual pertence o tutor ou aluno logado no sistema.
     * @version 3.0
     * @since 3.0
     */
    public Grupo getGrupo() {
        return grupo;
    }

    /**
     *
     * @author Pedro Augusto
     * @return lista de reativações pela qual passou o grupo ao qual pertence o tutor ou aluno logado no sistema.
     * @version 3.0
     * @since 3.0
     */
    public ArrayList<GrupoHistorico> getHistoricoAtivacao() {
        return historicoAtivacao;
    }

    /**
     *
     * @author Pedro Augusto
     * @return lista de desativações pela qual passou o grupo ao qual pertence o tutor ou aluno logado no sistema.
     * @version 3.0
     * @since 3.0
     */
    public ArrayList<GrupoHistorico> getHistoricoDesativacao() {
        return historicoDesativacao;
    }

    /**
     *
     * @author Pedro Augusto
     * @return lista de objetos "AtivacaoDesativacao", os quais mantém uma desativação e uma reativação consecutiva.
     * @version 3.0
     * @since 3.0
     */
    public ArrayList<AtivacaoDesativacao> getAtivacaoDesativacao() {
        return ativacaoDesativacao;
    }
}
