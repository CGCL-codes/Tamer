package com.google.appengine.datanucleus.test;

import com.google.appengine.api.datastore.Key;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

/**
 * @author Max Ross <max.ross@gmail.com>
 */
public class UnidirectionalOneToManySubclassesJPA {

    @Entity
    @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
    public static class SuperParentWithSuperChild {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String superParentString;

        @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        @OrderBy(value = "aString desc")
        private List<SuperChild> superChildren = new ArrayList<SuperChild>();

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getSuperParentString() {
            return superParentString;
        }

        public void setSuperParentString(String superParentString) {
            this.superParentString = superParentString;
        }

        public List<SuperChild> getSuperParentSuperChildren() {
            return superChildren;
        }

        public void setSuperParentSuperChildren(List<SuperChild> superChildren) {
            this.superChildren = superChildren;
        }
    }

    @Entity
    @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
    public static class SuperParentWithSubChild {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String superParentString;

        @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        @OrderBy(value = "aString desc")
        private List<SubChild> subChildren = new ArrayList<SubChild>();

        public Long getId() {
            return id;
        }

        public String getSuperParentString() {
            return superParentString;
        }

        public void setSuperParentString(String superParentString) {
            this.superParentString = superParentString;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public List<SubChild> getSuperParentSubChildren() {
            return subChildren;
        }

        public void setSuperParentSubChildren(List<SubChild> subChildren) {
            this.subChildren = subChildren;
        }
    }

    @Entity
    public static class SubParentWithSuperChild extends SuperParentWithSuperChild {

        private String subParentString;

        public String getSubParentString() {
            return subParentString;
        }

        public void setSubParentString(String subParentString) {
            this.subParentString = subParentString;
        }
    }

    @Entity
    public static class SubParentWithSubChild extends SuperParentWithSubChild {

        private String subParentString;

        public String getSubParentString() {
            return subParentString;
        }

        public void setSubParentString(String subParentString) {
            this.subParentString = subParentString;
        }
    }

    @Entity
    @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
    public static class SuperChild {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Key id;

        private String aString;

        public Key getId() {
            return id;
        }

        public void setId(Key id) {
            this.id = id;
        }

        public String getAString() {
            return aString;
        }

        public void setAString(String aString) {
            this.aString = aString;
        }
    }

    @Entity
    public static class SubChild extends SuperChild {

        private String bString;

        public String getBString() {
            return bString;
        }

        public void setBString(String bString) {
            this.bString = bString;
        }
    }
}
