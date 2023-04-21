package org.jazzteam.edu.oop.bank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Bank {

    public static final int MAX_ROOM_NUMBER = 100;

    private List<Worker> workers = new ArrayList<Worker>();

    private Director director;

    private Map<Integer, Integer> workersLocation = new HashMap<Integer, Integer>();

    private static int currentFreeRoom = 0;

    public void addWorker(final Worker worker) {
        currentFreeRoom++;
        if (currentFreeRoom >= MAX_ROOM_NUMBER) {
            throw new IllegalStateException("Worker can not be added as we do not have free rooms");
        }
        workers.add(worker);
        worker.setRoom(currentFreeRoom);
        workersLocation.put(worker.getId(), currentFreeRoom);
    }

    public int getWorkerLocation(final Worker w) {
        if (null == w) {
            throw new IllegalStateException();
        }
        return workersLocation.get(w.getId());
    }

    public List<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(List<Worker> workers) {
        this.workers = workers;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((director == null) ? 0 : director.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Bank other = (Bank) obj;
        if (director == null) {
            if (other.director != null) return false;
        } else if (!director.equals(other.director)) return false;
        return true;
    }

    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }
}
