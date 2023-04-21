package esdomaci.ai;

import java.awt.Point;
import java.util.*;
import esdomaci.gamemap.Stanje;
import esdomaci.mediator.IMapaVidljivoAI;
import esdomaci.mediator.ISimulatorVidljivoAI;
import esdomaci.mediator.IAIVidljivoSimulatoru;
import esdomaci.mediator.IAIVidljivoPlatnu;

/**
 * @author Milan Aleksic, milanaleksic@gmail.com
 */
public class AI implements IAIVidljivoSimulatoru, IAIVidljivoPlatnu {

    private ISimulatorVidljivoAI owner = null;

    private IMapaVidljivoAI mapa = null;

    private Point igrac;

    private Point stariIgrac;

    private int brojPoteza;

    private Point igracPreULP;

    private boolean poslednjiULPJeIsaoKaJedinomNeposecenomPolju = false;

    private ArrayList<Point> ULP = null;

    public AI(ISimulatorVidljivoAI tata, IMapaVidljivoAI dedicatedMapa) {
        owner = tata;
        mapa = dedicatedMapa;
        resetuj();
    }

    public void resetuj() {
        ULP = new ArrayList<Point>();
        brojPoteza = 0;
        igrac = null;
        stariIgrac = null;
    }

    /**
     * Tekstualni opis trenutne odaje na osnovu osecaja
     * @return String koji opisuje lokaciju
     */
    private String opisiTrenutnuOdaju() {
        String tmp = "\n-----СТАТУС потеза " + brojPoteza;
        if (!mapa.osecamMirisKanibala(igrac) && !mapa.osecamPromaju(igrac) && mapa.osecamSjaj(igrac) && ULP.size() == 0) {
            ULP.clear();
            ArrayList<Point> samoOkolneOdaje = mapa.vratiOkolneOdaje(igrac);
            Iterator<Point> iter = samoOkolneOdaje.iterator();
            while (iter.hasNext()) {
                Point odaja = iter.next();
                if (!mapa.vrati(odaja).isPoseceno()) {
                    ULP.add(odaja);
                    if (iter.hasNext()) ULP.add(igrac);
                }
            }
            opisiULP();
            return tmp + "\nСамо злато у околини!!!";
        }
        if (mapa.osecamMirisKanibala(igrac)) tmp += "\nОсећам мирис";
        if (mapa.osecamPromaju(igrac)) tmp += "\nОсећам промају";
        if (mapa.osecamSjaj(igrac)) tmp += "\nОсећам злато !!!";
        if (tmp.equals("\n-----СТАТУС потеза " + brojPoteza)) return tmp + "\nНишта интересантно";
        return tmp;
    }

    private void postaviSumnjuOkolnimPoljima(int sumnja) {
        for (Point odaja : mapa.vratiOkolneOdaje(igrac)) {
            if (!mapa.vrati(odaja).isBezbedno()) {
                if (mapa.vrati(odaja).isSumnjivo()) {
                    mapa.vrati(odaja).setSumnjivost(mapa.vrati(odaja).getSumnjivost() & sumnja);
                    if (!mapa.vrati(odaja).isSumnjivo()) {
                        mapa.vrati(odaja).setPoseceno(false);
                        mapa.vrati(odaja).setFiksnost(Stanje.FIKSNO_BEZBEDNO);
                    }
                } else if (!mapa.vrati(odaja).isFiksno()) mapa.vrati(odaja).setSumnjivost(sumnja); else ;
            }
        }
    }

    private void postaviFiksnoOkolnaPolja(int osobina) {
        for (Point odaja : mapa.vratiOkolneOdaje(igrac)) mapa.vrati(odaja).setFiksnost(osobina);
    }

    private void opisiSvePoznato() {
        String opis = "[" + this.brojPoteza + "]\n    ";
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if ((igrac.x == j) && (igrac.y == i)) opis += 'O'; else if (mapa.vrati(j, i).isSumnjivo() && mapa.vrati(j, i).isFiksno()) opis += 'Q'; else if (mapa.vrati(j, i).isSumnjivo()) {
                    if (!mapa.vrati(j, i).isProstaSumnja()) opis += 'x'; else if (mapa.vrati(j, i).imaSumnjivost(Stanje.SUMNJIVO_KANIBAL)) opis += 'k'; else if (mapa.vrati(j, i).imaSumnjivost(Stanje.SUMNJIVO_ZLATO)) opis += 'z'; else if (mapa.vrati(j, i).imaSumnjivost(Stanje.SUMNJIVO_RUPA)) opis += 'r'; else opis += 'n';
                } else if (mapa.vrati(j, i).isFiksno()) {
                    if (mapa.vrati(j, i).getFiksnost() == Stanje.FIKSNO_KANIBAL + Stanje.FIKSNO_RUPA) opis += 'X'; else if (mapa.vrati(j, i).getFiksnost() == Stanje.FIKSNO_KANIBAL) opis += 'K'; else if (mapa.vrati(j, i).getFiksnost() == Stanje.FIKSNO_ZLATO) opis += 'Z'; else if (mapa.vrati(j, i).isBezbedno()) {
                        if (mapa.vrati(j, i).isPoseceno()) opis += 'B'; else opis += 'J';
                    } else if (mapa.vrati(j, i).getFiksnost() == Stanje.FIKSNO_RUPA) opis += 'R'; else opis += 'N';
                } else opis += '?';
            }
            opis += "\n    ";
        }
        owner.outputAIMap(opis);
    }

    private void obradiPotezIzULP() {
        Point potez = ULP.get(0);
        ULP.remove(0);
        opisiULP();
        setIgrac(potez);
    }

    private void opisiULP() {
        if (ULP.size() == 0) return;
        owner.output("УЛП - још " + ULP.size() + " потез" + (ULP.size() == 1 ? "" : "а") + ":");
        String ostalo = "";
        for (int i = 0; i < ULP.size(); i++) {
            Point iter = ULP.get(i);
            ostalo += "(" + (iter.x + 1) + "," + (iter.y + 1) + ")" + (i == ULP.size() - 1 ? "" : ", ");
        }
        if (!ostalo.equals("")) owner.output(ostalo);
    }

    private ArrayList<Point> postojiPutDoNeposeceneBezbedneOdaje() {
        ArrayList<Point> neposecene = new ArrayList<Point>();
        for (int i = 0; i < 4; i++) for (int j = 0; j < 4; j++) if ((mapa.vrati(i, j).isBezbedno()) && !mapa.vrati(i, j).isPoseceno()) neposecene.add(new Point(i, j));
        if (neposecene.size() == 0) return null;
        ArrayList<ArrayList<Point>> putevi = new ArrayList<ArrayList<Point>>();
        NalazenjePuta gugl = new NalazenjePuta(mapa);
        gugl.Floyd();
        for (int i = 0; i < neposecene.size(); i++) {
            ArrayList<Point> put = gugl.Path(igrac, neposecene.get(i));
            if (put.size() > 0) putevi.add(put);
        }
        ArrayList<Point> izabranPut = null;
        Iterator<ArrayList<Point>> iter = putevi.iterator();
        while (iter.hasNext()) {
            ArrayList<Point> tmp = iter.next();
            if ((izabranPut == null) || (tmp.size() < izabranPut.size())) izabranPut = tmp;
        }
        return izabranPut;
    }

    /**
     * Agresivan proces pravljenja nove ULP liste na osnovu
     * odgovarajuceg zahteva.
     */
    private void napraviULPDoOdaje(Point cilj, String objasnjenje, boolean dozvoliSumnjivePuteve) {
        igracPreULP = igrac;
        if (mapa.vratiPoseceneOdaje().size() - mapa.vratiBezbednePoseceneOdaje().size() == 1) {
            poslednjiULPJeIsaoKaJedinomNeposecenomPolju = true;
            return;
        }
        poslednjiULPJeIsaoKaJedinomNeposecenomPolju = false;
        NalazenjePuta gugl = new NalazenjePuta(mapa, dozvoliSumnjivePuteve);
        gugl.Floyd();
        ArrayList<Point> put = gugl.Path(igrac, cilj);
        if (put.size() == 0) return;
        ULP.clear();
        ULP.addAll(put);
        owner.output(objasnjenje);
        opisiULP();
    }

    /**
     * Ova metoda sve okolne odaje koje su bezbedna oznacava kao 
	 * neposecene da bi igrac mogla ponovo da prodje kroz njih
     */
    private void obeleziSveBezbedneOdajeKaoNeposeceneOkoOveOdaje(Point odaja) {
        for (Point pt : mapa.vratiOkolneOdaje(odaja)) {
            if (pt.equals(igrac)) continue;
            Stanje ptStanje = mapa.vrati(pt);
            if (ptStanje.isPoseceno() && ptStanje.isBezbedno()) {
                if (igracPreULP == null) ptStanje.setPoseceno(false); else if (!(igracPreULP.equals(ptStanje)) && (poslednjiULPJeIsaoKaJedinomNeposecenomPolju)) ptStanje.setPoseceno(false);
            }
        }
    }

    /**
     * Kompletna obrada trenutne odaje na kojoj se nalazi
     * igrac.
     */
    public void obradaTrenutneOdaje() {
        mapa.vrati(igrac).setPoseceno(true);
        String str = mapa.krajIgre(igrac);
        if (str != null) {
            owner.output(str);
            owner.osveziPanel();
            return;
        }
        mapa.vrati(igrac).setFiksnost(Stanje.FIKSNO_BEZBEDNO);
        int sumnja = 0;
        if (mapa.osecamMirisKanibala(igrac)) sumnja |= Stanje.SUMNJIVO_KANIBAL;
        if (mapa.osecamPromaju(igrac)) sumnja |= Stanje.SUMNJIVO_RUPA;
        if (mapa.osecamSjaj(igrac)) sumnja |= Stanje.SUMNJIVO_ZLATO;
        postaviSumnjuOkolnimPoljima(sumnja);
        if (!mapa.osecamMirisKanibala(igrac) && !mapa.osecamPromaju(igrac) && !mapa.osecamSjaj(igrac)) postaviFiksnoOkolnaPolja(Stanje.FIKSNO_BEZBEDNO);
        owner.output(opisiTrenutnuOdaju());
        opisiSvePoznato();
        owner.osveziPanel();
        obradaHeuristika();
    }

    /**
     * Vraca true ukoliko postoji mogucnost da se izvede riskantan potez
     * koji ispunjava odgovarajuc kriterijum
     */
    public boolean mogucanRizicanPotez(int rizik, String opis) {
        ArrayList<Point> sumnjiveOdaje = mapa.vratiOdajeSaSumnjom(rizik, true);
        if (sumnjiveOdaje.size() > 0) {
            int slucajni = (int) Math.round(Math.random() * (sumnjiveOdaje.size() - 1));
            napraviULPDoOdaje(sumnjiveOdaje.get(slucajni), opis, true);
            obradiPotezIzULP();
            obradaTrenutneOdaje();
            return true;
        }
        return false;
    }

    /**
     * Obrada primitivnog pomeraja unapred
     */
    public void korakUnapred() {
        if (mapa.krajIgre(igrac) != null) {
            owner.output("\nИгра је завршена!");
            return;
        }
        brojPoteza++;
        if (ULP.size() > 0) {
            obradiPotezIzULP();
            obradaTrenutneOdaje();
            return;
        }
        if (mogucanRizicanPotez(Stanje.SUMNJIVO_ZLATO, "Ризиковаћу око злата")) return;
        ArrayList<Point> put = null;
        put = postojiPutDoNeposeceneBezbedneOdaje();
        if (put != null) {
            owner.output("Идем у сигурну одају!");
            ULP.addAll(put);
            obradiPotezIzULP();
            obradaTrenutneOdaje();
            return;
        }
        if (mogucanRizicanPotez(Stanje.SUMNJIVO_KANIBAL | Stanje.SUMNJIVO_ZLATO, "Ризиковаћу где мислим да је и злато и канибал")) return;
        if (mogucanRizicanPotez(Stanje.SUMNJIVO_KANIBAL, "Ризиковаћу око канибала")) return;
        if (mogucanRizicanPotez(Stanje.SUMNJIVO_RUPA | Stanje.SUMNJIVO_ZLATO, "Ризиковаћу где мислим да је и злато и рупа")) return;
        if (mogucanRizicanPotez(Stanje.SUMNJIVO_RUPA, "Ризиковаћу око рупе")) return;
        if (mogucanRizicanPotez(Stanje.SUMNJIVO_KANIBAL | Stanje.SUMNJIVO_RUPA | Stanje.SUMNJIVO_ZLATO, "Ризиковаћу око комбинације злата и комбиноване опасности")) return;
        if (mogucanRizicanPotez(Stanje.SUMNJIVO_KANIBAL | Stanje.SUMNJIVO_RUPA, "Ризиковаћу око комбиноване опасности")) return;
        ArrayList<Point> smrtneOdaje = mapa.vratiFiksneOpasneOdaje();
        if (smrtneOdaje.size() > 0) {
            int slucajni = (int) Math.round(Math.random() * (smrtneOdaje.size() - 1));
            napraviULPDoOdaje(smrtneOdaje.get(slucajni), "Намерно гинем јер немам где !!!", true);
            obradiPotezIzULP();
            obradaTrenutneOdaje();
            return;
        }
    }

    private void obradaHeuristika() {
        boolean radioHPF = false;
        boolean radioHUSPF = false;
        boolean radioHUSDR = false;
        do {
            if (mapa.vratiBezbedneNePoseceneOdaje().size() == 0) radioHPF = heuristikaPrepoznavanjaFiksova(); else radioHPF = false;
            radioHUSDR = heuristikaUklanjanjaSumnjiNaDalekeRupe();
            radioHUSPF = heuristikaUklanjanjaSumnjiPrepoznatihFikseva();
            if (radioHPF) owner.output("ХПФ АКЦИЈА");
            if (radioHUSPF) owner.output("ХУСПФ АКЦИЈА");
            if (radioHUSDR) owner.output("ХУСДР АКЦИЈА");
            if (radioHPF || radioHUSPF || radioHUSDR) opisiSvePoznato();
        } while (radioHPF || radioHUSPF || radioHUSDR);
    }

    /**
     * Heuristika : pretraga za 
     *  B
     * BSB
     *  B
     * ili primitivnim dijagonalnim segmentima 
     */
    private boolean heuristikaPrepoznavanjaFiksova() {
        for (Point sumnjiva : mapa.vratiSumnjiveOdaje()) {
            ArrayList<Point> okolneBezbedne = new ArrayList<Point>();
            int brojBezbednih = 0;
            for (Point okolna : mapa.vratiOkolneOdaje(sumnjiva)) {
                Stanje tmpStanje = mapa.vrati(okolna);
                if (tmpStanje.isPoseceno() && tmpStanje.isBezbedno()) {
                    brojBezbednih++;
                    okolneBezbedne.add(okolna);
                }
            }
            if (brojBezbednih >= 2) {
                if (mapa.vrati(sumnjiva).isProstaSumnja()) {
                    if (mapa.vrati(sumnjiva).imaSumnjivost(Stanje.SUMNJIVO_RUPA)) if (ovaOdajaJeSumnjaNaRupuIzmedjuDveDijagonalneSumnjeIliFiksaNaRupu(sumnjiva)) return false;
                    fiksirajOpasnost(sumnjiva, mapa.vrati(sumnjiva).getSumnjivost());
                } else {
                    ArrayList<Point> okolneOkolnih = new ArrayList<Point>();
                    for (Point okolnaBezbedna : okolneBezbedne) for (Point iter : mapa.vratiOkolneOdaje(okolnaBezbedna)) if ((iter != sumnjiva) && (iter.equals(sumnjiva)) && (mapa.vrati(iter).isSumnjivo())) okolneOkolnih.add(iter);
                    int brojPotvrdeKanibala = 0;
                    for (Point pt : okolneOkolnih) if (mapa.vrati(pt).imaSumnjivost(Stanje.SUMNJIVO_KANIBAL)) brojPotvrdeKanibala++;
                    if (brojPotvrdeKanibala >= 2) fiksirajOpasnost(sumnjiva, Stanje.FIKSNO_KANIBAL);
                }
                if (mapa.vrati(sumnjiva).getFiksnost() == Stanje.FIKSNO_ZLATO) napraviULPDoOdaje(sumnjiva, "ХПС је пронашао злато, мењам УЛП", true);
                return true;
            }
        }
        return false;
    }

    /**
     * Uklanjanje svih sumnji koje su suvisne u matrici igre.
     * Npr. ako je pronadjen kanibal onda se sve sumnje za kanibalom
     * uklanjaju iz matrice igre.
     */
    private boolean heuristikaUklanjanjaSumnjiPrepoznatihFikseva() {
        boolean pronadjenoZlato = false;
        boolean pronadjenKanibal = false;
        int brojPronadjenihRupa = 0;
        boolean izmenjeno = false;
        for (int i = 0; i < 4; i++) for (int j = 0; j < 4; j++) {
            if (mapa.vrati(i, j).imaFiksnost(Stanje.FIKSNO_KANIBAL)) pronadjenKanibal = true;
            if (mapa.vrati(i, j).imaFiksnost(Stanje.FIKSNO_ZLATO)) pronadjenoZlato = true;
            if (mapa.vrati(i, j).imaFiksnost(Stanje.FIKSNO_RUPA)) brojPronadjenihRupa++;
        }
        for (int i = 0; i < 4; i++) for (int j = 0; j < 4; j++) {
            if ((pronadjenKanibal) && (mapa.vrati(i, j).imaSumnjivost(Stanje.SUMNJIVO_KANIBAL))) {
                mapa.vrati(i, j).setSumnjivost(mapa.vrati(i, j).getSumnjivost() - Stanje.SUMNJIVO_KANIBAL);
                if (!mapa.vrati(i, j).isSumnjivo()) mapa.vrati(i, j).setFiksnost(Stanje.FIKSNO_BEZBEDNO);
                izmenjeno = true;
            } else if ((pronadjenoZlato) && (mapa.vrati(i, j).imaSumnjivost(Stanje.SUMNJIVO_ZLATO))) {
                mapa.vrati(i, j).setSumnjivost(mapa.vrati(i, j).getSumnjivost() - Stanje.SUMNJIVO_ZLATO);
                if (!mapa.vrati(i, j).isSumnjivo()) mapa.vrati(i, j).setFiksnost(Stanje.FIKSNO_BEZBEDNO);
                izmenjeno = true;
            } else if ((brojPronadjenihRupa == 2) && (mapa.vrati(i, j).imaSumnjivost(Stanje.SUMNJIVO_RUPA))) {
                mapa.vrati(i, j).setSumnjivost(mapa.vrati(i, j).getSumnjivost() - Stanje.SUMNJIVO_RUPA);
                if (!mapa.vrati(i, j).isSumnjivo()) mapa.vrati(i, j).setFiksnost(Stanje.FIKSNO_BEZBEDNO);
                izmenjeno = true;
            }
        }
        return izmenjeno;
    }

    /**
     * U skracenom obliku moze se reci: 
     * "Ukloni moguce sumnje na rupu oko fiksne rupe ukoliko 
     * postoji bar jedna sumnja koja ne pripada skupu mogucih 
     * na ovu fiksnu rupu"
     * 
     * "Moguce sumnje" se definisu kao sve one odaje na kojima
     * je mozda prijavljena sumnja na nasu fiksnu rupu
     */
    private boolean heuristikaUklanjanjaSumnjiNaDalekeRupe() {
        boolean izmenjeno = false;
        ArrayList<Point> fiksneRupe = new ArrayList<Point>();
        for (int i = 0; i < 4; i++) for (int j = 0; j < 4; j++) {
            if (mapa.vrati(i, j).isFiksno()) if (mapa.vrati(i, j).imaFiksnost(Stanje.FIKSNO_RUPA)) fiksneRupe.add(new Point(i, j));
        }
        if (fiksneRupe.size() != 1) return false;
        Point fiksiranaRupa = fiksneRupe.get(0);
        ArrayList<Point> sumnjeOkoFiksneRupe = new ArrayList<Point>();
        for (Point odajaSaMogucomSumnjom : mapa.vratiSveOdajeKojeMoguDaSumnjajuNaOvuOdaju(fiksiranaRupa)) if (mapa.vrati(odajaSaMogucomSumnjom).imaSumnjivost(Stanje.SUMNJIVO_RUPA)) sumnjeOkoFiksneRupe.add(odajaSaMogucomSumnjom);
        if (sumnjeOkoFiksneRupe.size() == 0) return false;
        for (Point sumnjaNaUdaljenuRupu : mapa.vratiOdajeSaSumnjom(Stanje.SUMNJIVO_RUPA, false)) {
            if (!sumnjeOkoFiksneRupe.contains(sumnjaNaUdaljenuRupu)) {
                int cntEliminisucih = 0;
                for (Point okolnaBezbednaOkoUdaljeneRupe : mapa.vratiOkolneOdaje(sumnjaNaUdaljenuRupu)) {
                    Stanje stanjeOkolneBezbedneOdajeOkoUdaljeneRupe = mapa.vrati(okolnaBezbednaOkoUdaljeneRupe);
                    if ((stanjeOkolneBezbedneOdajeOkoUdaljeneRupe.isBezbedno()) && (stanjeOkolneBezbedneOdajeOkoUdaljeneRupe.isPoseceno())) cntEliminisucih++; else if (stanjeOkolneBezbedneOdajeOkoUdaljeneRupe.isFiksno()) cntEliminisucih++;
                }
                if (cntEliminisucih < 2) continue;
                for (Point sumnjaZaUklanjanje : sumnjeOkoFiksneRupe) {
                    Stanje stanjeZaUklanjanje = mapa.vrati(sumnjaZaUklanjanje);
                    if (stanjeZaUklanjanje.imaSumnjivost(Stanje.SUMNJIVO_RUPA)) {
                        stanjeZaUklanjanje.setSumnjivost(stanjeZaUklanjanje.getSumnjivost() - Stanje.SUMNJIVO_RUPA);
                        if (!stanjeZaUklanjanje.isSumnjivo()) stanjeZaUklanjanje.setFiksnost(Stanje.FIKSNO_BEZBEDNO);
                        izmenjeno = true;
                    }
                }
            }
        }
        return izmenjeno;
    }

    private void fiksirajOpasnost(Point odaja, int fiks) {
        mapa.vrati(odaja).setFiksnost(fiks);
        owner.output("ФИКСНО: (" + (odaja.x + 1) + "," + (odaja.y + 1) + ") је " + mapa.vrati(odaja));
        obeleziSveBezbedneOdajeKaoNeposeceneOkoOveOdaje(odaja);
    }

    private boolean nePostojiSumnjaNaRupuVanOveDijagonale(Point d1, Point d2, Point d3) {
        ArrayList<Point> sumnjiveOdaje = mapa.vratiOdajeSaSumnjom(Stanje.SUMNJIVO_RUPA, false);
        sumnjiveOdaje.remove(d1);
        sumnjiveOdaje.remove(d2);
        sumnjiveOdaje.remove(d3);
        return sumnjiveOdaje.size() == 0;
    }

    /**
     * Argumenti ovoj metodi su elementi dijagonale sa levo na desno!
     * Ova heuristika vraca TRUE ako se zabranjuje da se
     * srednja sumnja u dijagonali pretvori u fiks.
     * Fazon kod heuristike je taj sto gleda paralelnu dijagonalu
     * koja je udaljena za dva mesta i na osnovu nje zakljucuje
     * koji element od ova tri treba da bude fiksan!!! 
     */
    private boolean heuristikaDugeDijagonaleRupa(Point d1, Point d2, Point d3) {
        if (nePostojiSumnjaNaRupuVanOveDijagonale(d1, d2, d3)) {
            owner.output("ХДДР АКЦИЈА");
            return true;
        }
        if (d2.y + 2 < 4) if (mapa.vrati(d2.x, d2.y + 2).imaSumnjivost(Stanje.SUMNJIVO_RUPA)) {
            mapa.vrati(d2).setSumnjivost(mapa.vrati(d2).getSumnjivost() - Stanje.SUMNJIVO_RUPA);
            if (d1.y > d3.y) fiksirajOpasnost(d3, Stanje.FIKSNO_RUPA); else fiksirajOpasnost(d1, Stanje.FIKSNO_RUPA);
            owner.output("ХДДР АКЦИЈА");
            return false;
        }
        if (d2.y - 2 >= 0) if (mapa.vrati(d2.x, d2.y - 2).imaSumnjivost(Stanje.SUMNJIVO_RUPA)) {
            mapa.vrati(d2).setSumnjivost(mapa.vrati(d2).getSumnjivost() - Stanje.SUMNJIVO_RUPA);
            if (d1.y > d3.y) fiksirajOpasnost(d1, Stanje.FIKSNO_RUPA); else fiksirajOpasnost(d3, Stanje.FIKSNO_RUPA);
            owner.output("ХДДР АКЦИЈА");
            return false;
        }
        if (d2.x + 2 < 4) if (mapa.vrati(d2.x + 2, d2.y).imaSumnjivost(Stanje.SUMNJIVO_RUPA)) {
            mapa.vrati(d2).setSumnjivost(mapa.vrati(d2).getSumnjivost() - Stanje.SUMNJIVO_RUPA);
            if (d1.y > d3.y) fiksirajOpasnost(d1, Stanje.FIKSNO_RUPA); else fiksirajOpasnost(d3, Stanje.FIKSNO_RUPA);
            owner.output("ХДДР АКЦИЈА");
            return false;
        }
        if (d2.x - 2 >= 0) if (mapa.vrati(d2.x - 2, d2.y).imaSumnjivost(Stanje.SUMNJIVO_RUPA)) {
            mapa.vrati(d2).setSumnjivost(mapa.vrati(d2).getSumnjivost() - Stanje.SUMNJIVO_RUPA);
            if (d1.y > d3.y) fiksirajOpasnost(d3, Stanje.FIKSNO_RUPA); else fiksirajOpasnost(d1, Stanje.FIKSNO_RUPA);
            owner.output("ХДДР АКЦИЈА");
            return false;
        }
        return true;
    }

    private boolean ovaOdajaJeSumnjaNaRupuIzmedjuDveDijagonalneSumnjeIliFiksaNaRupu(Point odaja) {
        if ((odaja.x == 0) || (odaja.x == 3)) return false;
        if ((odaja.y == 0) || (odaja.y == 3)) return false;
        Point nw = new Point(odaja.x - 1, odaja.y - 1);
        Point se = new Point(odaja.x + 1, odaja.y + 1);
        if (mapa.vrati(nw).imaSumnjivostIliFiksnost(Stanje.SUMNJIVO_RUPA) && mapa.vrati(se).imaSumnjivostIliFiksnost(Stanje.SUMNJIVO_RUPA)) {
            return heuristikaDugeDijagonaleRupa(nw, odaja, se);
        }
        Point ne = new Point(odaja.x + 1, odaja.y - 1);
        Point sw = new Point(odaja.x - 1, odaja.y + 1);
        if (mapa.vrati(ne).imaSumnjivostIliFiksnost(Stanje.SUMNJIVO_RUPA) && mapa.vrati(sw).imaSumnjivostIliFiksnost(Stanje.SUMNJIVO_RUPA)) {
            return heuristikaDugeDijagonaleRupa(sw, odaja, ne);
        }
        return false;
    }

    public Point getIgrac() {
        return igrac;
    }

    public Point getStariIgrac() {
        return stariIgrac;
    }

    public ArrayList<Point> getULP() {
        return ULP;
    }

    public void setIgrac(Point igrac) {
        stariIgrac = this.igrac;
        this.igrac = igrac;
        mapa.vrati(igrac).setPoseceno(true);
        if (stariIgrac != null) owner.output("   Играч (" + (stariIgrac.x + 1) + "," + (stariIgrac.y + 1) + ")->(" + (igrac.x + 1) + "," + (igrac.y + 1) + ")"); else owner.output("   Играч=(" + (igrac.x + 1) + "," + (igrac.y + 1) + ")");
    }

    public boolean getSpremnoZaRad() {
        if (igrac == null) return false;
        return true;
    }
}
