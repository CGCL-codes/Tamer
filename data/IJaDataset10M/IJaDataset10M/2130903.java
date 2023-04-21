package edu.nctu.csie.jichang.database.save;

import java.io.File;
import java.util.List;
import edu.nctu.csie.jichang.database.dbinfo.LoginInfo;
import edu.nctu.csie.jichang.database.model.cell.DBDatabase;

public interface ISave {

    /**
	 * insert data to database
	 * @param pConns connection set
	 * @param pDatabase DBDatabase
	 * @throws Exception if has error
	 */
    public void doSave(List<LoginInfo> pConns, DBDatabase pDatabase) throws Exception;

    /**
	 * save DBDatabase to File
	 * @param pFile File
	 * @param pDatabase DBDatabase
	 * @throws Exception if has error
	 */
    public void doSave(File pFile, DBDatabase pDatabase) throws Exception;
}
