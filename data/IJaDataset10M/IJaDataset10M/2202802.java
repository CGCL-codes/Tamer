package br.com.sysmap.crux.core.rebind;

import br.com.sysmap.crux.core.i18n.MessagesFactory;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;

/**
 * @author Thiago da Rosa de Bustamante
 *
 */
public abstract class AbstractGenerator extends Generator {

    protected static GeneratorMessages messages = (GeneratorMessages) MessagesFactory.getMessages(GeneratorMessages.class);

    @Override
    public String generate(TreeLogger logger, GeneratorContext ctx, String requestedClass) throws UnableToCompleteException {
        TypeOracle typeOracle = ctx.getTypeOracle();
        assert (typeOracle != null);
        JClassType baseIntf = typeOracle.findType(requestedClass);
        if (baseIntf == null) {
            logger.log(TreeLogger.ERROR, messages.generatorSourceNotFound(requestedClass), null);
            throw new UnableToCompleteException();
        }
        return createProxy(logger, ctx, baseIntf).create();
    }

    /**
	 * @param logger
	 * @param ctx
	 * @param baseIntf
	 * @return
	 * @throws UnableToCompleteException 
	 */
    protected abstract AbstractProxyCreator createProxy(TreeLogger logger, GeneratorContext ctx, JClassType baseIntf) throws UnableToCompleteException;
}
