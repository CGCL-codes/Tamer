package com.dfamaj.textproc.xwm.primitives;

import com.dfamaj.textproc.xwm.Location;
import com.dfamaj.textproc.xwm.Token;
import com.dfamaj.textproc.xwm.TokenImpl;
import com.dfamaj.textproc.xwm.Tokenizer;
import com.dfamaj.textproc.xwm.XwmEngine;
import com.dfamaj.textproc.xwm.XwmMacroDefinition;
import com.dfamaj.textproc.xwm.exceptions.IllegalSyntaxException;
import com.dfamaj.textproc.xwm.exceptions.XwmEngineException;
import org.apache.commons.lang.NullArgumentException;
import java.io.IOException;
import java.io.Serializable;

/**
 * <div lang="fr"> la primitive "<tt>\ifvoid</tt>" </div>
 *
 * @author <a href="mailto:david.andriana@free.fr">David Andriana</a>
 * @version 2.0 -- 2007-02-04 -- $Revision$
 * @since 2.0
 */
public final class IfvoidPrimitive extends AbstractConditionPrimitive implements Serializable {

    /**
	 * <div lang="fr"> la version de la classe, pour les sérialisations. </div>
	 */
    private static final long serialVersionUID = 20070204L;

    /**
	 * <div lang="fr"> constructeur privé. </div>
	 */
    private IfvoidPrimitive() {
    }

    /**
	 * <div lang="fr"> renvoie une instance de la primitive. </div>
	 *
	 * @notNull
	 */
    public static XwmPrimitive getInstance() {
        return new IfvoidPrimitive();
    }

    /**
	 * <div lang="fr"> traite la primitive "<tt>\ifvoid</tt>". </div>
	 *
	 * @notNull primitive
	 * @notNull engine
	 */
    public Tokenizer handlePrimitive(final String primitive, final XwmEngine engine) throws IOException, XwmEngineException {
        if (primitive == null) {
            throw new NullArgumentException("primitive");
        }
        if (engine == null) {
            throw new NullArgumentException("engine");
        }
        final Location originalLocation = engine.getLocation();
        while (true) {
            final Token token = engine.innerNextNonVoidToken(primitive, originalLocation);
            if (TokenImpl.isOpenBrace(token)) {
                break;
            } else if (!TokenImpl.isWhiteSpace(token)) {
                throw new IllegalSyntaxException(primitive, originalLocation, token.getText());
            }
        }
        int level = 0;
        boolean isVoidCondition = true;
        while (true) {
            final Token token = engine.innerNextToken();
            if (TokenImpl.isCloseBrace(token)) {
                if (level == 0) {
                    break;
                } else {
                    --level;
                }
            } else if (TokenImpl.isOpenBrace(token)) {
                ++level;
            }
            if (isVoidCondition && !TokenImpl.isWhiteSpace(token)) {
                isVoidCondition = false;
            }
        }
        return handleAction(primitive, engine, isVoidCondition);
    }

    /**
	 * <div lang="fr"> renvoie <tt>true</tt> si la macro passée en paramètre
	 * est la primitive "<tt>\ifvoid</tt>". </div>
	 *
	 * @notNull macroDefinition
	 */
    public static boolean isPrimitive(final XwmMacroDefinition macroDefinition) {
        if (macroDefinition == null) {
            throw new NullArgumentException("macroDefinition");
        }
        return IfvoidPrimitive.class.isInstance(macroDefinition);
    }
}
