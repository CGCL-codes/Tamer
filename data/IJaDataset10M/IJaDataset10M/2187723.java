package com.google.appengine.datanucleus.test;

import com.google.appengine.datanucleus.test.HasPolymorphicRelationsJDO.HasOneToManyJDO;
import com.google.appengine.datanucleus.test.HasPolymorphicRelationsJDO.HasOneToManyLongPkJDO;
import com.google.appengine.datanucleus.test.HasPolymorphicRelationsJDO.HasOneToManyUnencodedStringPkJDO;
import com.google.appengine.datanucleus.test.HasPolymorphicRelationsSetJDO.HasOneToManyLongPkSet;
import com.google.appengine.datanucleus.test.HasPolymorphicRelationsSetJDO.HasOneToManySet;
import com.google.appengine.datanucleus.test.HasPolymorphicRelationsSetJDO.HasOneToManyUnencodedStringPkSet;
import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.DiscriminatorStrategy;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

public class BidirectionalSuperclassTableChildSetJDO {

    @PersistenceCapable(detachable = "true")
    @Discriminator(column = "DISCRIMINATOR")
    public static class BidirTop implements BidirectionalSuperclassTableChildJDO.BidirTop {

        @PrimaryKey
        @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
        @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
        private String id;

        private String childVal;

        @Persistent
        private HasOneToManySet parent;

        public void setChildVal(String childVal) {
            this.childVal = childVal;
        }

        public String getChildVal() {
            return childVal;
        }

        public void setParent(HasOneToManyJDO parent) {
            this.parent = (HasOneToManySet) parent;
        }

        public HasOneToManyJDO getParent() {
            return this.parent;
        }

        public String getId() {
            return id;
        }

        public int getPropertyCount() {
            return 2;
        }
    }

    @PersistenceCapable(detachable = "true")
    public static class BidirMiddle extends BidirTop implements BidirectionalSuperclassTableChildJDO.BidirMiddle {

        private Long middleChildVal;

        public Long getMiddleChildVal() {
            return middleChildVal;
        }

        public void setMiddleChildVal(Long middleChildVal) {
            this.middleChildVal = middleChildVal;
        }

        public int getPropertyCount() {
            return 3;
        }
    }

    @PersistenceCapable(detachable = "true")
    public static class BidirBottom extends BidirMiddle implements BidirectionalSuperclassTableChildJDO.BidirBottom {

        private Double bottomChildVal;

        public Double getBottomChildVal() {
            return bottomChildVal;
        }

        public void setBottomChildVal(Double bottomChildVal) {
            this.bottomChildVal = bottomChildVal;
        }

        public int getPropertyCount() {
            return 4;
        }
    }

    @PersistenceCapable(detachable = "true")
    @Discriminator(column = "DISCRIMINATOR")
    public static class BidirTopLongPk implements BidirectionalSuperclassTableChildJDO.BidirTopLongPk {

        @PrimaryKey
        @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
        @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
        private String id;

        private String childVal;

        @Persistent
        private HasOneToManyLongPkSet parent;

        public void setChildVal(String childVal) {
            this.childVal = childVal;
        }

        public String getChildVal() {
            return childVal;
        }

        public void setParent(HasOneToManyLongPkJDO parent) {
            this.parent = (HasOneToManyLongPkSet) parent;
        }

        public HasOneToManyLongPkJDO getParent() {
            return this.parent;
        }

        public String getId() {
            return id;
        }
    }

    @PersistenceCapable(detachable = "true")
    public static class BidirMiddleLongPk extends BidirTopLongPk implements BidirectionalSuperclassTableChildJDO.BidirMiddleLongPk {
    }

    @PersistenceCapable(detachable = "true")
    public static class BidirBottomLongPk extends BidirMiddleLongPk implements BidirectionalSuperclassTableChildJDO.BidirBottomLongPk {
    }

    @PersistenceCapable(detachable = "true")
    @Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
    @Discriminator(strategy = DiscriminatorStrategy.VALUE_MAP, value = "T")
    public static class BidirTopUnencodedStringPkJDO implements BidirectionalSuperclassTableChildJDO.BidirTopUnencodedStringPkJDO {

        @PrimaryKey
        @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
        @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
        private String id;

        private String childVal;

        @Persistent
        private HasOneToManyUnencodedStringPkSet parent;

        public void setChildVal(String childVal) {
            this.childVal = childVal;
        }

        public String getChildVal() {
            return childVal;
        }

        public void setParent(HasOneToManyUnencodedStringPkJDO parent) {
            this.parent = (HasOneToManyUnencodedStringPkSet) parent;
        }

        public HasOneToManyUnencodedStringPkJDO getParent() {
            return this.parent;
        }

        public String getId() {
            return id;
        }
    }

    @PersistenceCapable(detachable = "true")
    @Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
    @Discriminator(value = "M")
    public static class BidirMiddleUnencodedStringPk extends BidirTopUnencodedStringPkJDO implements BidirectionalSuperclassTableChildJDO.BidirMiddleUnencodedStringPkJDO {
    }

    @PersistenceCapable(detachable = "true")
    @Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
    @Discriminator(value = "B")
    public static class BidirBottomUnencodedStringPk extends BidirMiddleUnencodedStringPk implements BidirectionalSuperclassTableChildJDO.BidirBottomUnencodedStringPkJDO {
    }
}
