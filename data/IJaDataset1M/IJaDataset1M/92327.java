package br.com.mcampos.ejb.cloudsystem.account.costcenter.session;

import br.com.mcampos.ejb.cloudsystem.account.costcenter.entity.CostArea;
import br.com.mcampos.ejb.cloudsystem.account.costcenter.entity.CostAreaPK;
import br.com.mcampos.ejb.cloudsystem.user.company.entity.Company;
import br.com.mcampos.ejb.cloudsystem.user.login.Login;
import br.com.mcampos.exception.ApplicationException;
import java.io.Serializable;
import java.util.List;
import javax.ejb.Local;

@Local
public interface CostAreaSessionLocal extends Serializable {

    void delete(Login login, CostAreaPK key) throws ApplicationException;

    void delete(Login login, Company company, Integer id) throws ApplicationException;

    CostArea get(CostAreaPK key) throws ApplicationException;

    CostArea get(Company company, Integer id) throws ApplicationException;

    List<CostArea> getAll(Company company) throws ApplicationException;

    CostArea add(Login login, CostArea entity) throws ApplicationException;

    CostArea update(CostArea entity) throws ApplicationException;

    Integer nextId(Company company) throws ApplicationException;
}
