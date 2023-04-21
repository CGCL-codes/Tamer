package negocios;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;
import negocios.excecoes.EntradaInvalidaException;
import negocios.excecoes.LocalizacaoException;
import negocios.excecoes.NaoEncontradoException;
import negocios.excecoes.SessaoInexistenteException;

/**
 * Fachada da camada de negocios, a ser acessada pelas demais camadas.
 * 
 * @author Jonathan Brilhante
 * @author Jose Rafael
 * @author Nate Venancio
 * @author Renato Almeida
 * 
 * @version 2.0
 */
public class FacadeNegocios {

    private static FacadeNegocios instance;

    /**
	 * Construtor privado da classe
	 */
    private FacadeNegocios() {
    }

    /**
	 * Retorna a instancia unica da fachada.
	 * 
	 * @return a instancia unica da fachada.
	 */
    public static FacadeNegocios getInstance() {
        if (instance == null) {
            instance = new FacadeNegocios();
        }
        return instance;
    }

    /**
	 * Retorna o Usuario atraves da busca pelo login
	 * 
	 * @param login
	 *            o login do usuario
	 * @return o usuario encontrado na busca
	 * @throws NaoEncontradoException
	 *             caso ele nao seja encontrado
	 */
    public Usuario buscaPorLogin(String login) throws NaoEncontradoException {
        return Sistema.getInstance().buscar("", login, "", "");
    }

    /**
	 * Inicia um chat entre dois usuarios
	 * 
	 * @param login1
	 *            o login do primeiro
	 * @param login2
	 *            o login do segundo
	 * @throws NaoEncontradoException
	 *             caso alguns deles nao exista
	 */
    public void iniciarChat(String login1, String login2) throws NaoEncontradoException {
        Usuario us1 = buscaPorLogin(login1);
        Usuario us2 = buscaPorLogin(login2);
        Sistema.getInstance().iniciarChat(us1, us2);
    }

    /**
	 * Envia uma mensagem de chat para o destinatario
	 * 
	 * @param remetente
	 *            o remetente da mensagem
	 * @param destinatario
	 *            o destinatario da mensagem
	 * @param texto
	 *            a mensagem
	 * 
	 * @throws NaoEncontradoException
	 *             caso um dos usuarios nao seja encontrado
	 */
    public void enviarMensagem(String remetente, String destinatario, String texto) throws NaoEncontradoException {
        Usuario rem = buscaPorLogin(remetente);
        Usuario des = buscaPorLogin(destinatario);
        Sistema.getInstance().enviarMensagem(rem, des, texto);
    }

    /**
	 * Encerra o chat entre os dois usuarios. Caso nao exista, nao faz nada.
	 * Apos encerrado, o chat e guardado no historico do sistema
	 * 
	 * @param login1
	 *            o login do usuario 1
	 * @param login2
	 *            o login do usuario 2
	 * 
	 * @throws IOException
	 *             caso nao seja possivel fazer o log do chat no historico
	 * @throws NaoEncontradoException
	 *             caso um dos usuarios nao exista
	 */
    public void encerrarChat(String login1, String login2) throws IOException, NaoEncontradoException {
        Usuario us1 = buscaPorLogin(login1);
        Usuario us2 = buscaPorLogin(login2);
        Sistema.getInstance().encerrarChat(us1, us2);
    }

    /**
	 * Retorna a ultima mensagem, do remetente para o destinatario
	 * 
	 * @param remetente
	 *            quem enviou a mensagem
	 * @param destinatario
	 *            o destinatario da mensagem
	 * @return a mensagem
	 * 
	 * @throws NaoEncontradoException
	 *             caso um dos usuarios nao exista
	 */
    public String receberMensagem(String remetente, String destinatario) throws NaoEncontradoException {
        Usuario rem = buscaPorLogin(remetente);
        Usuario des = buscaPorLogin(destinatario);
        return Sistema.getInstance().receberMensagem(rem, des);
    }

    /**
	 * Retorna a lista de amigos do usuario passado como parametro
	 * 
	 * @param login
	 *            o login do usuario
	 * @return a lista ordenada de amigos do usuario
	 * @throws SessaoInexistenteException
	 *             caso o usuario nao esteja logado
	 */
    public List<Relacionamento> getAmigos(String login) throws SessaoInexistenteException {
        Usuario us = null;
        try {
            us = buscaPorLogin(login);
            if (us == null || !us.isLogado()) {
                throw new SessaoInexistenteException("Permissao negada.");
            }
        } catch (NaoEncontradoException e) {
            throw new SessaoInexistenteException("Permissao negada.");
        }
        Collections.sort(us.amigos());
        return us.amigos();
    }

    /**
	 * Envia um email para um Usuario, atraves de um remetente
	 * 
	 * @param remetente
	 *            o login do remetente
	 * @param para
	 *            o login do receptor
	 * @param assunto
	 *            o assunto do email
	 * @param mensagem
	 *            a mensagem do email
	 * @throws NaoEncontradoException
	 *             caso algum dos usuarios nao seja encontrado
	 * @throws IOException
	 *             caso nao seja possivel logar a mensagem no sistema
	 */
    public void enviarEmail(String remetente, String para, String assunto, String mensagem) throws NaoEncontradoException, IOException {
        Usuario envio = buscaPorLogin(remetente);
        Usuario buscado = buscaPorLogin(para);
        MensagemTexto email = new MensagemTexto(envio.getEmail(), buscado.getEmail(), assunto, mensagem);
        Sistema.getInstance().enviarEmail(buscado, email);
    }

    /**
	 * Envia um SMS ao telefone passado como parametro
	 * 
	 * @param remetente
	 *            o remetente do SMS
	 * @param para
	 *            o login do destinatario
	 * @param mensagem
	 *            a mensagem do SMS
	 * 
	 * @throws NaoEncontradoException
	 *             caso o telefone nao esteja cadastrado
	 * @throws EntradaInvalidaException
	 *             caso o tamanho da mensagem esteja fora dos padroes
	 * @throws IOException
	 *             caso nao seja possivel acessar o historico do sistema
	 */
    public void enviarSMS(String remetente, String para, String mensagem) throws NaoEncontradoException, EntradaInvalidaException, IOException {
        Usuario rem = buscaPorLogin(remetente);
        Usuario buscado = buscaPorLogin(para);
        SMS sms = new SMS(rem.getNome(), buscado.getTelefone(), mensagem);
        Sistema.getInstance().enviarSMS(buscado, sms);
    }

    /**
	 * Envia um Convite ao email passado como parametro
	 * 
	 * @param remetente
	 *            o remetente do convite
	 * @param email
	 *            o email do receptor
	 * 
	 * @throws NaoEncontradoException
	 *             caso o telefone nao esteja cadastrado
	 * @throws IOException
	 *             caso nao seja possivel acessar o historico do sistema
	 */
    public void enviarConvite(String remetente, String email) throws NaoEncontradoException, IOException {
        Usuario envio = buscaPorLogin(remetente);
        Usuario buscado = null;
        Convite convite = new Convite(envio.getNome(), envio.getEmail(), email);
        try {
            buscado = Sistema.getInstance().buscar("", "", email, "");
        } catch (NaoEncontradoException e) {
        }
        Sistema.getInstance().enviarConvite(buscado, convite);
    }

    /**
	 * Metodo que sera chamado quando houver suporte a web, e o usuario puder
	 * clicar no link de compartilhamento dos convites
	 * 
	 * @param de
	 *            o usuario convidado
	 * @param com
	 *            o segundo que enviou o convite
	 * @param oculto
	 *            o modo de visibilidade do compartilhamento
	 * 
	 * @throws NaoEncontradoException
	 *             caso os usuarios nao existam no sistema
	 * @throws EntradaInvalidaException
	 *             caso o usuario ja seja amigo
	 */
    public void confirmarCompartilhamento(String de, String com, boolean oculto) throws NaoEncontradoException, EntradaInvalidaException {
        Usuario us1 = buscaPorLogin(de);
        Usuario us2 = buscaPorLogin(com);
        Sistema.getInstance().confirmarCompartilhamento(us1, us2, oculto);
    }

    /**
	 * Desloga um usuario do sistema
	 * 
	 * @param login
	 *            o login do usuario a ser deslogado pelo sistem
	 * @throws SessaoInexistenteException
	 *             caso a sessao desse usuario ja tenha expirado, ou ele nao
	 *             exista
	 */
    public void deslogar(String login) throws SessaoInexistenteException {
        Sistema.getInstance().deslogar(login);
    }

    /**
	 * Loga um usuario no sistema, se o login e senha forem compativeis com o
	 * banco de dados. Seta o IP da maquina.
	 * 
	 * @param login
	 *            o login do usuario
	 * @param senha
	 *            a senha do usuario
	 * @return o usuario logado
	 * 
	 * @throws NaoEncontradoException
	 *             caso o login nao exista no banco de dados, ou a senha nao
	 *             seja compativel com o login
	 * @throws LocalizacaoException
	 *             caso o ip passado como parametro nao seja valido
	 * @throws UnknownHostException
	 *             caso nao seja possivel obter o ip da maquina
	 */
    public Usuario logar(String login, String senha) throws NaoEncontradoException, LocalizacaoException, UnknownHostException {
        Localizador localizador = new LocalizadorIP();
        Usuario us = Sistema.getInstance().autenticar(login, senha);
        us.setLocalizador(localizador);
        return us;
    }

    /**
	 * Loga um usuario no sistema, se o login e senha forem compativeis com o
	 * banco de dados. Seta o IP passado como parametro no metodo de
	 * localizacao.
	 * 
	 * @param login
	 *            o login do usuario
	 * @param senha
	 *            a senha do usuario
	 * @param ip
	 *            o ip do usuario
	 * @return o usuario logado
	 * 
	 * @throws NaoEncontradoException
	 *             caso o login nao exista no banco de dados, ou a senha nao
	 *             seja compativel com o login
	 * @throws LocalizacaoException
	 *             caso o ip passado como parametro nao seja valido
	 */
    public Usuario logar(String login, String senha, String ip) throws NaoEncontradoException, IOException, LocalizacaoException {
        Localizador localizador = new LocalizadorIP(ip);
        Usuario us = Sistema.getInstance().autenticar(login, senha);
        if (ip.equals(LocalizadorIP.IP_LOCAL)) {
            localizador = us.getLocalizador();
        }
        us.setLocalizador(localizador);
        return us;
    }

    /**
	 * Loga um usuario no sistema, se o login e senha forem compativeis com o
	 * banco de dados. Seta a latitude e a longitude no metodo de localizacao
	 * manual.
	 * 
	 * @param login
	 *            o login do usuario
	 * @param senha
	 *            a senha do usuario
	 * @param latitude
	 *            a latitude do usuario, informada manualmente
	 * @param longitude
	 *            a longitude do usuario, informada manualmente
	 * @return o usuario logado
	 * 
	 * @throws NaoEncontradoException
	 *             caso o login nao exista no banco de dados, ou a senha nao
	 *             seja compativel com o login
	 * @throws LocalizacaoException
	 *             caso o ip passado como parametro nao seja valido
	 */
    public Usuario logar(String login, String senha, double latitude, double longitude) throws NaoEncontradoException, IOException, LocalizacaoException {
        Localizador localizador = new LocalizadorManual(latitude, longitude);
        Usuario us = Sistema.getInstance().autenticar(login, senha);
        us.setLocalizador(localizador);
        return us;
    }

    /**
	 * Cria um novo UsuarioComum a partir dos dados passados como parametro.
	 * 
	 * @param login
	 *            o login do usuario
	 * @param nome
	 *            o nome do usuario
	 * @param email
	 *            o email do usuario
	 * @param senha
	 *            a senha do usuario
	 * @param telefone
	 *            o telefone do usuario
	 * @throws EntradaInvalidaException
	 *             caso algum dado seja invalido
	 */
    public void criarUsuario(String login, String nome, String email, String senha, String telefone) throws EntradaInvalidaException {
        Sistema.getInstance().criarUsuario(login, nome, email, senha, telefone);
    }

    /**
	 * Deleta um usuario do sistema, caso login e senha batam com o esperado.
	 * 
	 * @param login
	 *            o login do usuario deletado
	 * @param senha
	 *            a senha do usuario deletado, por questao de seguranca
	 * @throws NaoEncontradoException
	 *             caso o usuario a deletar nao seja encontrado
	 */
    public void deletarUsuario(String login, String senha) throws NaoEncontradoException {
        Sistema.getInstance().deletarUsuario(login, senha);
    }

    /**
	 * Retorna um conjunto de usuarios com o nome comecando com a substring
	 * passada como parametro
	 * 
	 * @param prefixo
	 *            o prefixo dos nomes dos usuarios a buscar
	 * @return um conjunto de nomes ordenado em ordem crescente
	 */
    public List<Usuario> usuariosPorNome(String prefixo) {
        return Sistema.getInstance().usuariosPorNome(prefixo);
    }

    /**
	 * Reseta a lista de usuarios e o historico, apagando tudo dos bancos de
	 * dados
	 * 
	 * @throws IOException
	 *             caso nao seja possivel acessar o historico
	 */
    public void reset() throws IOException {
        Sistema.getInstance().reset();
    }

    /**
	 * Encerra o sistema, mas antes salva todas as informacoes dos usuarios no
	 * banco de dados
	 */
    public void sair() {
        Sistema.getInstance().sair();
    }
}
