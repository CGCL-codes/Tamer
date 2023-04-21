package org.larOzanam.arquitetura.relatorio;

import com.celiosilva.swingDK.frames.InternalFrame;
import java.beans.XMLDecoder;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.larOzanam.arquitetura.core.Sessao;
import org.larOzanam.arquitetura.dao.Parametro;
import org.larOzanam.arquitetura.excecoes.ExcecaoNegocio;
import org.larOzanam.arquitetura.excecoes.ExcecaoSistema;
import org.larOzanam.arquitetura.excecoes.GerenciadorExcecao;
import org.larOzanam.arquitetura.logging.GerenciadorLog;
import org.larOzanam.arquitetura.util.Funcoes;
import org.larOzanam.view.messangers.InputMessanger;

/**
 *
 * @author celio@celiosilva.com
 */
public class GerenciadorRelatorio {

    private static EstruturaRelatorio estruturaRelatorio;

    private static final String ARQ_ESTRUTURA_RELATORIO = "/META-INF/estruturaRelatorio.xml";

    public static final ExcecaoNegocio DADOS_INEXISTENTES = new ExcecaoNegocio("N�o h� informa��es para criar o relat�rio");

    public static final String CAMINHO_RELATORIOS = "/org/larOzanam/relatorios/";

    public static final String EXTENSAO_RELATORIO = ".jasper";

    public static final String NOME_ENTIDADE = "NOME_ENT";

    public static final String DESC_ENTIDADE = "DESC_ENT";

    public static final String LOGO_ENTIDADE = "LOGO_ENT";

    public static final String INFO_ENTIDADE = "INFO_ENT";

    public static final String LOGO_ALTERNATIVO = "LOGO_ALTERNATIVO";

    public static final String INFO_ALTERNATIVO = "INFO_ALTERNATIVO";

    public static final String NOME_RELATORIO = "NOME_REL";

    public static final String DATA_RELATORIO = "DATA_REL";

    public static final String HORA_RELATORIO = "HORA_REL";

    public static final String DESC_RELATORIO = "DESC_RELATORIO";

    private static final GerenciadorLog logger = GerenciadorLog.getInstance(GerenciadorRelatorio.class);

    /** Creates a new instance of GerenciadorRelatorio */
    private GerenciadorRelatorio() {
    }

    private static EstruturaRelatorio getEstruturaRelatorio() {
        logger.debug("getEstruturaRelatorio >>");
        if (estruturaRelatorio == null) {
            logger.info("Carregando Estrutura B�sica do Relat�rio...");
            carregarEstruturaRelatorio();
        }
        logger.debug("getEstruturaRelatorio << " + estruturaRelatorio);
        return estruturaRelatorio;
    }

    private static void carregarEstruturaRelatorio() {
        InputStream inputStream = new GerenciadorRelatorio().getClass().getResourceAsStream(ARQ_ESTRUTURA_RELATORIO);
        if (inputStream == null) throw new NullPointerException("O arquivo de estrutura b�sica de relat�rio � inv�lido\n" + "Verifique se o arquivo " + ARQ_ESTRUTURA_RELATORIO + " foi criado corretamente...");
        XMLDecoder decoder = new XMLDecoder(inputStream);
        estruturaRelatorio = (EstruturaRelatorio) decoder.readObject();
        if (estruturaRelatorio.getLogoEntidade().length() == 0) estruturaRelatorio.setLogoEntidade(null);
        if (estruturaRelatorio.getLogoAlternativo().length() == 0) estruturaRelatorio.setLogoAlternativo(null);
    }

    private static Parametro criarEstruturaRelatorio() {
        logger.debug("criarEstruturaRelatorio >>");
        EstruturaRelatorio es = GerenciadorRelatorio.getEstruturaRelatorio();
        Parametro p = new Parametro();
        Date dataAtual = Sessao.getInstance().getDataBancoDeDados();
        p.put(NOME_ENTIDADE, es.getNomeEntidade());
        p.put(DESC_ENTIDADE, es.getDescricaoEntidade());
        p.put(LOGO_ENTIDADE, es.getLogoEntidade());
        p.put(INFO_ENTIDADE, es.getInfoEntidade());
        p.put(LOGO_ALTERNATIVO, es.getLogoAlternativo());
        p.put(INFO_ALTERNATIVO, es.getInfoAlternativo());
        p.put(DATA_RELATORIO, Funcoes.formatarDataDiaMesAno(dataAtual));
        p.put(HORA_RELATORIO, Funcoes.formatarHorario(dataAtual));
        logger.debug("criarEstruturaRelatorio <<");
        return p;
    }

    public static void criarRelatorio(Relatorio relatorio, Collection dados, InternalFrame controller) throws ExcecaoSistema, ExcecaoNegocio {
        InputMessanger input = new InputMessanger(controller).setTitle("Descri��o/Fonte do Relat�rio").setMessage("Digite uma descri��o da consulta para ser anexada ao relat�rio").showMessage();
        criarRelatorio(relatorio, dados, "" + input.getValued());
    }

    public static void criarRelatorio(Relatorio relatorio, Collection dados, String descricaoRelatorio) throws ExcecaoSistema, ExcecaoNegocio {
        Parametro p = new Parametro();
        if (descricaoRelatorio != null && descricaoRelatorio.length() > 0) p.put(DESC_RELATORIO, descricaoRelatorio);
        relatorio.setParametros(p);
        criarRelatorio(relatorio, dados);
    }

    public static void criarRelatorio(Relatorio relatorio, Collection dados) throws ExcecaoSistema, ExcecaoNegocio {
        logger.debug("criarRelatorio >>");
        logger.info("Verificando dados do relat�rio...");
        if (dados == null || dados.size() == 0) {
            logger.fatal("N�o h� informa��es suficiente para criar o relat�rio", DADOS_INEXISTENTES);
            throw DADOS_INEXISTENTES;
        }
        JRDataSource dataSource = new JRBeanCollectionDataSource(dados);
        String nomeArquivoRelatorio = relatorio.getNomeArquivoRelatorio().replace(EXTENSAO_RELATORIO, "");
        String caminhoRelatorio = CAMINHO_RELATORIOS + nomeArquivoRelatorio + EXTENSAO_RELATORIO;
        logger.info("Procando arquivo fonte do relat�rio em " + caminhoRelatorio + "...");
        InputStream reportStream = new GerenciadorRelatorio().getClass().getResourceAsStream(caminhoRelatorio);
        if (reportStream == null) {
            NullPointerException ex = new NullPointerException("O relat�rio com o nome " + caminhoRelatorio + " n�o pode ser encontrado...");
            logger.fatal("Verifique se o relat�rio que est� informando com o nome " + caminhoRelatorio + " est� correto e no caminho indicado...", ex);
            throw ex;
        }
        JasperPrint jp = null;
        Parametro p = criarEstruturaRelatorio();
        relatorio.setParametros(p);
        logger.info("Criando relat�rio com as informa��es abaixo\n " + relatorio);
        try {
            logger.info("Adicionando os dados ao relat�rio...");
            jp = JasperFillManager.fillReport(reportStream, relatorio.getParametros(), dataSource);
            logger.info("Relat�rio criado com sucesso!");
            logger.info("Invocando Visualizador de Relat�rios...");
            new VisualizadorRelatorio(jp);
        } catch (JRException ex) {
            logger.fatal("Erro enquanto carregando as informa��es do relat�rio", ex);
            GerenciadorExcecao.tratarExcecao(ex, relatorio);
        }
        logger.debug("criarRelatorio <<");
    }
}
