package org.eclipse.jface.contentassist;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.IEventConsumer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;

/**
 * An <code>AbstractControlContentAssistSubjectAdapter</code> delegates assistance requests from a
 * {@linkplain org.eclipse.jface.text.contentassist.ContentAssistant content assistant}
 * to a <code>Control</code>.
 *
 * A visual feedback can be configured via {@link #setContentAssistCueProvider(ILabelProvider)}.
 *
 * @since 3.0
 * @deprecated As of 3.2, replaced by Platform UI's field assist support
 */
public abstract class AbstractControlContentAssistSubjectAdapter implements IContentAssistSubjectControl {

    protected static final boolean DEBUG = "true".equalsIgnoreCase(Platform.getDebugOption("org.eclipse.jface.text/debug/ContentAssistSubjectAdapters"));

    /**
	 * VerifyKeyListeners for the control.
	 */
    private List fVerifyKeyListeners;

    /**
	 * KeyListeners for the control.
	 */
    private Set fKeyListeners;

    /**
	 * The Listener installed on the control which passes events to
	 * {@link #fVerifyKeyListeners fVerifyKeyListeners} and {@link #fKeyListeners}.
	 */
    private Listener fControlListener;

    /**
	 * The cue label provider, or <code>null</code> iff none.
	 * @since 3.3
	 */
    private ILabelProvider fCueLabelProvider;

    /**
	 * The control decoration, or <code>null</code> iff fCueLabelProvider is null.
	 * @since 3.3
	 */
    private ControlDecoration fControlDecoration;

    /**
	 * The default cue image, or <code>null</code> if not cached yet.
	 * @since 3.3
	 */
    private Image fCachedDefaultCueImage;

    /**
	 * Creates a new {@link AbstractControlContentAssistSubjectAdapter}.
	 */
    public AbstractControlContentAssistSubjectAdapter() {
        fVerifyKeyListeners = new ArrayList(1);
        fKeyListeners = new HashSet(1);
    }

    public abstract Control getControl();

    public void addKeyListener(KeyListener keyListener) {
        fKeyListeners.add(keyListener);
        if (DEBUG) System.out.println("AbstractControlContentAssistSubjectAdapter#addKeyListener()");
        installControlListener();
    }

    public void removeKeyListener(KeyListener keyListener) {
        boolean deleted = fKeyListeners.remove(keyListener);
        if (DEBUG) {
            if (!deleted) System.out.println("removeKeyListener -> wasn't here");
            System.out.println("AbstractControlContentAssistSubjectAdapter#removeKeyListener() -> " + fKeyListeners.size());
        }
        uninstallControlListener();
    }

    public boolean supportsVerifyKeyListener() {
        return true;
    }

    public boolean appendVerifyKeyListener(final VerifyKeyListener verifyKeyListener) {
        fVerifyKeyListeners.add(verifyKeyListener);
        if (DEBUG) System.out.println("AbstractControlContentAssistSubjectAdapter#appendVerifyKeyListener() -> " + fVerifyKeyListeners.size());
        installControlListener();
        return true;
    }

    public boolean prependVerifyKeyListener(final VerifyKeyListener verifyKeyListener) {
        fVerifyKeyListeners.add(0, verifyKeyListener);
        if (DEBUG) System.out.println("AbstractControlContentAssistSubjectAdapter#prependVerifyKeyListener() -> " + fVerifyKeyListeners.size());
        installControlListener();
        return true;
    }

    public void removeVerifyKeyListener(VerifyKeyListener verifyKeyListener) {
        fVerifyKeyListeners.remove(verifyKeyListener);
        if (DEBUG) System.out.println("AbstractControlContentAssistSubjectAdapter#removeVerifyKeyListener() -> " + fVerifyKeyListeners.size());
        uninstallControlListener();
    }

    public void setEventConsumer(IEventConsumer eventConsumer) {
        if (DEBUG) System.out.println("AbstractControlContentAssistSubjectAdapter#setEventConsumer()");
    }

    public String getLineDelimiter() {
        return System.getProperty("line.separator");
    }

    /**
	 * Installs <code>fControlListener</code>, which handles VerifyEvents and KeyEvents by
	 * passing them to {@link #fVerifyKeyListeners} and {@link #fKeyListeners}.
	 */
    private void installControlListener() {
        if (DEBUG) System.out.println("AbstractControlContentAssistSubjectAdapter#installControlListener() -> k: " + fKeyListeners.size() + ", v: " + fVerifyKeyListeners.size());
        if (fControlListener != null) return;
        fControlListener = new Listener() {

            public void handleEvent(Event e) {
                if (!getControl().isFocusControl()) return;
                VerifyEvent verifyEvent = new VerifyEvent(e);
                KeyEvent keyEvent = new KeyEvent(e);
                switch(e.type) {
                    case SWT.Traverse:
                        if (DEBUG) dump("before traverse", e, verifyEvent);
                        verifyEvent.doit = true;
                        for (Iterator iter = fVerifyKeyListeners.iterator(); iter.hasNext(); ) {
                            ((VerifyKeyListener) iter.next()).verifyKey(verifyEvent);
                            if (!verifyEvent.doit) {
                                e.detail = SWT.TRAVERSE_NONE;
                                e.doit = true;
                                if (DEBUG) dump("traverse eaten by verify", e, verifyEvent);
                                return;
                            }
                            if (DEBUG) dump("traverse OK", e, verifyEvent);
                        }
                        break;
                    case SWT.KeyDown:
                        for (Iterator iter = fVerifyKeyListeners.iterator(); iter.hasNext(); ) {
                            ((VerifyKeyListener) iter.next()).verifyKey(verifyEvent);
                            if (!verifyEvent.doit) {
                                e.doit = verifyEvent.doit;
                                if (DEBUG) dump("keyDown eaten by verify", e, verifyEvent);
                                return;
                            }
                        }
                        if (DEBUG) dump("keyDown OK", e, verifyEvent);
                        for (Iterator iter = fKeyListeners.iterator(); iter.hasNext(); ) {
                            ((KeyListener) iter.next()).keyPressed(keyEvent);
                        }
                        break;
                    default:
                        Assert.isTrue(false);
                }
            }

            /**
			 * Dump the given events to "standard" output.
			 *
			 * @param who who dump's
			 * @param e the event
			 * @param ve the verify event
			 */
            private void dump(String who, Event e, VerifyEvent ve) {
                StringBuffer sb = new StringBuffer("--- [AbstractControlContentAssistSubjectAdapter]\n");
                sb.append(who);
                sb.append(" - e: keyCode=" + e.keyCode + hex(e.keyCode));
                sb.append("; character=" + e.character + hex(e.character));
                sb.append("; stateMask=" + e.stateMask + hex(e.stateMask));
                sb.append("; doit=" + e.doit);
                sb.append("; detail=" + e.detail + hex(e.detail));
                sb.append("; widget=" + e.widget);
                sb.append("\n");
                sb.append("  verifyEvent keyCode=" + e.keyCode + hex(e.keyCode));
                sb.append("; character=" + e.character + hex(e.character));
                sb.append("; stateMask=" + e.stateMask + hex(e.stateMask));
                sb.append("; doit=" + ve.doit);
                sb.append("; widget=" + e.widget);
                System.out.println(sb);
            }

            private String hex(int i) {
                return "[0x" + Integer.toHexString(i) + ']';
            }
        };
        getControl().addListener(SWT.Traverse, fControlListener);
        getControl().addListener(SWT.KeyDown, fControlListener);
        if (DEBUG) System.out.println("AbstractControlContentAssistSubjectAdapter#installControlListener() - installed");
    }

    /**
	 * Uninstalls <code>fControlListener</code> iff there are no <code>KeyListener</code>s and no
	 * <code>VerifyKeyListener</code>s registered.
	 * Otherwise does nothing.
	 */
    private void uninstallControlListener() {
        if (fControlListener == null || fKeyListeners.size() + fVerifyKeyListeners.size() != 0) {
            if (DEBUG) System.out.println("AbstractControlContentAssistSubjectAdapter#uninstallControlListener() -> k: " + fKeyListeners.size() + ", v: " + fVerifyKeyListeners.size());
            return;
        }
        getControl().removeListener(SWT.Traverse, fControlListener);
        getControl().removeListener(SWT.KeyDown, fControlListener);
        fControlListener = null;
        if (DEBUG) System.out.println("AbstractControlContentAssistSubjectAdapter#uninstallControlListener() - done");
    }

    /**
	 * Sets the visual feedback provider for content assist.
	 * The given {@link ILabelProvider} methods are called with
	 * {@link #getControl()} as argument.
	 *
	 * <ul>
	 *   <li><code>getImage(Object)</code> provides the visual cue image.
	 *     The image can maximally be 5 pixels wide and 8 pixels high.
	 *     If <code>getImage(Object)</code> returns <code>null</code>, a default image is used.
	 *   </li>
	 *   <li><code>getText(Object)</code> provides the hover info text.
	 *     It is shown when hovering over the cue image or the adapted {@link Control}.
	 *     No info text is shown if <code>getText(Object)</code> returns <code>null</code>.
	 *   </li>
	 * </ul>
	 * <p>
	 * The given {@link ILabelProvider} becomes owned by the {@link AbstractControlContentAssistSubjectAdapter},
	 * i.e. it gets disposed when the adapted {@link Control} is disposed
	 * or when another {@link ILabelProvider} is set.
	 * </p>
	 *
	 * @param labelProvider a {@link ILabelProvider}, or <code>null</code>
	 * 	if no visual feedback should be shown
	 */
    public void setContentAssistCueProvider(final ILabelProvider labelProvider) {
        if (fCueLabelProvider != null) {
            fCueLabelProvider.dispose();
        }
        fCueLabelProvider = labelProvider;
        if (labelProvider == null) {
            if (fControlDecoration != null) {
                fControlDecoration.dispose();
                fControlDecoration = null;
            }
        } else {
            if (fControlDecoration == null) {
                fControlDecoration = new ControlDecoration(getControl(), (SWT.TOP | SWT.LEFT));
                getControl().addDisposeListener(new DisposeListener() {

                    public void widgetDisposed(DisposeEvent e) {
                        if (fCueLabelProvider != null) {
                            fCueLabelProvider.dispose();
                            fCueLabelProvider = null;
                        }
                        if (fControlDecoration != null) {
                            fControlDecoration.dispose();
                            fControlDecoration = null;
                        }
                        if (fCachedDefaultCueImage != null) {
                            fCachedDefaultCueImage.dispose();
                            fCachedDefaultCueImage = null;
                        }
                    }
                });
                fControlDecoration.setShowHover(true);
                fControlDecoration.setShowOnlyOnFocus(true);
            }
            ILabelProviderListener listener = new ILabelProviderListener() {

                public void labelProviderChanged(LabelProviderChangedEvent event) {
                    fControlDecoration.setDescriptionText(labelProvider.getText(getControl()));
                    Image image = labelProvider.getImage(getControl());
                    if (image == null) image = getDefaultCueImage();
                    fControlDecoration.setImage(image);
                }
            };
            labelProvider.addListener(listener);
            listener.labelProviderChanged(new LabelProviderChangedEvent(labelProvider));
        }
    }

    /**
	 * Returns the default cue image.
	 * 
	 * @return the default cue image
	 * @since 3.3
	 */
    private Image getDefaultCueImage() {
        if (fCachedDefaultCueImage == null) {
            ImageDescriptor cueID = ImageDescriptor.createFromFile(AbstractControlContentAssistSubjectAdapter.class, "images/content_assist_cue.gif");
            fCachedDefaultCueImage = cueID.createImage(getControl().getDisplay());
        }
        return fCachedDefaultCueImage;
    }
}
