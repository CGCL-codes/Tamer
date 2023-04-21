package EcoSpeed;

import java.util.Calendar;

/**
 * Classe des methodes et valeur de paramètrage d'EcoSpeed
 */
public class Utils {

    /**
	 * Nombre d'étage
	 */
    public static int NB_FLOORS = 45;

    /**
     * Nombre de sous sols
     */
    public static int NB_UNDERGROUND = 4;

    /**
     * Nombre d'ascenseur
     */
    public static int NB_LIFTS = 6;

    /**
     * Pondération pour replacement de l'ascensuer dans sa zone
     */
    public static double RELOCATION_WEIGHT = 2.0 / 3.0;

    /**
     * Return the reprsentation id of the next day from
     * the parameter day.
     * @param currentDay the current day
     * @return numero du jour suivant
     */
    public static int nextDay(int currentDay) {
        if (currentDay == Calendar.SATURDAY) {
            return Calendar.SUNDAY;
        } else {
            return (currentDay + 1);
        }
    }

    /**
     * Donne le numéro d'un jour en fonction d'une chaine
     * @param day nom du jour
     * @return numero du jour
     */
    public static int getDay(String day) {
        if (day.toUpperCase().equals("MONDAY")) return Calendar.MONDAY; else if (day.toUpperCase().equals("TUESDAY")) return Calendar.TUESDAY; else if (day.toUpperCase().equals("WEDNESDAY")) return Calendar.WEDNESDAY; else if (day.toUpperCase().equals("THURSDAY")) return Calendar.THURSDAY; else if (day.toUpperCase().equals("FRIDAY")) return Calendar.FRIDAY; else if (day.toUpperCase().equals("SATURDAY")) return Calendar.SATURDAY; else if (day.toUpperCase().equals("SUNDAY")) return Calendar.SUNDAY; else return -1;
    }

    /**
      * Donne le nom du jour en fonction se son numero
      * @param day numero du jour
      * @return nom du jour
      */
    public static String getDay(int day) {
        if (day == Calendar.MONDAY) return "MONDAY"; else if (day == Calendar.TUESDAY) return "TUESDAY"; else if (day == Calendar.WEDNESDAY) return "WEDNESDAY"; else if (day == Calendar.THURSDAY) return "THURSDAY"; else if (day == Calendar.FRIDAY) return "FRIDAY"; else if (day == Calendar.SATURDAY) return "SATURDAY"; else if (day == Calendar.SUNDAY) return "SUNDAY"; else return null;
    }
}
