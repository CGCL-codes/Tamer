package bz.ziro.kanbe.dao;

import java.util.ArrayList;
import java.util.List;
import org.slim3.datastore.Datastore;
import com.google.appengine.api.datastore.Key;
import bz.ziro.kanbe.meta.TemplateMeta;
import bz.ziro.kanbe.model.Template;
import bz.ziro.kanbe.model.TemplateBrowser;
import bz.ziro.kanbe.util.KeyFactory;

/**
 * テンプレート用DAO
 * @author Administrator
 */
public class TemplateDao extends BaseDao<Template> {

    /**
	 * メタデータ
	 */
    private static final TemplateMeta templateMeta = new TemplateMeta();

    /**
     * 単一検索
     * @param aSiteKey
     * @param aKey
     * @return
     */
    public static Template find(Long aSiteKey, Long aKey) {
        Key key = KeyFactory.createTemplateKey(aSiteKey, aKey);
        return Datastore.query(templateMeta).filter(templateMeta.key.equal(key)).asSingle();
    }

    /**
     * 削除
     * @param aSiteKey
     * @param aKey
     */
    public static void delete(Long aSiteKey, Long aKey) {
        Key key = KeyFactory.createTemplateKey(aSiteKey, aKey);
        List<TemplateBrowser> browserList = TemplateBrowserDao.all(key);
        List<Key> keyList = new ArrayList<Key>();
        KeyFactory.addBrowserKey(browserList, keyList);
        keyList.add(key);
        Datastore.delete(keyList);
    }

    /**
     * 全件取得
     * @param siteKey
     * @return
     */
    public static List<Template> all(Long siteKey) {
        Key key = KeyFactory.createSiteKey(siteKey);
        return Datastore.query(templateMeta).filter(templateMeta.siteKey.equal(key)).asList();
    }
}
