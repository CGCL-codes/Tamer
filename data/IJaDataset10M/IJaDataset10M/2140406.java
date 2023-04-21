package org.intellij.vcs.mks.sicommands;

import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vcs.history.VcsRevisionNumber;
import org.intellij.vcs.mks.MksCLIConfiguration;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.List;

/**
 * @author Thibaut Fagart
 */
public class GetContentRevision extends SiCLICommand {

    @NonNls
    public static final String COMMAND = "viewrevision";

    @NotNull
    private final String member;

    @NotNull
    private final VcsRevisionNumber revisionNumber;

    public GetContentRevision(@NotNull List<VcsException> errors, @NotNull MksCLIConfiguration mksCLIConfiguration, @NotNull final VcsRevisionNumber revisionNumber, @NotNull final String path) {
        super(errors, mksCLIConfiguration, COMMAND, "-r", revisionNumber.asString(), path);
        this.member = path;
        this.revisionNumber = revisionNumber;
    }

    @Override
    public void execute() {
        try {
            executeCommand();
        } catch (IOException e) {
            errors.add(new VcsException(e));
        }
    }

    public String getContent() throws VcsException {
        if (foundError()) {
            for (VcsException vcsException : errors.subList(previousErrorCount, errors.size())) {
                LOGGER.error(vcsException);
            }
            throw new VcsException(errors.get(previousErrorCount));
        } else {
            return commandOutput;
        }
    }

    @Override
    public String toString() {
        return "GetContentRevision[" + member + "," + revisionNumber.asString() + "]";
    }
}
