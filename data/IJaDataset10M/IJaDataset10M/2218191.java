package l1jDevils;

import java.sql.Timestamp;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import l1jDevils.common.StaticUtil;
import l1jDevils.dao.DevilsDao;
import l1jDevils.dto.DevilsDropLimitDto;

public class DropLimit {

    private static Logger _log = Logger.getLogger(DropLimit.class.getName());

    private static DropLimit _instance;

    private static final long MSEC_DAY = 86400000;

    private static final long MSEC_HOUR = 3600000;

    private static final long MSEC_MINUTE = 60000;

    private Map<Integer, DevilsDropLimitDto> _itemLimitMap = null;

    public static DropLimit getInstance() {
        if (_instance == null) {
            _instance = new DropLimit();
        }
        return _instance;
    }

    private DropLimit() {
        this._itemLimitMap = DevilsDao.getInstance().getDevilsDropLimit();
    }

    public boolean isVaildDrop(int itemId) {
        boolean result = false;
        try {
            DevilsDropLimitDto dto = _itemLimitMap.get(new Integer(itemId));
            if (dto != null) {
                if (dto.getDropCount() == 0 || dto.getLastDropDate().getTime() + (dto.getIntervalDay() * MSEC_DAY) + (dto.getIntervalHour() * MSEC_HOUR) + (dto.getIntervalMinute() * MSEC_MINUTE) <= StaticUtil.getSysTime()) {
                    dto.setDropCount(dto.getDropCount() + 1);
                    dto.setLastDropDate(new Timestamp(StaticUtil.getSysTime()));
                    DevilsDao.getInstance().updateDevilsDropLimit(dto);
                    result = true;
                }
            } else {
                result = true;
            }
        } catch (Exception e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        return result;
    }

    public static void test() {
        DropLimit.getInstance();
        try {
            for (int i = 0; i < 100; ++i) {
                System.out.println(i + ": " + DropLimit.getInstance().isVaildDrop(1));
                Thread.sleep(30 * 1000);
            }
        } catch (Exception e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }
}
