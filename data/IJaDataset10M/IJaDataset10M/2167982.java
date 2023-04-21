package org.ibit.avanthotel.offer.logic.ejb;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import org.apache.log4j.Category;
import org.ibit.j2eeUtils.jndi.ServiceLocatorException;
import org.ibit.avanthotel.offer.logic.data.DescripcioData;
import org.ibit.avanthotel.offer.logic.data.DescripcionsData;
import org.ibit.avanthotel.offer.logic.data.DistanciaReferentData;
import org.ibit.avanthotel.offer.logic.data.DistanciesData;
import org.ibit.avanthotel.offer.logic.data.ImatgesData;
import org.ibit.avanthotel.offer.logic.data.InformacioBasica;
import org.ibit.avanthotel.offer.logic.data.ServeisAllotjamentData;
import org.ibit.avanthotel.offer.logic.interfaces.AllotjamentOfferFacade;
import org.ibit.avanthotel.offer.logic.util.OfferLogicServiceLocator;
import org.ibit.avanthotel.persistence.logic.data.BasicData;
import org.ibit.avanthotel.persistence.logic.data.DistanciaData;
import org.ibit.avanthotel.persistence.logic.data.allotjament.AllotjamentBasicData;
import org.ibit.avanthotel.persistence.logic.interfaces.cmp.allotjament.AllotjamentLocal;
import org.ibit.avanthotel.persistence.logic.interfaces.cmp.allotjament.AllotjamentLocalHome;
import org.ibit.avanthotel.persistence.logic.interfaces.cmp.allotjament.DistanciaLocalHome;
import org.ibit.avanthotel.persistence.logic.interfaces.cmp.allotjament.ImatgesLocal;
import org.ibit.avanthotel.persistence.logic.interfaces.cmp.allotjament.ImatgesLocalHome;
import org.ibit.avanthotel.persistence.logic.interfaces.cmp.allotjament.TraduccioAllotjamentLocal;
import org.ibit.avanthotel.persistence.logic.interfaces.cmp.allotjament.TraduccioAllotjamentLocalHome;
import org.ibit.avanthotel.persistence.logic.interfaces.cmp.reserva.ReservaLocal;
import org.ibit.avanthotel.persistence.logic.interfaces.cmp.reserva.ReservaLocalHome;
import org.ibit.avanthotel.persistence.logic.interfaces.cmp.standar.*;
import org.ibit.avanthotel.persistence.logic.interfaces.facade.StandarFacade;

/**
 * Session Bean amb les funcions possibles d'un hoteler sobre un allotjament assignat. <p>
 *
 * Un hoteler no pot realitzar qualsevol operacio sobre un dels seus hotels. Algunes de les operacions com son la
 * creacio o l'eliminacio les pot fer unicament l'administrador de l'aplicacio.
 *
 * @created 21 de enero de 2003
 * @ejb.bean type="Stateless"
 *      name="AllotjamentOfferFacade"
 *      jndi-name="${jndi.ejb.allotjamentOfferFacade}"
 *      view-type="remote"
 * @ejb.ejb-external-ref ref-name="ejb/Allotjament"
 *    view-type="local"
 *    type="Entity"
 *    link="persistence.jar#Allotjament"
 *    home="org.ibit.avanthotel.persistence.logic.interfaces.cmp.allotjament.AllotjamentLocalHome"
 *    business="org.ibit.avanthotel.persistence.logic.interfaces.cmp.allotjament.AllotjamentLocal"
 * @ejb.ejb-external-ref ref-name="ejb/Reserva"
 *    view-type="local"
 *    type="Entity"
 *    link="persistence.jar#Reserva"
 *    home="org.ibit.avanthotel.persistence.logic.interfaces.cmp.reserva.ReservaLocalHome"
 *    business="org.ibit.avanthotel.persistence.logic.interfaces.cmp.reserva.ReservaLocal"
 * @ejb.ejb-external-ref ref-name="ejb/TraduccioIdioma"
 *    view-type="local"
 *    type="Entity"
 *    link="persistence.jar#TraduccioIdioma"
 *    home="org.ibit.avanthotel.persistence.logic.interfaces.cmp.standar.TraduccioIdiomaLocalHome"
 *    business="org.ibit.avanthotel.persistence.logic.interfaces.cmp.standar.TraduccioIdiomaLocal"
 * @ejb.ejb-external-ref ref-name="ejb/Servei"
 *    view-type="local"
 *    type="Entity"
 *    link="persistence.jar#Servei"
 *    home="org.ibit.avanthotel.persistence.logic.interfaces.cmp.allotjament.TraduccioServeiLocalHome"
 *    business="org.ibit.avanthotel.persistence.logic.interfaces.cmp.allotjament.TraduccioServeiLocal"
 * @ejb.ejb-external-ref ref-name="ejb/TraduccioServei"
 *    view-type="local"
 *    type="Entity"
 *    link="persistence.jar#TraduccioServei"
 *    home="org.ibit.avanthotel.persistence.logic.interfaces.cmp.allotjament.TraduccioServeiLocalHome"
 *    business="org.ibit.avanthotel.persistence.logic.interfaces.cmp.allotjament.TraduccioServeiLocal"
 * @ejb.ejb-external-ref ref-name="ejb/TraduccioAllotjament"
 *    view-type="local"
 *    type="Entity"
 *    link="persistence.jar#TraduccioAllotjament"
 *    home="org.ibit.avanthotel.persistence.logic.interfaces.cmp.standar.TraduccioAllotjamentLocalHome"
 *    business="org.ibit.avanthotel.persistence.logic.interfaces.cmp.standar.TraduccioAllotjamentLocal"
 * @ejb.ejb-external-ref ref-name="ejb/TipusCategoria"
 *    view-type="local"
 *    type="Entity"
 *    link="persistence.jar#TipusCategoria"
 *    home="org.ibit.avanthotel.persistence.logic.interfaces.cmp.standar.TipusCategoriaLocalHome"
 *    business="org.ibit.avanthotel.persistence.logic.interfaces.cmp.standar.TipusCategoriaLocal"
 * @ejb.ejb-external-ref ref-name="ejb/Tipus"
 *    view-type="local"
 *    type="Entity"
 *    link="persistence.jar#Tipus"
 *    home="org.ibit.avanthotel.persistence.logic.interfaces.cmp.standar.TipusLocalHome"
 *    business="org.ibit.avanthotel.persistence.logic.interfaces.cmp.standar.TipusLocal"
 * @ejb.ejb-external-ref ref-name="ejb/Imatges"
 *    view-type="local"
 *    type="Entity"
 *    link="persistence.jar#Imatges"
 *    home="org.ibit.avanthotel.persistence.logic.interfaces.cmp.allotjament.ImatgeLocalHome"
 *    business="org.ibit.avanthotel.persistence.logic.interfaces.cmp.allotjament.ImatgeLocal"
 * @ejb.ejb-external-ref ref-name="ejb/Idioma"
 *    view-type="local"
 *    type="Entity"
 *    link="persistence.jar#Idioma"
 *    home="org.ibit.avanthotel.persistence.logic.interfaces.cmp.standar.IdiomaLocalHome"
 *    business="org.ibit.avanthotel.persistence.logic.interfaces.cmp.standar.IdiomaStandarLocal"
 * @ejb.ejb-external-ref ref-name="ejb/ReferentDistancia"
 *    view-type="local"
 *    type="Entity"
 *    link="persistence.jar#ReferentDistancia"
 *    home="org.ibit.avanthotel.persistence.logic.interfaces.cmp.standar.ReferentDistanciaHome"
 *    business="org.ibit.avanthotel.persistence.logic.interfaces.cmp.standar.ReferentDistanciaLocal"
 * @ejb.ejb-external-ref ref-name="ejb/UnitatDistancia"
 *    view-type="local"
 *    type="Entity"
 *    link="persistence.jar#UnitatDistancia"
 *    home="org.ibit.avanthotel.persistence.logic.interfaces.cmp.standar.UnitatDistanciaHome"
 *    business="org.ibit.avanthotel.persistence.logic.interfaces.cmp.standar.UnitatDistanciaLocal"
 * @ejb.ejb-external-ref ref-name="ejb/Distancia"
 *    view-type="local"
 *    type="Entity"
 *    link="persistence.jar#Distancia"
 *    home="org.ibit.avanthotel.persistence.logic.interfaces.cmp.allotjament.DistanciaLocalHome"
 *    business="org.ibit.avanthotel.persistence.logic.interfaces.cmp.allotjament.DistanciaLocal"
 * @ejb.ejb-external-ref ref-name="ejb/ZonaInterseccio"
 *    view-type="local"
 *    type="Entity"
 *    link="persistence.jar#ZonaInterseccio"
 *    home="org.ibit.avanthotel.persistence.logic.interfaces.cmp.allotjament.ZonaInterseccioHome"
 *    business="org.ibit.avanthotel.persistence.logic.interfaces.cmp.allotjament.ZonaInterseccioLocal"
 * @ejb.ejb-external-ref ref-name="ejb/StandarFacade"
 *    view-type="remote"
 *    type="Session"
 *    link="persistence.jar#StandarFacade"
 *    home="org.ibit.avanthotel.persistence.logic.interfaces.facade.StandarFacadeHome"
 *    business="org.ibit.avanthotel.persistence.logic.interfaces.facade.StandarFacade"
 * -------------------
 * ejb.ejb-external-ref ref-name="ejb/Usuari"
 *    view-type="local"
 *    type="Entity"
 *    link="persistence.jar#Usuari"
 *    home="org.ibit.avanthotel.persistence.logic.interfaces.cmp.standar.UsuariLocalHome"
 *    business="org.ibit.avanthotel.persistence.logic.interfaces.cmp.standar.UsuariLocal"
 */
public abstract class AllotjamentOfferFacadeEJB implements SessionBean {

    private Category logger;

    private AllotjamentLocalHome allotjamentHome;

    private StandarFacade standarFacadeHome;

    private ImatgesLocalHome imatgesHome;

    private ReferentDistanciaLocalHome referentsHome;

    private UnitatDistanciaLocalHome unitatsHome;

    private DistanciaLocalHome distanciesHome;

    private IdiomaLocalHome idiomaHome;

    private TraduccioIdiomaLocalHome traduccioIdiomaHome;

    private TipusCategoriaLocalHome tipusCategoriaHome;

    private TipusLocalHome tipusHome;

    private ZonaInterseccioLocalHome zonaInterseccioHome;

    private TraduccioServeiLocalHome traduccioServeiHome;

    private TraduccioAllotjamentLocalHome traduccioAllotjamentHome;

    private ServeiLocalHome serveiHome;

    private ReservaLocalHome reservaHome;

    private Map cacheAllotjaments;

    private SessionContext ctx;

    /**
    * @exception CreateException
    * @ejb.create-method view-type="remote"
    */
    public void ejbCreate() throws CreateException {
        logger = Category.getInstance(AllotjamentOfferFacadeEJB.class);
        logger.info("ejbCreate()");
        try {
            allotjamentHome = OfferLogicServiceLocator.getAllotjamentHome();
            standarFacadeHome = OfferLogicServiceLocator.getStandarFacade();
            imatgesHome = OfferLogicServiceLocator.getImatgeLocalHome();
            referentsHome = OfferLogicServiceLocator.getReferentDistanciaHome();
            unitatsHome = OfferLogicServiceLocator.getUnitatHome();
            distanciesHome = OfferLogicServiceLocator.getDistanciaHome();
            idiomaHome = OfferLogicServiceLocator.getIdiomaHome();
            traduccioIdiomaHome = OfferLogicServiceLocator.getTraduccioIdiomaHome();
            tipusCategoriaHome = OfferLogicServiceLocator.getTipusCategoriaHome();
            tipusHome = OfferLogicServiceLocator.getTipusHome();
            zonaInterseccioHome = OfferLogicServiceLocator.getZonaInterseccioHome();
            traduccioServeiHome = OfferLogicServiceLocator.getTraduccioServeiHome();
            traduccioAllotjamentHome = OfferLogicServiceLocator.getTraduccioAllotjamentHome();
            serveiHome = OfferLogicServiceLocator.getServeiHome();
            reservaHome = OfferLogicServiceLocator.getReservesLocalHome();
        } catch (ServiceLocatorException e) {
            throw new EJBException(e);
        } catch (RemoteException e) {
            throw new EJBException(e);
        } catch (CreateException e) {
            throw new EJBException(e);
        }
        cacheAllotjaments = Collections.synchronizedMap(new HashMap());
    }

    /** */
    public void ejbActivate() {
        logger = Category.getInstance(AllotjamentOfferFacadeEJB.class);
    }

    /** */
    public void ejbPassivate() {
        logger = null;
    }

    /**
    * fitxa el valor de sessionContext
    *
    * @param sessionContext nou valor per sessionContext
    */
    public void setSessionContext(SessionContext sessionContext) {
        ctx = sessionContext;
    }

    /**
    * Carrega les descripcions d'un allotjament. <br>
    * DescripcionsData conte una entrada per cada un dels idiomes donats d'alta. Per tant conte una entrada tambe pels
    * idiomes pels quals l'allotjament no te descripcio.
    *
    * @param allotjamentId
    * @return valor de descripcions
    * @exception FinderException
    */
    public DescripcionsData getDescripcions(Integer allotjamentId) throws FinderException {
        logger.info("inici getDescripcions");
        AllotjamentLocal local = getAllotjamentLocal(allotjamentId);
        DescripcionsData descripcions = new DescripcionsData();
        descripcions.carregaIdiomes(getIdiomes());
        descripcions.carregaDescripcions(local.getTraduccionsLocals());
        logger.info("fi getDescripcions");
        return descripcions;
    }

    /**
    * @param allotjament
    * @param idioma
    * @param canviaNotificades
    * @return valor de reservesNoNotificades
    * @exception FinderException
    * @ejb.transaction type="Required"
    * @ejb.interface-method
    */
    public Collection getReservesConfirmadesNoNotificades(Integer allotjament, String idioma, boolean canviaNotificades) throws FinderException {
        return getReservesNoNotificades(allotjament, "Y", idioma, canviaNotificades);
    }

    /**
    * @param allotjament
    * @param idioma
    * @param canviaNotificades
    * @return valor de reservesNoNotificades
    * @exception FinderException
    * @ejb.transaction type="Required"
    * @ejb.interface-method
    */
    public Collection getReservesAnuladesNoNotificades(Integer allotjament, String idioma, boolean canviaNotificades) throws FinderException {
        return getReservesNoNotificades(allotjament, "C", idioma, canviaNotificades);
    }

    /**
    * Modifica la informacio basica d'un allotjament. <p>
    *
    * L'allotjament que se modificara sera el que te com identificador data.getId().<br>
    * Nomes es modificaran les dades que son modificables per l'offer. Per tant, amb aquest metode mai es podran
    * modificar variables com son l'identificador o el nom.
    *
    * @param data
    * @exception FinderException
    * @ejb.interface-method view-type="remote"
    */
    public void update(AllotjamentBasicData data) throws FinderException {
        logger.info("inici update");
        AllotjamentLocal local = getAllotjamentLocal(data.getId());
        local.setNom(data.getNom());
        local.setDireccio(data.getDireccio());
        local.setCodiPostal(data.getCodiPostal());
        local.setTelefon(data.getTelefon());
        local.setFax(data.getFax());
        local.setCadena(data.getCadena());
        local.setEmail(data.getEmail());
        local.setUrl(data.getUrl());
        local.setVisible(data.getVisible());
        try {
            Integer idTipus = local.getTipusCategoria().getTipus().getId();
            local.setTipusCategoria(tipusCategoriaHome.findByTipusCategoria(idTipus, data.getIdCategoria()));
            local.setUbicacio(zonaInterseccioHome.findByMunicipiZona(data.getIdMunicipi(), data.getIdZona()));
        } catch (FinderException e) {
            logger.error(e);
            logger.error("tipus: " + data.getIdTipus() + ", categoria: " + data.getIdCategoria());
            logger.error("municipi: " + data.getIdMunicipi() + " zona : " + data.getIdZona());
            throw new FinderException("error a update allotjamentOfferFacade :" + e);
        }
        logger.info("fi update");
    }

    /**
    * Carrega la informacio minima d'un allotjament.
    *
    * @param allotjamentId
    * @return
    * @exception FinderException
    * @ejb.interface-method view-type="remote"
    */
    public BasicData loadMinima(Integer allotjamentId) throws FinderException {
        logger.info("loadMinima " + allotjamentId);
        return getAllotjamentLocal(allotjamentId).getMinData();
    }

    /**
    * @param allotjamentId
    * @return coleccio de ServeiAllotjamentData
    * @exception FinderException
    * @ejb.interface-method view-type="remote"
    */
    public Collection loadServeis(Integer allotjamentId) throws FinderException {
        logger.info("cercant els serveis per l'allotjament " + allotjamentId);
        ServeisAllotjamentData data = new ServeisAllotjamentData();
        data.carregaServeis(traduccioServeiHome.findByIdioma("es"));
        data.carregaServeisPropis(getAllotjamentLocal(allotjamentId).getServeisData());
        logger.info("fi cera serveis");
        return data.getServeis();
    }

    /**
    * @param data noves descripcions per l'allotjament
    * @param id
    * @exception FinderException
    * @exception CreateException
    * @ejb.interface-method view-type="remote"
    */
    public void updateDescripcions(Integer id, DescripcionsData data) throws FinderException, CreateException {
        logger.info("actualitzem les descripcions per l'allotjament " + id);
        AllotjamentLocal local = getAllotjamentLocal(id);
        Iterator iter = data.getEntrades().iterator();
        while (iter.hasNext()) {
            DescripcioData descripcioData = (DescripcioData) iter.next();
            String idiomaId = descripcioData.getIdIdioma();
            String descripcio = descripcioData.getDescripcio();
            logger.debug("entram la descripcio {" + idiomaId + "," + descripcio + "}");
            if (local.teTraduccio(idiomaId)) {
                logger.debug("la traduccio existeix: modifiquem la descripcio");
                TraduccioAllotjamentLocal traduccioLocal = traduccioAllotjamentHome.findByIdiomaIAllotjament(idiomaId, id);
                traduccioLocal.setDescripcio(descripcio);
            } else {
                if (descripcio != null && descripcio.length() != 0) {
                    logger.debug("la traduccio no existeix: en cream una de nova");
                    traduccioAllotjamentHome.create(local, idiomaHome.findByPrimaryKey(idiomaId), descripcio, null);
                }
            }
        }
        logger.info("fi updateDescripcions");
    }

    /**
    * @param id
    * @return
    * @exception FinderException
    * @exception RemoteException
    * @ejb.interface-method type="remote"
    */
    public DistanciesData loadDistancies(Integer id) throws FinderException, RemoteException {
        logger.info("cercant distancies per l'allotjament " + id);
        DistanciesData distancies = new DistanciesData();
        distancies.carregaEntrades(standarFacadeHome.getReferents());
        distancies.afegirEntrades(getAllotjamentLocal(id).getDistanciesLocals());
        logger.info("fi loadDistancies");
        return distancies;
    }

    /**
    * @param id
    * @param distancies
    * @exception FinderException
    * @exception CreateException
    * @exception RemoveException
    * @ejb.interface-method type="remote"
    */
    public void updateDistancies(Integer id, DistanciesData distancies) throws FinderException, CreateException, RemoveException {
        logger.info("modificant distancies per l'allotjament " + id);
        AllotjamentLocal allotjament = getAllotjamentLocal(id);
        logger.debug("esborrem primer les distancies per l'allotjament");
        allotjament.removeDistancies();
        logger.debug("comencem a donar les noves distancies d'alta");
        for (Iterator iterator = distancies.getEntrades().iterator(); iterator.hasNext(); ) {
            DistanciaReferentData data = (DistanciaReferentData) iterator.next();
            logger.debug("donat d'alta distancia " + data);
            DistanciaData distancia = new DistanciaData();
            distancia.setDistancia(data.getDistancia());
            distanciesHome.create(allotjament, referentsHome.findByPrimaryKey(data.getIdReferencia()), unitatsHome.findByPrimaryKey(data.getIdUnitats()), distancia);
        }
        logger.info("fi updateDistancies");
    }

    /**
    * @param allotjamentId
    * @param idioma
    * @return valor de informacioBasica
    * @exception FinderException
    * @ejb.interface-method type="remote"
    */
    public InformacioBasica getInformacioBasica(Integer allotjamentId, String idioma) throws FinderException {
        InformacioBasica info = new InformacioBasica();
        AllotjamentOfferFacade ejbObject = (AllotjamentOfferFacade) ctx.getEJBObject();
        try {
            info.setInfoBasica(allotjamentHome.findByPrimaryKey(allotjamentId).loadBasica());
            info.setArees(standarFacadeHome.getArees());
            info.setDistancies(ejbObject.loadDistancies(allotjamentId));
            info.setUnitats(standarFacadeHome.getUnitatsByIdioma(idioma));
            info.setTipus(tipusHome.getNomTipus(info.getInfoBasica().getIdTipus(), idioma));
            info.setCategories(standarFacadeHome.getCategories(info.getInfoBasica().getIdTipus(), idioma));
        } catch (RemoteException e) {
            throw new EJBException(e);
        }
        info.setDescripcions(getDescripcions(allotjamentId));
        return info;
    }

    /**
    * @param id
    * @param serveis
    * @exception FinderException
    * @ejb.interface-method view-type="remote"
    */
    public void updateServeis(Integer id, Integer[] serveis) throws FinderException {
        logger.debug("obtenim l'allotjament local amb id : " + id);
        AllotjamentLocal allotjament = getAllotjamentLocal(id);
        logger.debug("esborrem tots els serveis");
        allotjament.removeAllServeis();
        logger.debug("cercam el serveiHome per obtenir els serveis a partir de les claus passades");
        logger.debug("donem d'alta els serveis passats");
        for (int i = 0; i < serveis.length; i++) {
            logger.debug("alta del servei " + serveis[i]);
            ServeiLocal serveiLocal = serveiHome.findByPrimaryKey(serveis[i]);
            allotjament.addServei(serveiLocal);
        }
    }

    /**
    * @param idAllotjament
    * @return
    * @exception FinderException
    * @ejb.interface-method view-type="remote"
    */
    public Map loadMultimedia(Integer idAllotjament) throws FinderException {
        Map map = new HashMap();
        logger.info("Esteim a loadMultimedia");
        Iterator iter = imatgesHome.findByAllotjament(idAllotjament).iterator();
        while (iter.hasNext()) {
            ImatgesLocal imatgesLocal = (ImatgesLocal) iter.next();
            ImatgesData imatgesData = new ImatgesData(imatgesLocal.getId());
            map.put(imatgesLocal.getIndex(), imatgesData);
        }
        return map;
    }

    /**
    * @param idAllotjament
    * @param colImatges
    * @exception FinderException
    * @exception CreateException
    * @exception RemoveException
    * @ejb.interface-method view-type="remote"
    */
    public void updateMultimedia(Integer idAllotjament, Collection colImatges) throws FinderException, CreateException, RemoveException {
        logger.debug("Esteim a updateMultimedia. Han arribat " + colImatges.size() + " imatges");
        AllotjamentLocal allotjamentLocal = getAllotjamentLocal(idAllotjament);
        Iterator iter = colImatges.iterator();
        while (iter.hasNext()) {
            ImatgesData imatgesData = (ImatgesData) iter.next();
            byte[] image = null;
            if (imatgesData.getImatge() != null) {
                image = imatgesData.getImatge();
            }
            logger.debug("Enviamos la imagen con pos_vertical =  " + imatgesData.getPosVertical());
            imatgesHome.creaSiNoExisteix(imatgesData.getId(), image, imatgesData.getPosVertical(), allotjamentLocal, imatgesData.isEsborrar());
        }
    }

    /**
    * retorna el valor per la propietat reservesNoNotificades
    *
    * @param allotjament
    * @param estat
    * @param idioma
    * @param canviaNotificades
    * @return valor de reservesNoNotificades
    * @exception FinderException
    */
    private Collection getReservesNoNotificades(Integer allotjament, String estat, String idioma, boolean canviaNotificades) throws FinderException {
        Collection reservesLocals = reservaHome.findReservesNoNotificadesPerAllotjament(allotjament, estat);
        Collection reserves = new Vector(reservesLocals.size());
        for (Iterator iterator = reservesLocals.iterator(); iterator.hasNext(); ) {
            ReservaLocal reservaLocal = (ReservaLocal) iterator.next();
            reserves.add(reservaLocal.getReservaData(idioma));
            if (canviaNotificades) {
                reservaLocal.setNotificada(true);
            }
        }
        return reserves;
    }

    /**
    * Obte l'allotjament amb l'identificador especificat.
    *
    * @param id
    * @return
    * @exception FinderException
    * @exception EJBException
    */
    private AllotjamentLocal getAllotjamentLocal(Integer id) throws FinderException, EJBException {
        AllotjamentLocal local = (AllotjamentLocal) cacheAllotjaments.get(id);
        if (local == null) {
            local = allotjamentHome.findByPrimaryKey(id);
            cacheAllotjaments.put(id, local);
        }
        return local;
    }

    /**
    * retorna una llista de TraduccioIdiomaData
    *
    * @return
    * @exception FinderException
    */
    private Collection getIdiomes() throws FinderException {
        Collection elementsData = new java.util.Vector();
        Iterator elementsEJB = traduccioIdiomaHome.findByTraduitEn("es").iterator();
        while (elementsEJB.hasNext()) {
            TraduccioIdiomaLocal element = (TraduccioIdiomaLocal) elementsEJB.next();
            elementsData.add(element.getData());
        }
        return elementsData;
    }
}
