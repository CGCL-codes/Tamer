package musite;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Jianjiong Gao
 */
public class ProteinsImpl implements Proteins {

    protected final Map<String, Protein> mapAccessionProtein;

    public ProteinsImpl() {
        this(null);
    }

    public ProteinsImpl(Proteins proteins) {
        this(proteins, false, (Set) null);
    }

    public ProteinsImpl(Proteins proteins, boolean deepCopy, Set<String> fields) {
        mapAccessionProtein = new LinkedHashMap<String, Protein>();
        addAll(proteins, deepCopy, fields, ConfictHandleOption.RENAME);
    }

    /**
     * {@inheritDoc}
     */
    public int proteinCount() {
        return mapAccessionProtein.size();
    }

    /**
     * {@inheritDoc}
     */
    public Collection<Protein> proteins() {
        return Collections.unmodifiableCollection(mapAccessionProtein.values());
    }

    /**
     * {@inheritDoc}
     */
    public void addAll(Proteins proteins, boolean deepCopy, Set<String> fields, ConfictHandleOption option) {
        if (proteins != null) {
            Iterator<Protein> it = proteins.proteinIterator();
            while (it.hasNext()) {
                Protein protein = it.next();
                if (deepCopy) addProtein(new ProteinImpl(protein, fields), option); else addProtein(protein, option);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isProteinContained(final String proteinAccession) {
        if (proteinAccession == null) {
            throw new NullPointerException("proteinAccession cannot be null");
        }
        return mapAccessionProtein.containsKey(proteinAccession);
    }

    /**
     * {@inheritDoc}
     */
    public Set<String> getProteinsAccessions() {
        return Collections.unmodifiableSet(mapAccessionProtein.keySet());
    }

    /**
     * {@inheritDoc}
     */
    public Iterator proteinIterator() {
        return mapAccessionProtein.values().iterator();
    }

    /**
     * {@inheritDoc}
     */
    public Protein getProtein(final String proteinAccession) {
        if (proteinAccession == null) {
            throw new NullPointerException("proteinAccession cannot be null");
        }
        return mapAccessionProtein.get(proteinAccession);
    }

    /**
     * {@inheritDoc}
     */
    public void addProtein(Protein protein) {
        addProtein(protein, ConfictHandleOption.RENAME);
    }

    /**
     * {@inheritDoc}
     */
    public boolean addProtein(Protein protein, ConfictHandleOption option) {
        if (protein == null) {
            throw new NullPointerException();
        }
        String proteinAccession = protein.getAccession();
        if (proteinAccession == null) {
            throw new IllegalArgumentException();
        }
        if (!mapAccessionProtein.containsKey(proteinAccession)) {
            mapAccessionProtein.put(proteinAccession, protein);
        } else {
            if (option == ConfictHandleOption.SKIP || protein == mapAccessionProtein.get(proteinAccession)) {
                return false;
            } else if (option == ConfictHandleOption.OVERWRITE) {
                mapAccessionProtein.put(proteinAccession, protein);
            } else {
                int i = 1;
                while (mapAccessionProtein.containsKey(proteinAccession + "." + i)) {
                    i++;
                }
                proteinAccession += "." + i;
                protein.setAccession(proteinAccession);
                mapAccessionProtein.put(proteinAccession, protein);
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public boolean removeProtein(final String proteinAccession) {
        if (proteinAccession == null) {
            throw new NullPointerException("proteinAccession cannot be null");
        }
        Protein old = mapAccessionProtein.remove(proteinAccession);
        return old != null;
    }

    /**
     * {@inheritDoc}
     */
    public void retainProteins(final Set<String> accessions) {
        if (accessions == null) {
            throw new NullPointerException();
        }
        Iterator<Protein> it = proteinIterator();
        while (it.hasNext()) {
            String acc = it.next().getAccession();
            if (!accessions.contains(acc)) it.remove();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void removeProteins(final Set<String> accessions) {
        if (accessions == null) {
            throw new NullPointerException();
        }
        Iterator<Protein> it = proteinIterator();
        while (it.hasNext()) {
            String acc = it.next().getAccession();
            if (accessions.contains(acc)) it.remove();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void travelProteins(final ProteinVisitor visitor) {
        if (visitor == null) throw new NullPointerException();
        Iterator<Protein> it = proteinIterator();
        while (it.hasNext()) {
            visitor.visit(it.next());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void filterProteins(final ProteinFilter filter) {
        Set<String> accs = new HashSet<String>(getProteinsAccessions());
        for (String acc : accs) {
            if (!filter.filter(getProtein(acc))) {
                removeProtein(acc);
            }
        }
    }
}
