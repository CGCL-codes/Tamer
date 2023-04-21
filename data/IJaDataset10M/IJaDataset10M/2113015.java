package net.sf.reqbook.services.format;

import net.sf.reqbook.common.InternalErrorException;
import net.sf.reqbook.TestUtil;
import net.sf.reqbook.services.InvalidInputFileException;
import net.sf.reqbook.services.InvalidOutputPathException;
import net.sf.reqbook.services.parser.InputFileIOException;
import java.io.File;

/**
 * $Id: WordMLTransformerImplTest.java,v 1.6 2006/02/21 10:59:19 poma Exp $
 *
 * @author Pavel Sher
 */
public class WordMLTransformerImplTest extends TransformerTestCase {

    public void testPreparePipe() throws InternalErrorException, InvalidOutputPathException, InputFileIOException {
        expectPreparePipe();
        WordMLTransformerImpl tr = new WordMLTransformerImpl();
        setupTransformer(tr);
        tr.preparePipe();
    }

    public void testTransform() throws InternalErrorException, InvalidOutputPathException, InputFileIOException, InvalidInputFileException {
        expectPreparePipe();
        String input = new File(TestUtil.filesDir(), "simple.xml").getAbsolutePath();
        String output = TestUtil.tmpDir().getAbsolutePath();
        String outputPathParam = new File(TestUtil.tmpDir(), "simple.xml").getAbsolutePath();
        expectSetOutputPathParam(outputPathParam);
        expectParsing(input);
        WordMLTransformerImpl tr = new WordMLTransformerImpl();
        setupTransformer(tr);
        tr.transform(new WordMLFormat(), input, output);
    }

    private void expectSetOutputPathParam(String outputPathParam) {
        docbookTransformerHandler.expects(once()).method("clearParameters");
        docbookTransformerHandler.expects(once()).method("setParameter").with(eq(WordMLTransformerImpl.OUTPUT_PATH_PARAM), eq(outputPathParam));
    }

    private void expectPreparePipe() {
        expectPrepareValidator();
        expectPrepareReqbookTransformer("/reqbook/xsl/wordml");
        expectPrepareDocbookTransformer("/reqbook/xsl/wordml/docbook");
        stubsHandlersBinding();
    }
}
