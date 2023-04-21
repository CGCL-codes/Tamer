package gate;

import java.util.List;
import gate.event.CorpusListener;

/** Corpora are lists of Document. TIPSTER equivalent: Collection.
  */
public interface Corpus extends SimpleCorpus {

    /**
   * Unloads the document from memory. Only needed if memory
   * preservation is an issue. Only supported for Corpus which is
   * stored in a Datastore. To get this document back in memory,
   * use get() on Corpus or if you have its persistent ID, request it
   * from the Factory.
   * <P>
   * Transient Corpus objects do nothing,
   * because there would be no way to get the document back
   * again afterwards.
   * @param doc Document to be unloaded from memory.
   */
    public void unloadDocument(Document doc);

    /**
   * This method returns true when the document is already loaded in memory.
   * The transient corpora will always return true as they can only contain
   * documents that are present in the memory.
   */
    public boolean isDocumentLoaded(int index);

    /**
   * Removes one of the listeners registered with this corpus.
   * @param l the listener to be removed.
   */
    public void removeCorpusListener(CorpusListener l);

    /**
   * Registers a new {@link CorpusListener} with this corpus.
   * @param l the listener to be added.
   */
    public void addCorpusListener(CorpusListener l);
}
