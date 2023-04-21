package edu.ucdavis.genomics.metabolomics.binbase.connector.references.pubchem;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import edu.ucdavis.genomics.metabolomics.binbase.connector.references.ContentResolver;
import edu.ucdavis.genomics.metabolomics.binbase.connector.references.exception.ResolverException;

/**
 * a content resolver which is based on the pubchem service
 * 
 * @author wohlgemuth
 */
public class PubchemContentResolver extends ContentResolver {

    /**
	 * constructor which generates the connection to the pubchem service
	 */
    public PubchemContentResolver() {
    }

    public String getInchiHashKey() {
        return (String) getMap().get("InChIKey___Standard");
    }

    public String getInchiKey() {
        return (String) getMap().get("InChI___Standard");
    }

    public double getMolareMass() {
        return Double.parseDouble((String) getMap().get("Molecular Weight"));
    }

    public double getExactMass() {
        return Double.parseDouble((String) getMap().get("Mass___Exact"));
    }

    public String getFingerprint() {
        return (String) getMap().get("Fingerprint___SubStructure Keys");
    }

    public double getMonoIsotopicMass() {
        return Double.parseDouble((String) getMap().get("Weight___MonoIsotopic"));
    }

    public String getMolecularFormula() {
        return (String) getMap().get("Molecular Formula");
    }

    ;

    public String getIsomericSmiles() {
        return (String) getMap().get("SMILES___Isomeric");
    }

    public double getLogP() {
        return Double.parseDouble((String) getMap().get("Log P___XLogP3"));
    }

    public String getCanonicalSmile() {
        return (String) getMap().get("SMILES___Canonical");
    }

    public String getIUPACName() {
        return (String) this.getMap().get("IUPAC Name___Preferred");
    }

    public Map<String, Object> generateMap(Object id) throws ResolverException {
        try {
            PubchemFileParser parser = new PubchemFileParser();
            if (id instanceof File) {
                return parser.parseInputStream(new FileInputStream((File) id));
            } else if (id instanceof InputStream) {
                return parser.parseInputStream((InputStream) id);
            }
        } catch (Exception e) {
            throw new ResolverException(e);
        }
        throw new ResolverException("nothing found for the id...");
    }
}
