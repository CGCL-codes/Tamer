package com.safi.workshop.sheet.actionstep;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GroupEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;

/**
 * A command that will enable/disable the canonical editpolicy 
 * installed on the supplied editparts.
 * @author mhanner
 */
public class SafiToggleCanonicalModeCommand extends Command {

    /** enablement flag. */
    private boolean _enable;

    /** list of semantic elements canonical editpolicies are listening to. */
    private Collection _semanticHosts = new ArrayList();

    private TransactionalEditingDomain domain;

    /**
	 * Create an instance.
	 * @param editParts collection of editparts who's canonical editpolicies will be affected.
	 * @param enable enablement flag
	 */
    public SafiToggleCanonicalModeCommand(Collection editParts, boolean enable) {
        super(DiagramUIMessages.ToggleCanonicalModeCommand_Label);
        Object[] editparts = new Object[editParts.size()];
        editParts.toArray(editparts);
        for (int i = 0; i < editparts.length; i++) {
            EditPart editPart = (EditPart) editparts[i];
            if (editPart != null) {
                CanonicalEditPolicy editPolicy = getCanonicalEditPolicy(editPart);
                if (editPolicy != null) {
                    _semanticHosts.add(new WeakReference(editPolicy.getSemanticHost()));
                }
            }
        }
        _enable = enable;
    }

    /**
	 * Create an instance.
	 * @param element a semantic element
	 * @param enable enablement flag
	 */
    public SafiToggleCanonicalModeCommand(EObject element, boolean enable) {
        super(DiagramUIMessages.ToggleCanonicalModeCommand_Label);
        _semanticHosts.add(new WeakReference(element));
        _enable = enable;
    }

    /**
	 * Create an instance.
	 * @param target the target editpart
	 * @param enable the enablement flag
	 */
    public SafiToggleCanonicalModeCommand(EditPart target, boolean enable) {
        this(Collections.singletonList(target), enable);
    }

    /**
	 * <code>ToggleCanonicalModeCommand</code> factory method.  
	 * @param editParts collection of editparts who's canonical editpolicies will be affected.
	 * @param enable enablement flag
	 * @return a <code>ToggleCanonicalModeCommand</code> if at least one of the supplied editparts
	 * has a <code>CanonicalEditPolicy</code> installed on it, otherwise <tt>null</tt>.
	 */
    public static SafiToggleCanonicalModeCommand getToggleCanonicalModeCommand(Collection editParts, boolean enable) {
        SafiToggleCanonicalModeCommand cmd = new SafiToggleCanonicalModeCommand(editParts, enable);
        return cmd.getSemanticHosts().isEmpty() ? null : cmd;
    }

    /**
	 * <code>ToggleCanonicalModeCommand</code> factory method.  This copy constructor style factory
	 * will return a new command that shares the supplied commands semantic hosts.
	 * @param tcmd a <code>ToggleCanonicalModeCommand</code>
	 * @param enable enablement flag
	 * @return a <code>ToggleCanonicalModeCommand</code> if at least one of the supplied editparts
	 * has a <code>CanonicalEditPolicy</code> installed on it, otherwise <tt>null</tt>.
	 */
    public static SafiToggleCanonicalModeCommand getToggleCanonicalModeCommand(SafiToggleCanonicalModeCommand tcmd, boolean enable) {
        if (tcmd == null || tcmd.getSemanticHosts().isEmpty()) {
            return null;
        }
        SafiToggleCanonicalModeCommand cmd = new SafiToggleCanonicalModeCommand(Collections.EMPTY_LIST, enable);
        cmd.setSemanticHosts(tcmd.getSemanticHosts());
        return cmd;
    }

    /**
	 * Return the set of semantic hosts on which a canonical editpolicy is listening. 
	 * @return semantic hosts
	 */
    protected final Collection getSemanticHosts() {
        return _semanticHosts;
    }

    private final void setSemanticHosts(Collection hosts) {
        _semanticHosts.clear();
        _semanticHosts.addAll(hosts);
    }

    /**
	 * Return the canonical editpolicy installed on the supplied editpart.
	 * @param editPart edit part to use
	 * @return the canoncial edit policy if there is any
	 */
    protected static CanonicalEditPolicy getCanonicalEditPolicy(EditPart editPart) {
        while (editPart instanceof GroupEditPart) {
            editPart = editPart.getParent();
        }
        return (CanonicalEditPolicy) editPart.getEditPolicy(EditPolicyRoles.CANONICAL_ROLE);
    }

    /** Removes the canonical editpolict from the target editpart. */
    public void execute() {
        DoEnable(_enable);
    }

    /** 
	 * Enables the canonical editpolicies listening of the list of
	 * semantic elements. 
	 * @param enable enablement flag
	 */
    private void DoEnable(boolean enable) {
        Iterator references = getSemanticHosts().iterator();
        while (references.hasNext()) {
            WeakReference wr = (WeakReference) references.next();
            EObject semanticHost = (EObject) wr.get();
            if (semanticHost != null) {
                List ceps = CanonicalEditPolicy.getRegisteredEditPolicies(semanticHost);
                for (int i = 0; i < ceps.size(); i++) {
                    CanonicalEditPolicy cep = (CanonicalEditPolicy) ceps.get(i);
                    cep.enableRefresh(enable);
                }
            }
        }
    }

    public void redo() {
        DoEnable(_enable);
    }

    public void undo() {
        DoEnable(!_enable);
    }

    public void setDomain(TransactionalEditingDomain d) {
        domain = d;
    }
}
