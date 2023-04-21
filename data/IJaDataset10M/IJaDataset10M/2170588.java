package net.sourceforge.javautil.developer.enterprise.jboss;

import net.sourceforge.javautil.common.io.VirtualArtifact;
import org.jboss.shrinkwrap.api.Archive;

/**
 * This allows declaration of a type of artifact that is also an archive.
 *
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public interface JBossArchiveArtifact<T extends Archive<T>> extends Archive<T>, VirtualArtifact {
}
