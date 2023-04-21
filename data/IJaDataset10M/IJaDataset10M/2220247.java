package org.vardb.web.forms;

import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.support.StringMultipartFileEditor;
import org.vardb.CVardbException;
import org.vardb.alignment.CCodingSequencePairs;
import org.vardb.alignment.IAlignmentService;
import org.vardb.sequences.CSequenceFileParser;
import org.vardb.sequences.CSequenceType;
import org.vardb.util.CErrors;
import org.vardb.util.CStringHelper;
import org.vardb.web.CAbstractController;

public abstract class CAbstractUploadFormController extends CAbstractController {

    @Resource(name = "alignmentService")
    protected IAlignmentService alignmentService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringMultipartFileEditor());
    }

    protected String getOriginalFilename(HttpServletRequest request, String name) {
        if (!(request instanceof MultipartHttpServletRequest)) throw new CVardbException("request is not an instance of MultipartHttpServletRequest");
        MultipartHttpServletRequest multipart = (MultipartHttpServletRequest) request;
        CommonsMultipartFile file = (CommonsMultipartFile) multipart.getFileMap().get(name);
        return file.getOriginalFilename();
    }

    protected String stripFiletype(String filename) {
        int index = filename.lastIndexOf('.');
        return filename.substring(0, index);
    }

    protected String getText(byte[] bytes) {
        if (bytes == null) return null;
        StringBuffer buffer = new StringBuffer();
        for (int index = 0; index < bytes.length; index++) {
            buffer.append((char) bytes[index]);
        }
        return buffer.toString();
    }

    protected boolean uploadOkay(ISequenceUploadForm form, CErrors errors) {
        return uploadOkay(form.getAlignment(), errors);
    }

    protected boolean uploadOkay(DualSequenceUploadForm form, CErrors errors) {
        return (uploadOkay(form.getAaAlignment(), errors) && uploadOkay(form.getNtAlignment(), errors));
    }

    protected boolean uploadOkay(String str, CErrors errors) {
        if (!CStringHelper.hasContent(str)) {
            errors.addError("Cannot find uploaded content. Please make sure that the textarea or uploaded file is in the correct format. Submitted: " + str);
            return false;
        }
        if (CStringHelper.isSpam(str)) {
            errors.addError("The submitted text appears to contain links and so is filtered to reduce spam");
            return false;
        }
        return true;
    }

    protected static String getContent(String textarea, String file) {
        String content = textarea;
        if (CStringHelper.hasContent(file)) content = file;
        content = content.trim();
        return content;
    }

    protected Map<String, String> getAlignment(ISequenceUploadForm form, CSequenceType type, CErrors errors) {
        if (CStringHelper.hasContent(form.getAlignmentIdentifier())) return alignmentService.getAlignment(form.getAlignmentIdentifier()).getMap();
        String str = form.getAlignment();
        if (!uploadOkay(str, errors)) return null;
        Map<String, String> alignment = CSequenceFileParser.readAlignment(str, type);
        if (alignment.isEmpty()) {
            errors.addError("Cannot find uploaded content. Please make sure that the textarea or uploaded file is in the correct format. Submitted: " + str);
            return null;
        }
        return alignment;
    }

    public interface ISequenceUploadForm {

        String getAlignmentIdentifier();

        String getAlignment();

        CSequenceType getSequenceType();
    }

    public static class SequenceUploadForm implements ISequenceUploadForm {

        private String file;

        private String sequences = "";

        private String alignmentIdentifier = null;

        private CSequenceType sequenceType = CSequenceType.AA;

        public String getFile() {
            return this.file;
        }

        public void setFile(final String file) {
            this.file = file;
        }

        public String getSequences() {
            return this.sequences;
        }

        public void setSequences(final String sequences) {
            this.sequences = sequences;
        }

        public String getAlignmentIdentifier() {
            return this.alignmentIdentifier;
        }

        public void setAlignmentIdentifier(final String alignmentIdentifier) {
            this.alignmentIdentifier = alignmentIdentifier;
        }

        public CSequenceType getSequenceType() {
            return this.sequenceType;
        }

        public void setSequenceType(final CSequenceType sequenceType) {
            this.sequenceType = sequenceType;
        }

        public String getAlignment() {
            return CAbstractUploadFormController.getContent(this.sequences, this.file);
        }
    }

    public static class DualSequenceUploadForm {

        private String aafile;

        private String aasequences = "";

        private String ntfile;

        private String ntsequences = "";

        public String getAafile() {
            return this.aafile;
        }

        public void setAafile(final String aafile) {
            this.aafile = aafile;
        }

        public String getAasequences() {
            return this.aasequences;
        }

        public void setAasequences(final String aasequences) {
            this.aasequences = aasequences;
        }

        public String getNtfile() {
            return this.ntfile;
        }

        public void setNtfile(final String ntfile) {
            this.ntfile = ntfile;
        }

        public String getNtsequences() {
            return this.ntsequences;
        }

        public void setNtsequences(final String ntsequences) {
            this.ntsequences = ntsequences;
        }

        public String getAaAlignment() {
            return CAbstractUploadFormController.getContent(this.aasequences, this.aafile);
        }

        public String getNtAlignment() {
            return CAbstractUploadFormController.getContent(this.ntsequences, this.ntfile);
        }

        public CCodingSequencePairs getCodingSequencePairs() {
            return new CCodingSequencePairs(getAaAlignment(), getNtAlignment());
        }
    }
}
