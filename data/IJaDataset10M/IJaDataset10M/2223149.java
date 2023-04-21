package org.colombbus.tangara.ide.model.codeeditor;

import java.io.File;
import java.nio.charset.Charset;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.EventListenerList;
import org.apache.commons.lang.Validate;
import org.colombbus.tangara.core.Version;
import org.colombbus.tangara.ide.model.DefaultScriptHeader;
import org.colombbus.tangara.io.ScriptHeader;
import org.colombbus.tangara.util.bundle.TypedResourceBundle;

/**
 * Default {@link CodeEditor} implementation.
 *
 * @author Aurelien Bourdon <aurelien.bourdon@gmail.com>
 */
public class DefaultCodeEditor implements CodeEditor {

    /** {@link TypedResourceBundle} for internationalized messages */
    private static final TypedResourceBundle RESOURCE_BUNDLE = Messages.RESOURCE_BUNDLE;

    /** Code editor's script name */
    private String name;

    /** Code editor's absolute pathe name */
    private String absolutePathName;

    /** Code editor script's {@link ScriptHeader} */
    private ScriptHeader scriptHeader;

    /** Code editor's script */
    private String script;

    /** If {@link CodeEditor} is needed to be save */
    private boolean neededSave;

    /** The list of ChangeListeners for this model */
    private EventListenerList listenerList = new EventListenerList();

    public DefaultCodeEditor() {
        Charset charset = Charset.forName(RESOURCE_BUNDLE.getString("CodeEditor.defaultCharset"));
        Version version = new Version(RESOURCE_BUNDLE.getString("CodeEditor.defaultVersion"));
        scriptHeader = new DefaultScriptHeader(charset, version);
    }

    @Override
    public String getAbsolutePathName() throws IllegalStateException {
        if (absolutePathName == null) throw new IllegalStateException("absolute path has not been initialized");
        return absolutePathName;
    }

    @Override
    public String getName() throws IllegalStateException {
        if (name == null) throw new IllegalStateException("file name has not been initialized");
        return name;
    }

    @Override
    public void setName(String name) throws IllegalArgumentException {
        Validate.notNull(name, "fileName argument is null");
        this.name = name;
    }

    @Override
    public String getScript() throws IllegalStateException {
        if (script == null) throw new IllegalStateException("script has not been initialized");
        return script;
    }

    /**
	 * Set absolute path name change also the script name. It will be the end of
	 * the absolute path name.
	 */
    @Override
    public void setAbsolutePathName(String absolutePathName) throws IllegalArgumentException {
        Validate.notNull(absolutePathName, "absolutePathName argument is null");
        this.absolutePathName = absolutePathName;
        int lastIndexOf = absolutePathName.lastIndexOf(File.separator);
        if (lastIndexOf != -1) name = absolutePathName.substring(lastIndexOf + 1, absolutePathName.length()); else name = absolutePathName;
    }

    @Override
    public void setScript(String script) throws IllegalArgumentException {
        Validate.notNull(script, "script argument is null");
        this.script = script;
    }

    @Override
    public ScriptHeader getScriptHeader() throws IllegalStateException {
        if (scriptHeader == null) throw new IllegalStateException("script header has not been initialized");
        return scriptHeader;
    }

    @Override
    public void setScriptHeader(ScriptHeader scriptHeader) throws IllegalArgumentException {
        Validate.notNull(scriptHeader, "scriptHeader argument is null");
        this.scriptHeader = scriptHeader;
    }

    @Override
    public boolean isNeededSave() {
        return neededSave;
    }

    public void setNeededSave(boolean neededSave) {
        this.neededSave = neededSave;
    }

    @Override
    public void textInserted(DocumentEvent documentEvt) throws IllegalArgumentException {
        Validate.notNull(documentEvt, "documentEvt argument is null");
        if (!isNeededSave()) setNeededSave(true);
        fireStateChanged();
    }

    @Override
    public void textRemoved(DocumentEvent documentEvt) throws IllegalArgumentException {
        Validate.notNull(documentEvt, "documentEvt argument is null");
        if (!isNeededSave()) setNeededSave(true);
        fireStateChanged();
    }

    @Override
    public void addChangeListener(ChangeListener listener) {
        Validate.notNull(listener, "listener argument is null");
        listenerList.add(ChangeListener.class, listener);
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
        Validate.notNull(listener, "listener argument is null");
        listenerList.remove(ChangeListener.class, listener);
    }

    /**
	 * @return an array of all the <code>ChangeListener</code>s added to this
     * {@link DefaultCodeEditor} with addChangeListener().
	 */
    public ChangeListener[] getChangeListeners() {
        return listenerList.getListeners(ChangeListener.class);
    }

    /**
	 * Run each <code>ChangeListener</code>s <code>stateChanged()</code> method.
	 */
    public void fireStateChanged() {
        ChangeListener[] changeListenerList = getChangeListeners();
        for (ChangeListener changeListener : changeListenerList) changeListener.stateChanged(new ChangeEvent(this));
    }
}
