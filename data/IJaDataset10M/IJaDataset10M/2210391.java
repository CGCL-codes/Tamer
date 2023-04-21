package net.sf.ifw2rep.ui.util;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.infordata.ifw2.web.DialogResultEnum;
import net.infordata.ifw2.web.bnds.ActionInfo;
import net.infordata.ifw2.web.bnds.IBndsFlow;
import net.infordata.ifw2.web.bnds.IFieldAction;
import net.infordata.ifw2.web.ctrl.FlowContext;
import net.infordata.ifw2.web.ctrl.IDialogCallback;
import net.infordata.ifw2.web.ctrl.IFlow;
import net.infordata.ifw2.web.ctrl.IFlowAsDialog;
import net.infordata.ifw2.web.ctrl.IFlowEndState;
import net.infordata.ifw2.web.ctrl.IFlowState;
import net.infordata.ifw2m.mdl.flds.FieldDefinition;
import net.infordata.ifw2m.mdl.flds.FieldMetaData;
import net.infordata.ifw2m.mdl.flds.FieldSet;
import net.infordata.ifw2m.mdl.flds.IFieldSet;
import net.infordata.ifw2m.mdl.flds.StringField;
import net.infordata.ifw2m.web.AFieldSetForm;
import net.infordata.ifw2m.web.AListFlow;

public class LookupList implements IFieldAction {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String destFormFiled = null;

    private AFieldSetForm destForm = null;

    private List<String> list = null;

    private String label = null;

    private static final FieldSet LIST_FS = new FieldSet(new FieldDefinition(new FieldMetaData("tgroup", "Groups").setPreferredSize(20), new StringField()));

    @Override
    public void execute(IBndsFlow flow, HttpServletRequest request, String formName, String fieldName, ActionInfo action) {
        final AListFlow<String> listFlow = new AListFlow<String>(LIST_FS, "tgroup", destFormFiled, DialogResultEnum.OK, DialogResultEnum.CANCEL) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void beginLoading() {
            }

            @Override
            protected void endLoading() {
            }

            @Override
            protected List<String> getData() {
                return list;
            }

            @Override
            protected FieldSet pojoToFieldSet(String source, FieldSet dest) {
                dest.<String>get("tgroup").setValue((String) source);
                return dest;
            }
        };
        FlowContext.get().modalDialog(listFlow.asDialog("Select Group").setWidth("270px"), new IDialogCallback() {

            private static final long serialVersionUID = 1L;

            @Override
            public IFlowState endDialog(IFlow flow, IFlowAsDialog dialog, IFlowEndState res) {
                if (res != DialogResultEnum.OK) return null;
                IFieldSet fs = listFlow.getSelection();
                if (fs != null) {
                    destForm.getFieldSetField(destFormFiled).setValue(fs.<String>get("tgroup").getValue());
                    destForm.validate(destFormFiled);
                }
                return null;
            }
        });
    }

    public LookupList(List<String> list, AFieldSetForm destForm, String destFormFiled, String label) {
        this.destFormFiled = destFormFiled;
        this.destForm = destForm;
        this.list = list;
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
