package sunsite.service;

import java.util.List;

/**
 *
 * @author Ruby
 */
public interface SaiService {

    List<sunsite.po.Sai> getSaiList();

    sunsite.po.Sai getSai(int saiId);
}
