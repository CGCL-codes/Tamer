package br.com.mcampos.ejb.cloudsystem.client.session;

import br.com.mcampos.ejb.cloudsystem.client.entity.Client;
import br.com.mcampos.ejb.cloudsystem.client.entity.ClientPK;
import br.com.mcampos.ejb.cloudsystem.user.Users;
import br.com.mcampos.ejb.cloudsystem.user.company.entity.Company;
import br.com.mcampos.exception.ApplicationException;
import java.io.Serializable;
import java.util.List;
import javax.ejb.Local;

@Local
public interface ClientSessionLocal extends Serializable {

    Client add(Client entity) throws ApplicationException;

    void delete(ClientPK key) throws ApplicationException;

    Client get(ClientPK key) throws ApplicationException;

    Client get(Company myCompany, Users client) throws ApplicationException;

    List<Client> getAll(Company company) throws ApplicationException;

    List<Client> getAllCompanyClients(Company company) throws ApplicationException;

    List<Client> getAllPersonClients(Company company) throws ApplicationException;

    Client getSponsor(Company client) throws ApplicationException;
}
