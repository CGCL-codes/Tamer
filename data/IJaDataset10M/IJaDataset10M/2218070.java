package com.rb.ft.service;

import java.util.List;
import com.rb.ft.database.pojo.FtFootballer;

/**
 * @类功能说明: 球员service interface
 * @类修改者:   
 * @修改日期:   
 * @修改说明:   
 * @作者:       robin
 * @创建时间:   2011-9-4 上午09:23:48
 * @版本:       1.0.0
 */
public interface IFtFootballerService {

    /**
	 * @方法说明: 
	 * @参数:     @param keyword
	 * @参数:     @return
	 * @return    List<FtFootballer>
	 * @throws
	 */
    public List<FtFootballer> findAllFootballer(String keyword);
}
