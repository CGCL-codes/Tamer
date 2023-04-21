package se.studieren.dbvote.importer.writer;

import java.util.Collection;
import java.util.Set;
import se.studieren.dbvote.db.dao.BundeslandDAO;
import se.studieren.dbvote.db.dao.DAOFactory;
import se.studieren.dbvote.db.dao.LandeslisteDAO;
import se.studieren.dbvote.db.dao.ParteiDAO;
import se.studieren.dbvote.db.dao.WahlbezirkDAO;
import se.studieren.dbvote.db.dao.WahlkreisDAO;
import se.studieren.dbvote.db.dto.Bundesland;
import se.studieren.dbvote.db.dto.BundeslandID;
import se.studieren.dbvote.db.dto.Landesliste;
import se.studieren.dbvote.db.dto.LandeslisteID;
import se.studieren.dbvote.db.dto.Partei;
import se.studieren.dbvote.db.dto.ParteiID;
import se.studieren.dbvote.db.dto.Wahlbezirk;
import se.studieren.dbvote.db.dto.WahlbezirkID;
import se.studieren.dbvote.db.dto.Wahlkreis;
import se.studieren.dbvote.db.dto.WahlkreisID;

public class ParteienWriter {

    private DAOFactory daoFactory;

    private final int Anzahl_Bezirke = 10;

    public ParteienWriter(DAOFactory daoFacotory) {
        this.daoFactory = daoFacotory;
    }

    public void writeLands(Set<se.studieren.dbvote.importer.parser.ParteienParser.Bundesland> bundeslaender) {
        BundeslandDAO bundeslandDAO = daoFactory.getBundeslandDAO();
        for (se.studieren.dbvote.importer.parser.ParteienParser.Bundesland bundesland : bundeslaender) {
            BundeslandID bundeslandID = new BundeslandID(bundesland.getId());
            Bundesland dtoBundesland = new Bundesland(bundeslandID, bundesland.getName());
            bundeslandDAO.insertBundesland(dtoBundesland);
            writeWahlkreise(bundesland.getWahlkreise(), bundeslandID);
        }
    }

    private void writeWahlkreise(Set<se.studieren.dbvote.importer.parser.ParteienParser.Wahlkreis> wahlkreise, BundeslandID bundeslandID) {
        WahlkreisDAO wahlkreisDAO = daoFactory.getWahlkreisDAO();
        for (se.studieren.dbvote.importer.parser.ParteienParser.Wahlkreis wahlkreis : wahlkreise) {
            WahlkreisID wahlkreisID = new WahlkreisID(wahlkreis.getId());
            Wahlkreis dtoWahlkreis = new Wahlkreis(wahlkreisID, wahlkreis.getName(), bundeslandID);
            wahlkreisDAO.insertWahlkreis(dtoWahlkreis);
            writeWahlbezirke(dtoWahlkreis.getName(), wahlkreisID);
        }
    }

    private void writeWahlbezirke(String wahlkreisName, WahlkreisID wahlkreisID) {
        WahlbezirkDAO wahlbezirkDAO = daoFactory.getWahlbezirkDAO();
        for (int i = 0; i < Anzahl_Bezirke; i++) {
            String name = wahlkreisName + "-" + i;
            WahlbezirkID id = new WahlbezirkID(wahlkreisID.getInt() * 100 + i);
            Wahlbezirk wahlbezirk = new Wahlbezirk(id, name, wahlkreisID);
            wahlbezirkDAO.insertWahlbezirk(wahlbezirk);
        }
    }

    public void writeParteien(Collection<se.studieren.dbvote.importer.parser.ParteienParser.Parteien.Partei> parteien) {
        ParteiDAO parteiDAO = daoFactory.getParteiDAO();
        for (se.studieren.dbvote.importer.parser.ParteienParser.Parteien.Partei partei : parteien) {
            Partei dtoPartei = new Partei(new ParteiID(partei.getId()), partei.getName(), getParteienKuerzel(partei.getName()));
            parteiDAO.insertPartei(dtoPartei);
        }
    }

    public void writeLandeslisten(Collection<se.studieren.dbvote.importer.parser.ParteienParser.Parteien.Partei> parteien, Set<se.studieren.dbvote.importer.parser.ParteienParser.Bundesland> bundeslaender) {
        LandeslisteDAO landeslistenDAO = daoFactory.getLandeslisteDAO();
        int i = 1;
        for (se.studieren.dbvote.importer.parser.ParteienParser.Bundesland bundesland : bundeslaender) {
            BundeslandID bundeslandId = new BundeslandID(bundesland.getId());
            for (se.studieren.dbvote.importer.parser.ParteienParser.Parteien.Partei partei : parteien) {
                ParteiID parteiId = new ParteiID(partei.getId());
                LandeslisteID landeslisteId = new LandeslisteID(i++);
                Landesliste dtoLandesliste = new Landesliste(landeslisteId, bundeslandId, parteiId);
                landeslistenDAO.insertLandesliste(dtoLandesliste);
            }
        }
    }

    private static String getParteienKuerzel(String parteiname) {
        if (parteiname.length() <= 5) {
            return parteiname;
        } else {
            return parteiname.substring(0, 5);
        }
    }
}
