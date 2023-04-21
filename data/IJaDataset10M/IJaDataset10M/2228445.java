package de.walware.statet.r.internal.debug.ui.launchconfigs;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import de.walware.eclipsecommons.ICommonStatusConstants;
import de.walware.statet.nico.core.runtime.IToolRunnable;
import de.walware.statet.nico.core.runtime.IToolRunnableControllerAdapter;
import de.walware.statet.nico.core.runtime.Prompt;
import de.walware.statet.nico.core.runtime.SubmitType;
import de.walware.statet.nico.core.runtime.ToolProcess;
import de.walware.statet.r.internal.ui.RUIPlugin;
import de.walware.statet.r.nico.AbstractRController;
import de.walware.statet.r.nico.BasicR;
import de.walware.statet.r.nico.IBasicRAdapter;
import de.walware.statet.r.nico.ISetupRAdapter;
import de.walware.statet.r.nico.IncompleteInputPrompt;
import de.walware.statet.r.nico.RWorkspace;
import de.walware.statet.r.nico.ui.tools.RQuitRunnable;
import de.walware.statet.r.ui.RUI;

public class RTermController extends AbstractRController<IBasicRAdapter, RWorkspace> {

    private class RTermAdapter extends AbstractRAdapter implements IBasicRAdapter, ISetupRAdapter, IAdaptable {

        @Override
        protected void doBeforeSubmit(final String input) {
            final SubmitType type = fCurrentRunnable.getSubmitType();
            try {
                fProcessOutputThread.streamLock.lock();
                fInputStream.append(input, type, (fPrompt.meta & IToolRunnableControllerAdapter.META_HISTORY_DONTADD));
                fInputStream.append(fWorkspaceData.getLineSeparator(), type, IToolRunnableControllerAdapter.META_HISTORY_DONTADD);
            } finally {
                fProcessOutputThread.streamLock.unlock();
            }
        }

        @Override
        protected Prompt doSubmit(final String input, final IProgressMonitor monitor) {
            String completeInput = input;
            if ((fPrompt.meta & BasicR.META_PROMPT_INCOMPLETE_INPUT) != 0) {
                completeInput = ((IncompleteInputPrompt) fPrompt).previousInput + input;
            }
            monitor.subTask(fDefaultPrompt.text + " " + completeInput);
            try {
                fProcessInputWriter.write(input + fLineSeparator);
                fProcessInputWriter.flush();
            } catch (final IOException e) {
                RUIPlugin.logError(-1, "Rterm IO error", e);
                if (!isToolAlive()) {
                    markAsTerminated();
                    return Prompt.NONE;
                }
            }
            try {
                Thread.sleep(fProcessOutputThread.SYNC_MS * 2);
            } catch (final InterruptedException e) {
                Thread.interrupted();
            }
            fProcessOutputThread.streamLock.lock();
            fProcessOutputThread.streamLock.unlock();
            return fDefaultPrompt;
        }
    }

    private class ReadThread extends Thread {

        volatile int hasNoOutput;

        private final int SYNC_COUNT = 2;

        private final int SYNC_MS = 33;

        final Lock streamLock = new ReentrantLock();

        public ReadThread() {
            super("Rterm-Output Monitor");
        }

        @Override
        public void run() {
            boolean locked = false;
            try {
                boolean canRead = false;
                final char[] b = new char[1024];
                while (fProcess != null | (canRead = fProcessOutputReader.ready())) {
                    fProcessOutputBuffer.available();
                    if (canRead || hasNoOutput > SYNC_COUNT) {
                        if (!canRead && locked) {
                            streamLock.unlock();
                            locked = false;
                        }
                        int n = fProcessOutputReader.read(b);
                        if (n > 0) {
                            hasNoOutput = 0;
                            if (!locked) {
                                streamLock.lock();
                                locked = true;
                            }
                            final String s = new String(b, 0, n);
                            fDefaultOutputStream.append(s, SubmitType.CONSOLE, 0);
                            n = s.length();
                            if (n >= 2 && s.charAt(--n) == ' ' && (s.charAt(--n) == '>' || s.charAt(n) == '+')) {
                                hasNoOutput++;
                                getControllerThread().interrupt();
                            }
                            continue;
                        } else if (n < 0) {
                            onRTerminated();
                            return;
                        }
                    }
                    try {
                        Thread.sleep(SYNC_MS);
                        hasNoOutput++;
                    } catch (final InterruptedException e) {
                        Thread.interrupted();
                    }
                }
            } catch (final IOException e) {
                onRTerminated();
                return;
            } finally {
                if (locked) {
                    streamLock.unlock();
                    locked = false;
                }
                try {
                    fProcessOutputReader.close();
                } catch (final IOException e1) {
                }
            }
        }

        private void onRTerminated() {
            markAsTerminated();
            RTermController.this.resume();
        }
    }

    private final ProcessBuilder fConfig;

    private final Charset fCharset;

    private Process fProcess;

    private OutputStreamWriter fProcessInputWriter;

    private BufferedInputStream fProcessOutputBuffer;

    private InputStreamReader fProcessOutputReader;

    private ReadThread fProcessOutputThread;

    public RTermController(final ToolProcess process, final ProcessBuilder config, final Charset charset) {
        super(process);
        fConfig = config;
        fCharset = charset;
        fWorkspaceData = new RWorkspace(this);
        fRunnableAdapter = new RTermAdapter();
        ((ISetupRAdapter) fRunnableAdapter).setWorkspaceDir(EFS.getLocalFileSystem().fromLocalFile(config.directory()));
    }

    @Override
    protected void startTool(final IProgressMonitor monitor) throws CoreException {
        OutputStream processInput = null;
        InputStream processOutput;
        try {
            fConfig.redirectErrorStream(true);
            fProcess = fConfig.start();
            processOutput = fProcess.getInputStream();
            if (processOutput instanceof BufferedInputStream) {
                fProcessOutputBuffer = (BufferedInputStream) processOutput;
            }
            fProcessOutputReader = new InputStreamReader(processOutput, fCharset);
            fProcessOutputThread = new ReadThread();
            fProcessOutputThread.start();
            processInput = fProcess.getOutputStream();
            fProcessInputWriter = new OutputStreamWriter(processInput, fCharset);
        } catch (final IOException e) {
            if (processInput != null) {
                try {
                    processInput.close();
                } catch (final IOException e1) {
                }
            }
            throw new CoreException(new Status(IStatus.ERROR, RUI.PLUGIN_ID, ICommonStatusConstants.LAUNCHING_ERROR, "An error occured while starting Rterm process.", e));
        }
    }

    @Override
    protected IToolRunnable createQuitRunnable() {
        return new RQuitRunnable();
    }

    @Override
    protected void interruptTool(final int hardness) {
        if (hardness == 0) {
            return;
        }
        getControllerThread().interrupt();
    }

    @Override
    protected void killTool(final IProgressMonitor monitor) {
        final Process p = fProcess;
        if (p != null) {
            p.destroy();
            fProcess = null;
        }
        markAsTerminated();
    }

    @Override
    protected boolean isToolAlive() {
        final Process p = fProcess;
        if (p != null) {
            try {
                p.exitValue();
            } catch (final IllegalStateException e) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void finishTool() {
        fProcess = null;
        super.finishTool();
    }
}
