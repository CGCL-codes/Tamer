package jkook.vetan.model.hrm.beans;

import java.util.List;
import javax.ejb.Local;
import jkook.vetan.model.hrm.HsHrEmpContractExtend;

/**
 *
 * @author kirank
 */
@Local
public interface HsHrEmpContractExtendFacadeLocal {

    void create(HsHrEmpContractExtend hsHrEmpContractExtend);

    void edit(HsHrEmpContractExtend hsHrEmpContractExtend);

    void destroy(HsHrEmpContractExtend hsHrEmpContractExtend);

    HsHrEmpContractExtend find(Object pk);

    List findAll();
}
