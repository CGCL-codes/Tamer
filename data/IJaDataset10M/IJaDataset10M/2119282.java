package jp.ac.segakuen;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class SelectListTable {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private String listText;

    @Persistent
    private String displayPlaceHolder;

    @Persistent
    private Long sortOrder;

    public SelectListTable(String sortOrder, String listText, String displayPlaceHolder) {
        this.sortOrder = Long.valueOf(sortOrder);
        this.listText = listText;
        this.displayPlaceHolder = displayPlaceHolder;
    }

    public Long getSortOrder() {
        return sortOrder;
    }

    public String getStringSortOrder() {
        return Long.toString(sortOrder);
    }

    public void setSortOrder(Long sortOrder) {
        this.sortOrder = sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = Long.valueOf(sortOrder);
    }

    public Long getId() {
        return id;
    }

    public String getStringId() {
        return Long.toString(id);
    }

    public String getListText() {
        return listText;
    }

    public void setListText(String listText) {
        this.listText = listText;
    }

    public String getDisplayPlaceHolder() {
        return displayPlaceHolder;
    }

    public void setDisplayPlaceHolder(String displayPlaceHolder) {
        this.displayPlaceHolder = displayPlaceHolder;
    }
}
