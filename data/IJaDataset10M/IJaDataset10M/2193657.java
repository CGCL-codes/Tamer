package org.nakedobjects.persistence.nhibernate.type;

import NHibernate.IUserType;
import NUnit.Framework.*;

public abstract class ITypesTestCase {

    public static String[] names = { "name1" };

    public void basicTest(UserType type) {
        String test = "a";
        Assert.IsTrue(test == type.DeepCopy(test), "deep copy ==");
        Assert.IsTrue(test == type.assemble(test, null), "assemble");
        Assert.IsTrue(test == type.disassemble(test), "disassemble");
        Assert.IsTrue(test == type.replace(test, null, null), "replace");
        Assert.IsTrue(!type.get_IsMutable(), "!get_IsMutable");
        Assert.IsTrue(type.Equals(test, test), "Equals");
        Assert.IsTrue(!type.Equals(test, "b"), "!Equals");
        Assert.AreEqual(test.hashCode(), type.hashCode(test), "hash");
    }
}
