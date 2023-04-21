package org.vardb.web.forms;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.vardb.analysis.COligoRepeatHelper;
import org.vardb.analysis.IAnalysisService;
import org.vardb.sequences.CSequenceFileParser;
import org.vardb.sequences.CSequenceType;
import org.vardb.util.CErrors;

@Controller
@RequestMapping(value = "/analysis/oligorep.html")
public class COligorepFormController extends CAbstractUploadFormController {

    @Resource(name = "analysisService")
    protected IAnalysisService analysisService;

    @RequestMapping(method = RequestMethod.GET)
    public String onLoad(Model model) {
        Form form = new Form();
        model.addAttribute("params", form);
        return "analysis/oligorepform";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String onSubmit(Form form, Model model, HttpServletRequest request, HttpServletResponse response) {
        CErrors errors = new CErrors();
        if (!uploadOkay(form, errors)) return onLoad(addErrors(model, errors));
        form.setSequences(CSequenceFileParser.readFastaAlignment(form.getAlignment(), form.getSequenceType()));
        form.setUser_id(getUserId());
        String task_id = this.analysisService.oligorepTask(form);
        return super.loadTask(request, response, task_id);
    }

    public static class Form extends COligoRepeatHelper.Params implements ISequenceUploadForm {

        private String file;

        private String text;

        private String alignmentIdentifier = null;

        public String getFile() {
            return this.file;
        }

        public void setFile(final String file) {
            this.file = file;
        }

        public String getText() {
            return this.text;
        }

        public void setText(final String text) {
            this.text = text;
        }

        public String getAlignmentIdentifier() {
            return this.alignmentIdentifier;
        }

        public void setAlignmentIdentifier(final String alignmentIdentifier) {
            this.alignmentIdentifier = alignmentIdentifier;
        }

        public String getAlignment() {
            return CAbstractUploadFormController.getContent(this.text, this.file);
        }

        public CSequenceType getSequenceType() {
            return CSequenceType.NT;
        }
    }
}
