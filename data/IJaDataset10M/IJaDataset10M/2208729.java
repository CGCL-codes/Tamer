package org.radeox.macro;

import org.radeox.api.engine.context.InitialRenderContext;
import org.radeox.macro.parameter.MacroParameter;
import java.io.IOException;
import java.io.Writer;

public abstract class BaseMacro implements Macro {

    protected InitialRenderContext initialContext;

    protected String description = " ";

    protected String[] paramDescription = {};

    /**
   * Get the name of the macro. This is used to map a macro
   * in the input to the macro which should be called.
   * The method has to be implemented by subclassing classes.
   *
   * @return name Name of the Macro
   */
    public abstract String getName();

    /**
   * Get a description of the macro. This description explains
   * in a short way what the macro does
   *
   * @return description A string describing the macro
   */
    public String getDescription() {
        return description;
    }

    /**
   * Get a description of the paramters of the macro. The method
   * returns an array with an String entry for every parameter.
   * The format is {"1: description", ...} where 1 is the position
   * of the parameter.
   *
   * @return description Array describing the parameters of the macro
   */
    public String[] getParamDescription() {
        return paramDescription;
    }

    public void setInitialContext(InitialRenderContext context) {
        this.initialContext = context;
    }

    /**
   * Execute the macro. This method is called by MacroFilter to
   * handle macros.
   *
   * @param writer A write where the macro should write its output to
   * @param params Macro parameters with the parameters the macro is called with
   */
    public abstract void execute(Writer writer, MacroParameter params) throws IllegalArgumentException, IOException;

    public String toString() {
        return getName();
    }

    public int compareTo(Object object) {
        Macro macro = (Macro) object;
        return getName().compareTo(macro.getName());
    }
}
