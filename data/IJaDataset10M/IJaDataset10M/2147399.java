package org.nakedobjects.viewer.swing.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;
import org.nakedobjects.object.control.About;
import org.nakedobjects.object.ImageIcon;
import org.nakedobjects.object.Naked;
import org.nakedobjects.object.NakedClass;
import org.nakedobjects.object.NakedCollection;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.collection.InstanceCollection;
import org.nakedobjects.object.control.Permission;
import org.nakedobjects.object.reflect.Action;
import org.nakedobjects.object.reflect.Association;
import org.nakedobjects.object.reflect.Field;
import org.nakedobjects.object.reflect.OneToManyAssociation;
import org.nakedobjects.object.reflect.OneToOneAssociation;
import org.nakedobjects.object.reflect.Value;
import org.nakedobjects.security.Session;
import org.nakedobjects.viewer.swing.AbstractCompositeView;
import org.nakedobjects.viewer.swing.DnDHandler;
import org.nakedobjects.viewer.swing.LayoutHints;
import org.nakedobjects.viewer.swing.NakedAction;
import org.nakedobjects.viewer.swing.NakedConstants;
import org.nakedobjects.viewer.swing.NakedInternalFrame;
import org.nakedobjects.viewer.swing.NakedView;
import org.nakedobjects.viewer.swing.SwingFrame;
import org.nakedobjects.viewer.swing.SwingViewingMechanism;
import org.nakedobjects.viewer.swing.TitleLabel;
import org.nakedobjects.viewer.swing.ViewFactory;
import org.nakedobjects.viewer.swing.event.NakedActionEvent;
import org.nakedobjects.viewer.swing.event.NakedActionListener;
import org.nakedobjects.viewer.swing.util.GenericAction;
import org.nakedobjects.viewer.swing.util.GuiTools;

/**
 * Default view for a naked object. Both ICON and FORM are supported.
 * 
 * @author beders
 * @version $Id: NakedObjectView.java,v 1.3 2004/09/09 21:35:15 beders Exp $
 */
public class NakedObjectView extends AbstractCompositeView implements NakedActionListener, MouseListener {

    private static final Logger LOG = Logger.getLogger(NakedObjectView.class);

    /** Holds the label used to display the title() of a NO. */
    protected JLabel titleLabel;

    /**
   * Holds the label to show the field name (if a field is used in the view).
   */
    protected JLabel fieldLabel;

    /**
   * Set to an action that triggers a find. If null, the object shown is not a finder. Set in the fillPopup method.
   */
    protected GenericAction findAction;

    /** Holds a flag to enable/disable this view. */
    protected boolean enabled = true;

    /** Holds a list of NakedAction objects. Initialized in fillPopup */
    protected java.util.List nakedActions;

    /** Creates a new instance of NakedObjectView */
    public NakedObjectView() {
        ViewFactory.ViewStyle[] defaultStyles = new ViewFactory.ViewStyle[] { ViewFactory.ViewStyle.ICON, ViewFactory.ViewStyle.FORM };
        setSupportedViewStyles(defaultStyles);
    }

    /**
   * Implement this method to initialize the renderer and make it available using getComponent().
   *  
   */
    protected Component createComponent() {
        JComponent compo = new JPanel();
        fillMainPanel(compo);
        return compo;
    }

    /** Create the component to display the field. */
    protected Component createFieldComponent() {
        JLabel label = new JLabel(getField().getName());
        label.setTransferHandler(createTransferHandler(getField().getType()));
        return label;
    }

    /** Add and layout the subcomponents of compo. */
    protected void fillMainPanel(JComponent compo) {
        compo.setLayout(new GridBagLayout());
        JLabel emptyRenderer = null;
        JPanel formPanel = null;
        if (null == getNakedObject()) {
            emptyRenderer = createEmptyRenderer(compo);
        } else {
            if (ViewFactory.ViewStyle.ICON == getStyle()) {
                titleLabel = createIconRenderer(compo);
                compo.setMaximumSize(new Dimension(Integer.MAX_VALUE, compo.getPreferredSize().height));
            } else {
                titleLabel = createIconRenderer(compo);
                formPanel = createFormRenderer(compo);
                compo.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
            }
        }
    }

    /** Create a component that shows that no NakedObject is available here. */
    protected JLabel createEmptyRenderer(JComponent parent) {
        String noObjectText;
        Field asField = getField();
        noObjectText = title();
        JLabel noObject = new JLabel(noObjectText);
        Font baseFont = noObject.getFont();
        Font newFont = new Font(baseFont.getName(), Font.ITALIC, baseFont.getSize() - 1);
        noObject.setFont(newFont);
        noObject.setTransferHandler(createTransferHandler(asField.getType()));
        GuiTools.constrain(parent, noObject, 0, 0, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.NORTHWEST, 1.0, 1.0, 0, 0, 0, 0);
        return noObject;
    }

    /**
   * Create an icon view for the object (which also shows the title and is a handle for dragging the object and showing the popup).
   */
    protected JLabel createIconRenderer(JComponent parent) {
        boolean needCloseButton = true;
        boolean isFinder = (getNakedObject().isFinder());
        boolean isEnabled = isEnabled();
        if (getField() != null) {
            if (getField() instanceof OneToOneAssociation) {
                OneToOneAssociation assoc = (OneToOneAssociation) getField();
                About a = assoc.getAbout(Session.getSession().getSecurityContext(), (NakedObject) getParentObject(), getNakedObject());
                if (a.canUse().isVetoed()) {
                    needCloseButton = false;
                }
            } else if (getField() instanceof Value) {
                Value v = (Value) getField();
                About a = v.getAbout(Session.getSession().getSecurityContext(), (NakedObject) getParentObject());
                if (a.canUse().isVetoed()) {
                    needCloseButton = false;
                }
            }
        } else {
            NakedView parentView = getParentView();
            if (parentView != null) {
                if (parentView.getField() != null) {
                    if (parentView.getField() instanceof OneToManyAssociation) {
                        OneToManyAssociation assoc = (OneToManyAssociation) parentView.getField();
                        About a = assoc.getAbout(Session.getSession().getSecurityContext(), (NakedObject) (parentView.getParentView().getObject()));
                        if (a.canUse().isVetoed()) {
                            needCloseButton = false;
                        }
                    }
                } else {
                    needCloseButton = false;
                }
            } else {
                needCloseButton = false;
            }
        }
        String iconName = getNakedObject().getIconName();
        java.awt.Image image = ImageIcon.getImageIcon(iconName, ImageIcon.NORMAL);
        if (null == image) {
            image = ImageIcon.getImageIcon(iconName, ImageIcon.SMALL);
        }
        javax.swing.ImageIcon imageIcon = null;
        if (null == image) {
            URL imageResource = getClass().getResource("/images/" + iconName + ".gif");
            if (null == imageResource) {
                imageResource = getClass().getResource(iconName + ".gif");
            }
            if (null != imageResource) {
                imageIcon = new javax.swing.ImageIcon(imageResource);
            }
        } else {
            imageIcon = new javax.swing.ImageIcon(image);
        }
        TitleLabel lab = new TitleLabel(title(), imageIcon, needCloseButton, isFinder, isEnabled);
        if (needCloseButton) {
            lab.addCloseButtonListener(new GenericAction(this, "closeButtonPressed", null, null));
        }
        JLabel jbTitle = lab.getTitleLabel();
        if (isEnabled()) {
            installPopup(jbTitle);
            installSystemPopup(jbTitle);
            Class allowedClass = null;
            org.nakedobjects.object.reflect.Action actions[] = getDropActions();
            if (actions != null && actions.length > 0) {
                allowedClass = NakedObject.class;
            }
            jbTitle.setTransferHandler(createTransferHandler(allowedClass));
            GuiTools.installDragSupport(jbTitle);
            jbTitle.addMouseListener(this);
        }
        GuiTools.constrain(parent, lab, 0, 0, 2, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 0, 2, 0);
        return jbTitle;
    }

    /**
   * Get all actions which are triggered by dropping on the title field (or icon) In case of regular NakedObjects, these are object actions with one parameter.
   * 
   * @return the actions for drop
   */
    protected Action[] getDropActions() {
        return getNakedObject().getNakedClass().getObjectActions(Action.USER, 1);
    }

    /**
   * Create a form view for the object. The form view lays out all fields of a NakedObject.
   */
    protected JPanel createFormRenderer(JComponent parent) {
        JPanel form = (JPanel) parent;
        int xc = 1, yc = 1;
        Field fields[] = getNakedObject().getNakedClass().getFields();
        for (int f = 0; f < fields.length; f++) {
            Field field = fields[f];
            Class type;
            if (field instanceof org.nakedobjects.object.reflect.OneToManyAssociation) {
                type = NakedCollection.class;
            } else {
                type = field.getType();
            }
            Naked no = field.get(getNakedObject());
            ViewFactory.ViewStyle preferredStyle = ViewFactory.ViewStyle.ICON;
            LOG.debug("Looking for view for field: " + field + " for object: " + getNakedObject());
            if (type != null) {
                NakedView nakedView = SwingViewingMechanism.getInstance().getViewFactory().createView(type, preferredStyle);
                if (nakedView == null || nakedView instanceof ViewFactory.DummyView) {
                    preferredStyle = ViewFactory.ViewStyle.FORM;
                    nakedView = SwingViewingMechanism.getInstance().getViewFactory().createView(type, preferredStyle);
                }
                nakedView.init(no, field, this, preferredStyle);
                getSubviews().add(nakedView);
                if (null != nakedView.getFieldComponent()) {
                    GuiTools.constrain(form, nakedView.getFieldComponent(), xc - 1, yc, 1, 1, GridBagConstraints.NONE, GridBagConstraints.NORTHWEST, 0.0, 0.0);
                }
                Component mainComponent = nakedView.getComponent();
                LayoutHints hints = nakedView.getLayoutHints();
                int fill;
                if (null != hints) {
                    fill = hints.getFill();
                } else {
                    fill = GridBagConstraints.BOTH;
                }
                double weightY = 1.0;
                if (fill == GridBagConstraints.HORIZONTAL || fill == GridBagConstraints.NONE) {
                    weightY = 0.0;
                }
                GuiTools.constrain(form, mainComponent, xc, yc++, 2, 1, fill, GridBagConstraints.NORTHWEST, 1.0, weightY);
            } else {
                LOG.warn("Field type was null: can't add view. Field = " + field);
            }
        }
        return form;
    }

    /** Install a popup menu for the naked object on a component. */
    protected void installPopup(Component aComponent) {
        JPopupMenu popup = new JPopupMenu() {

            public void show(Component invoker, int x, int y) {
                NakedObjectView.this.checkNakedActions();
                super.show(invoker, x, y);
            }
        };
        popup.setInvoker(aComponent);
        fillPopup(popup);
        if (popup.getComponentCount() > 0) {
            GuiTools.installPopupListener(aComponent, popup);
        }
    }

    /**
   * Install a popup menu for system commands (which are not regularly defined)
   */
    public void installSystemPopup(Component aComponent) {
        JPopupMenu popup = new JPopupMenu();
        popup.setInvoker(aComponent);
        GenericAction destroyAction = new GenericAction(this, "destroyObject", "Destroy", null);
        popup.add(destroyAction);
        GuiTools.installPopupListener(aComponent, popup, InputEvent.SHIFT_MASK);
    }

    /** Fill the popup with the 0-parameter actions. */
    protected void fillPopup(JPopupMenu popup) {
        popup.removeAll();
        NakedObject object = getNakedObject();
        if (null == object) return;
        if (object.isFinder()) {
            findAction = new GenericAction(this, "findObjects", "Find...", null);
            popup.add(findAction);
        } else {
            org.nakedobjects.object.reflect.Action actions[] = object.getNakedClass().getObjectActions(Action.USER, 0);
            nakedActions = new ArrayList(actions.length);
            JMenuItem jmi;
            NakedAction na;
            for (int i = 0; i < actions.length; i++) {
                jmi = new JMenuItem();
                na = new NakedAction(actions[i], object);
                nakedActions.add(na);
                na.addNakedActionListener(this);
                jmi.setAction(na);
                popup.add(jmi);
            }
        }
    }

    /** Check all NakedActions if they are available. */
    protected void checkNakedActions() {
        if (nakedActions == null) return;
        int len = nakedActions.size();
        for (int i = 0; i < len; i++) {
            NakedAction na = (NakedAction) nakedActions.get(i);
            na.checkAbouts();
        }
    }

    /**
   * Callback from the close button on the TitleLabel. In this case, we want to disassociate us from our field or remove ourselves from a collection.
   */
    public void closeButtonPressed(ActionEvent ae) {
        Field field = getField();
        if (field instanceof Association) {
            Association assoc = (Association) field;
            LOG.debug("Removing from assocation in field " + field);
            assoc.clearAssociation((NakedObject) getParentObject(), getNakedObject());
        } else if (getParentObject() instanceof NakedCollection) {
            Field parentField = getParentView().getField();
            if (parentField instanceof Association) {
                Association assoc = (Association) parentField;
                LOG.debug("Disassociating from: " + assoc);
                assoc.clearAssociation((NakedObject) getParentView().getParentView().getObject(), getNakedObject());
            } else {
                NakedCollection coll = (NakedCollection) getParentObject();
                LOG.debug("Removing NO from collection:" + coll);
                coll.remove(getNakedObject());
            }
        }
    }

    /**
   * Callback from the find action. If the object is a finder, this action triggers a search.
   */
    public void findObjects(ActionEvent ae) {
        LOG.debug("Finding objects for:" + getNakedObject() + " persistence state: " + getNakedObject().isPersistent());
        InstanceCollection instances = new InstanceCollection(getNakedObject());
        SwingViewingMechanism.getInstance().showNakedObject(instances);
    }

    /** Callback from the destroy object action. */
    public void destroyObject(ActionEvent ae) {
        LOG.debug("Destroying:" + getNakedObject());
        DnDHandler.removeNakedObject(this, getNakedObject());
    }

    /**
   * Disable the view, because the object has been destroyed or otherwise disabled.
   */
    public void disableSelf(NakedObject anObject) {
        LOG.debug("Disabling me: " + this);
        setEnabled(false);
        setStyle(null);
        switchViewStyle(ViewFactory.ViewStyle.ICON);
    }

    /**
   * Switch to a supported view style. If an unsupported style is passed in, an exception will be thrown.
   */
    public void switchViewStyle(ViewFactory.ViewStyle aSupportedStyle) {
        if (aSupportedStyle == getStyle()) return;
        checkViewStyle(aSupportedStyle);
        LOG.debug("Switching style:" + aSupportedStyle);
        setStyle(aSupportedStyle);
        removeAllSubviews();
        JComponent compo = (JComponent) getComponent();
        compo.removeAll();
        fillMainPanel(compo);
        compo.revalidate();
        NakedInternalFrame.packInternalFrame(compo);
    }

    /**
   * Implement this method to refresh the UI of the NakedObject itself. Subviews will be informed in the refresh-implementation. The following checks are done
   * in here. Check:
   * <ul>
   * <li>if this view lost the object to show - resort to empty renderer
   * <li>if the object we show is different to the current object - refresh completely
   * <li>if the title of the object has changed - refresh the icon view
   * <li>if our field has been empty and we have received a new object - refresh completely
   */
    protected void refreshSelf(Naked changedObject) {
        if (getObject() != null) {
            boolean ourObjectHasChanged = false;
            NakedObject newObj = null;
            Association assoc = (Association) getField();
            if (null != assoc) {
                newObj = (NakedObject) assoc.get((NakedObject) getParentObject());
                if (null == newObj) {
                    LOG.debug("Lost our object.");
                    ourObjectHasChanged = true;
                } else {
                    if (newObj != getObject()) {
                        LOG.debug("Someone changed the field's object.");
                        ourObjectHasChanged = true;
                    }
                }
            }
            if (ourObjectHasChanged) {
                JComponent compo = (JComponent) getComponent();
                compo.removeAll();
                removeAllSubviews();
                setObject(newObj);
                fillMainPanel(compo);
                compo.revalidate();
                NakedInternalFrame.packInternalFrame(compo);
            } else {
                String oldTitle = titleLabel.getText();
                String newTitle = title();
                if (!oldTitle.equals(newTitle)) {
                    titleLabel.setText(newTitle);
                }
            }
        } else {
            Association assoc = (Association) getField();
            if (null != assoc) {
                NakedObject newObj = (NakedObject) assoc.get((NakedObject) getParentObject());
                if (newObj != null) {
                    LOG.debug("Got new object!");
                    JComponent compo = (JComponent) getComponent();
                    compo.removeAll();
                    removeAllSubviews();
                    setObject(newObj);
                    fillMainPanel(compo);
                    compo.revalidate();
                    NakedInternalFrame.packInternalFrame(compo);
                }
            }
        }
    }

    /** Dispose this view. This version just nulls the relevant values */
    public void dispose() {
        setObject(null);
        setField(null);
        setComponent(null);
        setFieldComponent(null);
        setSubviews(new ArrayList());
        nakedActions = null;
    }

    /** Deal with the drop of a NakedObject. */
    public boolean dropReceived(JComponent aComponent, Transferable theData) {
        boolean dropped = false;
        if (aComponent == titleLabel) {
            try {
                NakedObject droppedObj = DnDHandler.getDroppableNakedObject(theData.getTransferData(NakedConstants.NAKEDOBJECTFLAVOR));
                LOG.debug("Looking for 1-argument drop for " + droppedObj);
                NakedObject object = getNakedObject();
                if (null == object) return false;
                org.nakedobjects.object.reflect.Action action = findDropAction(droppedObj.getNakedClass());
                if (action != null) {
                    dropped = true;
                    Permission perm = action.getAbout(Session.getSession().getSecurityContext(), object, droppedObj).canUse();
                    if (perm.isAllowed()) {
                        NakedObject result = action.execute(object, droppedObj);
                        if (null != result) {
                            SwingViewingMechanism.getInstance().showNakedObject(result);
                        }
                    } else {
                        SwingViewingMechanism.getInstance().showPermission(perm, titleLabel);
                    }
                }
            } catch (UnsupportedFlavorException ufe) {
                throw new IllegalArgumentException("Unsupported flavour: NakedConstants.NAKEDOBJECTFLAVOR");
            } catch (IOException ie) {
                throw new RuntimeException("Trouble getting drop data.");
            }
        } else {
            LOG.debug("Drop on empty field received.");
            Field f = getField();
            if (f instanceof OneToOneAssociation) {
                OneToOneAssociation asso = (OneToOneAssociation) f;
                try {
                    NakedObject droppedObj = DnDHandler.getDroppableNakedObject(theData.getTransferData(NakedConstants.NAKEDOBJECTFLAVOR));
                    About assoAbout = asso.getAbout(Session.getSession().getSecurityContext(), (NakedObject) getParentView().getObject(), droppedObj);
                    if (assoAbout.canUse().isAllowed()) {
                        JComponent compo = (JComponent) getComponent();
                        compo.removeAll();
                        setObject(droppedObj);
                        fillMainPanel(compo);
                        compo.revalidate();
                        NakedInternalFrame.packInternalFrame(compo);
                        asso.setAssociation((NakedObject) getParentView().getObject(), droppedObj);
                        dropped = true;
                    } else {
                        LOG.debug("Assocation is not allowed.");
                        SwingViewingMechanism.getInstance().showPermission(assoAbout.canUse(), getFieldComponent());
                    }
                } catch (UnsupportedFlavorException ufe) {
                    throw new IllegalArgumentException("Unsupported flavour: NakedConstants.NAKEDOBJECTFLAVOR");
                } catch (IOException ie) {
                    throw new RuntimeException("Trouble getting drop data.");
                } catch (Exception e) {
                    LOG.error(e);
                    e.printStackTrace();
                }
            }
        }
        return dropped;
    }

    /**
   * An instance of nakedClass has been dropped on this object. Find the correct action to invoke in this case.
   * 
   * @param nakedClass
   * @return the action to execute
   */
    protected Action findDropAction(NakedClass nakedClass) {
        org.nakedobjects.object.reflect.Action action = getNakedObject().getNakedClass().getObjectAction(Action.USER, new NakedClass[] { nakedClass });
        return action;
    }

    /**
   * Invoked when an NakedAction has a NakedObject as result. This method adds the resulting object (if any) to the UI.
   */
    public void nakedActionPerformed(NakedActionEvent nav) {
        SwingViewingMechanism.getInstance().showNakedObject(nav.getNakedObject());
    }

    /** Called when an action could not be performed. */
    public void nakedActionVetoed(Permission aPermission) {
        SwingViewingMechanism.getInstance().showPermission(aPermission, titleLabel);
    }

    /**
   * If double-click with left mouse button happens, switch the view.
   *  
   */
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() > 1) {
            ViewFactory.ViewStyle style;
            if (getStyle().equals(ViewFactory.ViewStyle.FORM)) {
                style = ViewFactory.ViewStyle.ICON;
            } else {
                style = ViewFactory.ViewStyle.FORM;
            }
            switchViewStyle(style);
        }
    }

    /**
   * Invoked when the mouse enters a component.
   *  
   */
    public void mouseEntered(MouseEvent e) {
    }

    /**
   * Invoked when the mouse exits a component.
   *  
   */
    public void mouseExited(MouseEvent e) {
    }

    /**
   * Invoked when a mouse button has been pressed on a component.
   *  
   */
    public void mousePressed(MouseEvent e) {
    }

    /**
   * Invoked when a mouse button has been released on a component.
   *  
   */
    public void mouseReleased(MouseEvent e) {
    }

    /**
   * Getter for property enabled.
   * 
   * @return Value of property enabled.
   *  
   */
    public boolean isEnabled() {
        return enabled;
    }

    /**
   * Setter for property enabled.
   * 
   * @param enabled
   *          New value of property enabled.
   *  
   */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /** For debugging purposes. */
    public String toString() {
        return "NakedObjectView for " + getObject();
    }
}
