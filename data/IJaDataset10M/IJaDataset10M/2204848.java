package ca.petersens.gwt.databinding.client.ui;

import ca.petersens.gwt.databinding.client.Editor;
import ca.petersens.gwt.databinding.client.EditorChangeEvent;
import ca.petersens.gwt.databinding.client.EditorChangeListener;
import ca.petersens.gwt.databinding.client.converter.ConversionException;
import ca.petersens.gwt.databinding.client.converter.StringConverter;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.Widget;
import java.util.ArrayList;
import java.util.List;

public class HasTextEditor<T, W extends Widget & HasText & SourcesChangeEvents> extends HasTextViewer<T, W> implements Editor<T> {

    private static final class NativeListener implements ChangeListener {

        public void onChange(Widget sender) {
            ((HasTextEditor<?, ?>) sender.getParent()).widgetChanged();
        }
    }

    private static final NativeListener nativeListener = new NativeListener();

    private final StringConverter<T> converter;

    private List<EditorChangeListener<? super T>> listeners;

    private T value;

    public HasTextEditor(StringConverter<T> converter, W widget) {
        super(converter, widget);
        this.converter = converter;
        widget.addChangeListener(nativeListener);
    }

    public final void addChangeListener(EditorChangeListener<? super T> listener) {
        if (listeners == null) {
            listeners = new ArrayList<EditorChangeListener<? super T>>();
        }
        listeners.add(listener);
    }

    public final T readValue() {
        return value;
    }

    public final void removeChangeListener(EditorChangeListener<? super T> listener) {
        if (listeners == null) {
            return;
        }
        listeners.remove(listener);
    }

    private void widgetChanged() {
        try {
            value = doRead();
            if (listeners != null) {
                EditorChangeEvent<T> event = new EditorChangeEvent<T>(this);
                for (EditorChangeListener<? super T> listener : listeners) {
                    listener.valueChanged(event);
                }
            }
        } catch (ConversionException e) {
        }
        doDisplay(value);
    }

    protected final T doRead() throws ConversionException {
        return converter.fromString(getWidget().getText());
    }

    @Override
    protected final void onDisplay(T value) {
        this.value = value;
    }
}
