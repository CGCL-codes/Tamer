package es.ehrflex.client.main.medicalData.panel;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import es.ehrflex.client.main.event.EHRflexEvent;
import es.ehrflex.client.main.event.EHRflexEventType;
import es.ehrflex.client.main.medicalData.MedicalDataPanel;
import es.ehrflex.client.main.user.model.PatientDataModel;

public class MedicalRecordItem extends TabItem implements Listener<EHRflexEvent> {

    public String content = null;

    public String recordName = null;

    public PatientDataModel patient = null;

    public MedicalRecordItem(PatientDataModel pPatient, String pRecordName) {
        this.recordName = pRecordName;
        this.patient = pPatient;
        this.setText(this.recordName);
        this.setIconStyle("icon-medicaldata-report");
        this.setLayout(new FlowLayout());
        this.setScrollMode(Scroll.AUTO);
        Dispatcher.get().addListener(EHRflexEventType.SHOW_MEDICAL_RECORD.getEventType(), this);
    }

    public void handleEvent(EHRflexEvent be) {
        switch(be.mEventType) {
            case SHOW_MEDICAL_RECORD:
                if (((MedicalRecordItem) be.getData("TAB")).equals(this)) {
                    this.content = "";
                    MedicalDataPanel data = new MedicalDataPanel((String) be.getData("data"));
                    this.add(data);
                    this.layout();
                }
                break;
        }
    }
}
