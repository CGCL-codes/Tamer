package com.zhongkai.service.book;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.zhongkai.dao.book.CarBusinessDAO;
import com.zhongkai.dao.book.CarHistoryDAO;
import com.zhongkai.dao.book.CarRegisterDAO;
import com.zhongkai.model.book.TCsCclxdz;
import com.zhongkai.model.book.TDjCcdjxx;
import com.zhongkai.model.book.TDjCcdjxxLs;
import com.zhongkai.model.book.TDjCcywcl;
import com.zhongkai.service.BaseService;

@Transactional
@Component
public class CarRegisterService extends BaseService {

    private CarRegisterDAO CarRegisterDAO;

    private CarBusinessDAO carBusinessDAO;

    private CarHistoryDAO carHistoryDAO;

    @Resource
    public void setCarBusinessDAO(CarBusinessDAO carBusinessDAO) {
        this.carBusinessDAO = carBusinessDAO;
    }

    @Resource
    public void setCarRegisterDAO(CarRegisterDAO carRegisterDAO) {
        CarRegisterDAO = carRegisterDAO;
    }

    @Resource
    public void setCarHistoryDAO(CarHistoryDAO carHistoryDAO) {
        this.carHistoryDAO = carHistoryDAO;
    }

    /**
	 * 保存车船登记信息
	 * @author 吴嘉俊
	 * @param TDjCcdjxx
	 * @param TDjCcywcl
	 * @throws Exception
	 */
    public void SaveCarRegister(TDjCcdjxx tDjCcdjxx, TDjCcywcl tDjCcywcl) throws Exception {
        CarRegisterDAO.insert(tDjCcdjxx);
        carBusinessDAO.insert(tDjCcywcl);
    }

    /**
	 * 修改车船登记信息
	 * @author 吴嘉俊
	 * @param TDjCcdjxx
	 * @throws Exception
	 */
    public void UpdateCarRegister(TDjCcdjxx tDjCcdjxx, TDjCcywcl tDjCcywcl, TDjCcdjxxLs TDjCcdjxxLs) throws Exception {
        CarRegisterDAO.update(tDjCcdjxx);
        carBusinessDAO.insert(tDjCcywcl);
        TDjCcdjxxLs.setYwclXh(tDjCcywcl.getYwclXh());
        carHistoryDAO.insert(TDjCcdjxxLs);
    }

    /**
	 * 通过车船登记号查找车船登记信息
	 * @author 吴嘉俊
	 * @param CarNo
	 * @return TDjCcdjxx
	 * @throws Exception
	 */
    public TDjCcdjxx FindByCarNo(Integer CarNo) throws Exception {
        return (TDjCcdjxx) CarRegisterDAO.selectById(TDjCcdjxx.class, CarNo);
    }
}
