package net.googlecode.exigenlab.task5.sorting;

import net.googlecode.exigenlab.task5.salary.SalaryCalculator;
import net.googlecode.exigenlab.task5.personal.Worker;
import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Class for sorting and print information about workers.
 * 
 * @author Poddybniak Petr <a href="mailto:PPoddybniak@gmail.com">Poddybniak
 *         Petr</a>
 * @since 13.04.2011
 */
public class SortingTools {

    SalaryCalculator cal = new SalaryCalculator();

    /**
	 * Default constructor.
	 * 
	 */
    public SortingTools() {
    }

    /**
	 * Sort input Collection by id. (workers with the maximum and minimum salary
	 * didn't enter to the report)
	 * 
	 * @param workers
	 *            Collection of Workers
	 */
    public void idSortedWorkers(Collection<Worker> workers) {
        double minSalary, maxSalary;
        SalaryComparator salaryComparator = new SalaryComparator();
        IdComparator idComparator = new IdComparator();
        SortedSet<Worker> salarySorted = new TreeSet<Worker>(salaryComparator);
        salarySorted.addAll(workers);
        minSalary = salarySorted.first().getSalary();
        maxSalary = salarySorted.last().getSalary();
        SortedSet<Worker> idSortedSet = new TreeSet<Worker>(idComparator);
        idSortedSet.addAll(workers);
        for (Worker worker : idSortedSet) {
            if ((worker.getSalary() != minSalary) && (worker.getSalary() != maxSalary)) System.out.println(worker);
        }
    }

    /**
	 * Sort input Collection by Last name. (workers which age over 45 years
	 * didn't enter to the report)
	 * 
	 * @param workerList
	 *            Collection of Workers
	 */
    public void nameSortedWorkers(Collection<Worker> workerList) {
        LastNameComparator lastNameComparator = new LastNameComparator();
        SortedSet<Worker> lastNameSorted = new TreeSet<Worker>(lastNameComparator);
        lastNameSorted.addAll(workerList);
        for (Worker worker : lastNameSorted) {
            if (worker.getAge() <= 45) {
                System.out.println(worker);
            }
        }
    }

    /**
	 * Sort input Collection by salary. (workers with the same salary didn't
	 * enter to the report)
	 * 
	 * @param workerList
	 *            Collection of Workers
	 */
    public void salarySortedWorkers(Collection<Worker> workerList) {
        SalaryComparator salaryComparator = new SalaryComparator();
        double compareSalary = 0;
        SortedSet<Worker> salarySortedSet = new TreeSet<Worker>(salaryComparator);
        salarySortedSet.addAll(workerList);
        for (Worker worker : salarySortedSet) {
            if (worker.getSalary() != compareSalary) {
                System.out.println(worker);
                compareSalary = worker.getSalary();
            }
        }
    }

    /**
	 * Comparator which compare workers by salary.
	 * 
	 */
    class SalaryComparator implements Comparator<Worker> {

        public int compare(Worker o1, Worker o2) {
            return (o2.getSalary().compareTo(o1.getSalary()));
        }
    }

    /**
	 * Comparator which compare workers by id.
	 */
    class IdComparator implements Comparator<Worker> {

        public int compare(Worker o1, Worker o2) {
            return o1.getId().compareTo(o2.getId());
        }
    }

    /**
	 * Comparator which compare workers by Last Name.
	 */
    class LastNameComparator implements Comparator<Worker> {

        public int compare(Worker o1, Worker o2) {
            return o1.getLastName().compareToIgnoreCase(o2.getLastName());
        }
    }
}
