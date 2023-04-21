package com.google.template.soy.parsepasses;

import com.google.template.soy.shared.internal.SharedTestUtils;
import com.google.template.soy.soytree.CallNode;
import com.google.template.soy.soytree.SoyFileSetNode;
import junit.framework.TestCase;

/**
 * Unit tests for ChangeCallsToPassAllDataVisitor,
 *
 */
public class ChangeCallsToPassAllDataVisitorTest extends TestCase {

    public void testChangedCall() throws Exception {
        String callCode = "{call .foo}\n" + "  {param xxx: $xxx /}\n" + "  {param yyyZzz: $yyyZzz /}\n" + "{/call}\n";
        SoyFileSetNode soyTree = SharedTestUtils.parseSoyCode(callCode);
        (new ChangeCallsToPassAllDataVisitor()).exec(soyTree);
        assertEquals("{call .foo data=\"all\" /}", SharedTestUtils.getNode(soyTree, 0).toSourceString());
        callCode = "{call .foo data=\"all\"}\n" + "  {param xxx: $xxx /}\n" + "{/call}\n";
        soyTree = SharedTestUtils.parseSoyCode(callCode);
        (new ChangeCallsToPassAllDataVisitor()).exec(soyTree);
        assertEquals("{call .foo data=\"all\" /}", SharedTestUtils.getNode(soyTree, 0).toSourceString());
    }

    public void testUnchangedCall() throws Exception {
        String callCode = "{call .foo /}\n";
        testUnchangedCallHelper(callCode);
        callCode = "{call .foo data=\"$goo\" /}\n";
        testUnchangedCallHelper(callCode);
        callCode = "{call .foo data=\"$goo\"}\n" + "  {param xxx: $xxx /}\n" + "  {param yyyZzz: $yyyZzz /}\n" + "{/call}\n";
        testUnchangedCallHelper(callCode);
        callCode = "{call .foo}\n" + "  {param xxx: $xxx0 /}\n" + "{/call}\n";
        testUnchangedCallHelper(callCode);
        callCode = "{call .foo}\n" + "  {param xxx: xxx /}\n" + "{/call}\n";
        testUnchangedCallHelper(callCode);
        callCode = "{call .foo}\n" + "  {param xxx: $goo.xxx /}\n" + "{/call}\n";
        testUnchangedCallHelper(callCode);
        callCode = "{call .foo}\n" + "  {param xxx: $xxx.goo /}\n" + "{/call}\n";
        testUnchangedCallHelper(callCode);
        callCode = "{call .foo}\n" + "  {param xxx: 'xxx' /}\n" + "{/call}\n";
        testUnchangedCallHelper(callCode);
        callCode = "{call .foo}\n" + "  {param xxx}{$xxx}{/param}\n" + "{/call}\n";
        testUnchangedCallHelper(callCode);
        callCode = "{call .foo}\n" + "  {param xxx}xxx{/param}\n" + "{/call}\n";
        testUnchangedCallHelper(callCode);
        callCode = "{call .foo}\n" + "  {param xxx: $xxx /}\n" + "  {param yyyZzz: $xxx.yyyZzz /}\n" + "{/call}\n";
        testUnchangedCallHelper(callCode);
    }

    private void testUnchangedCallHelper(String callCode) throws Exception {
        SoyFileSetNode soyTree = SharedTestUtils.parseSoyCode(callCode);
        CallNode callNodeBeforePass = (CallNode) SharedTestUtils.getNode(soyTree, 0);
        (new ChangeCallsToPassAllDataVisitor()).exec(soyTree);
        CallNode callNodeAfterPass = (CallNode) SharedTestUtils.getNode(soyTree, 0);
        assertEquals(callNodeBeforePass, callNodeAfterPass);
    }

    public void testUnchangedCallWithLoopVar() throws Exception {
        String soyCode = "{call .foo}\n" + "  {param xxx: $xxx /}\n" + "{/call}\n" + "{foreach $xxx in $xxxs}\n" + "  {call .foo}\n" + "    {param xxx: $xxx /}\n" + "  {/call}\n" + "{/foreach}";
        SoyFileSetNode soyTree = SharedTestUtils.parseSoyCode(soyCode);
        CallNode callNodeOutsideLoopBeforePass = (CallNode) SharedTestUtils.getNode(soyTree, 0);
        CallNode callNodeInsideLoopBeforePass = (CallNode) SharedTestUtils.getNode(soyTree, 1, 0, 0);
        (new ChangeCallsToPassAllDataVisitor()).exec(soyTree);
        CallNode callNodeOutsideLoopAfterPass = (CallNode) SharedTestUtils.getNode(soyTree, 0);
        CallNode callNodeInsideLoopAfterPass = (CallNode) SharedTestUtils.getNode(soyTree, 1, 0, 0);
        assertNotSame(callNodeOutsideLoopBeforePass, callNodeOutsideLoopAfterPass);
        assertSame(callNodeInsideLoopBeforePass, callNodeInsideLoopAfterPass);
    }
}
