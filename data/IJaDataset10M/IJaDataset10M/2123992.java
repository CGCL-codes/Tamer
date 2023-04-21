package net.cygeek.tech.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import java.util.ArrayList;
import java.util.Date;
import net.cygeek.tech.client.data.*;

/**
 * Author: Thilina Hasantha
 */
public interface DistrictService extends RemoteService {

    ArrayList getDistricts();

    Boolean addDistrict(District mDistrict, boolean isNew);

    Boolean deleteDistrict(String districtCode);

    District getDistrict(String districtCode);
}
