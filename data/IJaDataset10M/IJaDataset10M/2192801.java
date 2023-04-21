package br.com.guaraba.wally.core.dominio.servicos.impl;

import java.util.Date;
import java.util.List;
import br.com.guaraba.commons.dao.DataAccessException;
import br.com.guaraba.commons.dao.IDAOGenerico.MatchMode;
import br.com.guaraba.commons.service.impl.ServiceException;
import br.com.guaraba.commons.util.EmailUtils;
import br.com.guaraba.commons.util.NumberUtils;
import br.com.guaraba.commons.util.StringUtils;
import br.com.guaraba.commons.util.exception.GenericExceptionMessageType;
import br.com.guaraba.commons.validators.DocumentValidator;
import br.com.guaraba.wally.core.dao.IFornecedorDAO;
import br.com.guaraba.wally.core.dao.impl.FornecedorDAO;
import br.com.guaraba.wally.core.dominio.entidades.Fornecedor;
import br.com.guaraba.wally.core.dominio.servicos.IFornecedorService;

public class FornecedorService implements IFornecedorService {

    private IFornecedorDAO fornecedorDAO;

    public FornecedorService() {
        fornecedorDAO = new FornecedorDAO();
    }

    public Fornecedor cadastrar(Fornecedor fornecedor) throws ServiceException {
        try {
            if (this.podeSerCadastrado(fornecedor)) {
                try {
                    fornecedor = this.fornecedorDAO.salvar(fornecedor);
                } catch (Exception e) {
                    throw new ServiceException("Ocorreu um erro ao tentar cadastrar o fornecedor " + fornecedor, e);
                }
            }
        } catch (DataAccessException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return fornecedor;
    }

    public Fornecedor carregarPorLoginSenha(String login, String senha) throws ServiceException {
        Fornecedor fornecedor = null;
        try {
            fornecedor = this.fornecedorDAO.carregarPorLoginSenha(login, senha);
        } catch (DataAccessException e) {
            throw new ServiceException("Ocorreu um erro ao tentar carregar o fornecedor " + fornecedor, e);
        } catch (Exception e) {
            throw new ServiceException("Ocorreu um erro ao tentar carregar o fornecedor " + fornecedor, e);
        }
        return fornecedor;
    }

    public List<Fornecedor> listarTodos() throws ServiceException {
        List<Fornecedor> fornecedores = null;
        try {
            fornecedores = this.fornecedorDAO.listarTodos();
        } catch (DataAccessException e) {
            throw new ServiceException("Ocorreu um erro ao tentar listar todos os fornecedores.", e);
        }
        return fornecedores;
    }

    public Fornecedor salvar(Fornecedor fornecedor) throws ServiceException {
        try {
            if (this.podeSerCadastrado(fornecedor)) {
                try {
                    fornecedor = this.fornecedorDAO.salvar(fornecedor);
                } catch (Exception e) {
                    throw new ServiceException("Ocorreu um erro ao tentar salvar o fornecedor " + fornecedor, e);
                }
            }
        } catch (DataAccessException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return fornecedor;
    }

    public void excluir(Fornecedor fornecedor) throws ServiceException {
        try {
            this.fornecedorDAO.excluir(fornecedor);
        } catch (DataAccessException e) {
            throw new ServiceException("Ocorreu um erro ao tentar excluir o fornecedor " + fornecedor, e);
        }
    }

    private boolean podeSerCadastrado(Fornecedor fornecedor) throws ServiceException {
        fornecedor.setCnpj(StringUtils.removeSpecialCharacters(fornecedor.getCnpj()));
        fornecedor.getContato().setTelefone(StringUtils.removeSpecialCharacters(fornecedor.getContato().getTelefone()));
        fornecedor.getEndereco().setCep(StringUtils.removeSpecialCharacters(fornecedor.getEndereco().getCep()));
        Fornecedor modelo = null;
        Fornecedor fornecedorExistente = null;
        ServiceException serviceException = new ServiceException();
        if (StringUtils.isNullOrEmpty(fornecedor.getNomeFantasia())) {
            serviceException.addMessage(GenericExceptionMessageType.WARNING, "� necess�rio informar o Nome Fantasia");
        }
        if (StringUtils.isNullOrEmpty(fornecedor.getRazaoSocial())) {
            serviceException.addMessage(GenericExceptionMessageType.WARNING, "� necess�rio informar a Raz�o Social");
        }
        if (StringUtils.isNullOrEmpty(fornecedor.getCnpj())) {
            serviceException.addMessage(GenericExceptionMessageType.WARNING, "� necess�rio informar o CNPJ");
        }
        if (!DocumentValidator.isValidCNPJ(fornecedor.getCnpj())) {
            serviceException.addMessage(GenericExceptionMessageType.WARNING, "CNPJ inv�lido");
        }
        if (!EmailUtils.isEmailValido(fornecedor.getContato().getEmail())) {
            serviceException.addMessage(GenericExceptionMessageType.WARNING, "� necess�rio informar um endere�oo de e-mail v�lido.");
        }
        if (fornecedor.getEndereco() == null || StringUtils.isNullOrEmpty(fornecedor.getEndereco().getLogradouro()) || StringUtils.isNullOrEmpty(fornecedor.getEndereco().getCidade()) || StringUtils.isNullOrEmpty(fornecedor.getEndereco().getEstado())) {
            serviceException.addMessage(GenericExceptionMessageType.WARNING, "� necess�rio informar o Endereço (ao menos o Logradouro, Cidade e Estado).");
        }
        if (fornecedor.getContato() == null) {
            serviceException.addMessage(GenericExceptionMessageType.WARNING, "� necess�rio informar um Contato, seu nome, email, login e senha.");
        } else {
            if (StringUtils.isNullOrEmpty(fornecedor.getContato().getNome())) {
                serviceException.addMessage(GenericExceptionMessageType.WARNING, "� necess�rio informar o nome do Contato.");
            }
            if (StringUtils.isNullOrEmpty(fornecedor.getContato().getEmail())) {
                serviceException.addMessage(GenericExceptionMessageType.WARNING, "� necess�rio informar o email do Contato.");
            }
            if (StringUtils.isNullOrEmpty(fornecedor.getContato().getLogin())) {
                serviceException.addMessage(GenericExceptionMessageType.WARNING, "� necess�rio informar o login do Contato.");
            }
            if (StringUtils.isNullOrEmpty(fornecedor.getContato().getSenha())) {
                serviceException.addMessage(GenericExceptionMessageType.WARNING, "� necess�rio informar a senha do Contato.");
            }
            if (fornecedor.getContato().getSenha().length() < 4) {
                serviceException.addMessage(GenericExceptionMessageType.WARNING, "A senha deve ter no m�nimo 4 d�gitos ou caracteres.");
            }
        }
        try {
            if (!StringUtils.isNullOrEmpty(fornecedor.getCnpj())) {
                modelo = new Fornecedor();
                modelo.setCnpj(fornecedor.getCnpj());
                fornecedorExistente = this.fornecedorDAO.carregar(modelo, MatchMode.EXAT);
                if (fornecedorExistente != null) {
                    serviceException.addMessage(GenericExceptionMessageType.WARNING, "O CNPJ '".concat(fornecedor.getCnpj().toString()).concat("'").concat(" j� est� cadastrado. N�o � poss�vel cadastrar um CNPJ j� cadastrado."));
                }
            }
            if (!StringUtils.isNullOrEmpty(fornecedor.getRazaoSocial())) {
                fornecedorExistente = null;
                modelo = new Fornecedor();
                modelo.setRazaoSocial(fornecedor.getRazaoSocial());
                fornecedorExistente = this.fornecedorDAO.carregar(modelo, MatchMode.EXAT);
                if (fornecedorExistente != null) {
                    serviceException.addMessage(GenericExceptionMessageType.WARNING, "A Raz�o Social '".concat(fornecedor.getRazaoSocial()).concat("'").concat(" j� est� cadastrada. N�o � poss�vel cadastrar uma Raz�o Social j� cadastrada."));
                }
            }
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
        if (serviceException.hasMessages()) {
            throw serviceException;
        }
        if (NumberUtils.isNullOrZero(fornecedor.getId())) {
            fornecedor.setDataHoraCadastro(new Date());
        }
        return true;
    }
}
