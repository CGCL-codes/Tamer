package com.google.appengine.datanucleus.test;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Sequence;
import javax.jdo.annotations.SequenceStrategy;

/**
 * @author Max Ross <maxr@google.com>
 */
public final class SequenceExamplesJDO {

    private SequenceExamplesJDO() {
    }

    @PersistenceCapable(identityType = IdentityType.APPLICATION)
    public static class HasSequence {

        @PrimaryKey
        @Persistent(valueStrategy = IdGeneratorStrategy.SEQUENCE)
        private Long id;

        private String val;

        public Long getId() {
            return id;
        }

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }
    }

    @PersistenceCapable(identityType = IdentityType.APPLICATION)
    @Sequence(name = "jdo1", datastoreSequence = "jdothat", strategy = SequenceStrategy.NONTRANSACTIONAL, extensions = @Extension(vendorName = "datanucleus", key = "key-cache-size", value = "12"))
    public static class HasSequenceWithSequenceGenerator {

        @PrimaryKey
        @Persistent(valueStrategy = IdGeneratorStrategy.SEQUENCE, sequence = "jdo1")
        private Long id;

        private String val;

        public Long getId() {
            return id;
        }

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }
    }

    @PersistenceCapable(identityType = IdentityType.APPLICATION)
    @Sequence(name = "jdo2", strategy = SequenceStrategy.NONTRANSACTIONAL, extensions = @Extension(vendorName = "datanucleus", key = "key-cache-size", value = "12"))
    public static class HasSequenceWithNoSequenceName {

        @PrimaryKey
        @Persistent(valueStrategy = IdGeneratorStrategy.SEQUENCE, sequence = "jdo2")
        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

    @PersistenceCapable(identityType = IdentityType.APPLICATION)
    public static class HasSequenceWithUnencodedStringPk {

        @PrimaryKey
        @Persistent(valueStrategy = IdGeneratorStrategy.SEQUENCE)
        private String id;

        public String getId() {
            return id;
        }
    }

    @PersistenceCapable(identityType = IdentityType.APPLICATION)
    public static class HasSequenceOnNonPkFields {

        @PrimaryKey
        private String id;

        @Persistent(valueStrategy = IdGeneratorStrategy.SEQUENCE)
        private long val1;

        @Persistent(valueStrategy = IdGeneratorStrategy.SEQUENCE)
        private long val2;

        public void setId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public long getVal1() {
            return val1;
        }

        public long getVal2() {
            return val2;
        }
    }
}
