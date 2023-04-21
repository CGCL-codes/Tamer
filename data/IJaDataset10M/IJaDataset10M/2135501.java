package org.santeplanning.model;

import java.sql.SQLException;
import java.util.GregorianCalendar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.stateengine.storage.AnsiDate;
import org.stateengine.storage.HibernateFactory;
import org.stateengine.storage.IDBConnection;

public class TravailFactory extends HibernateFactory {

    private static Log log = LogFactory.getLog(TravailFactory.class);

    public Travail createNew(IDBConnection connection, AnsiDate day, Employee employee, TypeHoraire typeHoraire, Service service) {
        Travail travail = new Travail();
        travail.setJour(day);
        travail.setEmployee(employee);
        travail.setTypeHoraire(typeHoraire);
        travail.setHeureDimanche(0f);
        travail.setHeureNormales(0f);
        travail.setHeureNuit(0f);
        travail.setHeureSuppl(0f);
        travail.setHeureFerieTravPaye(0f);
        travail.setHeureFerieTravRecup(0f);
        travail.setFerieNonTrav(0f);
        travail.setFeriePaye1sur24(0f);
        travail.setGainReposNuit(typeHoraire.getGainReposCompensateurNuit());
        travail.setCongesPaye(typeHoraire.getHeureConges());
        travail.setCongesSupplementaire(typeHoraire.getCongesSupplementaire());
        travail.setAbscenceDivers(typeHoraire.getJourMaladie() + typeHoraire.getJourAbsence());
        travail.setService(service);
        travail.setCommentaire("");
        if (day.isDayOfWeek(GregorianCalendar.SUNDAY)) {
            if (typeHoraire.getHeureNuit() > 0f) {
                AnsiDate minuit = day.getDayDate();
                minuit.ajouter(GregorianCalendar.DAY_OF_YEAR, 1);
                if (estFerie(connection, minuit)) {
                    AnsiDate beginHour = day.getDayDate();
                    beginHour.ajouter(GregorianCalendar.HOUR, typeHoraire.getHeureDebut().intValue());
                    beginHour.ajouter(GregorianCalendar.MINUTE, typeHoraire.getMinuteDebut().intValue());
                    int hour = diffHour(beginHour, minuit);
                    travail.setHeureDimanche(hour);
                    travail.setHeureFerieTravPaye(typeHoraire.getHeureNuit() - hour);
                } else {
                    AnsiDate beginHour = day.getDayDate();
                    beginHour.ajouter(GregorianCalendar.HOUR, typeHoraire.getHeureDebut().intValue());
                    beginHour.ajouter(GregorianCalendar.MINUTE, typeHoraire.getMinuteDebut().intValue());
                    int hour = diffHour(beginHour, minuit);
                    travail.setHeureDimanche(hour);
                    travail.setHeureNuit(typeHoraire.getHeureNuit() - hour);
                }
            } else {
                travail.setHeureDimanche(typeHoraire.getHeureNormales());
            }
        } else if (estFerie(connection, day)) {
            if (typeHoraire.getHeureNuit() > 0f) {
                AnsiDate minuit = day.getDayDate();
                minuit.ajouter(GregorianCalendar.DAY_OF_YEAR, 1);
                if (estFerie(connection, minuit)) {
                    travail.setHeureFerieTravPaye(typeHoraire.getHeureNuit());
                } else if (minuit.isDayOfWeek(GregorianCalendar.SUNDAY)) {
                    AnsiDate beginHour = day.getDayDate();
                    beginHour.ajouter(GregorianCalendar.HOUR, typeHoraire.getHeureDebut().intValue());
                    beginHour.ajouter(GregorianCalendar.MINUTE, typeHoraire.getMinuteDebut().intValue());
                    int hour = diffHour(beginHour, minuit);
                    travail.setHeureFerieTravPaye(hour);
                    travail.setHeureDimanche(typeHoraire.getHeureNuit() - hour);
                } else {
                    AnsiDate beginHour = day.getDayDate();
                    beginHour.ajouter(GregorianCalendar.HOUR, typeHoraire.getHeureDebut().intValue());
                    beginHour.ajouter(GregorianCalendar.MINUTE, typeHoraire.getMinuteDebut().intValue());
                    int hour = diffHour(beginHour, minuit);
                    travail.setHeureFerieTravPaye(hour);
                    travail.setHeureNuit(typeHoraire.getHeureNuit() - hour);
                }
            } else {
                travail.setHeureFerieTravPaye(typeHoraire.getHeureNormales());
            }
        } else {
            if (typeHoraire.getHeureNuit() > 0f) {
                AnsiDate minuit = day.getDayDate();
                minuit.ajouter(GregorianCalendar.DAY_OF_YEAR, 1);
                if (estFerie(connection, minuit)) {
                    AnsiDate beginHour = day.getDayDate();
                    beginHour.ajouter(GregorianCalendar.HOUR, typeHoraire.getHeureDebut().intValue());
                    beginHour.ajouter(GregorianCalendar.MINUTE, typeHoraire.getMinuteDebut().intValue());
                    int hour = diffHour(beginHour, minuit);
                    travail.setHeureNuit(hour);
                    travail.setHeureFerieTravPaye(typeHoraire.getHeureNuit() - hour);
                } else if (minuit.isDayOfWeek(GregorianCalendar.SUNDAY)) {
                    AnsiDate beginHour = day.getDayDate();
                    beginHour.ajouter(GregorianCalendar.HOUR, typeHoraire.getHeureDebut().intValue());
                    beginHour.ajouter(GregorianCalendar.MINUTE, typeHoraire.getMinuteDebut().intValue());
                    int hour = diffHour(beginHour, minuit);
                    travail.setHeureNuit(hour);
                    travail.setHeureDimanche(typeHoraire.getHeureNuit() - hour);
                } else {
                    travail.setHeureNuit(typeHoraire.getHeureNuit());
                }
            } else {
                travail.setHeureNormales(typeHoraire.getHeureNormales());
            }
        }
        return travail;
    }

    public boolean modifiedTravail(IDBConnection connection, Travail travail) {
        if (travail == null) return false;
        if (travail.getTypeHoraire() == null) return false;
        Travail testor = createNew(connection, travail.getJour(), travail.getEmployee(), travail.getTypeHoraire(), travail.getService());
        if ((testor.getHeureNormales() == travail.getHeureNormales()) && (testor.getHeureDimanche() == travail.getHeureDimanche()) && (testor.getHeureNuit() == travail.getHeureNuit()) && (testor.getHeureSuppl() == travail.getHeureSuppl()) && (testor.getHeureFerieTravPaye() == travail.getHeureFerieTravPaye()) && (testor.getHeureFerieTravRecup() == travail.getHeureFerieTravRecup()) && (testor.getAbscenceDivers() == travail.getAbscenceDivers()) && (testor.getGainReposNuit() == travail.getGainReposNuit()) && (testor.getCongesPaye() == travail.getCongesPaye()) && (testor.getCongesSupplementaire() == travail.getCongesSupplementaire()) && testor.getCommentaire().equals(travail.getCommentaire())) {
            return false;
        }
        return true;
    }

    public int diffHour(AnsiDate begin, AnsiDate end) {
        System.out.println(begin + "->" + end);
        return (int) minuteToHour2(begin.differenceWithInMinute(end));
    }

    public long minuteToHour2(long minute) {
        float fMinute = minute;
        fMinute = fMinute / 60;
        int result = Math.round(fMinute);
        System.out.println(minute + "->" + result);
        return result;
    }

    public boolean estFerie(IDBConnection db, AnsiDate day) {
        Ferie ferie = FerieEnumeration.getFromDate(db, day);
        if (ferie != null) {
            System.out.println(ferie.getJour().getAnsi());
            return true;
        }
        return false;
    }

    public String getId(Object o) {
        if (o == null) return null;
        if (((Travail) o).getTravailId() == null) return null;
        return ((Travail) o).getTravailId().toString();
    }

    public Log getLogger() {
        Log log = LogFactory.getLog(TravailFactory.class);
        return log;
    }

    public Class getTargetClass() {
        return Travail.class;
    }

    public Object load(IDBConnection db, String id) throws SQLException {
        return TravailEnumeration.getFromId(db, new Integer(id));
    }

    private static TravailFactory theInstance = new TravailFactory();

    public static TravailFactory instance() {
        return theInstance;
    }

    public static boolean isEmpty(Travail travail) {
        if ((travail.getHeureNormales() == 0f) && (travail.getHeureDimanche() == 0f) && (travail.getHeureNuit() == 0f) && (travail.getHeureFerieTravPaye() == 0f) && (travail.getHeureFerieTravRecup() == 0f) && (travail.getFeriePaye1sur24() == 0f) && (travail.getFerieNonTrav() == 0f) && (travail.getFerieRjm() == 0f) && (travail.getGainReposNuit() == 0f) && (travail.getCongesPaye() == 0f) && (travail.getCongesSupplementaire() == 0f) && (travail.getAbscenceDivers() == 0f)) {
            return true;
        }
        return false;
    }
}
