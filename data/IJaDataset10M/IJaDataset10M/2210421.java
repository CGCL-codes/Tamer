package fr.fg.server.scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import fr.fg.server.util.LoggingSystem;
import fr.fg.server.util.Utilities;

public abstract class JobScheduler<E> {

    private Map<E, Job<E>> elements;

    private List<Job<E>> jobs;

    public JobScheduler() {
        this.jobs = Collections.synchronizedList(new LinkedList<Job<E>>());
        this.elements = Collections.synchronizedMap(new HashMap<E, Job<E>>());
    }

    public void addJob(E e, long time) {
        if (elements.containsKey(e)) jobs.remove(elements.get(e));
        Job<E> newJob = new Job<E>(e, time);
        elements.put(e, newJob);
        synchronized (jobs) {
            Iterator<Job<E>> iterator = jobs.iterator();
            int i = 0;
            while (iterator.hasNext()) {
                Job<E> job = iterator.next();
                if (job.time == time && job.e.equals(e)) return;
                if (job.time > time) {
                    jobs.add(i, newJob);
                    return;
                }
                i++;
            }
            jobs.add(i, newJob);
        }
    }

    public void execute() {
        ArrayList<Job<E>> jobs = new ArrayList<Job<E>>(this.jobs);
        long now = Utilities.now();
        ArrayList<E> elements = new ArrayList<E>();
        Iterator<Job<E>> iterator = jobs.iterator();
        while (iterator.hasNext()) {
            Job<E> job = iterator.next();
            if (job.time > now) break;
            this.jobs.remove(job);
            this.elements.remove(job.e);
            elements.add(job.e);
        }
        if (elements.size() > 0) for (E e : elements) {
            try {
                process(e, now);
            } catch (Exception ex) {
                LoggingSystem.getServerLogger().warn("Could not process element.", ex);
            }
        }
    }

    public abstract void process(E e, long time);

    private static class Job<E> {

        private long time;

        private E e;

        public Job(E e, long time) {
            this.e = e;
            this.time = time;
        }
    }
}
