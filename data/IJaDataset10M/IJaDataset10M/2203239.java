package vydavky.applicationlogic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import junit.framework.TestCase;
import org.junit.Test;
import vydavky.Application;
import vydavky.client.ciselniky.CMena;
import vydavky.client.ciselniky.CTypVydavku;
import vydavky.client.ciselniky.Ciselniky;
import vydavky.client.objects.BaseValue;
import vydavky.client.objects.ClovekValue;
import vydavky.client.objects.KoeficientVydavku;
import vydavky.client.objects.ProjektValue;
import vydavky.client.objects.SkupinaValue;
import vydavky.client.objects.TransakciaValue;
import vydavky.client.objects.clientserver.NavratValue;
import vydavky.client.objects.clientserver.ServerObject;
import vydavky.client.utils.ClientUtils;
import vydavky.testutils.TestUtils;
import vydavky.utils.MergeSolver;
import vydavky.utils.Utils;

/**
 * Testovanie prechodu offline a mergovania.
 */
public class OnlineOfflineHelperTest extends TestCase {

    public OnlineOfflineHelperTest(final String testName) {
        super(testName);
        Application.getApplication().getSettings().setZobrazovatChybu(false);
    }

    /**
   * Test of prechodOffline method, of class OnlineOfflineHelper.
   */
    @Test
    public void testPrechodOffline() {
        System.out.println("prechodOffline");
        Utils.nastavSystemovePremenne();
        final NavratValue prihlasenie = Application.getApplication().pripojAPrihlas(TestUtils.login, true);
        if (prihlasenie.isChyba()) {
            fail("prihlasenie zlyhalo: " + prihlasenie.getText());
            return;
        }
        final Map<Long, ClovekValue> ludia = new HashMap<Long, ClovekValue>(Application.getBusinessClient().getLudia());
        final Map<Long, SkupinaValue> skupiny = new HashMap<Long, SkupinaValue>(Application.getBusinessClient().getSkupiny());
        final Map<Long, ProjektValue> projekty = new HashMap<Long, ProjektValue>(Application.getBusinessClient().getProjekty());
        final Map<Long, TransakciaValue> transakcie = new HashMap<Long, TransakciaValue>(Application.getBusinessClient().getTransakcie(null));
        final Map<Long, CMena> meny = new HashMap<Long, CMena>(Application.getBusinessClient().getMeny());
        final Map<Long, CTypVydavku> typyVydavku = new HashMap<Long, CTypVydavku>(Application.getBusinessClient().getTypyVydavku());
        final NavratValue prechod = OnlineOfflineHelper.prechodOffline(null, false);
        if (prechod.isChyba()) {
            fail("Nastala chyba pri prechode: " + prechod);
        }
        for (ClovekValue clovek : ludia.values()) {
            final ClovekValue offline = Application.getBusinessClient().getLudia().get(clovek.getId());
            if (offline == null) {
                fail("objekt nebol najdeny v lokalnych datach: " + clovek);
            }
            if (!clovek.deepEquals(offline)) {
                fail("Povodny a nacitany objekt sa nezhoduju: " + clovek + "\n" + offline);
            }
        }
        for (SkupinaValue skupina : skupiny.values()) {
            final SkupinaValue offline = Application.getBusinessClient().getSkupiny().get(skupina.getId());
            if (offline == null) {
                fail("objekt nebol najdeny v lokalnych datach: " + skupina);
            }
            if (!skupina.deepEquals(offline)) {
                fail("Povodny a nacitany objekt sa nezhoduju: " + skupina + "\n" + offline);
            }
        }
        for (ProjektValue projekt : projekty.values()) {
            final ProjektValue offline = Application.getBusinessClient().getProjekty().get(projekt.getId());
            if (offline == null) {
                fail("objekt nebol najdeny v lokalnych datach: " + projekt);
            }
            if (!projekt.deepEquals(offline)) {
                fail("Povodny a nacitany objekt sa nezhoduju: " + projekt + "\n" + offline);
            }
        }
        for (TransakciaValue transakcia : transakcie.values()) {
            final TransakciaValue offline = Application.getBusinessClient().getTransakcie(null).get(transakcia.getId());
            if (offline == null) {
                fail("objekt nebol najdeny v lokalnych datach: " + transakcia);
            }
            if (!transakcia.deepEquals(offline)) {
                fail("Povodny a nacitany objekt sa nezhoduju: " + transakcia + "\n" + offline);
            }
        }
        for (CMena mena : meny.values()) {
            final CMena offline = Application.getBusinessClient().getMeny().get(mena.getId());
            if (offline == null) {
                fail("objekt nebol najdeny v lokalnych datach: " + mena);
            }
            if (!mena.deepEquals(offline)) {
                fail("Povodny a nacitany objekt sa nezhoduju: " + mena + "\n" + offline);
            }
        }
        for (CTypVydavku typVydavku : typyVydavku.values()) {
            final CTypVydavku offline = Application.getBusinessClient().getTypyVydavku().get(typVydavku.getId());
            if (offline == null) {
                fail("objekt nebol najdeny v lokalnych datach: " + typVydavku);
            }
            if (!typVydavku.deepEquals(offline)) {
                fail("Povodny a nacitany objekt sa nezhoduju: " + typVydavku + "\n" + offline);
            }
        }
    }

    /**
   * Test of prihlasAMerguj method, of class OnlineOfflineHelper.
   */
    @Test
    public void testPrihlasAMerguj() {
        System.out.println("prihlasAMerguj");
        testujProblemSIDckami(Ciselniky.RIESENIE_NAHRADIT_KLIENT);
        testujProblemSIDckami(Ciselniky.RIESENIE_NECHAT_SERVER);
        testujVZTAH_1_STORNO_NA_SERVERI_ZMENIT(Ciselniky.RIESENIE_OBNOVIT_STORNOVANE);
        testujVZTAH_1_STORNO_NA_SERVERI_ZMENIT(Ciselniky.RIESENIE_ZALOZIT_NOVE);
        testujVZTAH_1_STORNO_NA_SERVERI_ZMENIT(Ciselniky.RIESENIE_NAHRADIT_STORNOVANE);
        testujVZTAH_1_STORNO_NA_SERVERI_ZMENIT(Ciselniky.RIESENIE_NECHAT_STORNOVANE);
        testujVZTAH_2_ZMENA_NA_SERVERI_ZMENIT(Ciselniky.RIESENIE_NECHAT_SERVER);
        testujVZTAH_2_ZMENA_NA_SERVERI_ZMENIT(Ciselniky.RIESENIE_NAHRADIT_KLIENT);
        testujVZTAH_7_ZMENA_NA_SERVERI_STORNO(Ciselniky.RIESENIE_ANO);
        testujVZTAH_7_ZMENA_NA_SERVERI_STORNO(Ciselniky.RIESENIE_NIE);
        testujVZTAH_8_STORNO_POTVRDIT(Ciselniky.RIESENIE_ANO);
        testujVZTAH_8_STORNO_POTVRDIT(Ciselniky.RIESENIE_NIE);
    }

    @Test
    public void testujUlozenieLokalnych() {
        testujUlozenieLokalnych(false);
    }

    @Test
    public void testujUlozenieLokalnychSPosunomSekvencie() {
        testujUlozenieLokalnych(true);
    }

    /**
   * Overi, ci sa lokalne vytvorene data pri prechode do online rezimu korektne
   * ulozia do DB.
   *
   * @param posunutSekvenciu Priznak, ci sa ma medzicasom posunut sekvencia v DB
   *        o par cisel.
   */
    private void testujUlozenieLokalnych(final boolean posunutSekvenciu) {
        System.out.println("testujUlozenieLokalnych");
        Utils.nastavSystemovePremenne();
        final NavratValue prihlasenie = Application.getApplication().pripojAPrihlas(TestUtils.login, true);
        if (prihlasenie.isChyba()) {
            fail("prihlasenie zlyhalo: " + prihlasenie.getText());
            return;
        }
        final NavratValue prechod = OnlineOfflineHelper.prechodOffline(null, false);
        if (prechod.isChyba()) {
            fail("Nastala chyba pri prechode: " + prechod);
        }
        if (Application.getApplication().getSettings().isOnline()) {
            fail("bolo by fajn byt offline");
        }
        final List<BaseValue> testovacieObjekty = TestUtils.nahodneObjekty();
        if (testovacieObjekty == null) {
            fail("Zlyhalo plnenie testovacich objektov do aplikacnej logiky.");
        }
        if (posunutSekvenciu) {
            final NavratValue prihlasenie2 = Application.getApplication().pripojAPrihlas(TestUtils.login, true);
            if (prihlasenie2.isChyba()) {
                fail("prihlasenie zlyhalo: " + prihlasenie2.getText());
                return;
            }
            final int posunSekvencie = (new Random()).nextInt(5);
            for (int i = 0; i < posunSekvencie; i++) {
                final SkupinaValue skupina = TestUtils.nahodnaSkupina(true, false);
                final NavratValue ulozenieSkupiny = Application.getBusinessClient().ulozSkupinuNew(skupina);
                if (ulozenieSkupiny.isChyba()) {
                    fail("zlyhalo ulozenie skupiny: " + ulozenieSkupiny.getText());
                }
            }
            Application.getApplication().odpojAOdhlas();
            if (Application.getApplication().getSettings().isOnline()) {
                fail("bolo by fajn byt offline");
            }
        }
        final NavratValue prechodOnline = OnlineOfflineHelper.prihlasAMerguj(TestUtils.login, null, false, new MergeSolver() {

            @Override
            public List<ServerObject> vyriesKonflikty(final List<ServerObject> objektyNaRiesenie) {
                if (!ClientUtils.isEmpty(objektyNaRiesenie)) {
                    fail("Nemaju byt ziadne objekty na riesenie!");
                }
                return objektyNaRiesenie;
            }
        });
        if (prechodOnline.isChyba()) {
            fail("prechod do online rezimu zlyhal: " + prechodOnline.getText());
        }
        if (!Application.getApplication().getSettings().isOnline()) {
            fail("bolo by fajn byt online");
        }
        final Map<Long, ClovekValue> ludiaCache = Application.getBusinessClient().getLudia();
        final Map<Long, SkupinaValue> skupinyCache = Application.getBusinessClient().getSkupiny();
        final Map<Long, ProjektValue> projektyCache = Application.getBusinessClient().getProjekty();
        final Map<Long, TransakciaValue> transakcieCache = Application.getBusinessClient().getTransakcie(null);
        for (BaseValue objekt : testovacieObjekty) {
            switch(objekt.getTypObjektu()) {
                case CLOVEK:
                    boolean najdenyClovek = false;
                    for (ClovekValue clovek : ludiaCache.values()) {
                        if (clovek.deepEquals(objekt)) {
                            najdenyClovek = true;
                        }
                    }
                    if (!najdenyClovek) {
                        fail("ulozeny a nacitany objekt sa nezhoduju (alebo sa objekt nepodarilo nacitat). hladal som objekt: " + objekt);
                    }
                    break;
                case SKUPINA:
                    boolean najdenaSkupina = false;
                    final SkupinaValue povodnaSkupina = (SkupinaValue) objekt;
                    for (SkupinaValue skupina : skupinyCache.values()) {
                        if (skupina.getMeno().equals(povodnaSkupina.getMeno()) && skupina.getTyp().equals(povodnaSkupina.getTyp()) && skupina.getKoeficienty().size() == povodnaSkupina.getKoeficienty().size()) {
                            najdenaSkupina = true;
                        }
                    }
                    if (!najdenaSkupina) {
                        fail("ulozeny a nacitany objekt sa nezhoduju (alebo sa objekt nepodarilo nacitat). hladal som objekt: " + objekt);
                    }
                    break;
                case PROJEKT:
                    boolean najdenyProjekt = false;
                    final ProjektValue povodnyProjekt = (ProjektValue) objekt;
                    for (ProjektValue projekt : projektyCache.values()) {
                        if (projekt.getMeno().equals(povodnyProjekt.getMeno()) && projekt.isUzavrety() == povodnyProjekt.isUzavrety() && projekt.getLudia().size() == povodnyProjekt.getLudia().size() && projekt.getSkupiny().size() == povodnyProjekt.getSkupiny().size()) {
                            najdenyProjekt = true;
                        }
                    }
                    if (!najdenyProjekt) {
                        fail("ulozeny a nacitany objekt sa nezhoduju (alebo sa objekt nepodarilo nacitat). hladal som objekt: " + objekt);
                    }
                    break;
                case TRANSAKCIA:
                    boolean najdenaTransakcia = false;
                    final TransakciaValue povodnaTransakcia = (TransakciaValue) objekt;
                    for (TransakciaValue transakcia : transakcieCache.values()) {
                        if (transakcia.getPopis().equals(povodnaTransakcia.getPopis()) && transakcia.getKurz() == povodnaTransakcia.getKurz() && transakcia.getSuma() == povodnaTransakcia.getSuma() && transakcia.getMena().equals(povodnaTransakcia.getMena()) && transakcia.getTypVydavku().equals(povodnaTransakcia.getTypVydavku()) && transakcia.getPlatili().size() == povodnaTransakcia.getPlatili().size() && transakcia.getSpotrebovali().size() == povodnaTransakcia.getSpotrebovali().size()) {
                            najdenaTransakcia = true;
                        }
                    }
                    if (!najdenaTransakcia) {
                        fail("ulozeny a nacitany objekt sa nezhoduju (alebo sa objekt nepodarilo nacitat). hladal som objekt: " + objekt);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Neznamy typ objektu " + objekt.getTypObjektu());
            }
        }
        final String zmazanie = TestUtils.zmazTestovacieZaznamy();
        if (zmazanie != null) {
            fail(zmazanie);
        }
    }

    /**
   * Otestuje seriu zavaznych problemom najdenych vo verzii 0.6.6: totiz, ze ked
   * sa ukladaju lokalne objekty, menia sa im IDcka a tym sa porusia zavislosti
   * z inych objektov.
   *
   * Postup tohto testu:
   * 1) v online rezime sa vytvori skupina S
   * 2) prejde sa do rezimu offline
   * 3) vytvori sa clovek C, prida sa do S
   * 4) "paralelne" sa v online rezime zmeni S
   * 5) C a S sa merguju, to sposobi ulozenie C ako lokalneho a ukaze konflikt na S
   * 6) konflikt sa vyriesi podla zadaneho parametra
   *
   * @param sposobRiesenia
   */
    private void testujProblemSIDckami(final int sposobRiesenia) {
        System.out.println("testujProblemSIDckami");
        Utils.nastavSystemovePremenne();
        final NavratValue prihlasenie = Application.getApplication().pripojAPrihlas(TestUtils.login, true);
        if (prihlasenie.isChyba()) {
            fail("prihlasenie zlyhalo: " + prihlasenie.getText());
            return;
        }
        final SkupinaValue skupina = TestUtils.nahodnaSkupina(true, false);
        final NavratValue ulozenieSkupiny = Application.getBusinessClient().ulozSkupinuNew(skupina);
        if (ulozenieSkupiny.isChyba()) {
            fail("zlyhalo ulozenie skupiny: " + ulozenieSkupiny.getText());
        }
        skupina.setId((Long) ulozenieSkupiny.getValue());
        final NavratValue prechod = OnlineOfflineHelper.prechodOffline(null, false);
        if (prechod.isChyba()) {
            fail("Nastala chyba pri prechode: " + prechod);
        }
        if (Application.getApplication().getSettings().isOnline()) {
            fail("bolo by fajn byt offline");
        }
        final ClovekValue chudatko = TestUtils.nahodnyClovek(true);
        final NavratValue ulozenieCloveka = Application.getBusinessClient().ulozClovekaNew(chudatko);
        if (ulozenieCloveka.isChyba()) {
            fail("zlyhalo ulozenie cloveka: " + ulozenieCloveka.getText());
        }
        chudatko.setId((Long) ulozenieCloveka.getValue());
        final SkupinaValue skupinaNacitanaZOffline = Application.getBusinessClient().getSkupiny().get(skupina.getId());
        if (!skupina.deepEquals(skupinaNacitanaZOffline)) {
            fail("skupiny sa lisia " + skupina + "\n" + skupinaNacitanaZOffline);
        }
        final KoeficientVydavku koef = new KoeficientVydavku(1.0f, chudatko.getId(), null);
        skupinaNacitanaZOffline.getKoeficienty().add(koef);
        final NavratValue ulozenieSkupinyDoOffline = Application.getBusinessClient().ulozSkupinu(skupinaNacitanaZOffline);
        if (ulozenieSkupinyDoOffline.isChyba()) {
            fail("zlyhalo ulozenie skupiny: " + ulozenieSkupinyDoOffline.getText());
        }
        final NavratValue prihlasenie2 = Application.getApplication().pripojAPrihlas(TestUtils.login, true);
        if (prihlasenie2.isChyba()) {
            fail("prihlasenie zlyhalo: " + prihlasenie2.getText());
            return;
        }
        if (!Application.getApplication().getSettings().isOnline()) {
            fail("bolo by fajn byt online");
        }
        final SkupinaValue skupinaNacitanaZOnline = Application.getBusinessClient().getSkupiny().get(skupina.getId());
        if (skupinaNacitanaZOnline == null) {
            fail("nepodarilo sa nacitat skupinu z online logiky");
        }
        skupinaNacitanaZOnline.setMeno(skupinaNacitanaZOnline.getMeno() + "-zmenena");
        final NavratValue ulozenieSkupinyDoOnline = Application.getBusinessClient().ulozSkupinu(skupinaNacitanaZOnline);
        if (ulozenieSkupinyDoOnline.isChyba()) {
            fail("zlyhalo ulozenie skupiny: " + ulozenieSkupinyDoOnline.getText());
        }
        Application.getApplication().odpojAOdhlas();
        if (Application.getApplication().getSettings().isOnline()) {
            fail("bolo by fajn byt offline");
        }
        final NavratValue prechodOnline = OnlineOfflineHelper.prihlasAMerguj(TestUtils.login, null, false, new MergeSolver() {

            @Override
            public List<ServerObject> vyriesKonflikty(final List<ServerObject> objektyNaRiesenie) {
                if (objektyNaRiesenie.size() != 1) {
                    fail("ocakavam prave jeden objekt");
                    return null;
                }
                objektyNaRiesenie.get(0).setSposobRiesenia(Integer.valueOf(sposobRiesenia));
                objektyNaRiesenie.get(0).setValue(Utils.ziskajOfflineObjekt(objektyNaRiesenie.get(0).getValue().getId()));
                return objektyNaRiesenie;
            }
        });
        if (prechodOnline.isChyba()) {
            fail("prechod do online rezimu zlyhal: " + prechodOnline.getText());
        }
        final SkupinaValue skupinaNacitanaZOnline2 = Application.getBusinessClient().getSkupiny().get(skupina.getId());
        if (skupinaNacitanaZOnline == null) {
            fail("nepodarilo sa nacitat skupinu z online logiky");
        }
        if (sposobRiesenia == Ciselniky.RIESENIE_NAHRADIT_KLIENT && !skupinaNacitanaZOffline.deepEquals(skupinaNacitanaZOnline2)) {
            fail("skupiny sa lisia " + skupinaNacitanaZOffline + "\n" + skupinaNacitanaZOnline2);
        }
        final String zmazanie = TestUtils.zmazTestovacieZaznamy();
        if (zmazanie != null) {
            fail(zmazanie);
        }
        Application.getApplication().odpojAOdhlas();
    }

    /**
   * Overi vztah cislo 1, teda ze objekt bol na klientovi zmeneny a na serveri
   * zoServeru.
   */
    private void testujVZTAH_1_STORNO_NA_SERVERI_ZMENIT(final int sposobRiesenia) {
        System.out.println("testujVZTAH_1_STORNO_NA_SERVERI_ZMENIT");
        Utils.nastavSystemovePremenne();
        final NavratValue prihlasenie = Application.getApplication().pripojAPrihlas(TestUtils.login, true);
        if (prihlasenie.isChyba()) {
            fail("prihlasenie zlyhalo: " + prihlasenie.getText());
            return;
        }
        final ClovekValue clovek = TestUtils.nahodnyClovek(true);
        final NavratValue ulozenieCloveka = Application.getBusinessClient().ulozClovekaNew(clovek);
        if (ulozenieCloveka.isChyba()) {
            fail("zlyhalo ulozenie cloveka: " + ulozenieCloveka.getText());
        }
        clovek.setId((Long) ulozenieCloveka.getValue());
        final NavratValue prechod = OnlineOfflineHelper.prechodOffline(null, false);
        if (prechod.isChyba()) {
            fail("Nastala chyba pri prechode: " + prechod);
        }
        if (Application.getApplication().getSettings().isOnline()) {
            fail("bolo by fajn byt offline");
        }
        final ClovekValue clovekNacitanyZOffline = Application.getBusinessClient().getLudia().get(clovek.getId());
        if (!clovekNacitanyZOffline.deepEquals(clovek)) {
            fail("nacitany clovek sa nezhoduje s povodnym: " + clovek.toString() + "\n" + clovekNacitanyZOffline.toString());
        }
        clovekNacitanyZOffline.setPriezvisko(clovekNacitanyZOffline.getPriezvisko() + "-zmeneny");
        final NavratValue ulozenieDoOffline = Application.getBusinessClient().ulozCloveka(clovekNacitanyZOffline);
        if (ulozenieDoOffline.isChyba()) {
            fail("zlyhalo ulozenie cloveka: " + ulozenieDoOffline.getText());
        }
        final NavratValue prihlasenie2 = Application.getApplication().pripojAPrihlas(TestUtils.login, true);
        if (prihlasenie2.isChyba()) {
            fail("prihlasenie zlyhalo: " + prihlasenie2.getText());
            return;
        }
        if (!Application.getApplication().getSettings().isOnline()) {
            fail("bolo by fajn byt online");
        }
        final NavratValue storno = Application.getBusinessClient().stornoCloveka(clovekNacitanyZOffline);
        if (storno.isChyba()) {
            fail("nepodarilo sa stornovat cloveka: " + storno.getText());
        }
        Application.getApplication().odpojAOdhlas();
        if (Application.getApplication().getSettings().isOnline()) {
            fail("bolo by fajn byt offline");
        }
        final NavratValue prechodOnline = OnlineOfflineHelper.prihlasAMerguj(TestUtils.login, null, false, new MergeSolver() {

            @Override
            public List<ServerObject> vyriesKonflikty(final List<ServerObject> objektyNaRiesenie) {
                if (objektyNaRiesenie.size() != 1) {
                    fail("ocakavam prave jeden objekt");
                    return null;
                }
                objektyNaRiesenie.get(0).setSposobRiesenia(Integer.valueOf(sposobRiesenia));
                if (sposobRiesenia == Ciselniky.RIESENIE_ZALOZIT_NOVE || sposobRiesenia == Ciselniky.RIESENIE_NAHRADIT_STORNOVANE) {
                    objektyNaRiesenie.get(0).setValue(Utils.ziskajOfflineObjekt(objektyNaRiesenie.get(0).getValue().getId()));
                }
                return objektyNaRiesenie;
            }
        });
        if (prechodOnline.isChyba()) {
            fail("prechod do online rezimu zlyhal: " + prechodOnline.getText());
        }
        switch(sposobRiesenia) {
            case Ciselniky.RIESENIE_OBNOVIT_STORNOVANE:
                final ClovekValue stornovany = Application.getBusinessClient().getLudia().get(clovek.getId());
                if (stornovany == null) {
                    fail("nepodarilo sa nacitat objekt");
                }
                if (!stornovany.deepEquals(clovek)) {
                    fail("objekt nevyzera tak, ako by mal");
                }
                break;
            case Ciselniky.RIESENIE_ZALOZIT_NOVE:
                boolean najdeny = false;
                for (ClovekValue hladany : Application.getBusinessClient().getLudia().values()) {
                    if (hladany.deepEquals(clovekNacitanyZOffline)) {
                        najdeny = true;
                        break;
                    }
                }
                if (!najdeny) {
                    fail("novozalozeny objekt sa nenasiel");
                }
                final ClovekValue stornovanyPonechany = Application.getBusinessClient().getLudia().get(clovek.getId());
                if (stornovanyPonechany != null) {
                    fail("objekt sa nemal dat najst");
                }
                break;
            case Ciselniky.RIESENIE_NAHRADIT_STORNOVANE:
                final ClovekValue nahradeny = Application.getBusinessClient().getLudia().get(clovek.getId());
                if (nahradeny == null) {
                    fail("nepodarilo sa nacitat objekt");
                }
                if (!nahradeny.deepEquals(clovekNacitanyZOffline)) {
                    fail("objekt nevyzera tak, ako by mal");
                }
                break;
            case Ciselniky.RIESENIE_NECHAT_STORNOVANE:
                final ClovekValue ponechany = Application.getBusinessClient().getLudia().get(clovek.getId());
                if (ponechany != null) {
                    fail("objekt sa nemal dat najst");
                }
                break;
            default:
                throw new IllegalArgumentException();
        }
        final String zmazanie = TestUtils.zmazTestovacieZaznamy();
        if (zmazanie != null) {
            fail(zmazanie);
        }
        Application.getApplication().odpojAOdhlas();
    }

    private void testujVZTAH_2_ZMENA_NA_SERVERI_ZMENIT(final int sposobRiesenia) {
        System.out.println("testujVZTAH_2_ZMENA_NA_SERVERI_ZMENIT");
        Utils.nastavSystemovePremenne();
        final NavratValue prihlasenie = Application.getApplication().pripojAPrihlas(TestUtils.login, true);
        if (prihlasenie.isChyba()) {
            fail("prihlasenie zlyhalo: " + prihlasenie.getText());
            return;
        }
        final ClovekValue clovek = TestUtils.nahodnyClovek(true);
        final NavratValue ulozenieCloveka = Application.getBusinessClient().ulozClovekaNew(clovek);
        if (ulozenieCloveka.isChyba()) {
            fail("zlyhalo ulozenie cloveka: " + ulozenieCloveka.getText());
        }
        clovek.setId((Long) ulozenieCloveka.getValue());
        final NavratValue prechod = OnlineOfflineHelper.prechodOffline(null, false);
        if (prechod.isChyba()) {
            fail("Nastala chyba pri prechode: " + prechod);
        }
        if (Application.getApplication().getSettings().isOnline()) {
            fail("bolo by fajn byt offline");
        }
        final ClovekValue clovekNacitanyZOffline = Application.getBusinessClient().getLudia().get(clovek.getId());
        if (!clovekNacitanyZOffline.deepEquals(clovek)) {
            fail("nacitany clovek sa nezhoduje s povodnym: " + clovek.toString() + "\n" + clovekNacitanyZOffline.toString());
        }
        clovekNacitanyZOffline.setPriezvisko(clovekNacitanyZOffline.getPriezvisko() + "-zmeneny");
        final NavratValue ulozenieDoOffline = Application.getBusinessClient().ulozCloveka(clovekNacitanyZOffline);
        if (ulozenieDoOffline.isChyba()) {
            fail("zlyhalo ulozenie cloveka: " + ulozenieDoOffline.getText());
        }
        final NavratValue prihlasenie2 = Application.getApplication().pripojAPrihlas(TestUtils.login, true);
        if (prihlasenie2.isChyba()) {
            fail("prihlasenie zlyhalo: " + prihlasenie2.getText());
            return;
        }
        if (!Application.getApplication().getSettings().isOnline()) {
            fail("bolo by fajn byt online");
        }
        final ClovekValue clovekNacitanyZOnline = Application.getBusinessClient().getLudia().get(clovek.getId());
        clovekNacitanyZOnline.setPriezvisko(clovekNacitanyZOnline.getPriezvisko() + "-ZMENENY");
        final NavratValue ulozenie = Application.getBusinessClient().ulozCloveka(clovekNacitanyZOnline);
        if (ulozenie.isChyba()) {
            fail("nepodarilo sa ulozit cloveka: " + ulozenie.getText());
        }
        Application.getApplication().odpojAOdhlas();
        if (Application.getApplication().getSettings().isOnline()) {
            fail("bolo by fajn byt offline");
        }
        final NavratValue prechodOnline = OnlineOfflineHelper.prihlasAMerguj(TestUtils.login, null, false, new MergeSolver() {

            @Override
            public List<ServerObject> vyriesKonflikty(final List<ServerObject> objektyNaRiesenie) {
                if (objektyNaRiesenie.size() != 1) {
                    fail("ocakavam prave jeden objekt");
                    return null;
                }
                objektyNaRiesenie.get(0).setSposobRiesenia(Integer.valueOf(sposobRiesenia));
                if (sposobRiesenia == Ciselniky.RIESENIE_NAHRADIT_KLIENT) {
                    objektyNaRiesenie.get(0).setValue(Utils.ziskajOfflineObjekt(objektyNaRiesenie.get(0).getValue().getId()));
                }
                return objektyNaRiesenie;
            }
        });
        if (prechodOnline.isChyba()) {
            fail("prechod do online rezimu zlyhal: " + prechodOnline.getText());
        }
        switch(sposobRiesenia) {
            case Ciselniky.RIESENIE_NECHAT_SERVER:
                final ClovekValue zoServeru = Application.getBusinessClient().getLudia().get(clovek.getId());
                if (zoServeru == null) {
                    fail("nepodarilo sa nacitat objekt");
                }
                if (!zoServeru.deepEquals(clovekNacitanyZOnline)) {
                    fail("objekt nevyzera tak, ako by mal");
                }
                break;
            case Ciselniky.RIESENIE_NAHRADIT_KLIENT:
                final ClovekValue zKlienta = Application.getBusinessClient().getLudia().get(clovek.getId());
                if (zKlienta == null) {
                    fail("nepodarilo sa nacitat objekt");
                }
                if (!zKlienta.deepEquals(clovekNacitanyZOffline)) {
                    fail("objekt nevyzera tak, ako by mal");
                }
                break;
            default:
                throw new IllegalArgumentException();
        }
        final String zmazanie = TestUtils.zmazTestovacieZaznamy();
        if (zmazanie != null) {
            fail(zmazanie);
        }
        Application.getApplication().odpojAOdhlas();
    }

    private void testujVZTAH_7_ZMENA_NA_SERVERI_STORNO(final int sposobRiesenia) {
        System.out.println("testujVZTAH_7_ZMENA_NA_SERVERI_STORNO");
        Utils.nastavSystemovePremenne();
        final NavratValue prihlasenie = Application.getApplication().pripojAPrihlas(TestUtils.login, true);
        if (prihlasenie.isChyba()) {
            fail("prihlasenie zlyhalo: " + prihlasenie.getText());
            return;
        }
        final ClovekValue clovek = TestUtils.nahodnyClovek(true);
        final NavratValue ulozenieCloveka = Application.getBusinessClient().ulozClovekaNew(clovek);
        if (ulozenieCloveka.isChyba()) {
            fail("zlyhalo ulozenie cloveka: " + ulozenieCloveka.getText());
        }
        clovek.setId((Long) ulozenieCloveka.getValue());
        final NavratValue prechod = OnlineOfflineHelper.prechodOffline(null, false);
        if (prechod.isChyba()) {
            fail("Nastala chyba pri prechode: " + prechod);
        }
        if (Application.getApplication().getSettings().isOnline()) {
            fail("bolo by fajn byt offline");
        }
        final ClovekValue clovekNacitanyZOffline = Application.getBusinessClient().getLudia().get(clovek.getId());
        if (!clovekNacitanyZOffline.deepEquals(clovek)) {
            fail("nacitany clovek sa nezhoduje s povodnym: " + clovek.toString() + "\n" + clovekNacitanyZOffline.toString());
        }
        final NavratValue storno = Application.getBusinessClient().stornoCloveka(clovekNacitanyZOffline);
        if (storno.isChyba()) {
            fail("zlyhalo storno cloveka: " + storno.getText());
        }
        final NavratValue prihlasenie2 = Application.getApplication().pripojAPrihlas(TestUtils.login, true);
        if (prihlasenie2.isChyba()) {
            fail("prihlasenie zlyhalo: " + prihlasenie2.getText());
            return;
        }
        if (!Application.getApplication().getSettings().isOnline()) {
            fail("bolo by fajn byt online");
        }
        final ClovekValue clovekNacitanyZOnline = Application.getBusinessClient().getLudia().get(clovek.getId());
        clovekNacitanyZOnline.setPriezvisko(clovekNacitanyZOnline.getPriezvisko() + "-ZMENENY");
        final NavratValue ulozenie = Application.getBusinessClient().ulozCloveka(clovekNacitanyZOnline);
        if (ulozenie.isChyba()) {
            fail("nepodarilo sa ulozit cloveka: " + ulozenie.getText());
        }
        Application.getApplication().odpojAOdhlas();
        if (Application.getApplication().getSettings().isOnline()) {
            fail("bolo by fajn byt offline");
        }
        final NavratValue prechodOnline = OnlineOfflineHelper.prihlasAMerguj(TestUtils.login, null, false, new MergeSolver() {

            @Override
            public List<ServerObject> vyriesKonflikty(final List<ServerObject> objektyNaRiesenie) {
                if (objektyNaRiesenie.size() != 1) {
                    fail("ocakavam prave jeden objekt");
                    return null;
                }
                objektyNaRiesenie.get(0).setSposobRiesenia(Integer.valueOf(sposobRiesenia));
                return objektyNaRiesenie;
            }
        });
        if (prechodOnline.isChyba()) {
            fail("prechod do online rezimu zlyhal: " + prechodOnline.getText());
        }
        switch(sposobRiesenia) {
            case Ciselniky.RIESENIE_ANO:
                final ClovekValue stornovany = Application.getBusinessClient().getLudia().get(clovek.getId());
                if (stornovany != null) {
                    fail("clovek by nemal byt na serveri vidiet");
                }
                break;
            case Ciselniky.RIESENIE_NIE:
                final ClovekValue zoServeru = Application.getBusinessClient().getLudia().get(clovek.getId());
                if (zoServeru == null) {
                    fail("nepodarilo sa nacitat objekt");
                }
                if (!zoServeru.deepEquals(clovekNacitanyZOnline)) {
                    fail("objekt nevyzera tak, ako by mal");
                }
                break;
            default:
                throw new IllegalArgumentException();
        }
        final String zmazanie = TestUtils.zmazTestovacieZaznamy();
        if (zmazanie != null) {
            fail(zmazanie);
        }
        Application.getApplication().odpojAOdhlas();
    }

    private void testujVZTAH_8_STORNO_POTVRDIT(final int sposobRiesenia) {
        System.out.println("testujVZTAH_8_STORNO_POTVRDIT");
        Utils.nastavSystemovePremenne();
        final NavratValue prihlasenie = Application.getApplication().pripojAPrihlas(TestUtils.login, true);
        if (prihlasenie.isChyba()) {
            fail("prihlasenie zlyhalo: " + prihlasenie.getText());
            return;
        }
        final ClovekValue clovek = TestUtils.nahodnyClovek(true);
        final NavratValue ulozenieCloveka = Application.getBusinessClient().ulozClovekaNew(clovek);
        if (ulozenieCloveka.isChyba()) {
            fail("zlyhalo ulozenie cloveka: " + ulozenieCloveka.getText());
        }
        clovek.setId((Long) ulozenieCloveka.getValue());
        final NavratValue prechod = OnlineOfflineHelper.prechodOffline(null, false);
        if (prechod.isChyba()) {
            fail("Nastala chyba pri prechode: " + prechod);
        }
        if (Application.getApplication().getSettings().isOnline()) {
            fail("bolo by fajn byt offline");
        }
        final ClovekValue clovekNacitanyZOffline = Application.getBusinessClient().getLudia().get(clovek.getId());
        if (!clovekNacitanyZOffline.deepEquals(clovek)) {
            fail("nacitany clovek sa nezhoduje s povodnym: " + clovek.toString() + "\n" + clovekNacitanyZOffline.toString());
        }
        final NavratValue storno = Application.getBusinessClient().stornoCloveka(clovekNacitanyZOffline);
        if (storno.isChyba()) {
            fail("zlyhalo storno cloveka: " + storno.getText());
        }
        final NavratValue prechodOnline = OnlineOfflineHelper.prihlasAMerguj(TestUtils.login, null, false, new MergeSolver() {

            @Override
            public List<ServerObject> vyriesKonflikty(final List<ServerObject> objektyNaRiesenie) {
                if (objektyNaRiesenie.size() != 1) {
                    fail("ocakavam prave jeden objekt");
                    return null;
                }
                objektyNaRiesenie.get(0).setSposobRiesenia(Integer.valueOf(sposobRiesenia));
                return objektyNaRiesenie;
            }
        });
        if (prechodOnline.isChyba()) {
            fail("prechod do online rezimu zlyhal: " + prechodOnline.getText());
        }
        switch(sposobRiesenia) {
            case Ciselniky.RIESENIE_ANO:
                final ClovekValue stornovany = Application.getBusinessClient().getLudia().get(clovek.getId());
                if (stornovany != null) {
                    fail("clovek by nemal byt na serveri vidiet");
                }
                break;
            case Ciselniky.RIESENIE_NIE:
                final ClovekValue zoServeru = Application.getBusinessClient().getLudia().get(clovek.getId());
                if (zoServeru == null) {
                    fail("nepodarilo sa nacitat objekt");
                }
                if (!zoServeru.deepEquals(clovekNacitanyZOffline)) {
                    fail("objekt nevyzera tak, ako by mal");
                }
                break;
            default:
                throw new IllegalArgumentException();
        }
        final String zmazanie = TestUtils.zmazTestovacieZaznamy();
        if (zmazanie != null) {
            fail(zmazanie);
        }
        Application.getApplication().odpojAOdhlas();
    }
}
