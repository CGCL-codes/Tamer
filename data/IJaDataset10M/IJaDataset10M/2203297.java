package android.pim.vcard;

import android.pim.vcard.VCardConfig;
import android.text.TextUtils;
import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.List;

class LineVerifierElem {

    private final TestCase mTestCase;

    private final List<String> mExpectedLineList = new ArrayList<String>();

    private final boolean mIsV30;

    public LineVerifierElem(TestCase testCase, int vcardType) {
        mTestCase = testCase;
        mIsV30 = VCardConfig.isV30(vcardType);
    }

    public LineVerifierElem addExpected(final String line) {
        if (!TextUtils.isEmpty(line)) {
            mExpectedLineList.add(line);
        }
        return this;
    }

    public void verify(final String vcard) {
        final String[] lineArray = vcard.split("\\r?\\n");
        final int length = lineArray.length;
        boolean beginExists = false;
        boolean endExists = false;
        boolean versionExists = false;
        for (int i = 0; i < length; i++) {
            final String line = lineArray[i];
            if (TextUtils.isEmpty(line)) {
                continue;
            }
            if ("BEGIN:VCARD".equalsIgnoreCase(line)) {
                if (beginExists) {
                    mTestCase.fail("Multiple \"BEGIN:VCARD\" line found");
                } else {
                    beginExists = true;
                    continue;
                }
            } else if ("END:VCARD".equalsIgnoreCase(line)) {
                if (endExists) {
                    mTestCase.fail("Multiple \"END:VCARD\" line found");
                } else {
                    endExists = true;
                    continue;
                }
            } else if ((mIsV30 ? "VERSION:3.0" : "VERSION:2.1").equalsIgnoreCase(line)) {
                if (versionExists) {
                    mTestCase.fail("Multiple VERSION line + found");
                } else {
                    versionExists = true;
                    continue;
                }
            }
            if (!beginExists) {
                mTestCase.fail("Property other than BEGIN came before BEGIN property: " + line);
            } else if (endExists) {
                mTestCase.fail("Property other than END came after END property: " + line);
            }
            final int index = mExpectedLineList.indexOf(line);
            if (index >= 0) {
                mExpectedLineList.remove(index);
            } else {
                mTestCase.fail("Unexpected line: " + line);
            }
        }
        if (!mExpectedLineList.isEmpty()) {
            StringBuffer buffer = new StringBuffer();
            for (String expectedLine : mExpectedLineList) {
                buffer.append(expectedLine);
                buffer.append("\n");
            }
            mTestCase.fail("Expected line(s) not found:" + buffer.toString());
        }
    }
}
