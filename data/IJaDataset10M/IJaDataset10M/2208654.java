package net.cygeek.tech.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import java.util.ArrayList;
import java.util.Date;
import net.cygeek.tech.client.data.*;

/**
 * Author: Thilina Hasantha
 */
public interface EmpEmergencyContactsService extends RemoteService {

    ArrayList getEmpEmergencyContactss();

    Boolean addEmpEmergencyContacts(EmpEmergencyContacts mEmpEmergencyContacts, boolean isNew);

    Boolean deleteEmpEmergencyContacts(Double eecSeqno, int empNumber);

    ArrayList<EmpEmergencyContacts> getEmpEmergencyContactsByEmployee(int empNumber);

    EmpEmergencyContacts getEmpEmergencyContacts(Double eecSeqno, int empNumber);
}
