package org.ibit.avanthotel.persistence.logic.ejb.cmp.allotjament;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;
import org.apache.log4j.Category;
import org.ibit.j2eeUtils.jndi.ServiceLocatorException;
import org.ibit.avanthotel.persistence.logic.data.TraduccioAllotjamentData;
import org.ibit.avanthotel.persistence.logic.interfaces.cmp.allotjament.AllotjamentLocal;
import org.ibit.avanthotel.persistence.logic.interfaces.cmp.standar.IdiomaLocal;
import org.ibit.avanthotel.persistence.logic.interfaces.cmp.util.GeneradorFacadeLocal;
import org.ibit.avanthotel.persistence.logic.util.LogicServiceLocator;

/**
 * @created 23 de junio de 2003
 * @ejb.data-object package="org.ibit.avanthotel.persistence.logic.data"
 * @ejb.bean type="CMP"
 *    cmp-version="2.x"
 *    name="TraduccioAllotjament"
 *    local-jndi-name="${jndi.ejb.allotjamentTraduccio}"
 *    view-type="local"
 *    schema="TraduccioAllotjament"
 *    primkey-field="id"
 * @ejb.pk class="java.lang.Integer"
 * @ejb.finder signature="org.ibit.avanthotel.persistence.logic.interfaces.cmp.allotjament.TraduccioAllotjamentLocal findByIdiomaIAllotjament (java.lang.String idiomaId, java.lang.Integer allotjamentId)"
 *    query="SELECT OBJECT(o) FROM TraduccioAllotjament o WHERE o.idioma.id=?1 AND o.allotjament.id=?2"
 * @ejb.ejb-ref ejb-name="GeneradorFacade" view-type="local" ref-name="ejb/GeneradorFacade"
 * @ejb.persistence table-name="tava_acc_trans"
 * @jboss.persistence create-table="${db.create}" remove-table="${db.delete}"
 */
public abstract class TraduccioAllotjamentEJB implements EntityBean {

    /** */
    private static Category logger = Category.getInstance(TraduccioAllotjamentEJB.class.getName());

    /** */
    private EntityContext ctx;

    private GeneradorFacadeLocal generadorFacadeLocal;

    /**
    * @param allotjament
    * @param idioma
    * @param descripcio
    * @param condition
    * @return
    * @exception CreateException
    * @ejb.create-method view-type="local"
    */
    public Integer ejbCreate(AllotjamentLocal allotjament, IdiomaLocal idioma, String descripcio, String condition) throws CreateException {
        logger.debug("ejbCreate (" + allotjament.getId() + "," + idioma.getId() + "," + descripcio + "," + condition + ")");
        setId(getGeneradorFacadeLocal().next());
        setDescripcio(descripcio);
        setCondition(condition);
        return null;
    }

    /**
    * @param allotjament
    * @param idioma
    * @param descripcio
    * @param condition
    */
    public void ejbPostCreate(AllotjamentLocal allotjament, IdiomaLocal idioma, String descripcio, String condition) {
        setAllotjament(allotjament);
        setIdioma(idioma);
    }

    /**
    * fitxa el valor de entityContext
    *
    * @param ctx nou valor per entityContext
    */
    public void setEntityContext(EntityContext ctx) {
        this.ctx = ctx;
    }

    /** */
    public void unsetEntityContext() {
        ctx = null;
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
    * @return valor de idioma
    * @ejb.interface-method view-type="local"
    * @ejb.relation name="AllotjamentTraduccio-Idioma"
    *    role-name="traduccio-per-idioma"
    *    target-ejb="Idioma"
    *    target-role-name="idioma-de-traduccio"
    *    target-multiple="yes"
    * @jboss.relation fk-constraint="${db.constraints}"
    *    related-pk-field="id"
    *    fk-column="lang_v_id"
    */
    public abstract IdiomaLocal getIdioma();

    /**
    * fitxa el valor de idioma
    *
    * @param idioma nou valor per idioma
    */
    public abstract void setIdioma(IdiomaLocal idioma);

    /**
    * @return valor de allotjament
    * @ejb.interface-method view-type="local"
    * @ejb.relation name="Allotjament-Traduccions"
    *    role-name="traduccions-de-allotjament"
    *    cascade-delete="yes"
    *    target-multiple="yes"
    * @jboss.relation fk-constraint="${db.constraints}"
    *    related-pk-field="id"
    *    fk-column="acc_n_id"
    */
    public abstract AllotjamentLocal getAllotjament();

    /**
    * fitxa el valor de allotjament
    *
    * @param allotjament nou valor per allotjament
    */
    public abstract void setAllotjament(AllotjamentLocal allotjament);

    /**
    * @return valor de descripcio
    * @ejb.interface-method view-type="local"
    * @ejb.persistent-field
    * @jboss.column-name v_desc
    * @jboss.sql-type type="${db.type.mediumString}"
    * @jboss.jdbc-type VARCHAR
    */
    public abstract String getDescripcio();

    /**
    * @param descripcio nou valor per descripcio
    * @ejb.interface-method view-type="local"
    */
    public abstract void setDescripcio(String descripcio);

    /**
    * @return valor de condition
    * @ejb.interface-method view-type="local"
    * @ejb.persistent-field
    * @jboss.column-name v_condition
    * @jboss.sql-type type="${db.type.mediumString}"
    * @jboss.jdbc-type VARCHAR
    */
    public abstract String getCondition();

    /**
    * @param condition nou valor per condition
    * @ejb.interface-method view-type="local"
    */
    public abstract void setCondition(String condition);

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
