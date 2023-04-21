package Controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import javax.faces.context.FacesContext;
import Dao.*;
import Model.Curso;
import Model.Grupo;
import Model.GrupoHistorico;
import Model.Instituicao;
import Model.Tutor;
import converters.Criptografia;
import exceptions.ForbiddenException;
import exceptions.PageNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;
import org.apache.commons.mail.EmailException;

/**
 * Esta é uma classe de controle que realiza a interação entre a view,
 * os javabeans "Grupo", "Curso" e "Tutor" e o banco de dados.
 *
 * @author Douglas Antunes
 * @version 3.0
 * @since 3.0
 */
@ManagedBean(name = "GrupoController")
@ViewScoped
public class GrupoController {

    private GrupoDao grupoDao;

    private Grupo grupo;

    private CursoDao cursoDao;

    private Curso curso;

    private Tutor tutor;

    private TutorDao tutorDao;

    private String motivo;

    private int passo;

    private String id;

    private Grupo[] gruposSelecionados;

    private ArrayList<Grupo> gruposAtivos;

    public GrupoController() throws SQLException {
        grupoDao = new JDBCGrupoDao();
        cursoDao = new JDBCCursoDao();
        tutorDao = new JDBCTutorDao();
        grupo = new Grupo();
        grupo.setId(0);
        curso = new Curso();
        tutor = new Tutor();
        passo = 1;
    }

    /**
     * Verifica o id da instituição para certificar se o usuário tem permissão
     * para executar uma determinada ação.
     *
     * @author Tiago Peres
     * @return true, se é seguro. false se não é seguro
     * @version 3.0
     * @since 3.0
     */
    private boolean isSeguro() {
        Instituicao iLogada = LoginController.getInstituicao();
        try {
            return (iLogada.equals(grupo.getInstituicao()));
        } catch (NullPointerException ex) {
            return false;
        }
    }

    /**
     * Retorna uma lista dos grupos PET ativos da instituição do "cla" logado.
     *
     * @author Douglas Antunes
     * @return Lista de Grupos PET ativos na "instituicao".
     * @throws SQLException
     * @version 3.0
     * @since 3.0
     */
    public ArrayList<Grupo> getGruposAtivos() throws SQLException {
        if (gruposAtivos == null) {
            gruposAtivos = getGrupos();
            tutorDao.setConexao(Conexao.getConnection());
            for (int i = 0; i < gruposAtivos.size(); i++) {
                Grupo g = gruposAtivos.get(i);
                if (!g.isAtivado()) {
                    gruposAtivos.remove(i);
                } else {
                    g.setTutor(tutorDao.recuperarTutorAtivo(g.getId()));
                }
            }
            tutorDao.getConexao().close();
        }
        return gruposAtivos;
    }

    /**
     * Salva no banco de dados o grupo, o tutor e o curso cadastrado pelo CLA e que foram carregados nos atributos
     * "grupo", "tutor" e "curso"
     *
     * @author Douglas Antunes
     * @return String com a página a qual o usuário será redirecionado após a operação de salvar.
     * @throws SQLException, EmailException
     * @version 3.0
     * @since 3.0
     */
    public String salvar() throws SQLException, EmailException {
        Instituicao instituicao = LoginController.getInstituicao();
        FacesContext context = FacesContext.getCurrentInstance();
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        Connection conexao = Conexao.getConnection();
        conexao.setAutoCommit(false);
        try {
            grupoDao.setConexao(conexao);
            tutorDao.setConexao(conexao);
            cursoDao.setConexao(conexao);
            if (grupo.getTipo() != Grupo.SECAD) {
                curso.setInstituicao(instituicao);
                curso.setId(cursoDao.salvar(curso));
            }
            grupo.setInstituicao(instituicao);
            grupo.setCurso(curso);
            grupo.setId(grupoDao.salvar(grupo));
            tutor.setGrupo(grupo);
            tutor.setSenha(Criptografia.randomMd5());
            tutorDao.salvar(tutor);
            conexao.commit();
            context.addMessage(null, new FacesMessage("O grupo foi cadastrado com sucesso!"));
            ArrayList<String> destinatarios = new ArrayList<String>();
            destinatarios.add(tutor.getEmail());
            UsuarioController.boasVindas(tutor.getNome(), tutor.getSenha(), destinatarios);
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1062) context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "O endereço de e-mail do tutor informado já existe no sistema.", null)); else context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro na conexão com o banco de dados. Por favor, tente mais tarde." + "Caso o problema persista cantacte o administrador do sistema.", null));
            Logger.getLogger(GrupoController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexao.setAutoCommit(true);
            conexao.close();
        }
        return ("grupo.xhtml?faces-redirect=true");
    }

    /**
     * Carrega do banco de dados as informações do grupo pelo atributo "id" no atributo "grupo", posteriormente
     * carrega o tutor do grupo no atributo "tutor" e o curso referente ao grupo ao atributo "curso", se o grupo
     * em questão está vinculado a um curso.
     *
     * @author Douglas Antunes
     * @throws SQLException, IOException
     * @version 3.0
     * @since 3.0
     */
    public void carregarGrupo() throws SQLException, IOException {
        Connection conexao = Conexao.getConnection();
        grupoDao.setConexao(conexao);
        tutorDao.setConexao(conexao);
        cursoDao.setConexao(conexao);
        if (id != null) {
            grupo = grupoDao.recuperarPorId(Integer.parseInt(id));
            if (grupo == null) throw new PageNotFoundException();
            tutor = tutorDao.recuperarTutorAtivo(grupo.getId());
            if (tutor == null) {
                tutor = new Tutor();
            }
            if (grupo.getCurso().getId() != 0) {
                curso = cursoDao.recuperarPorId(grupo.getCurso().getId());
            }
        }
        conexao.close();
    }

    /**
     * Salva no banco de dados a mudança de status sofrida pelo grupo, seja ela uma reativação se o grupo está desativado
     * ou uma desativação se o grupo está ativo.
     *
     * @author Douglas Antunes
     * @version 3.0
     * @since 3.0
     */
    public void alterarStatus() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (isSeguro()) {
            GrupoHistorico h = new GrupoHistorico();
            h.setGrupo(grupo);
            h.setJustificativa(motivo);
            h.setData(new Date());
            String msg;
            try {
                Connection conexao = Conexao.getConnection();
                conexao.setAutoCommit(false);
                grupoDao.setConexao(conexao);
                grupo = grupoDao.recuperarPorId(grupo.getId());
                if (grupo.isAtivado()) {
                    grupo.setAtivado(false);
                    h.setOperacao(GrupoHistorico.DESATIVACAO);
                    msg = "O grupo foi desativado com sucesso!";
                } else {
                    grupo.setAtivado(true);
                    h.setOperacao(GrupoHistorico.ATIVACAO);
                    msg = "O grupo foi reativado com sucesso!";
                }
                grupoDao.editar(grupo);
                GrupoHistoricoDao hdao = new JDBCGrupoHistoricoDao(conexao);
                hdao.salvar(h);
                conexao.commit();
                conexao.setAutoCommit(true);
                conexao.close();
                context.addMessage(null, new FacesMessage(msg));
            } catch (SQLException ex) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro na conexão com o banco de dados. Por favor, tente mais tarde." + "Caso o problema persista contacte o administrador do sistema.", null));
                Logger.getLogger(GrupoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else throw new ForbiddenException();
    }

    /**
     * Edita apenas informações básicas do grupo. Utilizado pelo tutor.
     *
     * @author Douglas Antunes
     * @throws SQLException
     * @version 3.0
     * @since 3.0
     */
    public void editarSimples() throws SQLException {
        if (!isSeguro()) throw new ForbiddenException();
        FacesContext context = FacesContext.getCurrentInstance();
        grupoDao.setConexao(Conexao.getConnection());
        grupoDao.getConexao().setAutoCommit(false);
        try {
            grupoDao.editar(grupo);
            grupoDao.getConexao().commit();
            context.addMessage(null, new FacesMessage("Os dados do grupo foram alterados com sucesso!"));
        } catch (SQLException ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro na conexão com o banco de dados. Por favor, tente mais tarde." + "Caso o problema persista contacte o administrador do sistema.", null));
            Logger.getLogger(GrupoController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            grupoDao.getConexao().setAutoCommit(true);
            grupoDao.getConexao().close();
        }
    }

    /**
     * Atualiza as informações referentes ao grupo, o tutor e ao curso, que estão nos atributos "grupo", "tutor" e "curso",
     * no banco de dados.
     *
     * @author Douglas Antunes
     * @throws SQLException
     * @version 3.0
     * @since 3.0
     */
    public String editar() throws UnsupportedEncodingException, SQLException {
        if (!isSeguro()) throw new ForbiddenException();
        FacesContext context = FacesContext.getCurrentInstance();
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        Connection conexao = Conexao.getConnection();
        conexao.setAutoCommit(false);
        try {
            grupoDao.setConexao(conexao);
            tutorDao.setConexao(conexao);
            cursoDao.setConexao(conexao);
            if (grupo.getTipo() != Grupo.SECAD && curso.getId() != 0) {
                System.out.println(curso.getId() + ": " + curso.getNome());
                cursoDao.editar(curso);
            } else if (grupo.getTipo() != Grupo.SECAD && curso.getId() == 0) {
                curso = new Curso();
                curso.setFormacao(Curso.BACHARELADO);
                curso.setPeriodo(Curso.SEMESTRAL);
                curso.setNome(null);
                curso.setInstituicao(grupo.getInstituicao());
                curso.setId(cursoDao.salvar(curso));
                grupo.setCurso(curso);
            }
            grupoDao.editar(grupo);
            if (tutor.getId() != 0) {
                tutorDao.editar(tutor);
            } else {
                tutor.setGrupo(grupo);
                String senha = Criptografia.randomMd5();
                tutor.setSenha(senha);
                tutorDao.salvar(tutor);
                ArrayList<String> destinatarios = new ArrayList<String>();
                destinatarios.add(tutor.getEmail());
                UsuarioController.boasVindas(tutor.getNome(), senha, destinatarios);
                context.addMessage(null, new FacesMessage("O novo tutor foi cadastrado com sucesso!"));
            }
            conexao.commit();
            context.addMessage(null, new FacesMessage("Os dados do grupo foram alterados com sucesso!"));
            if (tutor.getDataSaidaPet() != null) {
                context.addMessage(null, new FacesMessage("A operação de desligamento do tutor foi realizada com sucesso!"));
            }
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1062) context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "O endereço de e-mail do tutor informado já existe no sistema.", null)); else context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro na conexão com o banco de dados. Por favor, tente mais tarde." + "Caso o problema persista cantacte o administrador do sistema.", null));
            Logger.getLogger(GrupoController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexao.setAutoCommit(true);
            conexao.close();
        }
        return "sucesso";
    }

    /**
     * Retorna uma lista de todos os grupos da instituição à qual pertence o cla que está logado
     *
     * @author Douglas Antunes
     * @return Lista de todos os grupos da instituição à qual pertence o cla que está logado
     * @throws SQLException
     * @version 3.0
     * @since 3.0
     */
    public ArrayList<Grupo> getGrupos() throws SQLException {
        grupoDao.setConexao(Conexao.getConnection());
        ArrayList<Grupo> grupos = grupoDao.listarTodos(LoginController.getInstituicao().getId());
        grupoDao.getConexao().close();
        return grupos;
    }

    /**
     * Retorna o grupo carregado no atributo "grupo"
     *
     * @author Douglas Antunes
     * @return Grupo que está carregado no atributo "grupo"
     * @version 3.0
     * @since 3.0
     */
    public Grupo getGrupo() {
        return grupo;
    }

    /**
     * Define o grupo, que será utilizado por este controller, no atributo "grupo"
     *
     * @author Douglas Antunes
     * @version 3.0
     * @since 3.0
     * @param grupo Grupo que será utilizado por este controller.
     */
    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    /**
     * Retorna uma lista de tuplas com nome do grupo e seu respectivo id, grupos esses, que fazem
     * parte da instituição cujo id foi passado como parâmetro para este método. Essa lista
     * é utilizada pelo componente do jsf <f:selectItens> que exibe uma combo-box com o nome dos
     * grupos da lista.
     *
     * @author Douglas Antunes
     * @return Lista de tuplas com o nome de todos os grupos e seus respectivos id, da instituição cujo id é passado como
     * parâmetro deste método.
     * @version 3.0
     * @since 3.0
     * @param idInstituicao id da instituição da qual terá todos os seus grupos recuperados.
     */
    public TreeMap<String, String> getTreeMap(int idInstituicao) throws SQLException {
        grupoDao.setConexao(Conexao.getConnection());
        TreeMap<String, String> mapa = new TreeMap<String, String>();
        ArrayList<Grupo> grupos = grupoDao.listarTodos(idInstituicao);
        grupoDao.getConexao().close();
        for (int i = 0; i < grupos.size(); i++) {
            Grupo g = grupos.get(i);
            if (g.isAtivado()) {
                String nome = g.getSigla();
                mapa.put(nome, String.valueOf(g.getId()));
            }
        }
        return mapa;
    }

    /**
     * Retorna o motivo especificado pelo cla para a desativação de um grupo.
     *
     * @author Douglas Antunes
     * @return Motivo de desativação de um grupo.
     * @version 3.0
     * @since 3.0
     */
    public String getMotivo() {
        return motivo;
    }

    /**
     * Define o motivo dado pelo cla para a desativação de um grupo.
     *
     * @author Douglas Antunes
     * @version 3.0
     * @since 3.0
     * @param motivo motivo da desativação de um grupo.
     */
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    /**
     * Retorna o curso que está sendo utilizada por este controller
     *
     * @author Douglas Antunes
     * @return curso curso que será utilizado por este controller.
     * @version 3.0
     * @since 3.0
     */
    public Curso getCurso() {
        return curso;
    }

    /**
     * Incrementa o atributo que controla a etapa atual de criação ou edição de um grupo pelo cla.
     * São 3 etapas: criação do grupo, cadastramento do curso e por fim, cadastramento do tutor.
     * No caso de criação de um grupo SECAD, o passo 2, de criação de um curso, não está presente.
     *
     * @author Douglas Antunes
     * @version 3.0
     * @since 3.0
     */
    public void proximoPasso() {
        if (grupo.getTipo() == Grupo.SECAD && getPasso() == 1) {
            passo = 3;
        } else {
            ++passo;
        }
    }

    /**
     * Decrementa o atributo que controla a etapa atual de criação ou edição de um grupo pelo cla.
     * São 3 etapas: criação do grupo, cadastramento do curso e por fim, cadastramento do tutor.
     * No caso de criação de um grupo SECAD, o passo 2, de criação de um curso, não está presente.
     *
     * @author Douglas Antunes
     * @version 3.0
     * @since 3.0
     */
    public void passoAnterior() {
        if (grupo.getTipo() == Grupo.SECAD && getPasso() == 3) {
            passo = 1;
        } else {
            --passo;
        }
    }

    /**
     * 
     * @author Douglas Antunes
     * @return passo da criação ou edição de um grupo, que pode ser 1, 2 ou 3.
     * @version 3.0
     * @since 3.0
     */
    public int getPasso() {
        return passo;
    }

    /**
     *
     * @author Douglas Antunes
     * @return tutor que está sendo utilizado por este controller.
     * @version 3.0
     * @since 3.0
     */
    public Tutor getTutor() {
        return tutor;
    }

    /**
     * Define o tutor no atributo "tutor" que será utilizado por este controller
     *
     * @author Douglas Antunes
     * @version 3.0
     * @since 3.0
     * @param tutor tutor que será utilizado por este controller
     */
    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    /**
     *
     * @author Douglas Antunes
     * @return id de um grupo que será utilizado para carregar um grupo no atributo "grupo"
     * @version 3.0
     * @since 3.0
     */
    public String getId() {
        return id;
    }

    /**
     * Define o id de um grupo, que posteriormente será utilizado para carregar um grupo no atributo "grupo"
     *
     * @author Douglas Antunes
     * @version 3.0
     * @since 3.0
     * @param id id de um grupo escolhido pelo cla.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retorna a lista dos grupos que foram selecionados pelo cla, para a busca de atividades.
     *
     * @author Douglas Antunes
     * @return lista dos grupos selecionados pelo cla, para a busca de atividades.
     * @version 3.0
     * @since 3.0
     */
    public Grupo[] getGruposSelecionados() {
        return gruposSelecionados;
    }

    /**
     * Retorna a lista dos grupos selecionados pelo cla, para a busca de atividades.
     *
     * @author Douglas Antunes
     * @version 3.0
     * @since 3.0
     * @param gruposSelecionados lista dos grupos selecionados pelo cla, para a busca de atividades.
     */
    public void setGruposSelecionados(Grupo[] gruposSelecionados) {
        this.gruposSelecionados = gruposSelecionados;
    }

    /**
     * Retorna a lista dos e-mails de tutores selecionados pelo cla para o envio de uma mensagem.
     *
     * @author Douglas Antunes
     * @return lista dos e-mails de tutores selecionados pelo cla para o envio de uma mensagem.
     * @version 3.0
     * @since 3.0
     */
    public ArrayList<String> getEmailsSelecionados() {
        ArrayList<String> emails = new ArrayList<String>();
        for (int i = 0; i < gruposSelecionados.length; i++) {
            emails.add(gruposSelecionados[i].getTutor().getEmail());
        }
        return emails;
    }

    /**
     * Verifica se a sigla do grupo passada pelo cla na inserção ou na edição de um grupo é única na instituição
     * em questão. Caso não seja, o método lança uma exceção que impede a inserção no banco de dados desta sigla e
     * ainda notifica o usuário da repetição da mesma na instituição.
     *
     * @author Pedro Augusto
     * @throws SQLException
     * @version 3.0
     * @since 3.0
     */
    public void validarGrupo(FacesContext context, UIComponent component, Object value) throws SQLException {
        String sigla = (String) value;
        Connection conexao = Conexao.getConnection();
        conexao.setAutoCommit(false);
        grupoDao.setConexao(conexao);
        if (grupo.getId() == 0) {
            if (grupoDao.recuperaPorSigla(sigla, LoginController.getInstituicao().getId()) == null) {
                conexao.close();
                return;
            } else {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro. O grupo " + sigla + " já existe nesta instituição.", null));
            }
        } else {
            Grupo g = new Grupo();
            g = grupoDao.recuperaPorSigla(sigla, LoginController.getInstituicao().getId());
            conexao.setAutoCommit(true);
            conexao.close();
            if (g == null) {
                return;
            } else if (grupo.getId() == g.getId() || g.getId() == 0) {
                return;
            } else {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro. O grupo " + sigla + " já existe nesta instituição.", null));
            }
        }
    }

    /**
     * Verifica se o nome do curso passado pelo cla na inserção ou na edição de um curso é única na instituição
     * em questão. Caso não seja, o método lança uma exceção que impede a inserção no banco de dados deste nome e
     * ainda notifica o usuário da repetição da mesma na instituição.
     *
     * @author Pedro Augusto
     * @throws SQLException
     * @version 3.0
     * @since 3.0
     */
    public void validarCurso(FacesContext context, UIComponent component, Object value) throws SQLException {
        String sigla = (String) value;
        Connection conexao = Conexao.getConnection();
        cursoDao.setConexao(conexao);
        if (curso.getId() == 0) {
            if (cursoDao.recuperarPorNome(sigla, LoginController.getInstituicao().getId()) == null) {
                conexao.close();
                return;
            } else {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro. O curso " + sigla + " já existe nesta instituição.", null));
            }
        } else {
            Curso c = new Curso();
            System.out.println("Sigla: " + sigla);
            c = cursoDao.recuperarPorNome(sigla, LoginController.getInstituicao().getId());
            conexao.close();
            if (c == null) {
                return;
            } else if (curso.getId() == c.getId() || c.getId() == 0) {
                return;
            } else {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro. O curso " + sigla + " já existe nesta instituição.", null));
            }
        }
    }
}
