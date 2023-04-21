package org.jsresources.apps.esemiso.data;

import org.jsresources.apps.jmvp.model.ModelEvent;

/**	Description of a change in the tempo of a RhythmModel.
 *
 * @see RhythmModel
 * @see RhythmModel.notifyTempoChange
 * @see RhythmModel.setTempo
 *
 * @author Matthias Pfisterer
 */
public class TempoChangeEvent extends ModelEvent {

    private int m_nTempo;

    /** Constructor.
	 *
	 * @param model the RhythmModel that changed
	 * @param nTempo the new tempo
	 *
	 * @see #getTempo
	 */
    public TempoChangeEvent(RhythmModel model, int nTempo) {
        super(model);
        m_nTempo = nTempo;
    }

    /** Returns the new tempo.
	 */
    public int getTempo() {
        return m_nTempo;
    }

    public String toString() {
        return super.toString() + "[(" + getTempo() + ")]";
    }
}
