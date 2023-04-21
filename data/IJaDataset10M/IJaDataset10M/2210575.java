package de.tum.in.elitese.wahlsys.loader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JFileChooser;
import de.tum.in.elitese.wahlsys.common.DigestHelper;
import de.tum.in.elitese.wahlsys.parser.Parser;
import de.tum.in.elitese.wahlsys.persister.Persister;
import de.tum.in.elitese.wahlsys.persister.persistence_objects.Person;
import de.tum.in.elitese.wahlsys.persister.persistence_objects.Wahlbezirk;
import de.tum.in.elitese.wahlsys.persister.persistence_objects.Wahlhelfer;
import de.tum.in.elitese.wahlsys.persister.persistence_objects.Wahlkabine;
import de.tum.in.elitese.wahlsys.persister.persistence_objects.Wahlschein;

/**
 * Main class that manages process of loading data to DB using Parser and
 * Persister. At first data are parsed with Parser, than Persister persists to
 * DB every lists of BusinessObjects got from Parser. Additionally data for
 * prototype are created and persisted.
 * 
 * @author stepan
 * 
 */
public class Wahlloader {

    private static List<Person> personenList;

    private static List<Wahlhelfer> wahlhelferList;

    private static List<Wahlkabine> wahlkabineList;

    private static List<Wahlschein> wahlscheinList;

    public static void main(String[] args) {
        File voiceF = getFileGUI("voices");
        if (voiceF == null) {
            return;
        }
        File candF = getFileGUI("candidates");
        if (candF == null) {
            return;
        }
        Parser p = new Parser(voiceF, candF);
        p.parse();
        createDataForPrototype(p.getWahlbezirks());
        long startTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss:SS");
        Date date = new Date();
        date.setTime(startTime);
        System.out.println("Starttime: " + sdf.format(date));
        System.out.println();
        System.out.println("Saving...");
        System.out.println("Bundesland " + p.getBundeslands().size());
        Persister.persist(p.getBundeslands());
        System.out.println("Wahlkreis " + p.getWahlkreises().size());
        Persister.persist(p.getWahlkreises());
        System.out.println("Wahlbezirk " + p.getWahlbezirks().size());
        Persister.persist(p.getWahlbezirks());
        System.out.println("Partei " + p.getParties().size());
        Persister.persist(p.getParties());
        System.out.println("Direktkandidaten " + p.getDirektkandidaten().size());
        Persister.persist(p.getDirektkandidaten());
        System.out.println("Kandidaten " + p.getKandidaten().size());
        Persister.persist(p.getKandidaten());
        System.out.println("Landesliste " + p.getLandeslists().size());
        Persister.persist(p.getLandeslists());
        System.out.println("Kandidatenlistrang " + p.getKLRs().size());
        Persister.persist(p.getKLRs());
        System.out.println("Erststimmen " + p.getErststimmen().size());
        Persister.persist(p.getErststimmen());
        System.out.println("Zweitenstimmen " + p.getZweitstimmen().size());
        Persister.persist(p.getZweitstimmen());
        System.out.println("Wahlhelfer " + wahlhelferList.size());
        Persister.persist(wahlhelferList);
        System.out.println("Wahlkabinen " + wahlkabineList.size());
        Persister.persist(wahlkabineList);
        System.out.println("Wahlscheine " + wahlscheinList.size());
        Persister.persist(wahlscheinList);
        System.out.println("Personen " + personenList.size());
        Persister.persist(personenList);
        long endtime = System.currentTimeMillis();
        date.setTime(endtime);
        System.out.println();
        System.out.println("Endtime: " + sdf.format(date));
        date.setTime(endtime - startTime);
        System.out.println("Duration: " + sdf.format(date));
    }

    /**
	 * Ask user to specify necessary file
	 */
    private static File getFileGUI(String name) {
        File result = null;
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Select file with " + name);
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            result = fc.getSelectedFile();
        }
        return result;
    }

    /**
	 * Creates wahlmithelfer and 3 waehler
	 * 
	 * @param wahlbezirkList
	 */
    private static void createDataForPrototype(List<Wahlbezirk> wahlbezirkList) {
        wahlhelferList = new ArrayList<Wahlhelfer>(1);
        wahlkabineList = new ArrayList<Wahlkabine>(2);
        wahlscheinList = new ArrayList<Wahlschein>(1);
        personenList = new ArrayList<Person>(1);
        Wahlhelfer helfer = new Wahlhelfer();
        helfer.setUserName("wahlhelfer");
        helfer.setPassword(DigestHelper.getInstance().getDigestForString("wahlhelfer"));
        helfer.setWahlbezirk(wahlbezirkList.get(0));
        wahlhelferList.add(helfer);
        Wahlkabine kabine = new Wahlkabine();
        kabine.setNummer(1);
        kabine.setPassword(DigestHelper.getInstance().getDigestForString("1"));
        kabine.setWahlbezirk(wahlbezirkList.get(0));
        wahlkabineList.add(kabine);
        kabine = new Wahlkabine();
        kabine.setNummer(2);
        kabine.setPassword(DigestHelper.getInstance().getDigestForString("2"));
        kabine.setWahlbezirk(wahlbezirkList.get(0));
        wahlkabineList.add(kabine);
        Person person = new Person();
        person.setPersonalausweisnummer(123456789);
        person.setName("Buktu");
        person.setVorname("Tim");
        person.setOrt("Hintertupfingen");
        person.setStrasseHausnummer("Hinter den sieben Bergen 7");
        person.setGeburtsdatum(new Date());
        person.setGeburtsort("Inthemiddleofnowhere");
        person.setPostleitzahl(99999);
        person.setWahlbezirk(wahlbezirkList.get(0));
        personenList.add(person);
        Wahlschein schein = new Wahlschein();
        schein.setHashWert(DigestHelper.getInstance().getDigestForString(person.getPersonalausweisnummer() + "12345"));
        wahlscheinList.add(schein);
        person = new Person();
        person.setPersonalausweisnummer(223456789);
        person.setName("Zaun");
        person.setVorname("Gitta");
        person.setOrt("Hintertupfingen");
        person.setStrasseHausnummer("Hinter den sieben Bergen 8");
        person.setGeburtsdatum(new Date());
        person.setGeburtsort("Linsengericht");
        person.setPostleitzahl(99999);
        person.setWahlbezirk(wahlbezirkList.get(0));
        personenList.add(person);
        schein = new Wahlschein();
        schein.setHashWert(DigestHelper.getInstance().getDigestForString(person.getPersonalausweisnummer() + "22345"));
        wahlscheinList.add(schein);
        person = new Person();
        person.setPersonalausweisnummer(323456789);
        person.setName("Lator");
        person.setVorname("Wendy");
        person.setOrt("Hintertupfingen");
        person.setStrasseHausnummer("Hinter den sieben Bergen 9");
        person.setGeburtsdatum(new Date());
        person.setGeburtsort("Buchen");
        person.setPostleitzahl(99999);
        person.setWahlbezirk(wahlbezirkList.get(0));
        personenList.add(person);
        schein = new Wahlschein();
        schein.setHashWert(DigestHelper.getInstance().getDigestForString(person.getPersonalausweisnummer() + "32345"));
        wahlscheinList.add(schein);
    }
}
