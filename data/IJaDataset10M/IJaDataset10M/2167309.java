package org.ibit.avanthotel.persistence.logic.ejb.cmp.reserva;

import java.util.Date;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import org.apache.log4j.Category;
import org.ibit.j2eeUtils.jndi.ServiceLocatorException;
import org.ibit.avanthotel.persistence.logic.interfaces.cmp.util.GeneradorFacadeLocal;
import org.ibit.avanthotel.persistence.logic.util.LogicServiceLocator;

/**
 * Entity bean con la informacion de los periodos de oferta
 *
 * @created 27 de Enero de 2004
 * @ejb.data-object package="org.ibit.avanthotel.persistence.logic.data"
 *    implements="org.ibit.avanthotel.persistence.logic.data.DataObject"
 * @ejb.bean type="CMP"
 *    cmp-version="2.x"
 *    name="Oferta"
 *    local-jndi-name="${jndi.ejb.oferta}"
 *    view-type="local"
 *    schema="Oferta"
 *    primkey-field="id"
 * @ejb.pk class="java.lang.Integer"
 * @ejb.ejb-ref ejb-name="GeneradorFacade" view-type="local" ref-name="ejb/GeneradorFacade"
 * @ejb.persistence table-name="tava_ofertas"
 * @jboss.persistence create-table="${db.create}" remove-table="${db.delete}"
 */
public abstract class OfertaEJB implements EntityBean {

    /** */
    private static Category logger = Category.getInstance(OfertaEJB.class.getName());

    private EntityContext ctx;

    private GeneradorFacadeLocal generadorFacadeLocal;

    /**
    * @return
    * @exception CreateException
    * @ejb.create-method view-type="local"
    */
    public Integer ejbCreate() throws CreateException {
        setId(getGeneradorFacadeLocal().next());
        return null;
    }

    /** */
    public void ejbPostCreate() {
    }

    /**
    * @return valor de id
    * @ejb.interface-method view-type="local"
    * @ejb.persistent-field
    * @jboss.column-name n_id
    * @jboss.sql-type type="${db.type.int}"
    * @jboss.jdbc-type INTEGER
    */
    public abstract Integer getId();

    /**
    * fitxa el valor de id
    *
    * @param id nou valor per id
    */
    public abstract void setId(Integer id);

    /**
    * @return valor de dataInici
    * @ejb.interface-method view-type="local"
    * @ejb.persistent-field
    * @jboss.column-name d_inici
    * @jboss.sql-type type="${db.type.date}"
    * @jboss.jdbc-type DATE
    */
    public abstract Date getDataInici();

    /**
    * @param data nou valor per dataInici
    * @ejb.interface-method view-type="local"
    */
    public abstract void setDataInici(Date data);

    /**
    * @return valor de dataFi
    * @ejb.interface-method view-type="local"
    * @ejb.persistent-field
    * @jboss.column-name d_fi
    * @jboss.sql-type type="${db.type.date}"
    * @jboss.jdbc-type DATE
    */
    public abstract Date getDataFi();

    /**
    * @param data nou valor per dataFi
    * @ejb.interface-method view-type="local"
    */
    public abstract void setDataFi(Date data);

    /**
    * retorna el valor per la propietat generadorFacadeLocal
    *
    * @return valor de generadorFacadeLocal
    */
    private GeneradorFacadeLocal getGeneradorFacadeLocal() {
        if (generadorFacadeLocal == null) {
            try {
                generadorFacadeLocal = LogicServiceLocator.getGeneratorFacadeHome().create();
            } catch (CreateException e) {
                throw new EJBException(e);
            } catch (ServiceLocatorException e) {
                throw new EJBException(e);
            }
        }
        return generadorFacadeLocal;
    }
}
