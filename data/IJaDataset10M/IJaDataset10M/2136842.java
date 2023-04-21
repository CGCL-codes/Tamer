package com.google.template.soy.soyparse;

import com.google.common.base.Joiner;
import com.google.template.soy.base.SoyFileSupplier;
import com.google.template.soy.base.SoySyntaxException;
import com.google.template.soy.soytree.AbstractSoyNodeVisitor;
import com.google.template.soy.soytree.SoyFileSetNode;
import com.google.template.soy.soytree.SoyNode;
import com.google.template.soy.soytree.SoyNode.ParentSoyNode;
import junit.framework.TestCase;

/**
 * Tests that the Soy file and template parsers properly embed source locations.
 */
public class SourceLocationTest extends TestCase {

    public final void testLocationsInParsedContent() throws Exception {
        assertSourceLocations(Joiner.on('\n').join("SoyFileSetNode                 @ unknown", "  SoyFileNode                  @ /example/file.soy", "    TemplateBasicNode          @ /example/file.soy:1", "      RawTextNode              @ /example/file.soy:2", "      PrintNode                @ /example/file.soy:4", "      RawTextNode              @ /example/file.soy:5", "      CallBasicNode            @ /example/file.soy:7", "    TemplateBasicNode          @ /example/file.soy:9", "      RawTextNode              @ /example/file.soy:10", ""), "/example/file.soy", Joiner.on('\n').join("{template foo}", "  Hello", "  {lb}", "  {print $world}", "  {rb}!", "", "  {call bar /}", "{/template}", "{template bar}", "  Gooodbye", "{/template}"));
    }

    public final void testSwitches() throws Exception {
        assertSourceLocations(Joiner.on('\n').join("SoyFileSetNode                 @ unknown", "  SoyFileNode                  @ /example/file.soy", "    TemplateBasicNode          @ /example/file.soy:1", "      RawTextNode              @ /example/file.soy:2", "      SwitchNode               @ /example/file.soy:3", "        SwitchCaseNode         @ /example/file.soy:4", "          RawTextNode          @ /example/file.soy:5", "        SwitchCaseNode         @ /example/file.soy:6", "          RawTextNode          @ /example/file.soy:7", "        SwitchCaseNode         @ /example/file.soy:8", "          RawTextNode          @ /example/file.soy:9", "        SwitchDefaultNode      @ /example/file.soy:10", "          RawTextNode          @ /example/file.soy:11", "      RawTextNode              @ /example/file.soy:13", ""), "/example/file.soy", Joiner.on('\n').join("{template foo}", "  Hello,", "  {switch $i}", "    {case 0}", "      Mercury", "    {case 1}", "      Venus", "    {case 2}", "      Mars", "    {default}", "      Gassy", "  {/switch}", "  !", "{/template}", ""));
    }

    public final void testForLoop() throws Exception {
        assertSourceLocations(Joiner.on('\n').join("SoyFileSetNode                 @ unknown", "  SoyFileNode                  @ /example/file.soy", "    TemplateBasicNode          @ /example/file.soy:1", "      RawTextNode              @ /example/file.soy:2", "      ForNode                  @ /example/file.soy:3", "        RawTextNode            @ /example/file.soy:4", "        PrintNode              @ /example/file.soy:5", "      RawTextNode              @ /example/file.soy:7", ""), "/example/file.soy", Joiner.on('\n').join("{template foo}", "  Hello", "  {for $i in range($s, $e, $t)}", "    ,", "    {print $planet[$i]}", "  {/for}", "  !", "{/template}", ""));
    }

    public final void testForeachLoop() throws Exception {
        assertSourceLocations(Joiner.on('\n').join("SoyFileSetNode                 @ unknown", "  SoyFileNode                  @ /example/file.soy", "    TemplateBasicNode          @ /example/file.soy:1", "      RawTextNode              @ /example/file.soy:2", "      ForeachNode              @ /example/file.soy:3", "        ForeachNonemptyNode    @ /example/file.soy:3", "          RawTextNode          @ /example/file.soy:4", "          PrintNode            @ /example/file.soy:5", "        ForeachIfemptyNode     @ /example/file.soy:6", "          RawTextNode          @ /example/file.soy:7", "      RawTextNode              @ /example/file.soy:9", ""), "/example/file.soy", Joiner.on('\n').join("{template foo}", "  Hello", "  {foreach $planet in $planets}", "    ,", "    {print $planet[$i]}", "  {ifempty}", "    lifeless interstellar void", "  {/foreach}", "  !", "{/template}", ""));
    }

    public final void testConditional() throws Exception {
        assertSourceLocations(Joiner.on('\n').join("SoyFileSetNode                 @ unknown", "  SoyFileNode                  @ /example/file.soy", "    TemplateBasicNode          @ /example/file.soy:1", "      RawTextNode              @ /example/file.soy:2", "      IfNode                   @ /example/file.soy:3", "        IfCondNode             @ /example/file.soy:3", "          RawTextNode          @ /example/file.soy:4", "        IfCondNode             @ /example/file.soy:5", "          RawTextNode          @ /example/file.soy:6", "        IfElseNode             @ /example/file.soy:7", "          RawTextNode          @ /example/file.soy:8", "      RawTextNode              @ /example/file.soy:10", ""), "/example/file.soy", Joiner.on('\n').join("{template foo}", "  Hello,", "  {if $skyIsBlue}", "    Earth", "  {elseif $isReallyReallyHot}", "    Venus", "  {else}", "    Cincinatti", "  {/if}", "  !", "{/template}", ""));
    }

    public final void testDoesntAccessPastEnd() {
        try {
            (new SoyFileSetParser(SoyFileSupplier.Factory.create("{template t}\nHello, World!\n", "borken.soy"))).setDoRunInitialParsingPasses(false).parse();
        } catch (SoySyntaxException ex) {
            return;
        }
    }

    void assertSourceLocations(String asciiArtExpectedOutput, String soySourcePath, String soySourceCode) throws Exception {
        SoyFileSetNode soyTree = (new SoyFileSetParser(SoyFileSupplier.Factory.create(soySourceCode, soySourcePath))).setDoRunInitialParsingPasses(false).parse();
        String actual = new AsciiArtVisitor().exec(soyTree);
        assertEquals(asciiArtExpectedOutput, actual);
    }

    /**
   * Generates a concise readable summary of a soy tree and its source locations.
   */
    static class AsciiArtVisitor extends AbstractSoyNodeVisitor<String> {

        final StringBuilder sb = new StringBuilder();

        int depth;

        @Override
        public String exec(SoyNode node) {
            visit(node);
            return sb.toString();
        }

        @Override
        protected void visitSoyNode(SoyNode node) {
            for (int indent = depth; --indent >= 0; ) {
                sb.append("  ");
            }
            String typeName = node.getClass().getSimpleName();
            sb.append(typeName);
            int pos = typeName.length() + 2 * depth;
            while (pos < 30) {
                sb.append(' ');
                ++pos;
            }
            sb.append(" @ ").append(node.getLocation()).append('\n');
            if (node instanceof ParentSoyNode<?>) {
                ++depth;
                visitChildren((ParentSoyNode<?>) node);
                --depth;
            }
        }
    }
}
