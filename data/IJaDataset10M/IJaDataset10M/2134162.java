package org.fudaa.fudaa.tr.post.persist;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import org.fudaa.ctulu.CtuluLibFile;
import org.fudaa.fudaa.tr.post.TrPostSource;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Couple path source/ID qui sera persist� en xml. Permet de faire le lien entre l'id de la source et la source en elee
 * meme pour le reste du projet persistant.
 * 
 * @author Adrien Hadoux
 */
public class TrPostSourcePersist {

    @XStreamAlias("warning")
    String warning = "ID unique";

    String IdSource;

    boolean rechargerLesLiens = false;

    String[] pathRelatifSource;

    String[] pathSource;

    public TrPostSourcePersist() {
    }

    /**
   * Constructeur utilis� pour la cr�ation de la donn�e persistante.
   * 
   * @param source
   */
    public TrPostSourcePersist(TrPostSource source, File projectDirectory) {
        fillDataWithSource(source, projectDirectory);
    }

    public void fillDataWithSource(TrPostSource source, File projectDirectory) {
        IdSource = source.getId();
        Collection<File> files = source.getFiles();
        pathSource = new String[files.size()];
        pathRelatifSource = new String[files.size()];
        int idx = 0;
        for (Iterator iterator = files.iterator(); iterator.hasNext(); ) {
            File file = (File) iterator.next();
            pathSource[idx] = file.getAbsolutePath();
            File fileFromStart = new File(projectDirectory.getAbsolutePath() + File.separator + TrPostPersistenceManager.setupProject);
            pathRelatifSource[idx] = CtuluLibFile.getRelativePathnameTo(fileFromStart, file).getPath();
            idx++;
        }
    }

    public void fillSourceWithData(TrPostSource source) {
        source.setId(IdSource);
    }
}
