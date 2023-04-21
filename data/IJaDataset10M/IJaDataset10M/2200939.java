package databases;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import dataClass.Course;
import exceptions.CourseDoesNotExistException;
import exceptions.FilePermissionException;

/**
 * CourseBase contains all valid read from databases.
 * It uses the DatabaseReader to read the course from the databases.
 * @author Niels Thykier
 * @author Frederik Nordahl Sabroe
 * @author Morten Sørensen
 */
public class CourseBase {

    /**
	 * enum that handles the (file-)name (and the type) of the databases.
	 * Each enum contains a filename of one of the databasefiles.
	 * @author Niels Thykier
	 */
    enum DatabaseFiles {

        /**
		 * The name database for long courses. 
		 */
        NAME_LONG("navne.txt"), /**
		 * The name database for short courses.
		 */
        NAME_SHORT("navne3uger.txt"), /**
		 * The cross semester databases for courses extending more than one period/semester.
		 */
        MULTI_PERIOD("flerperioder.txt"), /**
		 * The short course database for course appearing in three week periods and are not multi-period courses.
		 */
        SHORT_COURSE("skemagrp3uger.txt"), /**
		 * The Skema database
		 */
        SKEMA("skgrpKrav13.txt"), /**
		 * The dependency database
		 */
        DEPENDENCY("forud.txt");

        /**
		 * The filename related to the given enum.
		 */
        private String filename;

        /**
		 * Constructor for the enums
		 * @param filename
		 */
        DatabaseFiles(String filename) {
            this.filename = filename;
        }

        /**
		 * Used to get the filename related to the enum.
		 * @return The filename
		 * @see java.lang.Enum#toString()
		 */
        public String toString() {
            return filename;
        }
    }

    /**
	 * An ArrayList containing all the courses from the databases 
	 */
    private Course[] allCourses;

    /**
	 * DatabaseReader, used whenever it is reloading all the courses or searching in the 
	 * databases.
	 */
    private DatabaseReader dbRead;

    /**
	 * Statistic data: time (in milliseconds) it took from start of reload till the finish of parsing the file.
	 */
    private Long[] loadTimeFile = new Long[DatabaseFiles.values().length];

    /**
	 * Statistic data: time (in milliseconds) it took to finish the complete load.
	 */
    private Long loadTime;

    /**
	 * The amount of courses that are only in short periods 
	 */
    int amountOfShortCourses;

    /**
	 * The amount of courses that takes more than one period.
	 */
    int amountOfMultiPeriodCourses;

    /**
	 * The amount of courses, which appeared twice in the databases.
	 */
    int amountOfDoubleCourses;

    /**
	 * The amount of courses, which have dependencies.
	 */
    int amountOfCoursesWithDependencies;

    /**
	 * Constructs a new CourseBase that contains all valid courses.
	 * @throws FileNotFoundException if the database files could not be found.
	 * @throws FilePermissionException if the files could not be read due to missing permissions
	 */
    public CourseBase() throws FileNotFoundException, FilePermissionException {
        dbRead = new DatabaseReader();
        reloadDatabase();
    }

    /**
	 * Used to (re-)load all the courses from the databases.
	 * The previous list of the Courses are voided, so only the courses in the databases
	 * at the time of reloading will appear in the CourseBase, when it is done.
	 */
    private void reloadDatabase() {
        this.allCourses = dbRead.loadAllCourses().toArray(new Course[1]);
    }

    /**
	 * Get the amount of courses loaded (statistical data) 
	 * @return The amount of courses that was loaded during the last reload.
	 */
    public int getAmountOfCourses() {
        return allCourses.length;
    }

    /**
	 * Get the load average for Courses per second (statistical data)
	 * @return The load average meassured in courses per second. 
	 */
    public long getLoadAverage() {
        if (loadTime == 0) return 0;
        return (allCourses.length * 1000) / loadTime;
    }

    /**
	 * Get the time it took to load the courses in the last load/reload. (statistical data) 
	 * @return the time in milliseconds it took to do the last load/reload.
	 */
    public long getLoadTime() {
        return loadTime;
    }

    /**
	 * Look up a Course in the CourseBase by its ID.
	 * @param courseID The ID of the course to find.
	 * @return The Course with that given ID.
	 * @throws CourseDoesNotExistException Thrown if the course does not exist.
	 */
    public Course findCourse(String courseID) throws CourseDoesNotExistException {
        int size = allCourses.length;
        for (int i = 0; i < size; i++) {
            if (allCourses[i].isSameCourseID(courseID)) {
                return allCourses[i];
            }
        }
        throw new CourseDoesNotExistException(courseID);
    }

    /**
	 * Search through the database files for a course containing a pattern.
	 * @param pattern The pattern to search for. 
	 * @return The Courses that match the pattern in some way.
	 * @throws CourseDoesNotExistException
	 */
    public Course[] search(String pattern) throws CourseDoesNotExistException {
        ArrayList<Course> match = new ArrayList<Course>();
        Scanner scan;
        String patternLower = pattern.toLowerCase();
        int size = getAmountOfCourses();
        for (int i = 0; i < size; i++) {
            scan = new Scanner(allCourses[i].toString().toLowerCase());
            if (null != scan.findInLine("(" + patternLower + ")")) {
                match.add(allCourses[i]);
            }
        }
        if (match.size() < 1) {
            throw new CourseDoesNotExistException("indeholdende \"" + pattern + "\"");
        }
        return match.toArray(new Course[1]);
    }

    /**
	 * Get an array of all the courses in the CourseBase.
	 * @return All the courses in the CourseBase as a Array.
	 */
    public Course[] getAllCourses() {
        if (allCourses == null) return null;
        int size = allCourses.length;
        Course[] copy = new Course[size];
        for (int i = 0; i < size; i++) {
            copy[i] = allCourses[i];
        }
        return copy;
    }

    /**
	 * Get the statistical data about load times as a string.
	 * 
	 * @return The Statistical Data from the last Database (re)load.
	 */
    public String getStatisticalData() {
        String toReturn = "+---------------------------------+\n";
        String line[] = { "|Load Statistics:", "|Total load time: " + loadTime + "ms", "|half-times:", "| - name (long) load: " + loadTimeFile[DatabaseFiles.NAME_LONG.ordinal()] + "ms", "| - name (short) load: " + loadTimeFile[DatabaseFiles.NAME_SHORT.ordinal()] + "ms", "| - short course load: " + loadTimeFile[DatabaseFiles.SHORT_COURSE.ordinal()] + "ms", "| - multi-period load: " + loadTimeFile[DatabaseFiles.MULTI_PERIOD.ordinal()] + "ms", "| - skema load: " + loadTimeFile[DatabaseFiles.SKEMA.ordinal()] + "ms", "| - dependency load: " + loadTimeFile[DatabaseFiles.DEPENDENCY.ordinal()] + "ms", "|Total amount of courses: " + allCourses.length, "| - Short courses: " + amountOfShortCourses, "| - Multi-period courses: " + amountOfMultiPeriodCourses, "| - Courses with Dependencies: " + amountOfCoursesWithDependencies, "|Removed " + amountOfDoubleCourses + " duplicate courses." };
        for (int i = 0; i < 13; i++) {
            while (line[i].length() < 34) {
                line[i] += " ";
            }
            line[i] += "|";
            toReturn += line[i] + "\n";
        }
        toReturn += "+---------------------------------+";
        return toReturn;
    }

    /**
	 * Prints a list of all the courses in the CourseBase.
	 * @see java.lang.Object#toString()
	 * @return A list of all the courses in the CourseBase
	 */
    public String toString() {
        String toReturn = "Kursus list: \n";
        int size = allCourses.length;
        for (int i = 0; i < size; i++) {
            toReturn += allCourses[i].toString() + "\n";
        }
        return toReturn;
    }

    /**
	 * Reads and parses all the database files. This is used by CourseBase to load all the courses.
	 * 
	 * @author Niels Thykier
	 */
    private class DatabaseReader {

        /**
		 * Files objects, the databases loaded into memory.
		 */
        private BufferedReader database[] = new BufferedReader[DatabaseFiles.values().length];

        /**
		 * Opens the three database files for reading.
		 * @throws FileNotFoundException If one of the database files are missing.
		 * @throws FilePermissionException if permissions is missing to read the file
		 */
        public DatabaseReader() throws FileNotFoundException, FilePermissionException {
            DatabaseFiles[] array = DatabaseFiles.values();
            for (int i = 0; i < array.length; i++) {
                database[i] = openFile(array[i].toString());
            }
        }

        /**
		 * This is called by the constructor to open the database files.
		 * @param filename The name of the file to open.
		 * @return A BufferedReader to read the file.
		 * @throws FileNotFoundException if the file is not found.
		 * @throws FilePermissionException if permissions is missing to read the file
		 */
        private BufferedReader openFile(String filename) throws FileNotFoundException, FilePermissionException {
            File f = new File(filename);
            if (!f.exists()) throw new FileNotFoundException(f.getAbsolutePath());
            if (!f.canRead()) throw new FilePermissionException("read");
            return new BufferedReader(new FileReader(f));
        }

        /**
		 * This loads all courses into memory from the files. 
		 * 
		 * This method will also update some statisical data in CourseBase
		 * 
		 * @return An ArrayList containing all the valid courses that could be loaded.
		 */
        public ArrayList<Course> loadAllCourses() {
            ArrayList<Course> name = new ArrayList<Course>();
            ArrayList<Course> skema = new ArrayList<Course>();
            ArrayList<Course> multiPeriod = new ArrayList<Course>();
            ArrayList<Course> shortCourse = new ArrayList<Course>();
            String input, data[];
            long startTime = System.currentTimeMillis();
            try {
                while ((input = database[DatabaseFiles.NAME_LONG.ordinal()].readLine()) != null) {
                    data = input.split(" ", 2);
                    try {
                        if (data[0].length() == 5 && Integer.parseInt(data[0]) > -1) {
                            name.add(new Course(data[0], data[1]));
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            } catch (IOException e) {
            }
            loadTimeFile[DatabaseFiles.NAME_LONG.ordinal()] = System.currentTimeMillis() - startTime;
            try {
                while ((input = database[DatabaseFiles.NAME_SHORT.ordinal()].readLine()) != null) {
                    data = input.split(" ", 2);
                    try {
                        if (data[0].length() == 5 && Integer.parseInt(data[0]) > -1) {
                            name.add(new Course(data[0], data[1]));
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            } catch (IOException e) {
            }
            loadTimeFile[DatabaseFiles.NAME_SHORT.ordinal()] = System.currentTimeMillis() - startTime;
            int size = name.size();
            try {
                while ((input = database[DatabaseFiles.SHORT_COURSE.ordinal()].readLine()) != null) {
                    data = input.split(" ", 2);
                    try {
                        if (data[0].length() == 5 && Integer.parseInt(data[0]) > -1) {
                            for (int i = 0; i < size; i++) {
                                Course course = name.get(i);
                                if (course.isSameCourseID(data[0])) {
                                    course.updateSeason(data[1], false);
                                    shortCourse.add(course);
                                    break;
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            } catch (IOException e) {
            }
            amountOfShortCourses = shortCourse.size();
            loadTimeFile[DatabaseFiles.SHORT_COURSE.ordinal()] = System.currentTimeMillis() - startTime;
            try {
                while ((input = database[DatabaseFiles.MULTI_PERIOD.ordinal()].readLine()) != null) {
                    data = input.split(" ", 2);
                    try {
                        if (data[0].length() == 5 && Integer.parseInt(data[0]) > -1) {
                            for (int i = 0; i < size; i++) {
                                Course course = name.get(i);
                                if (course.isSameCourseID(data[0])) {
                                    course.updateSeason(data[1], true);
                                    multiPeriod.add(course);
                                    break;
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            } catch (IOException e) {
            }
            amountOfMultiPeriodCourses = multiPeriod.size();
            loadTimeFile[DatabaseFiles.MULTI_PERIOD.ordinal()] = System.currentTimeMillis() - startTime;
            try {
                while ((input = database[DatabaseFiles.SKEMA.ordinal()].readLine()) != null) {
                    data = input.split(" ", 2);
                    try {
                        if (data[0].length() == 5 && Integer.parseInt(data[0]) > -1) {
                            for (int i = 0; i < size; i++) {
                                Course course = name.get(i);
                                if (course.isSameCourseID(data[0])) {
                                    course.setSkemagruppe(data[1], true);
                                    skema.add(course);
                                    break;
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            } catch (IOException e) {
            }
            loadTimeFile[DatabaseFiles.SKEMA.ordinal()] = System.currentTimeMillis() - startTime;
            int depending = 0;
            size = skema.size();
            try {
                while ((input = database[DatabaseFiles.DEPENDENCY.ordinal()].readLine()) != null) {
                    data = input.split(" ", 2);
                    try {
                        if (data[0].length() == 5 && Integer.parseInt(data[0]) > -1) {
                            for (int i = 0; i < size; i++) {
                                Course course = skema.get(i);
                                if (course.isSameCourseID(data[0])) {
                                    course.setDependencies(data[1]);
                                    depending++;
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println(e);
                    }
                }
            } catch (IOException e) {
            }
            amountOfCoursesWithDependencies = depending;
            loadTimeFile[DatabaseFiles.DEPENDENCY.ordinal()] = System.currentTimeMillis() - startTime;
            Course temp1, temp2;
            int doubleCourses = 0;
            for (int i = 0; i < skema.size(); i++) {
                temp1 = skema.get(i);
                for (int j = i; j < skema.size(); j++) {
                    temp2 = skema.get(j);
                    if ((i < j) && temp1.equals(temp2)) {
                        doubleCourses++;
                    }
                }
            }
            amountOfDoubleCourses = doubleCourses;
            for (int i = 0; i < database.length; i++) {
                try {
                    database[i].close();
                } catch (IOException e) {
                }
            }
            loadTime = System.currentTimeMillis() - startTime;
            return skema;
        }
    }
}
