package org.nomicron.suber.model.bean;

import org.nomicron.suber.enums.EntryFieldType;
import org.nomicron.suber.enums.PageStatus;
import org.nomicron.suber.enums.PlayerStatus;
import org.nomicron.suber.enums.SortType;
import org.nomicron.suber.model.factory.MetaFactory;
import org.nomicron.suber.model.factory.PageFactory;
import org.nomicron.suber.model.factory.PlayerFactory;
import org.nomicron.suber.model.object.GameState;
import org.nomicron.suber.model.object.MessageObject;
import org.nomicron.suber.model.object.Player;
import org.nomicron.suber.validation.Validation;
import com.dreamlizard.miles.model.SelectableFactory;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Bean for ElementTypes.
 */
public class ElementType<E extends Enum<E>> extends NamedBean {

    private EntryFieldType entryFieldType;

    private Integer orderLevel;

    private Set<Validation> validationSet = new HashSet<Validation>();

    private Set<AlterationType> editRightsSet = new HashSet<AlterationType>();

    private Class<E> enumClass;

    private Class<NamedBean> namedBeanClass;

    private List<NamedBean> beanSelectList;

    private Boolean useHtmlDiff = true;

    private Boolean textFlag = false;

    private Boolean hidden = false;

    private Boolean userAdd = false;

    public EntryFieldType getEntryFieldType() {
        return entryFieldType;
    }

    public void setEntryFieldType(EntryFieldType entryFieldType) {
        this.entryFieldType = entryFieldType;
    }

    public Integer getOrderLevel() {
        return orderLevel;
    }

    public void setOrderLevel(Integer orderLevel) {
        this.orderLevel = orderLevel;
    }

    public Set<Validation> getValidationSet() {
        return validationSet;
    }

    public void setValidationSet(Set<Validation> validationSet) {
        this.validationSet = validationSet;
    }

    public Set<AlterationType> getEditRightsSet() {
        return editRightsSet;
    }

    public void setEditRightsSet(Set<AlterationType> editRightsSet) {
        this.editRightsSet = editRightsSet;
    }

    public boolean hasEditRights(AlterationType alterationType) {
        return getEditRightsSet().contains(alterationType);
    }

    public Class<E> getEnumClass() {
        return enumClass;
    }

    public void setEnumClass(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    public Class<NamedBean> getNamedBeanClass() {
        return namedBeanClass;
    }

    public void setNamedBeanClass(Class<NamedBean> namedBeanClass) {
        this.namedBeanClass = namedBeanClass;
    }

    public List<NamedBean> getBeanSelectList() {
        return beanSelectList;
    }

    public void setBeanSelectList(List<NamedBean> beanSelectList) {
        this.beanSelectList = beanSelectList;
    }

    public Boolean getUseHtmlDiff() {
        return useHtmlDiff;
    }

    public void setUseHtmlDiff(Boolean useHtmlDiff) {
        this.useHtmlDiff = useHtmlDiff;
    }

    public Boolean getTextFlag() {
        return textFlag;
    }

    public void setTextFlag(Boolean textFlag) {
        this.textFlag = textFlag;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Boolean getUserAdd() {
        return userAdd;
    }

    public void setUserAdd(Boolean userAdd) {
        this.userAdd = userAdd;
    }

    public List getSelectList() {
        LinkedList selectList = null;
        GameState gameState = MetaFactory.getInstance().getGameStateFactory().getGameState();
        if (getEntryFieldType() != null) {
            if ((EntryFieldType.SELECT.equals(getEntryFieldType())) || (EntryFieldType.ADMIN_SELECT.equals(getEntryFieldType()))) {
                selectList = new LinkedList(Arrays.asList(getEnumClass().getEnumConstants()));
            } else if (EntryFieldType.SELECT_PLAYER.equals(getEntryFieldType())) {
                PlayerFactory playerFactory = MetaFactory.getInstance().getPlayerFactory();
                SelectableFactory selectableFactory = MetaFactory.getInstance().getSelectableFactory();
                List<Player> playerList = playerFactory.getPlayerListByStatus(PlayerStatus.ACTIVE);
                Collections.sort(playerList, new Player.IdentityComparator(SortType.ASCENDING));
                selectList = selectableFactory.getSelectableListFromPersistentNamed(playerList);
                selectList.addFirst(selectableFactory.getDummy("<None>"));
            } else if ((EntryFieldType.SELECT_PAGE.equals(getEntryFieldType())) || (EntryFieldType.DISPLAY_PAGE.equals(getEntryFieldType()))) {
                PageFactory pageFactory = MetaFactory.getInstance().getPageFactory();
                selectList = new LinkedList();
                selectList.addAll(pageFactory.getPageListByBookTurnAndStatus(null, gameState.getNextTurn(), PageStatus.EFFECTIVE));
                selectList.addAll(pageFactory.getPageListByBookTurnAndStatus(null, gameState.getNextTurn(), PageStatus.MODULE));
                Collections.sort(selectList, new MessageObject.NumberVersionComparator(SortType.ASCENDING));
            } else if (EntryFieldType.SELECT_NAMED_BEAN.equals(getEntryFieldType())) {
                selectList = new LinkedList(getBeanSelectList());
            }
        }
        return selectList;
    }

    /**
     * Comparator for sorting first by order, then by name.
     */
    public static class OrderNameComparator implements Comparator {

        public int compare(Object o1, Object o2) {
            int comparison = 0;
            ElementType elementType1 = (ElementType) o1;
            ElementType elementType2 = (ElementType) o2;
            if ((elementType1 != null) && (elementType1.getOrderLevel() != null) && (elementType2 != null) && (elementType2.getOrderLevel() != null)) {
                comparison = elementType1.getOrderLevel().compareTo(elementType2.getOrderLevel());
                if ((comparison == 0) && (elementType1.getName() != null) && (elementType2.getName() != null)) {
                    comparison = elementType1.getName().compareTo(elementType2.getName());
                }
            }
            return comparison;
        }
    }
}
