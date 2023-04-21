package nl.openu.tiles.model.impl;

import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.CacheModel;
import nl.openu.tiles.model.TileLayout;

/**
 * The cache model class for representing TileLayout in entity cache.
 *
 * @author Hubert Vogten, Harrie Martens
 * @see TileLayout
 * @generated
 */
public class TileLayoutCacheModel implements CacheModel<TileLayout> {

    @Override
    public String toString() {
        StringBundler sb = new StringBundler(9);
        sb.append("{tileLayoutId=");
        sb.append(tileLayoutId);
        sb.append(", plid=");
        sb.append(plid);
        sb.append(", userId=");
        sb.append(userId);
        sb.append(", layout=");
        sb.append(layout);
        sb.append("}");
        return sb.toString();
    }

    public TileLayout toEntityModel() {
        TileLayoutImpl tileLayoutImpl = new TileLayoutImpl();
        tileLayoutImpl.setTileLayoutId(tileLayoutId);
        tileLayoutImpl.setPlid(plid);
        tileLayoutImpl.setUserId(userId);
        if (layout == null) {
            tileLayoutImpl.setLayout(StringPool.BLANK);
        } else {
            tileLayoutImpl.setLayout(layout);
        }
        tileLayoutImpl.resetOriginalValues();
        return tileLayoutImpl;
    }

    public long tileLayoutId;

    public long plid;

    public long userId;

    public String layout;
}
