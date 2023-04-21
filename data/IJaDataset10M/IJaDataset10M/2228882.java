package org.waveprotocol.wave.client.wavepanel.view.dom;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import org.waveprotocol.wave.client.wavepanel.view.InlineThreadView;
import org.waveprotocol.wave.client.wavepanel.view.IntrinsicContinuationIndicatorView;

/**
 * Dom impl of an inline continuation indicator.
 */
public final class ContinuationIndicatorDomImpl implements DomView, IntrinsicContinuationIndicatorView {

    /**
   * Handles structural queries on menu-item views.
   *
   * @param <I> intrinsic indicator implementation
   */
    public interface Helper<I> {

        InlineThreadView getParent(I impl);
    }

    /** The DOM element of this view. */
    private final Element self;

    /** The HTML id of {@code self}. */
    private final String id;

    ContinuationIndicatorDomImpl(Element e, String id) {
        this.self = e;
        this.id = id;
    }

    public static ContinuationIndicatorDomImpl of(Element e) {
        return new ContinuationIndicatorDomImpl(e, e.getId());
    }

    @Override
    public Element getElement() {
        return self;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return DomViewHelper.equals(this, obj);
    }

    @Override
    public int hashCode() {
        return DomViewHelper.hashCode(this);
    }

    @Override
    public void enable() {
        self.getStyle().clearDisplay();
    }

    @Override
    public void disable() {
        self.getStyle().setDisplay(Display.NONE);
    }

    public void remove() {
        getElement().removeFromParent();
    }
}
