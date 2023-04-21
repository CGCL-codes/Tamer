package snipsnap.api.label;

import java.util.Set;

/**
 * @author Matthias L. Jugel <matthias.jugel@first.fraunhofer.de>
 * @version $Id$
 */
public interface LabelManager {

    Label getLabel(String type);

    Label getDefaultLabel();

    Set getTypes();
}
