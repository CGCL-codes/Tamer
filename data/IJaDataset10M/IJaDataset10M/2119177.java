package org.waveprotocol.wave.client.wavepanel.view.dom;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ImageElement;
import org.waveprotocol.wave.client.wavepanel.view.IntrinsicParticipantView;

/**
 * DOM implementation of a participant.
 *
 */
public final class ParticipantAvatarDomImpl implements DomView, IntrinsicParticipantView {

    private final ImageElement self;

    ParticipantAvatarDomImpl(Element self) {
        this.self = self.cast();
    }

    static ParticipantAvatarDomImpl of(Element e) {
        return new ParticipantAvatarDomImpl(e);
    }

    @Override
    public void setAvatar(String url) {
        self.setSrc(url);
    }

    @Override
    public void setName(String name) {
        self.setTitle(name);
        self.setAlt(name);
    }

    void remove() {
        self.removeFromParent();
    }

    @Override
    public Element getElement() {
        return self;
    }

    @Override
    public String getId() {
        return self.getId();
    }

    @Override
    public boolean equals(Object obj) {
        return DomViewHelper.equals(this, obj);
    }

    @Override
    public int hashCode() {
        return DomViewHelper.hashCode(this);
    }
}
