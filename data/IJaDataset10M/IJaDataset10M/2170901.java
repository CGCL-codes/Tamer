package nzdis.agent.fipa.ontology.management;

import java.io.IOException;
import nzdis.lang.sl.lexer.LexerException;
import nzdis.lang.sl.parser.ParserException;
import nzdis.lang.sl.node.Node;
import nzdis.lang.sl.tool.TreeBuilder;
import nzdis.simplesl.Content;
import nzdis.simplesl.ActionExpression;
import nzdis.simplesl.FunctionalTerm;
import nzdis.simplesl.fipasl.SyntaxConverter;
import nzdis.simplesl.linker.Linker;
import nzdis.simplesl.linker.OntologyManager;

/**
 * Utility class for handling DF and AMS search actions.
 *
 *<br><br>
 * SearchActionHelper.java<br>
 * Created: Mon Feb 10 16:05:14 2003<br>
 *
 * @author  <a href="mariusz@rakiura.org">Mariusz Nowostawski</a>
 * @author Marcos Oliveira
 * @version $Revision: 1.3 $ $Date: 2003/02/12 03:05:14 $
 */
public class SearchActionHelper {

    AMSAgentDescription amsAgentDescription = null;

    DFAgentDescription dfAgentDescription = null;

    SearchConstraints searchConstraints;

    /**
   1. Convert sl0 to AST of SL
   2. Convert AST of SL to SimpleSL AST
   3. Link all the terms into proper Java Objects
   4. Traverse the SimpleSL AST for df- or ams- search
   5. Extract search constraints
     */
    public SearchActionHelper(final String input) throws LexerException, ParserException, IOException, UnsupportedOperationException {
        final Node node = TreeBuilder.getNode(input);
        final SyntaxConverter conv = new SyntaxConverter();
        node.apply(conv);
        final Content rawContent = conv.getContent();
        OntologyManager manager = new OntologyManager();
        manager.register("nzdis.agent.fipa.ontology.management");
        manager.register("nzdis.simplesl.standard");
        final Linker linker = manager.getLinker();
        final Content content = linker.compile(rawContent);
        processContent(content);
    }

    public boolean isDFAgentDescription() {
        return this.dfAgentDescription != null;
    }

    public boolean isAMSAgentDescription() {
        return this.amsAgentDescription != null;
    }

    public DFAgentDescription getDFAgentDescription() {
        return this.dfAgentDescription;
    }

    public AMSAgentDescription getAMSAgentDescription() {
        return this.amsAgentDescription;
    }

    public SearchConstraints getSearchConstraints() {
        return this.searchConstraints;
    }

    private void processContent(Content content) throws UnsupportedOperationException {
        final ActionExpression actionTerm = (ActionExpression) content.getContentExpressions()[0];
        final FunctionalTerm searchTerm = (FunctionalTerm) actionTerm.getTerm();
        if (!searchTerm.getSymbol().getValue().trim().equals("search")) throw new UnsupportedOperationException("ACTION NOT a SEARCH\nContent: " + content.toString());
        final FunctionalTerm agentDescription = (FunctionalTerm) searchTerm.getTerms()[0];
        if (agentDescription instanceof DFAgentDescription) {
            this.dfAgentDescription = (DFAgentDescription) agentDescription;
        } else if (agentDescription instanceof AMSAgentDescription) {
            this.amsAgentDescription = (AMSAgentDescription) agentDescription;
        } else {
            throw new UnsupportedOperationException("AgentDescription screwed up!\nContent: " + content.toString());
        }
        System.out.println(searchTerm.getTerms()[1].getClass());
        final FunctionalTerm searchConstraintsTerm = (FunctionalTerm) searchTerm.getTerms()[1];
        if (searchConstraintsTerm instanceof SearchConstraints) {
            this.searchConstraints = (SearchConstraints) searchConstraintsTerm;
        } else {
            throw new UnsupportedOperationException("Search constraints screwed up!\nContent: " + content.toString());
        }
    }

    /**
    * Just testing.  This is an example of how to use this helper
    * to parse and process AgentCities-like DF and AMS search action queries.
    */
    public static void main(final String[] args) throws Exception {
        SearchActionHelper h = new SearchActionHelper("((action (agent-identifier  :name df@OtagoAgentCities :addresses (sequence http://waitaki.otago.ac.nz:2020/agent_9 ) ) (search (df-agent-description ) (search-constraints  :max-results 100 :max-depth 100 ) ) ) )");
        System.out.println(h.isAMSAgentDescription());
        System.out.println(h.isDFAgentDescription());
        System.out.println(h.getAMSAgentDescription());
        System.out.println(h.getSearchConstraints());
        if (h.getSearchConstraints().getMaxDepth() != null) System.out.println(h.getSearchConstraints().getMaxDepth().longValue() + "");
        if (h.getSearchConstraints().getMaxResults() != null) System.out.println(h.getSearchConstraints().getMaxResults().longValue() + "");
    }
}
