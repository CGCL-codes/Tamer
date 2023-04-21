package br.ufpe.cin.ontocompo.module.owlrender;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import br.ufpe.cin.ontocompo.module.exception.ModuleRendererException;
import br.ufpe.cin.ontocompo.module.exception.UnknownModuleException;
import br.ufpe.cin.ontocompo.module.model.Module;
import br.ufpe.cin.ontocompo.module.model.OWLModuleManager;

/**
 * Class AbstractOWLModuleRenderer.java
 * Abstract class to render a module object
 * 
 * @author Camila Bezerra (kemylle@gmail.com)
 * @date Jul 18, 2008
 */
public abstract class AbstractOWLModuleRenderer {

    /** The owl module manager. */
    private OWLModuleManager owlModuleManager;

    /**
     * Instantiates a new abstract owl module renderer.
     * 
     * @param owlModuleManager the owl module manager
     */
    protected AbstractOWLModuleRenderer(OWLModuleManager owlModuleManager) {
        this.owlModuleManager = owlModuleManager;
    }

    /**
    * Sets the oWL module manager.
    * 
    * @param owlModuleManager the new oWL module manager
    */
    public void setOWLModuleManager(OWLModuleManager owlModuleManager) {
        this.owlModuleManager = owlModuleManager;
    }

    /**
     * Gets the oWL module manager.
     * 
     * @return the oWL module manager
     */
    protected OWLModuleManager getOWLModuleManager() {
        return owlModuleManager;
    }

    /**
     * Render.
     * 
     * @param module the module
     * @param os the os
     * 
     * @throws ModuleRendererException the module renderer exception
     * @throws UnknownModuleException the unknown module exception
     */
    public void render(Module module, OutputStream os) throws ModuleRendererException, UnknownModuleException {
        try {
            Writer writer = new BufferedWriter(new OutputStreamWriter(os));
            render(module, writer);
            writer.flush();
        } catch (IOException e) {
            throw new ModuleRendererException(e);
        }
    }

    /**
     * Renders the specified module using the specified writer.
     * 
     * @param writer The writer that should be used to write the module.
     * Note that this writer need not be wrapped with a <code>BufferedWriter</code>
     * because this is taken care of by this abstract implementation.
     * @param module the module
     * 
     * @throws UnknownModuleException the unknown module exception
     * @throws ModuleRendererException the module renderer exception
     */
    public abstract void render(Module module, Writer writer) throws ModuleRendererException, UnknownModuleException;
}
