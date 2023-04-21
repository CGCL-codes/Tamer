package com.volantis.mcs.eclipse.ab.editors.devices;

import java.util.List;
import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;
import com.volantis.mcs.eclipse.ab.editors.devices.odom.DeviceODOMElement;
import com.volantis.mcs.eclipse.ab.editors.devices.odom.DeviceODOMElementFactory;
import com.volantis.mcs.eclipse.ab.ABPlugin;
import com.volantis.mcs.eclipse.common.odom.*;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoManager;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.xml.xpath.XPathException;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.core.ResolvedDevicePolicy;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.JDOMFactory;

/**
 * The PolicyController is the control that allows policies to be modified. It
 * uses two widgets, a PolicyOriginSelector and a PolicyValueModifier. An
 * optional Label widget may also be supplied to provide a text description of
 * the policy. If the PolicyController is overriding a particular policy, the
 * PolicyValueModifier and Label (if any) are enabled and the user can enter a
 * new value. If the policy inherits its value, the PolicyValueModifier and
 * Label are disabled. The PolicyController takes the policy name at
 * construction time. This name is fixed for the lifetime of the
 * PolicyController. The name of the device whose policy is being modified by
 * PolicyController is set with {@link #setDeviceName}.
 * <STRONG>
 * Note that when you have finished using a PolicyController instance, you
 * must call its {@link #dispose} method.
 * </STRONG>
 */
public class PolicyController {

    /**
     * The factory to use for creating <inherit /> elements when fallback is
     * selected.
     */
    private static final JDOMFactory ODOM_FACTORY = new DeviceODOMElementFactory();

    /**
     * The name of the policy to modify. This is set on construction and cannot
     * change.
     */
    private final String policyName;

    /**
     * The PolicyValueOriginSelector used by this controller.
     */
    private final PolicyOriginSelector selector;

    /**
     * The PolicyValueModifier used by this controller.
     */
    private final PolicyValueModifier modifier;

    /**
     * The label for the policy name, if any.
     */
    private final Label policyLabel;

    /**
     * The DeviceEditorContext to use.
     */
    private final DeviceEditorContext context;

    /**
     * The name of the device currently being controlled.
     */
    private String deviceName;

    /**
     * The device element with the selected device name whose policies
     * can be modified with the controller.
     */
    private ODOMElement deviceElement;

    /**
     * The current policy origin selection type.
     */
    private PolicyOriginSelectionType currentOriginSelection;

    /**
     * The policy element listener that is used to react odom change events fired
     * by UNDO/REDO events.
     */
    private ODOMChangeListener policiesElementListener;

    /**
     * Creates a PolicyController.
     * @param policyName the name of the policy to control. Cannot be null or
     * empty.
     * @param selector the PolicyValueOriginSelector to use. Cannot be null.
     * @param modifier the PolicyValueModifier to use. Cannot be null.
     * @param policyLabel the Label widget for the policy. Can be null.
     * @param context the DeviceEditorContext to use for retrieving
     * policy information. Cannot be null.
     * @throws IllegalArgumentException if policyName is null or empty, or
     * either selector, modifier or dram is null
     */
    public PolicyController(final String policyName, PolicyOriginSelector selector, final PolicyValueModifier modifier, Label policyLabel, final DeviceEditorContext context) {
        if (policyName == null || policyName.length() == 0) {
            throw new IllegalArgumentException("Cannot be null or empty: " + "policyName.");
        }
        if (selector == null) {
            throw new IllegalArgumentException("Cannot be null: selector.");
        }
        if (modifier == null) {
            throw new IllegalArgumentException("Cannot be null: modifier.");
        }
        if (context == null) {
            throw new IllegalArgumentException("Cannot be null: context");
        }
        this.policyName = policyName;
        this.selector = selector;
        this.modifier = modifier;
        this.policyLabel = policyLabel;
        this.context = context;
        this.currentOriginSelection = selector.getPolicySelectorOriginType();
        enableModifierControls(currentOriginSelection == PolicyOriginSelectionType.OVERRIDE);
        this.selector.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent selectionEvent) {
                handlePolicyOriginChange();
            }
        });
        policiesElementListener = new ODOMChangeListener() {

            public void changed(ODOMObservable node, ODOMChangeEvent event) {
                try {
                    ODOMElement policyElement = null;
                    if (event.getSource() instanceof ODOMAttribute && event.getSource().getParent() == null) {
                        policyElement = (ODOMElement) event.getOldValue();
                    } else {
                        String xPathString = "ancestor-or-self::" + MCSNamespace.DEVICE.getPrefix() + ":" + DeviceRepositorySchemaConstants.POLICY_ELEMENT_NAME;
                        XPath xPath = new XPath(xPathString, new Namespace[] { MCSNamespace.DEVICE });
                        policyElement = (ODOMElement) xPath.selectSingleNode(event.getSource());
                    }
                    if (policyElement != null) {
                        String policyElementName = policyElement.getAttributeValue(DeviceRepositorySchemaConstants.POLICY_NAME_ATTRIBUTE);
                        if (policyElementName.equals(policyName)) {
                            ResolvedDevicePolicy rdp = context.getDeviceRepositoryAccessorManager().resolvePolicy(deviceName, policyName);
                            node.removeChangeListener(policiesElementListener);
                            if (rdp != null) {
                                updateOriginSelector();
                                modifier.setPolicy(rdp.policy);
                                node.addChangeListener(policiesElementListener);
                            } else {
                                dispose();
                            }
                        }
                    }
                } catch (XPathException e) {
                    EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
                }
            }
        };
    }

    /**
     * Handler for policy origin selector changes. Action is taken only if the
     * selection has changed. The PolicyValueModifier is updated accordingly.
     */
    private void handlePolicyOriginChange() {
        PolicyOriginSelectionType type = selector.getPolicySelectorOriginType();
        if (type != currentOriginSelection) {
            currentOriginSelection = type;
            if (PolicyOriginSelectionType.OVERRIDE == type) {
                handleOverrideSelection();
            } else if (PolicyOriginSelectionType.FALLBACK == type) {
                handleFallbackSelection();
            } else if (PolicyOriginSelectionType.RESTORE == type) {
                handleRestoreSelection();
            }
        }
    }

    /**
     * Handles changing to the Override policy origin by retrieving the
     * fallback policy, cloning it and adding the clone to the current policies
     * element.
     */
    private void handleOverrideSelection() {
        ResolvedDevicePolicy resolvedDevicePolicy = context.getDeviceRepositoryAccessorManager().resolvePolicy(deviceName, policyName);
        ODOMElement policy = (ODOMElement) resolvedDevicePolicy.policy.clone();
        ODOMElement clonedStandardElement = (ODOMElement) policy.getChild(DeviceRepositorySchemaConstants.STANDARD_ELEMENT_NAME, deviceElement.getNamespace());
        if (clonedStandardElement != null) {
            clonedStandardElement.detach();
        }
        ODOMElement policies = (ODOMElement) deviceElement.getChild(DeviceRepositorySchemaConstants.POLICIES_ELEMENT_NAME, deviceElement.getNamespace());
        DeviceODOMElement currentPolicy = (DeviceODOMElement) context.getDeviceRepositoryAccessorManager().retrievePolicy(deviceName, policyName);
        UndoRedoManager undoRedoManager = context.getUndoRedoManager();
        undoRedoManager.demarcateUOW();
        try {
            if (currentPolicy != null) {
                currentPolicy.override((DeviceODOMElement) policy);
            } else {
                policies.addContent(policy);
            }
        } finally {
            undoRedoManager.demarcateUOW();
        }
        enableModifierControls(true);
        modifier.setPolicy(policy);
    }

    /**
     * Handles changing to the Fallback policy origin by replacing all value
     * content or attributes, or field content from the overridden policy and
     * adding an inherit element instead.
     */
    private void handleFallbackSelection() {
        ODOMElement policy = (ODOMElement) context.getDeviceRepositoryAccessorManager().resolvePolicy(deviceName, policyName).policy;
        UndoRedoManager undoRedoManager = context.getUndoRedoManager();
        undoRedoManager.demarcateUOW();
        try {
            if (policy.getAttributeValue(DeviceRepositorySchemaConstants.POLICY_VALUE_ATTRIBUTE) != null) {
                policy.removeAttribute(DeviceRepositorySchemaConstants.POLICY_VALUE_ATTRIBUTE);
            } else {
                policy.removeChildren(DeviceRepositorySchemaConstants.POLICY_VALUE_ELEMENT_NAME, policy.getNamespace());
                policy.removeChildren(DeviceRepositorySchemaConstants.POLICY_DEFINITION_FIELD_ELEMENT_NAME, policy.getNamespace());
            }
            Element inherit = ODOM_FACTORY.element(DeviceRepositorySchemaConstants.INHERIT_ELEMENT_NAME, policy.getNamespace());
            List children = policy.getChildren();
            children.add(0, inherit);
            Element fallbackPolicy = context.getDeviceRepositoryAccessorManager().resolvePolicy(deviceName, policyName).policy;
            modifier.setPolicy(fallbackPolicy);
            enableModifierControls(false);
        } finally {
            undoRedoManager.demarcateUOW();
        }
    }

    /**
     * Handles changing to the restore policy origin by restoring the old
     * content using and updating the origin selector accordingly.
     */
    private void handleRestoreSelection() {
        Element policy = context.getDeviceRepositoryAccessorManager().retrievePolicy(deviceName, policyName);
        if (policy != null) {
            UndoRedoManager undoRedoManager = context.getUndoRedoManager();
            undoRedoManager.demarcateUOW();
            try {
                ((DeviceODOMElement) policy).restore();
                policy = context.getDeviceRepositoryAccessorManager().resolvePolicy(deviceName, policyName).policy;
                modifier.setPolicy(policy);
            } finally {
                undoRedoManager.demarcateUOW();
            }
        }
        updateOriginSelector();
    }

    /**
     * Enables or disables the PolicyValueModifier controls and optional label.
     * @param enable enable the controls if true; disable otherwise
     */
    private void enableModifierControls(boolean enable) {
        modifier.getControl().setEnabled(enable);
        if (policyLabel != null) {
            policyLabel.setEnabled(enable);
        }
    }

    /**
     * Updates the PolicyOriginSelector with the current device and policy,
     * and enables the PolicyValueModifier controls accordingly.
     */
    private void updateOriginSelector() {
        selector.setDetails(new PolicyOriginSelectorDetails(deviceName, policyName));
        currentOriginSelection = selector.getPolicySelectorOriginType();
        enableModifierControls(currentOriginSelection == PolicyOriginSelectionType.OVERRIDE);
    }

    /**
     * Sets the device name for the device element controlled by the
     * PolicyController. Action is only taken if the device element is for a
     * different device.
     * @param deviceName the name of the device. Cannot be null or empty.
     * @throws IllegalArgumentException if deviceElement is null or is not a
     * device element.
     */
    public void setDeviceName(String deviceName) {
        if (deviceName == null || deviceName.length() == 0) {
            throw new IllegalArgumentException("Cannot be null: deviceName");
        }
        if (!deviceName.equals(this.deviceName)) {
            this.deviceName = deviceName;
            deviceElement = (ODOMElement) context.getDeviceRepositoryAccessorManager().retrieveDeviceElement(deviceName);
            updateOriginSelector();
            ResolvedDevicePolicy rdp = context.getDeviceRepositoryAccessorManager().resolvePolicy(deviceName, policyName);
            modifier.setPolicy(rdp.policy);
            DeviceODOMElement policies = (DeviceODOMElement) deviceElement.getChild(DeviceRepositorySchemaConstants.POLICIES_ELEMENT_NAME, deviceElement.getNamespace());
            policies.submitRestorableName(policyName);
            policies.removeChangeListener(policiesElementListener);
            policies.addChangeListener(policiesElementListener);
        }
    }

    /**
     * Removes internal ODOMChangeListeners used by PolicyController. This
     * method must be called when a user is finished with this instance
     * of PolicyController.
     */
    public void dispose() {
        if (deviceElement != null) {
            DeviceODOMElement policies = (DeviceODOMElement) deviceElement.getChild(DeviceRepositorySchemaConstants.POLICIES_ELEMENT_NAME, deviceElement.getNamespace());
            policies.removeChangeListener(policiesElementListener);
        }
    }
}
