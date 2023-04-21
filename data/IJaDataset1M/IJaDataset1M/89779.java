package cz.cuni.mff.ksi.jinfer.base.interfaces.inference;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import java.util.List;

/**
 * Interface defining a response to IGGenerator finishing its work.
 *
 * @author vektor
 */
public interface IGGeneratorCallback {

    /**
   * This method is called by a IGGenerator implementation, after it has
   * finished its work.
   *
   * @param grammar Initial grammar as retrieved from input files.
   */
    void finished(final List<Element> grammar);
}
