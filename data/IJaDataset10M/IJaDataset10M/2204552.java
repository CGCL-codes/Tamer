package uk.co.ordnancesurvey.rabbitparser.disambiguator.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.ordnancesurvey.rabbitparser.disambiguator.IAmbiguousPart;
import uk.co.ordnancesurvey.rabbitparser.disambiguator.IRabbitParsedPartDisambiguator;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.comparator.ParsedPartBySpanComparator;

/**
 * Provides basic functionality for {@link IRabbitParsedPartDisambiguator}s.
 * This class provides a standard implementation of the
 * {@link #disambiguate(List)} method of {@link IRabbitParsedPartDisambiguator}.
 */
public abstract class BasePartDisambiguator<PARTTYPE extends IAmbiguousPart> implements IRabbitParsedPartDisambiguator<PARTTYPE> {

    private static final Logger log = Logger.getLogger(BasePartDisambiguator.class.getName());

    private static final Level logLevel = Level.FINE;

    private final ParsedPartBySpanComparator partComparator = new ParsedPartBySpanComparator();

    private final List<PARTTYPE> sourceList = new ArrayList<PARTTYPE>();

    private final Class<PARTTYPE> partInterface;

    public BasePartDisambiguator(Class<PARTTYPE> aClass) {
        assert aClass != null;
        partInterface = aClass;
    }

    /**
	 * Copies the contents of inputList to the {@link #sourceList}, which is
	 * available to subclasses through method {@link #getSourceList()}.
	 * Furthermore, the sourcelist is sorted by begin index.
	 * 
	 * Invokes the {@link #preProcessSourcePSList()} and then invokes the
	 * {@link #performDisambiguation(List)}.
	 * 
	 * @see uk.co.ordnancesurvey.rabbitparser.disambiguator.IRabbitParsedPartDisambiguator#disambiguate(java.util.List)
	 */
    public final List<PARTTYPE> disambiguate(List<PARTTYPE> inputList) {
        log.log(logLevel, "Preprocess Disambiguate " + inputList.size() + " " + partInterface.getSimpleName());
        setAmbiguousSentences(inputList);
        final List<PARTTYPE> result = new ArrayList<PARTTYPE>();
        log.log(logLevel, "Disambiguate " + inputList.size() + " " + partInterface.getSimpleName());
        performDisambiguation(result);
        log.log(logLevel, "Done disambiguate " + inputList.size() + " " + partInterface.getSimpleName());
        return result;
    }

    /**
	 * Subclasses override this method to perform the actual disambiguation by
	 * processing {@link #getSourceList()} and filling aResult with the
	 * disambiguated list.
	 * 
	 * @param result
	 */
    protected abstract void performDisambiguation(List<PARTTYPE> result);

    /**
	 * Initialises the {@link #sourceList} with the parsed parts in
	 * ambiguousSentences. This process includes the sorting by span performed
	 * by the {@link #partComparator} and pre-processing as defined by
	 * {@link #preProcessSourcePSList()}.
	 * 
	 * @param ambiguousSentences
	 */
    private void setAmbiguousSentences(List<PARTTYPE> ambiguousSentences) {
        sourceList.clear();
        sourceList.addAll(ambiguousSentences);
        Collections.sort(sourceList, partComparator);
        preProcessSourcePSList();
    }

    protected final List<PARTTYPE> getSourceList() {
        return sourceList;
    }

    /**
	 * Subclasses are required to implement this method to validate the source
	 * list to be disambiguated. Most disambiguators need to impose certain
	 * restrictions on the source list of sentences. This method should throw a
	 * runtime exception when the incoming list is not valid.
	 */
    protected abstract void preProcessSourcePSList();

    protected final Class<PARTTYPE> getPartInterface() {
        return partInterface;
    }
}
