package net.sf.jqueryfaces.component.draggable;

import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;
import net.sf.jqueryfaces.util.JSFUtility;

/**
 * This is the <code>DraggableTag</code> that is tied to the <code>Draggable</code
 * component.  It sets the values of the component.
 * 
 */
public class DraggableTag extends UIComponentTag {

    private String _style;

    private String _styleClass;

    private Boolean _addClasses;

    private String _appendTo;

    private String _axis;

    private String _cancel;

    private String _connectToSortable;

    private String _containment;

    private String _cursor;

    private Object _cursorAt;

    private Integer _distance;

    private String _grid;

    private String _handle;

    private String _helper;

    private Boolean _iframeFix;

    private Double _opacity;

    private Boolean _refreshPositions;

    private Boolean _revert;

    private Integer _revertDuration;

    private String _scope;

    private Boolean _scroll;

    private Integer _scrollSensitivity;

    private Integer _scrollSpeed;

    private Boolean _snap;

    private String _snapMode;

    private Integer _snapTolerance;

    private Object _stack;

    private Integer _zIndex;

    private String _onStart;

    private String _onDrag;

    private String _onStop;

    /**
     * Default constructor
     */
    public DraggableTag() {
        super();
    }

    public void release() {
        super.release();
        _style = null;
        _styleClass = null;
        _addClasses = null;
        _appendTo = null;
        _axis = null;
        _cancel = null;
        _connectToSortable = null;
        _containment = null;
        _cursor = null;
        _cursorAt = null;
        _distance = null;
        _grid = null;
        _handle = null;
        _helper = null;
        _iframeFix = null;
        _opacity = null;
        _refreshPositions = null;
        _revert = null;
        _revertDuration = null;
        _scope = null;
        _scroll = null;
        _scrollSensitivity = null;
        _scrollSpeed = null;
        _snap = null;
        _snapMode = null;
        _snapTolerance = null;
        _stack = null;
        _zIndex = null;
        _onStart = null;
        _onDrag = null;
        _onStop = null;
    }

    /**
     * @return  Gets the <code>COMPONENT_TYPE</code>
     */
    public String getComponentType() {
        return Draggable.COMPONENT_TYPE;
    }

    /**
     * @return  Gets the renderer type.  In this case the component renders itself
     */
    public String getRendererType() {
        return null;
    }

    /**
     * @param component Sets the component options that have been used
     */
    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        JSFUtility.initTagVariable(_style, Draggable.STYLE, component);
        JSFUtility.initTagVariable(_styleClass, Draggable.STYLECLASS, component);
        JSFUtility.initTagVariable(_addClasses, Draggable.ADDCLASSES, component);
        JSFUtility.initTagVariable(_appendTo, Draggable.APPENDTO, component);
        JSFUtility.initTagVariable(_axis, Draggable.AXIS, component);
        JSFUtility.initTagVariable(_cancel, Draggable.CANCEL, component);
        JSFUtility.initTagVariable(_connectToSortable, Draggable.CONNECTTOSORTABLE, component);
        JSFUtility.initTagVariable(_containment, Draggable.CONTAINMENT, component);
        JSFUtility.initTagVariable(_cursor, Draggable.CURSOR, component);
        JSFUtility.initTagVariable(_cursorAt, Draggable.CURSORAT, component);
        JSFUtility.initTagVariable(_distance, Draggable.DISTANCE, component);
        JSFUtility.initTagVariable(_grid, Draggable.GRID, component);
        JSFUtility.initTagVariable(_handle, Draggable.HANDLE, component);
        JSFUtility.initTagVariable(_helper, Draggable.HELPER, component);
        JSFUtility.initTagVariable(_iframeFix, Draggable.IFRAMEFIX, component);
        JSFUtility.initTagVariable(_opacity, Draggable.OPACITY, component);
        JSFUtility.initTagVariable(_refreshPositions, Draggable.REFRESHPOSITIONS, component);
        JSFUtility.initTagVariable(_revert, Draggable.REVERT, component);
        JSFUtility.initTagVariable(_revertDuration, Draggable.REVERTDURATION, component);
        JSFUtility.initTagVariable(_scope, Draggable.SCOPE, component);
        JSFUtility.initTagVariable(_scroll, Draggable.SCROLL, component);
        JSFUtility.initTagVariable(_scrollSensitivity, Draggable.SCROLLSENSITIVITY, component);
        JSFUtility.initTagVariable(_scrollSpeed, Draggable.SCROLLSPEED, component);
        JSFUtility.initTagVariable(_snap, Draggable.SNAP, component);
        JSFUtility.initTagVariable(_snapMode, Draggable.SNAPMODE, component);
        JSFUtility.initTagVariable(_snapTolerance, Draggable.SNAPTOLERANCE, component);
        JSFUtility.initTagVariable(_stack, Draggable.STACK, component);
        JSFUtility.initTagVariable(_zIndex, Draggable.ZINDEX, component);
        JSFUtility.initTagVariable(_onStart, Draggable.ONSTART.getOptionName(), component);
        JSFUtility.initTagVariable(_onDrag, Draggable.ONDRAG.getOptionName(), component);
        JSFUtility.initTagVariable(_onStop, Draggable.ONSTOP.getOptionName(), component);
    }

    /**
     * @param addClasses
     */
    public void setAddClasses(Boolean addClasses) {
        this._addClasses = addClasses;
    }

    /**
     * @return
     */
    public Boolean getAddClasses() {
        return _addClasses;
    }

    /**
     * @param axis
     */
    public void setAxis(String axis) {
        this._axis = axis;
    }

    /**
     * @return
     */
    public String getAxis() {
        return _axis;
    }

    /**
     * @param cursor
     */
    public void setCursor(String cursor) {
        this._cursor = cursor;
    }

    /**
     * @return
     */
    public String getCursor() {
        return _cursor;
    }

    /**
     * @param cursorAt
     */
    public void setCursorAt(Object cursorAt) {
        this._cursorAt = cursorAt;
    }

    /**
     * @return
     */
    public Object getCursorAt() {
        return _cursorAt;
    }

    /**
     * @param distance
     */
    public void setDistance(Integer distance) {
        this._distance = distance;
    }

    /**
     * @return
     */
    public Integer getDistance() {
        return _distance;
    }

    /**
     * @param helper
     */
    public void setHelper(String helper) {
        this._helper = helper;
    }

    /**
     * @return
     */
    public String getHelper() {
        return _helper;
    }

    /**
     * @param iframeFix
     */
    public void setIframeFix(Boolean iframeFix) {
        this._iframeFix = iframeFix;
    }

    /**
     * @return
     */
    public Boolean getIframeFix() {
        return _iframeFix;
    }

    /**
     * @param opacity
     */
    public void setOpacity(Double opacity) {
        this._opacity = opacity;
    }

    /**
     * @return
     */
    public Double getOpacity() {
        return _opacity;
    }

    /**
     * @param refreshPosition
     */
    public void setRefreshPosition(Boolean refreshPosition) {
        this._refreshPositions = refreshPosition;
    }

    /**
     * @return
     */
    public Boolean getRefreshPosition() {
        return _refreshPositions;
    }

    /**
     * @param revert
     */
    public void setRevert(Boolean revert) {
        this._revert = revert;
    }

    /**
     * @return
     */
    public Boolean getRevert() {
        return _revert;
    }

    /**
     * @param revertDuration
     */
    public void setRevertDuration(Integer revertDuration) {
        this._revertDuration = revertDuration;
    }

    /**
     * @return
     */
    public Integer getRevertDuration() {
        return _revertDuration;
    }

    /**
     * @param scope
     */
    public void setScope(String scope) {
        this._scope = scope;
    }

    /**
     * @return
     */
    public String getScope() {
        return _scope;
    }

    /**
     * @param scroll
     */
    public void setScroll(Boolean scroll) {
        this._scroll = scroll;
    }

    /**
     * @return
     */
    public Boolean getScroll() {
        return _scroll;
    }

    /**
     * @param scrollSensitivity
     */
    public void setScrollSensitivity(Integer scrollSensitivity) {
        this._scrollSensitivity = scrollSensitivity;
    }

    /**
     * @return
     */
    public Integer getScrollSensitivity() {
        return _scrollSensitivity;
    }

    /**
     * @param scrollSpeed
     */
    public void setScrollSpeed(Integer scrollSpeed) {
        this._scrollSpeed = scrollSpeed;
    }

    /**
     * @return
     */
    public Integer getScollSpeed() {
        return _scrollSpeed;
    }

    /**
     * @param snap
     */
    public void setSnap(Boolean snap) {
        this._snap = snap;
    }

    /**
     * @return
     */
    public Boolean getSnap() {
        return _snap;
    }

    /**
     * @param snapMode
     */
    public void setSnapMode(String snapMode) {
        this._snapMode = snapMode;
    }

    /**
     * @return
     */
    public String getSnapMode() {
        return _snapMode;
    }

    /**
     * @param snapTolerance
     */
    public void setSnapTolerance(Integer snapTolerance) {
        this._snapTolerance = snapTolerance;
    }

    /**
     * @return
     */
    public Integer getSnapTolerance() {
        return _snapTolerance;
    }

    /**
     * @param stack
     */
    public void setStack(Object stack) {
        this._stack = stack;
    }

    /**
     * @return
     */
    public Object getStack() {
        return _stack;
    }

    /**
     * @param zIndex
     */
    public void setZIndex(Integer zIndex) {
        this._zIndex = zIndex;
    }

    /**
     * @return
     */
    public Integer getZIndex() {
        return _zIndex;
    }

    /**
     * @param onStart
     */
    public void setOnStart(String onStart) {
        this._onStart = onStart;
    }

    /**
     * @return
     */
    public String getOnStart() {
        return _onStart;
    }

    /**
     * @param onDrag
     */
    public void setOnDrag(String onDrag) {
        this._onDrag = onDrag;
    }

    /**
     * @return
     */
    public String getOnDrag() {
        return _onDrag;
    }

    /**
     * @param onStop
     */
    public void setOnStop(String onStop) {
        this._onStop = onStop;
    }

    /**
     * @return
     */
    public String getOnStop() {
        return _onStop;
    }

    /**
     * @return
     */
    public String getAppendTo() {
        return _appendTo;
    }

    /**
     * @param appendTo
     */
    public void setAppendTo(String appendTo) {
        _appendTo = appendTo;
    }

    /**
     * @return
     */
    public String getCancel() {
        return _cancel;
    }

    /**
     * @param cancel
     */
    public void setCancel(String cancel) {
        _cancel = cancel;
    }

    /**
     * @return
     */
    public String getConnectToSortable() {
        return _connectToSortable;
    }

    /**
     * @param connectToSortable
     */
    public void setConnectToSortable(String connectToSortable) {
        _connectToSortable = connectToSortable;
    }

    /**
     * @return
     */
    public String getContainment() {
        return _containment;
    }

    /**
     * @param containment
     */
    public void setContainment(String containment) {
        _containment = containment;
    }

    /**
     * @return
     */
    public String getGrid() {
        return _grid;
    }

    /**
     * @param grid
     */
    public void setGrid(String grid) {
        _grid = grid;
    }

    /**
     * @return
     */
    public String getHandle() {
        return _handle;
    }

    /**
     * @param handle
     */
    public void setHandle(String handle) {
        _handle = handle;
    }

    /**
     * @return
     */
    public Boolean getRefreshPositions() {
        return _refreshPositions;
    }

    /**
     * @param refreshPositions
     */
    public void setRefreshPositions(Boolean refreshPositions) {
        _refreshPositions = refreshPositions;
    }

    /**
     * @return
     */
    public Integer getScrollSpeed() {
        return _scrollSpeed;
    }

    /**
     * @return
     */
    public String getStyle() {
        return _style;
    }

    /**
     * @param style
     */
    public void setStyle(String style) {
        _style = style;
    }

    /**
     * @return
     */
    public String getStyleClass() {
        return _styleClass;
    }

    /**
     * @param styleClass
     */
    public void setStyleClass(String styleClass) {
        _styleClass = styleClass;
    }
}
