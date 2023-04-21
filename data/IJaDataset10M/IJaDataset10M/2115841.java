package org.jcvi.fluvalidator.report;

import java.io.PrintStream;
import java.io.PrintWriter;
import org.jcvi.fluvalidator.DefaultEvaluator;
import org.jcvi.fluvalidator.SampleValidationResult;
import org.jcvi.fluvalidator.SegmentValidationResult;
import org.jcvi.fluvalidator.ValidationEvaluator;
import org.jcvi.fluvalidator.ValidationResult;
import org.jcvi.fluvalidator.errors.DeletionError;
import org.jcvi.fluvalidator.errors.FrameshiftError;
import org.jcvi.fluvalidator.errors.InsertionError;

/**
 * 
 *
 * @author jsitz@jcvi.org
 */
public class SegmentQAValidationReport extends AbstractValidationReport<PrintWriter> {

    /** The minimum number of missing bases on either end to be classified as "complete". */
    private static final int END_COVERAGE_THRESHOLD = 9;

    /**
     * Creates a new <code>SegmentQAValidationReport</code>.
     */
    public SegmentQAValidationReport(String name, SampleValidationResult result, PrintStream out) {
        this(name, result, new DefaultEvaluator(), out);
    }

    /**
     * Creates a new <code>SegmentQAValidationReport</code>.
     */
    public SegmentQAValidationReport(String name, SampleValidationResult result, ValidationEvaluator evaluator, PrintStream out) {
        this(name, result, evaluator, new PrintWriter(out));
    }

    /**
     * Creates a new <code>SegmentQAValidationReport</code>.
     */
    public SegmentQAValidationReport(String name, SampleValidationResult result, PrintWriter output) {
        this(name, result, new DefaultEvaluator(), output);
    }

    /**
     * Creates a new <code>SegmentQAValidationReport</code>.
     */
    public SegmentQAValidationReport(String name, SampleValidationResult result, ValidationEvaluator evaluator, PrintWriter output) {
        super(name, result, evaluator, output);
    }

    @Override
    public void print() {
        final String lineFormat = "%-20s %8s %8s %9s %12s %12s %8s %10s\n";
        this.getWriter().printf(lineFormat, ">Report", "Segment", "Contigs", "Complete", "Ambiguities", "Frameshifts", "In/Dels", "Validity");
        for (final SegmentValidationResult segmentResult : this.getSampleResult()) {
            int contigs = 0;
            boolean missingStart = true;
            boolean missingEnd = true;
            int ambiguities = 0;
            int frameshifts = 0;
            int indels = 0;
            for (final ValidationResult result : segmentResult) {
                contigs++;
                ambiguities += result.getAmbiguityCount();
                frameshifts += result.getErrorsByType(FrameshiftError.class).size();
                indels += result.getErrorsByType(InsertionError.class).size();
                indels += result.getErrorsByType(DeletionError.class).size();
                if (result.getMissingStartBases() <= SegmentQAValidationReport.END_COVERAGE_THRESHOLD) missingStart = false;
                if (result.getMissingEndBases() <= SegmentQAValidationReport.END_COVERAGE_THRESHOLD) missingEnd = false;
            }
            this.getWriter().printf(lineFormat, this.getReportName(), segmentResult.getSegment().toString(), contigs, (!missingStart && !missingEnd) ? "Y" : "N", String.valueOf(ambiguities), String.valueOf(frameshifts), String.valueOf(indels), this.getEvaluator().evaluate(segmentResult));
            this.getWriter().flush();
        }
    }
}
