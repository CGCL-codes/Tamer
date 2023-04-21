package opennlp.tools.sentdetect;

/**
 * Interface for {@link SentenceDetectorME} context generators.
 */
public interface SDContextGenerator {

    /**
   * Returns an array of contextual features for the potential sentence boundary at the
   * specified position within the specified string buffer. 
   * 
   * @param sb The string buffer for which sentences are being determined.
   * @param position An index into the specified string buffer when a sentence boundary may occur.
   * 
   * @return an array of contextual features for the potential sentence boundary at the
   * specified position within the specified string buffer.
   */
    public abstract String[] getContext(StringBuffer sb, int position);
}
