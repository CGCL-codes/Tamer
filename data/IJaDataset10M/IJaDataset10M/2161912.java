package net.sourceforge.akrogen.eclipse.wizard;

import java.util.Collection;
import net.sourceforge.akrogen.core.component.AkrogenComponent;
import net.sourceforge.akrogen.core.component.AkrogenComponents;

public interface IAkrogenManager {

    public Collection getAkrogenComponentsList();

    public AkrogenComponents getAkrogenComponents(String namespace);

    public AkrogenComponent getAkrogenComponent(String name, String namespace);
}
