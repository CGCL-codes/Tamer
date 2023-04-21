package com.example;

import java.util.Collection;
import com.beanview.BeanView;
import com.beanview.annotation.PropertyOptions;

/**
 * @author $Author: wiverson $
 * @version $Revision: 1.1.1.1 $, $Date: 2006/09/19 04:21:44 $
 */
public class PeoplePicker {

    Collection<SimpleObject> allPeople;

    Collection<SimpleObject> favoriteLastNameMPeople;

    Collection<SimpleObject> peopleByContext;

    Collection<SimpleObject> peopleByObjectMethod;

    Collection<SimpleObject> peopleLetterMByObjectMethod;

    Collection<SimpleObject> peopleByObjectMethodWithContext;

    /** Testing retrieval by static factory method with no context */
    @PropertyOptions(options = "SimpleObjectFactory.getLastNameStartsWithM")
    public Collection<SimpleObject> getFavoriteLastNameMPeople() {
        return favoriteLastNameMPeople;
    }

    public void setFavoriteLastNameMPeople(Collection<SimpleObject> favoriteLastNameMPeople) {
        this.favoriteLastNameMPeople = favoriteLastNameMPeople;
    }

    /** Testing retrieval by static factory method with no context */
    @PropertyOptions(options = "SimpleObjectFactory.getPotentialObjects")
    public Collection<SimpleObject> getAllPeople() {
        return allPeople;
    }

    public void setAllPeople(Collection<SimpleObject> allPeople) {
        this.allPeople = allPeople;
    }

    /** Testing retrieval by static factory method with a context */
    @PropertyOptions(options = "SimpleObjectFactory.getFromUserIDContext")
    public Collection<SimpleObject> getPeopleByContext() {
        return peopleByContext;
    }

    public void setPeopleByContext(Collection<SimpleObject> peopleByContext) {
        this.peopleByContext = peopleByContext;
    }

    /** Testing retrieval by a local object method without a context */
    @PropertyOptions(options = "findAllPeople")
    public Collection<SimpleObject> getPeopleByObjectMethod() {
        return peopleByObjectMethod;
    }

    public void setPeopleByObjectMethod(Collection<SimpleObject> peopleByObjectMethod) {
        this.peopleByObjectMethod = peopleByObjectMethod;
    }

    public Collection<SimpleObject> findAllPeople() {
        return SimpleObjectFactory.getPotentialObjects();
    }

    public Collection<SimpleObject> findAllLetterMPeople() {
        return SimpleObjectFactory.getLastNameStartsWithM();
    }

    public Collection<SimpleObject> findPeopleByContext(BeanView context) {
        return SimpleObjectFactory.getFromUserIDContext(context);
    }

    /** Testing retrieval by a local object method without a context */
    @PropertyOptions(options = "findPeopleByContext")
    public Collection<SimpleObject> getPeopleByObjectMethodWithContext() {
        return peopleByObjectMethodWithContext;
    }

    public void setPeopleByObjectMethodWithContext(Collection<SimpleObject> peopleByObjectMethodWithContext) {
        this.peopleByObjectMethodWithContext = peopleByObjectMethodWithContext;
    }

    /** Testing retrieval by a local object method without a context */
    @PropertyOptions(options = "findAllLetterMPeople")
    public Collection<SimpleObject> getPeopleLetterMByObjectMethod() {
        return peopleLetterMByObjectMethod;
    }

    public void setPeopleLetterMByObjectMethod(Collection<SimpleObject> peopleLetterMByObjectMethod) {
        this.peopleLetterMByObjectMethod = peopleLetterMByObjectMethod;
    }
}
