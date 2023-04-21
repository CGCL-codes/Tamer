package common;

import persistence.com.bosspolis.crm.admin.Member;
import biz.com.bosspolis.crm.AppConstants;
import com.eis.ds.businesstieradapter.ServiceDelegate;
import com.eis.ds.businesstieradapter.ServiceDelegateFactory;
import com.eis.ds.core.commandpattern.CommandException;
import com.eis.ds.core.dto.DTOArea;

public class TestCreateMemberCommand {

    public static void main(String[] args) {
        DTOArea dto = new DTOArea();
        dto.setCommand("common.createMemberCommand");
        Member u = new Member();
        u.setMemberId(1);
        u.setMemberName("m5");
        u.setMemberType("Normal");
        u.setAddress1("Addr4");
        u.setAddress2("Addr4");
        u.setCountry("CN44");
        u.setStatus("logout222");
        dto.addParamItem(AppConstants.KEY_MEMBER, u);
        ServiceDelegate sd = ServiceDelegateFactory.getServiceDelegate();
        sd.setDTOArea(dto);
        sd.execute();
        dto = sd.getDTOArea();
        Member mm = (Member) dto.getParamItem(AppConstants.KEY_MEMBER);
        System.out.println("member Id = " + mm.getMemberId());
        if (dto.hasException()) {
            ((CommandException) dto.getExceptions().get(0)).printStackTrace();
        }
    }
}
