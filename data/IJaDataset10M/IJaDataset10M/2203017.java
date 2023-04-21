package cn.myapps.core.expimp.exp.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import cn.myapps.constans.Environment;
import cn.myapps.core.expimp.exp.util.SQLPackage;
import cn.myapps.core.image.repository.ejb.ImageRepositoryProcess;
import cn.myapps.core.image.repository.ejb.ImageRepositoryVO;
import cn.myapps.util.ProcessFactory;

public class ExpImageRepository extends ExpElement {

    public ExpImageRepository(boolean expAll) {
        super(expAll);
    }

    private static final String NAME = "T_IMAGEREPOSITORY";

    public Map exportSQLS(String[] ids, Collection ignoreIds) throws Exception {
        Map rtn = new LinkedHashMap();
        StringBuffer query = new StringBuffer();
        for (int i = 0; i < ids.length; i++) {
            if (ignoreIds.contains(ids[i])) continue;
            ignoreIds.add(ids[i]);
            query.append("SELECT * FROM ");
            query.append(NAME + " ");
            query.append("WHERE id ='");
            query.append(ids[i] + "'");
            SQLPackage values = new SQLPackage(NAME, query.toString());
            if (!isExpAll()) {
                values.addRestColumn(FOREINGKEY_COLUMN_APPLICATION);
            }
            rtn.put(ids[i], values);
            query = new StringBuffer();
        }
        return rtn;
    }

    public Collection getImageUrls(String[] ids, Environment evt) throws Exception {
        Collection rtn = new ArrayList();
        for (int i = 0; i < ids.length; i++) {
            String id = ids[i];
            ImageRepositoryProcess process = (ImageRepositoryProcess) ProcessFactory.createProcess(ImageRepositoryProcess.class);
            ImageRepositoryVO imglib = (ImageRepositoryVO) process.doView(id);
            String url = imglib.getContent();
            if (url != null && url.trim().length() > 0) {
                rtn.add(evt.getRealPath(url));
            }
        }
        return rtn;
    }

    public String getName() {
        return NAME;
    }
}
