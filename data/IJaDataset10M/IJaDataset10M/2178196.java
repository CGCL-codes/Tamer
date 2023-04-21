package org.fudaa.fudaa.tr.post;

import org.fudaa.ctulu.ProgressionInterface;

/**
 * @author fred deniger
 * @version $Id: TrPostInspectorReader.java,v 1.2 2006-09-19 15:07:27 deniger Exp $
 */
public interface TrPostInspectorReader {

    /**
   * @return true si la lecture a donne quelque chose. Si true la maj de l'interface peut etre effectuee
   */
    int read();

    boolean isPostActivated();

    boolean isPostActivating();

    TrPostProjet getPostActivatedProject();

    void setImpl(TrPostCommonImplementation _impl);

    void close();

    void setProgression(ProgressionInterface _prog);
}
