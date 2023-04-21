package org.hibernate.search.test.id.providedId;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.ProvidedId;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.bridge.builtin.LongBridge;
import java.io.Serializable;

/**
 * @author Navin Surtani
 */
@ProvidedId(bridge = @FieldBridge(impl = LongBridge.class))
@Indexed
public class ProvidedIdPerson implements Serializable {

    private long id;

    @Field(index = Index.TOKENIZED, store = Store.YES)
    private String name;

    @Field(index = Index.TOKENIZED, store = Store.YES)
    private String blurb;

    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBlurb() {
        return blurb;
    }

    public void setBlurb(String blurb) {
        this.blurb = blurb;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
