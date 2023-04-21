package clubmixerlibraryeditor.handler;

import java.util.EventListener;

/**
 *
 * @author Alexander Schindler
 */
public interface DirTreeEventListener extends EventListener {

    public void dirTreeSelectionChanged(DirTreeEvent event);
}
