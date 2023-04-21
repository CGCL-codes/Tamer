package nw;

import nw.NwException.*;
import java.awt.Graphics;
import java.awt.Color;

/**
 * Az egyik legalapvetőbb digitális építőelemet valósítja meg, az invertert.
 * Egy bemenetű és egy kimenetű kapu a bemeneti értékének negáltját küldi ki a kimenetén.
 * A BreadBoard hozza létre, és szünteti meg.
 */
public class INVERTER extends EpitoElem {

    Port input = null;

    Port output = null;

    INVERTER(BreadBoard bb, WatchDog watchdog) {
        super(bb, watchdog);
    }

    /** Az INVERTER által megvalósított függvény, amely az INVERTER kapu funkciójának megfelelően ellenőrzi, hogy az adott elem helyesen van-e bekötve a kialakított áramkörbe.
         * @return True: Helyes bekotes / False: Hibas bekotes
         */
    @Override
    public boolean ellenoriz() {
        Irdki.indul("INVERTER.ellenoriz()");
        if (input == null) {
            Irdki.vege("INVERTER.ellenoriz():false");
            return false;
        } else {
            Irdki.vege("INVERTER.ellenoriz():true");
            return true;
        }
    }

    /** Hatására az adott INVERTER kiértékeli állapotát, és amennyiben rendelkezik kimenettel/kimenetekkel, és kiküldi a logikai jelet/jeleket.
         * 
         */
    @Override
    public void ertekeld() throws InfinitLoop_NwException {
        Irdki.indul("INVERTER.ertekeld()");
        Irdki.uzenet(3, this.ID + "-es Inverter értéke: " + Irdki.sz(!input.allapotKeres()) + " lesz.");
        output.uzenetKuldes(!input.allapotKeres());
        Irdki.vege("INVERTER.ertekeld()");
    }

    /** Inicializáló függvény, hogy konzisztens állapotba kerüljön az áramkör a szimuláció kezdete előtt. Meghívására alapállapotba kerül az adott kapu.
         * alap inicializáló érték: false.
         */
    @Override
    public void init() {
        Irdki.indul("INVERTER.init()");
        input.init();
        output.init();
        Irdki.vege("INVERTER.init()");
    }

    /** Két EpitoElem összekapcsolódását kezdeményező függvény.
         * Megkéri a hívott felet, hogy a „forrás” típusú portját kapcsolja
         * össze a paraméterben kapott EpitoElem cél típusú portjával.
         * Visszatérési értéke a kapcsolat felépítésének sikerességét jelzi.
         * <i>megjegyzés: A hívott EpitoElem még nem tudja eldönteni,
         * hogy a kért kapcsolat szabályos lesz-e. Mert minden EpitoElem
         * csak az általa megvalósítandó lábak Tipus-át ismeri.
         * Így csak a másik fél tud döntést hozni a kérdésben
         * , s ő is csak azzal a segítséggel, ha megtudja, hogy kimenetet szeretnének-e rákötni.</i>
         * 
         * @param kihez EpitoElem aminek Port-jahoz kapcsolni szeretnenk a masik Port-ot
         * @param forras a felkero objektum (EpitoElem) Port tipusa: Input/Output
         * @param cel a hozzakapcsolni kivant Port tipus-a
         * @return kapcsolodas ' <kihez> ' - boolean: True == Sikeres / False == Sikertelen
         */
    @Override
    public boolean kapcsolodasElemhez(EpitoElem kihez, String forras, String cel) throws UjPortHiba_NwException {
        Irdki.indul("INVERTER.kapcsolodasElemhez(EpitoElem kihez, String forras, String cel)");
        if (forras.equalsIgnoreCase(cel)) return false; else {
            Port seged = new Port(this, wd);
            if (seged == null) throw new UjPortHiba_NwException("Nem jött létre új port"); else if (forras.equalsIgnoreCase("Output")) {
                output = seged;
                Irdki.uzenet(3, "A " + this.ID + " ID-jű elem portja kimenet és létrehozva");
                output.kapcsolatBeallitasa(kihez.kapcsolatKerelem(output, true, cel));
            } else {
                Irdki.uzenet(3, "A " + this.ID + " ID-jű elem portja bemenet és létrehozva");
                input = seged;
                input.kapcsolatBeallitasa(kihez.kapcsolatKerelem(input, false, cel));
            }
        }
        Irdki.vege("INVERTER.kapcsolodasElemhez(EpitoElem kihez, String forras, String cel)");
        return true;
    }

    /** Kapcsolat felépítésekor két EpitoElem típusú objektum kommunikációja, hogy a megkért objektum kapcsolódjon a felkérő objektum forras Port-jára. (Jelezve, hogy a forras Port kimenet-e.) Amennyiben elfogadja a kapcsolatot, úgy a visszatérési érték a felkért objektum – kapcsolathoz használt - Port-ja, amennyiben a bekötés hibát okozna, úgy a felkért EpitoElem visszautasítja a kapcsolatot, s ennek megfelelően NULL értékkel tér vissza.
         * @param forras a felkero objektum (EpitoElem) Port-ja
         * @param kimenet a forras objektum (EpitoElem) Port-ja kimenet-e: TRUE==Igen / FALSE==Nem
         * @param celport a hozzakapcsolni kivan Port tipusa
         * @return a felkert EpitoElem Port-ja. Ha hibát okozna akkor NULL.
         */
    @Override
    public Port kapcsolatKerelem(Port forras, boolean kimenet, String celport) throws UjPortHiba_NwException {
        Irdki.indul("INVERTER.kapcsolatKerelem(Port forras, boolean kimenet, String celport)");
        if (celport.equalsIgnoreCase("Output") && (kimenet == true)) {
            Irdki.uzenet(4, "A " + this.ID + " ID-jű elem kért portja nem hozható létre: kimenet - kimenet kapcsolás miatt");
            return null;
        } else if (celport.equalsIgnoreCase("Input") && (kimenet == false)) {
            Irdki.uzenet(4, "A " + this.ID + " ID-jű elem kért portja nem hozható létre: bemenet - bemenet kapcsolás miatt");
            return null;
        } else {
            Port seged = new Port(this, wd);
            if (seged == null) throw new UjPortHiba_NwException("Nem jött létre új port"); else if (celport.equalsIgnoreCase("Output")) {
                output = seged;
                Irdki.uzenet(3, "A " + this.ID + " ID-jű elem portja kimenet és létrehozva");
                output.kapcsolatBeallitasa(forras);
                Irdki.vege("INVERTER.kapcsolatKerelem(Port forras, boolean kimenet, String celport)");
                return output;
            } else {
                Irdki.uzenet(3, "A " + this.ID + " ID-jű elem portja bemenet és létrehozva");
                input = seged;
                input.kapcsolatBeallitasa(forras);
                Irdki.vege("INVERTER.kapcsolatKerelem(Port forras, boolean kimenet, String celport)");
                return input;
            }
        }
    }

    /**Visszaadja a bementek számát
	*
	*/
    @Override
    public int bemenetekSzama() {
        return 1;
    }

    /**Az ALLAPOT parancsra végül ez a függvény hívódik meg, visszaadja az elem belső állapotát.
	*
	*/
    @Override
    public boolean allapot() {
        return output.allapotKeres();
    }

    /**
     * EpitoElem belso allapotat allitja be
     * @param jel
     */
    @Override
    public void beallit(boolean jel) {
        Irdki.indul("INVERTER.beallit(boolean jel");
        try {
            output.setErtek(jel);
            input.setErtek(jel);
        } catch (Exception ex) {
            Irdki.uzenet(1, "Nincs létrehozott port, így nem lehet beállítani az értéket.");
        }
        Irdki.uzenet(1, this.ID + " ID-ju elem uj allapota: " + Irdki.sz(output.allapotKeres()));
        Irdki.vege("INVERTER.beallit(boolean jel");
        beallitott = true;
    }

    /**
     * Kirajzolja az adott építőelemet a kapott koordinatakba.
     * @param k kapott koordinata
     * @param g grafikus objektum amire rajzolunk
     */
    @Override
    public void Rajzol(Koordinata k, Graphics g) {
        g.setColor(Color.black);
        g.drawString("INV", k.X() + 10, k.Y() + 10);
        g.drawRect(k.X(), k.Y(), k.GetWidth(), k.GetHeight());
        g.drawRect(k.X(), k.Y() + 5, 8, 8);
        g.drawRect(k.X() + k.GetWidth() - 8, k.Y() + 5, 8, 8);
    }

    /**
     * Kapott x, y egér-koordinata alapján meghatározza a port típusát, amire kattintva lett.
     * @param x az egér x koordinátája
     * @param y az egér y koordinátája
     * @return String: Input/Output
     */
    @Override
    public String MelyikPort(int x, int y) {
        Koordinata k = View.Keres(ID);
        if (k.X() <= x && x <= k.X() + 8 && k.Y() <= y && y <= k.Y() + k.GetHeight()) {
            return ("Input");
        } else if (k.X() + k.GetWidth() - 8 < x && x < k.X() + k.GetWidth() && k.Y() + 5 < y && y < k.Y() + 5 + 8) {
            return ("Output");
        } else return null;
    }

    /**
     * Kirajzolja az elem bejövő kapcsolatait
     * @param g grafikus objektum amire rajzolunk
     */
    @Override
    public void KapcsolatRajzol(Graphics g) {
        Koordinata kezdet = Porthelye(View.Keres(ID), input);
        if (input != null) {
            Port tulport = input.getContact();
            Koordinata veg = tulport.getTulaj().Porthelye(View.Keres(tulport.getTulaj().ID), tulport);
            g.setColor(Color.BLACK);
            try {
                g.drawLine(kezdet.X(), kezdet.Y(), veg.X(), veg.Y());
            } catch (Exception e) {
                View.Irdki(e.getMessage());
            }
        }
    }

    /**
     * Megadja az adott port azon pontját ahová az összekötés grafikusan csatlakozik
     * @param k az adott elem koordinátája
     * @param p az adott port
     * @return a grafikus felületen az összekötő vonal portnál lévő koordinátája
     */
    @Override
    public Koordinata Porthelye(Koordinata k, Port p) {
        if (p == output) return new Koordinata(k.X() + k.GetWidth(), (k.Y() + 9)); else if (p == input) return new Koordinata(k.X(), (k.Y() + 9)); else return null;
    }
}
