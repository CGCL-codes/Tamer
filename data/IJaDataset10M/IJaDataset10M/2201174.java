package org.apache.isis.extensions.jpa.metamodel.specloader.classsubstitutor;

public class JpaClassSubstitutorCglib extends JpaClassSubstitutorAbstract {

    public JpaClassSubstitutorCglib() {
        ignore(org.hibernate.repackage.cglib.proxy.Factory.class);
        ignore(org.hibernate.repackage.cglib.proxy.MethodProxy.class);
        ignore(org.hibernate.repackage.cglib.proxy.Callback.class);
    }
}
