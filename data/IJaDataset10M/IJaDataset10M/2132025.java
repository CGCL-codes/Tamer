package br.com.mcampos.ejb.session.system;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import br.com.mcampos.ejb.entity.system.SystemParameters;

@Stateless(name = "SystemParametersSession", mappedName = "CloudSystems-EjbPrj-SystemParametersSession")
public class SystemParametersSessionBean implements SystemParametersSessionLocal {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1945108545053728238L;

    @PersistenceContext(unitName = "EjbPrj")
    private EntityManager em;

    public SystemParametersSessionBean() {
    }

    /**
	 * Obtém o valor do parametro do sistema solicitado. Deve ser utilizado uma
	 * string com o código do parâmetro, que identifica um e somente um
	 * parâmetro do sistema. Este código está cadastrado na tabela
	 * SystemParemeters
	 * 
	 * @param id
	 *            Chave única na tabela de parâmetros do sistema
	 *            (SystemParameter)
	 * @return String com o valor do parâmetro solicitado ou null
	 */
    @Override
    public String getValue(String id) {
        SystemParameters systemParameter;
        systemParameter = em.find(SystemParameters.class, id);
        if (systemParameter != null) return systemParameter.getValue(); else return null;
    }
}
