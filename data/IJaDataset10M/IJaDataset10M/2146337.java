package org.openremote.beehive.serviceHibernateImpl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.openremote.beehive.api.dto.VendorDTO;
import org.openremote.beehive.api.service.VendorService;
import org.openremote.beehive.domain.Vendor;

/**
 * {@inheritDoc}
 * 
 * @author allen 2009-2-17
 */
public class VendorServiceImpl extends BaseAbstractService<Vendor> implements VendorService {

    private static Logger logger = Logger.getLogger(VendorServiceImpl.class.getName());

    /**
    * {@inheritDoc }
    */
    public List<VendorDTO> loadAllVendors() {
        List<VendorDTO> vendorDTOs = new ArrayList<VendorDTO>();
        for (Vendor vendor : loadAll()) {
            VendorDTO vendorDTO = new VendorDTO();
            try {
                BeanUtils.copyProperties(vendorDTO, vendor);
            } catch (IllegalAccessException e) {
                logger.error("error occurs while BeanUtils.copyProperties(vendorDTO, vendor);");
            } catch (InvocationTargetException e) {
                logger.error("error occurs while BeanUtils.copyProperties(vendorDTO, vendor);");
            }
            vendorDTOs.add(vendorDTO);
        }
        return vendorDTOs;
    }
}
