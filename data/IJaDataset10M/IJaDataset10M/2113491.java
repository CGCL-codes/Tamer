package fr.xebia.demo.objectgrid.ticketing.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import junit.framework.TestCase;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import com.ibm.websphere.objectgrid.ClientClusterContext;
import com.ibm.websphere.objectgrid.ObjectGrid;
import com.ibm.websphere.objectgrid.ObjectGridException;
import com.ibm.websphere.objectgrid.ObjectGridManager;
import com.ibm.websphere.objectgrid.ObjectGridManagerFactory;
import com.ibm.websphere.objectgrid.Session;
import com.ibm.websphere.objectgrid.em.EntityManager;
import com.ibm.websphere.objectgrid.em.Query;
import com.ibm.websphere.projector.Tuple;
import com.ibm.websphere.projector.md.TupleAssociation;
import com.ibm.websphere.projector.md.TupleAttribute;
import com.ibm.websphere.projector.md.TupleMetadata;
import fr.xebia.demo.objectgrid.ticketing.Seat;
import fr.xebia.demo.objectgrid.ticketing.Train;
import fr.xebia.demo.objectgrid.ticketing.TrainStop;
import fr.xebia.demo.objectgrid.ticketing.Train.Type;

/**
 * @author <a href="mailto:cyrille.leclerc@pobox.com">Cyrille Le Clerc</a>
 */
public abstract class AbstractTicketingGridTest extends TestCase {

    private static final Logger logger = Logger.getLogger(AbstractTicketingGridTest.class);

    protected static final String PARIS_GARE_DE_LYON = "paris-gare-de-lyon";

    protected static final String AVIGNON_TGV = "avignon-tgv";

    protected static final String MARSEILLE_SAINT_CHARLES = "marseille-saint-charles";

    protected ObjectGrid objectGrid;

    protected List<Integer> persistedTrainIds = new ArrayList<Integer>();

    protected void loadData(EntityManager entityManager) {
        Random random = new Random();
        Date today = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
        for (int hour = 6; hour <= 24; hour++) {
            Train train = new Train(random.nextInt(), "PARIS-MARSEILLE-" + hour, Type.HIGH_SPEED);
            train.getTrainStops().add(new TrainStop(random.nextInt(), DateUtils.addHours(today, hour), PARIS_GARE_DE_LYON));
            train.getTrainStops().add(new TrainStop(random.nextInt(), DateUtils.addHours(today, 2 + hour), AVIGNON_TGV));
            train.getTrainStops().add(new TrainStop(random.nextInt(), DateUtils.addHours(today, 3 + hour), MARSEILLE_SAINT_CHARLES));
            for (int seatNumber = 1; seatNumber <= 10; seatNumber++) {
                train.getSeats().add(new Seat(random.nextInt(), seatNumber, random.nextBoolean(), 100 + random.nextInt(100)));
            }
            logger.info("persist " + train);
            entityManager.getTransaction().begin();
            entityManager.persist(train);
            entityManager.getTransaction().commit();
            persistedTrainIds.add(train.getId());
        }
        for (int hour = 6; hour <= 24; hour++) {
            Train train = new Train(random.nextInt(), "MARSEILLE-PARIS-" + hour, Type.HIGH_SPEED);
            train.getTrainStops().add(new TrainStop(random.nextInt(), DateUtils.addHours(today, hour), MARSEILLE_SAINT_CHARLES));
            train.getTrainStops().add(new TrainStop(random.nextInt(), DateUtils.addHours(today, 1 + hour), AVIGNON_TGV));
            train.getTrainStops().add(new TrainStop(random.nextInt(), DateUtils.addHours(today, 3 + hour), PARIS_GARE_DE_LYON));
            for (int seatNumber = 1; seatNumber <= 10; seatNumber++) {
                train.getSeats().add(new Seat(random.nextInt(), seatNumber, random.nextBoolean(), 200 + random.nextInt(100)));
            }
            logger.info("persist " + train);
            entityManager.getTransaction().begin();
            entityManager.persist(train);
            entityManager.getTransaction().commit();
            persistedTrainIds.add(train.getId());
        }
    }

    public ObjectGrid loadObjectGrid() throws ObjectGridException {
        ObjectGridManager objectGridManager = ObjectGridManagerFactory.getObjectGridManager();
        objectGridManager.setTraceEnabled(true);
        objectGridManager.setTraceSpecification("ObjectGrid=all=enabled");
        objectGridManager.setTraceFileName("traceObjectGrid.log");
        ObjectGrid result;
        boolean useLocalGrid = false;
        if (useLocalGrid) {
            logger.info("Use local ObjectGrid");
            result = objectGridManager.createObjectGrid();
            result.registerEntities(new Class[] { Train.class, TrainStop.class, Seat.class });
        } else {
            logger.info("Use distributed ObjectGrid");
            String catalogServerAddresses = "localhost:2809";
            ClientClusterContext clientClusterContext = objectGridManager.connect(catalogServerAddresses, null, null);
            result = objectGridManager.getObjectGrid(clientClusterContext, "ticketingGrid");
        }
        return result;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.objectGrid = loadObjectGrid();
        Session session = objectGrid.getSession();
        EntityManager entityManager = session.getEntityManager();
        loadData(entityManager);
    }

    public void dumpTrains(List<Train> trains) {
        for (Train train : trains) {
            dumpTrain(train);
        }
    }

    public void dumpTrain(Train train) {
        System.out.println(train);
        System.out.println("\tStops:");
        for (TrainStop trainStop : train.getTrainStops()) {
            System.out.println("\t\t" + trainStop);
        }
        System.out.println("\tSeats : available=" + train.getAvailableSeatsCount() + ", total=" + train.getTotalSeatsCount());
    }

    public void dumpTuple(Tuple tuple) {
        TupleMetadata tupleMetadata = tuple.getMetadata();
        System.out.println("\tATTRIBUTES");
        for (int i = 0; i < tupleMetadata.getNumAttributes(); i++) {
            TupleAttribute tupleAttribute = tupleMetadata.getAttribute(i);
            System.out.println("\t\tname=" + tupleAttribute.getName() + ", type=" + tupleAttribute.getType() + ", alias=" + tupleAttribute.getAlias() + ", VALUE=" + tuple.getAttribute(i));
        }
        System.out.println("\tASSOCIATIONS");
        for (int i = 0; i < tupleMetadata.getNumAssociations(); i++) {
            TupleAssociation tupleAssociation = tupleMetadata.getAssociation(i);
            System.out.println("\t\tname=" + tupleAssociation.getName() + ", targetEntity=" + tupleAssociation.getTargetEntityName() + ", type=" + tupleAssociation.getType() + ", targetEntityMetadata=" + tupleAssociation.getTargetEntityMetadata());
        }
    }
}
