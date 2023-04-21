package com.trentech.billcalc;

import java.beans.BeanInfo;
import java.beans.DefaultPersistenceDelegate;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.event.EventListenerList;
import com.trentech.billcalc.event.ArriveEvent;
import com.trentech.billcalc.event.BillEvent;
import com.trentech.billcalc.event.BillSpecificEvent;
import com.trentech.billcalc.event.Event;
import com.trentech.billcalc.event.EventFactory;
import com.trentech.billcalc.event.LeaveEvent;
import com.trentech.billcalc.event.OptInEvent;
import com.trentech.billcalc.event.OptOutEvent;
import com.trentech.billcalc.event.PayEvent;
import com.trentech.billcalc.event.RenewLeaseEvent;
import com.trentech.billcalc.event.SetupBillEvent;
import com.trentech.billcalc.event.StartLeaseEvent;
import com.trentech.billcalc.event.StartVacationEvent;
import com.trentech.billcalc.event.StopVacationEvent;
import com.trentech.billcalc.event.TransferLeaseEvent;

/**
 * FinanceManager contains macro functions for managing the money for Residents
 * and Billers.  It can also be used to query data from Residents and Billers.
 *  
 * @author Trent Hoeppner
 */
public class FinanceManager {

    /**
     * Used to {@link #roundToWhole(BigDecimal) round to whole numbers}.
     */
    private static final BigDecimal HALF = BigDecimal.valueOf(0.5);

    /**
     * The EventComparator instance to use, to avoid the cost of recreating for 
     * every comparison.
     */
    private static final EventComparator EVENT_COMPARATOR = new EventComparator();

    /**
     * The number of days ahead to generate events.  This allows the user to 
     * plan a little earlier than when a payment deadline is hit.
     */
    private static final int LOOKAHEAD_DAYS = 5;

    /**
     * The listeners to this.
     */
    private EventListenerList listenerList = new EventListenerList();

    /**
     * The date when the first person arrived at the apartment.  The first
     * bills will be calculated for each person based on this date.
     */
    private Date firstArrival;

    /**
     * The Billers who have charged for services to the apartment so far.
     */
    private List<Biller> billers;

    /**
     * The residents who live, or have lived, in the apartment.
     */
    private List<Resident> residents;

    /**
     * The leases between tenants and landlords.
     */
    private List<Lease> leases = new ArrayList<Lease>();

    /**
     * A temporary index for iterating through the events list.  This allows
     * events to be added to the list while iterating.
     */
    private int currentProcessedEventIndex;

    /**
     * The Events that are used as the basis for other data in this.  This list
     * should always be sorted.
     */
    private List<Event> events = new ArrayList<Event>();

    /**
     * True if any non-generated events have been removed from this since the 
     * last save.
     */
    private boolean hasEventsRemoved;

    /**
     * Constructor for FinanceManager.
     */
    public FinanceManager() {
        resetNonEventData();
    }

    /**
     * Adds the given FinanceManagerListener to this.
     * 
     * @param   l   the listener to add.  Cannot be null.
     */
    public void addFinanceManagerListener(FinanceManagerListener l) {
        listenerList.add(FinanceManagerListener.class, l);
    }

    /**
     * Removes the given FinanceManagerListener from this.
     * 
     * @param   l   the listener to remove.  Cannot be null.
     */
    public void removeFinanceManagerListener(FinanceManagerListener l) {
        listenerList.remove(FinanceManagerListener.class, l);
    }

    /**
     * Notifies all listeners that the non-event data has been reset.
     */
    private void fireDataReset() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == FinanceManagerListener.class) {
                ((FinanceManagerListener) listeners[i + 1]).dataReset();
            }
        }
    }

    /**
     * Notifies all listeners that the Events have been processed.
     */
    private void fireEventsProcessed() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == FinanceManagerListener.class) {
                ((FinanceManagerListener) listeners[i + 1]).eventsProcessed();
            }
        }
    }

    /**
     * Adds the given Event to this.
     * 
     * @param   event   the Event to add.  Cannot be null.
     */
    public void addEvent(Event event) {
        addEvent(event, -1);
    }

    /**
     * Adds an event if the index where it belongs comes after the given index.
     * If the event should be placed at or before the given index, an 
     * IllegalArgumentException is thrown.  This method can be used while 
     * iterating through the events list, to add events after the current index 
     * in the list.
     * 
     * @param   event               the event to add.  Cannot be null.
     * @param   mustBeAfterIndex    the index to add after.  If less than 0,
     *                              event is guaranteed to be added.
     */
    private void addEvent(Event event, int mustBeAfterIndex) {
        int i = Collections.binarySearch(events, event, EVENT_COMPARATOR);
        int insertionPoint = -(i + 1);
        if (i < 0) {
            if (insertionPoint > mustBeAfterIndex) {
                events.add(insertionPoint, event);
            } else {
                throw new IllegalArgumentException("Event must be inserted at " + insertionPoint + " before but is after " + mustBeAfterIndex);
            }
        }
    }

    /**
     * Removes the given Event from this.
     * 
     * @param   event   the Event to remove.  Cannot be null.
     */
    public void removeEvent(Event event) {
        removeEvent(event, -1);
    }

    /**
     * Removes an event if its index comes after the given index.  If the event 
     * is at or before the given index, an IllegalArgumentException is thrown.  
     * This method can be used while iterating through the events list, to 
     * remove events after the current index in the list.
     * 
     * @param   event               the event to remove.  Cannot be null.
     * @param   mustBeAfterIndex    the index to remove after.  If less than 0,
     *                              event is guaranteed to be removed (if it 
     *                              exists in the list).
     */
    private void removeEvent(Event event, int mustBeAfterIndex) {
        int i = Collections.binarySearch(events, event, EVENT_COMPARATOR);
        if (i >= 0) {
            if (i > mustBeAfterIndex) {
                events.remove(i);
                if (!event.isGenerated()) {
                    hasEventsRemoved = true;
                }
            } else {
                throw new IllegalArgumentException("Event must be removed at " + i + " but should be after " + mustBeAfterIndex);
            }
        }
    }

    /**
     * Returns the list of Events in this.
     * 
     * @return  the list of Events in this.  Will not be null.
     */
    public List<Event> getEvents() {
        return Collections.unmodifiableList(events);
    }

    /**
     * Returns whether the Events in this need to be saved.
     * 
     * @return  true if any Events have been added or changed or removed since 
     *          the last save, false otherwise.
     */
    public boolean isEventsSaved() {
        boolean saved = !hasEventsRemoved;
        if (saved) {
            for (Event event : events) {
                if (!event.isGenerated() && !event.isSaved()) {
                    saved = false;
                    break;
                }
            }
        }
        return saved;
    }

    /**
     * Saves all events to a file called events.xml in the working directory.
     * 
     * @throws  FileNotFoundException   if there was an error opening the file 
     *                                  for writing.
     */
    public void saveEvents() throws FileNotFoundException {
        try {
            BeanInfo info = Introspector.getBeanInfo(Event.class);
            PropertyDescriptor[] propertyDescriptors = info.getPropertyDescriptors();
            for (PropertyDescriptor pd : propertyDescriptors) {
                if (pd.getName().equals("saved")) {
                    pd.setValue("transient", Boolean.TRUE);
                    break;
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        XMLEncoder e = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("events.xml")));
        e.setPersistenceDelegate(ArriveEvent.class, new DefaultPersistenceDelegate(new String[] { "date", "name" }));
        e.setPersistenceDelegate(LeaveEvent.class, new DefaultPersistenceDelegate(new String[] { "date", "name" }));
        e.setPersistenceDelegate(BillEvent.class, new DefaultPersistenceDelegate(new String[] { "date", "name", "amount" }));
        e.setPersistenceDelegate(PayEvent.class, new DefaultPersistenceDelegate(new String[] { "date", "name", "amount", "paidEntity" }));
        e.setPersistenceDelegate(OptOutEvent.class, new DefaultPersistenceDelegate(new String[] { "date", "name", "billerName" }));
        e.setPersistenceDelegate(OptInEvent.class, new DefaultPersistenceDelegate(new String[] { "date", "name", "billerName" }));
        e.setPersistenceDelegate(StartLeaseEvent.class, new DefaultPersistenceDelegate(new String[] { "date", "name", "amount", "payerName", "leaseEnd", "paymentFrequency", "deposit", "canSublet", "roomDescription", "leaseBreakPenalty" }));
        e.setPersistenceDelegate(RenewLeaseEvent.class, new DefaultPersistenceDelegate(new String[] { "date", "name", "leaseEnd" }));
        e.setPersistenceDelegate(TransferLeaseEvent.class, new DefaultPersistenceDelegate(new String[] { "date", "name", "newTenant", "transferPaid" }));
        e.setPersistenceDelegate(StartVacationEvent.class, new DefaultPersistenceDelegate(new String[] { "date", "name" }));
        e.setPersistenceDelegate(StopVacationEvent.class, new DefaultPersistenceDelegate(new String[] { "date", "name" }));
        e.setPersistenceDelegate(SetupBillEvent.class, new DefaultPersistenceDelegate(new String[] { "date", "name", "fixedCost", "afterConsumption" }));
        resetNonEventData();
        e.writeObject(events);
        e.close();
        processEvents();
        for (Event event : events) {
            if (!event.isSaved()) {
                event.setSaved(true);
            }
        }
        hasEventsRemoved = false;
    }

    /**
     * Loads the Event data from a file called events.xml in the working 
     * directory.
     * 
     * @throws  FileNotFoundException   if there was an error opening the file 
     *                                  for reading.
     */
    @SuppressWarnings("unchecked")
    public void loadEvents() throws FileNotFoundException {
        XMLDecoder e = new XMLDecoder(new BufferedInputStream(new FileInputStream("events.xml")));
        events = (List<Event>) e.readObject();
        e.close();
        for (Event event : events) {
            event.setSaved(true);
            Date date = event.getDate();
            date = clearSubDayFields(date);
            if (!date.equals(event.getDate())) {
                event.setDate(date);
            }
        }
    }

    /**
     * Resorts the list of Events.  This should be done when Event data
     * changes, especially if the date changes.
     */
    public void resortEvents() {
        Collections.sort(events, EVENT_COMPARATOR);
    }

    /**
     * Erases all data (except Events) and reprocesses the list of Events.
     */
    public void processEvents() {
        resetNonEventData();
        for (currentProcessedEventIndex = 0; currentProcessedEventIndex < events.size(); currentProcessedEventIndex++) {
            Event event = events.get(currentProcessedEventIndex);
            if (firstArrival == null) {
                firstArrival = event.getDate();
            }
            event.process(this);
        }
        fireEventsProcessed();
    }

    /**
     * Erases all data except Events, to prepare for reprocessing.
     */
    private void resetNonEventData() {
        firstArrival = null;
        billers = new ArrayList<Biller>();
        residents = new ArrayList<Resident>();
        leases = new ArrayList<Lease>();
        Iterator<Event> iterator = events.iterator();
        while (iterator.hasNext()) {
            Event event = iterator.next();
            if (event.isGenerated()) {
                iterator.remove();
            }
        }
        fireDataReset();
    }

    /**
     * Adds a new Resident to be managed.
     * 
     * @param   resident    the Resident to add.  Cannot be null.
     */
    public void addResident(Resident resident) {
        residents.add(resident);
        if (firstArrival == null || resident.getArrivalDate().before(firstArrival)) {
            firstArrival = resident.getArrivalDate();
        }
    }

    /**
     * Returns the list of Residents to add.
     * 
     * @return  the residents to add.  Will not be null.
     */
    public List<Resident> getResidents() {
        return Collections.unmodifiableList(residents);
    }

    /**
     * Notifies this that the given Resident has left on the given date.
     * 
     * @param   resident    the Resident who is leaving.  Cannot be null.
     * @param   date        the date that the Resident is leaving.  Cannot be 
     *                      null.
     */
    public void residentLeaves(Resident resident, Date date) {
        resident.setDepartureDate(date);
        Lease lease = findLease(resident);
        if (lease != null) {
            Date leaseEnd = lease.getLeasePeriod().getLastDate();
            String landlord = lease.getController().getName();
            String tenant = lease.getPayer().getName();
            if (date.before(leaseEnd) && lease.getLeaseBreakPenalty() > 0) {
                Event breakLeaseEvent = new BillSpecificEvent(date, landlord, lease.getLeaseBreakPenalty(), tenant, "Lease Break Penalty");
                addEvent(breakLeaseEvent, currentProcessedEventIndex);
            }
            boolean exists = moveDepositChargebackEvent(date, lease);
            if (!exists && lease.getDeposit() > 0) {
                BillSpecificEvent depositRefundEvent = new BillSpecificEvent(date, tenant, lease.getDeposit(), landlord, "Deposit");
                addEvent(depositRefundEvent, currentProcessedEventIndex);
            }
            Date rentPaidEnd = leaseEnd;
            Date lastStart = lease.getLeasePeriod().getFirstDate();
            for (Date periodStart : lease.getPeriodStarts()) {
                if (!date.before(lastStart) && date.before(periodStart)) {
                    rentPaidEnd = periodStart;
                    break;
                }
                lastStart = periodStart;
            }
            int rentDiff = calcRent(date, rentPaidEnd, lease);
            if (rentDiff > 0) {
                BillSpecificEvent rentRefundEvent = new BillSpecificEvent(date, tenant, rentDiff, landlord, "Rent");
                addEvent(rentRefundEvent, currentProcessedEventIndex);
            }
        }
    }

    /**
     * Moves the deposit chargeback event associated with the given lease to 
     * the given date.
     * 
     * @param   date    the new date for the deposit chargeback.  Cannot be 
     *                  null.
     * @param   lease   the lease to find the deposit chargeback event for.  
     *                  Cannot be null.
     *                  
     * @return  true if the deposit chargeback exists and was moved, false if 
     *          it doesn't exist.
     */
    private boolean moveDepositChargebackEvent(Date date, Lease lease) {
        boolean exists = false;
        if (lease.getDeposit() > 0) {
            Date leaseEnd = lease.getLeasePeriod().getLastDate();
            for (Event event : events) {
                if (isMatchingBillEvent(event, lease, "Deposit", leaseEnd)) {
                    BillSpecificEvent billEvent = (BillSpecificEvent) event;
                    removeEvent(billEvent, currentProcessedEventIndex);
                    billEvent.setDate(date);
                    addEvent(billEvent, currentProcessedEventIndex);
                    exists = true;
                    break;
                }
            }
        }
        return exists;
    }

    /**
     * Returns whether the given event is a bill from the tenant to the 
     * landlord for the given billName.  This method is used while iterating 
     * through the list of events.
     * 
     * @param   event       the Event to check.  Cannot be null.
     * @param   lease       the Lease to check against.  Cannot be null.
     * @param   billName    the name of the bill find.  Cannot be null or empty.
     * @param   eventDate   the date that the event should have occurred on.  
     *                      Cannot be null.
     * 
     * @return  true if the Event matches, false otherwise.
     */
    private boolean isMatchingBillEvent(Event event, Lease lease, String billName, Date eventDate) {
        boolean match = false;
        String landlord = lease.getController().getName();
        String tenant = lease.getPayer().getName();
        if (event.getDate().equals(eventDate) && event instanceof BillSpecificEvent) {
            BillSpecificEvent billEvent = (BillSpecificEvent) event;
            if (billEvent.getName().equals(tenant) && billEvent.getOwerName().equals(landlord) && billEvent.getBillName().equals(billName)) {
                match = true;
            }
        }
        return match;
    }

    /**
     * Find the Lease that has the given Resident as the payer.
     * 
     * @param   resident    the tenant on the Lease.  Cannot be null.
     * 
     * @return  the Lease, or null if there is no Lease.
     */
    private Lease findLease(Resident resident) {
        Lease found = null;
        for (Lease lease : leases) {
            if (lease.getPayer().equals(resident)) {
                found = lease;
                break;
            }
        }
        return found;
    }

    /**
     * Adds a new Biller to be managed.
     * 
     * @param   biller  the Biller to add.  Cannot be null.
     */
    private void addBiller(Biller biller) {
        billers.add(biller);
    }

    /**
     * Notifies this that a new bill has arrived.  If the Biller does not yet
     * exist it will be created.
     * 
     * @param   billerName  the name of the Biller.  Cannot be null or empty.
     * @param   date        the date that the bill arrived.  Cannot be null.
     * @param   amount      the amount of money that is owed to the Biller.
     */
    public void billArrived(String billerName, Date date, int amount) {
        Biller biller = findBiller(billerName);
        if (biller == null) {
            biller = new Biller(billerName, firstArrival);
            addBiller(biller);
        }
        Date lastTime = biller.getLastBillDay();
        if (lastTime == null) {
            lastTime = calcDateFromStart(firstArrival, -1);
        }
        Date startTime = calcDateFromStart(lastTime, 1);
        Date endTime = date;
        BigDecimal amountDecimal = BigDecimal.valueOf(amount);
        BigDecimal toCharge;
        if (biller.isAfterConsumption()) {
            toCharge = amountDecimal;
        } else {
            toCharge = biller.getLastBill();
        }
        splitBill(biller, startTime, endTime, toCharge.intValue());
        biller.addBill(date, amountDecimal);
    }

    /**
     * Notifies this that a new bill has arrived for a specific Resident.
     * 
     * @param   owedName    the name of the Resident who should get the money. 
     *                      Cannot be null or empty.
     * @param   billName    the name of the bill.  Cannot be null or empty.
     * @param   date        the date that the bill arrived.  Cannot be null.
     * @param   amount      the amount of money that is owed to the Biller.
     * @param   owerName    the name of the Resident who should pay the bill.  
     *                      Cannot be null.
     */
    public void specificBillArrived(String owedName, String billName, Date date, int amount, String owerName) {
        Resident owed = findResident(owedName);
        Biller biller = findBiller(billName);
        if (biller == null) {
            biller = new Biller(billName, firstArrival);
            addBiller(biller);
        }
        Resident ower = findResident(owerName);
        ower.chargeSpecificBill(biller, owed, BigDecimal.valueOf(amount));
        owed.chargeSpecificBill(biller, ower, BigDecimal.valueOf(-amount));
    }

    /**
     * Notifies this that a Resident is paying a Biller or another Resident.
     * 
     * @param   residentName    the name of the Resident that is paying.  
     *                          Cannot be null or empty.
     * @param   payeeName       the name of the Resident or Biller to be paid.
     *                          Cannot be null or empty.
     * @param   amount          the amount that is paid.
     */
    public void residentPaysSomeone(String residentName, String payeeName, int amount) {
        Resident payer = null;
        Resident paidResident = null;
        List<Resident> owers = new ArrayList<Resident>();
        Biller possibleBiller = findBiller(payeeName);
        Iterator<Resident> residentIterator = residents.iterator();
        while (residentIterator.hasNext()) {
            Resident resident = residentIterator.next();
            if (resident.getName().equals(residentName)) {
                payer = resident;
            } else if (resident.getName().equals(payeeName)) {
                paidResident = resident;
            } else if (resident.owes(possibleBiller)) {
                owers.add(resident);
            }
        }
        if (paidResident != null) {
            payer.charge(paidResident, BigDecimal.valueOf(-amount));
            paidResident.charge(payer, BigDecimal.valueOf(amount));
        } else {
            if (possibleBiller == null) {
                possibleBiller = new Biller(payeeName, firstArrival);
                addBiller(possibleBiller);
            }
            payer.charge(possibleBiller, BigDecimal.valueOf(-amount));
            BigDecimal owed = payer.getAmountOwed(possibleBiller);
            if (owed.compareTo(BigDecimal.ZERO) < 0 && owers.size() > 0) {
                residentIterator = owers.iterator();
                while (residentIterator.hasNext()) {
                    Resident resident = residentIterator.next();
                    BigDecimal toChange = resident.getAmountOwed(possibleBiller);
                    BigDecimal negToChange = toChange.negate();
                    resident.transferDebt(possibleBiller, payer, toChange);
                    payer.transferDebt(possibleBiller, resident, negToChange);
                }
            }
        }
    }

    /**
     * Returns whether the given Resident has some costs that need to be 
     * estimated for the given Biller.  This occurs when a Resident has left 
     * but the bill has not yet arrived.
     * 
     * @param   resident    the Resident to check.  Cannot be null.
     * @param   biller      the Biller that may need an estimate.  Cannot be 
     *                      null.
     * 
     * @return  true if there are costs to be estimated, false otherwise.
     */
    public boolean hasEstimatedCosts(Resident resident, Biller biller) {
        return resident.getDepartureDate() != null && biller.getLastBillDay().before(resident.getDepartureDate());
    }

    /**
     * Estimates the amount that the resident owes for the given bill.
     * <p>
     * See {@link #splitBill(Biller, Date, Date, int)} for a detailed 
     * description of how the splitting occurs and a description of the 
     * parameters.
     * 
     * @param   resident        the Resident to determine the charge for.  
     *                          Cannot be null.
     * @param   biller          the Biller for this bill.  Cannot be null 
     *                          or empty.
     * @param   billStartTime   the start day as described above.  Cannot be 
     *                          null.
     * @param   billEndTime     the end day as described above.  Cannot be null.
     * @param   amount          the amount of money for this bill as described 
     *                          above.
     * 
     * @return  the amount that the Resident should be charged.  Will not 
     *          be null.
     */
    private BigDecimal estimateRemaining(Resident resident, Biller biller, Date billStartTime, Date billEndTime, int amount) {
        Map<Resident, BigDecimal> residentToChargeMap = splitWithoutCharging(biller, billStartTime, billEndTime, amount);
        BigDecimal estimate = residentToChargeMap.get(resident);
        if (estimate == null) {
            estimate = BigDecimal.ZERO;
        }
        return estimate;
    }

    /**
     * Calculates the estimated cost for the given Resident toward the given 
     * Biller.
     * 
     * @param   resident    the Resident to estimate the costs for.  Cannot be 
     *                      null.
     * @param   biller      the Biller who will bill in the future.  Cannot be 
     *                      null.
     *                      
     * @return  the estimated amount for the bill.  Will be >= 0.
     */
    public BigDecimal calcEstimatedCost(Resident resident, Biller biller) {
        int numDays = calcDays(biller.getLastBillDay(), resident.getDepartureDate());
        BigDecimal average = biller.getAverage();
        BigDecimal apartmentEstimate = roundToWhole(average.multiply(BigDecimal.valueOf(numDays)));
        BigDecimal estimate = estimateRemaining(resident, biller, biller.getLastBillDay(), resident.getDepartureDate(), apartmentEstimate.intValue());
        estimate = roundToWhole(estimate);
        return estimate;
    }

    /**
     * Calculates the number of days between startDay (inclusive) and 
     * endDay (exclusive).  This operation is the inverse of {@link #calcDateFromStart}.
     * 
     * @param   startDay    the first day in the period.  Cannot be null.
     * @param   endDay      the day after the last day in the period.  Cannot 
     *                      be null.
     *                      
     * @return  the number of days between startDay and endDay.  Will be >= 0.
     */
    public static int calcDays(Date startDay, Date endDay) {
        int days = (int) ((endDay.getTime() - startDay.getTime()) / DateGroup.DAY) + 1;
        return days;
    }

    /**
     * Calculates the endDay given the number of days after a startDay.  This
     * operation is the inverse of {@link #calcDays}.
     * 
     * @param   startDay        the day to start from (inclusive).  Cannot be 
     *                          null.
     * @param   numDaysAfter    the number of days after and including the 
     *                          startDay.
     *                          
     * @return  the calculated end date (exclusive).  Will not be null.
     */
    public static Date calcDateFromStart(Date startDay, int numDaysAfter) {
        long daysInMillis = ((long) numDaysAfter) * DateGroup.DAY;
        Date date = new Date(startDay.getTime() + daysInMillis);
        return date;
    }

    /**
     * Rounds the given BigDecimal to the nearest whole number.
     * 
     * @param   num the number to round.  Cannot be null.
     * 
     * @return  the rounded whole number.  Will not be null.  
     */
    public static BigDecimal roundToWhole(BigDecimal num) {
        return BigDecimal.valueOf(num.add(HALF).intValue());
    }

    /**
     * Returns true if the given testDay is between startDay (inclusive) and 
     * endDay (exclusive).
     * 
     * @param   startDay    the first day in the range.  Cannot be null.
     * @param   endDay      the day after the last day in the range.  Cannot be
     *                      null.
     * @param   testDay     the day to check the bounds for.  Cannot be null.
     * 
     * @return  true if testDay falls in the range, false otherwise.
     */
    private boolean isInBounds(Date startDay, Date endDay, Date testDay) {
        boolean startPassed;
        if (startDay == null) {
            startPassed = true;
        } else {
            startPassed = testDay.equals(startDay) || testDay.after(startDay);
        }
        boolean endPassed;
        if (endDay == null) {
            endPassed = true;
        } else {
            endPassed = testDay.before(calcDateFromStart(endDay, 1));
        }
        return startPassed && endPassed;
    }

    /**
     * Finds the Biller with the given name.
     * 
     * @param   name    the name of the Biller.  Cannot be null or empty.
     * 
     * @return  the Biller with the given name, or null if none exists.
     */
    public Biller findBiller(String name) {
        Biller found = null;
        Iterator<Biller> billerIterator = billers.iterator();
        while (billerIterator.hasNext()) {
            Biller biller = billerIterator.next();
            if (biller.getName().equals(name)) {
                found = biller;
                break;
            }
        }
        return found;
    }

    /**
     * Finds the Resident with the given name.
     * 
     * @param   name    the name of the Resident.  Cannot be null or empty.
     * 
     * @return  the Resident with the given name, or null if none exists.
     */
    public Resident findResident(String name) {
        Resident found = null;
        Iterator<Resident> residentIterator = residents.iterator();
        while (residentIterator.hasNext()) {
            Resident resident = residentIterator.next();
            if (resident.getName().equals(name)) {
                found = resident;
                break;
            }
        }
        return found;
    }

    /**
     * Splits a bill between all current, and possibly some past, residents of 
     * the apartment.
     * <p>
     * If a Biller is marked as being charged after consumption, then the 
     * residents have already used the services that the bill is being charged 
     * for.  In this case, the residents currently live in the apartment or 
     * lived there in the past.
     * <ul>
     *  <li>the start date is the day that the last bill came (inclusive), or 
     *      the date of the first event (presumably when the first residents 
     *      arrived)
     *  <li>the end date is the day that the current bill arrived (exclusive)
     *  <li>the amount comes from the <b>current</b> bill
     * </ul>
     * <p>
     * If the Biller charges before consumption, then the residents have not 
     * yet used the service (for example, paying for water for the water 
     * cooler).  In this case, we don't know who should pay until the service
     * is consumed (e.g. all the water in the water cooler is gone).
     * <ul>
     *  <li>the start date is the day that the last bill came (inclusive)
     *  <li>the end date is the day that the current bill arrived (exclusive)
     *  <li>the amount comes from the <b>last</b> bill
     * </ul>
     * 
     * @param   biller          the Biller for this bill.  Cannot be null or 
     *                          empty.
     * @param   billStartTime   the start day as described above.  Cannot be 
     *                          null.
     * @param   billEndTime     the end day as described above.  Cannot be null.
     * @param   totalAmount     the amount of money for this bill as described 
     *                          above.
     */
    private void splitBill(Biller biller, Date billStartTime, Date billEndTime, int totalAmount) {
        Map<Resident, BigDecimal> residentToTotalMap = splitWithoutCharging(biller, billStartTime, billEndTime, totalAmount);
        int remainingTotal = totalAmount;
        Iterator<Resident> residentIterator = residentToTotalMap.keySet().iterator();
        while (residentIterator.hasNext()) {
            Resident resident = residentIterator.next();
            BigDecimal total = roundToWhole(residentToTotalMap.get(resident));
            resident.charge(biller, total);
            remainingTotal -= total.intValue();
        }
        System.out.println("not split amount = " + remainingTotal);
    }

    /**
     * Splits a bill between all current, and possibly some past, residents of 
     * the apartment, but without charging those residents.  A map is returned
     * with the results.
     * <p>
     * See {@link #splitBill(Biller, Date, Date, int)} for a detailed 
     * description of how the splitting occurs and a description of the 
     * parameters.
     * 
     * @param   biller          the Biller for this bill.  Cannot be null or 
     *                          empty.
     * @param   billStartTime   the start day as described above.  Cannot be 
     *                          null.
     * @param   billEndTime     the end day as described above.  Cannot be null.
     * @param   totalAmount     the amount of money for this bill as described 
     *                          above.
     * 
     * @return  the result of the calculations as a mapping from Residents to 
     *          how much they should pay the Biller.  Will not be null. 
     */
    private Map<Resident, BigDecimal> splitWithoutCharging(Biller biller, Date billStartTime, Date billEndTime, int totalAmount) {
        int billableDays = calcDays(billStartTime, billEndTime);
        BigDecimal amountPerDay = BigDecimal.valueOf(totalAmount).divide(BigDecimal.valueOf(billableDays), MathContext.DECIMAL128);
        Map<Resident, BigDecimal> residentToTotalMap = new HashMap<Resident, BigDecimal>();
        List<Resident> todayResidents = new ArrayList<Resident>();
        for (int i = 0; i < billableDays; i++) {
            Date currentDate = calcDateFromStart(billStartTime, i);
            todayResidents.clear();
            Iterator<Resident> residentIterator = residents.iterator();
            while (residentIterator.hasNext()) {
                Resident resident = residentIterator.next();
                if (isInBounds(resident.getArrivalDate(), resident.getDepartureDate(), currentDate) && !resident.isOptedOut(biller, currentDate) && (!resident.isOnVacation(currentDate) || biller.isFixedCost())) {
                    todayResidents.add(resident);
                }
            }
            BigDecimal amountPerResident = amountPerDay.divide(BigDecimal.valueOf(todayResidents.size()), MathContext.DECIMAL128);
            residentIterator = todayResidents.iterator();
            while (residentIterator.hasNext()) {
                Resident resident = residentIterator.next();
                BigDecimal total = residentToTotalMap.get(resident);
                if (total == null) {
                    total = BigDecimal.ZERO;
                }
                total = total.add(amountPerResident);
                residentToTotalMap.put(resident, total);
            }
        }
        return residentToTotalMap;
    }

    /**
     * Returns the amount that first owes to second.
     * 
     * @param   first   the Resident to ask about debt.  Cannot be null.
     * @param   second  the Resident who may or may not be owed debt.  Cannot 
     *                  be null.
     * 
     * @return  the amount that first owes second.  Will be negative if second 
     *          owes first.
     */
    public int getAmountOwed(Resident first, Resident second) {
        int total = 0;
        Map<Biller, BigDecimal> originalDebtMap = first.getDebtsTowardResident(second);
        for (Biller biller : originalDebtMap.keySet()) {
            BigDecimal owing = originalDebtMap.get(biller);
            total += owing.intValue();
        }
        return total;
    }

    /**
     * Returns the total amount that the given Resident owes to all Billers and
     * other Residents.
     * 
     * @param   resident    the Resident to find debt for.  Cannot be null.
     * 
     * @return  the amount owed.  Will be negative if other people owe the 
     *          Resident.
     */
    public int getTotalOwing(Resident resident) {
        int total = 0;
        for (Biller biller : resident.getOwedBillers()) {
            BigDecimal owing = resident.getAmountOwed(biller);
            total += owing.intValue();
        }
        for (Resident otherResident : resident.getOwedResidents()) {
            total += getAmountOwed(resident, otherResident);
        }
        return total;
    }

    /**
     * Adds the given lease to this.
     * 
     * @param   lease   the lease to add.  Cannot be null.  
     */
    private void addLease(Lease lease) {
        leases.add(lease);
    }

    /**
     * Creates a new Lease, and adds events for lease-related Events (such as 
     * rent).
     * 
     * @param   leaseStart          the date that the lease starts.  Cannot be 
     *                              null.
     * @param   controllerName      the name of the landlord or subletter.  
     *                              Cannot be null or empty.
     * @param   payerName           the name of the tenant.  Cannot be null or 
     *                              empty.
     * @param   amountPerMonth      the amount of rent per month.  Must be 
     *                              >= 0.
     * @param   leaseEnd            the date that the lease ends (exclusive).  
     *                              Cannot be null.
     * @param   paymentFrequency    the number of months between payments.  
     *                              Must be > 0.
     * @param   deposit             the deposit amount.  Must be >= 0.
     * @param   canSublet           true indicates that the tenant can sublet, 
     *                              false indicates that subletting is not 
     *                              allowed.
     * @param   roomDescription     a description of the apartment or room (to 
     *                              differentiate it from other rooms).  
     *                              Cannot be null.
     * @param   leaseBreakPenalty   the amount of money to charge for breaking 
     *                              the lease.  Must be >= 0.
     */
    public void createLease(Date leaseStart, String controllerName, String payerName, int amountPerMonth, Date leaseEnd, int paymentFrequency, int deposit, boolean canSublet, String roomDescription, int leaseBreakPenalty) {
        Entity controller = findOrCreateLeaseEntity(controllerName);
        Entity payer = findOrCreateLeaseEntity(payerName);
        DateGroup leasePeriod = new DateGroup();
        leasePeriod.startRange(leaseStart);
        leasePeriod.endRange(leaseEnd);
        Lease lease = new Lease(controller, payer, leasePeriod, amountPerMonth, deposit, canSublet, null, roomDescription, leaseBreakPenalty, paymentFrequency);
        addLease(lease);
        Date today = createToday();
        addBillsUntilToday(lease, leaseStart, today);
        if (lease.getDeposit() > 0) {
            Event event = new BillSpecificEvent(leaseStart, lease.getController().getName(), deposit, lease.getPayer().getName(), "Deposit");
            addEvent(event, currentProcessedEventIndex);
            if (isEnoughDaysBefore(today, leaseEnd)) {
                event = new BillSpecificEvent(leaseEnd, lease.getPayer().getName(), deposit, lease.getController().getName(), "Deposit");
                addEvent(event, currentProcessedEventIndex);
            }
        }
    }

    /**
     * Creates a Date representing today, stripped of data about the time of 
     * day.
     * 
     * @return  the data without any time data.  Will not be null.
     */
    private Date createToday() {
        Date today = new Date();
        today = clearSubDayFields(today);
        return today;
    }

    /**
     * Returns a date without information about the hour, minute, second, or 
     * millisecond.
     * 
     * @param   date    the date to clear fields for.  Cannot be null.
     * 
     * @return  a new Date with the specified fields cleared.  Will not be 
     *          null.
     */
    private Date clearSubDayFields(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.clear(Calendar.AM_PM);
        calendar.clear(Calendar.HOUR);
        calendar.clear(Calendar.HOUR_OF_DAY);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        Date newDate = calendar.getTime();
        return newDate;
    }

    /**
     * Adds the rent bill events from the given date until today.
     * 
     * @param   lease   the lease to charge rent for.  Cannot be null.
     * @param   from    the date to start charging from.  Cannot be null.
     * @param   today   the date to stop charging rent at.  Cannot be null.
     */
    private void addBillsUntilToday(Lease lease, Date from, Date today) {
        for (int i = 0; i < lease.getNumPeriods(); i++) {
            DateGroup period = lease.getPeriod(i);
            if (period.getFirstDate().before(from)) {
                continue;
            }
            if (!isEnoughDaysBefore(today, period.getFirstDate())) {
                break;
            }
            int rent = calcRent(period.getFirstDate(), period.getLastDate(), lease);
            Event event = new BillSpecificEvent(period.getFirstDate(), lease.getController().getName(), rent, lease.getPayer().getName(), "Rent");
            addEvent(event, currentProcessedEventIndex);
        }
    }

    /**
     * Returns whether the given date occurs within 
     * {@link #LOOKAHEAD_DAYS enough} days of the given deadline, or anytime 
     * after the deadline.
     *  
     * @param   currentDate a date representing today.  Cannot be null.
     * @param   deadline    the deadline we want to test against.  Cannot be 
     *                      null.
     *                      
     * @return  true if currentDate occurs within {@link #LOOKAHEAD_DAYS enough}
     *          days of deadline (or after), false if currentDate is too early.
     */
    private boolean isEnoughDaysBefore(Date currentDate, Date deadline) {
        Date modifiedDeadline = new Date(deadline.getTime() - LOOKAHEAD_DAYS * 24 * 60 * 60 * 1000);
        return !currentDate.before(modifiedDeadline);
    }

    /**
     * Finds an entity with the given name for a lease.  First it attempts to 
     * find a Resident, then a Biller.  If neither is found, a Biller with the 
     * given name is created.
     * 
     * @param   entityName  the name of the Entity to find or create.  Cannot 
     *                      be null or empty.
     * 
     * @return  the Resident or Biller that was found or created.  Will not be 
     *          null.
     */
    private Entity findOrCreateLeaseEntity(String entityName) {
        Entity entity = findResident(entityName);
        if (entity == null) {
            entity = findBiller(entityName);
            if (entity == null) {
                Biller biller = new Biller(entityName, firstArrival);
                addBiller(biller);
                entity = biller;
            }
        }
        return entity;
    }

    /**
     * Calculates the rent from start (inclusive) to end (exclusive).  Rent
     * is based on the monthly rate in the lease.  The rent is calculated by 
     * whole months, until the last month.  If the last month is less than a 
     * whole month, the daily rate is calculated by dividing the monthly rent
     * by the number of days in that month, then multiplying by the number of
     * days remaining.
     * <p>
     * For example, if the start date is from January 16 2008, the end date is 
     * March 25 2008, and the rent is 1000:
     * <ul>
     * <li>January 16 2008 to March 16 2008 is 2 whole months, which is 
     *  <code>2000</code>
     * <li>March has 31 days, so the daily rate is 
     *  <code>monthly rent/31 = 32.26</code>.  March 16 to March 24 (inclusive) 
     *  is 9 days, so the subtotal is 
     *  <code>9*32.26 = 290.32</code>
     * </ul>
     * The total is 2290.32.
     * 
     * @param   start   the first day of the rent period (inclusive).  Cannot 
     *                  be null.
     * @param   end     the last day of the rent period (exclusive).  Cannot 
     *                  be null, and must occur after start.
     * @param   lease   the lease to use to calculate the rent.  Cannot be 
     *                  null.
     *                  
     * @return  the amount of rent that should be paid.  Will be > 0.
     */
    private int calcRent(Date start, Date end, Lease lease) {
        Calendar currentCal = Calendar.getInstance();
        currentCal.setTime(start);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(end);
        int currentDay = currentCal.get(Calendar.DAY_OF_MONTH);
        if (currentDay > 1) {
            int daysToRollBack = currentDay - 1;
            currentCal.add(Calendar.DAY_OF_MONTH, -daysToRollBack);
            endCal.add(Calendar.DAY_OF_MONTH, -daysToRollBack);
        }
        BigDecimal total = BigDecimal.ZERO;
        int currentMonth = currentCal.get(Calendar.YEAR) * 100 + currentCal.get(Calendar.MONTH);
        int endMonth = endCal.get(Calendar.YEAR) * 100 + endCal.get(Calendar.MONTH);
        while (currentMonth < endMonth) {
            int dayOfMonth = currentCal.get(Calendar.DAY_OF_MONTH);
            if (dayOfMonth > 1) {
                int highestDay = currentCal.getActualMaximum(Calendar.DAY_OF_MONTH);
                BigDecimal dailyRate = calcDailyRate(highestDay, lease.getAmountPerMonth());
                int numDays = highestDay - dayOfMonth + 1;
                BigDecimal amountThisMonth = dailyRate.multiply(BigDecimal.valueOf(numDays));
                total = total.add(amountThisMonth);
            } else {
                total = total.add(BigDecimal.valueOf(lease.getAmountPerMonth()));
            }
            currentCal.set(Calendar.DAY_OF_MONTH, 1);
            currentCal.add(Calendar.MONTH, 1);
            currentMonth = currentCal.get(Calendar.YEAR) * 100 + currentCal.get(Calendar.MONTH);
        }
        int highestDay = currentCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        BigDecimal dailyRate = calcDailyRate(highestDay, lease.getAmountPerMonth());
        int numDays = endCal.get(Calendar.DAY_OF_MONTH) - 1;
        BigDecimal amountThisMonth = dailyRate.multiply(BigDecimal.valueOf(numDays));
        total = total.add(amountThisMonth);
        return roundToWhole(total).intValue();
    }

    /**
     * Calculates the daily rent given the number of days in the month and the 
     * amount of rent per month.  Note that this will change from month to 
     * month.
     * 
     * @param   daysPerMonth    the number of days in the month to calculate 
     *                          for.  Must be > 0.
     * @param   monthlyRate     the amount of rent per month.  Must be >= 0.
     * 
     * @return  the daily rate for the rent.  Will not be null, and will be >= 
     *          0.
     */
    private BigDecimal calcDailyRate(int daysPerMonth, int monthlyRate) {
        BigDecimal dailyRate = BigDecimal.valueOf(monthlyRate).divide(BigDecimal.valueOf(daysPerMonth), MathContext.DECIMAL128);
        return dailyRate;
    }

    /**
     * Renews an existing lease for the given tenant.
     * 
     * @param   date        the date that the renewal is made.  Cannot be null.
     * @param   tenantName  the name of the tenant who wants to renew.  Cannot
     *                      be null or empty.
     * @param   leaseEnd    the new end of the lease.  Cannot be null.
     */
    public void renewLease(Date date, String tenantName, Date leaseEnd) {
        Resident tenant = findResident(tenantName);
        Lease lease = findLease(tenant);
        moveDepositChargebackEvent(leaseEnd, lease);
        lease.adjustLeaseEnd(leaseEnd);
        addBillsUntilToday(lease, date, createToday());
    }

    /**
     * Transfers the lease under oldTenant to newTenant.
     * 
     * @param   transferDate    the date that the lease was renewed (should be 
     *                          the end of the old lease).  Cannot be null.
     * @param   oldTenant       the name of the tenant who currently has the 
     *                          lease.  Cannot be null or empty.
     * @param   newTenant       the name of the tenant who will take over the 
     *                          lease. Cannot be null or empty.
     * @param   transferPaid    true indicates that the new tenant will give
     *                          the deposit and extra rent to the old tenant, 
     *                          false indicates that the landlord will give the
     *                          deposit and extra rent to the old tenant and 
     *                          collect them from the new tenant.
     */
    public void transferLease(Date transferDate, String oldTenant, String newTenant, boolean transferPaid) {
        Resident oldResident = findResident(oldTenant);
        Lease lease = findLease(oldResident);
        Resident newResident = findResident(newTenant);
        Date paymentEnd = null;
        for (int i = 0; i < lease.getNumPeriods(); i++) {
            DateGroup period = lease.getPeriod(i);
            if (transferDate.after(period.getFirstDate())) {
                paymentEnd = period.getLastDate();
                break;
            }
        }
        int paidRent = 0;
        if (paymentEnd != null) {
            paidRent = calcRent(transferDate, paymentEnd, lease);
        }
        if (transferPaid) {
            removeGeneratedChargebackEvent(lease, "Deposit", transferDate);
            if (paidRent > 0) {
                removeGeneratedChargebackEvent(lease, "Rent", transferDate);
            }
        }
        lease.setPayer(newResident);
        String whoToPay;
        if (transferPaid) {
            whoToPay = oldTenant;
        } else {
            whoToPay = lease.getController().getName();
        }
        Event transferDepositEvent = new BillSpecificEvent(transferDate, whoToPay, lease.getDeposit(), newTenant, "Deposit");
        addEvent(transferDepositEvent, currentProcessedEventIndex);
        if (paidRent > 0) {
            Event transferRentEvent = new BillSpecificEvent(transferDate, whoToPay, paidRent, newTenant, "Rent");
            addEvent(transferRentEvent, currentProcessedEventIndex);
        }
    }

    /**
     * Removes the chargeback event for the given bill, from the tenant to the 
     * landlord under the given lease.
     * 
     * @param   lease       the lease.  Cannot be null.
     * @param   billName    the type of bill to find and remove the chargeback 
     *                      event for.  Cannot be null or empty.
     * @param   date        the date that the event should have occurred.  
     *                      Cannot be null.                      
     */
    private void removeGeneratedChargebackEvent(Lease lease, String billName, Date date) {
        for (Event event : events) {
            if (isMatchingBillEvent(event, lease, billName, date)) {
                removeEvent(event, currentProcessedEventIndex);
                break;
            }
        }
    }

    /**
     * Notifies this that the given resident has gone on vacation.
     * 
     * @param   date            the first day the resident was not in the 
     *                          apartment.  Cannot be null.
     * @param   residentName    the name of the resident who is going on 
     *                          vacation.  Cannot be null or empty.
     */
    public void vacationStarts(Date date, String residentName) {
        Resident resident = findResident(residentName);
        resident.vacationStart(date);
    }

    /**
     * Notifies this that the given resident has come back from vacation.
     * 
     * @param   date            the date the person came back to the apartment.
     *                          Cannot be null.
     * @param   residentName    the name of the resident who came back from 
     *                          vacation.  Cannot be null or empty.
     */
    public void vacationEnds(Date date, String residentName) {
        Resident resident = findResident(residentName);
        resident.vacationEnd(date);
    }

    /**
     * Changes the settings for the given bill.  If no Biller with the name 
     * exists, it will be created.
     * 
     * @param   name                the name of the bill to setup.  Cannot be 
     *                              null or empty.
     * @param   fixedCost           true if the bill does not change every 
     *                              month, false if it depends on usage.
     * @param   afterConsumption    true if the bill comes after consumption, 
     *                              false if the bill comes before consumption.
     */
    public void setupBill(String name, boolean fixedCost, boolean afterConsumption) {
        Biller biller = findBiller(name);
        if (biller == null) {
            biller = new Biller(name, firstArrival);
            addBiller(biller);
        }
        biller.setFixedCost(fixedCost);
        biller.setAfterConsumption(afterConsumption);
    }

    /**
     * EventComparator is a class for comparing Events.  Events are first 
     * separated by date, and if the date is the same, they are sorted by class.
     */
    private static class EventComparator implements Comparator<Event> {

        /**
         * Constructor for EventComparator.
         */
        public EventComparator() {
        }

        /**
         * {@inheritDoc}
         */
        public int compare(Event o1, Event o2) {
            long timeDiff = o1.getDate().getTime() - o2.getDate().getTime();
            if (timeDiff != 0) {
                return timeDiff < 0 ? -1 : 1;
            }
            int classOrderDiff = getClassOrder(o1.getClass()) - getClassOrder(o2.getClass());
            if (classOrderDiff != 0) {
                return classOrderDiff;
            }
            int result = o1.compareTo(o2);
            if (result != 0) {
                return result;
            }
            return 0;
        }

        /**
         * Returns the index that defines the order for the given class.
         * 
         * @param   type    the Event class to find the order of.  Cannot be 
         *                  null.
         *                  
         * @return  the order number of the class.  Will be >= 0.
         */
        private int getClassOrder(Class<? extends Event> type) {
            int value = EventFactory.valueOf(type.getSimpleName()).ordinal();
            if (value < 0) {
                throw new IllegalArgumentException("Invalid class " + type);
            }
            return value;
        }
    }
}
