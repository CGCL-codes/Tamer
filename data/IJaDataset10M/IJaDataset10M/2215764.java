package com.google.gwt.soyc.io;

import com.google.gwt.core.ext.linker.EmittedArtifact.Visibility;
import com.google.gwt.core.ext.linker.SyntheticArtifact;
import com.google.gwt.core.linker.SoycReportLinker;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * An {@link OutputDirectory} that writes its output as a list of GWT compiler
 * artifacts.
 */
public class ArtifactsOutputDirectory implements OutputDirectory {

    public static final String COMPILE_REPORT_DIRECTORY = "compile-report";

    /**
   * An in-memory output stream. When it is closed, its contents are saved to an
   * artifact.
   */
    private class OutputStreamForArtifact extends OutputStream {

        private ByteArrayOutputStream baos = new ByteArrayOutputStream();

        private boolean closed = false;

        private final String path;

        public OutputStreamForArtifact(String path) {
            this.path = path;
        }

        @Override
        public void close() {
            if (!closed) {
                closed = true;
                SyntheticArtifact newArtifact = new SyntheticArtifact(SoycReportLinker.class, COMPILE_REPORT_DIRECTORY + "/" + path, baos.toByteArray());
                newArtifact.setVisibility(Visibility.Private);
                artifacts.add(newArtifact);
                baos = null;
            }
        }

        @Override
        public void write(byte b[]) throws IOException {
            baos.write(b);
        }

        @Override
        public void write(byte b[], int off, int len) throws IOException {
            baos.write(b, off, len);
        }

        @Override
        public void write(int b) throws IOException {
            baos.write(b);
        }
    }

    private List<SyntheticArtifact> artifacts = new ArrayList<SyntheticArtifact>();

    /**
   * Return the list of artifacts that have been written so far.
   */
    public List<SyntheticArtifact> getArtifacts() {
        return artifacts;
    }

    public OutputStream getOutputStream(String path) throws IOException {
        return new OutputStreamForArtifact(path);
    }
}
