package org.in4ama.documentengine.evaluator.handlers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import org.in4ama.documentengine.evaluator.EvaluableContent;
import org.in4ama.documentengine.evaluator.EvaluationContext;
import org.in4ama.documentengine.evaluator.EvaluationHelper;
import org.in4ama.documentengine.evaluator.TagHandler;
import org.in4ama.documentengine.exception.EvaluationException;
import org.in4ama.documentengine.generator.InforamaContext;
import org.in4ama.documentengine.util.FileUtil;

/** FO images handler */
public class ImgHandler {

    private static final String TMP_FILES = "TMP_FILES";

    private static final String DEFAULT_SCALE = "70%";

    /** Underlying evaluation context */
    private EvaluationContext context;

    /**
	 * Creates a new instance ImgHandler
	 * 
	 * @param context
	 *            owning evaluation context.
	 */
    public ImgHandler(EvaluationContext context) {
        this.context = context;
    }

    /** Stores an absolute path to the created temporary file */
    @SuppressWarnings("unchecked")
    private void storePath(String path) {
        List<String> tmpImages = (List<String>) context.getValue(TMP_FILES);
        if (tmpImages == null) context.setValue(TMP_FILES, tmpImages = new ArrayList<String>());
        tmpImages.add(path);
    }

    @TagHandler("IMG")
    public String handler(String img) throws Exception {
        return handler(img, DEFAULT_SCALE);
    }

    @TagHandler("IMG")
    public String handler(String img, String size) throws Exception {
        return handler(img, size, size);
    }

    @TagHandler("IMG")
    public String handler(String img, String width, String height) throws Exception {
        EvaluationHelper evaluator = InforamaContext.getInstance().getEvaluationHelper();
        EvaluableContent evalContent = evaluator.getEvaluableContent(img, context);
        String imgUrl = null;
        if ((evalContent.size() >= 2) && !(evalContent.getEvaluationUnit(1).getValue() instanceof String)) {
            InputStream in = toInputStream(evalContent.getEvaluationUnit(1).getValue());
            if (in == null) {
                String msg = "Unable to retrieve the binary data of an image specified in the binding: " + img;
                throw new EvaluationException(msg);
            }
            File tmpFile = File.createTempFile("img", ".png");
            FileUtil.saveInputStreamtoFile(in, tmpFile);
            imgUrl = tmpFile.getAbsolutePath();
            storePath(tmpFile.getAbsolutePath());
        } else {
            imgUrl = evalContent.getAsText();
        }
        if (imgUrl == null) {
            String msg = "Unable to retrieve the image specified in the binding: " + img;
            throw new EvaluationException(msg);
        }
        return getFoTag(imgUrl, width, height);
    }

    private String getFoTag(String imgUrl, String width, String height) {
        String tag = "<fo:external-graphic xmlns:fo=\"http://www.w3.org/1999/XSL/Format\" content-height=\"" + height + "\" content-width=\"" + width + "\" margin-bottom=\"0\" margin-left=\"0\" margin-right=\"0\" margin-top=\"0\" scaling=\"uniform\" src=\"url(" + imgUrl + ")\" text-align=\"right\" vertical-align=\"top\"/>";
        return tag;
    }

    private InputStream toInputStream(Object obj) throws EvaluationException {
        InputStream in = null;
        if (obj instanceof Blob) {
            try {
                in = ((Blob) obj).getBinaryStream();
            } catch (Exception ex) {
                String msg = "Cannot retrieve the data from a Blob object.";
                throw new EvaluationException(msg, ex);
            }
        } else if (obj instanceof byte[]) {
            in = new ByteArrayInputStream((byte[]) obj);
        }
        return in;
    }
}
