package end2end.jcvi.autoTasker.flu.projectDb;

import java.io.File;
import java.io.IOException;
import org.jcvi.common.io.fileServer.ResourceFileServer;
import end2end.jcvi.autoTasker.flu.AbstractEndToEndFluTest;

public abstract class AbstractProjectDbEndtoEndFluTest extends AbstractEndToEndFluTest {

    protected final ResourceFileServer resources = new ResourceFileServer(AbstractProjectDbEndtoEndFluTest.class);

    @Override
    protected String[] generateCommandOptions() throws IOException {
        return new String[] { "-D", getProjectDb(), "-sample", getSampleId(), "-txt", txtOutFile.getAbsolutePath(), "-nav", navOutFile.getAbsolutePath(), "-ncbi", ncbiDraftOutFile.getAbsolutePath(), "-flu" };
    }

    @Override
    protected File getExpectedAutoTaskerTxtOutput() throws IOException {
        return resources.getFile("files/autoTasker2." + getFluAceFileName() + ".txt");
    }

    @Override
    protected File getExpectedAutoTaskerNavOutput() throws IOException {
        return resources.getFile("files/autoTasker2." + getFluAceFileName() + ".nav");
    }

    @Override
    protected File getExpectedAutoTaskerNcbiDraftSubmissionOutput() throws IOException {
        return resources.getFile("files/autoTasker2." + getFluAceFileName() + ".ncbi_submission");
    }
}
