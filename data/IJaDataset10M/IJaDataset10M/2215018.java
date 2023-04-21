package net.sourceforge.ivi.waveview.ui.display;

import net.sourceforge.ivi.waveview.ui.IWaveDisplay;
import net.sourceforge.ivi.waveview.ui.IWaveTraceCursor;
import net.sourceforge.ivi.waveview.ui.editor.WaveViewEditor;
import net.sourceforge.ivi.waveview.ui.format.IFormatEntity;
import net.sourceforge.ivi.waveview.ui.internal.format.CursorFormatWrapper;

public class iviWaveTraceCursor implements IWaveTraceCursor {

    /****************************************************************
	 * iviWaveTraceCursor() 
	 ****************************************************************/
    public iviWaveTraceCursor(WaveViewEditor editor, IFormatEntity cursor_config) {
        d_editor = editor;
        d_cursorData = cursor_config;
        d_cursorDataWrapper = new CursorFormatWrapper(d_cursorData);
        d_time = d_cursorDataWrapper.getTime();
    }

    /**
	 * Returns the format data belonging to this cursor
	 */
    public IFormatEntity getConfigData() {
        return d_cursorData;
    }

    public void setTime(long time) {
        if (d_time != time) {
            d_cursorDataWrapper.setTime(time);
            d_editor.setDirty();
        }
        d_time = time;
    }

    public long getTime() {
        return d_time;
    }

    public void setIdx(int idx) {
        d_cursorIdx = idx;
    }

    public int getIdx() {
        return d_cursorIdx;
    }

    public void setSelected(boolean sel) {
        d_selected = sel;
    }

    public boolean getSelected() {
        return d_selected;
    }

    /****************************************************************
	 * Private Data 
	 ****************************************************************/
    private WaveViewEditor d_editor;

    private long d_time;

    private IFormatEntity d_cursorData;

    private CursorFormatWrapper d_cursorDataWrapper;

    private int d_cursorIdx;

    private boolean d_selected;
}
