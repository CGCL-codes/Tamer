package net.sourceforge.traffiscope.entity.jpa;

import net.sourceforge.traffiscope.model.DatabaseRemote;
import net.sourceforge.traffiscope.model.xml.ConfigXO;
import net.sourceforge.traffiscope.model.xml.DayXO;
import net.sourceforge.traffiscope.model.xml.GangXO;
import net.sourceforge.traffiscope.model.xml.GaugeXO;
import net.sourceforge.traffiscope.model.xml.QuantityXO;
import net.sourceforge.traffiscope.model.xml.SampleXO;
import net.sourceforge.traffiscope.model.xml.ServerXO;
import net.sourceforge.traffiscope.model.xml.SummaryXO;
import net.sourceforge.traffiscope.model.xml.TraffiscopeXO;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJBException;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.xml.bind.JAXBException;

@Stateless
@Remote(DatabaseRemote.class)
public class DatabaseBean extends AbstractJPASessionBean implements DatabaseRemote {

    @Override
    public void backup(File file) throws IOException {
        Writer output = new FileWriter(file);
        try {
            TraffiscopeXO xTraffiscope = new TraffiscopeXO();
            backup(xTraffiscope);
            TraffiscopeXO.marshal(xTraffiscope, output);
        } catch (JAXBException x) {
            throw new EJBException(x);
        } finally {
            output.close();
        }
    }

    @Override
    public void restore(URL location) throws IOException {
        TraffiscopeXO traffiscope;
        InputStream input = location.openStream();
        try {
            traffiscope = TraffiscopeXO.unmarshal(input);
        } catch (JAXBException x) {
            throw new IOException(x);
        } finally {
            input.close();
        }
        getTransaction().begin();
        try {
            persist(traffiscope);
        } catch (RuntimeException x) {
            getTransaction().rollback();
            throw x;
        }
        getTransaction().commit();
    }

    protected Config getConfigEntity() {
        Query q = entityManager.createNamedQuery(Config.QUERY_GET_CONFIG);
        try {
            return (Config.class.cast(q.getSingleResult()));
        } catch (NoResultException x) {
            return null;
        }
    }

    protected void backup(TraffiscopeXO xTraffiscope) {
        Map<Object, Object> idmap = new HashMap<Object, Object>();
        ConfigXO xConfig = new ConfigXO();
        getConfigEntity().writeTO(xConfig);
        xTraffiscope.setConfig(xConfig);
        Query query;
        query = entityManager.createNamedQuery(Quantity.QUERY_LIST);
        for (Object o : query.getResultList()) {
            QuantityXO xQuantity = new QuantityXO();
            ((Quantity) o).writeTO(xQuantity);
            xTraffiscope.getQuantities().add(xQuantity);
            idmap.put(xQuantity.getId(), xQuantity);
        }
        query = entityManager.createNamedQuery(Gang.QUERY_LIST);
        for (Object o : query.getResultList()) {
            GangXO xGang = new GangXO();
            ((Gang) o).writeTO(xGang);
            for (Server server : ((Gang) o).getServers()) {
                ServerXO xServer = new ServerXO();
                server.writeTO(xServer);
                for (Gauge gauge : server.getGauges()) {
                    GaugeXO xGauge = new GaugeXO();
                    gauge.writeTO(xGauge);
                    xGauge.setServer(xServer);
                    xGauge.setQuantity((QuantityXO) idmap.get(xGauge.getQuantityId()));
                    xServer.getGauges().add(xGauge);
                    idmap.put(xGauge.getId(), xGauge);
                }
                xGang.getServers().add(xServer);
            }
            xTraffiscope.getGangs().add(xGang);
        }
        query = entityManager.createNamedQuery(Day.QUERY_LIST);
        for (Object o : query.getResultList()) {
            DayXO xDay = new DayXO();
            ((Day) o).writeTO(xDay);
            xTraffiscope.getDays().add(xDay);
            idmap.put(xDay.getDate(), xDay);
        }
        query = entityManager.createNamedQuery(Summary.QUERY_LIST);
        for (Object o : query.getResultList()) {
            SummaryXO xSummary = new SummaryXO();
            ((Summary) o).writeTO(xSummary);
            xSummary.setTimestamp(xSummary.getTimestamp());
            xSummary.setGauge((GaugeXO) idmap.get(xSummary.getGaugeId()));
            xTraffiscope.getSummaries().add(xSummary);
        }
        query = entityManager.createNamedQuery(Sample.QUERY_LIST);
        for (Object o : query.getResultList()) {
            SampleXO xSample = new SampleXO();
            ((Sample) o).writeTO(xSample);
            xSample.setGauge((GaugeXO) idmap.get(xSample.getGaugeId()));
            xTraffiscope.getSamples().add(xSample);
        }
    }

    protected void persist(TraffiscopeXO xTraffiscope) {
        Map<Integer, Object> idmap = new HashMap<Integer, Object>();
        Config config = new Config();
        config.readTO(xTraffiscope.getConfig());
        entityManager.persist(config);
        for (QuantityXO xQuantity : xTraffiscope.getQuantities()) {
            Quantity quantity = new Quantity();
            quantity.readTO(xQuantity);
            entityManager.persist(quantity);
            idmap.put(xQuantity.getId(), quantity);
        }
        for (GangXO xGang : xTraffiscope.getGangs()) {
            Gang gang = new Gang();
            gang.readTO(xGang);
            entityManager.persist(gang);
            idmap.put(xGang.getId(), gang);
            for (ServerXO xServer : xGang.getServers()) {
                Server server = new Server();
                server.readTO(xServer);
                server.setGang(gang);
                entityManager.persist(server);
                idmap.put(xServer.getId(), server);
                for (GaugeXO xGauge : xServer.getGauges()) {
                    Gauge gauge = new Gauge();
                    gauge.readTO(xGauge);
                    gauge.setServer(server);
                    gauge.setQuantity((Quantity) idmap.get(xGauge.getQuantityId()));
                    entityManager.persist(gauge);
                    idmap.put(xGauge.getId(), gauge);
                }
            }
        }
        for (DayXO xDay : xTraffiscope.getDays()) {
            Day day = new Day();
            day.readTO(xDay);
            day.setDate(xDay.getDate());
            entityManager.persist(day);
        }
        for (SampleXO xSample : xTraffiscope.getSamples()) {
            Sample sample = new Sample();
            sample.readTO(xSample);
            sample.setGauge((Gauge) idmap.get(xSample.getGaugeId()));
            entityManager.persist(sample);
        }
        for (SummaryXO xSummary : xTraffiscope.getSummaries()) {
            Summary summary = new Summary();
            summary.readTO(xSummary);
            summary.setGauge((Gauge) idmap.get(xSummary.getGaugeId()));
            entityManager.persist(summary);
        }
    }
}
