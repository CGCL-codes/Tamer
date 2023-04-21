package root.plumbum2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import root.repositorios.RepositorioEmprestimo;
import root.repositorios.RepositorioInterface;
import root.repositorios.RepositorioPublicacao;
import root.repositorios.RepositorioSessao;
import root.repositorios.RepositorioUsuario;
import root.util.ComparadorDeEnderecos;

public class Sistema {

    private static Sistema sistemaInstance = null;

    private RepositorioInterface repositorioUsuario;

    private RepositorioInterface repositorioSessao;

    public static Sistema getInstance() {
        if (sistemaInstance == null) sistemaInstance = new Sistema();
        return sistemaInstance;
    }

    private Sistema() {
        repositorioUsuario = new RepositorioUsuario();
        repositorioSessao = new RepositorioSessao();
    }

    /**
	 * 
	 * Cria um novo usuário e insere ele no repositorio de Usuarios
	 * 
	 * @param login
	 * @param nome
	 * @param endereco
	 * @throws Exception já existe um usuário com este login
	 * 
	 * 
	 * */
    public void criarUsuario(String login, String nome, String endereco) throws Exception {
        Usuario user = new Usuario(login, nome, endereco);
        if (repositorioUsuario.search(user) == null) {
            repositorioUsuario.insert(user);
        } else {
            throw new Exception("Já existe um usuário com este login");
        }
    }

    /**
	 * 
	 * Abre uma sessao para o usuario já cadastrado
	 * @param login Login do usuario
	 * @throws Exception Usuario inexistente caso você tente abrir a sessao de um usuario que nao exista
	 * @return idSessao
	 * 
	 * */
    public String abrirSessao(String login) throws Exception {
        VerificadorDadosEntrada.verificaLogin(login);
        Usuario user = ((RepositorioUsuario) repositorioUsuario).searchLogin(login);
        if (user == null) throw new Exception("Usuário inexistente");
        String retorno = "";
        Sessao sessao = new Sessao(user);
        repositorioSessao.insert(sessao);
        retorno = sessao.getIdSessao();
        return retorno;
    }

    /**
	 * Procura o login (Usuario) e retorna o atributo(nome ou endereco) do usuario
	 * @param login Login do usuario
	 * @param atributo atributo que você deseja ver desse usuario
	 * @throws Exception Usuario inexistente se nao encontrar o usuario
	 * @return nome ou endereco do usuario
	 * */
    public String getAtributoUsuario(String login, String atributo) throws Exception {
        VerificadorDadosEntrada.verificaLogin(login);
        VerificadorDadosEntrada.verificaAtributo(atributo);
        String retorno = "";
        Usuario user = ((RepositorioUsuario) repositorioUsuario).searchLogin(login);
        if (user == null) throw new Exception("Usuário inexistente");
        if (atributo.equals("nome")) retorno = user.getNome(); else if (atributo.equals("endereco")) retorno = user.getEndereco();
        return retorno;
    }

    /**
	 * O usuario cadastra um item
	 * 
	 * @param idSessao ID do usuario que vai cadastrar o item
	 * @param nome do item
	 * @param descricao do item
	 * @param categoria do item
	 * @throws Exception A exceção pode ser lançada por outras chamadas de metodos
	 * @return idItem
	 * */
    public String cadastrarItem(String IdSessao, String nome, String descricao, String categoria) throws Exception {
        VerificadorDadosEntrada.verificaIdSessao(IdSessao);
        Sessao session = ((RepositorioSessao) repositorioSessao).search(IdSessao);
        Usuario usuario = session.getUsuario();
        Item item = new Item(nome, descricao, categoria, usuario.getLogin());
        String idItem = usuario.cadastrarItem(item);
        return idItem;
    }

    /**
	 *
	 * Porcura um item e retorna o atributo pesquisado
	 * 
	 * @param idItem
	 * @param atributo
	 * @throws Exception Item inexistente
	 * @return atributo pesquisado
	 * 
	 * */
    public String getAtributoItem(String idItem, String atributo) throws Exception {
        VerificadorDadosEntrada.verificaAtributoItem(atributo);
        VerificadorDadosEntrada.verificaIdItem(idItem);
        String atri = null;
        for (Usuario u : ((List<Usuario>) this.repositorioUsuario.getRepositorio())) {
            atri = u.getAtributoItemOutException(idItem, atributo);
            if (atri != null) break;
        }
        if (atri == null) throw new Exception("Item inexistente");
        return atri;
    }

    /**
	 * Um usuario pode localizar outro usuario cadastrado,
	 * para isso é necessario informar o nome ou endereço
	 * 
	 * @param idSessao usuario logado no sistema
	 * @param chave 
	 * @param atributo
	 * @return String com uma lista dos usuarios com as caracteristicas pesquisadas
	 * @throws Exception
	 */
    public String localizarUsuario(String idSessao, String chave, String atributo) throws Exception {
        VerificadorDadosEntrada.verificaLocalizarUsuario(idSessao, chave, atributo);
        Sessao session = ((RepositorioSessao) repositorioSessao).search(idSessao);
        String login = session.getUsuario().getLogin();
        List<Usuario> listaUser = ((RepositorioUsuario) repositorioUsuario).search(login, chave);
        Collections.sort(listaUser, new ComparadorDeEnderecos(session.getUsuario().getEndereco()));
        return concatenaUser(listaUser);
    }

    public String localizarUsuario(String idSessao) throws Exception {
        VerificadorDadosEntrada.verificaIdSessao(idSessao);
        Sessao session = ((RepositorioSessao) repositorioSessao).search(idSessao);
        ArrayList<Usuario> listaUsers = (ArrayList<Usuario>) ((ArrayList) ((RepositorioUsuario) repositorioUsuario).getRepositorio()).clone();
        listaUsers.remove(session.getUsuario());
        Collections.sort(listaUsers, new ComparadorDeEnderecos(session.getUsuario().getEndereco()));
        return concatenaUser(listaUsers);
    }

    private String concatenaUser(List<Usuario> lista) {
        String retorno = "";
        for (Usuario user : lista) {
            retorno += user.getNome() + " - " + user.getEndereco() + "; ";
        }
        return retorno.length() > 0 ? retorno.substring(0, retorno.length() - 2) : "Nenhum usuário encontrado";
    }

    /**
	 * Mostra os usuario que estao requisitando amizade
	 * 
	 * @param idSessao
	 * @return
	 * @throws Exception
	 */
    public String getRequisicoesDeAmizade(String idSessao) throws Exception {
        VerificadorDadosEntrada.verificaIdSessao(idSessao);
        Sessao session = ((RepositorioSessao) repositorioSessao).search(idSessao);
        Usuario usuario = session.getUsuario();
        List<Usuario> meusAmigos = usuario.getRepositorioRequisicoesAmizade();
        String retorno = "";
        for (Usuario user : meusAmigos) {
            retorno += user.getLogin() + "; ";
        }
        return retorno.length() > 0 ? retorno.substring(0, retorno.length() - 2) : "Não há requisições";
    }

    /**
	 * 
	 * O usuario requisita amizade a qualquer outro usuario do sistema
	 * informando o login do amigo
	 * @param idSessao
	 * @param login
	 * @throws Exception o login procurado pode nao existir
	 */
    public void requisitarAmizade(String idSessao, String login) throws Exception {
        VerificadorDadosEntrada.verificaIdSessaoLogin(idSessao, login);
        Sessao session = ((RepositorioSessao) repositorioSessao).search(idSessao);
        Usuario usuario = session.getUsuario();
        Usuario amigo = ((RepositorioUsuario) repositorioUsuario).searchLogin(login);
        if (amigo == null) throw new Exception("Usuário inexistente");
        amigo.addRequisicao(usuario);
    }

    /**
	 * Verifica se dois usuarios ja sao amigos
	 * 
	 * @param idSessao
	 * @param login
	 * @return
	 * @throws Exception o login (usuario) pode nao existir
	 */
    public boolean ehAmigo(String idSessao, String login) throws Exception {
        VerificadorDadosEntrada.verificaIdSessaoLogin(idSessao, login);
        Sessao session = ((RepositorioSessao) repositorioSessao).search(idSessao);
        Usuario usuario = session.getUsuario();
        Usuario amigo = ((RepositorioUsuario) repositorioUsuario).searchLogin(login);
        if (amigo == null) throw new Exception("Usuário inexistente");
        return usuario.saoAmigos(amigo);
    }

    /**
	 * Aprova a amizade requisitada 
	 * 
	 * @param idSessao
	 * @param login
	 * @throws Exception (login)usuario inexistente 
	 * @throws Exception usuarios ja sao amigos
	 * @throws Exception nao existe a requisicao de amizade
	 */
    public void aprovarAmizade(String idSessao, String login) throws Exception {
        VerificadorDadosEntrada.verificaIdSessaoLogin(idSessao, login);
        Sessao session = ((RepositorioSessao) repositorioSessao).search(idSessao);
        Usuario usuario = session.getUsuario();
        Usuario usuario2 = ((RepositorioUsuario) repositorioUsuario).searchLogin(login);
        usuario.aprovarAmizade(usuario2);
    }

    /**
	 * Mostra todos os seus amigos
	 * @param idSessao
	 * @return lista com meus amigos
	 * @throws Exception pode lançar exceções de outros metodos chamados
	 */
    public String getAmigos(String idSessao) throws Exception {
        VerificadorDadosEntrada.verificaIdSessao(idSessao);
        Sessao session = ((RepositorioSessao) repositorioSessao).search(idSessao);
        Usuario usuario = session.getUsuario();
        return usuario.getAmigos();
    }

    /**
	 * Mostra os amigos do meu amigo
	 * @param idSessao
	 * @param login Login do meu amigo
	 * @return lista com os amigos do meu amigo
	 * @throws Exception usuario(login) inexistente
	 */
    public String getAmigos(String idSessao, String login) throws Exception {
        VerificadorDadosEntrada.verificaIdSessaoLogin(idSessao, login);
        Sessao session = ((RepositorioSessao) repositorioSessao).search(idSessao);
        Usuario usuario = session.getUsuario();
        Usuario usuario2 = ((RepositorioUsuario) repositorioUsuario).searchLogin(login);
        if (usuario2 == null) throw new Exception("Usuário inexistente");
        return usuario2.getAmigos();
    }

    /**
	 * Mostra os meus itens
	 * @param idSessao
	 * @return lista com meus itens
	 * @throws Exception Exception pode lançar exceções de outros metodos chamados
	 */
    public String getItens(String idSessao) throws Exception {
        VerificadorDadosEntrada.verificaIdSessao(idSessao);
        Sessao session = ((RepositorioSessao) repositorioSessao).search(idSessao);
        Usuario usuario = session.getUsuario();
        return usuario.getItens();
    }

    /**
	 * Mostra os items de um amigo
	 * @param idSessao
	 * @param login do amigo
	 * @return lista com itens do meu amigo
	 * @throws Exception Usuario nao tem permissao para ver o item
	 * @throws Exception Usuario(login) inexistente 
	 */
    public String getItens(String idSessao, String login) throws Exception {
        VerificadorDadosEntrada.verificaIdSessaoLogin(idSessao, login);
        Sessao session = ((RepositorioSessao) repositorioSessao).search(idSessao);
        Usuario usuario = session.getUsuario();
        Usuario usuario2 = ((RepositorioUsuario) repositorioUsuario).searchLogin(login);
        if (usuario2 == null) throw new Exception("Usuário inexistente");
        if (ehAmigo(idSessao, login)) return usuario2.getItens(); else throw new Exception("O usuário não tem permissão para visualizar estes itens");
    }

    /**
	 * Mostra uma lista com os emprestimos do tipo escolhido, pode ser
	 * beneficiado, emprestador ou todos
	 * @param idSessao
	 * @param tipo
	 * @return lista com os emprestimos
	 * @throws Exception pode lançar exceções de outros metodos chamados
	 */
    public String getEmprestimos(String idSessao, String tipo) throws Exception {
        VerificadorDadosEntrada.verificaIdSessao(idSessao);
        Sessao session = ((RepositorioSessao) repositorioSessao).search(idSessao);
        Usuario usuario = session.getUsuario();
        return usuario.getEmprestimos(tipo);
    }

    /**
	 * Procura por um item e requisita o emprestimo do item ao usuario dono do item
	 * @param idSessao
	 * @param idItem
	 * @param duracao informa a duracao do emprestimo
	 * @return idEmprestimo 
	 * @throws Exception pode lançar exceções de outros metodos chamados
	 */
    public String requisitarEmprestimo(String idSessao, String idItem, int duracao) throws Exception {
        VerificadorDadosEntrada.verificaIdSessao(idSessao);
        VerificadorDadosEntrada.verificaIdItem(idItem);
        Sessao session = ((RepositorioSessao) repositorioSessao).search(idSessao);
        Usuario usuario = session.getUsuario();
        Item item = null;
        Usuario dono = null;
        for (Usuario user : ((RepositorioUsuario) repositorioUsuario).getRepositorio()) {
            for (Item it : user.getRepositorioItem()) {
                if (it.getIDItem().equals(idItem)) {
                    item = it;
                    dono = user;
                }
            }
        }
        if (item == null) throw new Exception("Item inexistente");
        return usuario.requisitarEmprestimo(dono, item.getIDItem(), duracao, usuario.getLogin());
    }

    /**
	 * Aprova um emprestimo
	 * @param idSessao
	 * @param idRequisicaoEmprestimo
	 * @return idEmprestimo
	 * @throws Exception pode lançar exceções de outros metodos chamados
	 */
    public String aprovarEmprestimo(String idSessao, String idRequisicaoEmprestimo) throws Exception {
        Sessao session = ((RepositorioSessao) repositorioSessao).search(idSessao);
        Usuario usuario = session.getUsuario();
        return usuario.aprovarEmprestimo(usuario.getLogin(), idRequisicaoEmprestimo);
    }

    /**
	 * Devolve o item que foi pedido emprestado
	 * @param idSessao
	 * @param idEmprestimo
	 * @throws Exception pode lançar exceções de outros metodos chamados
	 */
    public void devolverItem(String idSessao, String idEmprestimo) throws Exception {
        VerificadorDadosEntrada.verificaIdSessao(idSessao);
        VerificadorDadosEntrada.verificaIdEmprestimo(idEmprestimo);
        Sessao session = ((RepositorioSessao) repositorioSessao).search(idSessao);
        Usuario beneficiado = session.getUsuario();
        beneficiado.devolverItem(idEmprestimo);
    }

    /**
	 * O dono do item emprestado confirma o termino do emprestimo
	 * 
	 * @param idSessao
	 * @param idEmprestimo
	 * @throws Exception pode lançar exceções de outros metodos chamados
	 */
    public void confirmarTerminoEmprestimo(String idSessao, String idEmprestimo) throws Exception {
        VerificadorDadosEntrada.verificaIdSessao(idSessao);
        VerificadorDadosEntrada.verificaIdEmprestimo(idEmprestimo);
        Sessao session = ((RepositorioSessao) repositorioSessao).search(idSessao);
        Usuario usuario = session.getUsuario();
        usuario.confirmarTerminoEmprestimo(idEmprestimo);
    }

    /**
	 * Envia uma mensagem do tipo offtopic para qualquer amigo
	 * 
	 * @param idSessao
	 * @param destinatario
	 * @param assunto
	 * @param mensagem
	 * @return idTopico
	 * @throws Exception pode lançar exceções de outros metodos chamados
	 */
    public String enviarMensagem(String idSessao, String destinatario, String assunto, String mensagem) throws Exception {
        VerificadorDadosEntrada.verificaIdSessao(idSessao);
        VerificadorDadosEntrada.verificaDestinatario(destinatario);
        VerificadorDadosEntrada.verificaAssunto(assunto);
        VerificadorDadosEntrada.verificaMensagem(mensagem);
        Sessao session = ((RepositorioSessao) repositorioSessao).search(idSessao);
        Usuario usuario = session.getUsuario();
        Usuario destino = ((RepositorioUsuario) repositorioUsuario).searchLogin(destinatario);
        return usuario.enviarMensagem(destinatario, assunto, mensagem, destino);
    }

    /**
	 * Envia uma mensagem do tipo negociacao para qualquer amigo
	 * 
	 * @param idSessao
	 * @param destinatario
	 * @param assunto
	 * @param mensagem
	 * @param idRequisicaoEmprestimo
	 * @return idTopico
	 * @throws Exception pode lançar exceções de outros metodos chamados
	 */
    public String enviarMensagem(String idSessao, String destinatario, String assunto, String mensagem, String idRequisicaoEmprestimo) throws Exception {
        VerificadorDadosEntrada.verificaIdSessao(idSessao);
        VerificadorDadosEntrada.verificaDestinatario(destinatario);
        VerificadorDadosEntrada.verificaAssunto(assunto);
        VerificadorDadosEntrada.verificaMensagem(mensagem);
        VerificadorDadosEntrada.verificaIdRequisicaoEmprestimo(idRequisicaoEmprestimo);
        Sessao session = ((RepositorioSessao) repositorioSessao).search(idSessao);
        Usuario usuario = session.getUsuario();
        return usuario.enviarMensagem(destinatario, assunto, mensagem, idRequisicaoEmprestimo);
    }

    /**
	 * Mostra os topicos do usuario relacionados a negociacao e offtopic
	 * 
	 * @param idSessao
	 * @param tipo
	 * @return
	 * @throws Exception pode lançar exceções de outros metodos chamados
	 */
    public String lerTopicos(String idSessao, String tipo) throws Exception {
        VerificadorDadosEntrada.verificaIdSessao(idSessao);
        VerificadorDadosEntrada.verificaTipo(tipo);
        Sessao session = ((RepositorioSessao) repositorioSessao).search(idSessao);
        Usuario usuario = session.getUsuario();
        return usuario.lerTopicos(tipo);
    }

    /**
	 * 
	 * @param idSessao
	 * @param idTopico
	 * @return
	 * @throws Exception
	 */
    public String lerMensagens(String idSessao, String idTopico) throws Exception {
        VerificadorDadosEntrada.verificaIdSessao(idSessao);
        VerificadorDadosEntrada.verificaIdTopico(idTopico);
        Sessao session = ((RepositorioSessao) repositorioSessao).search(idSessao);
        Usuario usuario = session.getUsuario();
        return usuario.lerMensagens(idTopico);
    }

    /**
	 * O usuario requisita a devolução do item emprestado
	 * 
	 * @param idSessao
	 * @param idEmprestimo
	 * @throws Exception pode lançar exceções de outros metodos chamados
	 */
    public void requisitarDevolucao(String idSessao, String idEmprestimo) throws Exception {
        VerificadorDadosEntrada.verificaIdSessao(idSessao);
        VerificadorDadosEntrada.verificaIdEmprestimo(idEmprestimo);
        Sessao session = ((RepositorioSessao) repositorioSessao).search(idSessao);
        Usuario usuario = session.getUsuario();
        usuario.requisitarDevolucao(idEmprestimo);
    }

    /**
	 * @param dias
	 */
    public void adicionarDias(int dias) {
        for (Usuario u : ((RepositorioUsuario) repositorioUsuario).getRepositorio()) {
            for (Emprestimo e : ((RepositorioEmprestimo) u.getRepositorioEmprestimo2()).getListaEmpBeneficiado()) {
                e.reduzDuracao(dias);
            }
        }
    }

    /**
	 * Registra interesse em um item
	 * 
	 * @param idSessao
	 * @param idItem
	 * @throws Exception pode lançar exceções de outros metodos chamados
	 */
    public void registrarInteresse(String idSessao, String idItem) throws Exception {
        VerificadorDadosEntrada.verificaIdSessao(idSessao);
        VerificadorDadosEntrada.verificaIdItem(idItem);
        Sessao session = ((RepositorioSessao) repositorioSessao).search(idSessao);
        Usuario usuario = session.getUsuario();
        Item item = null;
        Usuario dono = null;
        for (Usuario u : ((RepositorioUsuario) repositorioUsuario).getRepositorio()) {
            for (Item i : u.getRepositorioItem()) {
                if (i.getIDItem().equals(idItem)) {
                    item = i;
                    dono = u;
                    break;
                }
            }
        }
        usuario.registrarInteresse(item, dono);
    }

    public String pesquisarItem(String idSessao, String chave, String atributo, String tipoOrdenacao, String criterioOrdenacao) throws Exception {
        VerificadorDadosEntrada.verificaIdSessao(idSessao);
        Sessao session = ((RepositorioSessao) repositorioSessao).search(idSessao);
        Usuario usuario = session.getUsuario();
        return usuario.pesquisarItem(chave, atributo, tipoOrdenacao, criterioOrdenacao);
    }

    /**
	 * Desfaz uma relacao de amizade
	 * 
	 * @param idSessao
	 * @param loginAmigo
	 * @throws Exception
	 */
    public void desfazerAmizade(String idSessao, String loginAmigo) throws Exception {
        VerificadorDadosEntrada.verificaIdSessao(idSessao);
        VerificadorDadosEntrada.verificaLogin(loginAmigo);
        Sessao session = ((RepositorioSessao) repositorioSessao).search(idSessao);
        Usuario usuario = session.getUsuario();
        Usuario amigo = ((RepositorioUsuario) repositorioUsuario).searchLogin(loginAmigo);
        if (amigo == null) throw new Exception("Usuário inexistente");
        if (!ehAmigo(idSessao, loginAmigo)) {
            throw new Exception("Amizade inexistente");
        }
        for (Emprestimo e : usuario.getRepositorioEmprestimo()) {
            for (Emprestimo i : amigo.getRepositorioEmprestimo()) {
                if (e.getIdEmprestimo().equals(i.getIdEmprestimo())) {
                    e.deleta();
                    i.deleta();
                }
            }
        }
        amigo.getRepositorioAmigos().remove(usuario);
        usuario.getRepositorioAmigos().remove(amigo);
    }

    /**
	 * Apaga um item do usuario
	 * 
	 * @param idSessao
	 * @param idItem
	 * @throws Exception
	 */
    public void apagarItem(String idSessao, String idItem) throws Exception {
        VerificadorDadosEntrada.verificaIdSessao(idSessao);
        VerificadorDadosEntrada.verificaIdItem(idItem);
        Sessao session = ((RepositorioSessao) repositorioSessao).search(idSessao);
        Usuario usuario = session.getUsuario();
        usuario.apagarItem(idItem);
    }

    /**
	 * Retorna um ranking pela reputacao
	 * @param idSessao
	 * @param categoria
	 * @return
	 * @throws Exception
	 */
    public String getRanking(String idSessao, String categoria) throws Exception {
        String retorno = "";
        Sessao session = ((RepositorioSessao) repositorioSessao).search(idSessao);
        VerificadorDadosEntrada.verificaCategoria(categoria);
        Usuario usuario = session.getUsuario();
        if (categoria.equals("amigos")) {
            retorno = usuario.getRaking();
        } else {
            retorno = rankingGlobal();
        }
        return retorno;
    }

    private String rankingGlobal() {
        String retorno = "";
        ArrayList<Usuario> geral = (ArrayList) repositorioUsuario.getRepositorio();
        ArrayList<Usuario> clone = (ArrayList<Usuario>) geral.clone();
        Collections.sort(clone);
        retorno = compoeString(clone);
        if (retorno.equals("")) {
            retorno = "Não existe amigos";
        }
        return retorno;
    }

    private String compoeString(ArrayList<Usuario> lista) {
        String retorno = "";
        for (Usuario usuario : lista) {
            retorno = usuario.getLogin() + "; " + retorno;
        }
        if (!retorno.equals("")) retorno = retorno.substring(0, retorno.length() - 2);
        ;
        return retorno;
    }

    public String historicoAtividades(String idSessao) throws Exception {
        Sessao session = ((RepositorioSessao) repositorioSessao).search(idSessao);
        Usuario usuario = session.getUsuario();
        return usuario.historicoAtividades();
    }

    public String historicoAtividadesConjunto(String idSessao) throws Exception {
        Sessao session = ((RepositorioSessao) repositorioSessao).search(idSessao);
        Usuario usuario = session.getUsuario();
        return usuario.historicoAtividadesConjunto();
    }

    public String publicarPedido(String idSessao, String nomeItem, String descricaoItem) throws Exception {
        Sessao session = ((RepositorioSessao) repositorioSessao).search(idSessao);
        Usuario usuario = session.getUsuario();
        return usuario.publicarPedido(nomeItem, descricaoItem);
    }

    public void rePublicarPedido(String idSessao, String idPublicacao) throws Exception {
        Sessao session = ((RepositorioSessao) repositorioSessao).search(idSessao);
        Usuario usuario = session.getUsuario();
        usuario.rePublicarPedido(idPublicacao);
    }

    public void oferecerItem(String idSessao, String idPublicacaoPedido, String idItem) throws Exception {
        if (!VerificadorDadosEntrada.stringValida(idPublicacaoPedido)) throw new Exception("Identificador da publicação de pedido é inválido");
        if (!VerificadorDadosEntrada.stringValida(idItem)) throw new Exception("Identificador do item é inválido");
        Sessao session = ((RepositorioSessao) repositorioSessao).search(idSessao);
        Usuario usuario = session.getUsuario();
        Publicacao publicacao = null;
        for (Usuario us : ((RepositorioUsuario) repositorioUsuario).getRepositorio()) {
            publicacao = ((RepositorioPublicacao) us.getRepositorioPublicacao()).search(idPublicacaoPedido);
            if (publicacao != null) break;
        }
        if (publicacao == null) throw new Exception("Publicação de pedido inexistente");
        Usuario destinatario = ((RepositorioUsuario) repositorioUsuario).searchLogin(publicacao.getLoginDonoPublicacao());
        usuario.oferecerItem(idPublicacaoPedido, idItem, destinatario, publicacao);
    }

    /**
	 * zera o sistema
	 */
    public void zerarSistema() {
        repositorioUsuario = new RepositorioUsuario();
        repositorioSessao = new RepositorioSessao();
    }

    public void encerrarSistema() {
        zerarSistema();
    }

    /**
	 * Retorna uma sessao
	 * @param idSessao
	 * @return
	 * @throws Exception
	 */
    public Sessao getSessao(String idSessao) throws Exception {
        VerificadorDadosEntrada.verificaIdSessao(idSessao);
        Sessao sessao = null;
        for (Sessao s : ((RepositorioSessao) repositorioSessao).getRepositorio()) {
            if (s.getIdSessao().equals(idSessao)) {
                sessao = s;
            }
        }
        return sessao;
    }

    /**
	 * Retorna o usuario pesquisado por login
	 * @param login
	 * @return
	 * @throws Exception
	 */
    public Usuario getUsuarioPorLogin(String login) throws Exception {
        VerificadorDadosEntrada.verificaLogin(login);
        boolean existe = false;
        Usuario user = null;
        for (Usuario u : ((RepositorioUsuario) repositorioUsuario).getRepositorio()) {
            if (u.getLogin().equals(login)) {
                user = u;
                existe = true;
            }
        }
        if (existe) {
            return user;
        } else {
            throw new Exception("Usuário inexistente");
        }
    }
}
