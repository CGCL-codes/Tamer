package org.antlride.core.parser;

import org.antlride.core.CorePlugin;
import org.antlride.core.model.Grammar;
import org.antlride.internal.core.model.statement.ModelElementFactoryImpl;
import org.junit.Assert;

/**
 * Utility test class.
 * 
 * @author Edgar Espina
 * @since 2.1.0
 */
public abstract class SourceParserTest {

    protected Grammar parse(String source) {
        CorePlugin plugin = CorePlugin.getInstance();
        SourceParser sourceParser = plugin.getService(SourceParser.class);
        Grammar grammar = sourceParser.parse(source, new ModelElementFactoryImpl());
        Assert.assertNotNull(grammar);
        return grammar;
    }
}
