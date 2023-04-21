package net.cygeek.tech.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import java.util.ArrayList;
import java.util.Date;
import net.cygeek.tech.client.data.*;

/**
 * Author: Thilina Hasantha
 */
public interface ProvinceService extends RemoteService {

    ArrayList getProvinces();

    Boolean addProvince(Province mProvince, boolean isNew);

    Boolean deleteProvince(int id);

    Province getProvince(int id);
}
