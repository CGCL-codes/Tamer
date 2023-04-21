package org.rubato.composer.denobrowser;

import java.util.EventListener;
import org.rubato.math.yoneda.Denotator;

/**
 * @author Gérard Milmeister
 */
public interface ListViewListener extends EventListener {

    public void doubleClicked(int level, Denotator d);

    public void valueChanged(int level, Denotator d);

    public void addNew(int level, Denotator d);
}
