package org.weras.aplicacoes.clientes.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.weras.aplicacoes.clientes.business.pessoa.CadastroPessoaFisica;
import org.weras.commons.core.business.BusinessException;
import org.weras.commons.logger.LogMethod;
import org.weras.commons.search.Order;
import org.weras.commons.search.SearchResult;
import org.weras.portal.clientes.client.modulos.comum.PesquisaPessoaFisica;
import org.weras.portal.clientes.domain.pessoa.fisica.PessoaFisica;

@Service("PessoaFisicaFacade")
@Scope("singleton")
public class PessoaFisicaFacade implements IPessoaFisicaFacade {

    @Autowired
    protected CadastroPessoaFisica cadastro;

    @LogMethod
    @Transactional(readOnly = true)
    public SearchResult<PessoaFisica> pesquisar(PesquisaPessoaFisica pesquisa) throws BusinessException {
        return cadastro.pesquisar(pesquisa, new Order());
    }

    @LogMethod
    @Transactional
    public void excluir(Long id) throws BusinessException {
        cadastro.remove(id);
    }

    @LogMethod
    @Transactional
    public PessoaFisica salvar(PessoaFisica pessoa) throws BusinessException {
        return cadastro.salvar(pessoa);
    }

    @LogMethod
    @Transactional(readOnly = true)
    public PessoaFisica carregar(Long id) throws BusinessException {
        return cadastro.carrega(id, false);
    }

    @LogMethod
    @Transactional(readOnly = true)
    public SearchResult<PessoaFisica> pesquisarPorNome(PesquisaPessoaFisica pesquisa) {
        return cadastro.pesquisarPorNome(pesquisa.getNome(), pesquisa.getPaginacao());
    }
}
