package net.sf.reqbook.services.format;

import net.sf.reqbook.services.URIConstants;
import net.sf.reqbook.common.InternalErrorException;
import net.sf.reqbook.services.InvalidInputFileException;
import net.sf.reqbook.services.InvalidOutputPathException;
import net.sf.reqbook.services.fo.Fop;
import net.sf.reqbook.services.fo.FopPipeHandler;
import net.sf.reqbook.services.parser.InputFileIOException;
import net.sf.reqbook.services.pipe.Pipeline;
import net.sf.reqbook.services.transformer.TransformerPipeHandler;
import java.io.File;

/**
 * $Id: PDFTransformerImpl.java,v 1.8 2006/02/21 10:57:21 poma Exp $
 *
 * @author Pavel Sher
 */
public class PDFTransformerImpl extends TransformUtil implements PDFTransformer {

    private Pipeline pipe;

    private Fop fop;

    protected static final String INPUT_FILEDIR_URI_PARAM = "{" + URIConstants.REQBOOK_NS + "}input.file.dir.uri";

    public void setFop(Fop fop) {
        this.fop = fop;
    }

    public void preparePipe() throws InternalErrorException {
        if (pipe != null) return;
        assert fop != null;
        pipe = newPipeline();
        appendValidator(pipe);
        appendReqbookTransformer(pipe, "/reqbook/xsl/fo");
        appendDocbookTransformer(pipe, "/reqbook/xsl/fo/docbook");
        fop.setFormat(Fop.PDF);
        pipe.addHandler("fop", fop.createPipeHandler());
        pipe.bindHandlers();
    }

    public void transform(PDFFormat format, String inputPath, String outputPath) throws InputFileIOException, InvalidOutputPathException, InvalidInputFileException, InternalErrorException {
        checkInputPath(inputPath);
        prepareOutputPath(outputPath);
        Pipeline pipe = configurePipeline(inputPath, outputPath);
        doTransform(pipe, inputPath);
    }

    private Pipeline configurePipeline(String inputPath, String outputPath) throws InternalErrorException, InvalidOutputPathException {
        preparePipe();
        FopPipeHandler fop = (FopPipeHandler) pipe.getHandler("fop");
        fop.setOutputFile(new File(outputPath, getOutputFileName(inputPath, ".pdf")).getAbsolutePath());
        TransformerPipeHandler tph = getDocbookTransformerHandler(pipe);
        tph.clearParameters();
        tph.setParameter(INPUT_FILEDIR_URI_PARAM, new File(inputPath).toURI().resolve(".").normalize().toString());
        return pipe;
    }
}
