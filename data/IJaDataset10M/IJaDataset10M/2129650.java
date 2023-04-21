package jkook.vetan.model.hrm.beans;

import java.util.List;
import javax.ejb.Remote;
import jkook.vetan.model.hrm.HsHrEmpChildren;

/**
 *
 * @author kirank
 */
@Remote
public interface HsHrEmpChildrenFacadeRemote {

    void create(HsHrEmpChildren hsHrEmpChildren);

    void edit(HsHrEmpChildren hsHrEmpChildren);

    void destroy(HsHrEmpChildren hsHrEmpChildren);

    HsHrEmpChildren find(Object pk);

    List findAll();
}
