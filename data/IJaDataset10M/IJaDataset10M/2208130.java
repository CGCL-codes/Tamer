package com.mia.sct.controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.jdesktop.animation.timing.TimingTarget;
import com.mia.sct.controller.notes.INoteDisplay;
import com.mia.sct.data.IActionEventHandler;
import com.mia.sct.data.event.ClickMapEvent;
import com.mia.sct.data.event.SelectionSource;
import com.mia.sct.data.event.SelectionStateKey;
import com.mia.sct.data.instrument.AbstractOverlayAvatar;
import com.mia.sct.data.instrument.ShapeOverlayAvatar;
import com.mia.sct.data.model.ClickMapModel;
import com.mia.sct.data.model.DisplayOverlayModel;
import com.mia.sct.data.model.MapLocationModel;
import com.mia.sct.util.MIAShapeUtils;

/**
 * ClickMapManager.java
 *
 * @author Devon Bryant
 * @since Feb 2, 2008
 */
public class ClickMapManager implements TimingTarget, IActionEventBroadcaster, INoteDisplay {

    private static Logger logger = Logger.getLogger(ClickMapManager.class);

    private Map<String, AbstractOverlayAvatar> avatars = new HashMap<String, AbstractOverlayAvatar>();

    protected ClickMapModel clickMapModel = null;

    protected DisplayOverlayModel displayOverlayModel = null;

    protected Component parentComponent = null;

    protected List<IActionEventHandler> eventHandlers = new ArrayList<IActionEventHandler>();

    private List<String> selectedAvatars = new ArrayList<String>();

    /**
	 * Constructor 
	 * 
	 * @param inParentComponent the parent component (most likely a JPanel) for this manager
	 * @param inClickMap the click map model to use for locations, actions, etc.
	 * @param inDisplayOverlay the display overlay model to use for images, colors, etc.
	 */
    public ClickMapManager(Component inParentComponent, ClickMapModel inClickMap, DisplayOverlayModel inDisplayOverlay) {
        parentComponent = inParentComponent;
        clickMapModel = inClickMap;
        displayOverlayModel = inDisplayOverlay;
        setupOverlayAvatars();
    }

    private void setupOverlayAvatars() {
        String hexColorString = null;
        Color defaultColor = null;
        Color selectedColor = null;
        Color overColor = null;
        Color inactiveColor = null;
        float defaultOpac = 0f;
        float selectedOpac = 0f;
        float overOpac = 0f;
        float inactiveOpac = 0f;
        AbstractOverlayAvatar avatar = null;
        try {
            try {
                hexColorString = displayOverlayModel.getDefaultColor();
                if (hexColorString != null) {
                    defaultColor = MIAShapeUtils.getColorForString(hexColorString);
                    defaultOpac = displayOverlayModel.getDefaultOpac();
                }
                hexColorString = displayOverlayModel.getSelectedColor();
                if (hexColorString != null) {
                    selectedColor = MIAShapeUtils.getColorForString(hexColorString);
                    selectedOpac = displayOverlayModel.getSelectedOpac();
                }
                hexColorString = displayOverlayModel.getOverColor();
                if (hexColorString != null) {
                    overColor = MIAShapeUtils.getColorForString(hexColorString);
                    overOpac = displayOverlayModel.getOverOpac();
                }
                hexColorString = displayOverlayModel.getInactiveColor();
                if (hexColorString != null) {
                    inactiveColor = MIAShapeUtils.getColorForString(hexColorString);
                    inactiveOpac = displayOverlayModel.getInactiveOpac();
                }
            } catch (NumberFormatException nfexc) {
                logger.error("ClickMapManager.setupOverlayAvatars():", nfexc);
            }
            for (MapLocationModel model : clickMapModel.getMapLocationMap().values()) {
                avatar = new ShapeOverlayAvatar(clickMapModel.getActionModels(), model, defaultOpac, selectedOpac, inactiveOpac, overOpac, defaultColor, selectedColor, inactiveColor, overColor, this);
                addOverlayAvatar(avatar);
            }
        } catch (Exception exc) {
            logger.error("ClickMapManager.setupOverlayAvatars():", exc);
        } finally {
            hexColorString = null;
            defaultColor = null;
            selectedColor = null;
            overColor = null;
            inactiveColor = null;
        }
    }

    /**
	 * Get the collection of overlay avatars for this manager
	 * @return Collection of overlay avatars
	 */
    public Collection<AbstractOverlayAvatar> getOverlayAvatars() {
        return avatars.values();
    }

    /**
	 * Get an overlay avatar by its location id
	 * @param inLocationID the location id of the avatar
	 * @return the avatar for the location id, null if none exist
	 */
    public AbstractOverlayAvatar getOverlayAvatar(String inLocationID) {
        return avatars.get(inLocationID);
    }

    /**
	 * De-select all notes/overlays
	 * <p>
	 * Note:  This does not remove the ids from the current selection list.  This is so
	 * that the previously selected notes can be re-selected.  This is useful when showing
	 * overlays for scales and such.
	 */
    public void deSelectAll() {
        for (AbstractOverlayAvatar avatar : avatars.values()) {
            if (avatar.isSelected()) {
                avatar.setSelected(false);
            }
            if (avatar.isShowingInfo()) {
                avatar.showInfo(false);
            }
        }
    }

    /**
	 * Re-select all the users previously selected avatars
	 */
    public void reSelectAll() {
        List<String> tempList = new ArrayList<String>(selectedAvatars.size());
        for (String locID : selectedAvatars) {
            tempList.add(locID);
        }
        for (String locID : tempList) {
            setLocationSelected(locID, true);
        }
        tempList.clear();
    }

    /**
	 * Add an overlay avatar to this manager
	 * @param inAvatar the overlay avatar to add
	 */
    public void addOverlayAvatar(AbstractOverlayAvatar inAvatar) {
        avatars.put(inAvatar.getMapLocationID(), inAvatar);
    }

    /**
	 * Remove an overlay avatar from this manager
	 * @param inLocationID the location id of the avatar
	 */
    public void removeOverlayAvatar(String inLocationID) {
        avatars.remove(inLocationID);
    }

    /**
	 * Add a handler (listener) for click map Action Events
	 * @param inHandler the handler to add
	 */
    public void addActionEventHandler(IActionEventHandler inHandler) {
        eventHandlers.add(inHandler);
    }

    /**
	 * Remove a handler (listener) for click map Action Events
	 * @param inHandler the handler to remove
	 */
    public void removeActionEventHandler(IActionEventHandler inHandler) {
        eventHandlers.remove(inHandler);
    }

    public void notifyActionEventHandlers(ClickMapEvent inEvent) {
        for (IActionEventHandler handler : eventHandlers) {
            handler.handleActionEvent(inEvent);
            if (inEvent.getSource() == SelectionSource.USER) {
                if (inEvent.getStateKey() == SelectionStateKey.SELECTED) {
                    selectedAvatars.add(inEvent.getLocationPoint().getLocationID());
                } else {
                    selectedAvatars.remove(inEvent.getLocationPoint().getLocationID());
                }
            }
        }
    }

    public void begin() {
    }

    public void end() {
    }

    public void repeat() {
    }

    public void timingEvent(float arg0) {
        if (parentComponent != null) {
            parentComponent.repaint();
        }
    }

    public boolean hasLocation(String inLocationID) {
        boolean result = false;
        if (avatars != null) {
            result = avatars.containsKey(inLocationID);
        }
        return result;
    }

    public Point getLocationPoint(String inLocationID) {
        Point result = null;
        if (hasLocation(inLocationID)) {
            result = getOverlayAvatar(inLocationID).getLocationPoint().getPoint();
        }
        return result;
    }

    public boolean isLocationSelected(String inLocationID) {
        boolean result = false;
        if (hasLocation(inLocationID)) {
            result = getOverlayAvatar(inLocationID).isSelected();
        }
        return result;
    }

    public boolean isLocationShowingInfo(String inLocationID) {
        boolean result = false;
        if (hasLocation(inLocationID)) {
            result = getOverlayAvatar(inLocationID).isShowingInfo();
        }
        return result;
    }

    public void setLocationSelected(String inLocationID, boolean inSelected) {
        if (hasLocation(inLocationID)) {
            AbstractOverlayAvatar avatar = getOverlayAvatar(inLocationID);
            avatar.setSelected(inSelected);
        }
    }

    public void setShowLocationInfo(String inLocationID, boolean inInfo) {
        if (hasLocation(inLocationID)) {
            AbstractOverlayAvatar avatar = getOverlayAvatar(inLocationID);
            avatar.showInfo(inInfo);
        }
    }

    public void setLocked(boolean inLocked) {
        for (AbstractOverlayAvatar avatar : avatars.values()) {
            avatar.setLocked(inLocked);
        }
    }
}
