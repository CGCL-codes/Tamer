package manage.action;

import java.util.ArrayList;
import java.util.List;
import common.bean.Option;
import manage.model.AssetType;
import manage.service.AssetTypeService;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author xuweigui
 *
 */
@SuppressWarnings("serial")
public class AssetTypeAction extends ActionSupport {

    private List<AssetType> assetTypes = new ArrayList<AssetType>();

    private AssetTypeService assetTypeService;

    private AssetType assetType;

    private String result;

    private Integer parentId = null;

    private List<Option> options = new ArrayList<Option>();

    public String deleteAssetType() {
        assetTypeService.remove(assetType.getId());
        return SUCCESS;
    }

    public String execute() {
        assetTypes = assetTypeService.findAll();
        return SUCCESS;
    }

    public String createAssetType() {
        assetTypeService.add(assetType);
        return SUCCESS;
    }

    public String getAssetTypeOption() {
        if (parentId == null) {
            assetTypes = assetTypeService.findAll();
        } else {
            assetTypes = assetTypeService.findChildren(parentId);
        }
        for (AssetType assetType : assetTypes) {
            Option option = new Option();
            option.setId(assetType.getId());
            option.setName(assetType.getName());
            option.setParentId(assetType.getParentId());
            options.add(option);
        }
        return SUCCESS;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public List<AssetType> getAssetTypes() {
        return assetTypes;
    }

    public void setAssetTypes(List<AssetType> assetTypes) {
        this.assetTypes = assetTypes;
    }

    public AssetTypeService getAssetTypeService() {
        return assetTypeService;
    }

    public void setAssetTypeService(AssetTypeService assetTypeService) {
        this.assetTypeService = assetTypeService;
    }

    public AssetType getAssetType() {
        return assetType;
    }

    public void setAssetType(AssetType assetType) {
        this.assetType = assetType;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
