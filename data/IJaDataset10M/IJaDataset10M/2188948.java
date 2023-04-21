package org.nakedobjects.nos.store.hibernate.testobjects;

import java.util.List;

public class BiDirectional {

    public static String inverseOneToMany = "Many";

    private List manyToMany;

    private OneToMany oneToMany;

    private OneToMany secondOneToMany;

    private OneToOne oneToOne;

    public static String fieldOrder() {
        return "oneToOne, oneToMany, manyToMany, secondOneToMany";
    }

    public void addToManyToMany(ManyToMany other) {
        getManyToMany().add(other);
        other.getMany().add(this);
    }

    public void removeFromManyToMany(ManyToMany other) {
        getManyToMany().add(other);
        other.getMany().add(this);
    }

    public List getManyToMany() {
        return manyToMany;
    }

    public void setManyToMany(List manyToMany) {
        this.manyToMany = manyToMany;
    }

    public void modifyOneToMany(final OneToMany oneToMany) {
        setOneToMany(oneToMany);
        oneToMany.getMany().add(this);
    }

    public void clearOneToMany(final OneToMany oneToMany) {
        setOneToMany(null);
        oneToMany.getMany().remove(this);
    }

    public OneToMany getOneToMany() {
        return oneToMany;
    }

    public void setOneToMany(OneToMany oneToMany) {
        this.oneToMany = oneToMany;
    }

    public void modifySecondOneToMany(final OneToMany oneToMany) {
        setSecondOneToMany(oneToMany);
    }

    public void clearSecondOneToMany(final OneToMany oneToMany) {
        setSecondOneToMany(null);
    }

    public OneToMany getSecondOneToMany() {
        return secondOneToMany;
    }

    public void setSecondOneToMany(OneToMany oneToMany) {
        this.secondOneToMany = oneToMany;
    }

    public void modifyOneToOne(final OneToOne oneToOne) {
        setOneToOne(oneToOne);
        oneToOne.setOne(this);
    }

    public void clearOneToOne(final OneToOne oneToOne) {
        setOneToOne(null);
        oneToOne.setOne(null);
    }

    public OneToOne getOneToOne() {
        return oneToOne;
    }

    public void setOneToOne(OneToOne oneToOne) {
        this.oneToOne = oneToOne;
    }
}
