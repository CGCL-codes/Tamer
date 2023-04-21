package org.jactr.tools.tracer.transformer.procedural;

import org.antlr.runtime.tree.CommonTree;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jactr.core.event.IACTREvent;
import org.jactr.core.module.procedural.event.ProceduralModuleEvent;
import org.jactr.core.production.IProduction;
import org.jactr.io.antlr3.builder.JACTRBuilder;
import org.jactr.io.antlr3.misc.ASTSupport;
import org.jactr.io.resolver.ASTResolver;
import org.jactr.tools.tracer.transformer.IEventTransformer;
import org.jactr.tools.tracer.transformer.ITransformedEvent;

/**
 * @author developer
 */
public class ProceduralModuleEventTransformer implements IEventTransformer {

    /**
   * logger definition
   */
    private static final Log LOGGER = LogFactory.getLog(ProceduralModuleEventTransformer.class);

    /**
   * @see org.jactr.tools.tracer.transformer.IEventTransformer#transform(org.jactr.core.event.IACTREvent)
   */
    public ITransformedEvent transform(IACTREvent actrEvent) {
        ProceduralModuleEvent pme = (ProceduralModuleEvent) actrEvent;
        ProceduralModuleEvent.Type type = pme.getType();
        String modelName = pme.getSource().getModel().getName();
        long actualTime = pme.getSystemTime();
        double simTime = pme.getSimulationTime();
        CommonTree data = null;
        switch(type) {
            case PARAMETER_CHANGED:
                if (LOGGER.isWarnEnabled()) LOGGER.warn("We are ignoring parameter events");
                break;
            case PRODUCTION_CREATED:
                if (LOGGER.isDebugEnabled()) LOGGER.debug("Ignoring production creation");
                break;
            case PRODUCTION_FIRED:
            case PRODUCTION_WILL_FIRE:
            case PRODUCTION_ADDED:
                data = ASTResolver.toAST(pme.getProduction());
                if (LOGGER.isDebugEnabled()) LOGGER.debug("Transformed production " + pme.getProduction() + " into " + data);
                break;
            case PRODUCTIONS_MERGED:
                if (LOGGER.isWarnEnabled()) LOGGER.warn("We are ignoring production merges");
                break;
            case CONFLICT_SET_ASSEMBLED:
                ASTSupport support = new ASTSupport();
                data = support.createTree(JACTRBuilder.PROCEDURAL_MEMORY, "conflict set");
                for (IProduction instantiation : pme.getProductions()) data.addChild(ASTResolver.toAST(instantiation));
                if (LOGGER.isDebugEnabled()) LOGGER.debug("conflict set " + pme.getProductions() + " transformed into " + data);
                break;
        }
        return new TransformedProceduralEvent(modelName, actualTime, simTime, type, data);
    }
}
