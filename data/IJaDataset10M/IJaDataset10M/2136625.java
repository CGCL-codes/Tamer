package com.namazustudios.catfish.gae;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import junit.framework.Assert;
import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.namazustudios.catfish.Catfish;
import com.namazustudios.catfish.CatfishModule;
import com.namazustudios.catfish.annotation.Cacheable;
import com.namazustudios.catfish.annotation.Child;
import com.namazustudios.catfish.annotation.Delete;
import com.namazustudios.catfish.annotation.Entity;
import com.namazustudios.catfish.annotation.EntityKey;
import com.namazustudios.catfish.annotation.Flat;
import com.namazustudios.catfish.annotation.Get;
import com.namazustudios.catfish.annotation.PostPut;
import com.namazustudios.catfish.annotation.PrePut;
import com.namazustudios.catfish.annotation.Property;
import com.namazustudios.catfish.reference.WeakReference;

enum DummyEnum {

    FOO, BAR, BAZ
}

@Entity
@Cacheable
class ChildEntity implements Serializable {

    private static final long serialVersionUID = 82069600704165541L;

    @EntityKey
    public Key key;

    @Property
    public String string = "Child Entity" + new Random().nextInt();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ChildEntity other = (ChildEntity) obj;
        if (key == null) {
            if (other.key != null) return false;
        } else if (!key.equals(other.key)) return false;
        if (string == null) {
            if (other.string != null) return false;
        } else if (!string.equals(other.string)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((string == null) ? 0 : string.hashCode());
        return result;
    }
}

class FlatProperty implements Serializable {

    private static final long serialVersionUID = 3312349276152196728L;

    @Property
    public Email email = new Email("test" + new Random().nextInt() + "@example.com");

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        FlatProperty other = (FlatProperty) obj;
        if (email == null) {
            if (other.email != null) return false;
        } else if (!email.equals(other.email)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        return result;
    }
}

@Entity
class Referenced implements Serializable {

    private static final long serialVersionUID = 1132513662650161822L;

    @EntityKey
    public Key key = KeyFactory.createKey("Referenced", UUID.randomUUID().toString());

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Referenced other = (Referenced) obj;
        if (key == null) {
            if (other.key != null) return false;
        } else if (!key.equals(other.key)) return false;
        return true;
    }
}

@Entity
@Cacheable
class RootEntity implements Serializable {

    private static final long serialVersionUID = 5170905466175827270L;

    @EntityKey
    public Key key;

    @Property
    public short sprimitive = (short) new Random().nextInt(Short.MAX_VALUE);

    @Property
    public Short sobject = (short) new Random().nextInt(Short.MAX_VALUE);

    @Property
    public int iprimitive = new Random().nextInt();

    @Property
    public Integer iobject = new Random().nextInt();

    @Property
    public boolean bprimitive = new Random().nextBoolean();

    @Property
    public Boolean bobject = new Random().nextBoolean();

    @Property
    public String string = "Random String" + new Random().nextInt();

    @Flat
    public FlatProperty flat = new FlatProperty();

    @Child
    public ChildEntity child0 = new ChildEntity();

    @Child
    public ChildEntity child1 = new ChildEntity();

    @Child
    public ChildEntity childNull = null;

    @Property(converter = "enum")
    public DummyEnum enumNull;

    @Property(converter = "enum", provider = "enum")
    public DummyEnum enumValue = DummyEnum.BAR;

    @Property(converter = "enum")
    public List<DummyEnum> enumList = new ArrayList<DummyEnum>(Arrays.asList(DummyEnum.FOO, DummyEnum.BAR, DummyEnum.BAZ));

    @Property
    public List<Long> integers = new ArrayList<Long>(Arrays.asList(42l, 42l, 42l));

    @Child
    public List<ChildEntity> children = new ArrayList<ChildEntity>(Arrays.asList(new ChildEntity(), new ChildEntity()));

    @Property(converter = "weak")
    public final WeakReference<Referenced> ref = new WeakReference<Referenced>(Referenced.class);

    public RootEntity sort() {
        final Comparator<Object> c = new Comparator<Object>() {

            @Override
            public int compare(Object o1, Object o2) {
                int lhs, rhs;
                lhs = o1 == null ? 0 : o1.hashCode();
                rhs = o2 == null ? 0 : o2.hashCode();
                return lhs - rhs;
            }
        };
        Collections.sort(integers);
        Collections.sort(children, c);
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bobject == null) ? 0 : bobject.hashCode());
        result = prime * result + (bprimitive ? 1231 : 1237);
        result = prime * result + ((child0 == null) ? 0 : child0.hashCode());
        result = prime * result + ((child1 == null) ? 0 : child1.hashCode());
        result = prime * result + ((childNull == null) ? 0 : childNull.hashCode());
        result = prime * result + ((children == null) ? 0 : children.hashCode());
        result = prime * result + ((enumNull == null) ? 0 : enumNull.hashCode());
        result = prime * result + ((enumValue == null) ? 0 : enumValue.hashCode());
        result = prime * result + ((flat == null) ? 0 : flat.hashCode());
        result = prime * result + ((integers == null) ? 0 : integers.hashCode());
        result = prime * result + ((iobject == null) ? 0 : iobject.hashCode());
        result = prime * result + iprimitive;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((ref == null) ? 0 : ref.hashCode());
        result = prime * result + ((sobject == null) ? 0 : sobject.hashCode());
        result = prime * result + sprimitive;
        result = prime * result + ((string == null) ? 0 : string.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        RootEntity other = (RootEntity) obj;
        if (bobject == null) {
            if (other.bobject != null) return false;
        } else if (!bobject.equals(other.bobject)) return false;
        if (bprimitive != other.bprimitive) return false;
        if (child0 == null) {
            if (other.child0 != null) return false;
        } else if (!child0.equals(other.child0)) return false;
        if (child1 == null) {
            if (other.child1 != null) return false;
        } else if (!child1.equals(other.child1)) return false;
        if (childNull == null) {
            if (other.childNull != null) return false;
        } else if (!childNull.equals(other.childNull)) return false;
        if (children == null) {
            if (other.children != null) return false;
        } else if (!children.equals(other.children)) return false;
        if (enumNull == null) {
            if (other.enumNull != null) return false;
        } else if (!enumNull.equals(other.enumNull)) return false;
        if (enumValue == null) {
            if (other.enumValue != null) return false;
        } else if (!enumValue.equals(other.enumValue)) return false;
        if (flat == null) {
            if (other.flat != null) return false;
        } else if (!flat.equals(other.flat)) return false;
        if (integers == null) {
            if (other.integers != null) return false;
        } else if (!integers.equals(other.integers)) return false;
        if (iobject == null) {
            if (other.iobject != null) return false;
        } else if (!iobject.equals(other.iobject)) return false;
        if (iprimitive != other.iprimitive) return false;
        if (key == null) {
            if (other.key != null) return false;
        } else if (!key.equals(other.key)) return false;
        if (ref == null) {
            if (other.ref != null) return false;
        } else if (!ref.equals(other.ref)) return false;
        if (sobject == null) {
            if (other.sobject != null) return false;
        } else if (!sobject.equals(other.sobject)) return false;
        if (sprimitive != other.sprimitive) return false;
        if (string == null) {
            if (other.string != null) return false;
        } else if (!string.equals(other.string)) return false;
        return true;
    }

    public boolean get;

    @Get
    @SuppressWarnings("unused")
    private void get() {
        get = true;
    }

    public boolean prePut;

    @PrePut
    @SuppressWarnings("unused")
    private void prePut() {
        prePut = true;
        Assert.assertTrue(!postPut || !postPutC || !postPutCE);
    }

    public boolean postPut;

    @PostPut
    @SuppressWarnings("unused")
    private void postPut() {
        postPut = true;
        Assert.assertTrue(prePut && prePutC && prePutCE);
    }

    public boolean delete;

    @Delete
    @SuppressWarnings("unused")
    private void delete() {
        delete = true;
    }

    public boolean getC;

    @Get
    @SuppressWarnings("unused")
    private void get(Catfish catfish) {
        getC = true;
        Assert.assertNotNull(catfish);
    }

    public boolean prePutC;

    @PrePut
    @SuppressWarnings("unused")
    private void prePut(Catfish catfish) {
        prePutC = true;
        Assert.assertNotNull(catfish);
        Assert.assertTrue(!postPut || !postPutC || !postPutCE);
    }

    public boolean postPutC;

    @PostPut
    @SuppressWarnings("unused")
    private void postPut(Catfish catfish) {
        postPutC = true;
        Assert.assertNotNull(catfish);
        Assert.assertTrue(prePut && prePutC && prePutCE);
    }

    public boolean deleteC;

    @Delete
    @SuppressWarnings("unused")
    private void delete(Catfish catfish) {
        deleteC = true;
        Assert.assertNotNull(catfish);
    }

    public boolean getCE;

    @Get
    @SuppressWarnings("unused")
    private void get(Catfish catfish, com.google.appengine.api.datastore.Entity entity) {
        getCE = true;
        Assert.assertNotNull(entity);
        Assert.assertNotNull(catfish);
    }

    public boolean prePutCE;

    @PrePut
    @SuppressWarnings("unused")
    private void prePut(Catfish catfish, com.google.appengine.api.datastore.Entity entity) {
        prePutCE = true;
        Assert.assertNotNull(entity);
        Assert.assertNotNull(catfish);
        Assert.assertTrue(!postPut || !postPutC || !postPutCE);
    }

    public boolean postPutCE;

    @PostPut
    @SuppressWarnings("unused")
    private void postPut(Catfish catfish, com.google.appengine.api.datastore.Entity entity) {
        postPutCE = true;
        Assert.assertNotNull(entity);
        Assert.assertNotNull(catfish);
        Assert.assertTrue(prePut && prePutC && prePutCE);
    }

    @PrePut
    @SuppressWarnings("unused")
    private void prePut(com.google.appengine.api.datastore.Entity entity, Catfish catfish) {
        Assert.assertNotNull(entity);
        Assert.assertNotNull(catfish);
        Assert.assertTrue(!postPut || !postPutC || !postPutCE);
    }

    @Get
    @PrePut
    @PostPut
    @Delete
    @SuppressWarnings("unused")
    private void shouldNeverBeCalled(Integer integer) {
        Assert.fail();
    }

    public boolean wereCallBacksCalled() {
        return get && prePut && postPut && delete && getC && prePutC && postPutC && deleteC && getCE && prePutCE && postPutCE;
    }
}

class Module extends CatfishModule {

    @Override
    protected void configure() {
        bind(RootEntity.class);
        bind(ChildEntity.class);
        super.configure();
    }
}
