package hr.veleri.data;

import hr.veleri.data.dao.interfaces.*;
import hr.veleri.data.dataobjects.*;
import hr.veleri.util.Utilities;
import hr.veleri.util.UtilitiesDate;

/**
 * User: iivanovic
 * Date: 15.04.11.
 * Time: 12:06
 */
public class InitData {

    private boolean initialised;

    private KlijentDao klijentDao;

    private KorisnikDao korisnikDao;

    private KorisnikKlijentDao korisnikKlijentDao;

    private AplikacijaDao aplikacijaDao;

    private KorisnikZaposlenikDao korisnikZaposlenikDao;

    private PrijaveDao prijaveDao;

    private IntervencijeDao intervencijeDao;

    public void setKlijentDao(KlijentDao klijentDao) {
        this.klijentDao = klijentDao;
    }

    public void setKorisnikDao(KorisnikDao korisnikDao) {
        this.korisnikDao = korisnikDao;
    }

    public void setKorisnikKlijentDao(KorisnikKlijentDao korisnikKlijentDao) {
        this.korisnikKlijentDao = korisnikKlijentDao;
    }

    public void setAplikacijaDao(AplikacijaDao aplikacijaDao) {
        this.aplikacijaDao = aplikacijaDao;
    }

    public void setKorisnikZaposlenikDao(KorisnikZaposlenikDao korisnikZaposlenikDao) {
        this.korisnikZaposlenikDao = korisnikZaposlenikDao;
    }

    public void setPrijaveDao(PrijaveDao prijaveDao) {
        this.prijaveDao = prijaveDao;
    }

    public void setIntervencijeDao(IntervencijeDao intervencijeDao) {
        this.intervencijeDao = intervencijeDao;
    }

    public void init() {
        if (!isInitialised()) {
            initExampleData();
            setInitialised(true);
        } else {
            System.out.println("--- db already initialised !!! ---");
        }
    }

    private void initExampleData() {
        Klijent pl = klijentDao.save(new Klijent("001", "Plava Laguna d.d."));
        Klijent riviera = klijentDao.save(new Klijent("002", "Riviera Poreč d.d."));
        Klijent imperial = klijentDao.save(new Klijent("003", "Imperial Rab d.d."));
        Klijent rabac = klijentDao.save(new Klijent("004", "Rabac d.d."));
        Klijent ss = klijentDao.save(new Klijent("005", "Sunčana staza d.o.o."));
        Klijent makarska = klijentDao.save(new Klijent("006", "Hoteli Makarska d.d."));
        Klijent supetrus = klijentDao.save(new Klijent("007", "Supetrus Hoteli d.d."));
        Korisnik iivanovic = korisnikDao.save(new Korisnik("Igor", "Ivanović", "igor.ivanovic@veleri.hr", "3b6d69c25e32c90b85c11c03dfde97e6", TipKorisnika.PODRSKA));
        Korisnik ppauro = korisnikDao.save(new Korisnik("Patrik", "Pauro", "patrik.pauro@veleri.hr", "6c722ef06c1db589caef37c1b5fb8850", TipKorisnika.PODRSKA));
        Korisnik tcosic = korisnikDao.save(new Korisnik("Tomislav", "Ćosić", "tomislav.cosic@veleri.hr", "cb3918a280403e87479d2ec7141b32dc", TipKorisnika.KLIJENT));
        Korisnik vjuhas = korisnikDao.save(new Korisnik("Vanja", "Juhas", "vanja.juhas@veleri.hr", "9829bcb9e22402246d5f865c74117a16", TipKorisnika.KLIJENT));
        Korisnik dostojic = korisnikDao.save(new Korisnik("Damir", "Ostojić", "damir.ostojic@veleri.hr", "9bddd73fd4e48c8cc0adcd9c75df88be", TipKorisnika.KLIJENT));
        Korisnik mentor = korisnikDao.save(new Korisnik("Ivan", "Pogarčić", "mentor", "23cbeacdea458e9ced9807d6cbe2f4d6", TipKorisnika.ADMINISTRATOR));
        Korisnik klijent = korisnikDao.save(new Korisnik("Ivo", "Ivić", "klijent", "bdf4b9e8020eb9e0c2f9f775735548de", TipKorisnika.ADMINISTRATOR));
        Korisnik podrska = korisnikDao.save(new Korisnik("Tomislav", "Novak", "podrska", "6e3ed7150d87149f53b9490a3287cb3d", TipKorisnika.ADMINISTRATOR));
        Aplikacija ptw = aplikacijaDao.save(new Aplikacija("Prijava Turista - Web", "PTW"));
        Aplikacija ptz2 = aplikacijaDao.save(new Aplikacija("Prijava Turista u TZ", "PTZ2"));
        Aplikacija pos = aplikacijaDao.save(new Aplikacija("Blagajnička kasa", "POS"));
        Aplikacija rec = aplikacijaDao.save(new Aplikacija("Recepcijsko poslovanje", "REC2"));
        Aplikacija pro = aplikacijaDao.save(new Aplikacija("Prodaja smještajnih kapaciteta", "PRO"));
        Aplikacija gas = aplikacijaDao.save(new Aplikacija("Gastronomija", "GAS"));
        Aplikacija mje = aplikacijaDao.save(new Aplikacija("Mjenjačnica", "MJE3"));
        Aplikacija irs = aplikacijaDao.save(new Aplikacija("Internet Rezervacijski Sustav", "IRS"));
        KorisnikZaposlenik iivanovicPtw = korisnikZaposlenikDao.save(new KorisnikZaposlenik(ptw, iivanovic));
        KorisnikZaposlenik iivanovicPtz2 = korisnikZaposlenikDao.save(new KorisnikZaposlenik(ptz2, iivanovic));
        KorisnikZaposlenik iivanovicPos = korisnikZaposlenikDao.save(new KorisnikZaposlenik(pos, iivanovic));
        KorisnikZaposlenik iivanovicPro = korisnikZaposlenikDao.save(new KorisnikZaposlenik(pro, iivanovic));
        KorisnikZaposlenik ppauroMje = korisnikZaposlenikDao.save(new KorisnikZaposlenik(mje, ppauro));
        KorisnikZaposlenik podrskaMje = korisnikZaposlenikDao.save(new KorisnikZaposlenik(mje, podrska));
        KorisnikZaposlenik podrskaPos = korisnikZaposlenikDao.save(new KorisnikZaposlenik(pos, podrska));
        KorisnikZaposlenik podrskaPtw = korisnikZaposlenikDao.save(new KorisnikZaposlenik(ptw, podrska));
        KorisnikZaposlenik podrskaRec = korisnikZaposlenikDao.save(new KorisnikZaposlenik(rec, podrska));
        KorisnikZaposlenik podrskaPro = korisnikZaposlenikDao.save(new KorisnikZaposlenik(pro, podrska));
        KorisnikZaposlenik podrskaGas = korisnikZaposlenikDao.save(new KorisnikZaposlenik(gas, podrska));
        KorisnikZaposlenik podrskaPtz2 = korisnikZaposlenikDao.save(new KorisnikZaposlenik(ptz2, ppauro));
        KorisnikZaposlenik podrskaIrs = korisnikZaposlenikDao.save(new KorisnikZaposlenik(irs, ppauro));
        KorisnikZaposlenik ppauroRec = korisnikZaposlenikDao.save(new KorisnikZaposlenik(rec, ppauro));
        KorisnikZaposlenik ppauroGas = korisnikZaposlenikDao.save(new KorisnikZaposlenik(gas, ppauro));
        KorisnikZaposlenik ppauroIrs = korisnikZaposlenikDao.save(new KorisnikZaposlenik(irs, ppauro));
        KorisnikZaposlenik ppauroPro = korisnikZaposlenikDao.save(new KorisnikZaposlenik(pro, ppauro));
        KorisnikZaposlenik ppauroPtw = korisnikZaposlenikDao.save(new KorisnikZaposlenik(ptw, ppauro));
        KorisnikKlijent korisnikKlijent = korisnikKlijentDao.save(new KorisnikKlijent(pl, tcosic));
        KorisnikKlijent korisnikKlijent1 = korisnikKlijentDao.save(new KorisnikKlijent(ss, vjuhas));
        KorisnikKlijent korisnikKlijent2 = korisnikKlijentDao.save(new KorisnikKlijent(imperial, dostojic));
        KorisnikKlijent korisnikKlijent3 = korisnikKlijentDao.save(new KorisnikKlijent(imperial, klijent));
        Prijava p1 = prijaveDao.save(new Prijava(1, ptw, "", "Kako napraviti aktivaciju korisničkog računa", UtilitiesDate.getDate(2011, 5, 25, 8, 0), UtilitiesDate.getDate(2011, 5, 25, 8, 5), tcosic));
        Prijava p2 = prijaveDao.save(new Prijava(2, ptz2, "", "Prilikom prihvata prijava javlja se greška nedefinirane zemlje državljanstva (XXK)", UtilitiesDate.getDate(2011, 5, 26, 8, 1), UtilitiesDate.getDate(2011, 5, 26, 8, 5), tcosic));
        Prijava p3 = prijaveDao.save(new Prijava(3, pos, "Veza: recepcijsko poslovanje", "Greška kod naplate računa na račun gosta", UtilitiesDate.getDate(2011, 5, 26, 7, 22), UtilitiesDate.getDate(2011, 5, 26, 8, 5), vjuhas));
        Prijava p4 = prijaveDao.save(new Prijava(4, pro, "nedavno zamijenjen server", "Događa se dupliranje transakcija zbog sporog upita prema udaljenim serverima", UtilitiesDate.getDate(2011, 7, 19, 7, 55), UtilitiesDate.getDate(2011, 7, 19, 8, 3), dostojic));
        Prijava p5 = prijaveDao.save(new Prijava(5, mje, "HNB: izmjena uvjeta za certificiranje", "Ugraditi izmjene prema zadnjoj specifikaciji HNB-a", UtilitiesDate.getDate(2011, 3, 25, 7, 0), UtilitiesDate.getDate(2011, 3, 25, 7, 11), vjuhas));
        Prijava p6 = prijaveDao.save(new Prijava(6, irs, "T-ComPayWay, nova verzija", "Uskladiti sustav naplate s najnovijim PayWay sučeljem", UtilitiesDate.getDate(2011, 4, 12, 3, 1), UtilitiesDate.getDate(2011, 4, 14, 8, 10), dostojic));
        Intervencija i1 = intervencijeDao.save(new Intervencija(UtilitiesDate.getDate(2011, 5, 25, 14, 3), "Korisniku mailom dostavljene upute za operatere u TZ", p1, iivanovicPtw, 5));
        Intervencija i2 = intervencijeDao.save(new Intervencija(UtilitiesDate.getDate(2011, 5, 26, 8, 9), "Ažuriranje šifarnika zemalja. XXK je šifra koja se privremeno koristi za zemlju Kosovo", p2, iivanovicPtz2, 10));
        Intervencija i3 = intervencijeDao.save(new Intervencija(UtilitiesDate.getDate(2011, 5, 27, 11, 30), "Testiranje prihvata datoteka koje sadrže šifru zemlje XXK. Prijave se ispravno upisuju", p2, iivanovicPtw, 5));
        Intervencija i4 = intervencijeDao.save(new Intervencija(UtilitiesDate.getDate(2011, 5, 27, 12, 30), "Provjera postavki konekcije na bazu na klijentskom računalu. Ažurirana datoteka TNSNAMES.ORA, s postavkama novog oracle poslužitelja", p3, iivanovicPos, 25));
        Intervencija i5 = intervencijeDao.save(new Intervencija(UtilitiesDate.getDate(2011, 5, 27, 13, 22), "Testiranje naplate na račun gosta. Veza s recepcijskim poslovanjem je ispravna", p3, iivanovicPos, 15));
        Intervencija i6 = intervencijeDao.save(new Intervencija(UtilitiesDate.getDate(2011, 7, 22, 14, 2), "Optimizarana procedura REZ. Izmjena aplicirana na korisnikovoj bazi podataka", p4, iivanovicPro, 480));
        Intervencija i7 = intervencijeDao.save(new Intervencija(UtilitiesDate.getDate(2011, 5, 25, 9, 54), "Provjera logova na modulu PRO. Prema logovima vrijeme odziva je optimalno", p4, ppauroPro, 95));
        Intervencija i8 = intervencijeDao.save(new Intervencija(UtilitiesDate.getDate(2011, 3, 26, 11, 15), "Napravljene izmjene prema specifikaciji HNB-a", p5, ppauroMje, 900));
        Intervencija i9 = intervencijeDao.save(new Intervencija(UtilitiesDate.getDate(2011, 4, 15, 12, 22), "Dostava aplikacije na certificiranje", p5, ppauroMje, 25));
        Intervencija i10 = intervencijeDao.save(new Intervencija(UtilitiesDate.getDate(2011, 4, 15, 13, 55), "Sustav naplate usklađen sa novom verzijom T-ComPayWay payment gateway-a", p6, ppauroIrs, 750));
        Intervencija i11 = intervencijeDao.save(new Intervencija(UtilitiesDate.getDate(2011, 4, 16, 12, 5), "Omogućeno parametarsko korištenje vlastite forme za naplatu (SOAP klijent)", p6, ppauroIrs, 600));
        Intervencija i12 = intervencijeDao.save(new Intervencija(UtilitiesDate.getDate(2011, 4, 18, 14, 10), "Implementacija SSL certifikata (Thawte) na korisnikovom Tomcat poslužitelju - preduvjet za korištenje SOAP servisa za naplatu", p6, ppauroIrs, 120));
        Intervencija i13 = intervencijeDao.save(new Intervencija(UtilitiesDate.getDate(2011, 4, 18, 17, 35), "Ažuriranje uputa za parametrizaciju i korištenje novih mogućnosti sustava naplate. Upute dostavljene korisniku.", p6, ppauroIrs, 240));
    }

    public boolean isInitialised() {
        return initialised;
    }

    public void setInitialised(boolean initialised) {
        this.initialised = initialised;
    }

    public static void main(String[] args) {
        System.out.println("mentor: " + Utilities.getMD5("mentor"));
        System.out.println("klijent: " + Utilities.getMD5("klijent"));
        System.out.println("podrska: " + Utilities.getMD5("podrska"));
    }
}
