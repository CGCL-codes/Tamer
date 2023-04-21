package unclej.utasks.file;

import unclej.filepath.FileSpec;
import unclej.filepath.Filelike;
import unclej.framework.UTaskFailedException;
import unclej.log.ULog;
import unclej.log.ULogDecorator;
import unclej.util.Quote;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Copies a single file or set of files to a target location with text substitution.
 * @author scottv
 */
public class ReplaceUTask extends CopyUTask {

    public ReplaceUTask() {
    }

    public ReplaceUTask(File from, File to) {
        super(from, to);
    }

    public ReplaceUTask(FileSpec from, File to) {
        super(from, to);
    }

    public void describe(ULog log, boolean simulate) {
        ULog delegateLog = new ULogDecorator(log) {

            @Override
            public void log(Level level, Throwable t, String message, Object... params) {
                super.log(level, t, message + " with replacements", params);
            }
        };
        super.describe(delegateLog, simulate);
    }

    protected boolean isUpToDate(Filelike source, File dest) {
        return dest.exists() && dest.lastModified() >= source.getModTime();
    }

    protected boolean copyFile(Filelike source, File dest, long size, ULog log) throws UTaskFailedException {
        boolean retryFile = true;
        while (retryFile) {
            try {
                copyAndReplace(source, dest, size, log);
                return true;
            } catch (IOException x) {
                String message = "failed to copy/replace " + new Quote(source) + " to " + new Quote(dest);
                retryFile = getFileFailurePolicy().handle(this, log, x, message);
            }
        }
        return false;
    }

    private void copyAndReplace(Filelike source, File dest, long size, ULog log) throws IOException {
        OutputStream out = new BufferedOutputStream(new FileOutputStream(dest));
        InputStream in = new BufferedInputStream(source.getInputStream());
        try {
            log.fine("copying with replace rules {0} ({1} bytes)", new Quote(source), SIZE_FORMAT.format(size));
            byte[] sourceBytes = source.readWhole();
            String text = (charSetName == null) ? new String(sourceBytes) : new String(sourceBytes, charSetName);
            for (ReplaceRule rule : rules) {
                text = rule.replace(text);
            }
            byte[] destBytes = (charSetName == null) ? text.getBytes() : text.getBytes(charSetName);
            out.write(destBytes);
        } finally {
            in.close();
            out.flush();
            out.close();
        }
    }

    public void addRule(ReplaceRule rule) {
        rules.add(rule);
    }

    public void setCharSetName(String charSetName) {
        this.charSetName = charSetName;
    }

    private List<ReplaceRule> rules = new ArrayList<ReplaceRule>();

    private String charSetName;
}
