package com.aptana.ide.editor.js.runtime;

import com.aptana.ide.editors.managers.FileContextManager;
import com.aptana.ide.lexer.IRange;
import com.aptana.ide.lexer.Range;

/**
 * @author Kevin Lindsey
 * @author Robin Debreuil
 */
public class JSDateConstructor extends NativeConstructorBase {

    /**
	 * Create a new instance of NativeDate
	 * 
	 * @param owningEnvironment
	 *            The environment in which this native date was created
	 */
    public JSDateConstructor(Environment owningEnvironment) {
        super(owningEnvironment);
        int fileIndex = FileContextManager.BUILT_IN_FILE_INDEX;
        int offset = Range.Empty.getStartingOffset();
        IScope global = owningEnvironment.getGlobal();
        global.putPropertyValue("Date", this, fileIndex, Property.DONT_DELETE | Property.DONT_ENUM);
        IObject function = global.getPropertyValue("Function", fileIndex, offset);
        IObject functionPrototype = function.getPropertyValue("prototype", fileIndex, offset);
        this.setPrototype(functionPrototype);
        IObject prototype = owningEnvironment.createObject(fileIndex, Range.Empty);
        this.putPropertyValue("prototype", prototype, fileIndex);
    }

    /**
	 * Initialize the properties on this instance
	 */
    public void initializeProperties() {
        Environment environment = this.owningEnvironment;
        int attributes = Property.DONT_DELETE | Property.DONT_ENUM;
        int fileIndex = FileContextManager.BUILT_IN_FILE_INDEX;
        int offset = Range.Empty.getStartingOffset();
        this.putPropertyValue("length", environment.createNumber(fileIndex, Range.Empty), fileIndex, attributes);
        this.putPropertyValue("parse", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        this.putPropertyValue("UTC", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        IObject prototype = this.getPropertyValue("prototype", fileIndex, offset);
        prototype.putPropertyValue("constructor", this, fileIndex, attributes);
        prototype.putPropertyValue("toString", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("toDateString", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("toTimeString", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("toLocaleString", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("toLocaleDateString", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("toLocaleTimeString", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("valueOf", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("getTime", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("getFullYear", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("getUTCFullYear", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("getMonth", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("getUTCMonth", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("getDate", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("getUTCDate", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("getDay", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("getUTCDay", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("getHours", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("getUTCHours", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("getMinutes", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("getUTCMinutes", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("getSeconds", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("getUTCSeconds", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("getMilliseconds", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("getUTCMilliseconds", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("getTimezoneOffset", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("setTime", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("setMilliseconds", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("setUTCMilliseconds", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("setSeconds", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("setUTCSeconds", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("setMinutes", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("setUTCMinutes", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("setHours", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("setUTCHours", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("setDate", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("setUTCDate", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("setMonth", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("setUTCMonth", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("setFullYear", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("setUTCFullYear", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
        prototype.putPropertyValue("toUTCString", environment.createFunction(fileIndex, Range.Empty), fileIndex, attributes);
    }

    /**
	 * @see com.aptana.ide.editor.js.runtime.FunctionBase#construct(com.aptana.ide.editor.js.runtime.Environment, com.aptana.ide.editor.js.runtime.IObject[], int,
	 *      IRange)
	 */
    public IObject construct(Environment environment, IObject[] arguments, int fileIndex, IRange sourceRegion) {
        JSDate instance = new JSDate(sourceRegion);
        instance.setPrototype(this.getPropertyValue("prototype", fileIndex, sourceRegion.getStartingOffset()));
        return instance;
    }

    /**
	 * @see com.aptana.ide.editor.js.runtime.FunctionBase#invoke(com.aptana.ide.editor.js.runtime.Environment, com.aptana.ide.editor.js.runtime.IObject[], int, IRange)
	 */
    public IObject invoke(Environment environment, IObject[] arguments, int fileIndex, IRange sourceRegion) {
        return environment.createDate(fileIndex, sourceRegion);
    }
}
