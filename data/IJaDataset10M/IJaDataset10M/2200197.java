package org.liris.schemerger.ui.actions.chronicle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.liris.schemerger.core.AbortException;
import org.liris.schemerger.core.dataset.SimSequence;
import org.liris.schemerger.core.event.ISimEvent;
import org.liris.schemerger.core.instance.ChrOccSet;
import org.liris.schemerger.core.pattern.IChronicle;
import org.liris.schemerger.core.pattern.ITypeDec;
import org.liris.schemerger.ui.OccurrenceAdapter;
import org.liris.schemerger.ui.SchEmergerPlugin;
import org.liris.schemerger.ui.observablemodel.ObservableChronicle;

public class CountChronicleOccurrenceAction extends ChronicleAction {

    public CountChronicleOccurrenceAction(ObservableChronicle<?> chronicle) {
        super(chronicle);
        setText("Count");
        setToolTipText("Finds the occurrences of the chronicle in the active sequence");
        setImageDescriptor(SchEmergerPlugin.getImageDescriptor("icons/history_list.gif"));
        SchEmergerPlugin.getDefault().addPropertyChangeListener(SchEmergerPlugin.PROP_ACTIVE_SEQUENCE, new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                refreshEnablement(null);
            }
        });
    }

    private SimSequence<ISimEvent> sequence;

    @Override
    protected boolean shouldEnabled(IStructuredSelection selection) {
        sequence = (SimSequence<ISimEvent>) SchEmergerPlugin.getDefault().getActiveSequence();
        return SchEmergerPlugin.getDefault().getSequenceView() != null && SchEmergerPlugin.getDefault().getActiveSequence() != null && SchEmergerPlugin.getDefault().getOccurrenceAdapter() != null && SchEmergerPlugin.getDefault().getOccurrencesView() != null;
    }

    @Override
    public void run() {
        ChrOccSet<ISimEvent> occSet;
        try {
            OccurrenceAdapter occurrenceAdapter = SchEmergerPlugin.getDefault().getOccurrenceAdapter();
            occSet = (ChrOccSet<ISimEvent>) occurrenceAdapter.get(sequence, (IChronicle<ITypeDec>) chronicle);
            SchEmergerPlugin.getDefault().getOccurrencesView().setOccurrenceSet(occSet);
        } catch (AbortException e) {
            e.printStackTrace();
        }
    }
}
