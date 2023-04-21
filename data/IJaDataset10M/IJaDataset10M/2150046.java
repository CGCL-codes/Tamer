package com.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import com.model.*;

/**
 * What should this be called? DAO?
 * @author Haladria
 *
 */
public class ServiceProvider {

    public static final String databaseName1 = "MySQL";

    public static final String databaseName2 = "Oracle1";

    public static final String databaseName3 = "Oracle2";

    public List<String> databaseNames = new ArrayList<String>(3);

    private Map<String, EntityManagerFactory> entityManagerFactories = new HashMap<String, EntityManagerFactory>(3);

    public ServiceProvider() {
        databaseNames.add(databaseName1);
        databaseNames.add(databaseName2);
        databaseNames.add(databaseName3);
        for (String databaseName : databaseNames) {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory(databaseName);
            entityManagerFactories.put(databaseName, emf);
        }
    }

    public EntityManagerFactory getFactoryInstance(String databaseName) {
        EntityManagerFactory emf;
        emf = entityManagerFactories.get(databaseName);
        if (emf == null) {
            System.out.println("ERROR: " + databaseName + " is NULL!!");
        }
        return emf;
    }

    public Flight getFlight(String flightNumber) {
        Flight flight = null;
        for (EntityManagerFactory emf : entityManagerFactories.values()) {
            EntityManager newEm = emf.createEntityManager();
            EntityTransaction newTx = newEm.getTransaction();
            newTx.begin();
            flight = newEm.find(Flight.class, flightNumber);
            if (flight != null) break;
            newTx.commit();
            newEm.close();
        }
        return flight;
    }

    /**
	 * This method returns all flights in the database, happy parsing :)
	 * @param departureCity
	 * @param destinationCity
	 * @param flightDate
	 * @param numberOfPassengers
	 * @return
	 */
    public List<Flight> searchFlights(String departureCity, String destinationCity, Date flightDate, int numberOfPassengers) {
        List<Flight> allFlights = new ArrayList<Flight>();
        for (EntityManagerFactory emf : entityManagerFactories.values()) {
            EntityManager newEm = emf.createEntityManager();
            EntityTransaction newTx = newEm.getTransaction();
            newTx.begin();
            Query query = newEm.createQuery("select p from Flight p where p.arrAirport.city =:aCity and p.depAirport.city =:dCity");
            query.setParameter("aCity", destinationCity).setParameter("dCity", departureCity);
            allFlights.addAll((List<Flight>) query.getResultList());
            newTx.commit();
            newEm.close();
        }
        List<Flight> correctFlights = new ArrayList<Flight>();
        for (Flight flight : allFlights) {
            if (flight.getDepartureTime().getDay() == flightDate.getDay()) {
                correctFlights.add(flight);
            }
        }
        return correctFlights;
    }

    public List<String> getAllDepartures() {
        List<String> departureCities = new ArrayList<String>();
        for (EntityManagerFactory emf : entityManagerFactories.values()) {
            EntityManager newEm = emf.createEntityManager();
            EntityTransaction newTx = newEm.getTransaction();
            newTx.begin();
            Query query = newEm.createQuery("select p.depAirport.city from Flight p");
            departureCities.addAll((List<String>) query.getResultList());
            newTx.commit();
            newEm.close();
        }
        return departureCities;
    }

    public List<String> searchDestinationCity(String departureCity) {
        List<String> flights = new ArrayList<String>();
        if (departureCity == null || departureCity.equals("")) return flights;
        for (EntityManagerFactory emf : entityManagerFactories.values()) {
            EntityManager newEm = emf.createEntityManager();
            EntityTransaction newTx = newEm.getTransaction();
            newTx.begin();
            Query query = newEm.createQuery("select p.arrAirport.city from Flight p where p.depAirport.city = :city");
            query.setParameter("city", departureCity);
            flights.addAll((List<String>) query.getResultList());
            newTx.commit();
            newEm.close();
        }
        return flights;
    }

    public List<String> searchDepartureCity(String destinationCity) {
        List flights = new ArrayList<String>();
        if (destinationCity == null || destinationCity.equals("")) return flights;
        for (EntityManagerFactory emf : entityManagerFactories.values()) {
            EntityManager newEm = emf.createEntityManager();
            EntityTransaction newTx = newEm.getTransaction();
            newTx.begin();
            Query query = newEm.createQuery("select p from Flight p where p.arrAirport.city = :city ORDER BY p.flightNumber ascending");
            query.setParameter("city", destinationCity);
            flights = query.getResultList();
            newTx.commit();
            newEm.close();
        }
        List<String> correct = new ArrayList<String>();
        for (Object flight : flights) if (((Flight) flight).getArrAirport().getCity().equals(destinationCity)) correct.add(((Flight) flight).getDepAirport().getCity());
        return correct;
    }

    public int getAvailableSeats(String flightNumber) {
        for (EntityManagerFactory emf : entityManagerFactories.values()) {
            EntityManager newEm = emf.createEntityManager();
            EntityTransaction newTx = newEm.getTransaction();
            newTx.begin();
            Flight flight = newEm.find(Flight.class, flightNumber);
            if (flight != null) {
                int smallestAvailbleSeats = Integer.MAX_VALUE;
                for (FlightLeg flightLeg : flight.getFlightLegs()) {
                    if (flightLeg.getMaximumCapacity() - flightLeg.getSeatsBooked() < smallestAvailbleSeats) smallestAvailbleSeats = flightLeg.getMaximumCapacity() - flightLeg.getSeatsBooked();
                }
                newTx.commit();
                newEm.close();
                return smallestAvailbleSeats;
            }
            newTx.commit();
            newEm.close();
        }
        return -1;
    }

    /**
	 * Untested so far, might work?
	 * @param legNumber
	 * @return
	 */
    private int getAvailableSeatsLeg(String legNumber) {
        if (legNumber == null || legNumber.equals("")) return -1;
        for (EntityManagerFactory emf : entityManagerFactories.values()) {
            EntityManager newEm = emf.createEntityManager();
            EntityTransaction newTx = newEm.getTransaction();
            newTx.begin();
            Query query = newEm.createQuery("select m from FlightLeg m where m.legNumber = :legNumber");
            query.setParameter("legNumber", legNumber);
            List results = query.getResultList();
            FlightLeg flightLeg = null;
            if (results != null && !results.isEmpty()) flightLeg = (FlightLeg) results.get(0);
            newTx.commit();
            newEm.close();
        }
        return -1;
    }

    public boolean savePassenger(Passenger passenger) {
        EntityManagerFactory emf = getFactoryInstance(databaseName1);
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(passenger);
        tx.commit();
        em.close();
        return true;
    }

    public boolean cancelReservation(int reservationNumber) {
        EntityManagerFactory emf = getFactoryInstance(databaseName1);
        EntityManager newEm = emf.createEntityManager();
        EntityTransaction newTx = newEm.getTransaction();
        newTx.begin();
        Reserve reserve = null;
        reserve = newEm.find(Reserve.class, "" + reservationNumber);
        if (reserve == null) return false;
        for (FlightLeg flightLeg : reserve.getReservedFlightLegs()) {
            flightLeg.setSeatsBooked(flightLeg.getSeatsBooked() - 1);
            newEm.merge(flightLeg);
        }
        newEm.remove(reserve);
        newTx.commit();
        newEm.close();
        return true;
    }

    @SuppressWarnings("unchecked")
    public int reserveFlight(String flightNumber, Passenger passenger) {
        if (flightNumber == null) {
            System.out.println("ERROR: flightNumber is null! in ServiceProvider.reserveFlight. What have you done Denis!?");
            return -1;
        }
        int reservationNumber;
        while (true) {
            Reserve reserve = null;
            reservationNumber = (int) (Math.random() * 50000.0f);
            for (EntityManagerFactory emf : entityManagerFactories.values()) {
                EntityManager newEm = emf.createEntityManager();
                EntityTransaction newTx = newEm.getTransaction();
                newTx.begin();
                reserve = newEm.find(Reserve.class, "" + reservationNumber);
                newTx.commit();
                newEm.close();
            }
            if (reserve == null) break;
        }
        for (EntityManagerFactory emf : entityManagerFactories.values()) {
            EntityManager newEm = emf.createEntityManager();
            EntityTransaction newTx = newEm.getTransaction();
            newTx.begin();
            Query query = newEm.createQuery("select m.flightLegs from Flight m where m.flightNumber = :flightNumber");
            query.setParameter("flightNumber", flightNumber);
            List<FlightLeg> flightLegs = query.getResultList();
            if (flightLegs != null && !flightLegs.isEmpty()) {
                for (FlightLeg flightLeg : flightLegs) {
                    flightLeg.setSeatsBooked(flightLeg.getSeatsBooked() + 1);
                    newEm.merge(flightLeg);
                }
                Reserve reserve = new Reserve(String.valueOf(reservationNumber), flightLegs, "SeatNumber");
                newEm.persist(reserve);
                newTx.commit();
                newEm.close();
                break;
            }
            newTx.commit();
            newEm.close();
        }
        return reservationNumber;
    }

    public void cleanup() {
        for (EntityManagerFactory emf : entityManagerFactories.values()) emf.close();
        entityManagerFactories.clear();
    }
}
