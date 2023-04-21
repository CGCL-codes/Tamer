package net.infordata.ifw2.web.form;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.infordata.ifw2.web.bnds.IAction;
import net.infordata.ifw2.web.bnds.IField;
import net.infordata.ifw2.web.bnds.IForm;
import net.infordata.ifw2.web.bnds.StrokeStripe;

/**
 * A simple {@link IForm} implementation with no fields, it can contains only 
 * actions.<br>
 * @author valentino.proietti
 */
public class ActionsForm implements IForm, Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(ActionsForm.class);

    private final Map<String, IAction> ivActions = new HashMap<String, IAction>();

    private final StrokeStripe ivStrokes = new StrokeStripe();

    @Override
    public ActionsForm registerAction(String id, IAction action) {
        if (id == null) throw new NullPointerException("Id cannot be null");
        if (action == null) throw new NullPointerException("Action cannot be null");
        if (ivActions.containsKey(id)) throw new IllegalArgumentException("Id already registered: " + id);
        ivActions.put(id, action);
        if (LOGGER.isDebugEnabled()) LOGGER.debug("Registering action: " + id);
        return this;
    }

    @Override
    public ActionsForm replaceAction(String id, IAction action) {
        if (id == null) throw new NullPointerException("Id cannot be null");
        if (action == null) throw new NullPointerException("Action cannot be null");
        if (!ivActions.containsKey(id)) throw new IllegalArgumentException("Id not registered: " + id);
        if (LOGGER.isDebugEnabled()) LOGGER.debug("Replacing action: " + id);
        ivActions.put(id, action);
        return this;
    }

    @Override
    public IAction removeAction(String id) {
        return ivActions.remove(id);
    }

    @Override
    public void binded(int id) {
    }

    /**
   */
    @Override
    public <T extends IField> T bindField(Class<T> type, String id) {
        throw new IllegalStateException("This form cannot have binded fields");
    }

    @Override
    public IAction getAction(String id) {
        return ivActions.get(id);
    }

    @Override
    public Set<String> getActions() {
        return Collections.unmodifiableSet(ivActions.keySet());
    }

    @Override
    public StrokeStripe getStrokes() {
        return ivStrokes;
    }

    @Override
    public String getLabel(String id) {
        return null;
    }

    @Override
    public IField getBindedField(String id) {
        return null;
    }

    @Override
    public Set<String> getBindedFields() {
        return Collections.emptySet();
    }

    @Override
    public IField getField(String id) {
        return null;
    }

    @Override
    public Set<String> getFields() {
        return Collections.emptySet();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public boolean validate(String fieldId, String... otherFieldsToValidate) {
        return true;
    }

    @Override
    public final int hashCode() {
        return super.hashCode();
    }

    @Override
    public final boolean equals(Object obj) {
        return super.equals(obj);
    }
}
