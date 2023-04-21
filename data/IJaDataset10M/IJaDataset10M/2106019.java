package com.alturatec.dienstreise.persistence;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.eclipse.core.runtime.IPath;
import com.alturatec.dienstreise.internal.DienstreiseJRCAFClientPlugin;
import com.alturatec.dienstreise.model.Kunde;
import com.alturatec.dienstreise.model.Ort;

public class PersistenceManager implements Serializable {

    private static final String DIENSTREISEN_FILE_NAME = "dienstreisen.dat";

    private static PersistenceManager singleton;

    private List<Kunde> kunden = new ArrayList<Kunde>();

    private List<Ort> orte = new ArrayList<Ort>();

    public static List<Kunde> getKunden() {
        if (singleton == null) init();
        return singleton.kunden;
    }

    private static void init() {
        IPath stateLocation = DienstreiseJRCAFClientPlugin.getDefault().getStateLocation();
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(stateLocation.append(DIENSTREISEN_FILE_NAME).toFile()));
            singleton = (PersistenceManager) objectInputStream.readObject();
        } catch (FileNotFoundException ex) {
            try {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(stateLocation.append(DIENSTREISEN_FILE_NAME).toFile()));
                singleton = new PersistenceManager();
                objectOutputStream.writeObject(singleton);
            } catch (FileNotFoundException ex1) {
                ex1.printStackTrace();
            } catch (IOException ex1) {
                ex1.printStackTrace();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public static void commit() {
        try {
            IPath stateLocation = DienstreiseJRCAFClientPlugin.getDefault().getStateLocation();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(stateLocation.append(DIENSTREISEN_FILE_NAME).toFile()));
            objectOutputStream.writeObject(singleton);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static List<Ort> searchOrte(Map<String, Object> aSearchParameters) {
        List<Ort> foundOrte = new ArrayList<Ort>();
        for (Ort ort : singleton.orte) if (matches(ort, aSearchParameters) == true) foundOrte.add(ort);
        return foundOrte;
    }

    private static boolean matches(Ort aOrt, Map<String, Object> aSearchParameters) {
        String stringSearchParameter = (String) aSearchParameters.get(".name");
        if ((stringSearchParameter.length() > 0) && (aOrt.getName().startsWith(stringSearchParameter) == false)) return false;
        stringSearchParameter = (String) aSearchParameters.get(".strasse");
        if ((stringSearchParameter.length() > 0) && (aOrt.getStrasse().startsWith(stringSearchParameter) == false)) return false;
        stringSearchParameter = (String) aSearchParameters.get(".hausnummer");
        if ((stringSearchParameter.length() > 0) && (aOrt.getHausnummer().startsWith(stringSearchParameter) == false)) return false;
        stringSearchParameter = (String) aSearchParameters.get(".plz");
        if ((stringSearchParameter.length() > 0) && (Integer.toString(aOrt.getPLZ()).startsWith(stringSearchParameter) == false)) return false;
        stringSearchParameter = (String) aSearchParameters.get(".ort");
        if ((stringSearchParameter.length() > 0) && (aOrt.getOrt().startsWith(stringSearchParameter) == false)) return false;
        Object searchParameter = aSearchParameters.get(".privat");
        searchParameter = aSearchParameters.get(".eigeneFirma");
        return true;
    }

    public static void addOrt(Ort aNeuerOrt) {
        singleton.orte.add(aNeuerOrt);
    }

    public static List<Ort> getOrte() {
        return singleton.orte;
    }
}
