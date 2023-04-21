package it.univaq.di.chameleonserver.abstractanalyzer.computazioni;

import it.univaq.di.chameleonserver.abstractanalyzer.risorse.ResourceProfile;
import it.univaq.di.chameleonserver.abstractanalyzer.risorse.ResourceSet;
import it.univaq.di.chameleonserver.abstractanalyzer.risorse.ResourcesDefinition;
import java.util.Iterator;
import java.util.Map.Entry;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * <pre>
 * Computazione rappresenta una computazione all'interno del codice in analisi. Per computazione si intende una parte sequenziale di codice.
 * 
 * Tiene traccia delle occorrenze dei pattern definiti dell'instruction profile e delle risorse annotate dall'utente.
 * </pre>
 *
 * @author <a href="mailto:cristian.dipanfilo@gmail.com">Cristian Di Panfilo</a>
 */
public class Computazione {

    /**
	 * Tiene traccia delle occorrenze dei pattern definiti nell'instruction profile
	 *
	 * @see OccorrenzeStatements
	 */
    private OccorrenzeStatements os;

    /**
	 * Tiene traccia delle risorse annotate dall'utente
	 *
	 * @see ara1.risorse.ResourceSet
	 */
    private ResourceSet rs;

    /**
	 * E' il nome della computazione. Ogni computazione ha un nome univoco.
	 */
    private String nome;

    /**
	 * Crea un'istanza della classe Computazione.
	 *
	 * @param resProfile Resource Profile utilizzato per caricare i pattern delle istruzioni
	 * @param resDef la definizione delle risorse
	 * @param nome il nome della computazione
	 *
	 * @see OccorrenzeStatements
	 * @see framework.abstractresanalyzer.risorse.ResourceSet
	 * @see framework.abstractresanalyzer.risorse.ResourcesDefinition
	 */
    public Computazione(ResourceProfile resProfile, ResourcesDefinition resDef, String nome) {
        this.nome = nome;
        os = new OccorrenzeStatements(resProfile);
        rs = new ResourceSet(resDef);
    }

    /**
	 * Crea un'istanza della classe Computazione speculare all'istanza passata come argomento 
	 *
	 * @param c un'istanza di una Computazione
	 *
	 * @see OccorrenzeStatements
	 * @see framework.abstractresanalyzer.risorse.ResourceSet
	 */
    public Computazione(Computazione c) {
        nome = c.getNome();
        os = new OccorrenzeStatements(c.getOccorrenzeStatements());
        rs = new ResourceSet(c.getResourceSet());
    }

    /**
	 * Crea un'istanza della classe Computazione.
	 *
	 * @param resDef la definizione delle risorse
	 * @param nome il nome della computazione
	 *
	 * @see OccorrenzeStatements
	 * @see framework.abstractresanalyzer.risorse.ResourceSet
	 * @see framework.abstractresanalyzer.risorse.ResourcesDefinition
	 */
    public Computazione(String nome, ResourcesDefinition resDef) {
        this.nome = nome;
        os = new OccorrenzeStatements();
        rs = new ResourceSet(resDef);
    }

    /**
	 * Imposta le occorrenze dei pattern all'interno della computazione
	 *
	 * @param l'oggetto OccorrenzeStatements
	 * @see OccorrenzeStatements
	 */
    public void setOccorrenze(OccorrenzeStatements os) {
        this.os = os;
    }

    /**
	 * Restituisce il nome della computazione
	 *
	 * @return il nome della computazione
	 */
    public String getNome() {
        return nome;
    }

    /**
	 * Imposta il nome della computazione
	 * @param nome il nome della computazione
	 */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
	 * Restituisce le occorrenze dei pattern all'interno della computazione
	 *
	 * @return l'oggetto OccorrenzeStatements
	 * @see OccorrenzeStatements
	 */
    public OccorrenzeStatements getOccorrenzeStatements() {
        return os;
    }

    /**
	 * Restituisce le risorse annotate dal programmatore
	 *
	 * @return l'oggetto ResourceSet
	 * @see framework.abstractresanalyzer.risorse.ResourceSet
	 */
    public ResourceSet getResourceSet() {
        return rs;
    }

    /**
	 * Aggiunge un occorrenza ai pattern che fanno match con lo statement
	 *
	 * @param statement un'istruzione bytecode 
	 * @see OccorrenzeStatements#registraOccorrenzaStatement
	 */
    public void aggiungiOccorrenzaStatement(String statement) {
        os.registraOccorrenzaStatement(statement);
    }

    /**
	 * Restituisce le occorrenze di un pattern
	 *
	 * @return l'occorrenza del pattern passato come argomento
	 */
    public String getNumeroOccorrenze(String pattern) {
        return os.getNumeroOccorrenzePattern(pattern);
    }

    /**
	 * Aggiunge un resource set alle risorse della computazione
	 *
	 * @see ara1.risorse.ResourceSet#sommaResourceSet
	 */
    public void aggiungiResourceSet(String resourceSet, ResourcesDefinition resourcesDef) {
        ResourceSet resSet = new ResourceSet(resourceSet, resourcesDef, true);
        rs.sommaResourceSet(resSet);
    }

    /**
	 * Accoda alla computazione corrente la computazione c.
	 * Ai nostri fini l'accodamento di due computazioni, e quindi di due parti sequenziali di codice,
	 * equivale alla somma delle occorrenze degli statement e alla somma delle risorse annotate
	 *
	 * @param c la computazione da accodare
	 * @param separator il separatore nel path delle computazioni
	 * @return la computazione corrente alla quale � stata accodata un'altra computazione
	 */
    public Computazione accodaComputazione(Computazione c, String separator) {
        os.sommaOccorrenzeStatements(c.getOccorrenzeStatements());
        rs.sommaResourceSet(c.getResourceSet());
        if (c.getNome().length() > 0) {
            nome = nome + separator + c.getNome();
        }
        return this;
    }

    /**
	 * Ripete per n volte la computazione corrente.
	 * Consiste nel moltiplicare tutte le occorrenze statement e le risorse annotate per un valore n
	 *
	 * @param n il fattore moltiplicativo
	 *
	 * @see OccorrenzeStatements#moltiplicaOccorrenzeStatements
	 * @see framework.abstractresanalyzer.risorse.ResourceSet#moltiplica
	 */
    public void ripetiComputazione(String n) {
        os.moltiplicaOccorrenzeStatements(n);
        rs.moltiplica(n);
    }

    /**
	 * Restituisce l'elemento dom corrispondente all'instruction set della computazione.
	 * Questo � un metodo utilizzato dall'analizzatore per il logging.
	 * Il dom document passato come argomento � utilizzato per mantenere in memoria l'albero xml del file di log.
	 * Ogni nuovo elemento da aggiungere al file di log, verr� creato *sempre* tramite questo dom document
	 *
	 * @param loggingDOM il dom document utilizzato per memorizzare la struttura xml del file di log
	 * @return l'elemento dom corrispondente all'instruction set della computazione
	 */
    public Element getInstructionSetElement(Document loggingDOM) {
        Element instructionSet = loggingDOM.createElement("instructionSet");
        Iterator<Entry<String, String>> occorrenze = os.iterator();
        Entry<String, String> occorrenzaCorrente;
        String patternCorrente;
        String numeroDiMatch;
        while (occorrenze.hasNext()) {
            occorrenzaCorrente = occorrenze.next();
            patternCorrente = occorrenzaCorrente.getKey();
            numeroDiMatch = occorrenzaCorrente.getValue();
            Element occorrenza = loggingDOM.createElement("occurrence");
            occorrenza.setAttribute("pattern", patternCorrente);
            occorrenza.setAttribute("match", numeroDiMatch);
            instructionSet.appendChild(occorrenza);
        }
        return instructionSet;
    }

    /**
	 * Restituisce l'elemento dom corrispondente al resource set della computazione.
	 * Questo � un metodo utilizzato dall'analizzatore per il logging.
	 * Il dom document passato come argomento � utilizzato per mantenere in memoria l'albero xml del file di log.
	 * Ogni nuovo elemento da aggiungere al file di log, verr� creato *sempre* tramite questo dom document
	 *
	 * @param loggingDOM il dom document utilizzato per memorizzare la struttura xml del file di log
	 * @return l'elemento dom corrispondente al resource set della computazione
	 */
    public Element getResourceSetElement(Document loggingDOM) {
        Element resourceSet = loggingDOM.createElement("resourceSet");
        Iterator<Entry<String, String>> rsIterator = rs.entryIterator();
        Entry<String, String> risorsaCorrente;
        while (rsIterator.hasNext()) {
            risorsaCorrente = rsIterator.next();
            Element resource = loggingDOM.createElement("resource");
            resource.setAttribute("id", risorsaCorrente.getKey());
            String tipo = ResourcesDefinition.getRisorsa(risorsaCorrente.getKey()).getTipo().toString();
            resource.setAttribute("type", tipo);
            String valore = risorsaCorrente.getValue();
            resource.setAttribute("value", valore);
            resourceSet.appendChild(resource);
        }
        return resourceSet;
    }

    /**
	 * Ridefinisce il metodo equals in quanto gli oggetti Computazione andranno a far parte di insiemi di tipo HashSet
	 * i quali non prevedono l'esistenza di due elementi uguali.
	 * Quindi tale metodo verifica se due oggetti Computazione sono uguali o no.
	 *
	 * @param obj l'oggetto da sottoporre alla verifica di uguaglianza
	 * @return true se l'oggetto this � uguale all'oggetto obj, false altrimenti
	 */
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Computazione other = (Computazione) obj;
        boolean equalCs = false;
        boolean equalOs = false;
        if (rs == null) {
            if (other.rs != null) return false;
        } else {
            equalCs = rs.equals(other.getResourceSet());
        }
        if (os == null) {
            if (other.os != null) {
                return false;
            }
        } else {
            equalOs = os.equals(other.getOccorrenzeStatements());
        }
        return (equalCs & equalOs);
    }
}
