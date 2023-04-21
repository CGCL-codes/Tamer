package jdiff.util;

import java.io.IOException;

/** Print a change list in the standard diff format. */
public class DiffNormalOutput extends DiffOutput {

    public DiffNormalOutput(Object[] a, Object[] b) {
        super(a, b);
    }

    public void writeScript(Diff.Change script) throws IOException {
        Diff.Change hunk = script;
        for (; hunk != null; hunk = hunk.next) {
            this.writeHunk(hunk);
        }
        this.out.flush();
    }

    /** Writes a hunk of a normal diff. */
    protected void writeHunk(Diff.Change hunk) throws IOException {
        int deletes = hunk.lines0;
        int inserts = hunk.lines1;
        if (deletes == 0 && inserts == 0) {
            return;
        }
        int first0 = hunk.first0;
        int first1 = hunk.first1;
        int last0 = hunk.first0 + hunk.lines0 - 1;
        int last1 = hunk.first1 + hunk.lines1 - 1;
        this.writeNumberRange(',', first0, last0);
        out.write(DiffOutput.changeLetter(inserts, deletes));
        this.writeNumberRange(',', first1, last1);
        out.write(this.lineSeparator);
        if (deletes != 0) {
            for (int i = first0; i <= last0; i++) {
                this.writeLine("< ", this.file0[i]);
            }
        }
        if (inserts != 0 && deletes != 0) {
            out.write("---" + this.lineSeparator);
        }
        if (inserts != 0) {
            for (int i = first1; i <= last1; i++) {
                this.writeLine("> ", this.file1[i]);
            }
        }
    }
}
