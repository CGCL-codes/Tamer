package com.itextpdf.tool.xml.html.tps;

import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.ParaGraph;
import com.itextpdf.tool.xml.pipeline.ctx.WorkerContextImpl;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

/**
 * @author itextpdf.com
 *
 */
public class ParaGraphTest {

    final ParaGraph p = new ParaGraph();

    List<Element> currentContent = new ArrayList<Element>();

    private WorkerContextImpl workerContextImpl;

    @Before
    public void init() {
        workerContextImpl = new WorkerContextImpl();
        p.setCssAppliers(new CssAppliersImpl());
        workerContextImpl.put(HtmlPipeline.class.getName(), new HtmlPipelineContext(null));
        currentContent.addAll(p.content(workerContextImpl, new Tag("p"), "some paragraph text"));
    }

    /**
	 * Verifies that the call to content of {@link ParaGraph} returns a Chunk.
	 */
    @Test
    public void verifyContent() {
        Assert.assertTrue(currentContent.get(0) instanceof Chunk);
    }

    /**
	 * Verifies if the class of the elements returned by {@link ParaGraph#end} is a Paragraph.
	 */
    @Test
    public void verifyEnd() {
        final List<Element> endContent = p.end(workerContextImpl, new Tag("p"), currentContent);
        Assert.assertTrue(endContent.get(0) instanceof Paragraph);
    }

    /**
	 * Verifies if {@link ParaGraph} is a stack owner. Should be true.
	 */
    @Test
    public void verifyIfStackOwner() {
        Assert.assertTrue(p.isStackOwner());
    }
}
