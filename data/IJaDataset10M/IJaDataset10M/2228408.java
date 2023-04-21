package net.sourceforge.ext4j.taglib.tag.repeater;

import java.util.List;
import javax.servlet.jsp.tagext.TagSupport;
import org.junit.Test;

/**
 * @author luc
 *
 */
public class HeaderTemplateTagTest extends TagBaseTest {

    @Test
    public void render() throws Exception {
        replayAllMocks();
        RepeaterTag oLoopTag = new RepeaterTag();
        oLoopTag.setPageContext(mMockPageContext);
        List<MyModel> oData = newData("Luc", "Jerome", "Tony");
        oLoopTag.setItems(oData);
        HeaderTemplateTag oHeaderTag = new HeaderTemplateTag();
        oHeaderTag.setPageContext(mMockPageContext);
        oHeaderTag.setParent(oLoopTag);
        oLoopTag.doStartTag();
        doStartTagAnsAssertEquals(oHeaderTag, TagSupport.EVAL_BODY_INCLUDE);
        oLoopTag.doAfterBody();
        doStartTagAnsAssertEquals(oHeaderTag, TagSupport.SKIP_BODY);
        oLoopTag.doAfterBody();
        doStartTagAnsAssertEquals(oHeaderTag, TagSupport.SKIP_BODY);
        verifyAllMocks();
    }
}
