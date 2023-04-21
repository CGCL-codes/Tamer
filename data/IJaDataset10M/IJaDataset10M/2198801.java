package org.antlride.internal.unit.ui.text.completion;

import org.antlride.internal.ui.text.completion.CompletionProposalCollector;
import org.antlride.internal.ui.text.completion.CompletionProposalComputer;
import org.antlride.internal.ui.text.templates.TemplateCompletionProcessor;
import org.easymock.EasyMock;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.ui.text.completion.ScriptContentAssistInvocationContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Unit test for {@link CompletionProposalCollector}
 * 
 * @author Edgar Espina
 * @since 2.1.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ CompletionProposalComputer.class, CompletionProposalCollector.class, TemplateCompletionProcessor.class })
public class CompletionProposalComputerTest {

    @Test
    public void createCollector() throws Exception {
        ScriptContentAssistInvocationContext context = EasyMock.createMock(ScriptContentAssistInvocationContext.class);
        ISourceModule sourceModule = EasyMock.createMock(ISourceModule.class);
        CompletionProposalCollector collector = PowerMock.createMockAndExpectNew(CompletionProposalCollector.class, sourceModule);
        EasyMock.expect(context.getSourceModule()).andReturn(sourceModule);
        EasyMock.replay(context, sourceModule);
        PowerMock.replay(collector, CompletionProposalCollector.class);
        new CompletionProposalComputer().createCollector(context);
        EasyMock.verify(context, sourceModule);
        PowerMock.verify(collector, CompletionProposalCollector.class);
    }

    @Test
    public void createTemplateProposalComputer() throws Exception {
        ScriptContentAssistInvocationContext context = EasyMock.createMock(ScriptContentAssistInvocationContext.class);
        TemplateCompletionProcessor templateProcessor = PowerMock.createMockAndExpectNew(TemplateCompletionProcessor.class, context);
        EasyMock.replay(context);
        PowerMock.replay(templateProcessor, TemplateCompletionProcessor.class);
        new CompletionProposalComputer().createTemplateProposalComputer(context);
        EasyMock.verify(context);
        PowerMock.verify(templateProcessor, TemplateCompletionProcessor.class);
    }
}
