package com.itextpdf.tool.xml.html.tps;

import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.ListItem;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.OrderedUnorderedListItem;
import com.itextpdf.tool.xml.pipeline.ctx.WorkerContextImpl;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

/**
 * @author itextpdf.com
 *
 */
public class ListItemTest {

    final OrderedUnorderedListItem li = new OrderedUnorderedListItem();

    List<Element> currentContent = new ArrayList<Element>();

    private WorkerContextImpl workerContextImpl;

    @Before
    public void init() {
        li.setCssAppliers(new CssAppliersImpl());
        workerContextImpl = new WorkerContextImpl();
        workerContextImpl.put(HtmlPipeline.class.getName(), new HtmlPipelineContext(null));
        currentContent.addAll(li.content(workerContextImpl, new Tag("li"), "list item"));
    }

    /**
	 * Verifies that the call to content of {@link OrderedUnorderedListItem} returns a Chunk.
	 */
    @Test
    public void verifyContent() {
        Assert.assertTrue(currentContent.get(0) instanceof Chunk);
    }

    /**
	 * Verifies if the class of the elements returned by {@link OrderedUnorderedListItem#end} is a ListItem.
	 */
    @Test
    public void verifyEnd() {
        final List<Element> endContent = li.end(workerContextImpl, new Tag("li"), currentContent);
        Assert.assertTrue(endContent.get(0) instanceof ListItem);
    }

    /**
	 * Verifies if {@link OrderedUnorderedListItem} is a stack owner. Should be true.
	 */
    @Test
    public void verifyIfStackOwner() {
        Assert.assertTrue(li.isStackOwner());
    }
}
