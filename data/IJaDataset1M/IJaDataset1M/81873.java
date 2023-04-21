package nl.huub.van.amelsvoort.duke.tools;

import obj.ObjImport;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import nl.huub.van.amelsvoort.duke.tools.map.Begin;
import nl.huub.van.amelsvoort.duke.tools.map.Figuur;
import nl.huub.van.amelsvoort.duke.tools.map.Map;
import nl.huub.van.amelsvoort.duke.tools.map.Muur;
import nl.huub.van.amelsvoort.duke.tools.map.Sector;
import obj.VeelhoekPaneel;

/**
 *
 * @author huub
 * Aanmaken van de map voor Eduke32 / HuubNbDuke
 */
public class HuubDukeMap {

    private String eduke;

    private String mapuitvoer = "/newboard.map";

    /**
   * Lezen van een nog nader te definieren
   * tekst bestand uit Blender en
   * schrijven van een duke map
   * Voorlopig eerst een bestaande map lezen
   * om een tekst bestand te definieren
   * @throws FileNotFoundException
   * @throws IOException
   */
    public void uitvoeren(String mapBestand) throws IOException, FileNotFoundException, Exception {
        ObjImport objImport = new ObjImport();
        objImport.uitvoeren("gegevens/tweeblokken.obj");
        schrijvenObjNaarCsv(Help.csvBestand, objImport);
        Map map = leesInkomendBestand();
        schrijfMapBestand(map);
    }

    /**
   * Voorlopig uit een zelf gemaakt csv tekstbestand, 
   * maar later uit bestaande 3d bestanden (Blender o.i.d)
   */
    private Map leesInkomendBestand() throws IOException, Exception {
        Map map = new Map();
        try {
            BufferedReader in = new BufferedReader(new FileReader(Help.csvBestand));
            String str;
            while ((str = in.readLine()) != null) {
                if (!str.startsWith("#")) verwerkGelezenRegel(str, map); else Help.printf("Informatie :%s\n", str);
            }
            in.close();
        } catch (IOException e) {
            System.err.println("Foutmelding lezen inkomend bestand : " + e.getMessage());
        }
        return map;
    }

    /**
 * Schrijven naar een binair map bestand voor .eduke32
 * @param map
 * @throws FileNotFoundException
 * @throws IOException
 * @throws Exception
 */
    private void schrijfMapBestand(Map map) throws FileNotFoundException, IOException, Exception {
        if (isEdukeAanwezig()) {
            schrijvenBestand(map);
        }
    }

    /**
   * Hier komt een regel binnen met pipe teken
   * @param str 
   */
    private void verwerkGelezenRegel(String str, Map map) throws IOException, Exception {
        int teller = -1;
        String onthoud_token = null;
        for (StringTokenizer stringTokenizer = new StringTokenizer(str, "|"); stringTokenizer.hasMoreTokens(); ) {
            String token = stringTokenizer.nextToken();
            if (token.compareTo(Konstanten.TEKST_BEGIN) == 0 || token.compareTo(Konstanten.TEKST_SECTOR) == 0 || token.compareTo(Konstanten.TEKST_MUUR) == 0 || token.compareTo(Konstanten.TEKST_FIGUUR) == 0) {
                onthoud_token = token;
                teller = 0;
            }
            if (teller >= 0) {
                if (onthoud_token.compareTo(Konstanten.TEKST_BEGIN) == 0 && teller > 0) {
                    map.getBegin().verwerkBegin(map, teller, token);
                }
                if (onthoud_token.compareTo(Konstanten.TEKST_SECTOR) == 0 && teller > 0) {
                    map.getSector().verwerkSector(map.getSector(), teller, token);
                }
                if (onthoud_token.compareTo(Konstanten.TEKST_MUUR) == 0 && teller > 0) {
                    map.getMuur().verwerkMuur(map.getMuur(), teller, token);
                }
                if (onthoud_token.compareTo(Konstanten.TEKST_FIGUUR) == 0 && teller > 0) {
                    map.getFiguur().verwerkFiguur(map.getFiguur(), teller, token);
                }
                teller++;
            }
        }
        if (teller >= 0) {
            if (onthoud_token.compareTo(Konstanten.TEKST_SECTOR) == 0) {
                map.getSectoren().add(map.getSector());
                map.setSector(new Sector());
            }
            if (onthoud_token.compareTo(Konstanten.TEKST_MUUR) == 0) {
                map.getMuren().add(map.getMuur());
                map.setMuur(new Muur());
            }
            if (onthoud_token.compareTo(Konstanten.TEKST_FIGUUR) == 0) {
                map.getFiguren().add(map.getFiguur());
                map.setFiguur(new Figuur());
            }
        }
    }

    /**
   * Voorlopig controle of .eduke32 bestaat in het home dir
   * @return
   */
    private boolean isEdukeAanwezig() {
        String home = System.getenv("HOME");
        eduke = home + "/.eduke32";
        Help.print(eduke);
        File dir = new File(eduke);
        boolean isDir = dir.isDirectory();
        if (isDir) {
            return true;
        } else {
            Help.print("Directory bestaat niet : " + eduke);
            return false;
        }
    }

    /**
   * Overgenomen van wad2map.c waar uiteindelijk deze
   * uitvoerende zaken worden gedaan.
   * In de map zitten de juiste gegevens of
   * ze worden hierna overschreven met default waarden
   * zie map.write
   */
    private void schrijvenBestand(Map map) throws FileNotFoundException, IOException, Exception {
        DataOutputStream bestand = new DataOutputStream(new FileOutputStream(eduke + mapuitvoer));
        map.write(bestand, map);
        bestand.close();
    }

    /**
   * Lees een voorbeeld map bestand
   * en schrijf naar een tekstbestand om weer te lezen
   */
    private void leesVoorbeeld(String mapBestand) {
        Map map = new Map();
        try {
            FileWriter write = new FileWriter(Help.csvBestand, false);
            write.close();
            write = new FileWriter(Help.csvBestand, true);
            DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(mapBestand)));
            Begin begin = vulBegin(in);
            begin.setInformatie(mapBestand);
            for (int i = 0; i < begin.getAantal_sectors(); i++) {
                ArrayList<Sector> sector = map.getSectoren();
                sector.add(vulSector(in));
            }
            int aantalMuren = Help.endianShort(in.readShort());
            Help.printf("aantalMuren : %d\n", aantalMuren);
            for (int i = 0; i < aantalMuren; i++) {
                ArrayList<Muur> muur = map.getMuren();
                muur.add(vulWall(in));
            }
            int aantalFiguren = Help.endianShort(in.readShort());
            Help.printf("aantalFiguren : %d\n", aantalFiguren);
            for (int i = 0; i < aantalFiguren; i++) {
                ArrayList<Figuur> figuur = map.getFiguren();
                figuur.add(vulFiguur(in));
            }
            begin.print(write);
            for (Sector sector : map.getSectoren()) {
                sector.print(write);
            }
            for (Muur muur : map.getMuren()) {
                muur.print(write);
            }
            for (Figuur figuur : map.getFiguren()) {
                figuur.print(write);
            }
            in.close();
            write.close();
        } catch (IOException e) {
            System.err.println("Foutmelding : " + e.getMessage());
        }
    }

    /**
   * Eerst 22 bytes versienummer e.d.
  mapversion,4
   * start positie
  posx,4
  posy,4
  posz,4          //Note: Z coordinates are all shifted up 4
  ang,2           //All angles are from 0-2047, clockwise
  cursectnum,2    //Sector of starting point
  //Load all sectors (see vulSector structure described below)
  &numsectors,2
   * @param tel
   * @param kar
   */
    private Begin vulBegin(DataInputStream in) throws IOException {
        Begin begin = new Begin();
        begin.leesBegin(in);
        return begin;
    }

    /**
  //sizeof(sectortype) = 40
  typedef struct
  {
  short wallptr, wallnum;
  long ceilingz, floorz;
  short ceilingstat, floorstat;
  short ceilingpicnum, ceilingheinum;
  signed char ceilingshade;
  char ceilingpal, ceilingxpanning, ceilingypanning;
  short floorpicnum, floorheinum;
  signed char floorshade;
  char floorpal, floorxpanning, floorypanning;
  char visibility, filler;
  short lotag, hitag, extra;
  } sectortype;
  sectortype vulSector[1024];
   * @param tel
   * @param kar
   */
    private Sector vulSector(DataInputStream in) throws IOException {
        Sector sector = new Sector();
        sector.leesSector(in);
        return sector;
    }

    /**
   * sizeof(walltype) = 32
  typedef struct
  {
  long x, y;
  short point2, nextwall, nextsector, cstat;
  short picnum, overpicnum;
  signed char shade;
  char pal, xrepeat, yrepeat, xpanning, ypanning;
  short lotag, hitag, extra;
  } walltype;
  walltype wall[8192];
   * @param tel
   * @param kar
   * @param walnummer
   */
    private Muur vulWall(DataInputStream in) throws IOException {
        Muur muur = new Muur();
        muur.leesMuur(in);
        return muur;
    }

    /**
  //sizeof(spritetype) = 44
  typedef struct
  {
  long x, y, z;
  short cstat, picnum;
  signed char shade;
  char pal, clipdist, filler;
  unsigned char xrepeat, yrepeat;
  signed char xoffset, yoffset;
  short sectnum, statnum;
  short ang, owner, xvel, yvel, zvel;
  short lotag, hitag, extra;
  } spritetype; *
   * @param tel
   * @param kar
   */
    private Figuur vulFiguur(DataInputStream in) throws IOException {
        Figuur figuur = new Figuur();
        figuur.leesFiguur(in);
        return figuur;
    }

    /**
 * Schrijf naar csvBestand uit het geimporteerde obj bestand
 * @param csvBestand
 * @param objImport
 */
    private void schrijvenObjNaarCsv(String csvBestand, ObjImport objImport) throws IOException {
        Map map = new Map();
        Begin begin = new Begin(objImport);
        ArrayList<Figuur> figuren = map.getFiguren();
        VeelhoekPaneel veelhoekPaneel = new VeelhoekPaneel(objImport.getZijden(), objImport.getDriehoeken());
        try {
            FileWriter write = new FileWriter(csvBestand, false);
            write.close();
            write = new FileWriter(Help.csvBestand, true);
            int aantalMuren = veelhoekPaneel.bepaalAantalMuren();
            Help.printf("aantalMuren : %d\n", aantalMuren);
            SectorenMuren sectorenMuren = new SectorenMuren(map, objImport.getDriehoeken(), objImport.getZijden());
            sectorenMuren.samenstellen();
            int aantalFiguren = 0;
            Help.printf("aantalFiguren : %d\n", aantalFiguren);
            for (int i = 0; i < aantalFiguren; i++) {
                Figuur figuur = new Figuur();
                figuren.add(figuur);
            }
            begin.print(write);
            for (Sector sector : map.getSectoren()) {
                sector.print(write);
            }
            for (Muur muur : map.getMuren()) {
                muur.print(write);
            }
            for (Figuur figuur : map.getFiguren()) {
                figuur.print(write);
            }
            write.close();
        } catch (IOException e) {
            System.err.println("Foutmelding : " + e.getMessage());
        }
    }
}
