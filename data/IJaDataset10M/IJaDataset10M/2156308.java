package starcup.core.game.card;

import com.csvreader.CsvReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Akira.Pan
 */
public class InitCardHelper {

    private static Log logger = LogFactory.getLog(InitCardHelper.class);

    private String filePath = "D:/NetBeansProjects/agggame/trunk/build/classes/starcup/core/game/card/card_list.csv";

    private Map<String, InfoRecord> infoMap = new HashMap<String, InfoRecord>(50);

    private CsvReader reader = null;

    public void initAllInfo() {
        try {
            reader = new CsvReader(filePath, ',', Charset.forName("UTF-8"));
            reader.readHeaders();
            while (reader.readRecord()) {
                InfoRecord record = new InfoRecord();
                record.setNo(reader.get("编号"));
                record.setType(reader.get("种类"));
                record.setAttr(reader.get("属性"));
                record.setSkillType(reader.get("类别"));
                record.setName(reader.get("名称"));
                record.setCount(Integer.parseInt(reader.get("数量")));
                this.infoMap.put(record.getNo(), record);
                logger.info("初始化卡号为" + ":" + record.getNo() + "成功");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error("获取卡牌信息，文件不存在");
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("获取卡牌信息，文件读写错误");
        } finally {
            reader.close();
        }
    }

    public static void main(String[] args) {
        InitCardHelper helper = new InitCardHelper();
        helper.initAllInfo();
        System.out.println(helper.getInfoMap().get("B001").getName());
    }

    public Map<String, InfoRecord> getInfoMap() {
        return infoMap;
    }
}
