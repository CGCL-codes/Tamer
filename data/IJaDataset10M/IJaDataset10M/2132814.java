package org.jcvi.trace.fourFiveFour.flowgram.sff;

import java.math.BigInteger;
import org.jcvi.trace.fourFiveFour.flowgram.sff.DefaultSFFCommonHeader;
import org.jcvi.trace.fourFiveFour.flowgram.sff.DefaultSFFCommonHeaderCodec;

public class AbstractTestDefaultSFFCommonHeaderCodec {

    protected BigInteger indexOffset = BigInteger.valueOf(100000L);

    protected int indexLength = 2000;

    protected int numberOfReads = 5;

    protected short numberOfFlowsPerRead = 12;

    protected String flow = "TCAGTCAGTCAG";

    protected String keySequence = "TCAG";

    protected short headerLength = (short) (31 + numberOfFlowsPerRead + SFFUtil.caclulatePaddedBytes(31 + numberOfFlowsPerRead));

    protected DefaultSFFCommonHeader expectedHeader = new DefaultSFFCommonHeader(indexOffset, indexLength, numberOfReads, numberOfFlowsPerRead, flow, keySequence);

    protected DefaultSFFCommonHeaderCodec sut = new DefaultSFFCommonHeaderCodec();
}
