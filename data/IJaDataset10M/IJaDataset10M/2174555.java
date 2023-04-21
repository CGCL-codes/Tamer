package org.eclipse.xtext.example.fj.ui.tests;

import org.eclipse.xtext.example.FJStandaloneSetup;
import org.eclipse.xtext.example.ui.FJUiModule;
import org.eclipse.xtext.example.ui.internal.FJActivator;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.xtext.ISetup;
import org.eclipse.xtext.ui.service.UIPluginModule;
import org.eclipse.xtext.ui.junit.editor.contentassist.AbstractContentAssistProcessorTest;
import org.eclipse.xtext.ui.junit.editor.contentassist.ContentAssistProcessorTestBuilder;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * @author bettini
 * 
 */
public class ContentAssistProposalTest extends AbstractContentAssistProcessorTest {

    public ISetup getFJTestLanguageSetup() {
        return new FJStandaloneSetup() {

            @Override
            public Injector createInjector() {
                return Guice.createInjector(new FJUiModule(), new UIPluginModule(FJActivator.getInstance()));
            }
        };
    }

    /**
	 * Checks proposals when going at the beginning of the program
	 * 
	 * @throws Exception
	 */
    public void testInitialProposal() throws Exception {
        newBuilder(getFJTestLanguageSetup()).append("class B {}").assertTextAtCursorPosition(0, "(", "class", "class definition - a standard class definition", "new", "this");
    }

    /**
	 * checks proposals at the end of the program
	 * 
	 * @throws Exception
	 */
    public void testEndProposal() throws Exception {
        newBuilder(getFJTestLanguageSetup()).append("class A {}").assertText("(", "class", "class definition - a standard class definition", "new", "this", "}");
    }

    /**
	 * checks proposals for a new
	 * 
	 * @throws Exception
	 */
    public void testNewProposal() throws Exception {
        newBuilder(getFJTestLanguageSetup()).append("class D {} new ").assertText("Object", "D");
    }

    /**
	 * Proposals for method invocation
	 * 
	 * @throws Exception
	 */
    public void testMethodCallProposal() throws Exception {
        newBuilder(getFJTestLanguageSetup()).append("class A { A clone(A a) { return this; } A m(A a, Object b) { return this; }} new A().").assertText("clone", "m", ".");
    }

    /**
	 * Proposals for cast (which also considers '(' as an opening expression
	 * parenthesis). This did not work with xtext versions previous than 0.8.0
	 * M3
	 * 
	 * @throws Exception
	 */
    public void testCastProposal() throws Exception {
        newBuilder(getFJTestLanguageSetup()).append("class A { } class B { } (").assertText("A", "B", "Object", "new", "this", "(");
    }

    /**
	 * Display proposals for method invocation
	 * 
	 * @throws Exception
	 */
    public void testMethodCallProposalDisplayString() throws Exception {
        String modelAsString = "class A { A clone(A a) { return this; } A m(A a, Object b) { return this; }} new A().";
        ContentAssistProcessorTestBuilder builder = newBuilder(getFJTestLanguageSetup());
        ICompletionProposal[] proposals = builder.computeCompletionProposals(modelAsString, modelAsString.length());
        for (ICompletionProposal iCompletionProposal : proposals) {
            System.out.println("proposal: " + iCompletionProposal.getDisplayString() + ", " + iCompletionProposal.getAdditionalProposalInfo());
        }
        assertEquals("clone(A) : A", proposals[0].getDisplayString());
        assertEquals("m(A, Object) : A", proposals[1].getDisplayString());
    }
}
