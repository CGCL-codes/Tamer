package br.com.cefetrn.apoena.dominio.home;

import br.com.cefetrn.apoena.dominio.entity.Pessoa;

/**
 * Home object for domain model class Pessoa.
 * @see br.com.cefetrn.apoena.dominio.entity.Pessoa
 * @author Gilmar P.S.L.
 */
public class Home4Pessoa extends AxHomeGeneric<Pessoa, Integer> {

    @Override
    public Pessoa persist(Pessoa instance) {
        Pessoa pessoa = null;
        if (instance != null) {
            Home4Login hl = new Home4Login();
            hl.setSession(getSession());
            hl.persist(instance.getLogin());
            Home4Endereco he = new Home4Endereco();
            he.setSession(getSession());
            he.persist(instance.getEndereco());
            pessoa = super.persist(instance);
        }
        return pessoa;
    }

    @Override
    public Pessoa delete(Pessoa persistentInstance) {
        Home4Login loginHome = new Home4Login();
        Home4Endereco enderecoHome = new Home4Endereco();
        loginHome.setSession(getSession());
        loginHome.delete(persistentInstance.getLogin());
        enderecoHome.setSession(getSession());
        enderecoHome.delete(persistentInstance.getEndereco());
        return super.delete(persistentInstance);
    }
}
