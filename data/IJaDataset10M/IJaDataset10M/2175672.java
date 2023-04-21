package com.objetdirect.gwt.umlapi.client;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.objetdirect.gwt.umlapi.client.controls.Keyboard;
import com.objetdirect.gwt.umlapi.client.engine.GeometryManager;
import com.objetdirect.gwt.umlapi.client.gfx.GfxManager;
import com.objetdirect.gwt.umlapi.client.helpers.CursorIconManager;
import com.objetdirect.gwt.umlapi.client.helpers.OptionsManager;
import com.objetdirect.gwt.umlapi.client.helpers.ThemeManager;
import com.objetdirect.gwt.umlapi.client.helpers.CursorIconManager.PointerStyle;
import com.objetdirect.gwt.umlapi.client.helpers.ThemeManager.Theme;
import com.objetdirect.gwt.umlapi.client.resources.Resources;
import com.objetdirect.gwt.umlapi.client.umlCanvas.ClassDiagram;
import com.objetdirect.gwt.umlapi.client.umlCanvas.DecoratorCanvas;
import com.objetdirect.gwt.umlapi.client.umlCanvas.ObjectDiagram;
import com.objetdirect.gwt.umlapi.client.umlCanvas.SequenceDiagram;
import com.objetdirect.gwt.umlapi.client.umlCanvas.UMLCanvas;
import com.objetdirect.gwt.umlapi.client.umlcomponents.DiagramType;

/**
 * This is the main entry class to add a drawer.
 * 
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
public class Drawer extends FocusPanel implements RequiresResize, ProvidesResize {

    private int width;

    private int height;

    private final UMLCanvas umlCanvas;

    private final DecoratorCanvas decoratorPanel;

    private boolean hotKeysEnabled;

    private final Keyboard keyboard;

    private final CursorIconManager cursorManager;

    public Drawer(UMLCanvas umlCanvas, DiagramType diagramType) {
        setupGfxPlatform();
        addHandlers();
        injectAllStyles();
        decoratorPanel = new DecoratorCanvas(this, umlCanvas);
        this.umlCanvas = umlCanvas;
        hotKeysEnabled = true;
        keyboard = createKeyboardFor(diagramType);
        cursorManager = new CursorIconManager();
        setWidget(decoratorPanel);
    }

    private Keyboard createKeyboardFor(DiagramType diagramType) {
        switch(diagramType) {
            case CLASS:
                return new Keyboard((ClassDiagram) umlCanvas);
            case OBJECT:
                return new Keyboard((ObjectDiagram) umlCanvas);
            case SEQUENCE:
                return new Keyboard((SequenceDiagram) umlCanvas);
        }
        throw new IllegalArgumentException("Invalid diagram type : " + diagramType);
    }

    private void injectAllStyles() {
        Resources.INSTANCE.iconStyles().ensureInjected();
    }

    private void setupGfxPlatform() {
        ThemeManager.setCurrentTheme((Theme.getThemeFromIndex(OptionsManager.get("Theme"))));
        GfxManager.setPlatform(OptionsManager.get("GraphicEngine"));
        GeometryManager.setPlatform(OptionsManager.get("GeometryStyle"));
    }

    private void addHandlers() {
        this.addKeyPressHandler(new KeyPressHandler() {

            @Override
            public void onKeyPress(KeyPressEvent event) {
                Log.trace("Drawer::addHandlers() onKeyPress on " + event.getNativeEvent().getKeyCode());
                if (hotKeysEnabled) {
                    keyboard.push(event.getCharCode(), event.isControlKeyDown(), event.isAltKeyDown(), event.isShiftKeyDown(), event.isMetaKeyDown());
                }
            }
        });
    }

    public void setCursorIcon(PointerStyle style) {
        cursorManager.changeCursorIcon(style, this);
    }

    @Override
    public void onResize() {
        int parentHeight = getParent().getOffsetHeight();
        int parentWidth = getParent().getOffsetWidth();
        int width = parentWidth - 16;
        int height = parentHeight - 16;
        this.width = width;
        this.height = height;
        this.setPixelSize(this.width, this.height);
        decoratorPanel.reSize(width, height);
    }

    /**
	 * @return the umlCanvas
	 */
    public UMLCanvas getUmlCanvas() {
        return umlCanvas;
    }

    /**
	 * @param hotKeysEnabled
	 *            the hotKeysEnabled to set
	 */
    public void setHotKeysEnabled(boolean hotKeysEnabled) {
        this.hotKeysEnabled = hotKeysEnabled;
    }

    /**
	 * @return the keyboard
	 */
    public Keyboard getKeyboard() {
        return keyboard;
    }
}
