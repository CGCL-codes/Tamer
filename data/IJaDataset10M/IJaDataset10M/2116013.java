package br.com.dotec.persistence.dao;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import org.hibernate.Session;
import br.com.caelum.vraptor.ioc.Component;
import br.com.dotec.model.Caixa;
import br.com.dotec.model.ClientePessoaFisica;
import br.com.dotec.util.DotecException;

@Component
public class ClientePessoaFisicaDAO {

    private Session session;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Caixa> caixas;

    public ClientePessoaFisicaDAO(Session session) {
        this.session = session;
    }

    public void salva(ClientePessoaFisica objtect) throws DotecException {
        this.session.save(objtect);
    }

    public void remove(ClientePessoaFisica objtect) {
        this.session.delete(objtect);
    }

    public ClientePessoaFisica carrega(Long id) {
        return (ClientePessoaFisica) this.session.get(ClientePessoaFisica.class, id);
    }

    public void atualiza(ClientePessoaFisica objtect) {
        this.session.update(objtect);
    }

    @SuppressWarnings("unchecked")
    public List<ClientePessoaFisica> lista() {
        return this.session.createCriteria(ClientePessoaFisica.class).list();
    }

    public List<Caixa> getCaixas() {
        return caixas;
    }

    public void setCaixas(List<Caixa> caixas) {
        this.caixas = caixas;
    }
}
