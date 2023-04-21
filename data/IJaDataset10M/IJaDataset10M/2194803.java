package ru.ksu.niimm.cll.mocassin.crawl.parser.gate;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.google.inject.Inject;
import com.mycila.testing.junit.MycilaJunitRunner;
import com.mycila.testing.plugin.guice.GuiceContext;

@RunWith(MycilaJunitRunner.class)
@GuiceContext({ GateModule.class })
public class GateProcessingFacadeTest {

    @Inject
    private GateProcessingFacade gateProcessingFacade;

    @Test
    public void testProcess() throws AccessGateDocumentException, AccessGateStorageException, ProcessException {
        gateProcessingFacade.process("ivm18");
        gateProcessingFacade.process("ivm537");
    }
}
