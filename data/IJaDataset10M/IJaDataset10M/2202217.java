package mts;

import java.util.Hashtable;
import java.util.Vector;
import ObjModel.*;

public class Filter {

    public BusStop[] busStops;

    public Hashtable busStopsFilter;

    public Bus[] buses;

    public Hashtable busesFilter;

    public boolean isEmpty() {
        return busStops == null && buses == TransSched.allTransportArray;
    }

    public static Bus[] FilterTransportByName(Bus[] all, String filter) {
        if (filter.length() == 0) return new Bus[0];
        Vector vec = new Vector();
        int filterLength = filter.length();
        for (int i = 0; i < all.length; i++) {
            Bus b = all[i];
            if (b.name.startsWith(filter) == false) continue;
            if (b.name.length() > filterLength) System.out.println(Character.isDigit(b.name.charAt(filterLength)));
            if (b.name.length() > filterLength && Character.isDigit(b.name.charAt(filterLength))) continue;
            vec.addElement(b);
            System.out.println("add - " + b.name);
        }
        Bus[] ret = new Bus[vec.size()];
        vec.copyInto(ret);
        return ret;
    }

    public void changeTransportFilter(int filterChangeMode, Bus[] transp) {
        if (filterChangeMode == SchedulerCanvas.FILTER_CHANGE_MODE_ADD) {
            if (buses == null) return;
            if (transp != null) {
                Vector vec = new Vector();
                for (int i = 0; i < buses.length; i++) {
                    vec.addElement(buses[i]);
                }
                for (int i = 0; i < transp.length; i++) {
                    if (busesFilter != null && busesFilter.containsKey(transp[i]) == false) {
                        vec.addElement(transp[i]);
                    }
                }
                transp = new Bus[vec.size()];
                vec.copyInto(transp);
            }
        } else if (filterChangeMode == SchedulerCanvas.FILTER_CHANGE_MODE_REMOVE) {
            if (transp == null) {
                transp = new Bus[0];
            } else {
                Vector vec = new Vector();
                Hashtable hash = new Hashtable(transp.length);
                for (int i = 0; i < transp.length; i++) {
                    if (!hash.containsKey(transp[i])) hash.put(transp[i], transp[i]);
                }
                if (buses != null) {
                    for (int i = 0; i < buses.length; i++) {
                        if (!hash.containsKey(buses[i])) vec.addElement(buses[i]);
                    }
                }
                transp = new Bus[vec.size()];
                vec.copyInto(transp);
            }
        }
        setBusesFilter(transp);
    }

    public void setBusesFilter(Bus[] filter) {
        filteredBusesHash = null;
        buses = filter;
        if (buses == null) buses = TransSched.allTransportArray;
        busesFilter = new Hashtable(buses.length);
        for (int i = 0; i < buses.length; i++) {
            if (busesFilter.containsKey(buses[i]) == false) busesFilter.put(buses[i], buses[i]);
        }
    }

    public void setBusStopsFilter(BusStop[] filter) {
        busStops = filter;
        filteredBusesHash = null;
        if (busStops == null || busStops.length == 0) {
            busStopsFilter = null;
            busStops = null;
            return;
        }
        busStopsFilter = new Hashtable(busStops.length);
        for (int i = 0; i < busStops.length; i++) {
            if (busStopsFilter.containsKey(busStops[i]) == false) busStopsFilter.put(busStops[i], busStops[i]);
        }
    }

    Hashtable filteredBusesHash;

    public Hashtable getFilteredBusesHash() {
        if (filteredBusesHash != null) return filteredBusesHash;
        BusStop[] bss = FilterIt(TransSched.allBusStopsArray);
        filteredBusesHash = new Hashtable();
        for (int i = 0; i < bss.length; i++) {
            BusStop bs = bss[i];
            Schedule[] filteredScheds = FilterIt(bs.schedules);
            for (int j = 0; j < filteredScheds.length; j++) {
                Bus b = filteredScheds[j].bus;
                if (filteredBusesHash.containsKey(b) == false) filteredBusesHash.put(b, b);
            }
        }
        return filteredBusesHash;
    }

    public BusStop[] FilterIt(BusStop[] all) {
        Vector v = new Vector();
        for (int i = 0; i < all.length; i++) {
            BusStop bs = all[i];
            if (busStopsFilter != null && !busStopsFilter.containsKey(bs)) continue;
            boolean add = true;
            if (busesFilter != null) {
                add = false;
                for (int j = 0; j < bs.schedules.length; j++) {
                    if (busesFilter.containsKey(bs.schedules[j].bus)) {
                        add = true;
                        break;
                    }
                }
            }
            if (add) v.addElement(bs);
        }
        BusStop[] ret = new BusStop[v.size()];
        v.copyInto(ret);
        return ret;
    }

    public BusStop[] getFavorites(BusStop[] all) {
        Vector v = new Vector();
        for (int i = 0; i < all.length; i++) {
            BusStop bs = all[i];
            if (bs.favorite == false) continue;
            v.addElement(bs);
        }
        BusStop[] ret = new BusStop[v.size()];
        v.copyInto(ret);
        return ret;
    }

    public Bus[] getFavorites(Bus[] all) {
        Vector v = new Vector();
        for (int i = 0; i < all.length; i++) {
            Bus b = all[i];
            if (b.favorite == false) continue;
            v.addElement(b);
        }
        Bus[] ret = new Bus[v.size()];
        v.copyInto(ret);
        return ret;
    }

    public Schedule[] FilterIt(Schedule[] all) {
        Vector v = new Vector();
        for (int i = 0; i < all.length; i++) {
            Schedule sched = all[i];
            if (busesFilter != null && !busesFilter.containsKey(sched.bus)) continue;
            v.addElement(sched);
        }
        Schedule[] ret = new Schedule[v.size()];
        v.copyInto(ret);
        return ret;
    }
}
