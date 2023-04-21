package org.objectwiz.franchisemgmtDAO;

import java.util.List;
import javax.ejb.Local;
import org.objectwiz.franchisemgmt.MoralEntity;

/**
 *
 * @author xym
 */
@Local
public interface MoralEntityFacadeLocal {

    void create(MoralEntity moralEntity);

    void edit(MoralEntity moralEntity);

    void remove(MoralEntity moralEntity);

    MoralEntity find(Object id);

    List<MoralEntity> findAll();

    List<MoralEntity> findRange(int[] range);

    int count();
}
