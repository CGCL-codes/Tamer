package jkook.vetan.model.hrm.beans;

import java.util.List;
import javax.ejb.Local;
import jkook.vetan.model.hrm.HsHrLicenses;

/**
 *
 * @author kirank
 */
@Local
public interface HsHrLicensesFacadeLocal {

    void create(HsHrLicenses hsHrLicenses);

    void edit(HsHrLicenses hsHrLicenses);

    void destroy(HsHrLicenses hsHrLicenses);

    HsHrLicenses find(Object pk);

    List findAll();
}
