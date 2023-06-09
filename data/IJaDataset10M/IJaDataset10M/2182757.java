package org.plantstreamer.treetable.node;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.script.Bindings;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import org.communications.CommunicationManager.STATUS;
import org.jinterop.dcom.common.JIException;
import org.opcda2out.util.Java2JavaCast;
import org.opcda2out.util.Java2JavaCastFactory;
import org.opcda2out.composite.Composite;
import org.opcda2out.composite.CompositeItem;
import org.plantstreamer.treetable.OPCTreeTableModel;
import org.opcda2out.output.database.nodes.PersistentOPCItemInfo;
import org.opcda2out.scripting.ScriptDataResult;
import org.opcda2out.scripting.handlers.ScriptingEngineHandler;
import org.plantstreamer.Main;
import org.plantstreamer.panel.compositeitempanel.CompositeNodeItemElement;
import org.plantstreamer.treetable.OPCTreeTableModel.SELECTIONSTATUS;
import org.opcda2out.output.database.nodes.PersistentCompositeNodeInfo;

/**
 * A composite item node for the OPC tree table model
 * 
 * @author Joao Leal
 */
public class CompositeTreeTableNode extends AbstractOPCTreeTableNode implements Composite, NodeData {

    /**
     * The selection status of this node
     */
    private SELECTIONSTATUS selected = SELECTIONSTATUS.UNSELECTED;

    /**
     * The list of composite item elements used in the script
     */
    private final CompositeItemElementList elements;

    /**
     * The script text
     */
    private String script;

    /**
     * The compiled script
     */
    private CompiledScript compiledScript;

    /**
     * The scripting engine handler
     */
    private ScriptingEngineHandler engineHandler;

    /**
     * The data generated by the script evaluation
     */
    private ScriptDataResult scriptResult;

    /**
     * The requested result data type for the script
     */
    private Class dataType;

    /**
     * Casts the script data result to the requested data type
     */
    private Java2JavaCast casting;

    /**
     * Creates a new composite item node
     * 
     * @param name The node name
     * @param engineHandler The scripting engine handler used by this node
     */
    public CompositeTreeTableNode(String name, ScriptingEngineHandler engineHandler) {
        this(name, engineHandler, Float.class, engineHandler.getScriptExample());
    }

    /**
     * Creates a new composite item node
     *
     * @param name The node name
     * @param engineHandler The scripting engine handler used by this node
     * @param dataType The requested data type
     * @param script The script text
     */
    public CompositeTreeTableNode(String name, ScriptingEngineHandler engineHandler, Class dataType, String script) {
        super(name);
        if (engineHandler == null) {
            throw new IllegalArgumentException("The scripting engine handler must be defined!");
        } else if (dataType == null) {
            throw new IllegalArgumentException("The data type must be defined!");
        }
        this.dataType = dataType;
        this.engineHandler = engineHandler;
        this.script = script;
        this.elements = new CompositeItemElementList(this);
    }

    @Override
    public SELECTIONSTATUS getSelectionStatus() {
        return selected;
    }

    @Override
    public boolean setSelectionStatus(SELECTIONSTATUS newStatus) {
        SELECTIONSTATUS oldStatus = getSelectionStatus();
        if (newStatus == oldStatus) {
            return false;
        }
        checkSelectionStatusChange(oldStatus, newStatus);
        this.selected = newStatus;
        elements.nodeSelectionChanged(oldStatus);
        return true;
    }

    /**
     * Provides the list of item used by the script
     * 
     * @return The list of item used by the script
     */
    public CompositeItemElementList getItemElementList() {
        return elements;
    }

    @Override
    public String getName() {
        return getUserObject().toString();
    }

    /**
     * Sets the node name
     *
     * @param newName The new node name
     */
    public void setName(String newName) {
        setUserObject(newName);
    }

    @Override
    public Class getDataType() {
        return dataType;
    }

    /**
     * Sets a new data type
     *
     * @param newDataType The new data type
     */
    public void setDataType(Class newDataType) {
        if (newDataType == null) {
            throw new IllegalArgumentException("The data type must be defined!");
        }
        dataType = newDataType;
        casting = null;
    }

    /**
     * Provides the object that performs the casting from the script evaluation
     * result to the requested data type
     * 
     * @return The object that performs the casting to the request data type
     */
    public Java2JavaCast getCasting() {
        return casting;
    }

    /**
     * Determines whether or not it is possible to select this node
     * 
     * @return <code>true</code> if this item may be selected,
     *         <code>false</code> otherwise
     */
    public boolean isSelectable() {
        return engineHandler.isEngineAvailable();
    }

    @Override
    public boolean isSelectionStatusEditable() {
        if (Main.opc2outManager.isRunning() || Main.opc.getConnectionStatus() != STATUS.CONNECTED) {
            return false;
        }
        SELECTIONSTATUS s = getSelectionStatus();
        return isSelectable() && (s == SELECTIONSTATUS.UNSELECTED || s == SELECTIONSTATUS.SELECTED);
    }

    @Override
    public boolean isEditable(int column) {
        if (column == OPCTreeTableModel.COLUMN_SAVE) {
            return isSelectionStatusEditable();
        }
        return false;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public boolean isOPCitemProperty() {
        return false;
    }

    @Override
    public List<PersistentOPCItemInfo> getItemsWithSelectedChilds() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<PersistentOPCItemInfo> getAllItemsNProperties() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getValueAt(int column) {
        if (column >= OPCTreeTableModel.COLUMN_COUNT || column < 0) {
            return null;
        }
        switch(column) {
            case OPCTreeTableModel.COLUMN_NAME:
                return getUserObject();
            case OPCTreeTableModel.COLUMN_ID:
                return null;
            case OPCTreeTableModel.COLUMN_TYPE:
                return getDataType();
            case OPCTreeTableModel.COLUMN_VALUE:
                return getValue();
            case OPCTreeTableModel.COLUMN_QUALITY:
            case OPCTreeTableModel.COLUMN_QUALITYLIMIT:
                return getQuality();
            case OPCTreeTableModel.COLUMN_SAVE:
                return getSelectionStatus();
            case OPCTreeTableModel.COLUMN_TIME:
            case OPCTreeTableModel.COLUMN_DATE:
                return getTime();
            case OPCTreeTableModel.COLUMN_READ:
                return isReadable();
            case OPCTreeTableModel.COLUMN_WRITE:
                return isWrittable();
            default:
                return null;
        }
    }

    @Override
    public String getScript() {
        return script;
    }

    /**
     * Defines the script text
     *
     * @param script The new script text
     * @throws ScriptException Thrown when there is a problem checking and
     *                         compiling the script text
     */
    public void setScript(String script) throws ScriptException {
        if (this.script == null || !this.script.equals(script)) {
            this.script = script;
            compiledScript = null;
            if (script != null) {
                compiledScript = engineHandler.checkAndCompileScript(script);
            }
        }
    }

    /**
     * Determines whether or not the script engine is compilable
     *
     * @return <code>true</code> if the engine implements
     *         {@link javax.script.Compilable Compilable}
     */
    public boolean isCompilable() {
        return engineHandler.isCompilable();
    }

    @Override
    public ScriptingEngineHandler getScriptingEngineHandler() {
        return engineHandler;
    }

    /**
     * Sets the scripting engine handler
     * 
     * @param engineHandler The new scripting engine handler
     * @return <code>true</code> if the handler was changed, <code>false</code>
     *         otherwise
     * @throws ScriptException Thrown when there is a problem checking and
     *                         compiling the script text with the new handler
     */
    public boolean setScriptingEngineHandler(ScriptingEngineHandler engineHandler) throws ScriptException {
        if (engineHandler == null) {
            throw new IllegalArgumentException("The scripting engine handler must not be null!");
        }
        if (!this.engineHandler.equals(engineHandler)) {
            this.engineHandler = engineHandler;
            Bindings bindings = null;
            if (engineHandler != null) {
                ScriptEngine engine = engineHandler.getScriptEngine();
                if (engine != null) {
                    bindings = engine.createBindings();
                }
            }
            this.elements.setBindings(bindings);
            compiledScript = null;
            if (script != null && !script.isEmpty()) {
                compiledScript = engineHandler.checkAndCompileScript(script);
            }
            return true;
        }
        return false;
    }

    @Override
    public CompiledScript getCompiledScript() {
        return compiledScript;
    }

    /**
     * Determines whether or not the script is compiled
     * 
     * @return <code>true</code> if the script is compiled, <code>false</code>
     *         otherwise
     */
    public boolean isScriptCompiled() {
        return compiledScript != null;
    }

    /**
     * Sets the compiled script used by this node
     * 
     * @param cScript The new compiled script
     */
    public void setCompiledScript(CompiledScript cScript) {
        this.compiledScript = cScript;
    }

    /**
     * Provides the data result from the script evaluation
     * 
     * @return The data result from the script evaluation (possibly
     *         <code>null</code>)
     */
    public ScriptDataResult getScriptDataResult() {
        return scriptResult;
    }

    /**
     * Sets the data result from the script evaluation
     * 
     * @param result The new data result
     */
    public void setScriptDataResult(ScriptDataResult result) {
        this.scriptResult = result;
    }

    /**
     * Provides a string with the value of the result from the evaluated script.
     * The the value is casted first.
     * 
     * @return The transformed script value
     */
    public String getTransformedValue() {
        if (scriptResult != null) {
            Object value = scriptResult.getValue();
            if (value == null) {
                return "<null>";
            } else {
                if (casting == null || !casting.isValidInputType(value)) {
                    casting = Java2JavaCastFactory.getJava2JavaCast(value.getClass(), getDataType());
                }
                return casting != null ? casting.convertValue(value).toString() : "<Invalid type>";
            }
        }
        return null;
    }

    @Override
    public Object getValue() {
        return getTransformedValue();
    }

    /**
     * Returns the original value evaluated by the script (before casting)
     * 
     * @return The original value evaluated by the script
     */
    public Object getOriginalValue() {
        return scriptResult != null ? scriptResult.getValue() : null;
    }

    @Override
    public Short getQuality() {
        return scriptResult != null ? scriptResult.getQuality() : null;
    }

    @Override
    public Date getTime() {
        return scriptResult != null ? scriptResult.getDate() : null;
    }

    @Override
    public Bindings getBindings() {
        return elements.getBindings();
    }

    /**
     * Evaluates the script
     * 
     * @throws ScriptException
     * @throws JIException
     */
    public void evaluate() throws ScriptException, JIException {
        if (engineHandler.isEngineAvailable()) {
            try {
                Short quality = elements.updateBindings();
                Bindings bindings = getBindings();
                Object value = engineHandler.evaluate(this, bindings);
                Date date = new Date(System.currentTimeMillis());
                scriptResult = new ScriptDataResult(value, date, quality);
            } catch (ScriptException ex) {
                scriptResult = new ScriptDataResult(ex);
            }
        } else {
            scriptResult = null;
        }
    }

    /**
     * Creates a new composite node persistent information object
     * 
     * @return A composite node persistent information object for this node
     */
    public PersistentCompositeNodeInfo getPersistentNodeInfo() {
        Map<String, String> alias2id = new HashMap<String, String>(elements.size());
        for (CompositeNodeItemElement e : elements.getElements()) {
            alias2id.put(e.getAlias(), e.getItemData().getOPCID());
        }
        return new PersistentCompositeNodeInfo(getName(), isSelected(), alias2id, script, engineHandler, dataType);
    }

    /**
     * Creates a composite item that can be used by the backup manager
     * 
     * @return A composite item
     */
    public CompositeItem createCompositeItem() {
        Map<String, String> elementAlias2Id = new HashMap<String, String>(elements.size());
        for (CompositeNodeItemElement e : elements.getElements()) {
            elementAlias2Id.put(e.getAlias(), e.getItemData().getOPCID());
        }
        return new CompositeItem(getName(), elementAlias2Id, script, engineHandler, dataType);
    }

    @Override
    public List<AbstractOPCTreeTableNode> getTreeTableNodes() {
        return Arrays.asList(new AbstractOPCTreeTableNode[] { this });
    }

    @Override
    public String getOPCID() {
        return null;
    }

    @Override
    public boolean isReadable() {
        return true;
    }

    @Override
    public boolean isWrittable() {
        return false;
    }

    /**
     * A comparator used to sort alphabetically a list o composite nodes by
     * their name
     */
    public static class CompositeComparator implements Comparator<CompositeTreeTableNode> {

        @Override
        public int compare(CompositeTreeTableNode o1, CompositeTreeTableNode o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }
}
