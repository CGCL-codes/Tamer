package at.campus02.datapit.weka.dao;

import at.campus02.datapit.core.data.Model;
import at.campus02.datapit.weka.data.ClustererModel;

/**
 * 
 * @author Gerhard Schlager
 */
public interface IClustererModelDao {

    ClustererModel findByID(Long id);

    ClustererModel findByModel(Model model);

    void save(ClustererModel model);

    void delete(ClustererModel model);
}
