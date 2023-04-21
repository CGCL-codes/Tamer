package br.gov.demoiselle.samples.addressbook.persistence.dao;

import br.gov.demoiselle.samples.addressbook.bean.Telefone;
import br.gov.framework.demoiselle.core.layer.IDAO;
import br.gov.framework.demoiselle.util.page.Page;
import br.gov.framework.demoiselle.util.page.PagedResult;

public interface ITelefoneDAO extends IDAO<Telefone> {

    PagedResult<Telefone> listar(Page page);

    Telefone consultar(Telefone telefone, String condicao);
}
