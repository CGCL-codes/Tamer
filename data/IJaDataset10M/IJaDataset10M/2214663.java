package org.outerj.daisy.diff.html;

import java.io.FileInputStream;
import java.util.*;
import org.eclipse.compare.internal.LCSSettings;
import org.eclipse.compare.rangedifferencer.RangeDifference;
import org.eclipse.compare.rangedifferencer.RangeDifferencer;
import org.outerj.daisy.diff.output.DiffOutput;
import org.outerj.daisy.diff.output.Differ;
import org.xml.sax.SAXException;

/**
 * Takes two {@link TextNodeComparator} instances, computes the difference
 * between them, marks the changes, and outputs a merged tree to a
 * {@link HtmlSaxDiffOutput} instance.
 */
public class HTMLDiffer implements Differ {

    private DiffOutput output;

    public HTMLDiffer(DiffOutput dm) {
        output = dm;
    }

    /**
     * {@inheritDoc}
     */
    public void diff(TextNodeComparator leftComparator, TextNodeComparator rightComparator) throws SAXException {
        int[] countDifferences = { 0, 0, 0 };
        diff(leftComparator, rightComparator, countDifferences);
    }

    public void diff(TextNodeComparator leftComparator, TextNodeComparator rightComparator, int[] countDifferences) throws SAXException {
        Properties compareProperties = new Properties();
        try {
            compareProperties.load(new FileInputStream("propertyFiles/compare.properties"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        LCSSettings settings = new LCSSettings();
        settings.setUseGreedyMethod(false);
        RangeDifference[] differences = RangeDifferencer.findDifferences(settings, leftComparator, rightComparator);
        List<RangeDifference> pdifferences = preProcess(differences);
        int currentIndexLeft = 0;
        int currentIndexRight = 0;
        for (RangeDifference d : pdifferences) {
            if (d.leftStart() > currentIndexLeft) {
                rightComparator.handlePossibleChangedPart(currentIndexLeft, d.leftStart(), currentIndexRight, d.rightStart(), leftComparator, compareProperties, countDifferences);
            }
            if (d.leftLength() > 0) {
                rightComparator.markAsDeleted(d.leftStart(), d.leftEnd(), leftComparator, d.rightStart(), compareProperties, countDifferences);
            }
            rightComparator.markAsNew(d.rightStart(), d.rightEnd(), compareProperties, countDifferences);
            currentIndexLeft = d.leftEnd();
            currentIndexRight = d.rightEnd();
        }
        if (currentIndexLeft < leftComparator.getRangeCount()) {
            rightComparator.handlePossibleChangedPart(currentIndexLeft, leftComparator.getRangeCount(), currentIndexRight, rightComparator.getRangeCount(), leftComparator, compareProperties, countDifferences);
        }
        rightComparator.expandWhiteSpace();
        output.generateOutput(rightComparator.getBodyNode());
    }

    private List<RangeDifference> preProcess(RangeDifference[] differences) {
        List<RangeDifference> newRanges = new LinkedList<RangeDifference>();
        for (int i = 0; i < differences.length; i++) {
            int leftStart = differences[i].leftStart();
            int leftEnd = differences[i].leftEnd();
            int rightStart = differences[i].rightStart();
            int rightEnd = differences[i].rightEnd();
            int kind = differences[i].kind();
            int leftLength = leftEnd - leftStart;
            int rightLength = rightEnd - rightStart;
            while (i + 1 < differences.length && differences[i + 1].kind() == kind && score(leftLength, differences[i + 1].leftLength(), rightLength, differences[i + 1].rightLength()) > (differences[i + 1].leftStart() - leftEnd)) {
                leftEnd = differences[i + 1].leftEnd();
                rightEnd = differences[i + 1].rightEnd();
                leftLength = leftEnd - leftStart;
                rightLength = rightEnd - rightStart;
                i++;
            }
            newRanges.add(new RangeDifference(kind, rightStart, rightLength, leftStart, leftLength));
        }
        return newRanges;
    }

    public static double score(int... numbers) {
        if ((numbers[0] == 0 && numbers[1] == 0) || (numbers[2] == 0 && numbers[3] == 0)) return 0;
        double d = 0;
        for (double number : numbers) {
            while (number > 3) {
                d += 3;
                number -= 3;
                number *= 0.5;
            }
            d += number;
        }
        return d / (1.5 * numbers.length);
    }
}
