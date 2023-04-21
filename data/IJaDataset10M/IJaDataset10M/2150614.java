package org.larOzanam.arquitetura.excecoes;

import com.celiosilva.swingDK.dataAbstractFields.SwingDKConstants;
import com.celiosilva.swingDK.exceptions.SwingDKException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.EntityExistsException;
import javax.persistence.Query;
import javax.persistence.RollbackException;
import oracle.toplink.essentials.exceptions.DatabaseException;
import org.larOzanam.arquitetura.core.Dicionario;
import org.larOzanam.arquitetura.core.Sessao;
import org.larOzanam.arquitetura.logging.GerenciadorLog;

/**
 *
 * @author Administrador
 */
public class GerenciadorExcecao extends SwingDKException {

    private static Sessao sessao = null;

    private static Dicionario dicionario = null;

    public static final int USUARIO_SENHA_INVALIDO = 1017;

    public static final int SERVIDOR_INIC_FINALIZADO = 1033;

    public static final int RESTRICAO_EXCLUSIVA_VIOLADA = 1;

    public static final int RESTRICAO_INTEGRIDADE_VIOLADA = 2292;

    public static final int RESTRICAO_VALOR_NAO_NULO = 1400;

    public static final int RESTRICAO_TAMANHO_NAO_PERMITIDO = 12899;

    public static final int SERVICO_INEXISTENTE_BANCO_DADOS = 0;

    public static final int ENCERRAMENTO_IMEDIATO_SISTEMA = 1089;

    public static final int CONEXAO_ABORTADA = 17002;

    public static final int SERVIDOR_ESTADO_INCONSISTENTE = 17447;

    public static final int SOCKET_FINALIZADO = 17410;

    public static final ExcecaoSistema LOGIN_INVALIDO = new ExcecaoSistema(null, "A senha fornecida para este usu�rio � inv�lida", "Erro de Login", TipoErroSistema.RESTRICAO_SISTEMA);

    public static final ExcecaoSistema SENHA_INVALIDA = new ExcecaoSistema(null, "O login fornecido para este usu�rio � inv�lido", "Erro de Login", TipoErroSistema.RESTRICAO_SISTEMA);

    private static final GerenciadorLog logger = GerenciadorLog.getInstance(GerenciadorExcecao.class);

    public GerenciadorExcecao() {
        sessao = Sessao.getInstance();
    }

    public static void tratarExcecao(Exception excecao, Object alvo) throws ExcecaoSistema {
        logger.fatal("\n\n[+]\nERRO LAN�ADO -----------\n " + excecao + "\n[-]\n");
        logger.fatal("\n\n[+]\nEXCECAO INTERNA LAN�ADA -----------\n " + excecao.getCause() + "\n[-]\n");
        if (excecao instanceof DatabaseException) {
            DatabaseException dex = (DatabaseException) excecao;
            switch(dex.getDatabaseErrorCode()) {
                case USUARIO_SENHA_INVALIDO:
                    tratarUsuarioSenhaInvalido(dex, alvo);
                    break;
                case SERVIDOR_INIC_FINALIZADO:
                    tratarServidorInicializandoFinalizando(dex, alvo);
                    break;
                case ENCERRAMENTO_IMEDIATO_SISTEMA:
                    tratarEncerramentoImediatoSistema(dex, alvo);
                    break;
                case CONEXAO_ABORTADA:
                    tratarConexaoAbortada(dex, alvo);
                    break;
                case SERVIDOR_ESTADO_INCONSISTENTE:
                    tratarServidorEstadoInconsistente(dex, alvo);
                    break;
                case SOCKET_FINALIZADO:
                    tratarSocketFinalizado(dex, alvo);
                    break;
                case SERVICO_INEXISTENTE_BANCO_DADOS:
                    tratarServicoInexistenteBancoDados(dex, alvo);
                    break;
            }
        }
        sessao = Sessao.getInstance();
        dicionario = sessao.getDicionario();
        if (excecao instanceof DatabaseException) {
            tratarExcecao((DatabaseException) excecao, alvo);
        } else if (excecao instanceof IllegalArgumentException) {
            tratarExcecao((IllegalArgumentException) excecao, alvo);
        } else if (excecao instanceof EntityExistsException) {
            tratarExcecao((EntityExistsException) excecao, alvo);
        } else if (excecao instanceof RollbackException && excecao.getCause() instanceof DatabaseException) {
            tratarExcecao((DatabaseException) excecao.getCause(), alvo);
        } else if (excecao instanceof ExcecaoSistema) {
            throw (ExcecaoSistema) excecao;
        }
        ExcecaoSistema ex = new ExcecaoSistema(excecao, "Houve um erro inesperado pelo sistema" + "\nFavor reportar essa mensagem imediatamente ao seu superior " + "\nou ao administrador para que isso possa ser corrigido", "Erro Inesperado", TipoErroSistema.ERRO_SISTEMA);
        throw ex;
    }

    private static void tratarExcecao(DatabaseException ex, Object alvo) throws ExcecaoSistema {
        switch(ex.getDatabaseErrorCode()) {
            case RESTRICAO_EXCLUSIVA_VIOLADA:
            case RESTRICAO_INTEGRIDADE_VIOLADA:
                tratarRestricaoExclusivaViolada(ex, alvo);
                break;
            case RESTRICAO_VALOR_NAO_NULO:
                tratarRestricaoPreenchimentoObrigatorio(ex, alvo);
                break;
            case RESTRICAO_TAMANHO_NAO_PERMITIDO:
                tratarRestricaoTamanhoNaoPermitido(ex, alvo);
                break;
        }
    }

    private static void tratarRestricaoExclusivaViolada(DatabaseException ex, Object alvo) throws ExcecaoSistema {
        logger.debug("tratarRestricaoExclusivaViolada = " + ex);
        String s = ex.getInternalException().getLocalizedMessage();
        String nomeRestricao = s.split("[\\(\\)]")[1];
        Restricao r = consultarRestricao(nomeRestricao);
        String mensagem = "";
        String titulo = "";
        switch(r.getTipoRestricao()) {
            case CHAVE_UNICA:
                mensagem = "J� existe um registro de " + dicionario.traduzirEntidade(r.getNomeTabela()) + " " + "com o(a) mesmo(a) " + dicionario.traduzirCampo(r.getNomeColuna(), r.getNomeTabela()) + " informado(a)." + "\nVerifique se o valor para o campo " + dicionario.traduzirCampo(r.getNomeColuna(), r.getNomeTabela()) + " est� correto e altere-o para poder prosseguir.";
                titulo = "Valor Informado J� Existente";
                break;
            case CHAVE_PRIMARIA:
                mensagem = "J� existe um registro de " + dicionario.traduzirEntidade(r.getNomeTabela()) + " " + "com o mesmo(a) " + dicionario.traduzirCampo(r.getNomeColuna(), r.getNomeTabela()) + " informado(a)." + "\nVerifique se o valor para o campo " + dicionario.traduzirCampo(r.getNomeColuna(), r.getNomeTabela()) + " est� correto e altere-o para poder prosseguir.";
                titulo = "Valor Informado J� Existente";
                break;
            case CHAVE_ESTRANGEIRA:
                mensagem = "N�o � poss�vel remover este registro de " + dicionario.traduzirEntidade(alvo.getClass()) + " pois existem registros de " + dicionario.traduzirEntidade(r.getNomeTabela()) + " utilizando-o.";
                titulo = "Registro N�o Pode Ser Removido";
                break;
        }
        ExcecaoSistema es = new ExcecaoSistema(ex, mensagem, titulo, TipoErroSistema.RESTRICAO_SISTEMA);
        throw es;
    }

    private static void tratarExcecao(IllegalArgumentException ex, Object alvo) throws ExcecaoSistema {
        String nomeEntidade = sessao.getDicionario().traduzirEntidade((Class<?>) alvo);
        String msg = "N�o � poss�vel inserir uma inst�ncia de " + nomeEntidade + " cujo valor � nulo.";
        ExcecaoSistema es = new ExcecaoSistema(ex, msg, "Valor inv�lido", TipoErroSistema.RESTRICAO_SISTEMA);
        throw es;
    }

    private static void tratarExcecao(EntityExistsException ex, Object alvo) throws ExcecaoSistema {
        String nomeEntidade = sessao.getDicionario().traduzirEntidade((Class<?>) alvo);
        String msg = "Este registro de " + nomeEntidade + " j� existe no sistema";
        ExcecaoSistema es = new ExcecaoSistema(ex, msg, "Registro Duplicado", TipoErroSistema.RESTRICAO_SISTEMA);
        throw es;
    }

    private static void tratarUsuarioSenhaInvalido(DatabaseException ex, Object alvo) throws ExcecaoSistema {
        throw new ExcecaoSistema(ex, "A configura��o do sistema foi alterada e/ou est� incorreta," + "portanto n�o foi poss�vel conectar-se ao servidor\n " + "O usu�rio e a senha de configura��o do sistema est�o incorretos\n" + "Contate o administrador para resolver este problema", "Erro Configura��o do Sistema", TipoErroSistema.RESTRICAO_SISTEMA);
    }

    private static Restricao consultarRestricao(String nomeRestricao) {
        logger.debug("consultarRestricao >>");
        String queryString = "SELECT UC.CONSTRAINT_NAME, UC.CONSTRAINT_TYPE, UC.TABLE_NAME, UCC.COLUMN_NAME " + "FROM USER_CONSTRAINTS UC, USER_CONS_COLUMNS UCC " + "WHERE UCC.CONSTRAINT_NAME = UC.CONSTRAINT_NAME " + "AND (UC.OWNER || '.' || UCC.CONSTRAINT_NAME) = ?";
        Query query = sessao.getEntityManager().createNativeQuery(queryString);
        query.setParameter(1, nomeRestricao);
        List resultado = (List) query.getSingleResult();
        Restricao r = new Restricao();
        r.setNomeRestricao((String) resultado.get(0));
        r.setTipoRestricao(TipoRestricao.getTipoRestricao((String) resultado.get(1)));
        r.setNomeTabela((String) resultado.get(2));
        r.setNomeColuna((String) resultado.get(3));
        logger.debug("consultarRestricao << " + r);
        return r;
    }

    private static void tratarRestricaoTamanhoNaoPermitido(DatabaseException ex, Object alvo) throws ExcecaoSistema {
        logger.debug("tratarRestricaoTamanhoNaoPermitido >>");
        final int SCHEMA = 0;
        final int TABELA = 1;
        final int CAMPO = 2;
        final int TAMANHO_CAMPO = 0;
        final int TAMANHO_MAX = 1;
        String msg = ex.getInternalException().getLocalizedMessage();
        String[] tamanhos = new String[2];
        String[] campos = new String[3];
        Pattern p = Pattern.compile("\"\\w+\"");
        Matcher m = p.matcher(msg);
        int i = 0;
        while (m.find()) campos[i++] = m.group().replace("\"", "");
        p = Pattern.compile("\\d+");
        m = p.matcher(msg.split("[\\(\\)]")[1]);
        i = 0;
        while (m.find()) tamanhos[i++] = m.group();
        String mensagemFormatada = "O campo " + dicionario.traduzirCampo(campos[CAMPO], campos[TABELA]) + " aceita valores com/at� " + tamanhos[TAMANHO_MAX] + " caracteres" + " e voc� informou um valor com " + tamanhos[TAMANHO_CAMPO] + " caracteres.\n" + "Corrija o valor informado para poder prosseguir.";
        ExcecaoSistema es = new ExcecaoSistema(ex, mensagemFormatada, "Valor Acima do Limite do Campo", TipoErroSistema.RESTRICAO_SISTEMA);
        logger.debug("tratarRestricaoTamanhoNaoPermitido <<");
        throw es;
    }

    private static void tratarRestricaoPreenchimentoObrigatorio(DatabaseException ex, Object alvo) throws ExcecaoSistema {
        logger.debug("tratarRestricaoPreenchimentoObrigatorio >>");
        final int SCHEMA = 0;
        final int TABELA = 1;
        final int CAMPO = 2;
        String msg = ex.getInternalException().getLocalizedMessage();
        String[] campos = new String[3];
        Pattern p = Pattern.compile("\"\\w+\"");
        Matcher m = p.matcher(msg);
        int i = 0;
        while (m.find()) campos[i++] = m.group().replace("\"", "");
        String mensagem = "O campo " + dicionario.traduzirCampo(campos[CAMPO], campos[TABELA]) + " possui preenchimento obrigat�rio.\n" + "N�o � poss�vel prosseguir sem informar o seu valor.";
        ExcecaoSistema es = new ExcecaoSistema(ex, mensagem, "Campo Obrigat�rio N�o Preenchido", TipoErroSistema.RESTRICAO_SISTEMA);
        logger.debug("tratarRestricaoPreenchimentoObrigatorio <<");
        throw es;
    }

    private static void tratarServidorInicializandoFinalizando(DatabaseException ex, Object alvo) throws ExcecaoSistema {
        String mensagem = "O servidor do sistema est� sendo inicializado ou finalizado no momento.\n" + "Tente reiniciar o sistema ap�s alguns minutos\n" + "Contate o administrador para resolver este problema se for necess�rio.";
        throw new ExcecaoSistema(ex, mensagem, "Servidor Inacess�vel", TipoErroSistema.ERRO_SISTEMA);
    }

    private static void tratarServicoInexistenteBancoDados(DatabaseException dex, Object alvo) throws ExcecaoSistema {
        throw new ExcecaoSistema(dex, "O servidor do sistema n�o est� respondendo\n" + "Verifique se este computador est� conectado � rede, se o servidor est� respondendo e tente reiniciar o sistema ap�s alguns minutos\n" + "Contate o administrador para resolver este problema se for necess�rio.", "Servidor N�o Respondendo", TipoErroSistema.ERRO_SISTEMA);
    }

    private static void tratarEncerramentoImediatoSistema(DatabaseException ex, Object alvo) throws ExcecaoSistema {
        throw new ExcecaoSistema(ex, "O servidor do sistema est� sendo finalizado no momento\n" + "Tente reiniciar o sistema ap�s alguns minutos\n" + "Contate o administrador para resolver este problema se for necess�rio.", "Servidor N�o Respondendo", TipoErroSistema.ERRO_SISTEMA);
    }

    private static void tratarConexaoAbortada(DatabaseException ex, Object alvo) throws ExcecaoSistema {
        throw new ExcecaoSistema(ex, "N�o � poss�vel conectar-se ao servidor do sistema\n" + "Verifique se este computador est� conectado � rede e tente reiniciar o sistema ap�s alguns minutos\n" + "Contate o administrador para resolver este problema se for necess�rio.", "Servidor N�o Respondendo", TipoErroSistema.ERRO_SISTEMA);
    }

    private static void tratarServidorEstadoInconsistente(DatabaseException ex, Object alvo) throws ExcecaoSistema {
        throw new ExcecaoSistema(ex, "N�o � poss�vel conectar-se ao servidor do sistema\n" + "Verifique se este computador est� conectado � rede, se o servidor est� respondendo e tente reiniciar o sistema ap�s alguns minutos\n" + "Contate o administrador para resolver este problema se for necess�rio.", "Servidor N�o Respondendo", TipoErroSistema.ERRO_SISTEMA);
    }

    private static int tratarSocketFinalizado(DatabaseException ex, Object alvo) throws ExcecaoSistema {
        throw new ExcecaoSistema(ex, "N�o � poss�vel conectar-se ao servidor do sistema\n" + "Verifique se este computador est� conectado � rede, se o servidor est� respondendo e tente reiniciar o sistema ap�s alguns minutos\n" + "Contate o administrador para resolver este problema se for necess�rio.", "Servidor N�o Respondendo", TipoErroSistema.ERRO_SISTEMA);
    }
}
