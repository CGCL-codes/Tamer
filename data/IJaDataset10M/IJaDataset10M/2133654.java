package it.tukano.jps.uicomponents;

import java.util.EventObject;

/**
 * Event performed by a DataEditor
 */
public class DataEditorEvent<T> extends EventObject {

    private final DataEditor<T> SOURCE;

    /**
     * Initializes this editor event
     * @param source the editor that generated this event
     */
    public DataEditorEvent(DataEditor<T> source) {
        super(source);
        SOURCE = source;
    }

    /**
     * Returns the source of this event
     * @return
     */
    @Override
    public DataEditor<T> getSource() {
        return SOURCE;
    }
}
