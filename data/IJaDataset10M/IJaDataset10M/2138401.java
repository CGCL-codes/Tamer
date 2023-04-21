package org.ibit.avanthotel.persistence.logic.ejb.cmp.standar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import org.apache.log4j.Category;
import org.ibit.j2eeUtils.jndi.ServiceLocatorException;
import org.ibit.avanthotel.persistence.logic.data.BasicData;
import org.ibit.avanthotel.persistence.logic.data.standar.TipusHabitData;
import org.ibit.avanthotel.persistence.logic.interfaces.cmp.standar.TipusHabitacioStandarLocal;
import org.ibit.avanthotel.persistence.logic.interfaces.cmp.standar.TipusHabitacioStandarLocalHome;
import org.ibit.avanthotel.persistence.logic.interfaces.cmp.standar.TipusLocal;
import org.ibit.avanthotel.persistence.logic.interfaces.cmp.standar.TraduccioTipusHabitacioStandarLocal;
import org.ibit.avanthotel.persistence.logic.interfaces.cmp.standar.TraduccioTipusHabitacioStandarLocalHome;
import org.ibit.avanthotel.persistence.logic.util.LogicServiceLocator;

/**
 * Entity bean amb la informacio dels tipus d'habitacio estandar
 * Els tipus d'habitacio standar son compartits per tots els hotelers i gestionats per l'administrador.
 *
 * @created 7 de octubre de 2003
 * @ejb.data-object package="org.ibit.avanthotel.persistence.logic.data"
 *    implements="org.ibit.avanthotel.persistence.logic.data.DataObject"
 * @ejb.bean type="CMP"
 *    cmp-version="2.x"
 *    name="TipusHabitacioStandar"
 *    local-jndi-name="${jndi.ejb.tipusHabitacioStandar}"
 *    view-type="local"
 *    schema="TipusHabitacioStandar"
 *    primkey-field="id"
 * @ejb.pk class="java.lang.Integer"
 * @ejb.finder signature="java.util.Collection findAll()"
 *      query="SELECT OBJECT(a) FROM TipusHabitacioStandar a"
 * @jboss.query signature="java.util.Collection findAll()"
 *      query="SELECT OBJECT(a) FROM TipusHabitacioStandar a ORDER BY a.seq"
 * @ejb.finder signature="java.util.Collection findByTipus(java.lang.Integer tipus)"
 *      query="SELECT OBJECT(th) FROM TipusHabitacioStandar th, IN(th.tipus) ta WHERE ta.id = ?1"
 * @ejb.ejb-ref ejb-name="GeneradorFacade" view-type="local"
 * @ejb.ejb-ref ejb-name="TraduccioTipusHabitacioStandar" view-type="local"
 * @jboss.read-only read-only="true"
 * @ejb.persistence table-name="tava_standar_room"
 * @jboss.persistence create-table="${db.create}" remove-table="${db.delete}"
 */
public abstract class TipusHabitacioStandarEJB implements EntityBean {

    /** */
    private static Category logger = Category.getInstance(TipusHabitacioStandarEJB.class.getName());

    /** */
    private EntityContext ctx;

    private TraduccioTipusHabitacioStandarLocalHome traduccioTipusHabitacioStandarHome;

    /**
    * crea el tipusHabitacioStandar
    *
    * @return
    * @exception CreateException
    * @ejb.create-method view-type="local"
    */
    public Integer ejbCreate() throws CreateException {
        try {
            setId(LogicServiceLocator.getGeneratorFacadeHome().create().next());
        } catch (ServiceLocatorException e) {
            throw new EJBException(e);
        }
        return null;
    }

    /** */
    public void ejbPostCreate() {
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
    * @return valor de seq
    * @ejb.interface-method view-type="local"
    * @ejb.persistent-field
    * @jboss.column-name n_seq
    * @jboss.sql-type type="${db.type.int}"
    * @jboss.jdbc-type INTEGER
    */
    public abstract Integer getSeq();

    /**
    * fitxa el valor de seq
    *
    * @param seq nou valor per seq
    */
    public abstract void setSeq(Integer seq);

    /**
    * @return valor de traduccions
    * @ejb.relation name="TipusHabitacioStandar-TraduccioTipusHabitacioStandar"
    *    role-name="tipusHabitacioStandar-que-es-tradueix"
    */
    public abstract Collection getTraduccions();

    /**
    * fitxa el valor de traduccions
    *
    * @param traduccions nou valor per traduccions
    */
    public abstract void setTraduccions(Collection traduccions);

    /**
    * Obte les {@link TraduccioTipusHabitacioStandarLocal traduccions} pel tipus d'habitacio
    *
    * @return valor de traduccionsLocals
    * @ejb.interface-method view-type="local"
    */
    public Collection getTraduccionsLocals() {
        return new java.util.Vector(getTraduccions());
    }

    /**
    * Retorna la {@link TraduccioTipusHabitacioStandarLocal traduccio} especificada
    *
    * @param idioma {@link org.ibit.avanthotel.persistence.logic.interfaces.cmp.standar.IdiomaLocal#getId codi} del idioma cercat
    * @return valor de traduccio
    * @exception ObjectNotFoundException
    * @ejb.interface-method view-type="local"
    */
    public TraduccioTipusHabitacioStandarLocal getTraduccio(String idioma) throws ObjectNotFoundException {
        logger.debug("getTraduccio( " + idioma + ")");
        Iterator iter = getTraduccions().iterator();
        while (iter.hasNext()) {
            TraduccioTipusHabitacioStandarLocal traduccio = (TraduccioTipusHabitacioStandarLocal) iter.next();
            if (traduccio.getIdioma().getId().equals(idioma)) {
                logger.debug("la traduccio existeix");
                return traduccio;
            }
        }
        logger.debug("la traduccio no existeix");
        throw new ObjectNotFoundException("no existeix traduccio en " + idioma + " per allotjament " + getId());
    }

    /**
    * @param idioma
    * @param tipus
    * @return
    * @ejb.home-method
    * @throws FinderException
    */
    public BasicData[] ejbHomeGetTraduccionsData(String idioma, Integer tipus) throws FinderException {
        Collection allLocals = ((TipusHabitacioStandarLocalHome) ctx.getEJBLocalHome()).findByTipus(tipus);
        BasicData[] traduccions = new BasicData[allLocals.size()];
        int i = 0;
        Iterator allLocalsIter = allLocals.iterator();
        while (allLocalsIter.hasNext()) {
            TipusHabitacioStandarLocal tipusHabitacioLocal = (TipusHabitacioStandarLocal) allLocalsIter.next();
            traduccions[i++] = tipusHabitacioLocal.getBasicData(idioma);
        }
        return traduccions;
    }

    /**
    * @param idioma amb el que volem la traduccio
    * @return basic data amb l'identificador i el nom traduit
    * @ejb.interface-method
    */
    public BasicData getBasicData(String idioma) {
        String nom;
        try {
            nom = getTraduccio(idioma).getNom();
        } catch (FinderException e) {
            nom = "sin nombre";
        }
        return new BasicData(getId(), nom);
    }

    /**
    * @return valor de tipus
    * @ejb.relation name="Habitacio-Tipus"
    *    role-name="tipus-de-habitacio-standar"
    *    target-multiple="yes"
    * @jboss.relation-table table-name="tava_standar_room_types"
    *    create-table="${db.create}"
    *    remove-table="${db.delete}"
    *    pk-constraint="true"
    * @jboss.relation related-pk-field="id"
    *    fk-column="typ_n_id"
    */
    public abstract Set getTipus();

    /**
    * @param tipus nou valor per tipus
    * @ejb.interface-method
    */
    public abstract void setTipus(Set tipus);

    /**
    * @return valor de tipusLocals
    * @ejb.interface-method
    */
    public Collection getTipusLocals() {
        return new Vector(getTipus());
    }

    /**
    * @param idioma
    * @return
    * @exception FinderException
    * @ejb.home-method
    */
    public Collection ejbHomeGetHabitacionsStandarITipus(String idioma) throws FinderException {
        Collection totes = ((TipusHabitacioStandarLocalHome) ctx.getEJBLocalHome()).findAll();
        Collection resultat = new ArrayList();
        for (Iterator iterator = totes.iterator(); iterator.hasNext(); ) {
            TipusHabitacioStandarLocal tipusHabitacioStandarLocal = (TipusHabitacioStandarLocal) iterator.next();
            Integer id = tipusHabitacioStandarLocal.getId();
            String nom = getTraduccioTipusHabitacioStandarHome().findByTipusHabitacioStandarIIdioma(id, idioma).getNom();
            for (Iterator iterTipus = tipusHabitacioStandarLocal.getTipusLocals().iterator(); iterTipus.hasNext(); ) {
                TipusLocal tipusLocal = (TipusLocal) iterTipus.next();
                resultat.add(new TipusHabitData(id, nom, tipusLocal.getId()));
            }
        }
        return resultat;
    }

    /**
    * retorna el valor per la propietat traduccioTipusHabitacioStandarHabitacionsLocalHome
    *
    * @return valor de traduccioTipusHabitacioStandarHabitacionsLocalHome
    */
    private TraduccioTipusHabitacioStandarLocalHome getTraduccioTipusHabitacioStandarHome() {
        if (traduccioTipusHabitacioStandarHome == null) {
            try {
                traduccioTipusHabitacioStandarHome = LogicServiceLocator.getTraduccioTipusHabitacioStandarHabitacionsLocalHome();
            } catch (ServiceLocatorException e) {
                logger.error("problema", e);
                throw new EJBException(e);
            }
        }
        return traduccioTipusHabitacioStandarHome;
    }
}
