package org.gwtoolbox.widget.client.panel.layout;

import org.gwtoolbox.widget.client.panel.layout.animation.AnimationCallbackAdapter;

/**
 * @author Uri Boness
 */
public abstract class DelegatingCompletionCallback extends CompletionCallback {

    private final CompletionCallback delegee;

    protected DelegatingCompletionCallback(CompletionCallback delegee) {
        this.delegee = delegee;
    }

    public final void onComplete() {
        doOnComplete();
        delegee.onComplete();
    }

    public abstract void doOnComplete();
}
