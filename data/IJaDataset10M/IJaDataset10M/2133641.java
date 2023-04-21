package br.com.dotec.controller;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.joda.time.DateTime;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.Validations;
import br.com.dotec.infra.interceptor.Publico;
import br.com.dotec.model.Boleto;
import br.com.dotec.model.Cliente;
import br.com.dotec.model.ClientePessoaFisica;
import br.com.dotec.model.ClientePessoaJuridica;
import br.com.dotec.model.Endereco;
import br.com.dotec.model.Movimentacao;
import br.com.dotec.model.Sexo;
import br.com.dotec.model.StatusDaMovimentacao;
import br.com.dotec.model.TipoDeLogradouro;
import br.com.dotec.model.TipoDeMovimentacao;
import br.com.dotec.model.TipoDeUsuario;
import br.com.dotec.model.Uf;
import br.com.dotec.persistence.dao.ClienteDAO;
import br.com.dotec.persistence.dao.EntregaCepDAO;
import br.com.dotec.persistence.dao.MovimentacaoDao;
import br.com.dotec.persistence.dao.UsuarioDAO;
import br.com.dotec.util.DotecException;
import br.com.dotec.util.manager.ManagerFactory;
import br.com.dotec.util.properties.ApplicationProperties;

@Resource
public class CadastroController {

    private final ClienteDAO clienteDao;

    private final UsuarioDAO usuarioDao;

    private final EntregaCepDAO entregaDAO;

    private final MovimentacaoDao movimentacaoDao;

    private final Result result;

    private final Validator validator;

    private Session session;

    static Logger logger = Logger.getLogger(CadastroController.class);

    public CadastroController(ClienteDAO clienteDao, Result result, Validator validator, MovimentacaoDao movimentacaoDao, Session session, UsuarioDAO usuarioDao, EntregaCepDAO entregaDAO) {
        super();
        this.clienteDao = clienteDao;
        this.result = result;
        this.validator = validator;
        this.movimentacaoDao = movimentacaoDao;
        this.session = session;
        this.usuarioDao = usuarioDao;
        this.entregaDAO = entregaDAO;
    }

    @Publico
    public void cadastro(Cliente cliente) {
        incluiListas();
        result.include("cliente", cliente);
    }

    @Publico
    public void cadastroefetuado() {
    }

    @Publico
    public void incluiListas() {
        result.include("sexoList", Sexo.values());
        result.include("ufs", Uf.values());
        result.include("tipoDeLogradouroList", TipoDeLogradouro.values());
    }

    /**
	 * 
	 * @param clientePessoaFisica
	 */
    @Publico
    @Path(value = { "cadastro/clientesPessoaFisica/adicionaPublico" }, priority = 1)
    public void adicionaPessoaFisica(ClientePessoaFisica clientePessoaFisica) {
        incluiListas();
        clientePessoaFisica.setEmail(clientePessoaFisica.getUsuarios().get(0).getEmail());
        validator.validate(clientePessoaFisica);
        for (Endereco endereco : clientePessoaFisica.getEnderecos()) {
            validator.validate(endereco);
        }
        final boolean possuiEnderecoDeCobranca = clientePessoaFisica.possuiEnderecoDeCobranca();
        validator.checking(new Validations() {

            {
                that(possuiEnderecoDeCobranca, "Endere�o", "validator.notHaveBillingAddress");
            }
        });
        final boolean leuContrato = clientePessoaFisica.isContrato();
        validator.checking(new Validations() {

            {
                that(leuContrato, "Contrato", "validator.notReadContract");
            }
        });
        String tel = clientePessoaFisica.getTelefone().getNumero();
        final boolean isValidPhone = ((tel == null) || (tel.length() != 8) || (!tel.matches("[0-9]{8}?"))) ? false : true;
        validator.checking(new Validations() {

            {
                that(isValidPhone, "Telefone", "validator.invalidPhone");
            }
        });
        final boolean userExist = usuarioDao.carrega(clientePessoaFisica.getEmail()) != null ? false : true;
        validator.checking(new Validations() {

            {
                that(userExist, "Usuario", "validator.userExists");
            }
        });
        String cep = clientePessoaFisica.getEnderecos().get(0).getCep();
        String[] ceps = cep.split("-");
        cep = ceps[0] + ceps[1];
        final boolean cepEntregaValida = entregaDAO.carrega(Long.parseLong(cep)) != null ? true : false;
        validator.checking(new Validations() {

            {
                that(cepEntregaValida, "Entrega", "validator.invalidDelivery");
            }
        });
        clientePessoaFisica = preencheDadosDoUsuario(clientePessoaFisica);
        validator.validate(clientePessoaFisica.getUsuarios().get(0));
        validator.onErrorUsePageOf(CadastroController.class).cadastro(clientePessoaFisica);
        try {
            criaMovimentacao(clientePessoaFisica);
            sendEmailCadastro(clientePessoaFisica);
            result.redirectTo(CadastroController.class).cadastroefetuado();
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        }
    }

    /**
 * 
 * @param clientePessoaJuridica
 */
    @Publico
    @Path(value = { "cadastro/clientesPessoaJuridica/adicionaPublico" }, priority = 1)
    public void adicionaPublico(ClientePessoaJuridica clientePessoaJuridica) {
        incluiListas();
        validator.validate(clientePessoaJuridica);
        for (Endereco endereco : clientePessoaJuridica.getEnderecos()) {
            validator.validate(endereco);
        }
        final boolean possuiEnderecoDeCobranca = clientePessoaJuridica.possuiEnderecoDeCobranca();
        validator.checking(new Validations() {

            {
                that(possuiEnderecoDeCobranca, "Endere�o", "validator.notHaveBillingAddress");
            }
        });
        final boolean userExist = usuarioDao.carrega(clientePessoaJuridica.getEmail()) != null ? true : false;
        validator.checking(new Validations() {

            {
                that(userExist, "Usuario", "validator.userExists");
            }
        });
        String tel = clientePessoaJuridica.getTelefone().getNumero();
        final boolean isValidPhone = ((tel == null) || (tel.length() != 8) || (!tel.matches("[0-9]{8}?"))) ? false : true;
        validator.checking(new Validations() {

            {
                that(isValidPhone, "Telefone", "validator.invalidPhone");
            }
        });
        String cep = clientePessoaJuridica.getEnderecos().get(0).getCep();
        String[] ceps = cep.split("-");
        cep = ceps[0] + ceps[1];
        final boolean cepEntregaValida = entregaDAO.carrega(Long.parseLong(cep)) != null ? true : false;
        validator.checking(new Validations() {

            {
                that(cepEntregaValida, "Entrega", "validator.invalidDelivery");
            }
        });
        clientePessoaJuridica.getUsuarios().get(0).setTipoDeUsuario(TipoDeUsuario.PRIMARIO);
        validator.validate(clientePessoaJuridica.getUsuarios().get(0));
        validator.onErrorUsePageOf(CadastroController.class).cadastro(clientePessoaJuridica);
        try {
            criaMovimentacao(clientePessoaJuridica);
            sendEmailCadastro(clientePessoaJuridica);
            result.redirectTo(CadastroController.class).cadastroefetuado();
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            validator.onErrorUsePageOf(CadastroController.class).cadastro(clientePessoaJuridica);
        }
    }

    /**
	 * 
	 * @param clientePessoaFisica
	 * @throws EmailException
	 * @throws DotecException
	 */
    private void sendEmailCadastro(Cliente clientePessoaFisica) throws EmailException, DotecException {
        HtmlEmail email = new HtmlEmail();
        String emailFrom = ApplicationProperties.getInstance().getMailLogin();
        String name = ApplicationProperties.getInstance().getMailName();
        String subject = ApplicationProperties.getInstance().getMailMessageSubjectCadastroCliente();
        email.setSubject(subject);
        email.addTo(clientePessoaFisica.getEmail(), clientePessoaFisica.getNome());
        email.setFrom(emailFrom, name);
        String html = ApplicationProperties.getInstance().getMailMessageBodyCadastroCliente();
        html.replaceAll("%NOME%", clientePessoaFisica.getNome());
        html.replaceAll("%EMAIL%", clientePessoaFisica.getEmail());
        html.replaceAll("%LOGIN%", clientePessoaFisica.getUsuarios().get(0).getEmail());
        email.setHtmlMsg(html);
        ManagerFactory.getSendMailManager().sendMail(email, html);
    }

    private ClientePessoaFisica preencheDadosDoUsuario(ClientePessoaFisica clientePessoaFisica) {
        clientePessoaFisica.getUsuarios().get(0).setNome(clientePessoaFisica.getNome());
        clientePessoaFisica.getUsuarios().get(0).setEmail(clientePessoaFisica.getEmail());
        clientePessoaFisica.getUsuarios().get(0).setCpf(clientePessoaFisica.getCpf());
        clientePessoaFisica.getUsuarios().get(0).setRg(clientePessoaFisica.getRg());
        clientePessoaFisica.getUsuarios().get(0).setDataDeNascimento(clientePessoaFisica.getDataDeNascimento());
        clientePessoaFisica.getUsuarios().get(0).setSexo(clientePessoaFisica.getSexo());
        clientePessoaFisica.getUsuarios().get(0).setTipoDeUsuario(TipoDeUsuario.PRIMARIO);
        return clientePessoaFisica;
    }

    /**
	 * 
	 * @param cliente
	 * @throws DotecException
	 */
    private void criaMovimentacao(Cliente cliente) throws DotecException {
        clienteDao.salva(cliente);
        Movimentacao mov = new Movimentacao();
        mov.setDataDeCriacao(DateTime.now());
        mov.setStatusDaMovimentacao(StatusDaMovimentacao.PENDENTE);
        mov.setTipoDeMovimentacao(TipoDeMovimentacao.NOVO_CLIENTE);
        mov.setValor(ApplicationProperties.getInstance().getValorCadastroCliente());
        mov.setCriadaPor(null);
        movimentacaoDao.salva(mov);
        List<Movimentacao> movs = new ArrayList<Movimentacao>();
        movs.add(mov);
        Boleto boleto = ManagerFactory.getMoipManager().generateInvoice(movs, session, cliente, movimentacaoDao, DateTime.now().dayOfMonth().get());
        cliente.setIdProprio(boleto.getIdProprio());
        List<Boleto> list = new ArrayList<Boleto>();
        list.add(boleto);
        cliente.setBoletos(list);
        clienteDao.atualiza(cliente);
    }
}
