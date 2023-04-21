package net.sourceforge.javautil.classloader.resolver;

import net.sourceforge.javautil.common.pkg.IPackageArtifactReference;
import net.sourceforge.javautil.common.xml.annotation.XmlTag;
import net.sourceforge.javautil.common.xml.annotation.XmlTag.ElementType;

/**
 * A reference to a class artifact, which does not include a version.
 *
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
@XmlTag(elementType = ElementType.Simple)
public interface IClassArtifactReference extends IPackageArtifactReference<String>, Comparable<IClassArtifactReference> {

    /**
	 * @return The group this dependency belongs to.
	 */
    String getGroupId();

    /**
	 * @return The group unique id of this dependency.
	 */
    String getArtifactId();

    /**
	 * @return A string that is unique for all artifacts of this type without version number
	 */
    String toArtifactString();
}
