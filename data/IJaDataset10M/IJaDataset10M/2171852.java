package dsb.mbc;

import dsb.mbc.Administration;
import dsb.mbc.AdministrationFactory;

public class JournalTest {

    public static void main(String[] args) {
        System.out.println("TEST Journal started.");
        Administration admin = AdministrationFactory.openDSBAdministration((short) 2007);
        System.out.println("Available journal IDs:");
        for (String journalID : admin.getJournalIDs()) System.out.println(journalID);
        System.out.println("TEST Journal finished.");
    }
}
