package br.com.jops.cci;

import br.com.jops.cgt.AplJops;
import br.com.jops.cgd.Filtro;
import br.com.jops.cdp.Voto;
import br.com.jops.cdp.Noticia;
import br.com.jops.cdp.Usuario;
import br.com.jops.cdp.Comentario;
import br.com.jops.cih.paginal.NoticiaPaginalTag;
import br.com.jops.cih.iterator.NoticiaIteratorTag;
import br.com.jops.cih.iterator.MensagemIteratorTag;
import br.com.jops.cih.form.NoticiaLeituraFormTag;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import java.io.IOException;
import java.util.Collection;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Welton
 * Date: 17/08/2007
 * Time: 19:41:33
 * Este servlet implementa o controlador
 * MVC sendo responsavel pelos mapeamentos
 * entre vis�o e modelo
 */
public class CtrlJopsServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(CtrlJopsServlet.class);

    /**
     * Tamanho da pagina de busca
     */
    private static final String TAM_PAGINA_BUSCA_INI_PARM = "tam-pagina-busca";

    /**
     * Integrador
     */
    private static final String INTEGRADOR_INI_PARM = "integrador";

    /**
     * Nomes das requisi��es suportadas pela ferramenta
     */
    public static final String REQ_PESQUISAR_NOTICIAS = "pesquisarNoticias.jops";

    public static final String REQ_LER_NOTICIA = "lerNoticia.jops";

    public static final String REQ_CRIAR_NOTICIA = "criarNoticia.jops";

    public static final String REQ_EDITAR_NOTICIA = "editarNoticia.jops";

    public static final String REQ_SALVAR_NOTICIA = "salvarNoticia.jops";

    public static final String REQ_CANCELAR_NOTICIA = "cancelarNoticia.jops";

    public static final String REQ_EXCLUIR_NOTICIA = "excluirNoticia.jops";

    public static final String REQ_SALVAR_COMENTARIO = "salvarComentario.jops";

    public static final String REQ_EXCLUIR_COMENTARIO = "excluirComentario.jops";

    public static final String REQ_VOTAR = "votar.jops";

    /**
     * Referencia para aplica��o Jops
     */
    static final AplJops JOPS = new AplJops();

    public void init(ServletConfig config) throws ServletException {
        String integradorClasse = config.getInitParameter(INTEGRADOR_INI_PARM);
        String paginaBusca = config.getInitParameter(TAM_PAGINA_BUSCA_INI_PARM);
        if (integradorClasse == null) {
            String msg = "Imposs�vel iniciar Jops. Integrador n�o especificado.";
            logger.fatal(msg);
            throw new ServletException(msg);
        }
        if (paginaBusca != null) {
            int tamPaginaBusca = Integer.parseInt(paginaBusca);
            if (tamPaginaBusca > 0) Configuracao.setTamPaginaBusca(tamPaginaBusca);
        }
        try {
            Class classe = Class.forName(integradorClasse);
            Configuracao.setIntegrador((Integrador) classe.newInstance());
        } catch (Throwable t) {
            String msg = "Imposs�vel iniciar Jops. Integrador inv�lido: " + integradorClasse;
            logger.fatal(msg);
            throw new ServletException(msg);
        }
    }

    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getRequestURI();
        path = path.substring(path.lastIndexOf('/') + 1);
        if (path.equals(REQ_PESQUISAR_NOTICIAS)) pesquisarNoticias(req, resp); else if (path.equals(REQ_LER_NOTICIA)) lerNoticia(req, resp); else if (path.equals(REQ_CRIAR_NOTICIA)) criarNoticia(req, resp); else if (path.equals(REQ_EDITAR_NOTICIA)) editarNoticia(req, resp); else if (path.equals(REQ_CANCELAR_NOTICIA)) cancelarNoticia(req, resp); else if (path.equals(REQ_SALVAR_NOTICIA)) salvarNoticia(req, resp); else if (path.equals(REQ_EXCLUIR_NOTICIA)) excluirNoticia(req, resp); else if (path.equals(REQ_SALVAR_COMENTARIO)) salvarComentario(req, resp); else if (path.equals(REQ_EXCLUIR_COMENTARIO)) excluirComentario(req, resp); else if (path.equals(REQ_VOTAR)) votar(req, resp);
    }

    private void pesquisarNoticias(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        NoticiaPesquisaForm form = new NoticiaPesquisaForm();
        form.mapear(req);
        String usrId = Configuracao.obterIntegrador().obterIdUsuarioLogado(req);
        Filtro filtro = (form.getFiltro() == null || form.getFiltro().isEmpty()) ? Filtro.RECENTES : Filtro.obterPorCodigo(form.getFiltro());
        int offset = 0;
        if (form.getOffset() != null) offset = Integer.parseInt(form.getOffset());
        int tamanho = JOPS.obterTamanhoPesquisaNoticia(form.getTitulo(), form.getTags(), form.getAutor(), form.getCategoria(), usrId, filtro);
        Collection<Noticia> resultado = JOPS.pesquisarNoticia(form.getTitulo(), form.getTags(), form.getAutor(), form.getCategoria(), usrId, filtro, offset, Configuracao.obterTamPaginaBusca());
        req.setAttribute(NoticiaIteratorTag.NOTICIAS_REQ_ATTR, resultado);
        req.setAttribute(NoticiaPaginalTag.TAMANHO_PESQUISA_REQ_ATTR, tamanho);
        Forward.setForward(req, Forward.LISTAGEM_FORWARD);
        String hospedeiro = Forward.obterHospedeiro(req);
        req.getRequestDispatcher(hospedeiro).forward(req, resp);
    }

    private void criarNoticia(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        NoticiaForm form = NoticiaForm.obterForm(req);
        if (form != null) form.liberarForm(req);
        Forward.setForward(req, Forward.EDICAO_FORWARD);
        String hospedeiro = Forward.obterHospedeiro(req);
        req.getRequestDispatcher(hospedeiro).forward(req, resp);
    }

    private void cancelarNoticia(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        NoticiaForm form = NoticiaForm.obterForm(req);
        if (form != null) form.liberarForm(req);
        pesquisarNoticias(req, resp);
    }

    private void lerNoticia(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String noticiaId = req.getParameter(NoticiaForm.ID_REQ_PARM);
        Noticia noticia = JOPS.obterNoticiaPorId(noticiaId, Configuracao.obterIntegrador().obterIdUsuarioLogado(req));
        req.setAttribute(NoticiaLeituraFormTag.NOTICIA_REQ_ATTR, noticia);
        Forward.setForward(req, Forward.LEITURA_FORWARD);
        String hospedeiro = Forward.obterHospedeiro(req);
        req.getRequestDispatcher(hospedeiro).forward(req, resp);
    }

    private void editarNoticia(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String noticiaId = req.getParameter(NoticiaForm.ID_REQ_PARM);
        Noticia noticia = JOPS.obterNoticiaPorId(noticiaId, Configuracao.obterIntegrador().obterIdUsuarioLogado(req));
        NoticiaForm form = new NoticiaForm();
        form.popular(req);
        form.mapear(noticia);
        form.armazenaForm(req);
        Forward.setForward(req, Forward.EDICAO_FORWARD);
        String hospedeiro = Forward.obterHospedeiro(req);
        req.getRequestDispatcher(hospedeiro).forward(req, resp);
    }

    private void salvarNoticia(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        NoticiaForm form = NoticiaForm.obterForm(req);
        form.mapear(req);
        Collection<String> erros = form.validar();
        if (erros != null) {
            req.setAttribute(MensagemIteratorTag.MENSAGENS_REQ_ATTR, erros);
            Forward.setForward(req, Forward.EDICAO_FORWARD);
            String hospedeiro = Forward.obterHospedeiro(req);
            req.getRequestDispatcher(hospedeiro).forward(req, resp);
        } else {
            Noticia noticia = form.obterEntidade();
            Usuario usuario = new Usuario();
            usuario.setId(Configuracao.obterIntegrador().obterIdUsuarioLogado(req));
            noticia.setUsuario(usuario);
            String noticiaId = JOPS.publicarNoticia(noticia);
            form.liberarForm(req);
            noticia = JOPS.obterNoticiaPorId(noticiaId, Configuracao.obterIntegrador().obterIdUsuarioLogado(req));
            req.setAttribute(NoticiaLeituraFormTag.NOTICIA_REQ_ATTR, noticia);
            Forward.setForward(req, Forward.LEITURA_FORWARD);
            String hospedeiro = Forward.obterHospedeiro(req);
            req.getRequestDispatcher(hospedeiro).forward(req, resp);
        }
    }

    private void excluirNoticia(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String noticiaId = req.getParameter(NoticiaForm.ID_REQ_PARM);
        Noticia noticia = new Noticia();
        noticia.setId(noticiaId);
        JOPS.excluirNoticia(noticia);
        pesquisarNoticias(req, resp);
    }

    private void salvarComentario(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ComentarioForm form = new ComentarioForm();
        form.mapear(req);
        Collection<String> erros = form.validar();
        if (erros != null) {
            req.setAttribute(MensagemIteratorTag.MENSAGENS_REQ_ATTR, erros);
            form.armazenaForm(req);
        } else {
            Comentario comentario = form.obterEntidade();
            Usuario usuario = new Usuario();
            usuario.setId(Configuracao.obterIntegrador().obterIdUsuarioLogado(req));
            comentario.setUsuario(usuario);
            JOPS.publicarComentario(comentario);
            form.liberarForm(req);
        }
        lerNoticia(req, resp);
    }

    private void excluirComentario(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String comentarioId = req.getParameter(ComentarioForm.ID_REQ_PARM);
        Comentario comentario = new Comentario();
        comentario.setId(comentarioId);
        JOPS.excluirComentario(comentario);
        lerNoticia(req, resp);
    }

    /**
     * Computa um voto para uma noticia.
     * A opera��o de voto � realizada
     * assincronamente por meio de AJAX.
     * O retorno desta opera��o � o painel
     * de votos com o novo n�mero de votos
     * da not�cia ap�s o novo voto ter sido
     * computado.
     * @param req http request
     * @param resp http response
     * @throws ServletException caso ocorra erro
     * @throws IOException caso ocorra erro
     */
    private void votar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String noticiaId = req.getParameter(NoticiaForm.ID_REQ_PARM);
        String usrId = Configuracao.obterIntegrador().obterIdUsuarioLogado(req);
        Voto voto = new Voto();
        voto.setNoticiaid(noticiaId);
        voto.setUsuarioid(usrId);
        JOPS.votar(voto);
        int nvotos = JOPS.obterNumeroVotos(noticiaId);
        Noticia noticia = new Noticia();
        noticia.setNvotos(nvotos);
        noticia.setVotada(true);
        req.setAttribute(NoticiaLeituraFormTag.NOTICIA_REQ_ATTR, noticia);
        req.getRequestDispatcher("/jops/jsp/voto.jsp").forward(req, resp);
    }
}
