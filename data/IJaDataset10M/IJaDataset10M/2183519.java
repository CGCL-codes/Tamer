package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import dao.VokabelDao;
import dao.VokabelDaoImpl;
import xml.Dom4JFileReader;
import xml.Dom4JXmlWriter;
import xml.DomHolder;

public class Vokabeltrainer {

    private List<Vokabel> vokabeln = new ArrayList<Vokabel>();

    private String xmlPath = "src-test-resources/vokabeltrainerTest.xml";

    private VokabelDao dao;

    public Vokabeltrainer() throws DocumentException, IOException {
        readXml();
        dao = new VokabelDaoImpl(xmlPath);
        createHashMap();
    }

    private void createHashMap() {
        if (dao.getAllVokabel() != null) vokabeln = dao.getAllVokabel(); else vokabeln = new ArrayList<Vokabel>();
    }

    private void readXml() throws DocumentException, IOException {
        File xml = new File(xmlPath);
        if (xml.exists()) {
            Dom4JFileReader reader = new Dom4JFileReader();
            DomHolder.getInstance().setDom(reader.readDocument(xmlPath));
        } else {
            Dom4JXmlWriter writer = new Dom4JXmlWriter();
            writer.writeXml(xmlPath);
        }
    }

    public List<Vokabel> getVokabeln() {
        return Collections.unmodifiableList(vokabeln);
    }

    public void setVokabel(ArrayList<Vokabel> vokabel) {
        this.vokabeln = vokabel;
    }

    public boolean addVokabelOrUpdateVokable(Vokabel vok) throws IOException {
        if (vok == null) throw new NullPointerException("Vokabeltrainer addVokabel: Vok must not be null");
        return vokabeln.add(dao.persist(vok));
    }

    public boolean remove(Vokabel vok) {
        if (vok == null) throw new NullPointerException("Vokabeltrainer remove: Vok must not be null");
        if (getIndexOfVokabel(vok) == null) throw new NoSuchElementException("Vokabeltrainer remove: Could not find Vokabel");
        return vokabeln.remove(vok);
    }

    public Integer getIndexOfVokabel(Vokabel vok) {
        for (Vokabel v : vokabeln) {
            if (v.getEnglish().equals(vok.getEnglish()) && v.getGerman().equals(vok.getGerman())) {
                return vokabeln.indexOf(v);
            }
        }
        return null;
    }

    public Vokabel getQuestion(char c) {
        if (vokabeln.size() <= 0) throw new IllegalArgumentException("Vokabeltrainer getQuestion: Vokablelist is empty");
        boolean ok = false;
        while (!ok) {
            int rndLvl = new Random().nextInt(5) + 1;
            for (int i = 0; i < vokabeln.size(); i++) {
                int rndSelect = new Random().nextInt(vokabeln.size());
                if (c == 'e') {
                    if (vokabeln.get(rndSelect).getLvlEnglish() <= rndLvl) return vokabeln.get(rndSelect);
                } else if (c == 'g') {
                    if (vokabeln.get(rndSelect).getLvlGerman() <= rndLvl) return vokabeln.get(rndSelect);
                }
            }
        }
        return null;
    }
}
