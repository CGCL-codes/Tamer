package businesstier;

import businesstier.utils.Generator;
import businesstier.utils.ValidatorUtil;
import core.SystemRegException;
import core.data_tier.ParticipantDAO;
import core.data_tier.entities.Action;
import core.data_tier.entities.Participant;
import core.data_tier.entities.Presence;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import core.data_tier.entities.Additional;

/**
 *
 * @author Lahvi
 */
public class ParticipantFacade {

    private ParticipantDAO parDataAccess;

    private Map<Long, Participant> pars;

    public ParticipantFacade() {
        pars = new TreeMap<Long, Participant>();
        String[] names = { "Petr", "Honza", "Jakub", "Vaclav", "Vojtech", "Honza" };
        String[] lNames = { "Hlaváček", "Bílý", "Merta", "Tarantik", "Letal", "Skrivanek" };
        String[] emails = { "hlavap@seznam.cz", "white@seznam.cz", "mertic@svitkov.cz", "vendy@centrum.cz", "tisek@gmail.cz", "ptak@zoo.cz" };
        long[] ids = { 505050, 191919, 181818, 110011, 226226, 727272 };
        long[] actionID = { 1, 1, 3, 4, 3, 2 };
        for (int i = 0; i < ids.length; i++) {
            createParticipant(names[i], lNames[i], emails[i], ids[i], actionID[i], null);
        }
    }

    /**
     * Vytvoří nového účastníka. AdInfo může být null. ID účastníka se vygenruje
     * automaticky v datbázi.
     * @param fName
     * @param lName
     * @param email
     * @param personalID
     * @param actionID
     * @param adInfo 
     */
    public void createParticipant(String fName, String lName, String email, long personalID, long actionID, Additional adInfo) {
        Participant p = new Participant(fName, lName, email, personalID, personalID, adInfo, actionID);
        pars.put(p.getId(), p);
    }

    /**
     * Vytvoří nového účastníka. AdInfo může být null. ID účastníka se vygenruje
     * automaticky v datbázi.
     * @param fName
     * @param lName
     * @param email
     * @param personalID
     * @param actionID
     * @param adInfo 
     */
    public void createParticipant(String fName, String lName, String email, long personalID, Collection<Long> actionID, Additional adInfo) {
        Participant p = new Participant(fName, lName, email, personalID, personalID, adInfo, actionID);
        pars.put(p.getId(), p);
    }

    /**
     * Vrátí účastníka s daným ID.
     * @param id
     * @return
     * @throws SystemRegException 
     */
    public Participant getParticipant(long id) throws SystemRegException {
        Participant res = pars.get(id);
        if (res == null) {
            throw new SystemRegException("Účastník s ID: " + id + " neexistuje!");
        }
        return res;
    }

    /**
     * Zkompletuje registraci, tzn. vygeneruje heslo a login, účastníkovi s daným
     * ID. Zároveň je potřeba ID akce, ke které se login a heslo vztahují. Po 
     * dokončení registrace se automaticky zaznamená příchod účastníkovi.
     * @param id
     * @param actionID
     * @throws SystemRegException 
     */
    public void completeReg(long id, long actionID) throws SystemRegException {
        Participant p = getParticipant(id);
        if (p.isCompleteReg()) {
            throw new SystemRegException("Registrace účastníka s ID: " + id + " " + "nemohla být dokončena, protože už dokončena byla!");
        } else {
            p.setLogin(Generator.randomString());
            p.setPassword(Generator.randomString());
            p.setCompleteReg();
            p.getPresence(actionID).addCheckIn(Calendar.getInstance());
        }
    }

    /**
     * Odstraní účastníka s daným ID.
     * @param id
     * @throws SystemRegException 
     */
    public void deleteParticipant(long id) throws SystemRegException {
        if (pars.remove(id) == null) {
            throw new SystemRegException("Účastník s ID: " + id + " nemohl " + "být odstraněn, protože neexistuje!");
        }
    }

    /**
     * Změní přítomnost účastníkovi s daným ID {@code id} na akci s ID {@code actionID}.
     * Pokud byl doposud účastník přítomen zaznamená se odchod a naopak.
     * @param id
     * @param actionID
     * @throws SystemRegException 
     */
    public void changePresence(long id, long actionID) throws SystemRegException {
        Participant p = getParticipant(id);
        if (p != null) {
            Presence pres = p.getPresence(actionID);
            if (pres.isPresent()) {
                pres.addCheckOut(Calendar.getInstance());
            } else {
                pres.addCheckIn(Calendar.getInstance());
            }
        }
    }

    /**
     * Vrátí všechny účastníky, kteří se účastní dané akce.
     * @param actionID
     * @return 
     */
    public Collection<Participant> getAllParticipants(long actionID) {
        Collection<Participant> all = pars.values();
        Collection<Participant> selecteP = new ArrayList<Participant>();
        for (Participant p : all) {
            if (p.getActionIDs().contains(actionID)) {
                selecteP.add(p);
            }
        }
        return selecteP;
    }

    /**
     * Vrátí všechny účasníka, kteří jsou zrovna na dané akaci přítomni.
     * @param actionID
     * @return 
     */
    public Collection<Participant> getPresent(long actionID) {
        Collection<Participant> all = getAllParticipants(actionID);
        Collection<Participant> pritomny = new ArrayList<Participant>();
        for (Participant p : all) {
            if (p.getPresence(actionID).isPresent()) {
                pritomny.add(p);
            }
        }
        return pritomny;
    }

    /**
     * Vrátí všechny účastníky, kteří na dané akci zrovna nejsou přítomni.
     * @param actionID
     * @return 
     */
    public Collection<Participant> getAbsent(long actionID) {
        Collection<Participant> all = getAllParticipants(actionID);
        Collection<Participant> absent = new ArrayList<Participant>();
        for (Participant p : all) {
            if (!p.getPresence(actionID).isPresent()) {
                absent.add(p);
            }
        }
        return absent;
    }

    /**
     * Vrátí všechny účastníky, kteří na dané akci mají dokončenou registraci.
     * @param actionID
     * @return 
     */
    public Collection<Participant> getCompleteRegParticipants(long actionID) {
        Collection<Participant> all = getAllParticipants(actionID);
        Collection<Participant> completReg = new ArrayList<Participant>();
        for (Participant p : all) {
            if (p.isCompleteReg()) {
                completReg.add(p);
            }
        }
        return completReg;
    }

    /**
     * Vrátí všechny účastníky, kteří na dané akci nemají dokončenou registraci.* 
     * @param actionID
     * @return 
     */
    public Collection<Participant> getIncompleteRegParticipants(long actionID) {
        Collection<Participant> all = getAllParticipants(actionID);
        Collection<Participant> incompletReg = new ArrayList<Participant>();
        for (Participant p : all) {
            if (!p.isCompleteReg()) {
                incompletReg.add(p);
            }
        }
        return incompletReg;
    }

    /**
     * Upraví přihlašovací údaje (jméno a heslo) danému účastníkovi.
     * @param id
     * @param newLogin
     * @param newPassword
     * @throws SystemRegException 
     */
    public void editLogins(long id, String newLogin, String newPassword) throws SystemRegException {
        Participant p = getParticipant(id);
        if (p.isCompleteReg()) {
            if (!ValidatorUtil.isntEmpty(newLogin) || !ValidatorUtil.isntEmpty(newPassword)) {
                throw new SystemRegException("Uživatel má dokončenou registaci, " + "login a heslo jsou povinné parametry!");
            }
            p.setLogin(newLogin);
            p.setPassword(newPassword);
        } else {
            throw new SystemRegException("Účastník nemá dokončenou registaci. " + "Nemůžete mu nastavovat přihlašovací údaje");
        }
    }

    /**
     * Upraví púdaje (email, jméno, číslo OP) danému účastníkovi
     * @param email
     * @param firstName
     * @param lastName
     * @param id
     * @throws SystemRegException 
     */
    public void editParameters(String email, String firstName, String lastName, long cardID, long id) throws SystemRegException {
        Participant p = getParticipant(id);
        if (!ValidatorUtil.isntEmpty(firstName) || !ValidatorUtil.isntEmpty(lastName) || !ValidatorUtil.isntEmpty(email)) {
            throw new SystemRegException("Email a jméno jsou povinné parametry!");
        }
        p.setFirstName(firstName);
        p.setLastName(lastName);
        p.setEmail(email);
        p.setCardID(cardID);
    }

    public void editAdditionals(long id, Additional adParams) throws SystemRegException {
        Participant p = getParticipant(id);
        p.setAddParams(adParams);
    }

    /**
     * Danému účastníkovi přidá účastn na nové akci.
     * @param id
     * @param actionID 
     */
    public void addAction(long id, long actionID) throws SystemRegException {
        Participant p = getParticipant(id);
        Collection<Long> exist = p.getActionIDs();
        if (!exist.contains(actionID)) p.addAction(actionID);
    }

    /**
     * Aktualizuje danému účastníkovi seznam účatní na akcích. Nový seznam, se 
     * účastníkovi přidá formou kolekce v parametru actions. Nejprve se porová
     * s dosavadním seznamem a zkonstroluje se, jestli nebyli nějaké akce odstraněny. 
     * Po 
     * @param id
     * @param actions
     * @throws SystemRegException 
     */
    public void addActions(long id, Collection<Long> actions) throws SystemRegException {
        Participant p = getParticipant(id);
        Collection<Long> parIDs = p.getActionIDs();
        for (Long long1 : parIDs) {
            if (!actions.contains(long1)) removeAction(id, long1);
        }
        for (Long long1 : actions) {
            addAction(id, long1);
        }
    }

    /**
     * Danému účastníkovi odebere účast na dané akci.
     * @param id
     * @param actionID 
     */
    public void removeAction(long id, long actionID) throws SystemRegException {
        Participant p = getParticipant(id);
        p.removeAction(actionID);
    }
}
