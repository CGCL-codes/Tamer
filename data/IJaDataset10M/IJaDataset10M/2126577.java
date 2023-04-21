package br.ufrj.cad.view.professor.planilha;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;
import br.ufrj.cad.model.util.ArrayUtil;

public class SalvarXmlLattesForm extends ActionForm {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5408634939593923443L;

    private FormFile Filedata;

    public void setFiledata(FormFile filedata) {
        Filedata = filedata;
    }

    public FormFile getFiledata() {
        return Filedata;
    }
}
