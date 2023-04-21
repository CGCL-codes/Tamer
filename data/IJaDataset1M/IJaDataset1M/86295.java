package rat.report.analyser;

import rat.analysis.IHeaderMatcher;
import rat.document.IDocumentAnalyser;
import rat.document.IDocumentMatcher;
import rat.document.impl.guesser.ArchiveGuesser;
import rat.document.impl.guesser.BinaryGuesser;
import rat.document.impl.guesser.NoteGuesser;
import rat.document.impl.util.ConditionalAnalyser;
import rat.document.impl.util.DocumentAnalyserMultiplexer;
import rat.document.impl.util.DocumentMatcherMultiplexer;
import rat.document.impl.util.MatchNegator;
import rat.report.claim.IClaimReporter;

/**
 * Creates default analysers.
 *
 */
public class DefaultAnalyserFactory {

    public static final IDocumentAnalyser createArchiveTypeAnalyser(final IClaimReporter reporter) {
        final AbstractSingleClaimAnalyser constantClaimAnalyser = new ConstantClaimAnalyser(reporter, "type", "archive", false);
        return constantClaimAnalyser;
    }

    public static final IDocumentAnalyser createNoticeTypeAnalyser(final IClaimReporter reporter) {
        final AbstractSingleClaimAnalyser constantClaimAnalyser = new ConstantClaimAnalyser(reporter, "type", "notice", false);
        return constantClaimAnalyser;
    }

    public static final IDocumentAnalyser createBinaryTypeAnalyser(final IClaimReporter reporter) {
        final AbstractSingleClaimAnalyser constantClaimAnalyser = new ConstantClaimAnalyser(reporter, "type", "binary", false);
        return constantClaimAnalyser;
    }

    public static final IDocumentAnalyser createStandardTypeAnalyser(final IClaimReporter reporter) {
        final AbstractSingleClaimAnalyser constantClaimAnalyser = new ConstantClaimAnalyser(reporter, "type", "standard", false);
        return constantClaimAnalyser;
    }

    public static final IDocumentAnalyser createDefaultBinaryAnalyser(final IClaimReporter reporter) {
        final IDocumentAnalyser result = createBinaryTypeAnalyser(reporter);
        return result;
    }

    public static final IDocumentAnalyser createDefaultNoticeAnalyser(final IClaimReporter reporter) {
        final IDocumentAnalyser result = createNoticeTypeAnalyser(reporter);
        return result;
    }

    public static final IDocumentAnalyser createDefaultArchiveAnalyser(final IClaimReporter reporter) {
        final IDocumentAnalyser[] components = { createArchiveTypeAnalyser(reporter), new ReadableArchiveAnalyser(reporter) };
        final DocumentAnalyserMultiplexer result = new DocumentAnalyserMultiplexer(components);
        return result;
    }

    public static final IDocumentAnalyser createDefaultStandardAnalyser(final IClaimReporter reporter, final IHeaderMatcher matcher) {
        final IDocumentAnalyser[] components = { createStandardTypeAnalyser(reporter), new DocumentHeaderAnalyser(matcher, reporter) };
        final DocumentAnalyserMultiplexer result = new DocumentAnalyserMultiplexer(components);
        return result;
    }

    public static final IDocumentAnalyser createDefaultAnalyser(final IClaimReporter reporter, final IHeaderMatcher matcher) {
        final IDocumentAnalyser binaryAnalyser = createDefaultBinaryAnalyser(reporter);
        final IDocumentAnalyser archiveAnalyser = createDefaultArchiveAnalyser(reporter);
        final IDocumentAnalyser noticeAnalyser = createDefaultNoticeAnalyser(reporter);
        final IDocumentAnalyser standardAnalyser = createDefaultStandardAnalyser(reporter, matcher);
        return createDefaultAnalyser(binaryAnalyser, archiveAnalyser, noticeAnalyser, standardAnalyser);
    }

    public static IDocumentAnalyser createDefaultAnalyser(final IDocumentAnalyser binaryAnalyser, final IDocumentAnalyser archiveAnalyser, final IDocumentAnalyser noticeAnalyser, final IDocumentAnalyser standardAnalyser) {
        final IDocumentMatcher binaryGuesser = new BinaryGuesser();
        final IDocumentMatcher archiveGuesser = new ArchiveGuesser();
        final IDocumentMatcher noteGuesser = new NoteGuesser();
        return createDefaultAnalyser(binaryAnalyser, archiveAnalyser, noticeAnalyser, standardAnalyser, binaryGuesser, archiveGuesser, noteGuesser);
    }

    public static IDocumentAnalyser createDefaultAnalyser(final IDocumentAnalyser binaryAnalyser, final IDocumentAnalyser archiveAnalyser, final IDocumentAnalyser noticeAnalyser, final IDocumentAnalyser standardAnalyser, final IDocumentMatcher binaryGuesser, final IDocumentMatcher archiveGuesser, final IDocumentMatcher noteGuesser) {
        final IDocumentMatcher binaryMatcher = new ConditionalAnalyser(binaryGuesser, binaryAnalyser);
        final IDocumentMatcher noticeMatcher = new ConditionalAnalyser(noteGuesser, noticeAnalyser);
        final IDocumentMatcher archiveMatcher = new ConditionalAnalyser(archiveGuesser, archiveAnalyser);
        final IDocumentMatcher[] matchers = { noticeMatcher, archiveMatcher, binaryMatcher };
        final IDocumentMatcher specialDocumentMatcher = new DocumentMatcherMultiplexer(matchers);
        final IDocumentMatcher documentMatcher = new MatchNegator(specialDocumentMatcher);
        final ConditionalAnalyser result = new ConditionalAnalyser(documentMatcher, standardAnalyser);
        return result;
    }
}
