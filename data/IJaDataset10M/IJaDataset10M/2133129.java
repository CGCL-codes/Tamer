package net.sf.jsequnit.tag;

import net.sf.jsequnit.javadoc.SequenceTaglet;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
public class OmitParameter extends AbstractParameterTest {

    public String getParamName() {
        return SequenceTaglet.EXCLUDE_PARAMETER;
    }
}
