package jkook.vetan.model.hrm.beans;

import java.util.List;
import javax.ejb.Remote;
import jkook.vetan.model.hrm.HsHrEec;

/**
 *
 * @author kirank
 */
@Remote
public interface HsHrEecFacadeRemote {

    void create(HsHrEec hsHrEec);

    void edit(HsHrEec hsHrEec);

    void destroy(HsHrEec hsHrEec);

    HsHrEec find(Object pk);

    List findAll();
}
