package org.openconcerto.ui.preferences;

import org.openconcerto.ui.valuewrapper.ValueWrapper;
import org.openconcerto.utils.AutoLayouter;
import org.openconcerto.utils.CollectionUtils;
import org.openconcerto.utils.checks.ValidChangeSupport;
import org.openconcerto.utils.checks.ValidListener;
import org.openconcerto.utils.checks.ValidObject;
import org.openconcerto.utils.checks.ValidState;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.JPanel;

/**
 * A {@link PreferencePanel} using java {@link Preferences}.
 * 
 * @author Sylvain CUAZ
 */
public abstract class JavaPrefPreferencePanel extends JPanel implements PreferencePanel {

    private final String title;

    private Preferences prefs;

    private final AutoLayouter layouter;

    private final Set<PrefView<?>> views;

    private boolean modified;

    private final ValidChangeSupport validSupp;

    public JavaPrefPreferencePanel(final String title, final Preferences prefs) {
        super(new BorderLayout());
        this.title = title;
        this.prefs = prefs;
        this.views = new HashSet<PrefView<?>>();
        this.modified = false;
        this.validSupp = new ValidChangeSupport(this);
        final JPanel content = new JPanel();
        this.add(content, BorderLayout.PAGE_START);
        this.layouter = new AutoLayouter(content);
    }

    public final void setPrefs(Preferences prefs) {
        if (this.prefs != null) throw new IllegalStateException("Already set : " + this.prefs);
        this.prefs = prefs;
    }

    @Override
    public final String getTitleName() {
        return this.title;
    }

    public final String getPrefPath() {
        return this.prefs.absolutePath();
    }

    @Override
    public void uiInit() {
        if (this.prefs == null) throw new NullPointerException("prefs wasn't set");
        this.addViews();
        this.reset();
    }

    protected abstract void addViews();

    protected final void addView(final PrefView<?> view) {
        final ValueWrapper<?> vw = view.getVW();
        this.layouter.add(view.getName(), vw.getComp());
        this.views.add(view);
        view.init(this);
        vw.addValueListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (!view.equalsToPrefValue(JavaPrefPreferencePanel.this.prefs)) setModified(true);
            }
        });
        vw.addValidListener(new ValidListener() {

            @Override
            public void validChange(ValidObject src, ValidState newValue) {
                JavaPrefPreferencePanel.this.validSupp.fireValidChange(getValidState());
            }
        });
    }

    /**
     * Reset UI to the stored values.
     */
    public final void reset() {
        for (final PrefView<?> v : this.views) {
            v.setViewValue(this.prefs);
        }
        this.setModified(false);
    }

    @Override
    public final void apply() {
        if (this.isModified()) {
            try {
                for (final PrefView<?> v : this.views) {
                    v.setPrefValue(this.prefs);
                }
                this.prefs.sync();
                this.reset();
            } catch (BackingStoreException e) {
                throw new IllegalStateException("Couldn't store values", e);
            }
        }
    }

    @Override
    public final void restoreToDefaults() {
        for (final PrefView<?> v : this.views) {
            v.resetViewValue();
        }
    }

    @Override
    public final void addModifyChangeListener(PreferencePanelListener l) {
        throw new UnsupportedOperationException();
    }

    private final void setModified(boolean modified) {
        if (this.modified != modified) {
            this.modified = modified;
        }
    }

    @Override
    public final boolean isModified() {
        return this.modified;
    }

    @Override
    public ValidState getValidState() {
        boolean res = true;
        final List<String> pbs = new ArrayList<String>();
        for (final PrefView<?> v : this.views) {
            final ValidState validState = v.getVW().getValidState();
            if (!validState.isValid()) {
                String explanation = "'" + v.getName() + "' n'est pas valide";
                final String txt = validState.getValidationText();
                if (txt != null) explanation += " (" + txt + ")";
                pbs.add(explanation);
                res = false;
            }
        }
        return ValidState.create(res, CollectionUtils.join(pbs, "\n"));
    }

    @Override
    public final void addValidListener(ValidListener l) {
        this.validSupp.addValidListener(l);
    }

    @Override
    public final void removeValidListener(ValidListener l) {
        this.validSupp.removeValidListener(l);
    }
}
