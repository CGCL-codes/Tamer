package ddss.web;

import java.io.IOException;
import java.util.Date;
import org.apache.http.ParseException;
import org.ksoap2.serialization.Marshal;
import org.ksoap2.serialization.MarshalDate;
import org.ksoap2.serialization.SoapObject;
import org.xmlpull.v1.XmlPullParserException;
import ddss.common.webservice.WSAccessDefinitions;
import ddss.loader.Task;
import ddss.loader.TaskPersonnal;
import ddss.util.StringConverter;

public class GetAvailableTasksRequest extends WSRequest {

    private static final String METHOD_NAME = "GetAvailableTasks";

    private SoapObject m_Parameters;

    public String PhoneNumber;

    public String Imei;

    public GetAvailableTasksRequest() {
        super();
        Initialize();
    }

    public GetAvailableTasksRequest(int arg0) {
        super(arg0);
        Initialize();
    }

    private void Initialize() {
        setMethodName(METHOD_NAME);
        m_Parameters = new SoapObject(WSAccessDefinitions.getNamespace(), METHOD_NAME);
        m_Parameters.addProperty("phoneNumber", "");
        m_Parameters.addProperty("imei", "");
        setOutputSoapObject(m_Parameters);
        Marshal dateMarshal = new MarshalDate();
        dateMarshal.register(this);
        addMapping(WSAccessDefinitions.getNamespace(), "Task", Task.class);
    }

    @Override
    public Object Send() throws IOException, XmlPullParserException {
        m_Parameters.setProperty(0, PhoneNumber);
        m_Parameters.setProperty(1, Imei);
        return super.Send();
    }

    @Override
    protected Task[] BuildResponse(Object _soapResponse) {
        if (_soapResponse == null) throw new NullPointerException("No response from server.");
        if (_soapResponse instanceof SoapObject) {
            SoapObject resultSOAP = (SoapObject) _soapResponse;
            Task[] result = new Task[resultSOAP.getPropertyCount()];
            TaskPersonnal[] persons;
            for (int i = 0; i < result.length; i++) {
                SoapObject TaskSOAP = (SoapObject) resultSOAP.getProperty(i);
                int taskId = Integer.parseInt(TaskSOAP.getProperty("ID").toString());
                int TaskTypeId = Integer.parseInt(TaskSOAP.getProperty("TaskTypeId").toString());
                String Description = TaskSOAP.getProperty("Description").toString();
                String Name = TaskSOAP.getProperty("Name").toString();
                String strUDate = TaskSOAP.getProperty("UpdateDate").toString();
                String strIDate = TaskSOAP.getProperty("InitialDate").toString();
                String strFDate = TaskSOAP.getProperty("FinalDate").toString();
                Date dtUDate = StringConverter.getDateWS(strUDate);
                Date dtIDate = StringConverter.getDateWS(strIDate);
                Date dtFDate = StringConverter.getDateWS(strFDate);
                result[i] = new Task(taskId, TaskTypeId, Name, Description, dtUDate, dtIDate, dtFDate);
                SoapObject Personal = (SoapObject) TaskSOAP.getProperty("Personnel");
                persons = new TaskPersonnal[Personal.getPropertyCount()];
                for (int j = 0; j < persons.length; j++) {
                    SoapObject tpSOAP = (SoapObject) Personal.getProperty(j);
                    int taskPersonalId = Integer.parseInt(tpSOAP.getProperty("ID").toString());
                    int personId = Integer.parseInt(tpSOAP.getProperty("PersonID").toString());
                    String strAccountUpdateDate = tpSOAP.getProperty("UpdateDate").toString();
                    Date dtAccountUpdateDate = StringConverter.getDateWS(strAccountUpdateDate);
                    TaskPersonnal tp = new TaskPersonnal(taskPersonalId, taskId, personId, dtAccountUpdateDate);
                    persons[j] = tp;
                }
                result[i].Personnal = persons;
            }
            return result;
        }
        throw new ParseException("Unexpected response from server.");
    }
}
