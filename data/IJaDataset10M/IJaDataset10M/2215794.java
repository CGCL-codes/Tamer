package no.ugland.utransprod.dao;

import java.util.List;
import no.ugland.utransprod.model.ProductAreaGroup;
import no.ugland.utransprod.model.Transport;
import no.ugland.utransprod.service.enums.LazyLoadTransportEnum;
import no.ugland.utransprod.util.Periode;

/**
 * Interface for DAO mot tabell TRANSPORT
 * @author atle.brekka
 */
public interface TransportDAO extends DAO<Transport> {

    /**
     * Oppdaterer objekt
     * @param transport
     */
    void refreshObject(Transport transport);

    /**
     * Lazy laster transport
     * @param transport
     * @param enums
     */
    void lazyLoadTransport(Transport transport, LazyLoadTransportEnum[] enums);

    /**
     * Finner alle
     * @return transport
     */
    List<Transport> findAll();

    /**
     * Finner basert p� �r og uke
     * @param year
     * @param week
     * @return transport
     */
    List<Transport> findByYearAndWeek(Integer year, Integer week);

    /**
     * Finner transport mellom to uker
     * @param year
     * @param fromWeek
     * @param toWeek
     * @param orderBy
     * @return transport
     */
    List<Transport> findBetweenYearAndWeek(Integer year, Integer fromWeek, Integer toWeek, String[] orderBy);

    /**
     * Finner transporter som er i dag eller senere
     * @return transporter
     */
    List<Transport> findNewTransports();

    List<Transport> findByYearAndWeekAndProductAreaGroup(Integer year, Integer week, ProductAreaGroup productAreaGroup);

    List<Transport> findSentInPeriode(Periode periode);

    List<Transport> findInPeriode(Periode periode, String productAreaGroupName);
}
