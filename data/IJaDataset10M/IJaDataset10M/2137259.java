package de.uniwue.tm.cev.extension;

import org.apache.uima.cas.CAS;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.ui.PartInitException;
import de.uniwue.tm.cev.data.CEVData;
import de.uniwue.tm.cev.editor.CEVViewer;

public interface ICEVArtifactViewerFactory {

    ICEVArtifactViewer createArtifactViewer(CEVViewer viewer, CTabItem tabItem, CEVData casData) throws PartInitException;

    void setPriority(int priority);

    int getPriority();

    boolean isAble(CAS cas);
}
