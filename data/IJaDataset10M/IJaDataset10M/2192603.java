package jkook.vetan.model.hrm.beans;

import java.util.List;
import javax.ejb.Local;
import jkook.vetan.model.hrm.HsHrEmpBasicsalary;

/**
 *
 * @author kirank
 */
@Local
public interface HsHrEmpBasicsalaryFacadeLocal {

    void create(HsHrEmpBasicsalary hsHrEmpBasicsalary);

    void edit(HsHrEmpBasicsalary hsHrEmpBasicsalary);

    void destroy(HsHrEmpBasicsalary hsHrEmpBasicsalary);

    HsHrEmpBasicsalary find(Object pk);

    List findAll();
}
