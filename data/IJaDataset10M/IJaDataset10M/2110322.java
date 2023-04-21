package net.sf.buildbox.parser.maven;

import net.sf.buildbox.parser.LineParser;
import net.sf.buildbox.parser.LookAheadBuffer;
import java.io.IOException;

public class M221LogBlock extends LineParser {

    public static final String INFO_DASH = "[INFO] ------------------------------------------------------------------------";

    private M221ScanningBlock scanningBlock;

    public M221ScanningBlock getScanningBlock() {
        return scanningBlock;
    }

    @Override
    public boolean parseChunk(LookAheadBuffer buf) throws IOException {
        final String line = buf.peek(0);
        if (line.equals(M221ScanningBlock.INFO_SCANNING_FOR_PROJECTS)) {
            scanningBlock = parseSubcontext(M221ScanningBlock.class, buf);
        }
        if (M221ModuleBuildBlock.isHeader(buf)) {
            parseSubcontext(M221ModuleBuildBlock.class, buf);
        }
        if (M221SkippedModuleBuildBlock.isHeader(buf)) {
            parseSubcontext(M221SkippedModuleBuildBlock.class, buf);
        }
        if (M221ReactorSummaryBlock.isHeader(buf)) {
            parseSubcontext(M221ReactorSummaryBlock.class, buf);
        }
        if (M221ResultsBlock.isHeader(buf)) {
            parseSubcontext(M221ResultsBlock.class, buf);
        }
        if (M221ModuleErrorBlock.isHeader(buf)) {
            parseSubcontext(M221ModuleErrorBlock.class, buf);
        }
        return true;
    }
}
