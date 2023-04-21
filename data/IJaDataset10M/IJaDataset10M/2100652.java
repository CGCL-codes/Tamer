package de.peacei.android.foodwatcher.data.database;

import de.peacei.android.foodwatcher.data.database.DaoSession;
import de.greenrobot.dao.DaoException;

/**
 * Entity mapped to table MEAL_ENTITY (schema version 1).
 */
public class MealEntity {

    private Long id;

    private String name;

    private String desc;

    private Byte extra;

    private Byte type;

    private String studentPrice;

    private String staffPrice;

    private Long menuId;

    /** Used to resolve relations */
    private DaoSession daoSession;

    private MenuEntity menuEntity;

    private Long menuEntity__resolvedKey;

    public MealEntity() {
    }

    public MealEntity(Long id) {
        this.id = id;
    }

    public MealEntity(Long id, String name, String desc, Byte extra, Byte type, String studentPrice, String staffPrice, Long menuId) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.extra = extra;
        this.type = type;
        this.studentPrice = studentPrice;
        this.staffPrice = staffPrice;
        this.menuId = menuId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Byte getExtra() {
        return extra;
    }

    public void setExtra(Byte extra) {
        this.extra = extra;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getStudentPrice() {
        return studentPrice;
    }

    public void setStudentPrice(String studentPrice) {
        this.studentPrice = studentPrice;
    }

    public String getStaffPrice() {
        return staffPrice;
    }

    public void setStaffPrice(String staffPrice) {
        this.staffPrice = staffPrice;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    /** To-one relationship, resolved on first access. */
    public MenuEntity getMenuEntity() {
        if (menuEntity__resolvedKey == null || !menuEntity__resolvedKey.equals(menuId)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MenuEntityDao dao = daoSession.getMenuEntityDao();
            menuEntity = dao.load(menuId);
            menuEntity__resolvedKey = menuId;
        }
        return menuEntity;
    }

    public void setMenuEntity(MenuEntity menuEntity) {
        this.menuEntity = menuEntity;
        menuId = menuEntity == null ? null : menuEntity.getId();
        menuEntity__resolvedKey = menuId;
    }
}
