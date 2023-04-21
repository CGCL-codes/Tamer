package de.knowwe.core.kdom.objects;

import java.util.ArrayList;
import java.util.Collection;
import de.knowwe.core.compile.Priority;
import de.knowwe.core.compile.TerminologyHandler;
import de.knowwe.core.kdom.KnowWEArticle;
import de.knowwe.core.kdom.parsing.Section;
import de.knowwe.core.kdom.subtreeHandler.SubtreeHandler;
import de.knowwe.core.report.Message;
import de.knowwe.core.report.Messages;
import de.knowwe.core.utils.KnowWEUtils;

/**
 * 
 * @author Albrecht
 * @created 16.12.2010
 */
public abstract class StringDefinition extends TermDefinition<String> {

    public StringDefinition() {
        super(String.class);
        this.addSubtreeHandler(Priority.HIGHER, new StringDefinitionRegistrationHandler());
    }

    /**
	 * 
	 * This handler registers this Term..
	 * 
	 * @author Jochen, Albrecht
	 * @created 08.10.2010
	 */
    static class StringDefinitionRegistrationHandler extends SubtreeHandler<StringDefinition> {

        @Override
        public Collection<Message> create(KnowWEArticle article, Section<StringDefinition> s) {
            TerminologyHandler tHandler = KnowWEUtils.getTerminologyHandler(article.getWeb());
            tHandler.registerTermDefinition(article, s);
            Section<? extends TermDefinition<String>> defSec = tHandler.getTermDefiningSection(article, s);
            if (defSec != s) {
                return Messages.asList(Messages.objectAlreadyDefinedError(s.get().getName() + ": " + s.get().getTermIdentifier(s), s));
            }
            s.get().storeTermObject(article, s, s.get().getTermIdentifier(s));
            return new ArrayList<Message>(0);
        }

        @Override
        public void destroy(KnowWEArticle article, Section<StringDefinition> s) {
            KnowWEUtils.getTerminologyHandler(article.getWeb()).unregisterTermDefinition(article, s);
        }
    }
}
