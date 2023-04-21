package common.dao;

import common.beans.AFileItem;
import org.apache.log4j.Logger;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class FileDAOImpl implements IFileDAO {

    final Logger logger = Logger.getLogger(getClass());

    @Override
    public boolean save(AFileItem item) {
        if (item.getContent() != null) {
            logger.info("file saved");
        } else if (item.getContent_file() != null) {
            logger.info("file saved");
        }
        throw new NullPointerException("file item contains no file");
    }

    @Override
    public boolean delete(AFileItem item) {
        if (item.getContent() != null) {
            logger.info("file deleted:" + item.getContent().getOriginalFilename());
        } else if (item.getContent_file() != null) {
            logger.info("file deleted:" + item.getContent_file().getAbsolutePath());
        } else if (item.getFile_name() != null) {
            logger.info("file deleted:" + item.getFile_name());
        }
        throw new NullPointerException("no file can be found by this file item");
    }
}
