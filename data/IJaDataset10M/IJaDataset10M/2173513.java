package apollo.dataadapter;

import java.util.*;
import java.io.*;
import apollo.seq.io.*;
import apollo.datamodel.*;
import apollo.config.Config;
import org.apache.log4j.*;
import org.bdgp.io.*;
import org.bdgp.util.*;
import org.bdgp.swing.widget.*;

public class ChromosomeAdapter {

    protected static final Logger logger = LogManager.getLogger(ChromosomeAdapter.class);

    String filename;

    Vector karyotypes;

    public void init() {
    }

    public String getName() {
        return "Chromosome properties";
    }

    public String getType() {
        return "Chromosome file";
    }

    public ChromosomeAdapter(String filename) {
        this.filename = filename;
        try {
            this.karyotypes = parse_data();
        } catch (Exception e) {
            logger.error("Execption parsing chromosome file " + e, e);
        }
    }

    public ChromosomeAdapter() {
    }

    public void setFilename(String filename) {
        this.filename = filename;
        try {
            this.karyotypes = parse_data();
        } catch (Exception e) {
            logger.error("Execption parsing chromosome file " + e, e);
        }
    }

    public Vector getKaryotypes() {
        return karyotypes;
    }

    public Karyotype getKaryotypeBySpeciesName(String name) {
        for (int i = 0; i < karyotypes.size(); i++) {
            Karyotype k = (Karyotype) karyotypes.elementAt(i);
            if (k.getSpeciesName().equals(name)) {
                return k;
            }
        }
        return null;
    }

    public Vector parse_data() throws ApolloAdapterException {
        karyotypes = new Vector();
        Hashtable chromosomes;
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(filename)));
            BufferedReader dataIn = new BufferedReader(new InputStreamReader(bis));
            String line;
            Hashtable orgs = new Hashtable();
            while ((line = dataIn.readLine()) != null) {
                StringTokenizer str = new StringTokenizer(line);
                int count = str.countTokens();
                if (count != 6) {
                    logger.error("Wrong number of tokens [" + count + "] should be 6");
                } else {
                    String organism = str.nextToken();
                    String chrname = str.nextToken();
                    int start = Integer.parseInt(str.nextToken());
                    int end = Integer.parseInt(str.nextToken());
                    String bandid = str.nextToken();
                    String stain = str.nextToken();
                    ChromosomeBand band = new ChromosomeBand(bandid, chrname, start, end, stain);
                    Hashtable chrs;
                    if (!orgs.containsKey(organism)) {
                        chrs = new Hashtable();
                        orgs.put(organism, chrs);
                    } else {
                        chrs = (Hashtable) orgs.get(organism);
                    }
                    Vector bandvec;
                    if (!chrs.containsKey(chrname)) {
                        bandvec = new Vector();
                        chrs.put(chrname, bandvec);
                    } else {
                        bandvec = (Vector) chrs.get(chrname);
                    }
                    bandvec.addElement(band);
                }
            }
            Enumeration en = orgs.keys();
            chromosomes = new Hashtable();
            while (en.hasMoreElements()) {
                String org = (String) en.nextElement();
                Hashtable chr = (Hashtable) orgs.get(org);
                Enumeration en2 = chr.keys();
                Vector chrs = new Vector();
                chromosomes.put(org, chrs);
                while (en2.hasMoreElements()) {
                    String chrname = (String) en2.nextElement();
                    Vector bands = (Vector) chr.get(chrname);
                    Chromosome newchr = new Chromosome(org, chrname, bands);
                    chrs.addElement(newchr);
                }
            }
            Enumeration en3 = chromosomes.keys();
            while (en3.hasMoreElements()) {
                String org = (String) en3.nextElement();
                Vector chrs = (Vector) chromosomes.get(org);
                Karyotype kary = new Karyotype(org, chrs);
                karyotypes.addElement(kary);
            }
        } catch (IOException ioex) {
            logger.error(ioex.getMessage(), ioex);
        }
        return karyotypes;
    }

    public static void main(String[] args) {
        try {
            ChromosomeAdapter ad = new ChromosomeAdapter(args[0]);
            Vector kary = ad.getKaryotypes();
            for (int i = 0; i < kary.size(); i++) {
                Karyotype ka = (Karyotype) kary.elementAt(i);
                logger.info("Org " + ka.getSpeciesName());
                Vector chr = ka.getChromosomes();
                for (int j = 0; j < chr.size(); j++) {
                    Chromosome c = (Chromosome) chr.elementAt(j);
                    logger.info("Chr " + c.getDisplayId() + " " + c.getPlength() + " " + c.getQlength() + " " + c.getCentroLength());
                    Vector bands = c.getBands();
                    for (int k = 0; k < bands.size(); k++) {
                        ChromosomeBand band = (ChromosomeBand) bands.elementAt(k);
                        logger.info("Band " + band.getDisplayId() + " " + band.getChrStart() + " " + band.getChrEnd() + " " + band.getStain());
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
