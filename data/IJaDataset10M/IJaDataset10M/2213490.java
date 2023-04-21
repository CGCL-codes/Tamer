package jkook.vetan.model.hrm.beans;

import java.util.List;
import javax.ejb.Remote;
import jkook.vetan.model.hrm.HsHrCompstructtree;

/**
 *
 * @author kirank
 */
@Remote
public interface HsHrCompstructtreeFacadeRemote {

    void create(HsHrCompstructtree hsHrCompstructtree);

    void edit(HsHrCompstructtree hsHrCompstructtree);

    void destroy(HsHrCompstructtree hsHrCompstructtree);

    HsHrCompstructtree find(Object pk);

    List findAll();
}
