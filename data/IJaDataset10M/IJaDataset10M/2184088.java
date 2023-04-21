package com.google.appengine.datanucleus.test;

import com.google.appengine.datanucleus.Utils;
import java.util.List;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * @author Max Ross <maxr@google.com>
 */
@PersistenceCapable(detachable = "true")
public class HasChildWithSeparateNameFieldJDO {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    List<HasEncodedStringPkSeparateNameFieldJDO> children = Utils.newArrayList();

    public Long getId() {
        return id;
    }

    public List<HasEncodedStringPkSeparateNameFieldJDO> getChildren() {
        return children;
    }
}
