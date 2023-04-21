package fr.albin.data.model.books;

import java.util.ArrayList;
import java.util.List;
import fr.albin.data.model.codes.Codes.Code;
import fr.albin.data.model.dao.jxb.JxbCodeDao;

public class CodeKeyUtils {

    /**
	 * Generates codes of a given rubric.
	 * 
	 * @param codes
	 * @param separator
	 * @return
	 */
    public static List<Code> generateCodes(String codesString, String separator, int type) {
        List<Code> result = new ArrayList<Code>();
        String[] codes = codesString.split(separator);
        for (String codeStr : codes) {
            Code code = new Code();
            code.setParent(type);
            code.setType(type);
            code.setLabel(codeStr);
            result.add(code);
        }
        JxbCodeDao.getInstance().addCodes(result);
        return result;
    }
}
