package nl.openu.tiles.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used by SOAP remote services.
 *
 * @author    Hubert Vogten, Harrie Martens
 * @generated
 */
public class TileProviderSoap implements Serializable {

    public static TileProviderSoap toSoapModel(TileProvider model) {
        TileProviderSoap soapModel = new TileProviderSoap();
        soapModel.setTileProviderId(model.getTileProviderId());
        soapModel.setCompanyId(model.getCompanyId());
        soapModel.setAssetCategoryId(model.getAssetCategoryId());
        soapModel.setProviderPortletId(model.getProviderPortletId());
        soapModel.setProviderClassName(model.getProviderClassName());
        soapModel.setTitle(model.getTitle());
        soapModel.setContext(model.getContext());
        return soapModel;
    }

    public static TileProviderSoap[] toSoapModels(TileProvider[] models) {
        TileProviderSoap[] soapModels = new TileProviderSoap[models.length];
        for (int i = 0; i < models.length; i++) {
            soapModels[i] = toSoapModel(models[i]);
        }
        return soapModels;
    }

    public static TileProviderSoap[][] toSoapModels(TileProvider[][] models) {
        TileProviderSoap[][] soapModels = null;
        if (models.length > 0) {
            soapModels = new TileProviderSoap[models.length][models[0].length];
        } else {
            soapModels = new TileProviderSoap[0][0];
        }
        for (int i = 0; i < models.length; i++) {
            soapModels[i] = toSoapModels(models[i]);
        }
        return soapModels;
    }

    public static TileProviderSoap[] toSoapModels(List<TileProvider> models) {
        List<TileProviderSoap> soapModels = new ArrayList<TileProviderSoap>(models.size());
        for (TileProvider model : models) {
            soapModels.add(toSoapModel(model));
        }
        return soapModels.toArray(new TileProviderSoap[soapModels.size()]);
    }

    public TileProviderSoap() {
    }

    public long getPrimaryKey() {
        return _tileProviderId;
    }

    public void setPrimaryKey(long pk) {
        setTileProviderId(pk);
    }

    public long getTileProviderId() {
        return _tileProviderId;
    }

    public void setTileProviderId(long tileProviderId) {
        _tileProviderId = tileProviderId;
    }

    public long getCompanyId() {
        return _companyId;
    }

    public void setCompanyId(long companyId) {
        _companyId = companyId;
    }

    public long getAssetCategoryId() {
        return _assetCategoryId;
    }

    public void setAssetCategoryId(long assetCategoryId) {
        _assetCategoryId = assetCategoryId;
    }

    public String getProviderPortletId() {
        return _providerPortletId;
    }

    public void setProviderPortletId(String providerPortletId) {
        _providerPortletId = providerPortletId;
    }

    public String getProviderClassName() {
        return _providerClassName;
    }

    public void setProviderClassName(String providerClassName) {
        _providerClassName = providerClassName;
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        _title = title;
    }

    public String getContext() {
        return _context;
    }

    public void setContext(String context) {
        _context = context;
    }

    private long _tileProviderId;

    private long _companyId;

    private long _assetCategoryId;

    private String _providerPortletId;

    private String _providerClassName;

    private String _title;

    private String _context;
}
